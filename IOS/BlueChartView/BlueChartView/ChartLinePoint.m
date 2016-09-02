//
//  ChartLinePoint.m
//  BlueChartView
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import "ChartLinePoint.h"

@implementation ChartLinePoint

-(instancetype)init:(float)xValue yValue:(float)yValue drawPoint:(BOOL)isDrawPoint{
    self = [super init];
    
    if(self){
        self.xValue = xValue;
        self.yValue = yValue;
        self.isDrawPoint = isDrawPoint;
    }
    
    return self;
}

-(instancetype)initXY:(float)x y:(float)y drawPoint:(BOOL)isDrawPoint{
    self = [super init];
    
    if(self){
        self.x = x;
        self.y = y;
        self.isDrawPoint = isDrawPoint;
    }
    
    return self;
}

@end
