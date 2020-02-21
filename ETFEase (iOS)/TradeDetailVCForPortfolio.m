    //
//  TradeDetailVCForPortfolio.m
//  ETFEase
//
//  Created by Michael Lindeboom on 12/31/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "TradeDetailVCForPortfolio.h"


@implementation TradeDetailVCForPortfolio
@synthesize etfNameLbl;
@synthesize etfSymbolLbl;
@synthesize portfolioTab;
@synthesize chart;


- (IBAction)cancel:(id)sender {
	[self dismissModalViewControllerAnimated:YES];
}

- (IBAction)remove:(id)sender{
	UIAlertView *alert = [[[UIAlertView alloc] initWithTitle:@"Remove" message:@"Remove from portfolio?" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:nil] autorelease];
    // optional - add more buttons:
    [alert addButtonWithTitle:@"Yes"];
    [alert show];
	
}


- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 1) {
		
		VariableStore *vs = [VariableStore sharedInstance];
		NSMutableArray *symbols =[[[NSMutableArray alloc]init]autorelease];
		[symbols addObjectsFromArray: vs.symbols];
		NSUInteger i = [symbols indexOfObject:etfSymbolLbl.text];
		if (i!=NSNotFound) {
			[symbols removeObjectAtIndex:i];
			vs.symbols = symbols;
			vs.isPortfolioChanged=YES;
			NSLog(@"%@ removed from portfolio", etfSymbolLbl.text);
		}
		[self dismissModalViewControllerAnimated:YES];
	}
}


- (void)dealloc {
	[etfNameLbl release];
	[etfSymbolLbl release];
	[portfolioTab release];
	[chart release];
    [super dealloc];
}


- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	
	static NSString *TradeDetailCellIdentifier = @"TradeDetailCellIdentifier";
	TradeDetailCell *cell = (TradeDetailCell *)[tv dequeueReusableCellWithIdentifier:TradeDetailCellIdentifier];
	UIColor *defaultTextColor;
	
	if (cell == nil) {
		NSArray *nibObjects = [[NSBundle mainBundle] loadNibNamed:@"TradeDetailCell" owner:nil options:nil];
		for (id currentObject in nibObjects){
			if ([currentObject isKindOfClass:[TradeDetailCell class]]) {
				cell = (TradeDetailCell *)currentObject;
			}
		}
		
		defaultTextColor = [cell.gainLbl textColor];
	}
	
	NSArray *cellContent = [self getCellContent:indexPath];
	
	[cell.tradeDateLbl setText: [cellContent objectAtIndex:0]];
	[cell.sharesAndPriceLbl setText: [cellContent objectAtIndex:1]];
	
	
	NSString *s = [cellContent objectAtIndex:2];
	NSRange textRange =[s rangeOfString:@"Loss"];
	
	if(textRange.location != NSNotFound)
	{
		[cell.gainLbl setTextColor:UIColor.redColor];
	} else {
		[cell.gainLbl setTextColor:[TradeDetailsViewController colorWithHexString:@"009933"]];
	}
	
	
	[cell.gainLbl setText: [cellContent objectAtIndex:2]];
	
	return cell;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	ETF *etf = self.portfolioTab.selectedETF;
	NSString *strategy = [[VariableStore sharedInstance] strategy];	
	NSMutableArray *trades = [[etf getTrades: strategy] trades];
	int i = [trades count];
	return i/2;
}



-(void)viewWillAppear:(BOOL)animated { 
	[super viewWillAppear:animated];
	ETF* etf = self.portfolioTab.selectedETF;
	NSString *imageURLString = [NSString stringWithFormat:@"http://www.google.com/finance/chart?cht=o&q=%@&tlf=12&chs=m&p=1d&client=sfa",etf.symbol];
	
	NSLog(@"%@",imageURLString);
	NSURL *imageURL = [NSURL URLWithString:imageURLString];
	
	NSData *mydata = [[NSData alloc] initWithContentsOfURL:imageURL];
	UIImage *myimage = [[UIImage alloc] initWithData:mydata];	
	
	self.chart.image=myimage;
	[[self etfNameLbl] setText:etf.name];
	[[self etfSymbolLbl] setText:etf.symbol];
	[tableView reloadData];
}


-(NSArray*)getCellContent:(NSIndexPath *)indexPath 
{
	NSString *buyDate;
	NSString *sellDate; 
	NSNumber *buyAmt;
	NSNumber *sellAmt;
	NSNumber *gain;
	NSNumber *shares;
	ETF *etf = self.portfolioTab.selectedETF;
	NSString *strategy = [[VariableStore sharedInstance] strategy];	
	NSArray *trades = [[etf getTrades: strategy] trades];
	
	int i;
	int j=0;
	for (i=[trades count]-1; i>=0; i--) {
		
		Trade *trade = [trades objectAtIndex:i];
		
		if ([trade.shares intValue]<0){
			j++;
			//record previous trade as a buy if current trade is a sell
			Trade *prevTrade = [trades objectAtIndex:i-1];
			buyDate = prevTrade.date;
			buyAmt = prevTrade.tradeAmount;
			
			sellDate = trade.date;
			sellAmt = trade.tradeAmount;
			gain = [NSNumber numberWithFloat:([sellAmt floatValue] - [buyAmt floatValue])];
			shares = [NSNumber numberWithInt:(-[trade.shares intValue])];
			
			if (indexPath.row==j-1)
			{
				NSMutableArray *ma = [[[NSMutableArray alloc] init] autorelease];
				NSMutableString *s = [[[NSMutableString alloc] initWithString:@""] autorelease];
				NSMutableString *s1 = (NSMutableString *)[buyDate stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
				NSMutableString *s2 = (NSMutableString *)[sellDate stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
				
				[s appendString:s1];
				[s appendString:@" to "];
				[s appendString:s2];
				[ma addObject:s];
				
				NSMutableString *s3 = [[[NSMutableString alloc] initWithString:@""] autorelease];
				[s3 appendString:[shares stringValue]];
				[s3 appendString:@" shares for $"];
				[s3 appendString:[trade.tradeAmount stringValue]];
				[ma addObject:s3];
				
				
				NSMutableString *s4 = [[[NSMutableString alloc] initWithString:@""] autorelease];
				
				if ([gain intValue]<0) {
					[s4 appendString:@"Loss: $"];
					[s4 appendString:[gain stringValue]];
				} else {
					[s4 appendString:@"Gain: $"];
					[s4 appendString:[gain stringValue]];
				}
				[ma addObject:s4];
				
				return (NSMutableArray *)ma;
				
			}
		}
	}
	
	return nil;
	
}

+ (UIColor *) colorWithHexString: (NSString *) stringToConvert{
	NSString *cString = [[stringToConvert stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] uppercaseString];
	// String should be 6 or 8 characters
	if ([cString length] < 6) return [UIColor blackColor];
	// strip 0X if it appears
	if ([cString hasPrefix:@"0X"]) cString = [cString substringFromIndex:2];
	if ([cString length] != 6) return [UIColor blackColor];
	// Separate into r, g, b substrings
	NSRange range;
	range.location = 0;
	range.length = 2;
	NSString *rString = [cString substringWithRange:range];
	range.location = 2;
	NSString *gString = [cString substringWithRange:range];
	range.location = 4;
	NSString *bString = [cString substringWithRange:range];
	// Scan values
	unsigned int r, g, b;
	[[NSScanner scannerWithString:rString] scanHexInt:&r];
	[[NSScanner scannerWithString:gString] scanHexInt:&g];
	[[NSScanner scannerWithString:bString] scanHexInt:&b];
	
	return [UIColor colorWithRed:((float) r / 255.0f)
						   green:((float) g / 255.0f)
							blue:((float) b / 255.0f)
						   alpha:1.0f];
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




@end
