//
//  XMLParser.h
//  Untitled
//
//  Created by Michael Lindeboom on 11/13/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ETFList.h"
#import "ETFList.h"
#import "ETF.h"
#import "Trades.h"
#import "Trade.h"

@class Constants; //Forward declaration of the Constants class

@interface XMLParser : NSObject {
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
	
}

@property (nonatomic, retain) NSMutableString *currentValue;
@property (nonatomic, retain) ETFList *etfList;

- (BOOL)parse: (NSMutableData *) data; // This performs the parsing of the file
- (id)initWithETFList:(ETFList *)etfList; // Custom initializer


@end