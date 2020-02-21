package com.etfease.datasource;

public interface ETFFilter {

	public abstract boolean[] getBuys();

	public abstract boolean[] getSells();

	public abstract String toString();

	public abstract String dump();
	
	public abstract ETF getETF();
	
	public abstract ETFFilterType getEtfFilterType();

}