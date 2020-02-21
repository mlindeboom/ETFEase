//
//  ETF.m
//  Untitled
//
//  Created by Michael Lindeboom on 11/15/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "ETF.h"
#import "Trades.h"


@implementation ETF
@synthesize name;
@synthesize symbol;
@synthesize etfType;
@synthesize ETFTypeAsString;
@synthesize tradesList;


NSInteger ETFTotalSort(id etf1, id etf2, void *etfFilterType)
{   
	
    Trades *etf1Trades = [etf1 getTrades: (NSString *)etfFilterType]; 
	Trades *etf2Trades = [etf2 getTrades: (NSString *)etfFilterType];
	
	NSNumber *total1 = [etf1Trades total];
	NSNumber *total2 = [etf2Trades total];
    
	return [total2 compare:total1]; 
	
}


NSInteger ETFAlphaSort(id etf1, id etf2, void *context)
{   
	NSString *symbol1 = [etf1 symbol];
	NSString *symbol2 = [etf2 symbol];
	return [symbol1 compare:symbol2]; 
}


-(id)init
{
    if (self = [super init])
    {
		// Initialization code here
		self.ETFTypeAsString = [NSArray arrayWithObjects:	@"USEquityETF",	@"GlobalEquityETF",	@"FixedIncomeETF",	@"CommodityBasedETF", nil];
		NSMutableArray *mut = [[NSMutableArray alloc] init]; 
		self.tradesList = mut;
		[mut release];
    }
    return self;
}






-(void) dealloc{
	[name release];
	[symbol release];
	[etfType release];
	[tradesList release];
	[ETFTypeAsString release];	
	[super dealloc];
}


-(void) addTrades: (Trades *) trades{
	[self.tradesList addObject:trades];
}

-(Trades *) getTrades:(NSString *) myEtfFilterType {
	int i;
	for (i=0;i<[self.tradesList count];i++){
		Trades *trades = [self.tradesList objectAtIndex:i];
		NSString* etfFilterType = [trades etfFilterType];
		if ([etfFilterType isEqualToString:myEtfFilterType]) {
			return trades;
		}
	}
	return NULL;
} 




-(NSArray *) filterByNonUsEtf:(NSArray *)etfs
{
	NSPredicate *globalEquityPredicate = [NSPredicate predicateWithFormat:@"etfType MATCHES %@",[ETFTypeAsString objectAtIndex:GlobalEquityETF]];
	return [etfs filteredArrayUsingPredicate:globalEquityPredicate];
}


-(NSArray *) filterByUsEtf:(NSArray *)etfs
{
	NSPredicate *USEquityPredicate = [NSPredicate predicateWithFormat:@"etfType MATCHES %@",[ETFTypeAsString objectAtIndex:USEquityETF]];
	return [etfs filteredArrayUsingPredicate:USEquityPredicate];
}


-(NSArray *) filterByBondEtf:(NSArray *)etfs
{
	NSPredicate *FixedIncomePredicate = [NSPredicate predicateWithFormat:@"etfType MATCHES %@",[ETFTypeAsString objectAtIndex:FixedIncomeETF]];
	return [etfs filteredArrayUsingPredicate:FixedIncomePredicate];
}

-(NSArray *) filterByCommodityEtf:(NSArray *)etfs
{
	NSPredicate *CommodityBasedPredicate = [NSPredicate predicateWithFormat:@"etfType MATCHES %@",[ETFTypeAsString objectAtIndex:CommodityBasedETF]];
	return [etfs filteredArrayUsingPredicate:CommodityBasedPredicate];
}







-(NSArray *) filterByRecentBuys:(NSArray *)etfs forETFFilterType:(NSString *)etfFilterType
{
	NSPredicate *RecentBuysPredicate = [NSPredicate predicateWithBlock: ^BOOL(id obj, NSDictionary *bind){
		Trades *myTrades = [(ETF*)obj getTrades:etfFilterType];
		return [myTrades isRecentBuy]==YES;
	}];
	
	return [etfs filteredArrayUsingPredicate:RecentBuysPredicate];
}

-(NSArray *) filterByRecentSells:(NSArray *)etfs forETFFilterType:(NSString *)etfFilterType
{
	NSPredicate *RecentSellsPredicate = [NSPredicate predicateWithBlock: ^BOOL(id obj, NSDictionary *bind){
		Trades *myTrades = [(ETF*)obj getTrades:etfFilterType];
		return [myTrades isRecentSell]==YES;
	}];
	return [etfs filteredArrayUsingPredicate:RecentSellsPredicate];
}

-(NSArray *) filterBySellNow:(NSArray *)etfs forETFFilterType:(NSString *)etfFilterType
{
	NSPredicate *SellNowPredicate = [NSPredicate predicateWithBlock: ^BOOL(id obj, NSDictionary *bind){
		Trades *myTrades = [(ETF*)obj getTrades:etfFilterType];
		return [myTrades isSellNow]==YES;
	}];
	return [etfs filteredArrayUsingPredicate:SellNowPredicate];
}


-(NSArray *) filterByBuyNow:(NSArray *)etfs forETFFilterType:(NSString *)etfFilterType
{
	NSPredicate *BuyNowPredicate = [NSPredicate predicateWithBlock: ^BOOL(id obj, NSDictionary *bind){
		Trades *myTrades = [(ETF*)obj getTrades:etfFilterType];
		return [myTrades isBuyNow]==YES;
	}];
	return [etfs filteredArrayUsingPredicate:BuyNowPredicate];
}


-(NSArray *) sortByTotal:(NSArray *)etfs forETFFilterType:(NSString *)etfFilterType
{
	return [etfs sortedArrayUsingFunction:ETFTotalSort context:etfFilterType];
}

-(NSArray *) sortBySymbol:(NSArray *)etfs
{
	return [etfs sortedArrayUsingFunction:ETFAlphaSort context:NULL];
}


@end
