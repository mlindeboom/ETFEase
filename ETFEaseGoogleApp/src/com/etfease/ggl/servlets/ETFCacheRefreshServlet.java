package com.etfease.ggl.servlets;
import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etfease.datasource.ETFCacheRefresh;
import com.etfease.datasource.ETFList;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions.Method;




public class ETFCacheRefreshServlet extends HttpServlet{

	private ETFList etfList = ETFList.getInstance();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {

		
		ETFCacheRefresh ecr = new ETFCacheRefresh();
		ecr.init();
		
		resp.setContentType("text/plain");
		resp.getWriter().println("Done");
	}
}


