package com.etfease.android.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etfease.datasource.ETF;
import com.etfease.datasource.Trade;

public class TradeAlertView extends Activity {

	public static final int MENU_ADD = 1;
	public static final int MENU_REMOVE = 2;
	public static final int MENU_DETAIL = 3;
	public static final String USER_PREFERENCE = "USER_PREFERENCES";
	private static final String CHART = "http://www.google.com/finance/chart?cht=o&q=<SYMBOL>&tlf=12&chs=m&p=<DAYS>d&client=sfa";
	SharedPreferences prefs;
	
	
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);

		setContentView(R.layout.trades_view);
		ETF etf = main.passAlarmETF;
		//Log.i("TradeView","ETF = "+etf.getSymbol());
		TextView tradeView1 = (TextView)this.findViewById(R.id.tradeText1);
		tradeView1.setText("");
		TextView tradeView2 = (TextView)this.findViewById(R.id.tradeText2);
		tradeView2.setText("");
		TextView tradeHeader = (TextView)this.findViewById(R.id.tradeHeader);
		tradeHeader.setText("Trade History");
		
		float total = etf.getTrades(main.passETFFilterType).getTotal();
		int count = etf.getTrades(main.passETFFilterType).getTrades().size()/2;
		float sellAmt=0;
		float buyAmt=0;
		float gain=0;
		String DATE_FORMAT = "yyyy-MM-d";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		
		tradeView1.append(Html.fromHtml("<b><big>" + etf.getSymbol() + "</big></b>"));
		tradeView1.append("\n");
		tradeView1.append(etf.getName());

		//check for any active buy recommendations
		Trade t = etf.recentBuy();
		if(t!=null){
			tradeView1.append("\nBuy recommended on ");
			tradeView1.append(t.getDate());
			tradeView1.append("\nPrice: ");
			tradeView1.append(""+ Round((t.getTradeAmount()/t.getShares()),2));
		}

		//check for any active buy recommendations
		t = etf.recentSell();
		
		if(t!=null){
			tradeView1.append("\nSell recommended on ");
			tradeView1.append(t.getDate());
			tradeView1.append("\nPrice: ");
			tradeView1.append(""+ Round((-t.getTradeAmount()/t.getShares()),2));
		}
		
		
		
		
		List<Trade> trades = etf.getTrades(main.passETFFilterType).getTrades();
		String sellDate = "";
		String buyDate = "";

		
		for(Trade trade:trades){
			trade.getRemainingAmount();
			trade.getFee();

			if(trade.getShares()<0){
				//sell
				sellDate = trade.getDate();
				sellAmt = trade.getTradeAmount();
				gain = sellAmt-buyAmt;
				tradeView2.append("\n\n");
				tradeView2.append(buyDate);
				tradeView2.append(" to ");
				tradeView2.append(sellDate);
				tradeView2.append("\n");
				tradeView2.append(""+-trade.getShares());
				tradeView2.append(" shares for $");
				tradeView2.append(Html.fromHtml("<b>"+sellAmt+"</b>"));
				if(gain<0){
					tradeView2.append("\n");
					tradeView2.append(Html.fromHtml("<font color=red>" + "Loss: $"+ Round(gain,2) + "</font>"));
				}else{
					tradeView2.append("\n");
					tradeView2.append(Html.fromHtml("<font color=green>" + "Gain: $"+ Round(gain,2) + "</font>"));
				}

			}else{
				//buy
				buyDate = trade.getDate();
				buyAmt = trade.getTradeAmount();
			}
			
		}

		tradeView2.append("\n\n\nNote: $10000 initial investment. Entire balance reinvested with each trade. Each trade costs $7 dollars.");

		String chart = CHART.replaceAll("<SYMBOL>",main.passAlarmETF.getSymbol());
		String days = "1";
		if (isMarketOpen()==false)days="5";
		
		chart = chart.replaceAll("<DAYS>", days);
		
		Drawable image = ImageOperations(this,chart);
		ImageView imgView = new ImageView(this);
		imgView = (ImageView)findViewById(R.id.tradeChart);

		
		
		
		imgView.setImageDrawable(image);		
		
	}
	

	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {

		SharedPreferences preferences = getSharedPreferences(USER_PREFERENCE, MODE_PRIVATE);
	    String myList = preferences.getString("PORTFOLIO", "");
	    String symbol = main.passAlarmETF.getSymbol();

	    if(myList.indexOf(symbol)!=-1){
	    	MenuItem mi = menu.add(0, MENU_REMOVE, 0, "Portfolio");
	    	mi.setIcon(R.drawable.remove);
	    }else{
	    	MenuItem mi = menu.add(0, MENU_ADD, 0, "Portfolio");
	    	mi.setIcon(R.drawable.add);
	    }
		
		return true;
	}
	
	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {    
		
		SharedPreferences preferences = getSharedPreferences(USER_PREFERENCE, MODE_PRIVATE);
	    String myList = preferences.getString("PORTFOLIO", "");
	    String symbol = main.passAlarmETF.getSymbol();
		SharedPreferences.Editor editor = preferences.edit();

	    switch (item.getItemId()) {    
		case MENU_ADD:
		    
		    if(myList.length()==0) myList = symbol;
		    else if(myList.indexOf(symbol)==-1){
		    	myList = myList + "," + symbol;
		    }
		    
			editor.putString("PORTFOLIO", myList); 
			editor.commit();
			Toast.makeText(getApplicationContext(), myList, Toast.LENGTH_SHORT).show();

			return true;    

		case MENU_REMOVE:
		    
		    if(myList.indexOf(symbol)!=-1){
		    	myList=myList.replaceAll(","+symbol, "");
		    	myList=myList.replaceAll(symbol+"," , "");
		    	myList=myList.replaceAll(symbol, "");
		    }
		    
			editor.putString("PORTFOLIO", myList); 
			editor.commit();
			Toast.makeText(getApplicationContext(), myList, Toast.LENGTH_SHORT).show();

			return true;    

			
		}    
		return false;
	}

	private Drawable ImageOperations(Context ctx, String url) {

		
		Bitmap bitmap;
		try {

			DisplayMetrics dm = new DisplayMetrics(); 
			getWindowManager().getDefaultDisplay().getMetrics(dm); 
			int screenWidth = dm.widthPixels; 
			
			URL myImageURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection)myImageURL.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			bitmap = BitmapFactory.decodeStream(input);

	        int width = bitmap.getWidth();
	        int height = bitmap.getHeight();
	        float scaleWidth = (((float) screenWidth) / width);
	        float scaleHeight = scaleWidth;
	        Matrix matrix = new Matrix();
	        matrix.postScale(scaleWidth, scaleHeight);
	        // create the new Bitmap object
	        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	        BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
	        
	        return bmd;
	        
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

/*		
		try {
			InputStream is = (InputStream) this.fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
*/
	}

	public Object fetch(String address) throws MalformedURLException,IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

	
	private boolean isMarketOpen(){
		
		// Get TimeZone of user
		Calendar c = Calendar.getInstance( TimeZone.getTimeZone(("America/New_York")));
		
		int weekday = c.get(Calendar.DAY_OF_WEEK);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (weekday>Calendar.SUNDAY&&weekday<Calendar.SATURDAY && hour>8 && hour<16) return true;
		
		return false;
	}
	
	private float Round(float Rval, int Rpl) {
		float p = (float)Math.pow(10,Rpl);
		Rval = Rval * p;
		float tmp = Math.round(Rval);
		return (float)tmp/p;
	}
	
	
}
