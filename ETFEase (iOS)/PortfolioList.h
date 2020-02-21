//
//  PortfolioList.h
//  ETFEase
//
//  Created by Michael Lindeboom on 12/31/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VariableStore.h"
#import "ETF.h";
#import "PortfolioTab.h";
#import "UrlDownloaderOperation.h"
@class PortfolioTab;


@interface PortfolioList : ETFList {

	PortfolioTab *portfolioTab;
} 


-(id)initWithPortfolioView: (PortfolioTab *) inPortfolioTab;
-(void) getData;
-(void) refresh;


		
@end
