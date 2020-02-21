package com.etfease.restlet.servlets;

import java.util.HashMap;
import java.util.Set;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.etfease.datasource.ETF;
import com.etfease.datasource.ETFList;
import com.etfease.datasource.ETFType;
import com.etfease.datasource.Trades;

public class ETFListServerResource extends ServerResource {
	//implements ETFListResource {

	private ETFList etfList = ETFList.getInstance();
	
	@Get  
	public ETFList getETFList() {  

		Iterable<ETF> etfi = null;
		String s = (String) getReference().getRemainingPart();
		if(s.endsWith(ETFType.CommodityBasedETF.name()))
			etfi = ETF.COMMODITY_ETF.filter(etfList.getEtfs().values());
		else if(s.endsWith(ETFType.FixedIncomeETF.name()))
			etfi = ETF.BOND_ETF.filter(etfList.getEtfs().values());
		else if(s.endsWith(ETFType.GlobalEquityETF.name()+"1"))
			etfi = ETF.NON_US_ETF1.filter(etfList.getEtfs().values());
		else if(s.endsWith(ETFType.GlobalEquityETF.name()+"2"))
			etfi = ETF.NON_US_ETF2.filter(etfList.getEtfs().values());
		else if(s.endsWith(ETFType.USEquityETF.name()))
			etfi = ETF.US_ETF.filter(etfList.getEtfs().values());
		else if(s.endsWith(ETFType.USEquityETF.name()+"1"))
			etfi = ETF.US_ETF1.filter(etfList.getEtfs().values());
		else if(s.endsWith(ETFType.USEquityETF.name()+"2"))
			etfi = ETF.US_ETF2.filter(etfList.getEtfs().values());
		else if(s.endsWith(ETFType.USEquityETF.name()+"3"))
			etfi = ETF.US_ETF3.filter(etfList.getEtfs().values());
		else if(s.endsWith(ETFType.USEquityETF.name()+"4"))
			etfi = ETF.US_ETF4.filter(etfList.getEtfs().values());
		else if(s.endsWith(ETFType.USEquityETF.name()+"5"))
			etfi = ETF.US_ETF5.filter(etfList.getEtfs().values());
		else
			etfi = etfList.getEtfs().values();
		

		//create a new local etf list using filtered results
		ETFList myEtfList = new ETFList();
		HashMap <String, ETF> etfs = myEtfList.getEtfs();
		for (ETF etf : etfi){
			etfs.put(etf.getSymbol(),etf);
		}

		//move through the etf list and refresh the trade data from memory
		Set <String> symbols = myEtfList.getEtfs().keySet();
		if (symbols.iterator().hasNext()){

			for(String symbol: symbols){
				ETF etf = myEtfList.getEtfs().get(symbol);
				etf.refreshTrades(Trades.SMALL);
				etf.minimize();
			}
		}
		
		/*etfi = ETF.CUTOFF.filter(myEtfList.getEtfs().values());
		myEtfList = new ETFList();*/
		etfs = myEtfList.getEtfs();
		for (ETF etf : etfi){
			etfs.put(etf.getSymbol(),etf);
		}

		return myEtfList;
	}  
}
