package com.etfease.datasource;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ETF {

	@PrimaryKey  
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    transient private Key key; 
    @Persistent
	private String name;  
    @Persistent
	private String symbol;
    @Persistent
	private ETFType etfType;
    @Persistent
	private float[] changeHistory;
    @Persistent
	private float[] priceHistory;
    @Persistent
	private String[] priceDate;
    @NotPersistent
    private float[] sma200History;
    @NotPersistent
	private float[] sma5History;
    @NotPersistent
    private float rsiHistory[];
    @NotPersistent
    private float rsi4History[];
    @NotPersistent
 	private List <Trades> tradesList = new ArrayList <Trades> ();
    @NotPersistent   
    private boolean invalid = false;
	
	static final String quotesUrl="http://finance.yahoo.com/d/quotes.csv?s=<symbolslist>&f=<taglist>";
	static final String taglist = "k1c6d1";
	static final String history = "http://ichart.finance.yahoo.com/table.csv?s=<symbolslist>&a=<startmonth>&b=<startday>&c=<startyear>&d=<endmonth>&e=<endday>&f=<endyear>&g=d";
	static final int MAX_DAYS=365*2;


	public ETF loadFromDB(){
		ETF etfFromDB = null; 
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    Query query = pm.newQuery(ETF.class);
	    query.setFilter("symbol == etfSymbol");
	    query.declareParameters("String etfSymbol");

	    try {
	        List<ETF> results = (List<ETF>) query.execute(this.symbol);
	        Iterator <ETF> etfs = results.iterator();
	        if (etfs.hasNext()) {
	        	etfFromDB = etfs.next();
	        } 
	    } finally {
	        query.closeAll();
	    }
	    return etfFromDB;
	}
	
	public void syncETF() throws Exception {

		ETF etfFromDB = null; 
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    Query query = pm.newQuery(ETF.class);
	    query.setFilter("symbol == etfSymbol");
	    query.declareParameters("String etfSymbol");

	    try {
	        List<ETF> results = (List<ETF>) query.execute(this.symbol);
	        Iterator <ETF> etfs = results.iterator();
	        if (etfs.hasNext()) {
	        	etfFromDB = etfs.next();  
	        } 
	    } finally {
	        query.closeAll();
	    }	    
		

		if(etfFromDB!=null){
			Date lastdate = new SimpleDateFormat("yyyy-MM-dd").parse(etfFromDB.getPriceDate()[0]);

			//how many days since last sample
			Calendar c = Calendar.getInstance();
			c.setTime(lastdate);
			long daysdiff = (Calendar.getInstance().getTimeInMillis()-c.getTimeInMillis())/(24 * 60 * 60 * 1000);
			if (daysdiff>MAX_DAYS) daysdiff=MAX_DAYS;
			//populate from web for only the dates needed
			loadETFData((int)daysdiff, etfFromDB);
			this.key = etfFromDB.key;
		} else {
			loadETFData(MAX_DAYS, null);
		}
		
		
		if (this.invalid==false) calculateTrades();
		


	}


	public void loadETFData(int days, ETF etfdb) throws Exception {

		changeHistory = new float[2000];
		priceHistory = new float[2000];
		priceDate = new String[2000];
		sma200History = new float[2000];
		sma5History = new float[2000];

		//This first data fetch gets the current day's last trade information. If the market is open, the last 
		//trade will be different then the first historical entry to follow. If the market is closed then the 
		//date of the last trade will (most likely) match the first historical entry. If that is the case 
		//the first historical entry will be skipped.

		InputStream is = null;
		DataInputStream dis;
		//information can be found at -- http://www.gummy-stuff.org/Yahoo-data.htm

		String qurl = quotesUrl.replace("<taglist>", taglist.trim()).replace("<symbolslist>", symbol.trim());			
		URL url = new URL(qurl);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
		String quote;
		float priceRt=0;
		float changeRt=0;
		String datestr="12/18/2009";
		if ((quote = in.readLine()) != null ){
			//System.out.println(quote);
			//"N/A - <b>3264.00</b>","+13.71","12/18/2009"
			String[] s = quote.split(",");
			int pos1 = s[0].indexOf("<b>")+3;
			int pos2 = s[0].indexOf("</b>");
			String prt = s[0].substring(pos1, pos2);
			priceRt = Float.parseFloat(prt);
			changeRt = Float.parseFloat(s[1].replaceAll("\"", "")); 
			datestr=s[2];
		}		

		this.changeHistory[0]=changeRt;
		this.priceHistory[0]=priceRt;
		DateFormat mmddyyyy = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
		Date mydate = (Date)mmddyyyy.parse(datestr.replace("\"", ""));
		this.priceDate[0]=yyyymmdd.format(mydate);


		//the
		Calendar c = new GregorianCalendar();
		int endDay = c.get(Calendar.DAY_OF_MONTH);
		int endMonth = c.get(Calendar.MONTH);
		int endYear = c.get(Calendar.YEAR);
		c.add(Calendar.DAY_OF_MONTH, -days);
		int startDay = c.get(Calendar.DAY_OF_MONTH);
		int startMonth = c.get(Calendar.MONTH);
		int startYear = c.get(Calendar.YEAR);
		//information can be found at -- http://www.diytraders.com/content/view/25/43/
		String shistory=history.replace("<startmonth>", ""+startMonth).replace("<startday>", ""+startDay).replace("<startyear>", ""+startYear).replace("<endmonth>", ""+endMonth).replace("<endday>", ""+endDay).replace("<endyear>", ""+endYear).replace("<symbolslist>", symbol.trim());

		//take the most recent entries for historical calculations
		url = new URL(shistory);
		in=null;
		try {
			in = new BufferedReader(new InputStreamReader(url.openStream()));
		}catch (java.io.FileNotFoundException fnf){
			System.out.println(fnf.getMessage());
		}

		if(in!=null){
			String line;
			int i=1;
			//read and throw out header line
			in.readLine();

			while((line = in.readLine()) != null ){

				//Date,Open,High,Low,Close,Volume,Adj Close
				//2009-07-31,18.00,18.39,17.92,18.05,46500,18.05
				String[] s = line.split(",");

				//for the first date, check of the historical date matches the last trade date. If
				//they are the same, skip this entry.
				if(i==1){
					if (s[0].equals(this.priceDate[0])){
						continue; 
					}
				}
				float open = Float.parseFloat(s[1]);
				float close = i==0?priceRt:Float.parseFloat(s[6]);
				float change = i==0?0:-close+this.priceHistory[i-1];

				//change history
				this.changeHistory[i]=change;
				//price history
				this.priceHistory[i]=close;
				this.priceDate[i]=s[0];
				i++;
				days = i;

			}
		}		
		
		//test if yahoo! historical data is current (within the last 5 days)
		if(calculateDaysFromToday(this.priceDate[1])>5) {
			this.invalid=true;
			return;
		}
		
		
		//compress arrays
		if(days==0)days=1;
		float tmparray[] = new float[days];
		String stmparray[] = new String[days];
		System.arraycopy(changeHistory, 0, tmparray, 0, days);
		changeHistory = tmparray;
		tmparray = new float[days];
		System.arraycopy(priceHistory, 0, tmparray, 0, days);
		priceHistory = tmparray;
		System.arraycopy(priceDate, 0, stmparray, 0, days);
		priceDate = stmparray;

		if(etfdb!=null){
			if (!etfdb.getPriceDate()[0].equals(priceDate[0])){

				//compress new data to remove duplicates
				compress(priceDate, etfdb.getPriceDate());
				compress(priceHistory,priceDate.length);
				compress(changeHistory,priceDate.length);
				
				//Note: merge arrays with stored arrays with incoming data if it is not null
				//and the last stored date does not match new data date
				changeHistory = concat(changeHistory, etfdb.getChangeHistory());
				priceHistory = concat(priceHistory, etfdb.getPriceHistory());
				priceDate = concat(priceDate, etfdb.getPriceDate());
				
				
			} else {
				changeHistory = etfdb.getChangeHistory();
				priceHistory = etfdb.getPriceHistory();
				priceDate = etfdb.getPriceDate();
			}

		} 
		///calculate RSI and moving averages
		calculateHistories();

	}

	private void calculateHistories(){
		//calculate and set 2 period RSI
		this.rsiHistory = new RSI(2,this.changeHistory, this.priceHistory, this.priceDate).getRSIHistory();
		//calculate and set 4 period RSI
		this.rsi4History = new RSI(4,this.changeHistory, this.priceHistory, this.priceDate).getRSIHistory();
		//calculate 5 day simple moving averages
		this.sma200History = new SMA(200,priceHistory,priceDate).getSMAHistory();
		//calculate 5 day simple moving averages
		this.sma5History = new SMA(5,priceHistory,priceDate).getSMAHistory();
	}
	
	
	private void calculateTrades(){
		for (ETFFilterType e : ETFFilterType.values()){
			ETFFilter etfFilter = e.getETFFilter(e, this);
			TradeCalculator tc = new TradeCalculator(etfFilter,10000f,7f);
			Trades t = tc.calculate();
			try{
				t.writeToMemory();
			} catch (Throwable throwable){throwable.printStackTrace();}
			this.addTrades(t);
		}		
	}

	public void refreshTrades(boolean size) {
		this.tradesList= new ArrayList <Trades> ();
		for (ETFFilterType e : ETFFilterType.values()){
			Trades t = new Trades();
			t.setSymbol(symbol);
			t.setEtfFilterType(e);
			try{
				t.restoreFromMemory(size);
			} catch (Throwable throwable){}
			this.addTrades(t);
		}
	}

	
	private long calculateDaysFromToday(String sdate) throws Exception {

		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(sdate);

		//how many days since last sample
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		long daysdiff = (Calendar.getInstance().getTimeInMillis()-c.getTimeInMillis())/(24 * 60 * 60 * 1000);
		return daysdiff;
	}
	
	
	
	
	/**
	 * @return the etfType 
	 */
	public ETFType getEtfType() {
		return etfType;
	}
	/**
	 * @param etfType the etfType to set
	 */
	public void setEtfType(ETFType etfType) {
		this.etfType = etfType;
	}
	/**
	 * @return the changeHistory
	 */
	public float[] getChangeHistory() {
		return changeHistory;
	}
	/**
	 * @param changeHistory the changeHistory to set
	 */
	public void setChangeHistory(float[] changeHistory) {
		this.changeHistory = changeHistory;
	}
	/**
	 * @return the rsi
	 */
	public float[] getRsiHistory() {
		return rsiHistory;
	}
	/**
	 * @return the priceHistory
	 */
	public float[] getPriceHistory() {
		return priceHistory;
	}
	/**
	 * @param priceHistory the priceHistory to set
	 */
	public void setPriceHistory(float[] priceHistory) {
		this.priceHistory = priceHistory;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name.replaceAll("[^\\p{ASCII}]", "");
	}
	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}



	/**
	 * @return the sma200History
	 */
	public float[] getSma200History() {
		return sma200History;
	}



	/**
	 * @return the sma5History
	 */
	public float[] getSma5History() {
		return sma5History;
	}



	/**
	 * @return the priceDate
	 */
	public String[] getPriceDate() {
		return priceDate;
	}

	public void minimize(){
		this.changeHistory=null;
		this.priceDate=null;
		this.priceHistory=null;
		this.sma200History=null;
		this.sma5History=null;
		this.rsiHistory=null;
		this.rsi4History=null;
	}


	public static final Filter<ETF> CUTOFF = 
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			return etf.getTrades(ETFFilterType.R3).getTotal()>11000;
		}
	};	

	public static final Filter<ETF> NON_US_ETF1 = 
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			boolean us = etf.getEtfType().name().equals(ETFType.GlobalEquityETF.name());
			char c = etf.getSymbol().charAt(0);
			int i = (int)c;
			boolean atof = (i>=65 && i<=70);
			return us && atof;
		}
	};	

	public static final Filter<ETF> NON_US_ETF2 = 
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			boolean us = etf.getEtfType().name().equals(ETFType.GlobalEquityETF.name());
			char c = etf.getSymbol().charAt(0);
			int i = (int)c;
			boolean atof = (i>=71 && i<=90);
			return us && atof;
		}
	};	

	public static final Filter<ETF> US_ETF =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			return etf.getEtfType().name().equals(ETFType.USEquityETF.name());
		}
	};	

	public static final Filter<ETF> US_ETF1 =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			boolean us = etf.getEtfType().name().equals(ETFType.USEquityETF.name());
			char c = etf.getSymbol().charAt(0);
			int i = (int)c;
			boolean atof = (i>=65 && i<=70);
			return us && atof;
		}
	};	

	public static final Filter<ETF> US_ETF2 =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			boolean us = etf.getEtfType().name().equals(ETFType.USEquityETF.name());
			char c = etf.getSymbol().charAt(0);
			int i = (int)c;
			boolean gtom = (i>=71 && i<=77);
			return us && gtom;
		}
	};	

	public static final Filter<ETF> US_ETF3 =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			boolean us = etf.getEtfType().name().equals(ETFType.USEquityETF.name());
			char c = etf.getSymbol().charAt(0);
			int i = (int)c;
			boolean ntot = (i>=78 && i<=81);
			return us && ntot;
		}
	};	

	public static final Filter<ETF> US_ETF4 =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			boolean us = etf.getEtfType().name().equals(ETFType.USEquityETF.name());
			char c = etf.getSymbol().charAt(0);
			int i = (int)c;
			boolean rtow = (i>=82 && i<=85);
			return us && rtow;
		}
	};	
	
	
	public static final Filter<ETF> US_ETF5 =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			boolean us = etf.getEtfType().name().equals(ETFType.USEquityETF.name());
			char c = etf.getSymbol().charAt(0);
			int i = (int)c;
			boolean xtoz = (i>=86 && i<=90);
			return us && xtoz;
		}
	};	
	
	
	public static final Filter<ETF> BOND_ETF =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			return etf.getEtfType().name().equals(ETFType.FixedIncomeETF.name());
		}
	};	

	public static final Filter<ETF> COMMODITY_ETF =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			return etf.getEtfType().name().equals(ETFType.CommodityBasedETF.name());
		}
	};



	public static float[] concat(float[] A, float[] B) {
		float[] C= new float[A.length+B.length];
		System.arraycopy(A, 0, C, 0, A.length);
		System.arraycopy(B, 0, C, A.length, B.length);

		return C;
	}

	public static String[] concat(String[] A, String[] B) {
		String[] C= new String[A.length+B.length];
		System.arraycopy(A, 0, C, 0, A.length);
		System.arraycopy(B, 0, C, A.length, B.length);

		return C;
	}

	
	public static String[] compress(String[] A, String[] B){
		//get the intersection set from each of the source sets. compress set A by size of the intersection
		Set s1 = new HashSet(Arrays.asList(A));
		Set s2 = new HashSet(Arrays.asList(B));

		Set intersect = new TreeSet(s1);
		intersect.retainAll(s2);
		
		//compress A by intersect.size() -- A should be the new data
		if (intersect.size()!=0&& intersect.size()>=A.length){
			String[] C= new String[A.length-intersect.size()];
			System.arraycopy(A, 0, C, 0, C.length);

			return C; //A compressed by size of intersection 
		}
		
		return A; //no compression
	}

	
	public static float[] compress(float[] A, int size){
		float[] B= new float[size];
		if(A.length>size){
			System.arraycopy(A,0,B,0,size);
			return B;
		}else{
			return A;
		}
	}	
	
	
	/**
	 * @param priceDate the priceDate to set
	 */
	public void setPriceDate(String[] priceDate) {
		this.priceDate = priceDate;
	}



	/**
	 * @param sma200History the sma200History to set
	 */
	public void setSma200History(float[] sma200History) {
		this.sma200History = sma200History;
	}



	/**
	 * @param sma5History the sma5History to set
	 */
	public void setSma5History(float[] sma5History) {
		this.sma5History = sma5History;
	}	


	
	private void addTrades(Trades trades){
		tradesList.add(trades);
	}


	public Trades getTrades(ETFFilterType etfFilterType){
		Iterable <Trades> tradesi = null;
		Trades trades=new Trades();
		
		switch(etfFilterType){

		case R3:	
			tradesi = Trades.R3.filter(tradesList);
			break;

		case RSI25:	
			tradesi = Trades.RSI25.filter(tradesList);
			break;
		
		case RSI75:
			tradesi = Trades.RSI75.filter(tradesList);
			break;
		}

		for(Trades myTrades : tradesi){
			trades=myTrades;
		}
		return trades ;
	}
	
	public boolean isPersistant(){
		return key!=null;
	}

	public boolean tradesExist(){
		return (tradesList!=null && !tradesList.isEmpty());
	}

	public float[] getRsi4History() {
		return rsi4History;
	}

	public void setRsi4History(float[] rsi4History) {
		this.rsi4History = rsi4History;
	}
	
}	