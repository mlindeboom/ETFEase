package com.etfease.xml;

// JDK Classes
import java.io.CharArrayWriter;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.etfease.datasource.Trade;

public class tradeshandlerx extends DefaultHandler
{
	private CharArrayWriter text = new CharArrayWriter ();
	private Stack path;
	private Map params;
	private DefaultHandler parent;
	private SAXParser parser;
	private List<Trade> trades;

	public tradeshandlerx(Stack path, Map params, Attributes attributes, SAXParser parser, DefaultHandler parent)  throws SAXException
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


	public void startTrade (Attributes attributes) throws SAXException
	{

	}




	public String getText()
	{
		return text.toString().trim();
	}

	public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, Attributes attributes) throws SAXException
	{
		text.reset();
		trades = (List <Trade>) path.peek();

		if (localName.equals("com.etfease.datasource.Trade"))
		{
			Trade trade = new Trade(0,0,0,0,"");
			trades.add(trade);
			startTrade (attributes);
			path.push(trade);
			DefaultHandler handler = new Tradehandler(path,params,attributes,parser,this);
			parser.getXMLReader().setContentHandler (handler);
		}


	}

	public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) throws SAXException
	{
		if (localName.equals("trades"))
		{
			end();
			path.pop();
			parser.getXMLReader().setContentHandler (parent);
		}




	}

	public void characters(char[] ch, int start, int length)
	{
		text.write (ch,start,length);
	}


}