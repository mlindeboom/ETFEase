package com.etfease.ggl.servlets;
import java.io.IOException;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etfease.datasource.ETF;
import com.etfease.datasource.ETFList;
import com.etfease.datasource.PMF;






public class ETFCollectionCleanupServlet extends HttpServlet{

	private ETFList etfList = ETFList.getInstance();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {

		Date start = new Date();
		long startms =  start.getTime();
		try{
	        PersistenceManager pm = PMF.get().getPersistenceManager();
	        try {
	            Query query = pm.newQuery(ETF.class); 
	            query.setFilter("etfType!=\"X\""); 
	            query.deletePersistentAll();
	            
	        } finally {
	            pm.close();
	        }
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


