package com.etfease.datasource;

public class RSI25Filter implements ETFFilter {
	
	private ETF etf;
	
	public RSI25Filter(ETF etf){
		this.etf=etf;
	}
	
	
	public String dump() {
		return "RSI25Filter";
	}

	
	public boolean[] getBuys() { 
		return new boolean[etf.getPriceDate().length];
	}

	
	public ETF getETF() {
		return etf;
	}

	
	public ETFFilterType getEtfFilterType() {
		return ETFFilterType.RSI25;
	}

	
	public boolean[] getSells() {
		return new boolean[etf.getPriceDate().length];
	}

}
