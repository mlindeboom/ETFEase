//
//  ETFCell.m
//  ETFEase
//
//  Created by Michael Lindeboom on 12/24/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "ETFCell.h"


@implementation ETFCell

@synthesize etfSymbolLbl;
@synthesize etfNameLbl;
@synthesize etfTotalLbl;
@synthesize etfTotalAmtLbl;
@synthesize etfBuySellLbl;


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
