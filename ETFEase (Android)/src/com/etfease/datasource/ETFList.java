package com.etfease.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.util.Log;

import com.etfease.android.app.Progression;
import com.etfease.xml.Parser;

//Name,Symbol,Fund Type,MasterDATA Followed ETF
public class ETFList {
	Progression progression=null;
	ConcurrentHashMap<String, ETF> etfs = new ConcurrentHashMap<String, ETF>();
	private static final String etflisturl = "http://etfease5.appspot.com/etflist?type=<ETFTYPE>";
	
	
	public ETFList(){
		System.out.println("new etflist");
	}
	
	public void init(Progression p){
		progression = p;
		String[] sa1 = {"USEquityETF1","USEquityETF2","USEquityETF3","USEquityETF4"};
		URLThread[] urltread = new URLThread[sa1.length];
        for (int i=0; i<sa1.length; i++){
        	urltread[i] = new URLThread(etflisturl.replaceAll("<ETFTYPE>", sa1[i]));
        	urltread[i].start();
        	//getData(etflisturl.replaceAll("<ETFTYPE>", etfType.name()));  
        } 

        for(int j=0; j<urltread.length;j++){
        	try{
        		urltread[j].join();
        	}
			catch (InterruptedException e) {
				System.out.print("Join interrupted\n");
			}
        }

        String[] sa2 = {"USEquityETF5","GlobalEquityETF1","GlobalEquityETF2","FixedIncomeETF","CommodityBasedETF"};
		urltread = new URLThread[sa2.length];
        for (int i=0; i<sa2.length; i++){
        	urltread[i] = new URLThread(etflisturl.replaceAll("<ETFTYPE>", sa2[i]));
        	urltread[i].start();
        	//getData(etflisturl.replaceAll("<ETFTYPE>", etfType.name()));  
        } 

        for(int j=0; j<urltread.length;j++){
        	try{
        		urltread[j].join();
        	}
			catch (InterruptedException e) {
				System.out.print("Join interrupted\n");
			}
        }
        
        System.out.println("Done");
	
	}

	
	private void getData(String myUrl){


		
		try {
			//Use 10.0.2.2 to communicate with Google App Engine running in local host
			//127.0.0.1 is used by Android emulator
			//URL url = new URL("http://10.0.2.2:8888/etflist");
			URL url = new URL(myUrl);

			URLConnection connection = url.openConnection(); 
			//set the user agent and properties to accept xml
			//so restlet sends xml
			connection.setRequestProperty("Accept","application/xml;q=0.9");
			HttpURLConnection httpConnection = (HttpURLConnection)connection; 

			int responseCode = httpConnection.getResponseCode(); 
			InputStream in=null;
			progression.progress();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				Log.i("getdata","Read:"+myUrl);
				in = httpConnection.getInputStream(); 
				Log.i("getdata","Data returned:"+myUrl+ " -- start parsing");
				Parser parser= new Parser(this);
				parser.parse(in);
				Log.i("getdata","Parse completed:"+myUrl+ "");
			}

		} catch (MalformedURLException e) {
			Log.i("getdata","exception:"+myUrl+ " " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("getdata","exception:"+myUrl+ " " + e.getMessage());
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			Log.i("getdata","exception:"+myUrl+ " " + e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			Log.i("getdata","exception:"+myUrl+ " " + e.getMessage());
			e.printStackTrace();
		}
		catch (Throwable e) {
			Log.i("getdata","exception:"+myUrl+ " " + e.getMessage());
			e.printStackTrace();
		}
		
		finally {
			progression.progress();
		}
		
	}
	
	
	/**
	 * @return the etfs
	 */
	public ConcurrentHashMap<String, ETF> getEtfs() {
		return etfs;
	}
	
	class URLThread extends Thread{
		
		String url="";
		URLThread(String url){
			this.url=url;
			
		}

		public void run(){
			Log.i("getdata","Trying "+url);
			ETFList.this.getData(url);
		}
		
	}
	
	
}
