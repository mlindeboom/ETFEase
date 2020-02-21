package com.etfease.datasource;


public enum ETFFilterType {
	R3,RSI25,RSI75;

	public ETFFilter getETFFilter(ETFFilterType etfFilterType, ETF etf){
		switch (etfFilterType){
		case R3: return new R3Filter(etf);
		case RSI75: return new RSI75Filter(etf);
		case RSI25: return new RSI25Filter(etf);
		}

		return null;
	}

}