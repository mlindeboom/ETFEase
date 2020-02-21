//
//  JSONParser.m
//  ETFEase
//
//  Created by Michael Lindeboom on 2/5/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "JSONParser.h"


@implementation JSONParser

@synthesize etf;
@synthesize trades;
@synthesize trade;
@synthesize shares;
@synthesize tradeAmount;
@synthesize fee;
@synthesize date;
@synthesize remainingAmount;
@synthesize etfList;
@synthesize elementName;

- (void) dealloc{
	[etf release];
	[trades release];
	[trade release];
	[shares release];
	[tradeAmount release];
	[fee release];
	[date release];
	[remainingAmount release];
	[etfList release];
	[elementName release];
	[super dealloc];
}


-(id)initWithETFList:(ETFList *)myEtfList{
    if (self = [super init]) {
		[self setEtfList:myEtfList];
    }
    return self;
}

- (void) parse {
	
	NSData *data = [NSData dataWithContentsOfURL:[NSURL URLWithString:@"http://etfease5.appspot.com/portfoliolist?symbol=FAS,URE,GLD,UBD,IYR,TNA"]];
	
	YAJLParser *parser = [[YAJLParser alloc] initWithParserOptions:YAJLParserOptionsAllowComments];
	parser.delegate = self;
	[parser parse:data];
	if (parser.parserError)
		NSLog(@"Error:\n%@", parser.parserError);
	
	parser.delegate = nil;
	[parser release];
	
}

- (void) parse: (NSMutableData *) data {
	
	YAJLParser *parser = [[YAJLParser alloc] initWithParserOptions:YAJLParserOptionsAllowComments];
	parser.delegate = self;
	[parser parse:data];
	if (parser.parserError)
		NSLog(@"Error:\n%@", parser.parserError);
	
	parser.delegate = nil;
	[parser release];
	
}

//Delegate methods for YAJLParserDelegate 

- (void)parserDidStartDictionary:(YAJLParser *)parser {
	
}

- (void)parserDidEndDictionary:(YAJLParser *)parser {
	
}

- (void)parserDidStartArray:(YAJLParser *)parser {
	
}

- (void)parserDidEndArray:(YAJLParser *)parser {
	
}

- (void)parser:(YAJLParser *)parser didMapKey:(NSString *)key {
	//NSLog(@"Key %@",key);
	self.elementName = key;

	if ([self.elementName isEqualToString:DATASOURCE_TRADE]) {
	}
	
	else if ([self.elementName isEqualToString:DATASOURCE_TRADES]) {
	}
	
	else if ([self.elementName isEqualToString:DATASOURCE_ETF]) {
		
	}
	

}

- (void)parser:(YAJLParser *)parser didAdd:(id)value { 

	
	if ([self.elementName isEqualToString:SHARES]) {		
		self.shares = value;
	}
	
	else if ([self.elementName isEqualToString:TRADE_AMOUNT]) {
		self.tradeAmount = value;
	}
	
	else if ([self.elementName isEqualToString:FEE]) {
		self.fee = value;
	}
	
	else if ([self.elementName isEqualToString:DATE]) {
		self.date = value;
	}
	
	else if ([self.elementName isEqualToString:REMAINING_AMOUNT]) {
		self.remainingAmount = value;
		
		//remaining is the last entry collected for trades
		[self.trades addTrade:shares tradeAmount:self.tradeAmount fee:self.fee date:self.date remainingAmount:self.remainingAmount];
		[self.trades setIsBuyNow:[trades buyNow]];
		[self.trades setIsSellNow:[trades sellNow]];
		[self.trades setIsRecentBuy:[trades recentBuy]];
		[self.trades setIsRecentSell:[trades recentSell]];
	
	}
	
	
	else if ([self.elementName isEqualToString:ETF_FILTER_TYPE]) {
		//first element of trades -- init trades object
		Trades* myTrades = [[Trades alloc] init];
		self.trades = myTrades;
		[myTrades release];
		
		[self.trades setEtfFilterType:value];
		[self.etf addTrades:trades];
	}
	
	else if ([self.elementName isEqualToString:TOTAL]) {
		[self.trades setTotal:value];
	}

	else if ([self.elementName isEqualToString:NAME]) {
		//first element of etf -- init etf object
		ETF* myEtf = [[ETF alloc]init];
		self.etf= myEtf;
		[self.etf setName:value];
		
		[myEtf release];
	}
	
	else if ([self.elementName isEqualToString:SYMBOL]) {
		[self.etf setSymbol:value];
	}
	
	else if ([self.elementName isEqualToString:ETF_TYPE]) {
		[self.etf setEtfType:value];
	}

	else if ([self.elementName isEqualToString:@"string"] && self.etf!=nil) {
		NSMutableDictionary *myEtfs = [self.etfList etfs];
		[myEtfs setObject:etf forKey:[self.etf symbol]];
		self.etf=nil;
	}
	
	
	[currentValue release];
	currentValue = nil;
	
}



@end
