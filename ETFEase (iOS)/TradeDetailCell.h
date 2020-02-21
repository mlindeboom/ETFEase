//
//  TradeDetailCell.h
//  ETFEase
//
//  Created by Michael Lindeboom on 12/30/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface TradeDetailCell : UITableViewCell {
	UILabel *tradeDateLbl;
	UILabel *sharesAndPriceLbl;
	UILabel *gainLbl;
}

@property (nonatomic,retain) IBOutlet UILabel *tradeDateLbl;
@property (nonatomic,retain) IBOutlet UILabel *sharesAndPriceLbl;
@property (nonatomic,retain) IBOutlet UILabel *gainLbl;


@end
