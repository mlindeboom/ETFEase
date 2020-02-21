//
//  ETFList.h
//  Untitled
//
//  Created by Michael Lindeboom on 11/26/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h> 
#import "VariableStore.h"
#import "ETF.h";
#import "ListTab.h";
#import "UrlDownloaderOperation.h"
@class ListTab;


@interface ETFList : NSObject {
	NSMutableDictionary * etfs;	

	NSArray *sortedEtfsBySymbol;
	NSArray *sortedEtfsByTotal;
	NSArray *etfsThatAreSorted;
	NSArray *filteredEtfsByNonUsEtf;
	NSArray *filterEtfsByUsEtf;
	NSArray *filterEtfsByBondEtf;
	NSArray *filterEtfsByCommodityEtf;
	NSArray *filterEtfsByRecentBuys;
	NSArray *filterEtfsByRecentSells;
	NSArray *filterEtfsByBuyNow;
	NSArray *filterEtfsBySellNow;

	NSString *etfListUrl;
	NSArray *etfUrlTypes;
	
    
	NSOperationQueue *_queue;
	ListTab *listTab;
	
	
} 

@property (nonatomic,retain) NSOperationQueue *_queue;
@property (nonatomic,retain) NSMutableDictionary * etfs;
@property (nonatomic,retain) NSArray * etfUrlTypes;
@property (nonatomic,retain) NSArray * sortedEtfsBySymbol;
@property (nonatomic,retain) NSArray * sortedEtfsByTotal;
@property (nonatomic,retain) NSArray *filterEtfsByNonUsEtf;
@property (nonatomic,retain) NSArray *filterEtfsByUsEtf;
@property (nonatomic,retain) NSArray *filterEtfsByBondEtf;
@property (nonatomic,retain) NSArray *filterEtfsByCommodityEtf;
@property (nonatomic,retain) NSArray *filterEtfsByRecentBuys;
@property (nonatomic,retain) NSArray *filterEtfsByRecentSells;
@property (nonatomic,retain) NSArray *filterEtfsByBuyNow;
@property (nonatomic,retain) NSArray *filterEtfsBySellNow;
@property (nonatomic,retain) NSArray *etfsThatAreSorted;

-(id)initWithView: (ListTab *) inListTab;
-(void) getData;
-(void) refresh;

@end

