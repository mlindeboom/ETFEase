//
//  TradeDetailsViewController.h
//  ETFEase
//
//  Created by Michael Lindeboom on 12/29/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ListTab.h"
#import "ETF.h";
#import "TradeDetailCell.h"
@class ListTab;


@interface TradeDetailsViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
	UIImageView * chart;
	ListTab *listTab;	
	UILabel * etfSymbolLbl;
	UILabel * etfNameLbl;
	IBOutlet UITableView *tableView;	
}

- (IBAction)cancel:(id)sender;
- (IBAction)add:(id)sender;
-(NSArray*)getCellContent:(NSIndexPath *)indexPath;
+(UIColor *) colorWithHexString: (NSString *) stringToConvert;

@property (nonatomic,retain) IBOutlet UILabel *etfSymbolLbl;
@property (nonatomic,retain) IBOutlet UILabel *etfNameLbl;
@property (nonatomic,retain) IBOutlet UIImageView * chart;
@property (nonatomic,retain) IBOutlet ListTab *listTab;

@end
