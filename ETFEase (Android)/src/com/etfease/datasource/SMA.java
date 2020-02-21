package com.etfease.datasource;

public class SMA {

	private String[] changeDate;
	private float[] priceHistory;
	private float[] smaHistory;
	private int period;
	
	public SMA(int period, float[] priceHistory, String[] changeDate){
		super();
		this.period = period;
		this.priceHistory = priceHistory;
		this.changeDate = changeDate;
		smaHistory = new float[priceHistory.length];
	}

	public float[] getSMAHistory(){
		for(int i=0; i<priceHistory.length; i++){
			float[]prices = new float[period];
			float sma = 0;
			if (priceHistory.length-i > period){
				System.arraycopy(priceHistory, i, prices, 0, period);
				sma = getSMA(prices);
			}
			smaHistory[i]=sma;
		}
		return smaHistory;
	}

	private float getSMA(float prices[]){
		float total=0;
		for (int i=0; i<prices.length; i++){
			total+=prices[i];
		}
		return total/period;
	}
}
