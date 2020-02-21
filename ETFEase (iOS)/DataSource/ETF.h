//
//  ETF.h
//  Untitled
//
//  Created by Michael Lindeboom on 11/15/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Trades.h"

typedef enum {USEquityETF,GlobalEquityETF,FixedIncomeETF,CommodityBasedETF} ETFType;


@interface ETF : NSObject {
	
	NSString *name;
	NSString *symbol;
	NSString *etfType;
	NSMutableArray *tradesList;
	NSArray *ETFTypeAsString;
}

@property (nonatomic, retain) NSString *name;
@property (nonatomic, retain) NSString *symbol;
@property (nonatomic, retain) NSString *etfType;
@property (nonatomic, retain) NSArray *ETFTypeAsString;
@property (nonatomic, retain) NSMutableArray *tradesList;

-(void) addTrades: (Trades *) trades;
-(Trades*) getTrades: (NSString *) etfFilterType; 

-(NSArray *) filterByNonUsEtf:(NSArray *)etfs;
-(NSArray *) filterByUsEtf:(NSArray *)etfs;
-(NSArray *) filterByBondEtf:(NSArray *)etfs;
-(NSArray *) filterByCommodityEtf:(NSArray *)etfs;

-(NSArray *) filterByRecentBuys:(NSArray *)etfs forETFFilterType:(NSString *)etfFilterType;
-(NSArray *) filterBySellNow:(NSArray *)etfs forETFFilterType:(NSString *)etfFIlterType;
-(NSArray *) filterByBuyNow:(NSArray *)etfs forETFFilterType:(NSString *)etfFilterType;
-(NSArray *) filterByRecentSells:(NSArray *)etfs forETFFilterType:(NSString *)etfFilterType;
-(NSArray *) sortByTotal:(NSArray *)etfs forETFFilterType:(NSString *)etfFilterType;
-(NSArray *) sortBySymbol:(NSArray *)etfs;

@end

