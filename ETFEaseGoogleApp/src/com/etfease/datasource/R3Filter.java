package com.etfease.datasource;
/*
 *The ETF is above the 200 day SMA
 *The 2 period RSI drops 3 days in a row
 *The first days drop is from below 60 for RSI (2)
 *The 2 period RSI closes under 10 - BUY trigger
 *The 2 period RSI closes above 70 - SELL trigger
 */

public class R3Filter implements ETFFilter {

	
	private ETF etf;
	private boolean[] above200;
	private boolean[] rsiBelow60;
	private boolean[] period3Drop;
	private boolean[] below10;
	private boolean[] buy;
	private boolean[] sell;
	
	public R3Filter(ETF etf){
		super();
		this.etf = etf;
		applyFilter();
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

	
	private void applyFilter(){

		//test 1 -- ETF is above the 200 day SMA
		test1();
		//test 2 -- RSI (2) is 60 or below 4 periods ago
		test2();
		//test 3 -- RSI (2) drops for 3 periods
		test3();
		//test 4 -- RSI (2) is below 10 for the current period
		test4();
		//buy -- all conditions met indicate buy
		buy();
		//sell -- RSI (2) is above 70 after buy is indicated
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

	//test 2 -- RSI (2) is 60 or below 4 periods ago
	private void test2(){
		int count = etf.getChangeHistory().length;
		rsiBelow60 = new boolean[count];
		for(int i=3;i<count;i++){
			if(etf.getRsiHistory()[i]<=60)
				rsiBelow60[i-3]=true;
			else rsiBelow60[i-3]=false;
		}		
	}

	//test 3 -- RSI (2) drops for 3 periods
	private void test3(){
		int count = etf.getChangeHistory().length;
		period3Drop = new boolean[count];
		for(int i=3;i<count;i++){
			if(etf.getRsiHistory()[i-3]<etf.getRsiHistory()[i-2])
				if(etf.getRsiHistory()[i-2]<etf.getRsiHistory()[i-1])
					if(etf.getRsiHistory()[i-1]<etf.getRsiHistory()[i])
						period3Drop[i-3]=true;
					else period3Drop[i-3]=false;
		}		
	}

	//test 4 -- RSI (2) is below 10 for the current period
	private void test4(){
		int count = etf.getChangeHistory().length;
		below10 = new boolean[count];

		for(int i=0;i<count;i++){
			if(etf.getRsiHistory()[i]<10)
				below10[i]=true;
			else below10[i]=false;
		}
	}

	//buy -- pass tests 1-4
	private void buy(){
		int count = etf.getChangeHistory().length;
		buy = new boolean[count];
		for(int i=0;i<count;i++){
			if(above200[i]&&rsiBelow60[i]&&period3Drop[i]&&below10[i]){
				buy[i]=true;
			}
		}
	}

	//test 5 -- RSI (2) is above 70 after passing tests 1-4
	private void sell(){
		int count = etf.getChangeHistory().length;
		sell = new boolean[count];
		boolean bought = false;
		for(int i=count-1;i!=-1;i--){
			if(buy[i]==true) bought=true ;
			if (bought==true && etf.getRsiHistory()[i]>70){
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
				sb.append(etf.getRsiHistory()[i]);
				sb.append(", above 200 ");
				sb.append(above200[i]);
				sb.append(", RSI<60 ");
				sb.append(rsiBelow60[i]);
				sb.append(", 3 drops in RSI ");
				sb.append(period3Drop[i]);
				sb.append(", RSI<10 ");
				sb.append(below10[i]);
				sb.append(sell[i]?",SELL " + etf.getPriceHistory()[i]:"");
				sb.append(buy[i]?",BUY " + etf.getPriceHistory()[i]:"");
				sb.append("\n");
			}


		return sb.toString();
	}

	/**
	 * @return the etf
	 */
	public ETF getETF() {
		return etf;
	}

	/**
	 * @return the etfFilterType
	 */
	public ETFFilterType getEtfFilterType() {
		return ETFFilterType.R3;
	}




}
