package com.etfease.xml;

// JDK Classes
import java.io.CharArrayWriter;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.etfease.datasource.ETF;
import com.etfease.datasource.ETFFilterType;
import com.etfease.datasource.Trades;

public class Tradeshandler extends DefaultHandler
{
	private CharArrayWriter text = new CharArrayWriter ();
	private Stack path;
	private Map params;
	private DefaultHandler parent;
	private SAXParser parser;
	private ETF etf;
	private Trades trades;

	public Tradeshandler(Stack path, Map params, Attributes attributes, SAXParser parser, DefaultHandler parent)  throws SAXException
	{
		this.path = path;
		this.params = params;
		this.parent = parent;
		this.parser = parser;
		start(attributes);
	}


	public void start (Attributes attributes)  throws SAXException
	{
	}

	public void end () throws SAXException
	{
	}


	public void startetfFilterType (Attributes attributes) throws SAXException
	{

	}


	public void endetfFilterType () throws SAXException
	{
		ETFFilterType etfFilterType = ETFFilterType.valueOf(getText());
		trades = etf.getTrades(etfFilterType);
		trades.setEtfFilterType(etfFilterType);
		etf.addTrades(trades);
	}



	public void starttotal (Attributes attributes) throws SAXException
	{

	}


	public void endtotal () throws SAXException
	{
		trades.setTotal(Float.valueOf(getText()));

	}

	

	public void starttrades (Attributes attributes) throws SAXException
	{

	}

	public void startRecentBuy (Attributes attributes) throws SAXException{}
	public void endRecentBuy () throws SAXException{
		trades.setRecentBuy(Boolean.valueOf(getText()));
	}
	public void startRecentSell (Attributes attributes) throws SAXException{}
	public void endRecentSell () throws SAXException{
		trades.setRecentSell(Boolean.valueOf(getText()));
	}
	public void startSellNow (Attributes attributes) throws SAXException{}
	public void endSellNow () throws SAXException{
		trades.setSellNow(Boolean.valueOf(getText()));
	}
	public void startBuyNow (Attributes attributes) throws SAXException{}
	public void endBuyNow () throws SAXException{
		trades.setBuyNow(Boolean.valueOf(getText()));
	}
	public void startLastTradeDate (Attributes attributes) throws SAXException{}
	public void endLastTradeDate () throws SAXException{
		trades.setLastTradeDate(getText());
	}

	public String getText()
	{
		return text.toString().trim();
	}

	public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, Attributes attributes) throws SAXException
	{
		text.reset();
		etf = (ETF)path.peek();



		if (localName.equals("etfFilterType")) startetfFilterType (attributes);

		if (localName.equals("total")) starttotal (attributes);

		if (localName.equals("recentBuy")) startRecentBuy (attributes);
		if (localName.equals("recentSell")) startRecentSell (attributes);
		if (localName.equals("buyNow")) startBuyNow (attributes);
		if (localName.equals("sellNow")) startSellNow (attributes);
		if (localName.equals("lastTradeDate")) startLastTradeDate (attributes);


		if (localName.equals("trades"))
		{
			starttrades (attributes);
			path.push(trades.getTrades());
			DefaultHandler handler = new tradeshandlerx(path,params,attributes,parser,this);
			parser.getXMLReader().setContentHandler (handler);
		}


	}

	public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) throws SAXException
	{
		if (localName.equals("com.etfease.datasource.Trades"))
		{
			end();
			path.pop();
			parser.getXMLReader().setContentHandler (parent);
		}


		if (localName.equals("etfFilterType")) endetfFilterType ();



		if (localName.equals("total")) endtotal ();

		if (localName.equals("recentBuy")) endRecentBuy ();
		if (localName.equals("recentSell")) endRecentSell ();
		if (localName.equals("buyNow")) endBuyNow ();
		if (localName.equals("sellNow")) endSellNow ();
		if (localName.equals("lastTradeDate")) endLastTradeDate();




	}

	public void characters(char[] ch, int start, int length)
	{
		text.write (ch,start,length);
	}


}