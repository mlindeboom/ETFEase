package com.etfease.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.logging.Logger;

//Name,Symbol,Fund Type,MasterDATA Followed ETF
public class ETFCacheRefresh {
    private static final Logger log = Logger.getLogger(ETFCacheRefresh.class.getName());

	private static final String etflisturl = "http://etfease5.appspot.com/etflist?type=<ETFTYPE>";
	private static int index = 0;
	public void init(){
		String[] sa1 = {"USEquityETF1","USEquityETF2","USEquityETF3","USEquityETF4","USEquityETF5","GlobalEquityETF1","GlobalEquityETF2","FixedIncomeETF","CommodityBasedETF"};
		index++;
		if(index >= sa1.length)index=0;
//		Random rand = new Random();
//		index = rand.nextInt(sa1.length);	
		getData(etflisturl.replaceAll("<ETFTYPE>", sa1[index]));  
	}

	
	private void getData(String myUrl){
		System.out.println(">>Start:"+ new Date() + " " + myUrl);
		try {
			URL url = new URL(myUrl);

			URLConnection connection = url.openConnection(); 
			connection.setConnectTimeout(9999);
			//set the user agent and properties to accept xml
			//so restlet sends xml
			connection.setRequestProperty("Accept","application/xml;q=0.9");
			HttpURLConnection httpConnection = (HttpURLConnection)connection; 

			int responseCode = httpConnection.getResponseCode(); 
			InputStream in=null;
			if (responseCode == HttpURLConnection.HTTP_OK) {
				in = httpConnection.getInputStream();
				System.out.println(">>OK:"+in.available());
				
			} else {
				System.out.println(">>Not OK:"+responseCode);
			}

		} catch (MalformedURLException e) {
			System.out.println(">>Not OK:"+e.getMessage());
		} catch (IOException e) {
			System.out.println(">>Not OK:"+e.getMessage());
		} catch (Exception e) {
			System.out.println(">>Not OK:"+e.getMessage());
		}
		
		finally {
			System.out.println(">>Finish:"+ new Date() + " " + myUrl);
		}
		
	}
	
	
	
	class URLThread extends Thread{
		
		String url="";
		URLThread(String url){
			this.url=url;
			
		}

		public void run(){
			ETFCacheRefresh.this.getData(url);
		}
		
	}
	
	
}
