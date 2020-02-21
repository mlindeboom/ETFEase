//
//  PortfolioList.m
//  ETFEase
//
//  Created by Michael Lindeboom on 12/31/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "PortfolioList.h"

static NSString * OperationsChangedContext = @"OperationsChangedContext";

@implementation PortfolioList


-(id)initWithPortfolioView: (PortfolioTab *) inPortfolioTab {
	if (self = [super init]){
		
		portfolioTab = inPortfolioTab;
		
		etfs = [[NSMutableDictionary alloc] init];
		
		_queue = [[NSOperationQueue alloc] init];
		
		// Set to 1 to serialize operations. Comment out for parallel operations.
		// [_queue setMaxConcurrentOperationCount:1];
		[_queue addObserver:self
				 forKeyPath:@"operations"
					options:0
					context:&OperationsChangedContext];
		
	}
	return self;
}



-(void) dealloc{
	[etfs release];
	
	[sortedEtfsBySymbol release];
	[sortedEtfsByTotal release];
	
    [_queue removeObserver:self forKeyPath:@"operations"];
	[_queue release];
	[super dealloc];
}

-(void)getData{
	[etfs release];
	etfs = [[NSMutableDictionary alloc] init];

	
	//	http://etfease4.appspot.com/portfoliolist?symbol=FAS,URE,GLD,UBD,IYR,TNA
	NSString *myUrl = @"http://etfease5.appspot.com/portfoliolist?symbol=";
	
	//build symbol list from variable store
	NSString * result = @"";
	NSEnumerator *e = [[[VariableStore sharedInstance] symbols] objectEnumerator];
	id object;
	while (object = [e nextObject]) {
		if (![result  isEqualToString:@""]) {
			result = [result stringByAppendingString:@","];
		}
		result = [result stringByAppendingString:object];
	}	
	//append the symbol list to the url and start the fetch operation 
	myUrl = [myUrl stringByAppendingString:result];
	NSLog(@"Fetching data from %@",myUrl);
	UrlDownloaderOperation * operation = [UrlDownloaderOperation urlDownloaderWithUrlString:myUrl AndETFList:self];
	[_queue addOperation:operation];
}


-(void) refresh
{
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
	
	[myETF release];
	self.etfsThatAreSorted = sortedEtfs;
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
			[portfolioTab didFinishDataLoadOpertion];
			
		} else {
			//keep activity indicator spinning
			[portfolioTab didStartDataLoadOpertion]; 
		}
		
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
