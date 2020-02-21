package com.etfease.restlet.servlets;

import org.restlet.resource.Get;

import com.etfease.datasource.ETFList;

public interface PortfolioResource {
	@Get  
	public String getETFList();  

}
