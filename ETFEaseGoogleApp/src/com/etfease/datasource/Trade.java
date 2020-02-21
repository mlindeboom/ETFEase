package com.etfease.datasource;

import java.io.Serializable;



class Trade implements Serializable{
	
	private static final long serialVersionUID = -1L;
	
	float shares;
	float tradeAmount;
	float fee;
	String date;
	float remainingAmount;

	public Trade(float shares, float tradeAmount, float remainingAmount, float fee, String date){
		this.shares = shares;
		this.tradeAmount = tradeAmount;
		this.fee = fee;
		this.date = date;
		this.remainingAmount = remainingAmount;
	}
	
	
	/**
	 * @return the shares
	 */
	public float getShares() {
		return shares;
	}
	/**
	 * @param shares the shares to set
	 */
	public void setShares(float shares) {
		this.shares = shares;
	}
	/**
	 * @return the fee
	 */
	public float getFee() {
		return fee;
	}
	/**
	 * @param fee the fee to set
	 */
	public void setFee(float fee) {
		this.fee = fee;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}


	/**
	 * @return the tradeAmount
	 */
	public float getTradeAmount() {
		return tradeAmount;
	}


	/**
	 * @param tradeAmount the tradeAmount to set
	 */
	public void setTradeAmount(float tradeAmount) {
		this.tradeAmount = tradeAmount;
	}


	/**
	 * @return the remainingAmount
	 */
	public float getRemainingAmount() {
		return remainingAmount;
	}


	/**
	 * @param remainingAmount the remainingAmount to set
	 */
	public void setRemainingAmount(float remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	
	@Override
	public String toString(){
		
		StringBuffer sb = new StringBuffer();
		sb.append("shares ");
		sb.append(this.shares);
		sb.append("amt ");
		sb.append(this.tradeAmount);
		sb.append("cost ");
		sb.append(this.fee);
		sb.append("date ");
		sb.append(this.date);
		sb.append("remaing ");
		sb.append(this.remainingAmount);
		
		return sb.toString();
	}
	
}

