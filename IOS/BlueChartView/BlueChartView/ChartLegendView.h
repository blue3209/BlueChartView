//
//  ChartLegendView.h
//  BlueChartView 示意图
//
//  Created by blue on 16/8/31.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ChartData.h"
#import "ChartStyle.h"
#import "ChartCalculator.h"

@interface ChartLegendView : UIView

/** 曲线图绘制的计算信息 */
@property (nonatomic,strong) ChartCalculator *calculator;
/** 曲线图的样式 */
@property (nonatomic,strong) ChartStyle *style;
/** 曲线图的数据 */
@property (nonatomic,strong) ChartData *data;

@end
