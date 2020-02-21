//
//  SortViewController.h
//  ETFEase
//
//  Created by Michael Lindeboom on 12/29/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ListTab.h"
@class ListTab;

@interface SortViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
	NSArray *sortTypes;
	IBOutlet ListTab *listTab;
	NSString *selectedSort;
}
@property (nonatomic,retain) NSArray *sortTypes;
@property (nonatomic,retain) NSString *selectedSort;

- (IBAction)cancel:(id)sender;
- (IBAction)done:(id)sender;


@end
