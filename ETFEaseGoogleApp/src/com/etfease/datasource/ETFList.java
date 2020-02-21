package com.etfease.datasource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;

//Name,Symbol,Fund Type,MasterDATA Followed ETF
public class ETFList  {
	private static ETFList instance = null;
	private static String etfListUrl="http://www.masterdata.com/HelpFiles/ETF_List_Downloads/AllETFs.csv";
	private HashMap<String, ETF> etfs = new HashMap<String, ETF>();

	public static ETFList getInstance(){
	      if(instance == null) {
	          instance = new ETFList();
	          instance.init();
	       }
	       return instance;
	}
	
	private void init() {
		try{
			URL url = new URL(etfListUrl);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			in.readLine();//skip first line
			String line;
			while ((line = in.readLine()) != null) {
				
				try{
					//Name,Symbol,Fund Type,MasterDATA Followed ETF
					String s[] = line.split(",");
					ETF etf = new ETF();
					etf.setName(s[0]);
					etf.setSymbol(s[1]);

					String category = s[2].replace(" ", "").replace("-", "");

					etf.setEtfType(ETFType.valueOf(category));
					
					//if (s[1].equalsIgnoreCase("ILF")) etfs.put(etf.getSymbol(),etf);
					//if (etf.getEtfType().equals(ETFType.USEquityETF)) etfs.put(etf.getSymbol(),etf);
					//if (etf.getEtfType().equals(ETFType.NonUSEquityETF)) etfs.put(etf.getSymbol(),etf);
					etfs.put(etf.getSymbol(),etf);

				} catch (Throwable t){
					//do nothing
				}
			}
			in.close();		
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	

	
	/**
	 * @return the etfs
	 */
	public HashMap<String, ETF> getEtfs() {
		return etfs;
	}
}
