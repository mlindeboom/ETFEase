//
//  ListTab.h
//  ETFEase
//
//  Created by Michael Lindeboom on 12/22/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ETFList.h"
#import "ETFCell.h"
#import	"FilterViewController.h"
#import "StrategyViewController.h"
#import "SortViewController.h"
#import "TradeDetailsViewController.h"
#import "VariableStore.h"
#import "Trade.h"

@class ETFList;
@class FilterViewController;
@class StrategyViewController;
@class SortViewController;
@class VariableStore;
@class TradeDetailsViewController;

@interface ListTab : UIViewController <UITableViewDataSource, UITableViewDelegate> {
	
	NSArray *listData;
	ETFList *etfList;
	ETF *selectedETF;
	UIActivityIndicatorView *activityIndicator;

	IBOutlet UITableView *tableView;
	IBOutlet FilterViewController *filterViewController;
	IBOutlet StrategyViewController *strategyViewController;
	IBOutlet SortViewController *sortViewController;
	IBOutlet TradeDetailsViewController *tradeDetailsViewController;
}

-(IBAction) filter: (id) sender;
-(IBAction) search: (id) sender;
-(IBAction) sort: (id) sender;
-(IBAction) refresh: (id) sender;


-(void) didStartDataLoadOpertion;
-(void) didFinishDataLoadOpertion;
-(NSArray*) filterSortStrategy;
-(void) reloadListTab;

@property(nonatomic, retain) NSArray *listData;
@property(nonatomic, retain) UIActivityIndicatorView *activityIndicator;
@property(nonatomic, retain) ETF * selectedETF;
@property(nonatomic, retain) ETFList *etfList;


@end
