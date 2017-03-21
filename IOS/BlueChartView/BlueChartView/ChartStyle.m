//
//  ChartStyle.m
//  BlueChartView
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import "ChartStyle.h"

@implementation ChartStyle

- (instancetype)init
{
    self = [super init];
    if (self) {
        self.gridColor = [UIColor lightGrayColor];
        self.horizontalTitleTextSize = 12;
        self.horizontalTitleTextColor = [UIColor lightGrayColor];
        
        self.horizontalLabelTextSize = 10;
        self.horizontalLabelTextColor = [UIColor lightGrayColor];
        
        self.verticalLabelTextSize = 10;
        self.verticalLabelTextColor = [UIColor lightGrayColor];
        
        self.axisLineWidth = 2;
        self.chartLineWidth = 3;
        self.radius = 4;
        
        self.horizontalTitlePaddingLeft = 20;
        self.horizontalTitlePaddingRight = 10;
        
        self.verticalCrossLineColor = [UIColor grayColor];
        self.verticalCrossLineWidth = 1;
        
        self.isShowLegendView = NO;
    }
    return self;
}

@end
