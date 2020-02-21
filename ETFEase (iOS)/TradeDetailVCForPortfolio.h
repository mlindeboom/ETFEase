//
//  TradeDetailVCForPortfolio.h
//  ETFEase
//
//  Created by Michael Lindeboom on 12/31/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PortfolioTab.h"
#import "ETF.h";
#import "TradeDetailCell.h"
@class PortfolioTab;

@interface TradeDetailVCForPortfolio : UIViewController {
	UIImageView * chart;
	PortfolioTab * portfolioTab;	
	UILabel * etfSymbolLbl;
	UILabel * etfNameLbl;
	IBOutlet UITableView *tableView;	
}

- (IBAction)cancel:(id)sender;
- (IBAction)remove:(id)sender;

-(NSArray*)getCellContent:(NSIndexPath *)indexPath;
+(UIColor *) colorWithHexString: (NSString *) stringToConvert;

@property (nonatomic,retain) IBOutlet UILabel *etfSymbolLbl;
@property (nonatomic,retain) IBOutlet UILabel *etfNameLbl;
@property (nonatomic,retain) IBOutlet UIImageView * chart;
@property (nonatomic,retain) IBOutlet PortfolioTab *portfolioTab;


@end
