package com.etfease.xml;

// JDK Classes
import java.io.CharArrayWriter;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.etfease.datasource.Trade;

public class Tradehandler extends DefaultHandler
{
	private CharArrayWriter text = new CharArrayWriter ();
	private Stack path;
	private Map params;
	private DefaultHandler parent;
	private SAXParser parser;
	private Trade trade;

	public Tradehandler(Stack path, Map params, Attributes attributes, SAXParser parser, DefaultHandler parent)  throws SAXException
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


	public void startshares (Attributes attributes) throws SAXException
	{

	}


	public void endshares () throws SAXException
	{
		trade.setShares(Float.valueOf(getText()));
	}



	public void starttradeAmount (Attributes attributes) throws SAXException
	{

	}


	public void endtradeAmount () throws SAXException
	{
		trade.setTradeAmount(Float.valueOf(getText()));
	}



	public void startfee (Attributes attributes) throws SAXException
	{

	}


	public void endfee () throws SAXException
	{
		trade.setFee(Float.valueOf(getText()));
	}



	public void startdate (Attributes attributes) throws SAXException
	{

	}


	public void enddate () throws SAXException
	{
		trade.setDate(getText());
	}



	public void startremainingAmount (Attributes attributes) throws SAXException
	{
	}


	public void endremainingAmount () throws SAXException
	{
		trade.setRemainingAmount(Float.valueOf(getText()));
	}




	public String getText()
	{
		return text.toString().trim();
	}

	public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, Attributes attributes) throws SAXException
	{
		text.reset();
		trade = (Trade)path.peek();

		if (localName.equals("shares")) startshares (attributes);

		if (localName.equals("tradeAmount")) starttradeAmount (attributes);

		if (localName.equals("fee")) startfee (attributes);

		if (localName.equals("date")) startdate (attributes);

		if (localName.equals("remainingAmount")) startremainingAmount (attributes);

	}

	public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) throws SAXException
	{
		if (localName.equals("com.etfease.datasource.Trade"))
		{
			end();
			path.pop();
			parser.getXMLReader().setContentHandler (parent);
		}


		if (localName.equals("shares")) endshares ();



		if (localName.equals("tradeAmount")) endtradeAmount ();



		if (localName.equals("fee")) endfee ();



		if (localName.equals("date")) enddate ();



		if (localName.equals("remainingAmount")) endremainingAmount ();



	}

	public void characters(char[] ch, int start, int length)
	{
		text.write (ch,start,length);
	}


}