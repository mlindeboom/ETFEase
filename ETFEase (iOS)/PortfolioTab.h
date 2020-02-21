//
//  Portfolio.h
//  ETFEase
//
//  Created by Michael Lindeboom on 12/22/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PortfolioList.h"
#import "TradeDetailVCForPortfolio.h"
#import "ETFCell.h"
#import "VariableStore.h"
#import "Trade.h"

@class PortfolioList;
@class TradeDetailVCForPortfolio;


@interface PortfolioTab : UIViewController <UITableViewDataSource, UITableViewDelegate>{
	NSArray *portfolioData;
	PortfolioList *portfolioList;
	ETF *selectedETF;

	
	UIActivityIndicatorView *activityIndicator;
	IBOutlet UITableView *tableView;
	IBOutlet TradeDetailVCForPortfolio *tradeDetailVCForPortfolio;
}

@property(nonatomic, retain) NSArray *portfolioData;
@property(nonatomic, retain) UIActivityIndicatorView *activityIndicator;
@property(nonatomic, retain) ETF * selectedETF;



-(IBAction) refresh: (id) sender;


-(void) didStartDataLoadOpertion;
-(void) didFinishDataLoadOpertion;
-(NSArray*) filterSortStrategy;
-(void) reloadPortfolioTab;



@end
