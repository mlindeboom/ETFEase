//
//  JSONParser.h
//  ETFEase
//
//  Created by Michael Lindeboom on 2/5/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <YAJL/YAJL.h>
#import "ETFList.h"
#import "ETFList.h"
#import "ETF.h"
#import "Trades.h"
#import "Trade.h"
#import "Constants.h"

@class Constants; //Forward declaration of the Constants class

@interface JSONParser : NSObject <YAJLParserDelegate> {

	NSMutableString *currentValue; //This will hold element values as we build them piece by piece
	ETFList *etfList;
	ETF *etf;
	Trades * trades;
	Trade * trade;
	NSNumber * shares;
	NSNumber * tradeAmount;
	NSNumber * fee;
	NSString * date;
	NSNumber * remainingAmount;
	NSString * elementName;
}

@property (nonatomic, retain) ETFList *etfList;
@property (nonatomic, retain) NSString *elementName;
@property (nonatomic, retain) ETF *etf;
@property (nonatomic, retain) Trades * trades;
@property (nonatomic, retain) Trade * trade;
@property (nonatomic, retain) NSNumber * shares;
@property (nonatomic, retain) NSNumber * tradeAmount;
@property (nonatomic, retain) NSNumber * fee;
@property (nonatomic, retain) NSString * date;
@property (nonatomic, retain) NSNumber * remainingAmount;

- (id)initWithETFList:(ETFList *)etfList; // Custom initializer
- (void)parse: (NSMutableData *) data; // This performs the parsing of the file

@end
