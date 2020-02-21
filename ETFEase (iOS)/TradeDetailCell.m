//
//  TradeDetailCell.m
//  ETFEase
//
//  Created by Michael Lindeboom on 12/30/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "TradeDetailCell.h"


@implementation TradeDetailCell

@synthesize tradeDateLbl;
@synthesize sharesAndPriceLbl;
@synthesize gainLbl;


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    if ((self = [super initWithStyle:style reuseIdentifier:reuseIdentifier])) {
        // Initialization code
    }
    return self;
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {

    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}


- (void)dealloc {
    [super dealloc];
}



@end
