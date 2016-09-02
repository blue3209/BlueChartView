//
//  ChartCalculator.h
//  BlueChartView 趋势图计算类
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "ChartData.h"
#import "ChartStyle.h"
#import "ChartUtils.h"
#import "Label.h"
#import "ChartLine.h"
#import "ChartLinePoint.h"

@interface ChartCalculator : NSObject

/** 图形的高度 */
@property (nonatomic,assign) CGFloat height;
/** 图形的宽度 */
@property (nonatomic,assign) CGFloat width;

/** 绘制曲线图的高度 */
@property (nonatomic,assign) CGFloat chartHeight;

/** 纵轴的宽度 */
@property (nonatomic,assign) CGFloat yAxisWidth;
/** 纵轴的高度 */
@property (nonatomic,assign) CGFloat yAxisHeight;

/** 横轴的高度 */
@property (nonatomic,assign) CGFloat xAxisHeight;
/** 横轴的长度 */
@property (nonatomic,assign) CGFloat xAxisWidth;

/** 横轴的标题的高度 */
@property (nonatomic,assign) CGFloat xTitleHeight;

/** 画布X轴的平移，用于实现曲线图的滚动效果 */
@property (nonatomic,assign) CGFloat translateX;

/** 光滑因子 */
@property (nonatomic,assign) CGFloat smoothness;

/** 最左侧和最右侧坐标 */
@property (nonatomic,assign) CGFloat minX, maxX;
@property (nonatomic,assign) CGFloat maxRightX;
/** 点与点之间的距离 */
@property (nonatomic,assign) CGFloat labelWidth;

/** 灰色竖线顶点 */
@property (nonatomic,retain) NSMutableArray *gridChartLinePoints;

@property (nonatomic,retain) ChartStyle *style;
@property (nonatomic,retain) ChartData *data;

-(instancetype)initWith:(ChartData *)data style:(ChartStyle *)style;

-(void)compute:(CGFloat)width height:(CGFloat)height;

-(void)computeIndex;

-(Label *)computeMoveIndex:(CGFloat)moveX;

-(void)move:(CGFloat)distanceX;

@end
