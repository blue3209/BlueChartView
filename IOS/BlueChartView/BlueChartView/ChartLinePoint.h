//
//  ChartLinePoint.h
//  BlueChartView 趋势图中线的点
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

@interface ChartLinePoint : NSObject

@property (nonatomic,assign) CGFloat x;             //曲线点得x坐标
@property (nonatomic,assign) CGFloat y;             //曲线点得y坐标

@property (nonatomic,assign) float xValue;        //实际x值
@property (nonatomic,assign) float yValue;        //实际y值

@property (nonatomic,assign) BOOL    isDrawPoint;   //是否绘制节点

-(instancetype)init:(float)xValue yValue:(float)yValue drawPoint:(BOOL)isDrawPoint;

-(instancetype)initXY:(float)x y:(float)y drawPoint:(BOOL)isDrawPoint;

@end
