//
//  Trades.h
//  Untitled
//
//  Created by Michael Lindeboom on 11/21/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {R3,RSI25,RSI75} ETFFilterType;

@interface Trades : NSObject {
	
	NSNumber * total;
	NSMutableArray * trades;
	NSString * etfFilterType;
	NSArray *ETFFilterTypeAsString;	
	BOOL isRecentBuy;
	BOOL isRecentSell;
	BOOL isBuyNow;
	BOOL isSellNow;
	
}

@property (nonatomic, retain) NSNumber *total;
@property (nonatomic, retain) NSMutableArray *trades;
@property (nonatomic, retain) NSString *etfFilterType;
@property (nonatomic, retain) NSArray *ETFFilterTypeAsString;
@property (readwrite) BOOL isRecentBuy;
@property (readwrite) BOOL isRecentSell;
@property (readwrite) BOOL isBuyNow;
@property (readwrite) BOOL isSellNow;

-(BOOL) recentBuy;
-(BOOL) recentSell;
-(BOOL) buyNow;
-(BOOL) sellNow; 
-(void) addTrade:(NSNumber *) inShares tradeAmount:(NSNumber *) inTradeAmount fee:(NSNumber *) inFee date:(NSString *) inDate remainingAmount:(NSNumber *)  inRemainingAmount;

@end
