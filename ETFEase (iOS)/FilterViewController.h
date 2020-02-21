//
//  Filter.h
//  ETFEase
//
//  Created by Michael Lindeboom on 12/27/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ListTab.h"
@class ListTab;


@interface FilterViewController :  UIViewController <UITableViewDataSource, UITableViewDelegate> {
	NSArray *filterTypes;
	IBOutlet ListTab *listTab;
	NSString *selectedFilter;
}
@property (nonatomic,retain) NSArray *filterTypes;
@property (nonatomic,retain) NSString *selectedFilter;

- (IBAction)cancel:(id)sender;
- (IBAction)done:(id)sender;

@end
