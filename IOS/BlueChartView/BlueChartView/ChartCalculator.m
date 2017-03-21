//
//  ChartCalculator.m
//  BlueChartView
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import "ChartCalculator.h"

@implementation ChartCalculator

-(instancetype)initWith:(ChartData *)data style:(ChartStyle *)style {
    self = [super init];
    
    if(self){
        self.data = data;
        self.style = style;
        
        self.translateX = 0.0f;
        self.smoothness = 0.5f;
        
        self.gridChartLinePoints = [NSMutableArray new];
    }
    
    return self;
}

#pragma 计算图形绘制的参数信息
-(void)compute:(CGFloat)width height:(CGFloat)height{
    self.width = width;
    self.height = height;
    self.translateX = 0;
    
    [self.data resetYLabels];
    [self computeAllWidthAndHeight:width height:height];
    
    if([ChartUtils isEmpty:self.data.lineList]){
        return;
    }

    [self computeVerticalAxisInfo];     //计算纵轴参数
    [self computeHorizontalAxisInfo];   //计算横轴参数
    [self computeTitlesInfo];
    [self computeChartLineCoordinate];
    [self computeBesselChartLinePoints];
    [self computeGridChartLinePoints];
}

//初始化的时候计算其索引值
-(void)computeIndex{
    if(![ChartUtils isEmpty:self.data.lineList]){
        if(self.data.lineList.count <= self.data.xLabelUsageSeries){
            return;
        }
        
        ChartLine *chartLine = [self.data.lineList objectAtIndex:self.data.xLabelUsageSeries];
        int count = (short)chartLine.points.count;
        
        if(count <= self.data.xMaxLabelCount){
            self.data.mStartIndex = 0;
            self.data.mEndIndex = count;
        }else{
            self.data.mStartIndex = (short)(count - self.data.xMaxLabelCount);
            self.data.mEndIndex = (short)(self.data.mStartIndex + self.data.xMaxLabelCount);
        }
        
        NSLog(@"start = %d,end = %d",self.data.mStartIndex,self.data.mEndIndex);
    }
}

//计算绘制试图的宽高
-(void)computeAllWidthAndHeight:(CGFloat)width height:(CGFloat)height{
    // y轴的宽度
    NSString *maxText = [self getMaxLengthText];
    self.yAxisWidth = [ChartUtils widthForString:maxText fontSize:self.style.verticalLabelTextSize] * 1.5f;
    
    // x轴的高度
    NSString *text = @"测";
    self.xAxisHeight = [ChartUtils heightForString:text fontSize:self.style.horizontalLabelTextSize] * 1.5f;
    
    // 计算示例图的高度
    self.xTitleHeight = 0;
    if(self.style.isShowLegendView){
        ChartLine *line = [self.data.lineList objectAtIndex:0];
        text = line.lineTitle;
        self.xTitleHeight = [ChartUtils heightForString:text fontSize:self.style.horizontalTitleTextSize] * 2.0f;
    }
    
    //点之间间距
    self.labelWidth = (width - self.yAxisWidth)/(self.data.xMaxLabelCount - 1);
    //最右侧的x坐标
    self.maxRightX = (width - self.yAxisWidth) - self.xAxisWidth;
    
    //绘制曲线区域的宽度
    self.xAxisWidth = (width - self.yAxisWidth);
    //绘制曲线区域的高度
    self.chartHeight = height - self.xTitleHeight;
    //绘制纵轴的高度
    self.yAxisHeight = self.chartHeight - self.xAxisHeight;
}

//获取长度最长的字符串
-(NSString *)getMaxLengthText{
    NSString *max = @"";
    
    for(Label *label in self.data.yLabels) {
        if([label.text length] > [max length]){
            max = label.text;
        }
    }
    
    return max;
}

//计算纵轴参数
-(void)computeVerticalAxisInfo{
    CGFloat vh = [ChartUtils heightForString:@"测" fontSize:self.style.verticalLabelTextSize];
    
    NSMutableArray *yLabels = self.data.yLabels;
    NSInteger yLableCount = [yLabels count];
    CGFloat padding = self.yAxisHeight / yLableCount;
    
    for(int i = 0; i < yLableCount; i++){
        Label *label = [yLabels objectAtIndex:i];
        label.index = i;
        CGFloat w = [ChartUtils widthForString:label.text fontSize:self.style.verticalLabelTextSize];
        label.width = w;
        label.height = vh;
        
        label.x = self.yAxisWidth - w;
        
        if(i == 0){
            label.y = 0;
        }else{
            label.y = padding * i;
        }
        label.drawingY = label.y + vh;
    }
}

//计算横轴参数
-(void)computeHorizontalAxisInfo{
    NSMutableArray *xLabels = self.data.xLabels;
    CGFloat vh = [ChartUtils heightForString:@"测" fontSize:self.style.horizontalLabelTextSize];
    NSInteger count = xLabels.count;
    
    for(int i = self.data.mStartIndex; i < self.data.mEndIndex;i++){
        Label *label = [xLabels objectAtIndex:i];
        label.index = i;
        label.width = [ChartUtils widthForString:label.text fontSize:self.style.verticalLabelTextSize];;
        label.height = vh;
        
        label.x = self.labelWidth * ((i - self.data.mStartIndex));
        if(i == self.data.mStartIndex){
            label.x += self.style.radius;
        }else if(i == self.data.mEndIndex - 1){
            label.x -= self.style.radius;
        }
        
        label.y = self.yAxisHeight;
        
        if(i == 0 || (i == self.data.mEndIndex - 1 && self.data.mEndIndex == count - 1)){
            label.drawingX = label.x;
        }else{
            label.drawingX = label.x - (label.width) / 2.0f;
        }
        label.drawingY = self.yAxisHeight;
        
        if(i == self.data.mStartIndex){
            self.minX = label.x;//最小值
        }else if(i == self.data.mEndIndex - 1){
            self.maxX = label.x;//最大值
        }
    }
}

//计算标题的坐标
-(void)computeTitlesInfo{
    if(self.style.isShowLegendView){
        NSMutableArray *titles = self.data.titles;
        NSInteger count = [titles count];
        CGFloat stepX = (self.width - self.style.horizontalTitlePaddingLeft - self.style.horizontalTitlePaddingRight)/count;
    
        for(Title *title in titles){
            title.radius = 10.0f;
            title.circleTextPadding = 10.0f;
            [title updateTextRect:self.style.horizontalTitleTextSize maxWidth:stepX];
            title.textX = self.style.horizontalTitlePaddingLeft
            + ([titles indexOfObject:title] + 0.5f) * stepX;
            title.textY = self.xTitleHeight * 0.75f;
            
            title.circleX = title.textX - title.width / 2 - title.circleTextPadding - title.radius;
            title.circleY = title.textY - title.height * 0.5f + 5;
        }
    }
}

//计算序列坐标信息
-(void)computeChartLineCoordinate{
    NSMutableArray *yLabels = self.data.yLabels;
    CGFloat minCoordinateY = ((Label *)[yLabels objectAtIndex:0]).y;
    CGFloat maxCoordinateY = ((Label *)[yLabels objectAtIndex:yLabels.count - 1]).y;
    
    for(ChartLine *chartLine in self.data.lineList){
        float pointWidth = self.labelWidth;
        for(int i = self.data.mStartIndex;i < self.data.mEndIndex;i++){
            ChartLinePoint *point = [chartLine.points objectAtIndex:i];
            point.x = pointWidth * ((i - self.data.mStartIndex));
            if(i == self.data.mStartIndex){
                point.x += self.style.radius;
            }else if(i == self.data.mEndIndex - 1){
                point.x -= self.style.radius;
            }
            float ratio = (point.yValue - self.data.minValueY) / (self.data.maxValueY - self.data.minValueY);
            point.y = maxCoordinateY - (maxCoordinateY - minCoordinateY) * ratio;
        }
    }
}

//计算贝塞尔节点
-(void)computeBesselChartLinePoints{
    for (ChartLine *chartLine in self.data.lineList) {
        NSMutableArray *besselChartLinePoints = chartLine.besselPoints;
        NSMutableArray *chartLinePoints = [NSMutableArray new];
        
        for (int i = self.data.mStartIndex; i < self.data.mEndIndex; i++) {
            ChartLinePoint *chartLinePoint = [chartLine.points objectAtIndex:i];
            if (chartLinePoint.yValue > 0){
                [chartLinePoints addObject:chartLinePoint];
            }
        }
        
        NSInteger count = chartLinePoints.count;
        if (count < 2)
            continue;
        
        [besselChartLinePoints removeAllObjects];
        for (int i = 0; i < count; i++) {
            if (i == 0 || i == count - 1) {
                [self computeUnMonotoneChartLinePoints:i points:chartLinePoints besselPoints:besselChartLinePoints];
            } else {
                ChartLinePoint *p0 = [chartLinePoints objectAtIndex:i - 1];
                ChartLinePoint *p1 = [chartLinePoints objectAtIndex:i];
                ChartLinePoint *p2 = [chartLinePoints objectAtIndex:i + 1];
                if ((p1.y - p0.y) * (p1.y - p2.y) >= 0) {// 极值点
                    [self computeUnMonotoneChartLinePoints:i points:chartLinePoints besselPoints:besselChartLinePoints];
                } else {
                    [self computeMonotoneChartLinePoints:i points:chartLinePoints besselPoints:besselChartLinePoints];
                }
            }
        }
    }
}

/** 计算非单调情况的贝塞尔结点 */
-(void)computeUnMonotoneChartLinePoints:(NSInteger)i points:(NSMutableArray *)chartLinePoints besselPoints:(NSMutableArray *)besselChartLinePoints {
    if (i == 0) {
        ChartLinePoint *p1 = [chartLinePoints objectAtIndex:0];
        ChartLinePoint *p2 = [chartLinePoints objectAtIndex:1];
        
        [besselChartLinePoints addObject:p1];
        [besselChartLinePoints addObject:[[ChartLinePoint alloc]initXY:p1.x + (p2.x - p1.x)
                                          * self.smoothness y:p1.y drawPoint:NO]];
    } else if (i == chartLinePoints.count - 1) {
        ChartLinePoint *p0 = [chartLinePoints objectAtIndex:i - 1];
        ChartLinePoint *p1 = [chartLinePoints objectAtIndex:i];
        
        [besselChartLinePoints addObject:[[ChartLinePoint alloc]initXY:p1.x - (p1.x - p0.x)
                                           * self.smoothness y:p1.y drawPoint:NO]];
        [besselChartLinePoints addObject:p1];
    } else {
        ChartLinePoint *p0 = [chartLinePoints objectAtIndex:i - 1];
        ChartLinePoint *p1 = [chartLinePoints objectAtIndex:i];
        ChartLinePoint *p2 = [chartLinePoints objectAtIndex:i + 1];
        
        [besselChartLinePoints addObject:[[ChartLinePoint alloc]initXY:p1.x - (p1.x - p0.x)
                                           * self.smoothness y:p1.y drawPoint:NO]];
        [besselChartLinePoints addObject:p1];
        [besselChartLinePoints addObject:[[ChartLinePoint alloc]initXY:p1.x + (p2.x - p1.x)
                                           * self.smoothness y:p1.y drawPoint:NO]];
    }
}

//计算单调情况的贝塞尔结点
-(void) computeMonotoneChartLinePoints:(NSInteger)i points:(NSMutableArray *)chartLinePoints besselPoints:(NSMutableArray*)besselChartLinePoints {
    ChartLinePoint *p0 = [chartLinePoints objectAtIndex:i - 1];
    ChartLinePoint *p1 = [chartLinePoints objectAtIndex:i];
    ChartLinePoint *p2 = [chartLinePoints objectAtIndex:i + 1];
    float k = (p2.y - p0.y) / (p2.x - p0.x);
    float b = p1.y - k * p1.x;
    ChartLinePoint *p01 = [ChartLinePoint new];
    p01.x = p1.x - (p1.x - (p0.y - b) / k) * self.smoothness;
    p01.y = k * p01.x + b;
    [besselChartLinePoints addObject:p01];
    [besselChartLinePoints addObject:p1];
    ChartLinePoint *p11 = [ChartLinePoint new];
    p11.x = p1.x + (p2.x - p1.x) * self.smoothness;
    p11.y = k * p11.x + b;
    [besselChartLinePoints addObject:p11];
}

//计算网格顶点
-(void)computeGridChartLinePoints{
    [self.gridChartLinePoints removeAllObjects];
    
    NSMutableArray *chartLineList = self.data.lineList;
    for(ChartLine *chartLine in chartLineList){
        for(int i = self.data.mStartIndex ;i < self.data.mEndIndex;i++){
            ChartLinePoint *point = [chartLine.points objectAtIndex:i];
            //NSInteger index = [chartLine.points indexOfObject:point];
            [self.gridChartLinePoints addObject:point];
        }
    }
}

//计算当前移动的点在哪个区域索引Label
-(Label *)computeMoveIndex:(CGFloat)moveX{
    if(self.labelWidth <= 0){
        return nil;
    }
    
    NSInteger index = (int)(moveX / self.labelWidth) + self.data.mStartIndex;
    NSInteger count = self.data.xLabels.count;
    if(count > 0 && index >= 0 && index < count){
        return [self.data.xLabels objectAtIndex:index];
    }
    return nil;
}

//计算平移画布
-(void)move:(CGFloat)distanceX{
    NSInteger moveIndex = (int)(fabs(distanceX) / self.labelWidth);
    if(moveIndex <= 0){
        moveIndex = 1;
    }else if(moveIndex > 1){
        moveIndex = 1;
    }
    
    if([ChartUtils isEmpty:self.data.lineList]){
        return;
    }
    
    ChartLine *line = [self.data.lineList objectAtIndex:0];
    NSInteger count = line.points.count;
    if(count <= self.data.xMaxLabelCount){
        return;
    }
    
    NSInteger start = self.data.mStartIndex;
    NSInteger end = self.data.mEndIndex;
    
    if(distanceX < 0){
        start = start + moveIndex;
    }else {
        start = start - moveIndex;
        if(start <= 0){
            start = 0;
        }
    }
    
    if(start >= count - self.data.xMaxLabelCount){
        start = count - self.data.xMaxLabelCount;
    }
    end = start + self.data.xMaxLabelCount;
    
    self.data.mStartIndex = (short)start;
    self.data.mEndIndex = (short)end;
    [self compute:self.width height:self.height];
}

@end
