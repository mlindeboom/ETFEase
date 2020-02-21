package com.etfease.filter;

import java.io.Serializable;
import java.util.Date;

public class CachedPage implements Serializable {
	
	private byte[] page;
	private Date datetime;

	
	public byte[] getPage() {
		return page;
	}
	public void setPage(byte[] page) {
		this.page = page;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

}
