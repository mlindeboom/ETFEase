package com.etfease.xml;

// JDK Classes
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.etfease.datasource.ETFList;

public class Parser extends DefaultHandler
{
    Stack path = new Stack();
    Map params = new ConcurrentHashMap();
    SAXParser parser = null;
	ETFList etfList;    

    public Parser(ETFList etfList) throws Exception {
    	this.etfList = etfList;
    	SAXParserFactory factory = SAXParserFactory.newInstance();
        parser = factory.newSAXParser();
    }    
    
    public void startDocument() throws SAXException
    {
    }

    public void endDocument() throws SAXException
    {
    }

    public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, Attributes attributes) throws SAXException
    {
        if (localName.equals("com.etfease.datasource.ETFList"))
        {
            DefaultHandler handler = new ETFListhandler(path,params,attributes,parser,this);
            //path.push ("com.etfease.datasource.ETFList");
            path.push(etfList);
            parser.getXMLReader().setContentHandler (handler);
        }

    }


    public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) throws SAXException
    {
    }

    public Map parse (InputStream is) throws SAXException, IOException
    {
        parser.parse( new InputSource (is),this);
        return params;
    }


}
