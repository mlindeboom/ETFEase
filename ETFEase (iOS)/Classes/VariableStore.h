//
//  VariableStore.h
//  ETFEase
//
//  Created by Michael Lindeboom on 12/27/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface VariableStore : NSObject {
	// Place any "global" variables here
	NSString * strategy;
	NSString * filter;
	NSString * sort;
	NSArray	 * symbols;
	BOOL isPortfolioChanged;
}

@property (readwrite,retain) 	NSString * strategy;
@property (readwrite,retain) 	NSString * filter;
@property (readwrite,retain) 	NSString * sort;
@property (readwrite,retain) 	NSArray * symbols;
@property (readwrite) 	BOOL isPortfolioChanged;


// message from which our instance is obtained
+ (VariableStore *)sharedInstance; 
- (void)save;

@end
