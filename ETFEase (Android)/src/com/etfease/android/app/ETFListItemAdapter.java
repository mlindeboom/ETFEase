package com.etfease.android.app;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etfease.datasource.ETF;
import com.etfease.datasource.Trade;

public class ETFListItemAdapter extends ArrayAdapter<ETF> {
	
	int resource;
	
	public ETFListItemAdapter(Context context, int textViewResourceId,
			List objects) {
		super(context, textViewResourceId, objects);
		
		resource = textViewResourceId;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent){

		LinearLayout etfView;
		ETF etf = getItem(position);
		
		String symbol = etf.getSymbol();
		String name = etf.getName();
		String total = "Growth " + main.passETFFilterType.name() + "  " + "<font color=green><B>" + Round(etf.getTrades(main.passETFFilterType).getTotal(),2) + "</B></font>";
		String transaction = "";
		//check for any buy recommendations
		if(etf.getTrades(main.passETFFilterType).isRecentBuy()) transaction = ("Buy recommended on "+etf.getTrades(main.passETFFilterType).getLastTradeDate());
		//check for any sell recommendations
		if(etf.getTrades(main.passETFFilterType).isRecentSell()) transaction = ("Sell recommended on "+etf.getTrades(main.passETFFilterType).getLastTradeDate());

		
		/*		Trade t = etf.recentBuy();
		if(t!=null){
			transaction = ("Buy recommended on ");
			transaction+=t.getDate();
		}
		//check for any sell recommendations
		t = etf.recentSell();
		if(t!=null){
			transaction = ("Sell recommended on ");
			transaction+=t.getDate();
		}
*/
		if(convertView == null){
			etfView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, etfView, true);
		} else {
			etfView = (LinearLayout) convertView;
		}

		TextView nameView = (TextView)etfView.findViewById(R.id.secondLine);
		TextView symbolView = (TextView)etfView.findViewById(R.id.firstLine);
		nameView.setText(Html.fromHtml(name+"<BR>"+ total +"<BR>"+transaction));
		
		
		symbolView.setText(Html.fromHtml("<b><big>" + symbol + "</big></b>"));
		
		
		return etfView;
	}
	
	public float Round(float Rval, int Rpl) {
		float p = (float)Math.pow(10,Rpl);
		Rval = Rval * p;
		float tmp = Math.round(Rval);
		return (float)tmp/p;
	}

	
	

}
