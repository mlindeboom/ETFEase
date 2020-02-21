//
//  Trades.m
//  Untitled
//
//  Created by Michael Lindeboom on 11/21/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "Trades.h"
#import "Trade.h"


@implementation Trades

@synthesize total;
@synthesize trades;
@synthesize etfFilterType;
@synthesize ETFFilterTypeAsString;
@synthesize isRecentBuy;
@synthesize isRecentSell;
@synthesize isBuyNow;
@synthesize isSellNow;


-(id)init{
    if (self = [super init])
    {
		self.ETFFilterTypeAsString = [NSArray arrayWithObjects:	@"R3", @"RSI25", @"RSI75", nil];
		NSMutableArray *mut =  [[NSMutableArray alloc] init]; 
		self.trades = mut;
		[mut release];
    }
    return self;
}

-(void) dealloc{
	[total release];
	[trades release];
	[etfFilterType release];
	[ETFFilterTypeAsString release];
	[super dealloc];
}


-(void) addTrade:(NSNumber *) inShares tradeAmount:(NSNumber *) inTradeAmount fee:(NSNumber *) inFee date:(NSString *) inDate remainingAmount:(NSNumber *)  inRemainingAmount {
	//create a trade object instance and add it to the trades list
	Trade* myTrade = [[Trade alloc] initWithTrade:inShares tradeAmount:inTradeAmount fee:inFee date:inDate remainingAmount:inRemainingAmount];
	[trades addObject:myTrade];
	[myTrade release]; 
}



-(BOOL) recentBuy{
	
	int last = [trades count];
	
	if (last==0) {
		return NO;
	} else last--;
	
	Trade *myTrade = [trades objectAtIndex:last];
	
	if ([[myTrade shares] intValue]>0) {
		return YES;
	} else {
		return NO;
	}
}


-(BOOL) recentSell{
	int last = [trades count];
	
	if (last==0) {
		return NO;
	} else last--;
	
	Trade *myTrade = [trades objectAtIndex:last];
	NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
	[dateFormatter setDateFormat:@"yyyy-MM-dd"];
	NSDate *myTradeDate = [dateFormatter dateFromString:[myTrade date]];
	NSDate *todaysDate = [NSDate date];
	NSTimeInterval secondsSinceTrade = [todaysDate timeIntervalSinceDate: myTradeDate];
	
	if ([[myTrade shares] intValue]<0 && secondsSinceTrade/86400<=7) return YES;
	else return NO;
}





-(BOOL) buyNow{
	
	int last = [trades count];
	
	if (last==0) {
		return NO;
	} else last--;
	
	Trade *myTrade = [trades objectAtIndex:last];
	NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
	[dateFormatter setDateFormat:@"yyyy-MM-dd"];
	NSDate *myTradeDate = [dateFormatter dateFromString:[myTrade date]];
	NSDate *todaysDate = [NSDate date];
	NSTimeInterval secondsSinceTrade = [todaysDate timeIntervalSinceDate: myTradeDate];
	
	if ([[myTrade shares] intValue]>0 && secondsSinceTrade/86400<=1) return YES;
	else return NO;
	
}




-(BOOL) sellNow{
	int last = [trades count];
	
	if (last==0) {
		return NO;
	} else last--;
	
	Trade *myTrade = [trades objectAtIndex:last];
	NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
	[dateFormatter setDateFormat:@"yyyy-MM-dd"];
	NSDate *myTradeDate = [dateFormatter dateFromString:[myTrade date]];
	NSDate *todaysDate = [NSDate date];
	NSTimeInterval secondsSinceTrade = [todaysDate timeIntervalSinceDate: myTradeDate];
	
	if ([[myTrade shares] intValue]<0 && secondsSinceTrade/86400<=1) return YES;
	else return NO;
	
}




@end
