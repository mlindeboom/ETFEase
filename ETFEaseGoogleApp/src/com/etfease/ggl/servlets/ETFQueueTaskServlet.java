package com.etfease.ggl.servlets;
import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etfease.datasource.ETFList;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions.Method;




public class ETFQueueTaskServlet extends HttpServlet{

	private ETFList etfList = ETFList.getInstance();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {

		Collection <String> etfSymbols = etfList.getEtfs().keySet();
		
		if(etfSymbols.iterator().hasNext()){
			for (String symbol : etfSymbols){
				try{
			        Queue queue = QueueFactory.getQueue("etfease");			        
			        
			        queue.add(url("/etfeasegoogleapp/refresh?symbol="+symbol).method(Method.GET));			
				}catch (Exception e){
					//
				}
				
			}
		}
		
		

		
		resp.setContentType("text/plain");
		resp.getWriter().println("Done");
	}
}


