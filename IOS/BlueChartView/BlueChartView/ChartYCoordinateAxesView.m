//
//  ChartYCoordinateAxesView.m
//  BlueChartView
//
//  Created by blue on 16/8/31.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import "ChartYCoordinateAxesView.h"

@implementation ChartYCoordinateAxesView

-(instancetype)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if(self){
        self.isDrawLine = YES;
    }
    return self;
}

-(void)setStyle:(ChartStyle *)style{
    _style = style;
 
    self.textFont = [UIFont systemFontOfSize:self.style.verticalLabelTextSize - 1];
    self.textAttributes = @{NSFontAttributeName:self.textFont,NSForegroundColorAttributeName:self.style.verticalLabelTextColor};
}

#pragma mark 绘制
-(void)drawRect:(CGRect)rect{
    if([ChartUtils isEmpty:self.data.yLabels]){
        return;
    }
    
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetAllowsAntialiasing(context,true);
    CGContextSetTextDrawingMode(context, kCGTextFill);
    
    for(Label *label in self.data.yLabels){
        [label.text drawInRect:CGRectMake(label.x, label.y,label.width, label.height) withAttributes:self.textAttributes];
    }
    
    if(self.isDrawLine){
        CGContextSetStrokeColorWithColor(context, self.style.gridColor.CGColor);
        CGContextSetLineWidth(context, 0.5);
        
        CGContextMoveToPoint(context, self.calculator.yAxisWidth - 0.5, 0);
        CGContextAddLineToPoint(context, self.calculator.yAxisWidth - 0.5, self.calculator.yAxisHeight);
        CGContextDrawPath(context, kCGPathStroke);
    }
}

@end
