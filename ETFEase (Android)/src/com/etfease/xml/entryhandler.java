package com.etfease.xml;

// JDK Classes
import java.io.CharArrayWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.etfease.datasource.ETF;

public class entryhandler extends DefaultHandler
{
	private CharArrayWriter text = new CharArrayWriter ();
	private Stack path;
	private Map params;
	private DefaultHandler parent;
	private SAXParser parser;
	
	private String entryName;
	private ETF etf;
	private String tempVal;

	
	
	public entryhandler(Stack path, Map params, Attributes attributes, SAXParser parser, DefaultHandler parent)  throws SAXException
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


	public void startstring (Attributes attributes) throws SAXException
	{
	}


	public void endstring () throws SAXException
	{
		entryName = tempVal;
		//Log.i("", tempVal);
	}



	public void startETF (Attributes attributes) throws SAXException
	{

	}




	public String getText()
	{
		return text.toString().trim();
	}

	public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, Attributes attributes) throws SAXException
	{
		text.reset();

		if (localName.equals("string")) {
			
            startstring (attributes);
		}


		if (localName.equals("com.etfease.datasource.ETF"))
		{
			startETF (attributes);
			
			//create a new etf instance and save it in the etfs map
			etf = new ETF();
			ConcurrentHashMap<String, ETF> etfs = (ConcurrentHashMap<String, ETF>)path.peek();
			etfs.put(entryName, etf);

			//path.push("com.etfease.datasource.ETF");
			path.push(etf);
			DefaultHandler handler = new ETFhandler(path,params,attributes,parser,this);
			parser.getXMLReader().setContentHandler (handler);
		}


	}

	public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) throws SAXException
	{
		if (localName.equals("entry"))
		{
			end();
			path.pop();
			parser.getXMLReader().setContentHandler (parent);
		}


		if (localName.equals("string")) endstring ();





	}

	public void characters(char[] ch, int start, int length)
	{
		text.write (ch,start,length);
		tempVal = new String(ch,start,length);

	}


}