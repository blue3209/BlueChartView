//
//  ChartLine.h
//  BlueChartView 趋势图中的线
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "Title.h"

@interface ChartLine : NSObject

@property (nonatomic,copy) NSString         *lineTitle;     //曲线标题
@property (nonatomic,assign) NSInteger      lineIndex;      //曲线索引
@property (nonatomic,assign) UIColor        *lineColor;      //曲线颜色

@property (nonatomic,assign) NSInteger      lineWidth;      //曲线宽度
@property (nonatomic,retain) NSMutableArray *points;        //曲线中点得集合
@property (nonatomic,retain) NSMutableArray *besselPoints;  //曲线中贝塞尔点得集合

@property (nonatomic,retain) Title          *title;         //曲线中标题类
@property (nonatomic,assign) BOOL           isDrawBesselLine;//是否绘制贝塞尔曲线

-(instancetype)init:(NSString *)lineTitle lineColor:(UIColor *)lineColor points:(NSMutableArray *)points drawBessel:(BOOL)isDrawBesselLine;

@end
