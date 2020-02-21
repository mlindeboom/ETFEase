//
//  ListTab.m
//  ETFEase
//
//  Created by Michael Lindeboom on 12/22/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "ListTab.h"



@implementation ListTab

@synthesize listData;
@synthesize activityIndicator;
@synthesize selectedETF;
@synthesize etfList;



-(void) turnOffActivityIndicator{
	UIActivityIndicatorView *spinner = self.activityIndicator;
	[spinner stopAnimating];
	[spinner removeFromSuperview];
	[spinner release];
	self.activityIndicator=nil;
}

-(void) turnOnActivityIndicator{
	UIActivityIndicatorView *spinner = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
	
	
	CGRect cgRect =[[UIScreen mainScreen] bounds];
	CGSize cgSize = cgRect.size;
	
	[spinner setCenter:CGPointMake(cgSize.width/2, cgSize.height/2)]; 
	[self.view addSubview:spinner]; // spinner is not visible until started
	[spinner startAnimating];
	self.activityIndicator = spinner;
}



- (IBAction)filter:(id)sender {
	[self presentModalViewController:filterViewController animated:YES];
}


-(IBAction) search: (id) sender
{
	[self presentModalViewController:strategyViewController animated:YES];
}

-(IBAction) sort: (id) sender
{
	[self presentModalViewController:sortViewController animated:YES];
}

-(IBAction) refresh: (id) sender
{
	
	[self.etfList getData];
}

-(void) reloadListTab
{
	[self.etfList refresh];
	[tableView reloadData];
}


-(NSArray*) filterSortStrategy{


	NSString *filter = [[VariableStore sharedInstance] filter];

	if ([filter compare:@"ALL"] == NSOrderedSame) {
		return [self.etfList etfsThatAreSorted];
	} else if ([filter compare:@"US ETF"] == NSOrderedSame) {
		return [self.etfList filterEtfsByUsEtf];
	} else if ([filter compare:@"Non-US ETF"] == NSOrderedSame) {
		return [self.etfList filterEtfsByNonUsEtf];
	} else if ([filter compare:@"Bond ETF"] == NSOrderedSame) {
		return [self.etfList filterEtfsByBondEtf];		
	} else if ([filter compare:@"Commodity ETF"] == NSOrderedSame) {
		return [self.etfList filterEtfsByCommodityEtf];		 
	} else if ([filter compare:@"Recent Buys"] == NSOrderedSame) {
		return [self.etfList filterEtfsByRecentBuys];	
	} else if ([filter compare:@"Buy Now"] == NSOrderedSame) {
		return [self.etfList filterEtfsByBuyNow];	
	} else if ([filter compare:@"Recent Sells"] == NSOrderedSame) {
		return [self.etfList filterEtfsByRecentSells];	
	} else if ([filter compare:@"Sell Now"] == NSOrderedSame) {
		return [self.etfList filterEtfsBySellNow];
	} else return nil;
}



- (void)viewDidLoad {
	[self turnOnActivityIndicator];

	ETFList *myList = [[ETFList alloc] initWithView:self]; 
	self.etfList = myList;
	[myList release];

	[self.etfList getData];
	[super viewDidLoad];
}


- (void)dealloc {
	[listData release];
	[etfList release];
	[activityIndicator release];
	[selectedETF release];
	[super dealloc];
}

- (void) didStartDataLoadOpertion{
	NSLog(@"didStartDataLoadOpertion: start");
} 

- (void) didFinishDataLoadOpertion{
	[self turnOffActivityIndicator];
	
	[tableView reloadData];
	NSLog(@"didFinishDataLoadOpertion: end");
	
}


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


- (void)tableView:(UITableView *)tv didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	
	NSArray *etfs=[self filterSortStrategy];
	self.selectedETF=[etfs objectAtIndex:indexPath.row];
	[self presentModalViewController:tradeDetailsViewController animated:YES];
	[tv deselectRowAtIndexPath:indexPath animated:YES];

}



#pragma mark -
#pragma mark Table View Data Source Methods



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	NSLog(@"tableView: numberOfRowsInSection");
	if (etfList != nil) {
		
		NSArray *etfs=[self filterSortStrategy];
		return [etfs count];
	}
	
	else return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	NSLog(@"tableView: next row : %d",indexPath.row);
	static NSString *ETFCellIdentifier = @"ETFCellIdentifier";
	ETFCell *cell = (ETFCell *)[tv dequeueReusableCellWithIdentifier:ETFCellIdentifier];

	if (cell == nil) {
		NSArray *nibObjects = [[NSBundle mainBundle] loadNibNamed:@"ETFCell" owner:nil options:nil];
		for (id currentObject in nibObjects){
			if ([currentObject isKindOfClass:[ETFCell class]]) {
				cell = (ETFCell *)currentObject;
			}
		}
	}

	
	NSArray *etfs=[self filterSortStrategy];
	ETF *etf=[etfs objectAtIndex:indexPath.row];
	VariableStore *vs = [VariableStore sharedInstance];
	Trades *trades = [etf getTrades: vs.strategy];
	
	//populate cell text
	[[cell etfSymbolLbl] setText:etf.symbol];
	[[cell etfNameLbl] setText:etf.name];
	NSString *growthOf10KStrategy = [NSString stringWithFormat:@"Growth %@",vs.strategy];
	[[cell etfTotalLbl] setText: growthOf10KStrategy];
	NSString *growthOf10KAmt = [NSString stringWithFormat:@"%@",[trades.total stringValue]];
	[[cell etfTotalAmtLbl] setText: growthOf10KAmt];
	
	//create text for buy or sell
	if (trades.isRecentBuy==YES||trades.isBuyNow==YES) {
		int last = [[trades trades] count];
		last--;
		Trade *myTrade = [[trades trades] objectAtIndex:last];
		NSString *myDate = [myTrade.date stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
		NSString *buyRecommended = [NSString stringWithFormat:@"Buy recommended on %@",myDate];
		
		[[cell etfBuySellLbl] setText: buyRecommended];
		
	} else if (trades.isRecentSell==YES||trades.isSellNow==YES) {
		int last = [[trades trades] count];
		last--;
		Trade *myTrade = [[trades trades] objectAtIndex:last];
		NSString *sellRecommended = [NSString stringWithFormat:@"Sell recommended on %@",myTrade.date];
		
		[[cell etfBuySellLbl] setText: sellRecommended];
		
	} else {
		[[cell etfBuySellLbl] setText: @""];
	}

	
	return cell;
}

@end
