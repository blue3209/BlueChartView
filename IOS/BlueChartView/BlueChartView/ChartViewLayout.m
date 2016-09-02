//
//  ChartViewLayout.m
//  BlueChartView
//
//  Created by blue on 16/8/31.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import "ChartViewLayout.h"

@implementation ChartViewLayout

-(instancetype)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    
    if(self){
        [self setup];
    }
    
    return self;
}

-(void)setup{
    self.data = [[ChartData alloc]init];
    self.style = [[ChartStyle alloc]init];
    self.calculator = [[ChartCalculator alloc]initWith:self.data style:self.style];
    
    self.chartView = [ChartView new];
    self.chartView.data = self.data;
    self.chartView.style = self.style;
    self.chartView.calculator = self.calculator;
    [self addSubview:self.chartView];
    
    self.chartLegendView = [ChartLegendView new];
    self.chartLegendView.data = self.data;
    self.chartLegendView.style = self.style;
    self.chartLegendView.calculator = self.calculator;
    [self addSubview:self.chartLegendView];
    
    self.chartVerticalAxes = [ChartYCoordinateAxesView new];
    self.chartVerticalAxes.data = self.data;
    self.chartVerticalAxes.style = self.style;
    self.chartVerticalAxes.calculator = self.calculator;
    [self addSubview:self.chartVerticalAxes];
    
    self.backgroundColor = [UIColor whiteColor];
    self.chartView.backgroundColor = [UIColor whiteColor];
    self.chartVerticalAxes.backgroundColor = [UIColor whiteColor];
    
    self.onDisplay = self;
}

#pragma 长按索引委托方法
-(void)setOnChartListener:(id)onChartListener{
    _onChartListener = onChartListener;
    self.chartView.onChartListener = onChartListener;
}

#pragma 刷新视图委托方法
-(void)setOnDisplay:(id)onDisplay{
    _onDisplay = onDisplay;
    self.chartView.onDisplay = onDisplay;
}

-(void)setChartLines:(NSMutableArray *)chartLines{
    [self.data setLineList:chartLines];
}

#pragma 通知刷新视图
-(void)onNeedDisplay{
    [self.chartVerticalAxes setNeedsDisplay];
}

//更新所有view的布局
-(void)updateViewFrame{
    self.chartVerticalAxes.frame = CGRectMake(0, 0 , self.calculator.yAxisWidth, self.calculator.yAxisHeight);
    self.chartView.frame = CGRectMake(self.calculator.yAxisWidth, 0 , self.calculator.xAxisWidth, self.calculator.chartHeight);
    
    if(self.style.isShowLegendView){
        self.chartLegendView.frame = CGRectMake(0, self.calculator.chartHeight, self.calculator.width, self.calculator.xTitleHeight);
    }else{
        
        self.chartLegendView.frame = CGRectMake(0, 0 , 0 , 0);
    }
}

-(void)refresh{
    [self.calculator computeIndex];
    [self.calculator compute:self.frame.size.width height:self.frame.size.height];
    [self updateViewFrame];
}

@end
