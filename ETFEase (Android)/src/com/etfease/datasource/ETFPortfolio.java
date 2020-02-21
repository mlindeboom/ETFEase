package com.etfease.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.etfease.xml.Parser;

//Name,Symbol,Fund Type,MasterDATA Followed ETF
public class ETFPortfolio extends ETFList {

	//HashMap<String, ETF> etfs = new HashMap<String, ETF>();
	private static final String etflisturl = "http://etfease5.appspot.com/portfoliolist?symbol=<PORTFOLIO>";
	public static final String PREFS_NAME = "PortfolioFile";
	
	public void init(String commaDelimitedEtfSymbols){
        	getData(etflisturl.replaceAll("<PORTFOLIO>", commaDelimitedEtfSymbols));
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
			if (responseCode == HttpURLConnection.HTTP_OK) { 
				in = httpConnection.getInputStream(); 

				Parser parser= new Parser(this);
				parser.parse(in);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		finally {
		}
		
	}
}
