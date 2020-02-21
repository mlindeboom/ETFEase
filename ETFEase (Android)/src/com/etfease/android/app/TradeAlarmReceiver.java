package com.etfease.android.app;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TradeAlarmReceiver extends BroadcastReceiver {

	public static final String ACTION_REFRESH_TRADE_ALARM = "com.etfease.android.ACTION_REFRESH_TRADE_ALARM"; 

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startIntent = new Intent(context, ETFService.class);
		context.startService(startIntent);
	}

}
