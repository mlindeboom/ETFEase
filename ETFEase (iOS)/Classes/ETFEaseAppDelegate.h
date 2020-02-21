//
//  ETFEaseAppDelegate.h
//  ETFEase
//
//  Created by Michael Lindeboom on 12/21/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VariableStore.h"
#import <YAJL/YAJL.h>
#import "JSONParser.h"

@interface ETFEaseAppDelegate : NSObject <UIApplicationDelegate,YAJLParserDelegate> {
    UIWindow *window;
	UITabBarController *tabBarController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UITabBarController *tabBarController;

@end

