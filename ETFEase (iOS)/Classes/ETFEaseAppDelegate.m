//
//  ETFEaseAppDelegate.m
//  ETFEase
//
//  Created by Michael Lindeboom on 12/21/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "ETFEaseAppDelegate.h"



@implementation ETFEaseAppDelegate

@synthesize window;
@synthesize tabBarController;
int i=0;
int j=0;

#pragma mark -
#pragma mark Application lifecycle

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {    
    
    // Override point for customization after application launch.
//*************EXPERIMENTAL START************	
	//NSArray *test = [NSArray arrayWithObjects:@"one", @"two", @"three", nil];xxx
	//NSLog(@"%@", [data yajl_JSONString]);
	
/*	NSData *data = [NSData dataWithContentsOfURL:[NSURL URLWithString:@"http://etfease4.appspot.com/portfoliolist?symbol=FAS,URE,GLD,UBD,IYR,TNA"]];
	
	YAJLParser *parser = [[YAJLParser alloc] initWithParserOptions:YAJLParserOptionsAllowComments];
	parser.delegate = self;
	[parser parse:data];
	if (parser.parserError)
		NSLog(@"Error:\n%@", parser.parserError);
	
	parser.delegate = nil;
	[parser release];
*/
	
 //**************EXPERIMENTAL END************	
 	
/*	ETFList *e = [[ETFList alloc]init];
	JSONParser *jp = [[JSONParser alloc ]initWithETFList: e];
	
	[jp parse];
	
	[jp.etfList etfs];
*/  
	[window addSubview:[tabBarController view]];
    [window makeKeyAndVisible];
    
    return YES;
}

//**************EXPERIMENTAL START************	

// Include delegate methods from YAJLParserDelegate 
- (void)parserDidStartDictionary:(YAJLParser *)parser { 
	NSLog(@"%@%d",@"parserDidStartDictionary",i++);
}
- (void)parserDidEndDictionary:(YAJLParser *)parser { 
	NSLog(@"%@%d",@"parserDidEndDictionary",i--);
}

- (void)parserDidStartArray:(YAJLParser *)parser { 
	NSLog(@"%@%d",@"parserDidStartArray",j++);
}
- (void)parserDidEndArray:(YAJLParser *)parser { 
	NSLog(@"%@%d",@"parserDidEndArray",j--);
}

- (void)parser:(YAJLParser *)parser didMapKey:(NSString *)key {
	NSLog(@"Key %@",key);
}
- (void)parser:(YAJLParser *)parser didAdd:(id)value { 
	NSLog(@"Value %@",value);
}
//**************EXPERIMENTAL END***********


- (void)applicationWillResignActive:(UIApplication *)application {
    /*
     Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
     Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
     */
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
	
    /*
     Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
     If your application supports background execution, called instead of applicationWillTerminate: when the user quits.
     */

	VariableStore *vs = [VariableStore sharedInstance];
	[vs save];
	NSLog(@"Saved settings");
	
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    /*
     Called as part of  transition from the background to the inactive state: here you can undo many of the changes made on entering the background.
     */
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    /*
     Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
     */
}


- (void)applicationWillTerminate:(UIApplication *)application {
    /*
     Called when the application is about to terminate.
     See also applicationDidEnterBackground:.
     */
}


#pragma mark -
#pragma mark Memory management

- (void)applicationDidReceiveMemoryWarning:(UIApplication *)application {
    /*
     Free up as much memory as possible by purging cached data objects that can be recreated (or reloaded from disk) later.
     */
}


- (void)dealloc {
    [window release];
	[tabBarController release];
    [super dealloc];
}


@end
