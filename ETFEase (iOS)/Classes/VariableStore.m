//
//  VariableStore.m
//  ETFEase
//
//  Created by Michael Lindeboom on 12/27/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "VariableStore.h"


@implementation VariableStore

@synthesize strategy;
@synthesize filter;
@synthesize sort;
@synthesize symbols;
@synthesize isPortfolioChanged;

+ (VariableStore *)sharedInstance
{
    // the instance of this class is stored here
    static VariableStore *myInstance = nil;
	
    // check to see if an instance already exists
    if (nil == myInstance) {
			
        myInstance  = [[[self class] alloc] init];
        // initialize variables here
		NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
		
		myInstance.strategy = [prefs objectForKey:@"strategy"];
		myInstance.filter = [prefs objectForKey:@"filter"];
		myInstance.sort = [prefs objectForKey:@"sort"];
		myInstance.symbols = [prefs objectForKey:@"symbols"];

		if (myInstance.strategy == nil) myInstance.strategy = @"RSI25";
		if (myInstance.filter == nil) myInstance.filter = @"ALL";
		if (myInstance.sort == nil) myInstance.sort = @"Total";
		if (myInstance.symbols == nil) myInstance.symbols = [NSArray arrayWithObjects:@"URE",@"FAS",@"TNA",nil]; 

		myInstance.isPortfolioChanged = YES;
    }
    // return the instance of this class
    return myInstance;
}

- (void) save{

	VariableStore *vs = [VariableStore sharedInstance];
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];

    [prefs removeObjectForKey:@"strategy"];
    [prefs removeObjectForKey:@"filter"];
    [prefs removeObjectForKey:@"sort"];
    [prefs removeObjectForKey:@"symbols"];
	
    [prefs setObject:vs.strategy forKey:@"strategy"];
    [prefs setObject:vs.filter forKey:@"filter"];
    [prefs setObject:vs.sort forKey:@"sort"];
    [prefs setObject:vs.symbols forKey:@"symbols"];
	[prefs synchronize];
	
}
		


@end
