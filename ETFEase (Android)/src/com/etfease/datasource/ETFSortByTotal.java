package com.etfease.datasource;

import java.util.Comparator;

public class ETFSortByTotal implements Comparator<ETF> {
	private ETFFilterType etfFilterType;
	public ETFSortByTotal(ETFFilterType etfFilterType){
		super();
		this.etfFilterType = etfFilterType;
	}
    public int compare(ETF o2, ETF o1) {
        return new Float(o1.getTrades(etfFilterType).getTotal()).compareTo(new Float(o2.getTrades(etfFilterType).getTotal()));
    }
}