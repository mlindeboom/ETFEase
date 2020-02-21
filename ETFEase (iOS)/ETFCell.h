//
//  ETFCell.h
//  ETFEase
//
//  Created by Michael Lindeboom on 12/24/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface ETFCell : UITableViewCell {
	UILabel *etfSymbolLbl;
	UILabel *etfNameLbl;
	UILabel *etfTotalLbl;
	UILabel *etfTotalAmtLbl;
	UILabel *etfBuySellLbl;
}

@property (nonatomic,retain) IBOutlet UILabel *etfSymbolLbl;
@property (nonatomic,retain) IBOutlet UILabel *etfNameLbl;
@property (nonatomic,retain) IBOutlet UILabel *etfTotalLbl;
@property (nonatomic,retain) IBOutlet UILabel *etfTotalAmtLbl;
@property (nonatomic,retain) IBOutlet UILabel *etfBuySellLbl;


@end
