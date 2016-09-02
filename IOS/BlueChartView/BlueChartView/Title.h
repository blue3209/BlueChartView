//
//  Title.h
//  BlueChartView 曲线的标题
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "ChartUtils.h"

@interface Title : NSObject

/** 文本对应的坐标X */
@property (nonatomic,assign) CGFloat textX;
/** 文本对应的坐标Y */
@property (nonatomic,assign) CGFloat textY;
/** 文本 */
@property (nonatomic,copy) NSString *text;
/** 圆点对应的坐标X */
@property (nonatomic,assign) CGFloat circleX;
/** 圆点对应的坐标Y */
@property (nonatomic,assign) CGFloat circleY;
/** 颜色 */
@property (nonatomic,retain) UIColor *color;
/** 圆点的半径 */
@property (nonatomic,assign) CGFloat radius;
/** 图形标注与文本的间距 */
@property (nonatomic,assign) CGFloat circleTextPadding;

@property (nonatomic,assign) CGFloat width;
@property (nonatomic,assign) CGFloat height;

-(instancetype)init:(NSString *)title color:(UIColor *)color;

-(void)updateTextRect:(float)fontSize maxWidth:(float)maxWidth;

@end
