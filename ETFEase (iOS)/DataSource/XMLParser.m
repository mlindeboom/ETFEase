//
//  XMLParser.m
//  Untitled
//
//  Created by Michael Lindeboom on 11/13/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//



#import "Constants.h"
#import "XMLParser.h"





@implementation XMLParser
// Synthesize our properties
@synthesize currentValue;
@synthesize etfList;


- (id)initWithETFList:(ETFList *)myEtfList{
    if (self = [super init]) {
		[self setEtfList:myEtfList];
    }
    return self;
}


// Make sure objects are released
-(void)dealloc{
    [currentValue release];
	[etfList release];
    [super dealloc];
}

#pragma mark NSXMLParser Delegate Calls

// This method gets called every time NSXMLParser encounters a new element
-(void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName 
 namespaceURI:(NSString *)namespaceURI 
qualifiedName:(NSString *)qualifiedName 
   attributes:(NSDictionary *)attributeDict{
	//
}

// This method gets called for every character NSXMLParser finds.
-(void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string{
    // If currentValue doesn't exist, initialize and allocate
    if (!currentValue) {
		currentValue = [[NSMutableString alloc] init];
    }
    
    // Append the current character value to the running string
    // that is being parsed
    [currentValue appendString:string];
}




// This method is called whenever NSXMLParser reaches the end of an element
-(void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName
 namespaceURI:(NSString *)namespaceURI 
qualifiedName:(NSString *)qName{
	
	elementName = [elementName stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
	NSMutableString *currentValueCopy = (NSMutableString *)[currentValue stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
	
	//NSLog(@"Element=%@ - %@",elementName, currentValueCopy);
	

	if ([elementName isEqualToString:NAME]) {
		//first element of etf -- init etf object
		etf= [[ETF alloc] init];
		[etf setName:currentValueCopy];
	}
	
	if ([elementName isEqualToString:SYMBOL]) {
		[etf setSymbol:currentValueCopy];
	}
	
	if ([elementName isEqualToString:ETF_TYPE]) {
		[etf setEtfType:currentValueCopy];
	}
	
	if ([elementName isEqualToString:DATASOURCE_ETF]) {
	
		NSMutableDictionary *myEtfs = [etfList etfs];
		
		[myEtfs setObject:etf forKey:[etf symbol]];
	}

	
	if ([elementName isEqualToString:DATASOURCE_TRADES]) {
		[trades setIsBuyNow:[trades buyNow]];
		[trades setIsSellNow:[trades sellNow]];
		[trades setIsRecentBuy:[trades recentBuy]];
		[trades setIsRecentSell:[trades recentSell]];
		[etf addTrades:trades];
	}
	
	if ([elementName isEqualToString:ETF_FILTER_TYPE]) {
		//first element of trades -- init trades object
		trades = [[Trades alloc] init];
		[trades setEtfFilterType:currentValueCopy];
	}

	if ([elementName isEqualToString:TOTAL]) {
		NSNumberFormatter * f = [[NSNumberFormatter alloc] init];
		[f setNumberStyle:NSNumberFormatterDecimalStyle];
		[trades setTotal:[f numberFromString:currentValueCopy]];
		[f dealloc];
	}
	

	if ([elementName isEqualToString:DATASOURCE_TRADE]) {
		[trades addTrade:shares tradeAmount:tradeAmount fee:fee date:date remainingAmount:remainingAmount];
	}
	
	if ([elementName isEqualToString:SHARES]) {		
		NSNumberFormatter * f = [[NSNumberFormatter alloc] init];
		[f setNumberStyle:NSNumberFormatterDecimalStyle];
		shares = [f numberFromString:currentValueCopy];
		[f dealloc];
	}
	
	if ([elementName isEqualToString:TRADE_AMOUNT]) {
		NSNumberFormatter * f = [[NSNumberFormatter alloc] init];
		[f setNumberStyle:NSNumberFormatterDecimalStyle];
		tradeAmount = [f numberFromString:currentValueCopy];
		[f dealloc];
	}
	
	if ([elementName isEqualToString:FEE]) {
		NSNumberFormatter * f = [[NSNumberFormatter alloc] init];
		[f setNumberStyle:NSNumberFormatterDecimalStyle];
		fee = [f numberFromString:currentValueCopy];
		[f dealloc];
	}
	
	if ([elementName isEqualToString:DATE]) {
		date = currentValue;
		[date retain];
	}
	
	if ([elementName isEqualToString:REMAINING_AMOUNT]) {
		NSNumberFormatter * f = [[NSNumberFormatter alloc] init];
		[f setNumberStyle:NSNumberFormatterDecimalStyle];
		remainingAmount = [f numberFromString:currentValueCopy];
		[f dealloc];
	
	}
	
	[currentValue release];
	currentValue = nil;

}


- (BOOL)parse: (NSMutableData *) data {

    NSXMLParser *parser = [[NSXMLParser alloc] initWithData:data];
	
    // Tell NSXMLParser that this class is its delegate
    [parser setDelegate:self];
    
    // Kick off file parsing
    [parser parse];
    
    // Clean up
    
    [parser setDelegate:nil];
    [parser release];
    return YES;
}


/*
- (BOOL)parse: (NSString *) urlString {
	
	NSURL *url = [NSURL URLWithString:urlString];  
	NSMutableURLRequest* urlReq = [[NSMutableURLRequest alloc] initWithURL:url];  
	NSHTTPURLResponse* response = nil;	
	NSError* error_req = nil;

	[urlReq setValue:@"application/xml;q=0.9" forHTTPHeaderField:@"Accept"]; 
	[urlReq setValue:@"application/xml;q=0.9" forHTTPHeaderField:@"Content-Type"];	
	
	
	
	NSData *responseData = [NSURLConnection sendSynchronousRequest:urlReq  returningResponse:&response error:&error_req];
	NSString * aStr = [[NSString alloc] initWithData:responseData encoding:NSASCIIStringEncoding];
	NSLog(@"%@",aStr);
	[aStr release];

    NSXMLParser *parser = [[NSXMLParser alloc] initWithData:responseData];
	
    // Tell NSXMLParser that this class is its delegate
    [parser setDelegate:self];
    
    // Kick off file parsing
    [parser parse];
    
    // Clean up
    [urlReq release];
    [parser setDelegate:nil];
    [parser release];
    return YES;
}
*/











@end