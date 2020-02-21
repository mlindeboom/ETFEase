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

import com.etfease.android.app.main;

public class ETF {

	private String name;
	private String symbol;
	private ETFType etfType;
	private float[] changeHistory;
	private float[] priceHistory;
	private String[] priceDate;
	private float[] sma200History;
	private float[] sma5History;
	private float rsiHistory[];
	private List <Trades> tradesList = new ArrayList <Trades> ();


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
		this.name = name;
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
	}


	public static final Filter<ETF> NON_US_ETF = 
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			return etf.getEtfType().name().equals(ETFType.GlobalEquityETF.name());
		}
	};	

	public static final Filter<ETF> US_ETF =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			return etf.getEtfType().name().equals(ETFType.USEquityETF.name());
		}
	};	

	public static final Filter<ETF> BOND_ETF =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			return etf.getEtfType().name().equals("FixedIncomeETF");
		}
	};	

	public static final Filter<ETF> COMMODITY_ETF =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			return etf.getEtfType().name().equals("CommodityBasedETF");
		}
	};

	public static final Filter<ETF> RECENTBUYS =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			return etf.isRecentBuy();
		}
	};

	public static final Filter<ETF> RECENTSELLS =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			return etf.isRecentSell();
		}
	};

	public static final Filter<ETF> SELLNOW =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			return etf.isSellNow();
		}
	};

	public static final Filter<ETF> BUYNOW =
		new Filter<ETF>() {
		public boolean passes(ETF etf) {
			return etf.isBuyNow();
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



	public void addTrades(Trades trades){
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


	public boolean isRecentBuy(){
		return getTrades(main.passETFFilterType).isRecentBuy();
	}
	
	public Trade recentBuy(){

		List<Trade> trades = getTrades(main.passETFFilterType).getTrades();
		int last = trades.size();

		if (last==0) return null;
		else last--;

		if(trades.get(last).getShares()>0){
			return trades.get(last);
		}
		else return null;
	}


	public boolean isBuyNow(){
		return getTrades(main.passETFFilterType).isBuyNow();
	}	
	
	public Trade buyNow(){

		List<Trade> trades = getTrades(main.passETFFilterType).getTrades();
		int last = trades.size();

		if (last==0) return null;
		else last--;


		if(trades.get(last).getShares()>0 && getTradeDateDiff(trades.get(last).getDate())<=1){
			return trades.get(last);
		}
		else return null;

	}

	public boolean isRecentSell(){
		return getTrades(main.passETFFilterType).isRecentSell();
	}	
	
	
	public Trade recentSell(){

		List<Trade> trades = getTrades(main.passETFFilterType).getTrades();
		int last = trades.size();

		if (last==0) return null; 
		else last--;
		if(trades.get(last).getShares()<0 && getTradeDateDiff(trades.get(last).getDate())<=7){
			return trades.get(last);
		}
		else return null;

	}


	public boolean isSellNow(){
		return getTrades(main.passETFFilterType).isSellNow();
	}	
	
	public Trade sellNow(){

		List<Trade> trades = getTrades(main.passETFFilterType).getTrades();
		int last = trades.size();

		if (last==0) return null;
		else last--;
		if(trades.get(last).getShares()<0 && getTradeDateDiff(trades.get(last).getDate())<=1) {

			return trades.get(last);
		}
		else return null;

	}

	private long getTradeDateDiff(String date){
		long diff=0;

		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d");
			Calendar tradeDate = Calendar.getInstance(); 
			Calendar today = Calendar.getInstance();
			tradeDate.setTime(sdf.parse(date));

			diff = (today.getTimeInMillis()-tradeDate.getTimeInMillis())/(24 * 60 * 60 * 1000);

		}catch(Throwable t){}

		return diff;
	}


}	