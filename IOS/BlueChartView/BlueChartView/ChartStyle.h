//
//  ChartStyle.h
//  BlueChartView 趋势图样式
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

@interface ChartStyle : NSObject

/** 网格线颜色 */
@property (nonatomic,assign) UIColor        *gridColor;
/** 坐标轴分隔线宽度 */
@property (nonatomic,assign) NSInteger      axisLineWidth;
/** 曲线宽度 */
@property (nonatomic,assign) NSInteger      chartLineWidth;

/** 横坐标文本大小 */
@property (nonatomic,assign) CGFloat        horizontalLabelTextSize;
/** 横坐标文本颜色 */
@property (nonatomic,assign) UIColor        *horizontalLabelTextColor;
/** 横坐标标题文本大小 */
@property (nonatomic,assign) CGFloat        horizontalTitleTextSize;
/** 横坐标标题文本颜色 */
@property (nonatomic,assign) UIColor        *horizontalTitleTextColor;
/** 横坐标标题文本左间距 */
@property (nonatomic,assign) NSInteger      horizontalTitlePaddingLeft;
/** 横坐标标题文本右间距 */
@property (nonatomic,assign) NSInteger      horizontalTitlePaddingRight;

/** 纵坐标文本大小 */
@property (nonatomic,assign) CGFloat        verticalLabelTextSize;
/** 纵坐标文本颜色 */
@property (nonatomic,assign) UIColor        *verticalLabelTextColor;
/** 竖向十字线颜色 */
@property (nonatomic,assign) UIColor        *verticalCrossLineColor;
/** 竖向十字线宽度 */
@property (nonatomic,assign) NSInteger      verticalCrossLineWidth;
/** 圆的半径*/
@property (nonatomic,assign) CGFloat        radius;
/** 是否添加示例图 */
@property (nonatomic,assign) BOOL           isShowLegendView;

@end
