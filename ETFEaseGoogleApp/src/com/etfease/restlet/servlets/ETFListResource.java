package com.etfease.restlet.servlets;

import org.restlet.resource.Get;

import com.etfease.datasource.ETFList;

public interface ETFListResource {
	@Get  
	public String getETFList();  

}
