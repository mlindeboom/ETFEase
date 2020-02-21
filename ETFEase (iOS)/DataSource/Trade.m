//
//  Trade.m
//  Untitled
//
//  Created by Michael Lindeboom on 11/20/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "Trade.h"


@implementation Trade

@synthesize shares;
@synthesize tradeAmount;
@synthesize fee;
@synthesize date;
@synthesize remainingAmount;


-(id)initWithTrade:(NSNumber *) inShares tradeAmount:(NSNumber *) inTradeAmount fee:(NSNumber *) inFee date:(NSString *) inDate remainingAmount:(NSNumber *)  inRemainingAmount {
    if (self = [super init])
    {
		[self setShares:inShares];
		[self setTradeAmount:inTradeAmount];
		[self setFee:inFee];
		[self setDate:inDate];
		[self setRemainingAmount:inRemainingAmount];
    }
    return self;
}

-(void) dealloc{
	[shares release];
	[tradeAmount release];
	[fee release];
	[date release];
	[remainingAmount release];
	
	[super dealloc];
}



@end
