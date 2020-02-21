package com.etfease.datasource;

import java.util.Comparator;

public class ETFSortByTicker implements Comparator<ETF> {
    public int compare(ETF o1, ETF o2) {
        return o1.getSymbol().compareTo(o2.getSymbol());
    }
}