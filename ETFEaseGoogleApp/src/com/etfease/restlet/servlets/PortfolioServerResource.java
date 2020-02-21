package com.etfease.restlet.servlets;

import java.util.HashMap;
import java.util.Set;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.etfease.datasource.ETF;
import com.etfease.datasource.ETFList;
import com.etfease.datasource.ETFType;
import com.etfease.datasource.Trades;

public class PortfolioServerResource extends ServerResource {
	//implements PortfolioResource {

	private ETFList etfList = ETFList.getInstance();

	@Get  
	public ETFList getETFList() {  

		Iterable<ETF> etfi = null;
		String s1 = (String) getReference().getRemainingPart();
		int pos = s1.indexOf("=");
		String s2[] = new String[0];
		//?symbols=FXI|ILF|EWW|XXX
		if (pos!=-1) s2 = s1.substring(pos+1).split(",");

		//create a new local etf list using filtered results
		ETFList myEtfList = new ETFList();
		HashMap <String, ETF> etfs = myEtfList.getEtfs();

		for(int i=0; i<s2.length; i++){
			ETF myETF = etfList.getEtfs().get(s2[i]);
			if (myETF!=null) etfs.put(myETF.getSymbol(),myETF);
		}


		//move through the etf list and refresh the trade data from memory
		Set <String> symbols = myEtfList.getEtfs().keySet();
		if (symbols.iterator().hasNext()){

			for(String symbol: symbols){
				ETF etf = myEtfList.getEtfs().get(symbol);
				etf.refreshTrades(Trades.BIG);
				etf.minimize();
			}
		}

		/*etfi = ETF.CUTOFF.filter(myEtfList.getEtfs().values());
		myEtfList = new ETFList();
		etfs = myEtfList.getEtfs();
		for (ETF etf : etfi){
			etfs.put(etf.getSymbol(),etf);
		}*/

		return myEtfList;
	}  
}
