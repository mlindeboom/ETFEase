package com.etfease.datasource;


/*
 * RSI
 * Take change field for 2 pts before current
 * if change is positive -> Advance
 * if change is negative -> Decline
 * Avg all advances -> AdvanceAvg
 * Avg all declines -> DeclineAvg
 * RS=AdvanceAvg/DeclineAvg
 * RSI = 100-(100/(1+RS)) if DeclineAvg=0 RSI=100
 * 
 */
public class RSI {
	private int period;
	private float[] changeHistory;
	private String[] changeDate;
	private float[] priceHistory;
	private float[] rsiHistory;
	
	public RSI(int period, float[] changeHistory, float[] priceHistory, String[] changeDate) {
		super();
		this.period = period;
		this.changeHistory = changeHistory;
		this.priceHistory = priceHistory;
		this.changeDate = changeDate;
		this.changeHistory = changeHistory;
		rsiHistory = new float[changeHistory.length];
	}


	public float[] getRSIHistory(){
		float advanceSum=0, declineSum=0; 
		float advanceAvg=0, declineAvg = 0;
		//calculate the first average
		for(int i=changeHistory.length-1; i>=changeHistory.length-period; i--){
			if(changeHistory[i]<0){
				declineSum+=Math.abs(changeHistory[i]);
			}
			else{	
				advanceSum+=changeHistory[i];
			}
		}
		
		advanceAvg = advanceSum/period;
		declineAvg = declineSum/period;
		
		//calculate running average
		for(int i=changeHistory.length-1; i>0; i--){
			if(changeHistory[i]<0){
				declineAvg = (declineAvg*(period-1)+Math.abs(changeHistory[i]))/period;
				advanceAvg = advanceAvg*(period-1)/period;
				//System.out.println(changeDate[i]+" "+priceHistory[i] +" "+changeHistory[i]+" "+advanceAvg+" "+declineAvg);
			}
			else{	
				advanceAvg = (advanceAvg*(period-1)+changeHistory[i])/period;
				declineAvg = declineAvg*(period-1)/period;
				//System.out.println(changeDate[i]+" "+priceHistory[i]+" "+changeHistory[i]+" "+advanceAvg+" "+declineAvg);
			}
			if (declineAvg==0)
				rsiHistory[i-1] = 100;
			else {
				float rs = advanceAvg/declineAvg;
				rsiHistory[i-1] = 100-(100/(1+rs)); 
			}
		}
		return rsiHistory;
	}
}
