//
//  ChartLine.m
//  BlueChartView
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import "ChartLine.h"

@implementation ChartLine

-(instancetype)init:(NSString *)lineTitle lineColor:(UIColor *)lineColor points:(NSMutableArray *)points drawBessel:(BOOL)isDrawBesselLine{
    self = [super init];
    if (self) {
        self.lineTitle = lineTitle;
        self.lineColor = lineColor;
        self.points = points;
        
        self.title = [[Title alloc]init:lineTitle color:lineColor];
        self.besselPoints = [NSMutableArray new];
        
        self.isDrawBesselLine = isDrawBesselLine;
    }
    return self;
}
@end
