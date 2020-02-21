package com.etfease.datasource;
/*
 *The ETF is above the 200 day SMA
 *The 4 period RSI closes under 25 - BUY trigger
 *The 4 period RSI closes above 55 - SELL trigger
 */
public class RSI25Filter implements ETFFilter {
	
	private ETF etf;
	private boolean[] above200;
	private boolean[] rsiBelow25;
	private boolean[] buy;
	private boolean[] sell;
	
	public RSI25Filter(ETF etf){
		super();
		this.etf = etf;
		applyFilter();
	} 
	

	
	private void applyFilter(){

		//test 1 -- ETF is above the 200 day SMA
		test1();
		//test 3 -- RSI (4) below 25 
		test2();
		//buy -- all conditions met indicate buy
		buy();
		//sell -- RSI (4) is above 55 after buy is indicated
		sell();
	}
	
	
	
	//test 1 -- ETF is above the 200 day SMA
	private void test1(){
		int count = etf.getChangeHistory().length;
		above200 = new boolean[count];

		for(int i=0;i<count;i++){
			if(etf.getPriceHistory()[i]>etf.getSma200History()[i] && etf.getSma200History()[i]>0)
				above200[i]=true;
			else above200[i]=false;
		}
	}
	
	//test 2 -- RSI (4) is below 25 for the current period
	private void test2(){
		int count = etf.getChangeHistory().length;
		rsiBelow25 = new boolean[count];

		for(int i=0;i<count;i++){
			if(etf.getRsi4History()[i]<25)
				rsiBelow25[i]=true;
			else rsiBelow25[i]=false;
		}
	}
	
	//buy -- pass tests 1-2
	private void buy(){
		int count = etf.getChangeHistory().length;
		buy = new boolean[count];
		for(int i=0;i<count;i++){
			if(above200[i]&&rsiBelow25[i]){
				buy[i]=true;
			}
		}
	}
	
	
	//sell -- RSI (4) is above 55 after passing tests 1-2
	private void sell(){
		int count = etf.getChangeHistory().length;
		sell = new boolean[count];
		boolean bought = false;
		for(int i=count-1;i!=-1;i--){
			if(buy[i]==true) bought=true ;
			if (bought==true && etf.getRsi4History()[i]>55){
				bought=false;
				sell[i]=true;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.etftrader.datasource.ETFFilter#toString()
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		int count = etf.getChangeHistory().length;
		for(int i=count-1; i>0; i--){
			if (buy[i]||sell[i]){
				sb.append(etf.getPriceDate()[i]);
				sb.append(sell[i]?",SELL " + etf.getPriceHistory()[i]:"");
				sb.append(buy[i]?",BUY " + etf.getPriceHistory()[i]:"");
				sb.append("\n");
			}
		}		


		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see com.etftrader.datasource.ETFFilter#dump()
	 */
	public String dump(){
		StringBuffer sb = new StringBuffer();
		int count = etf.getChangeHistory().length;
		for(int i=count-1; i>0; i--){
				sb.append(etf.getPriceDate()[i]);
				sb.append(", ");
				sb.append(etf.getPriceHistory()[i]);
				sb.append(", ");
				sb.append(etf.getSma200History()[i]);
				sb.append(", ");
				sb.append(etf.getRsi4History()[i]);
				sb.append(", above 200 ");
				sb.append(above200[i]);
				sb.append(", RSI<25 ");
				sb.append(rsiBelow25[i]);
				sb.append(sell[i]?",SELL " + etf.getPriceHistory()[i]:"");
				sb.append(buy[i]?",BUY " + etf.getPriceHistory()[i]:"");
				sb.append("\n");
			}


		return sb.toString();
	}

	
	

	
	public ETF getETF() {
		return etf;
	}

	
	public ETFFilterType getEtfFilterType() {
		return ETFFilterType.RSI25;
	}

	
	/* (non-Javadoc)
	 * @see com.etftrader.datasource.ETFFilter#getBuys()
	 */
	public boolean[] getBuys(){
		return buy;
	}

	/* (non-Javadoc)
	 * @see com.etftrader.datasource.ETFFilter#getSells()
	 */
	public boolean[] getSells(){
		return sell;
	}

}
