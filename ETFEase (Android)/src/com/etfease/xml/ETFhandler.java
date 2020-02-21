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
import com.etfease.datasource.ETFType;
import com.etfease.datasource.Trades;

public class ETFhandler extends DefaultHandler
{
	private CharArrayWriter text = new CharArrayWriter ();
	private Stack path;
	private Map params;
	private DefaultHandler parent;
	private SAXParser parser;
	private ETF etf;

	public ETFhandler(Stack path, Map params, Attributes attributes, SAXParser parser, DefaultHandler parent)  throws SAXException
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


	public void startname (Attributes attributes) throws SAXException
	{

	}


	public void endname () throws SAXException
	{
		etf.setName(getText());
	}



	public void startsymbol (Attributes attributes) throws SAXException
	{
	}


	public void endsymbol () throws SAXException
	{
		etf.setSymbol(getText());
	}



	public void startetfType (Attributes attributes) throws SAXException
	{

	}


	public void endetfType () throws SAXException
	{
		etf.setEtfType(ETFType.valueOf(getText()));
	}



	public void starttradesList (Attributes attributes) throws SAXException
	{

	}




	public String getText()
	{
		return text.toString().trim();
	}

	public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, Attributes attributes) throws SAXException
	{
		text.reset();
		etf = (ETF)path.peek();

		if (localName.equals("name")) startname (attributes);

		if (localName.equals("symbol")) startsymbol (attributes);

		if (localName.equals("etfType")) startetfType (attributes);


		if (localName.equals("tradesList"))
		{
			starttradesList (attributes);
			path.push(etf);
			DefaultHandler handler = new tradesListhandler(path,params,attributes,parser,this);
			parser.getXMLReader().setContentHandler (handler);
		}
	}

	public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) throws SAXException
	{
		if (localName.equals("com.etfease.datasource.ETF"))
		{
			end();
			path.pop();
			parser.getXMLReader().setContentHandler (parent);
		}


		if (localName.equals("name")) endname ();


		if (localName.equals("symbol")) endsymbol ();


		if (localName.equals("etfType")) endetfType ();


	}

	public void characters(char[] ch, int start, int length)
	{
		text.write (ch,start,length);
		
	}

}