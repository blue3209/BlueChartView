//
//  ChartView.h
//  BlueChartView 曲线图
//
//  Created by blue on 16/8/31.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ChartData.h"
#import "ChartStyle.h"
#import "ChartCalculator.h"

@protocol OnChartListener <NSObject>

-(void)onMove:(NSInteger)index moveX:(CGFloat)moveX;

@end

@protocol OnDisplay <NSObject>

-(void)onNeedDisplay;

@end

@interface ChartView : UIView

/** 曲线图绘制的计算信息 */
@property (nonatomic,strong) ChartCalculator *calculator;
/** 曲线图的样式 */
@property (nonatomic,strong) ChartStyle *style;
/** 曲线图的数据 */
@property (nonatomic,strong) ChartData *data;

@property (nonatomic,assign) id onChartListener;
@property (nonatomic,assign) id onDisplay;

/** 绘制文字字体 */
@property (nonatomic,strong) UIFont *textFont;
@property (nonatomic,strong) NSDictionary *textAttributes;

@property (nonatomic,assign) BOOL isLongTouchDown;
@property (nonatomic,assign) CGFloat longTouchMoveX;

@end
