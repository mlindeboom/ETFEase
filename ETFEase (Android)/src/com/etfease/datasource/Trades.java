package com.etfease.datasource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public class Trades {
	private ETFFilterType etfFilterType;
	private float total;
	private List<Trade> trades = new  ArrayList <Trade> ();
	private boolean recentBuy;
	private boolean recentSell;
	private boolean buyNow;
	private boolean sellNow;
	private String lastTradeDate;
	
	

	public static final Filter<Trades> R3 = 
		new Filter<Trades>() {
		public boolean passes(Trades trades) {
			return trades.getEtfFilterType().name().equals("R3");
		}
	};	

	public static final Filter<Trades> RSI25 = 
		new Filter<Trades>() {
		public boolean passes(Trades trades) {
			return trades.getEtfFilterType().name().equals("RSI25");
		}
	};	
	
	public static final Filter<Trades> RSI75 = 
		new Filter<Trades>() {
		public boolean passes(Trades trades) {
			return trades.getEtfFilterType().name().equals("RSI75");
		}
	};	
	
	
	
	public void addTrade(float shares, float tradeAmount, float remainingAmount, float fee, String date){
		trades.add(new Trade(shares, tradeAmount, remainingAmount, fee, date));
	}
	
	public float getTotal(){
		return total;
	}
	
	public List <Trade> getTrades(){
		return trades;
	}




	/**
	 * @return the etfFilter
	 */
	public ETFFilterType getEtfFilterType() {
		return etfFilterType;
	}

	/**
	 * @param etfFilter the etfFilter to set
	 */
	public void setEtfFilterType(ETFFilterType etfFilterType) {
		this.etfFilterType = etfFilterType;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(float total) {
		this.total = total;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		Iterator <Trade> trd= trades.iterator();
		while(trd.hasNext()){
			sb.append(trd.next().toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	public boolean isRecentBuy() {
		return recentBuy;
	}

	public void setRecentBuy(boolean recentBuy) {
		this.recentBuy = recentBuy;
	}

	public boolean isRecentSell() {
		return recentSell;
	}

	public void setRecentSell(boolean recentSell) {
		this.recentSell = recentSell;
	}

	public boolean isBuyNow() {
		return buyNow;
	}

	public void setBuyNow(boolean buyNow) {
		this.buyNow = buyNow;
	}

	public boolean isSellNow() {
		return sellNow;
	}

	public void setSellNow(boolean sellNow) {
		this.sellNow = sellNow;
	}

	public String getLastTradeDate() {
		return lastTradeDate;
	}

	public void setLastTradeDate(String lastTradeDate) {
		this.lastTradeDate = lastTradeDate;
	}

}
