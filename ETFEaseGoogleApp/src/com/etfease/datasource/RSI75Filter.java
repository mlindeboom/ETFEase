package com.etfease.datasource;

public class RSI75Filter implements ETFFilter {

	private ETF etf;
	
	public RSI75Filter(ETF etf){
		this.etf=etf;
	}
	
	
	public String dump() {
		return "RSI75Filter";
	}

	
	public boolean[] getBuys() {
		return new boolean[etf.getPriceDate().length];
	}

	
	public ETF getETF() {
		return etf;
	}

	
	public ETFFilterType getEtfFilterType() {
		return ETFFilterType.RSI75;
	}

	
	public boolean[] getSells() {
		return new boolean[etf.getPriceDate().length];
	}

}
