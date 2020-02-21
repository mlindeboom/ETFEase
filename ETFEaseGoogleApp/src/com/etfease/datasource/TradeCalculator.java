package com.etfease.datasource;


public class TradeCalculator {

	private ETFFilter etfFilter;
	private float tradeAmt;
	private float cost;
	private float trades[];

	public TradeCalculator(ETFFilter etfFilter, float tradeAmt, float cost){
		this.tradeAmt = tradeAmt;
		this.etfFilter = etfFilter;
		this.cost = cost;
	}


	public Trades calculate(){
		Trades trades = new Trades();
		int count = etfFilter.getBuys().length;
		float prices[] = etfFilter.getETF().getPriceHistory();
		trades.setSymbol(etfFilter.getETF().getSymbol());
		
		float shares=0;
		float moneyRemaining=0;
		
		for(int i=count-1;i>-1;i--){
			if(etfFilter.getBuys()[i] && shares==0) {
				shares = (tradeAmt-cost)/prices[i];
				float fractionalShares = shares - (int)shares;
				moneyRemaining = fractionalShares*prices[i];
				shares=shares-fractionalShares;
                //Log.i("TradeCalculator", "Buy: " + shares + " at " + prices[i] + " on " + etfFilter.getETF().getPriceDate()[i]);
				trades.addTrade(shares, tradeAmt, moneyRemaining, cost, etfFilter.getETF().getPriceDate()[i]);
			}
			if(etfFilter.getSells()[i]) {
				tradeAmt = (shares*prices[i])-cost+moneyRemaining;
				//Log.i("TradeCalculator", "Sell: " + shares + " at "+ prices[i]+" for " +tradeAmt +  " on " + etfFilter.getETF().getPriceDate()[i]);
				trades.addTrade(-shares, tradeAmt, moneyRemaining, cost, etfFilter.getETF().getPriceDate()[i]);
				shares=0;
			}
		}
		trades.setTotal(tradeAmt);
		trades.setEtfFilterType(etfFilter.getEtfFilterType());
		return trades;

	}


}
