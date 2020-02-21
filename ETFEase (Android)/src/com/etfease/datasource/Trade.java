package com.etfease.datasource;


public class Trade{

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
		return Round(shares, 0);
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
		return Round(tradeAmount, 2);
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
		return Round(remainingAmount,2); 
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
		sb.append(" shares:");
		sb.append(Round(this.shares,0));
		//sb.append(" amt:");
		//sb.append(this.tradeAmount);
		//sb.append(" cost:");
		//sb.append(this.fee);
		sb.append(" date:");
		sb.append(this.date);
		//sb.append(" remaing:");
		//sb.append(this.remainingAmount);

		return sb.toString();
	}

	public float Round(float Rval, int Rpl) {
		float p = (float)Math.pow(10,Rpl);
		Rval = Rval * p;
		float tmp = Math.round(Rval);
		return (float)tmp/p;
	}

}

