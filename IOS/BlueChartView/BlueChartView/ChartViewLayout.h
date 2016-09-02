//
//  ChartViewLayout.h
//  BlueChartView 曲线图的布局，包含Y轴视图、绘制试图以及示意图
//
//  Created by blue on 16/8/31.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ChartData.h"
#import "ChartStyle.h"
#import "ChartCalculator.h"
#import "ChartView.h"
#import "ChartLegendView.h"
#import "ChartYCoordinateAxesView.h"

@interface ChartViewLayout : UIView<OnDisplay>

/** 曲线图 */
@property (nonatomic,strong) ChartView *chartView;
/** 横向说明 */
@property (nonatomic,strong) ChartLegendView *chartLegendView;
/** 纵轴 */
@property (nonatomic,strong) ChartYCoordinateAxesView *chartVerticalAxes;

/** 曲线图绘制的计算信息 */
@property (nonatomic,strong) ChartCalculator *calculator;
/** 曲线图的样式 */
@property (nonatomic,strong) ChartStyle *style;
/** 曲线图的数据 */
@property (nonatomic,strong) ChartData *data;

@property (nonatomic,assign) id onChartListener;
@property (nonatomic,assign) id onDisplay;

-(void)setChartLines:(NSMutableArray *)chartLines;
-(void)refresh;

@end
