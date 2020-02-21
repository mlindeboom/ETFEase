package com.etfease.ggl.servlets;
import java.io.IOException;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etfease.datasource.ETF;
import com.etfease.datasource.ETFFilter;
import com.etfease.datasource.ETFFilterType;
import com.etfease.datasource.ETFList;
import com.etfease.datasource.PMF;
import com.etfease.datasource.TradeCalculator;
import com.etfease.datasource.Trades;





public class ETFCollectionTaskServlet extends HttpServlet{

	private ETFList etfList = ETFList.getInstance();
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		doGetProcess( req,  resp);
	}


	
	public void doGetProcess(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {

		Date start = new Date();
		long startms =  start.getTime();
		String symbol = req.getParameter("symbol");
		try{
	        PersistenceManager pm = PMF.get().getPersistenceManager();
	        //pm.currentTransaction().begin();
			ETF etf = etfList.getEtfs().get(symbol);
			System.out.println(etf);
			etf.syncETF();
	        try {
	            if(!etf.isPersistant()) pm.makePersistent(etf);
		        //pm.currentTransaction().commit();
	        } finally {
	            pm.close();
	        }
			etf.minimize();
			//etf reference has changed -- update the list (threading - may need to sync here)
			etfList.getEtfs().put(symbol, etf);
		}catch (Throwable e){
			e.printStackTrace();
		}

		Date finish = new Date();
		long finishms =  finish.getTime();
		
		long elapse = finishms-startms;
		resp.setContentType("text/plain");
		resp.getWriter().println("Done " + elapse);
	}
}


