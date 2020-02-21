package com.etfease.datasource;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;

import com.google.appengine.api.memcache.MemcacheService;

public class Trades implements Serializable{
	
	public static boolean SMALL = true;
	public static boolean BIG = false;

	private static final long serialVersionUID = -1L;
	
	private ETFFilterType etfFilterType;
	
	private float total;
	
	private List<Trade> trades = new  ArrayList <Trade> ();
	
	private String symbol;
	
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

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public boolean isRecentBuy() {
		return recentBuy;
	}

	public boolean isRecentSell() {
		return recentSell;
	}

	public boolean isBuyNow() {
		return buyNow;
	}

	public boolean isSellNow() {
		return sellNow;
	}
	
	
	public void writeToMemory() throws CacheException {

        Cache cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap()); 
        Map props = new HashMap(); 
        props.put(MemcacheService.SetPolicy.REPLACE_ONLY_IF_PRESENT, true);        
        String key = symbol+etfFilterType.name();
        System.out.println(key);
        cache.put(key, this);
        
        
	}
	
	public void restoreFromMemory(boolean size) throws CacheException {
		
		Cache cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap()); 

        String key = symbol+etfFilterType.name();
        System.out.println(key);
		Trades myTrades = (Trades) cache.get(key); 
		this.etfFilterType = myTrades.getEtfFilterType();
		this.symbol = myTrades.getSymbol();
		this.total = myTrades.getTotal();
		
		recentBuy(myTrades.getTrades());
		recentSell(myTrades.getTrades());
		sellNow(myTrades.getTrades());
		buyNow(myTrades.getTrades());
		
		//use size to minimize volume of data sent to client
		if (size == BIG) {
			this.trades = myTrades.getTrades();
		} else this.trades = new ArrayList<Trade>();
	}

	
	public void recentBuy(List<Trade> trades){

		int last = trades.size();

		if (last==0) this.recentBuy=false;
		else last--;

		if(trades.get(last).getShares()>0){
			this.lastTradeDate=trades.get(last).getDate();
			this.recentBuy=true;
		}
		else this.recentBuy=false;
	}

	public void buyNow(List<Trade> trades){

		int last = trades.size();

		if (last==0) buyNow=false;
		else last--;


		if(trades.get(last).getShares()>0 && getTradeDateDiff(trades.get(last).getDate())<=1){
			this.lastTradeDate=trades.get(last).getDate();
			buyNow=true;
		}
		else buyNow=false;
	}

	public void recentSell(List<Trade> trades){

		int last = trades.size();

		if (last==0) this.recentSell=false; 
		else last--;
		if(trades.get(last).getShares()<0 && getTradeDateDiff(trades.get(last).getDate())<=7){
			this.lastTradeDate=trades.get(last).getDate();
			this.recentSell=true;
		}
		else this.recentSell=false;

	}

	public void sellNow(List<Trade> trades){

		int last = trades.size();

		if (last==0) this.sellNow=false;
		else last--;
		if(trades.get(last).getShares()<0 && getTradeDateDiff(trades.get(last).getDate())<=1) {

			this.sellNow=true;
			this.lastTradeDate=trades.get(last).getDate();
		}
		this.sellNow=false;

	}

	private long getTradeDateDiff(String date){
		long diff=0;

		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d");
			Calendar tradeDate = Calendar.getInstance(); 
			Calendar today = Calendar.getInstance();
			tradeDate.setTime(sdf.parse(date));

			diff = (today.getTimeInMillis()-tradeDate.getTimeInMillis())/(24 * 60 * 60 * 1000);

		}catch(Throwable t){}

		return diff;
	}
	
	
	
}
