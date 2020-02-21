package com.etfease.android.app;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;

import com.etfease.datasource.ETF;
import com.etfease.datasource.ETFPortfolio;
import com.etfease.datasource.Trade;

public class ETFService extends Service {

	public static final int NOTIFICATION_ID = 1;
	public static final String USER_PREFERENCE = "USER_PREFERENCES";
	public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
	public static final String PREF_UPDATE_FREQ = "PREF_UPDATE_FREQ";
	public static final String TRADES_NOTIFIED = "TRADES_NOTIFIED";
	private Notification newTradeNotification;

	AlarmManager alarms;
	PendingIntent alarmIntent;

	@Override
	public void onCreate() {

		int icon = R.drawable.icon;
		String tickerText = "Trade Notification";
		long when = System.currentTimeMillis();

		newTradeNotification = new Notification(icon, tickerText, when);

		alarms = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		String ALARM_ACTION = TradeAlarmReceiver.ACTION_REFRESH_TRADE_ALARM;
		Intent intentToFire = new Intent(ALARM_ACTION);
		alarmIntent = PendingIntent.getBroadcast(this, 0, intentToFire, 0);

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// Retrieve the shared preferences
		SharedPreferences prefs = getSharedPreferences(USER_PREFERENCE, Activity.MODE_PRIVATE);
		boolean autoUpdate = prefs.getBoolean(PREF_AUTO_UPDATE, true);
		int updateFreq = prefs.getInt(PREF_UPDATE_FREQ, 20); 

		if (autoUpdate) {
			int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
			long timeToRefresh = SystemClock.elapsedRealtime() + updateFreq*60*1000;
			alarms.set(alarmType, timeToRefresh, alarmIntent);
		}
		else
			alarms.cancel(alarmIntent); 

		refreshTrades();
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void announceNewTrade(ETF etf) {
		String svcName = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager;
		notificationManager = (NotificationManager)getSystemService(svcName);

		Context context = getApplicationContext();
		main.passAlarmETF = etf; 
		String expandedText = etf.getName();
		String expandedTitle = etf.getSymbol(); 
		Intent startActivityIntent = new Intent(this, TradeAlertView.class); 
		PendingIntent launchIntent = PendingIntent.getActivity(context, 0, startActivityIntent, 0);

		newTradeNotification.setLatestEventInfo(context, 
				expandedTitle, 
				expandedText,
				launchIntent);
		newTradeNotification.when = java.lang.System.currentTimeMillis();
		Resources myResources = getResources();
		InputStream is = myResources.openRawResource(R.raw.notify);
		
		newTradeNotification.sound = Uri.parse("android.resource://com.etfease.android.app/" + R.raw.notify);
		notificationManager.notify(NOTIFICATION_ID, newTradeNotification);	  

	}

	private void refreshTrades() {
		Thread updateThread = new Thread(null, backgroundRefresh, "refresh_trades");  
		updateThread.start();        
	}    

	private Runnable backgroundRefresh = new Runnable() {
		public void run() {
			doRefreshTrades();
		}        
	};

	private void doRefreshTrades() {

		SharedPreferences prefs = getSharedPreferences(USER_PREFERENCE, Activity.MODE_PRIVATE);
		String tradesNotified = prefs.getString(TRADES_NOTIFIED, "");

		//get portfolio
		ETFPortfolio etfPortfolio = new ETFPortfolio();
		
	    String myList = prefs.getString("PORTFOLIO", "FXI,ILF,MXI");
	    etfPortfolio.init(myList);
		ConcurrentHashMap <String, ETF> portfolio = etfPortfolio.getEtfs();

		//search for buy or sell recommendations
		for(ETF etf : portfolio.values()){
			Trade buy = etf.buyNow();
			Trade sell = etf.sellNow();

			//check if its new and save the recommendation in preferences
			if(buy!=null){

				TradeList tradeList = new TradeList(tradesNotified);
				if (tradeList.isNewTrade(etf.getSymbol()+" "+buy.toString())){
					announceNewTrade(etf);
					//save to prevent repeat notifications
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString(TRADES_NOTIFIED, tradeList.toString()); 
					editor.commit();
				}
			}

			if(sell!=null){
				TradeList tradeList = new TradeList(tradesNotified);
				if (tradeList.isNewTrade(etf.getSymbol()+" "+sell.toString())){
					announceNewTrade(etf);
					//save to prevent repeat notifications
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString(TRADES_NOTIFIED, tradeList.toString()); 
					editor.commit();
				}
			}

		}
	}


	private class TradeList{

		List <String> listOfTrades;
		String listOfTradesString;
		
		TradeList(String s){
			//## delimited list of trades
			listOfTrades = Arrays.asList(s.split("##"));

		}

		boolean isNewTrade(String s){
			//if a trade comes along remove any trade of the same ticker
			if (listOfTrades.contains(s)){ 
				//do nothing trade is already in the list
				return false;
			}else{
				//add the new trade but remove any old ones
				addNewTrade(s);
				return true;
			}

		}

		//add a new trade to the list but remove any old ones
		private void addNewTrade(String s){
			StringBuffer sb = new StringBuffer();
			boolean found = false;
			for(String entry:listOfTrades){
				String symbol = "";
				if(entry.length()!=0) symbol = entry.substring(0,2);
				
				if(s!=null && s.length()!=0 && s.substring(0,2).equals(symbol)) {
					sb.append(s);
					sb.append("##");
					found=true;
				}else{
					sb.append(entry);
					sb.append("##");
				}
			}
			if(!found){
				sb.append(s);
				sb.append("##");

			}
			listOfTradesString=sb.toString();
		}
		
		public String toString(){
			return listOfTradesString;
		}
	}
	
}



