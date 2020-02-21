//
//  StrategyViewController.h
//  ETFEase
//
//  Created by Michael Lindeboom on 12/29/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ListTab.h"
@class ListTab;

@interface StrategyViewController : UIViewController <UITableViewDataSource, UITableViewDelegate>{
	NSArray *strategyTypes;
	IBOutlet ListTab *listTab;
	NSString *selectedStrategy;
}
@property (nonatomic,retain) NSArray *strategyTypes;
@property (nonatomic,retain) NSString *selectedStrategy;

- (IBAction)cancel:(id)sender;
- (IBAction)done:(id)sender;

@end
