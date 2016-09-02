//
//  ChartData.m
//  BlueChartView
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import "ChartData.h"

@implementation ChartData

- (instancetype)init
{
    self = [super init];
    if (self) {
        self.xLabels = [NSMutableArray new];
        self.yLabels = [NSMutableArray new];
        self.lineList = [NSMutableArray new];
        self.titles = [NSMutableArray new];
        
        self.labelTransform = self;
        
        self.maxPointsCount  = 50;
        self.xMaxLabelCount = 50;
        self.yLabelCount = DEF_YLABEL_COUNT;
        self.xLabelUsageSeries = DEF_YLABEL_USED;
    }
    return self;
}

#pragma mark LabelTransform委托方法
-(NSString *)horizontalTransform:(float)valueX{
    return [NSString stringWithFormat:@"%.2f",valueX];
}

-(NSString *)verticalTransform:(float)valueY{
    return [NSString stringWithFormat:@"%.2f",valueY];
}

-(BOOL)labelDrawing:(float)valueY{
    return YES;
}

#pragma mark 数据处理

-(void)setLineList:(NSMutableArray *)lineList{
    _lineList = lineList;
    if(![ChartUtils isEmpty:_lineList]){
        [self resetData];
    }
}

-(void)resetData{
    if([self.lineList count] <= self.xLabelUsageSeries){
        return;
    }
        
    [self resetXLabels];
    [self resetYLabels];
        
    [self.titles removeAllObjects];
    for(ChartLine *line in self.lineList){
        [self.titles addObject:line.title];
        if([line.points count] > self.maxPointsCount){
            self.maxPointsCount = [line.points count];
        }
    }
}

-(void)resetXLabels{
    [self.xLabels removeAllObjects];
    
    ChartLine *line = [self.lineList objectAtIndex:self.xLabelUsageSeries];
    Label *label = nil;
    for(ChartLinePoint *point in line.points){
        if([self labelDrawing:point.xValue]){
            label = [[Label alloc]init:point.xValue text:[self horizontalTransform:point.xValue]];
            [self.xLabels addObject:label];
        }
    }
}

-(void)resetYLabels{
    self.maxValueY = 0;
    self.minValueY = NSIntegerMax;
    [self.yLabels removeAllObjects];
    
    for(ChartLine *line in self.lineList){
        NSMutableArray *points = line.points;
        for(int i = self.mStartIndex;i < self.mEndIndex;i++){
            ChartLinePoint *point = [points objectAtIndex:i];
            if(point.yValue > self.maxValueY){
                self.maxValueY = point.yValue;
            }
            
            if(point.yValue > 0 && point.yValue < self.minValueY){
                self.minValueY = point.yValue;
            }
        }
    }
    NSLog(@"1resetYLabels:maxValueY = %ld,minValueY = %ld",self.maxValueY,self.minValueY);
    
    NSInteger step = (self.maxValueY - self.minValueY)/(self.yLabelCount - 1);
    self.minValueY = self.minValueY - step;
    self.maxValueY = self.maxValueY + step;
    step = (self.maxValueY - self.minValueY)/(self.yLabelCount - 1);
    
    float value = 0;
    for(int i = 0;i < self.yLabelCount;i++){
        value = self.minValueY + step * i;
        [self.yLabels insertObject:[[Label alloc] init:value text:[self verticalTransform:value]] atIndex:0];
    }
    
    self.maxValueY = value;
    
    NSLog(@"2resetYLabels:maxValueY = %ld,minValueY = %ld",self.maxValueY,self.minValueY);
}

@end
