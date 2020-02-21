/*
 * Copyright 2003 Jayson Falkner (jayson@jspinsider.com)
 * This code is from "Servlets and JavaServer pages; the J2EE Web Tier",
 * http://www.jspbook.com. You may freely use the code both commercially
 * and non-commercially. If you like the code, please pick up a copy of
 * the book and help support the authors, development of more free code,
 * and the JSP/Servlet/J2EE community.
 */
package com.etfease.filter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.memcache.MemcacheService;

public class CacheFilter implements Filter {
	ServletContext sc;
	FilterConfig fc;
	long cacheTimeout = Long.MAX_VALUE;

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain)
	throws IOException, ServletException {
		try{
			
		CachedPage ncp = new CachedPage();
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		Cache cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap()); 


		// check if was a resource that shouldn't be cached.
		String r = sc.getRealPath("");
		String path = fc.getInitParameter(request.getRequestURI());
		if (path!= null && path.equals("nocache")) {
			chain.doFilter(request, response);
			return;
		}
		path = r+path;

		// customize to match parameters
		String key = request.getRequestURI()+request.getQueryString()+request.getHeader("Accept");
		
		// optionally append i18n sensitivity
		String localeSensitive = fc.getInitParameter("locale-sensitive");
		if (localeSensitive != null) {
			StringWriter ldata = new StringWriter();
			Enumeration locales = request.getLocales();
			while (locales.hasMoreElements()) {
				Locale locale = (Locale)locales.nextElement();
				ldata.write(locale.getISO3Language());
			}
			key = key + ldata.toString();
		}

		long now = Calendar.getInstance().getTimeInMillis();

		//check that entry does not exist or that it is expired -- save new copy if either are true 
		CachedPage cp = (CachedPage)cache.get(key);
		if(cp==null || cacheTimeout < now - cp.getDatetime().getTime()){

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			CacheResponseWrapper wrappedResponse =
				new CacheResponseWrapper(response, baos);
			chain.doFilter(req, wrappedResponse);
			//write to cache
			ncp.setPage(baos.toByteArray());
			ncp.setDatetime(new Date());
			cache.put(key, ncp);
			cp = ncp;
		}

		String mt = sc.getMimeType(request.getRequestURI());
		response.setContentType(mt);
		ServletOutputStream sos = res.getOutputStream();

		sos.write(cp.getPage());
		System.out.println("cached:"+key);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void init(FilterConfig filterConfig) {
		this.fc = filterConfig;
		String ct = fc.getInitParameter("cacheTimeout");
		if (ct != null) {
			cacheTimeout = 60*1000*Long.parseLong(ct);
		}
		this.sc = filterConfig.getServletContext();
	}

	public void destroy() {
		this.sc = null;
		this.fc = null;
	}
}
