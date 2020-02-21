package com.etfease.datasource;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;

public class quotes {


	public static void main(String[] args) {
		try{
			InputStream is = null;
			DataInputStream dis;
			String myurl="http://finance.yahoo.com/d/quotes.csv?s=<symbolslist>&f=<taglist>";
			String taglist = "m4";//"sghm4X";
			String symbolslist = "SPY+QQQQ+GLD";
			
			myurl = myurl.replace("<taglist>", taglist);			
			myurl = myurl.replace("<symbolslist>", symbolslist);			
			URL url = new URL(myurl);
			is = url.openStream();         // throws an IOException
			dis = new DataInputStream(new BufferedInputStream(is));

			String s="";
			while ((s = dis.readLine()) != null) {
			//	System.out.println(s);
			}
			
			/*
			 * 
			 * s - This is where you can specify your stock quote, if you want to download stock quote for Microsoft, just enter it as 's=MSFT'
			 * a - This parameter is to get the input for the start month. '00' is for January, '01' is for February and so on.
			 * b - This parameter is to get the input for the start day, this one quite straight forward, '1' is for day one of the month, '2' is for second day of the month and so on.
			 * c - This parameter is to get the input for the start year
			 * d - This parameter is to get the input for end month, and again '00' is for January, '02' is for February and so on.
			 * e - This parameter is to get the input for the end day
			 * f - This parameter is to get the input for the end year
			 * g - This parameter is to specify the interval of the data you want to download. 'd' is for daily, 'w' is for weekly and 'm' is for monthly prices. The default is 'daily' if you ignore this parameter.
			 * 
			 */
			
			myurl="http://ichart.finance.yahoo.com/table.csv?s=QQQQ";
			url = new URL(myurl);
			is = url.openStream();         // throws an IOException
			dis = new DataInputStream(new BufferedInputStream(is));
			while ((s = dis.readLine()) != null) {
				System.out.println(s);
			}
			
			

			
		}catch(Throwable t){
			t.printStackTrace();
		}

	}




}
