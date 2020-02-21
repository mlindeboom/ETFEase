//
//  ETFList.m
//  Untitled
//
//  Created by Michael Lindeboom on 11/26/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "ETFList.h"



static NSString * OperationsChangedContext = @"OperationsChangedContext";


@implementation ETFList

@synthesize etfs;
@synthesize etfUrlTypes;
@synthesize _queue;
@synthesize sortedEtfsBySymbol;
@synthesize sortedEtfsByTotal;
@synthesize filterEtfsByNonUsEtf;
@synthesize filterEtfsByUsEtf;
@synthesize filterEtfsByBondEtf;
@synthesize filterEtfsByCommodityEtf;
@synthesize filterEtfsByRecentBuys;
@synthesize filterEtfsByRecentSells;
@synthesize filterEtfsByBuyNow;
@synthesize filterEtfsBySellNow;
@synthesize etfsThatAreSorted;


 

-(id)initWithView: (ListTab *) inListTab {
	if (self = [super init]){
		
		listTab = inListTab;
		etfListUrl= @"http://etfease5.appspot.com/etflist?type=<ETFTYPE>";

		
		NSMutableDictionary * mut = [[NSMutableDictionary alloc] init];
		self.etfs = mut;
		[mut release];
		
		self.etfUrlTypes = [NSArray arrayWithObjects:@"USEquityETF1",@"USEquityETF2",@"USEquityETF3",@"USEquityETF4", @"USEquityETF5",@"GlobalEquityETF1",@"GlobalEquityETF2",@"FixedIncomeETF",@"CommodityBasedETF",nil];
//		self.etfUrlTypes = [NSArray arrayWithObjects:@"USEquityETF1",@"GlobalEquityETF1",@"FixedIncomeETF",@"CommodityBasedETF",nil];
//		self.etfUrlTypes = [NSArray arrayWithObjects:@"USEquityETF1",@"GlobalEquityETF1",@"FixedIncomeETF",@"CommodityBasedETF",nil];

		NSOperationQueue* nq = [[NSOperationQueue alloc] init];
		self._queue = nq;
		[nq release];

		// Set to 1 to serialize operations. Comment out for parallel operations.
		// [_queue setMaxConcurrentOperationCount:1];
		[self._queue addObserver:self
				 forKeyPath:@"operations"
					options:0
					context:&OperationsChangedContext];
	
	}
	return self;
}



-(void) dealloc{
	[etfs release];
	[etfUrlTypes release];
	
	[sortedEtfsBySymbol release];
	[sortedEtfsByTotal release];
	
    [_queue removeObserver:self forKeyPath:@"operations"];
	[_queue release];
	[super dealloc];
}

-(void)getData{
	
	NSEnumerator *enumerator = [etfUrlTypes objectEnumerator];
	id etfType;
	
	while (etfType = [enumerator nextObject]) {
		
		NSString *myUrl = [etfListUrl stringByReplacingOccurrencesOfString:@"<ETFTYPE>" withString:(NSString *)etfType];

        UrlDownloaderOperation * operation = [UrlDownloaderOperation urlDownloaderWithUrlString:myUrl AndETFList:self];
        [_queue addOperation:operation];
	
	}
}


-(void) refresh
{
    NSLog(@"refresh::startTime = %@",[NSDate date]);
	NSString *strategy = [[VariableStore sharedInstance] strategy];
	NSString *sort = [[VariableStore sharedInstance] sort];
	NSArray *sortedEtfs=nil;
	
	ETF *myETF = [[ETF alloc] init]; 
	NSArray * allMyETFs = [[self etfs] allValues];
	self.sortedEtfsBySymbol = [myETF sortBySymbol:allMyETFs];
	self.sortedEtfsByTotal = [myETF sortByTotal:allMyETFs forETFFilterType:strategy];

	
	if ([sort compare:@"Total"] == NSOrderedSame) {
		sortedEtfs = self.sortedEtfsByTotal;
	} else {
		sortedEtfs = self.sortedEtfsBySymbol;
	}
	
	
	self.filterEtfsByNonUsEtf = [myETF filterByNonUsEtf: sortedEtfs];
	self.filterEtfsByUsEtf = [myETF filterByUsEtf:  sortedEtfs];
	self.filterEtfsByBondEtf = [myETF filterByBondEtf:  sortedEtfs];
	self.filterEtfsByCommodityEtf = [myETF filterByCommodityEtf:  sortedEtfs];
	self.filterEtfsByRecentBuys = [myETF filterByRecentBuys:  sortedEtfs forETFFilterType: strategy];
	self.filterEtfsByRecentSells = [myETF filterByRecentSells:  sortedEtfs forETFFilterType: strategy];
	self.filterEtfsByBuyNow = [myETF filterByBuyNow:  sortedEtfs forETFFilterType: strategy];
	self.filterEtfsBySellNow = [myETF filterBySellNow:  sortedEtfs forETFFilterType: strategy];
	self.etfsThatAreSorted = sortedEtfs;
	[myETF release];
    NSLog(@"refresh::endTime = %@",[NSDate date]);

}




- (void)observeValueForKeyPath:(NSString *)keyPath
                      ofObject:(id)object
                        change:(NSDictionary *)change
                       context:(void *)context
{
    if (context == &OperationsChangedContext)
    {

        NSLog(@"Queue size: %u", [[_queue operations] count]);
		
		//	
		if([[_queue operations] count]==0){

			//sort and filter
			[self refresh];

			//stop activity indicator spinning
			[listTab didFinishDataLoadOpertion];
			
		} else {
			//keep activity indicator spinning
			[listTab didStartDataLoadOpertion]; 
		}

		//	
		
    }
    else
    {
		
        [super observeValueForKeyPath:keyPath
                             ofObject:object
                               change:change
                              context:context];
    }
}


@end
