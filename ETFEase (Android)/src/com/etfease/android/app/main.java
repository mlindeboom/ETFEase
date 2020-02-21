package com.etfease.android.app;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;

import com.etfease.datasource.ETF;
import com.etfease.datasource.ETFFilterType;
import com.etfease.datasource.ETFList;
import com.etfease.datasource.ETFPortfolio;
import com.etfease.datasource.ETFSortByTotal;


public class main extends TabActivity {


	static final int DIALOG_FILTER_ID = 0;  
	static final int DIALOG_EDIT_ID = 1;  
	static final int DIALOG_LOAD_ID = 2; 
	static final int DIALOG_PORTFOLIO = 3;
	static final int DIALOG_STRATEGY = 4;

	static final int MENU_REFRESH = 0;
	static final int MENU_FILTER = 1;
	static final int MENU_STRATEGY = 2;
	static final int TAB_ETF = 0;
	static final int TAB_PORTFOLIO = 1;
	static final int TAB_NEWS = 2;


	private ListView myETFListView;
	private ListView myPortfolioListView;
	private View myTradesView;
	private ArrayAdapter<ETF> etfAdapter;
	private ArrayAdapter<ETF> portfolioAdapter;
	private ETFList etflist=last;//initialize with saved static value
	private ETFPortfolio etfPortfolio = new ETFPortfolio(); 
	private	ArrayList <ETF> etfs = new ArrayList<ETF>();
	private	ArrayList <ETF> portfolioEtfs = new ArrayList<ETF>();
	private ProgressDialog progressDialogETFTab;
	private ProgressDialog progressDialogPortfolioTab;
	private ProgressThreadETFTab progressThreadETFTab;
	private ProgressThreadPortfolioTab progressThreadPortfolioTab;
	private SharedPreferences preferences;



	public static final String USER_PREFERENCE = "USER_PREFERENCES";
	public static ETF passETF=null;
	public static ETF passAlarmETF=null;
	public static ETFFilterType passETFFilterType = ETFFilterType.RSI25;
	public static String filter="";
	private static ETFList last=null;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TabHost mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("ETFs",getResources().getDrawable(R.drawable.markets_selected)).setContent(R.id.tabview1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("Portfolio",getResources().getDrawable(R.drawable.portfolio_selected)).setContent(R.id.tabview2));
		//mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("News",getResources().getDrawable(R.drawable.news_selected)).setContent(R.id.tabview3));

		//reinit etf tab
		repopulateETFTab();

		//populate portfolio
		populatePortfolioTab(mTabHost);
		startService(new Intent(this, ETFService.class));

		//setup to refresh etf list the first time etf tab is visible
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {

				Log.v("", "click");
				if(etflist==null){
					populateETFTab(getTabHost());
				}

				getTabHost().getCurrentTabView().setOnTouchListener(
						new OnTouchListener() {
							public boolean onTouch(View v, MotionEvent event) {
								if (event.getAction() == MotionEvent.ACTION_DOWN)

									Log.v("", "touch");
								return false; // returning false seems do the
							}
						});
			}
		});





	}

	@Override
	public Dialog onCreateDialog(int id) {

		final CharSequence[] items = {"ALL", "US ETF", "Non-US ETF", "Bond ETF", "Commodity ETF", "RECENT BUYS", "BUY NOW", "RECENT SELLS", "SELL NOW"};
		final CharSequence[] strategies = {"R3", "RSI25"};



		switch(id) {
		case DIALOG_FILTER_ID :        
			LayoutInflater li = LayoutInflater.from(this);
			View filterView = li.inflate(R.layout.filter_view, null);

			AlertDialog.Builder filterDialog = new AlertDialog.Builder(this);
			filterDialog.setTitle("ETF Filter");         
			filterDialog.setView(filterView);

			filterDialog.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					Iterable<ETF> etfi = null;

					switch(item){

					case 0:	
						etfi = etflist.getEtfs().values();
						break;
					case 1:
						etfi = ETF.US_ETF.filter(etflist.getEtfs().values());
						break;
					case 2:
						etfi = ETF.NON_US_ETF.filter(etflist.getEtfs().values());
						break;
					case 3:
						etfi = ETF.BOND_ETF.filter(etflist.getEtfs().values());
						break;
					case 4:
						etfi = ETF.COMMODITY_ETF.filter(etflist.getEtfs().values());
						break;
					case 5:	
						etfi = ETF.RECENTBUYS.filter(etflist.getEtfs().values());
						break;
					case 6:	
						etfi = ETF.BUYNOW.filter(etflist.getEtfs().values());
						break;
					case 7:	
						etfi = ETF.RECENTSELLS.filter(etflist.getEtfs().values());
						break;

					case 8:	
						etfi = ETF.SELLNOW.filter(etflist.getEtfs().values());
						break;
					}

					etfs.clear();
					for (ETF etf : etfi){
						etfs.add(etf);
					}

					Collections.sort(etfs,new ETFSortByTotal(passETFFilterType));
					filter = items[item].toString();
					Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
				}
			});
			return filterDialog.create();

		case DIALOG_LOAD_ID : 

			progressDialogETFTab = new ProgressDialog(main.this);
			progressDialogETFTab.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialogETFTab.setMax(100);
			progressDialogETFTab.setMessage("Loading ETF List");
			return progressDialogETFTab;

		case DIALOG_PORTFOLIO : 
			progressDialogPortfolioTab = new ProgressDialog(main.this);
			progressDialogPortfolioTab.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialogPortfolioTab.setMessage("Loading Portfolio");
			return progressDialogPortfolioTab;


		case DIALOG_STRATEGY :        
			li = LayoutInflater.from(this);
			filterView = li.inflate(R.layout.filter_view, null);

			filterDialog = new AlertDialog.Builder(this);
			filterDialog.setTitle("Trading Strategy");         
			filterDialog.setView(filterView);

			filterDialog.setItems(strategies, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					Iterable<ETF> etfi = null;

					switch(item){ 

					case 0:	
						passETFFilterType = ETFFilterType.R3;
						break;
					case 1:	
						passETFFilterType = ETFFilterType.RSI25;
						break;
					}

					filter="";
					if(etflist!=null && etflist.getEtfs()!=null){
						etfi = etflist.getEtfs().values();
						etfs.clear();
						for (ETF etf : etfi){
							etfs.add(etf);
						}
						Collections.sort(etfs,new ETFSortByTotal(passETFFilterType));
					}

					ETFList mylist = etfPortfolio;
					portfolioEtfs.clear();
					portfolioEtfs.addAll(mylist.getEtfs().values());
					Collections.sort(portfolioEtfs,new ETFSortByTotal(passETFFilterType));


					Toast.makeText(getApplicationContext(), passETFFilterType.name()+" strategy", Toast.LENGTH_SHORT).show();
				}
			});
			return filterDialog.create();
		}

		return null;
	}

	@Override
	public void onPrepareDialog(int id, Dialog dialog) {

		switch(id) {
		case DIALOG_LOAD_ID : 
			progressThreadETFTab = new ProgressThreadETFTab();
			progressThreadETFTab.start(); 
			break;

		case DIALOG_PORTFOLIO : 
			progressThreadPortfolioTab = new ProgressThreadPortfolioTab();
			progressThreadPortfolioTab.start();
			break;
		}

	}

	/** Nested class for ETFList load (all ETFs) */
	private class ProgressThreadETFTab extends Thread implements Progression {

		public void run() {
			//build and sort etf list 

			filter="";

			etflist = new ETFList();
			etflist.init(this);
			last = etflist;
			etfs.clear();
			etfs.addAll(etflist.getEtfs().values());
			Collections.sort(etfs,new ETFSortByTotal(passETFFilterType));
			progressDialogETFTab.dismiss();
			removeDialog(DIALOG_LOAD_ID);

		}

		public void progress(){
			progressDialogETFTab.incrementProgressBy(5);
		}
	}


	/** Nested class for ETF portfolio */
	private class ProgressThreadPortfolioTab extends Thread {

		public void run() {
			//build and sort etf list 
			etfPortfolio = new ETFPortfolio();
			preferences = getSharedPreferences(USER_PREFERENCE, Activity.MODE_PRIVATE);
			String myList = preferences.getString("PORTFOLIO", "FXI,ILF,MXI");

			etfPortfolio.init(myList);
			ETFList mylist = etfPortfolio;
			portfolioEtfs.clear();
			portfolioEtfs.addAll(mylist.getEtfs().values());
			Collections.sort(portfolioEtfs,new ETFSortByTotal(passETFFilterType));
			progressDialogPortfolioTab.dismiss(); 
			removeDialog(DIALOG_PORTFOLIO);
		}
	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus){

		if(hasFocus==true) 	{
			updateStatus();
			if (etfAdapter!=null){
				etfAdapter.notifyDataSetChanged();
			}
			if (portfolioAdapter!=null) portfolioAdapter.notifyDataSetChanged();
		}

	}


	@Override
	public void onDestroy() {      
		// Close the database
		super.onDestroy();
	}

	/* Creates the menu items */
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();

		TabHost mTabHost = getTabHost();
		int tab = mTabHost.getCurrentTab();

		MenuItem menuItem = menu.add(0, MENU_REFRESH, 0, "Refresh");
		menuItem.setIcon(R.drawable.refresh);
		menuItem = menu.add(0, MENU_STRATEGY, 0, "Strategy");
		menuItem.setIcon(R.drawable.search);
		if (tab == TAB_ETF){
			menuItem = menu.add(0, MENU_FILTER, 0, "Filter");
			menuItem.setIcon(R.drawable.filter);
		}

		return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {    

		TabHost mTabHost = getTabHost();
		int tab = mTabHost.getCurrentTab();

		switch (item.getItemId()) {    

		case MENU_REFRESH: 
			if (tab==TAB_PORTFOLIO){
				populatePortfolioTab(mTabHost);
			} else if (tab==TAB_ETF){
				populateETFTab(mTabHost);
			} 
			return true;    

		case MENU_FILTER: 
			showDialog(DIALOG_FILTER_ID);				
			return true;    

		case MENU_STRATEGY :
			showDialog(DIALOG_STRATEGY);				
			return true;    



		}    



		return false;

	}


	private void repopulateETFTab(){

		if(etflist!=null){
			Iterable<ETF> etfi = etflist.getEtfs().values();
			etfs.clear();
			for (ETF etf : etfi){
				etfs.add(etf);
			}
			Collections.sort(etfs,new ETFSortByTotal(passETFFilterType));
			myETFListView = (ListView) findViewById(R.id.myListView1);
			myETFListView.setOnItemClickListener(new OnItemClickListener() {
				//ETF List item click processing
				public void onItemClick(AdapterView<?> _av, View _v, int _index, long _id) {
					try{
						passETF = (ETF) _av.getItemAtPosition(_index);

						Intent intent = new Intent(main.this, TradeView.class);
						startActivity(intent);
					} 
					catch(Throwable t){
						t.printStackTrace();
					}
				}
			});
			//create an array adapter to bind the array to the list view
			int resID = R.layout.etflist_row;
			etfAdapter = new ETFListItemAdapter(this,resID, etfs);
			//bind it to the list view and notify the view to update
			myETFListView.setAdapter(etfAdapter);
			etfAdapter.notifyDataSetChanged();			
		} 

	}



	private void populateETFTab(TabHost mTabHost){

		mTabHost.setCurrentTab(0);

		myETFListView = (ListView) findViewById(R.id.myListView1);
		myETFListView.setOnItemClickListener(new OnItemClickListener() {
			//ETF List item click processing
			public void onItemClick(AdapterView<?> _av, View _v, int _index, long _id) {
				try{
					passETF = (ETF) _av.getItemAtPosition(_index);

					Intent intent = new Intent(main.this, TradeView.class);
					startActivity(intent);
				} 
				catch(Throwable t){
					t.printStackTrace();
				}
			}
		});
		//create an array adapter to bind the array to the list view
		int resID = R.layout.etflist_row;
		etfAdapter = new ETFListItemAdapter(this,resID, etfs);
		//bind it to the list view and notify the view to update
		myETFListView.setAdapter(etfAdapter);
		//show progress dialog
		showDialog(DIALOG_LOAD_ID);

	}

	private void populatePortfolioTab(TabHost mTabHost){

		mTabHost.setCurrentTab(1);

		myPortfolioListView = (ListView) findViewById(R.id.myListView2);
		myPortfolioListView.setOnItemClickListener(new OnItemClickListener() {
			//ETF List item click processing
			public void onItemClick(AdapterView<?> _av, View _v, int _index, long _id) {
				try{
					passETF = (ETF) _av.getItemAtPosition(_index);

					Intent intent = new Intent(main.this, TradeView.class);
					startActivity(intent);
				} 
				catch(Throwable t){
					t.printStackTrace();
				}

			}
		});
		//create an array adapter to bind the array to the list view
		int resID = R.layout.etflist_row;
		portfolioAdapter = new ETFListItemAdapter(this, resID, portfolioEtfs);
		//bind it to the list view and notify the view to update
		myPortfolioListView.setAdapter(portfolioAdapter);
		//show progress dialog
		showDialog(DIALOG_PORTFOLIO);

	}

	private void updateStatus(){
		TextView filterType = (TextView)findViewById(R.id.filterType);
		if(filter!=""){
			filterType.setText("Filter: " + filter + "                Strategy: " + passETFFilterType.name());

		}else{
			filterType.setText("Strategy: " + passETFFilterType.name());

		}

	}


}