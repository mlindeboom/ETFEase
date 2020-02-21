    //
//  StrategyViewController.m
//  ETFEase
//
//  Created by Michael Lindeboom on 12/29/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "StrategyViewController.h"


@implementation StrategyViewController

@synthesize strategyTypes;
@synthesize selectedStrategy;



- (IBAction)cancel:(id)sender {
	[self dismissModalViewControllerAnimated:YES];
}

- (IBAction)done:(id)sender {
	
	VariableStore *vs = [VariableStore sharedInstance];
	vs.strategy=selectedStrategy;
	
	[listTab reloadListTab];
	[self dismissModalViewControllerAnimated:YES];
}


-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	return [[self strategyTypes] count];
}

- (NSInteger)numberOfSections{
	return 0;
}


- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	UITableViewCell *cell = [tv dequeueReusableCellWithIdentifier:@"strategyCell"];
	if(nil == cell) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:@"strategyCell"] autorelease];
	}
	if (indexPath.row<=[[self strategyTypes] count]) {
		cell.text = [self.strategyTypes objectAtIndex:indexPath.row];
	}
	return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    self.selectedStrategy = [strategyTypes objectAtIndex:[indexPath row]];
}	

/*
 // The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if ((self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil])) {
        // Custom initialization
    }
    return self;
}
*/

/*
// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView {
}
*/

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
	self.strategyTypes = [NSArray arrayWithObjects:@"R3", @"RSI25", nil];
}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}


- (void)dealloc {
	[strategyTypes release];
	[selectedStrategy release];
	[super dealloc];
}


@end
