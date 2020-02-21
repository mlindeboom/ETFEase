//
//  Portfolio.m
//  ETFEase
//
//  Created by Michael Lindeboom on 12/22/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "PortfolioTab.h"


@implementation PortfolioTab

@synthesize portfolioData;
@synthesize activityIndicator;
@synthesize selectedETF;




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



-(IBAction) refresh: (id) sender
{
	[portfolioList getData];
}


- (void)viewDidLoad {
	portfolioList = [[PortfolioList alloc] initWithPortfolioView:self];
	[super viewDidLoad];
}


- (void)dealloc {
	[portfolioData release];
	[portfolioList release];
	[activityIndicator release];
	[super dealloc];
}


-(void) reloadPortfolioTab
{
	[portfolioList refresh];
	[tableView reloadData];
}



- (void) didStartDataLoadOpertion{
	NSLog(@"didStartDataLoadOpertion: start");
} 

- (void) didFinishDataLoadOpertion{
	[self turnOffActivityIndicator];
	[tableView reloadData];
	NSLog(@"didFinishDataLoadOpertion: end");
	
}


-(NSArray*) filterSortStrategy{
	
	return [portfolioList etfsThatAreSorted];

}


- (void)tableView:(UITableView *)tv didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	
	NSArray *etfs=[self filterSortStrategy];
	self.selectedETF=[etfs objectAtIndex:indexPath.row];
	[self presentModalViewController:tradeDetailVCForPortfolio animated:YES];
	[tv deselectRowAtIndexPath:indexPath animated:YES];
 
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	NSLog(@"tableView: numberOfRowsInSection");
	if (portfolioList != nil) {
		
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


-(void) viewWillAppear:(BOOL)animated{
	//check if the portfolio has changed -- if so update the data
	VariableStore *vs = [VariableStore sharedInstance];
	if (vs.isPortfolioChanged==YES) {
		[self turnOnActivityIndicator];
		[portfolioList getData];
		vs.isPortfolioChanged=NO;
	}

	
	[super viewWillAppear:animated];
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




@end
