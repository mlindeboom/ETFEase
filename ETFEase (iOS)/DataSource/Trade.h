//
//  Trade.h
//  Untitled
//
//  Created by Michael Lindeboom on 11/20/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Trade : NSObject {
	
	NSNumber *shares;
	NSNumber *tradeAmount;
	NSNumber *fee;
	NSString *date;
	NSNumber *remainingAmount;

}

@property (nonatomic, retain) NSNumber *shares;
@property (nonatomic, retain) NSNumber *tradeAmount;
@property (nonatomic, retain) NSNumber *fee;
@property (nonatomic, retain) NSString *date;
@property (nonatomic, retain) NSNumber *remainingAmount;

-(id)initWithTrade:(NSNumber *) inShares tradeAmount:(NSNumber *) inTradeAmount fee:(NSNumber *) inFee date:(NSString *) inDate remainingAmount:(NSNumber *)  inrRemainingAmount;



@end
