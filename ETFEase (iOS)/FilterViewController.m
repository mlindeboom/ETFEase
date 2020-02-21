    //
//  Filter.m
//  ETFEase
//
//  Created by Michael Lindeboom on 12/27/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "FilterViewController.h"

@implementation FilterViewController
@synthesize filterTypes;
@synthesize selectedFilter;

- (IBAction)cancel:(id)sender {
	[self dismissModalViewControllerAnimated:YES];
}

- (IBAction)done:(id)sender {
	
	VariableStore *vs = [VariableStore sharedInstance];
	vs.filter=selectedFilter;
	
	[listTab reloadListTab];
	[self dismissModalViewControllerAnimated:YES];
}



-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	return [[self filterTypes] count];
}

- (NSInteger)numberOfSections{
	return 0;
}



	
- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	UITableViewCell *cell = [tv dequeueReusableCellWithIdentifier:@"filterCell"];
	if(nil == cell) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:@"filterCell"] autorelease];
	}
	if (indexPath.row<=[[self filterTypes] count]) {
		cell.text = [self.filterTypes objectAtIndex:indexPath.row];
	}
	return cell;
}
	




- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    self.selectedFilter = [filterTypes objectAtIndex:[indexPath row]];
	
    /*NSString *msg = [[NSString alloc] initWithFormat:
					 @"You have selected %@", stateSelected];
    UIAlertView *alert = 
	[[UIAlertView alloc] initWithTitle:@"State selected" 
							   message:msg 
							  delegate:self 
					 cancelButtonTitle:@"OK" 
					 otherButtonTitles:nil];    
    
    [alert show];     
    [alert release];
    [stateSelected release];
    [msg release]; 
	 */
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
	self.filterTypes = [NSArray arrayWithObjects:@"ALL", @"US ETF", @"Non-US ETF", @"Bond ETF", @"Commodity ETF", @"Recent Buys", @"Buy Now", @"Recent Sells", @"Sell Now", nil];
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
	[filterTypes release];
	[selectedFilter release];
    [super dealloc];
}


@end
