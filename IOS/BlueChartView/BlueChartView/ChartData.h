//
//  ChartData.h
//  BlueChartView 趋势图数据类
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Label.h"
#import "ChartUtils.h"
#import "ChartLine.h"
#import "ChartLinePoint.h"

#define DEF_YLABEL_COUNT 6  //默认的y坐标文本刻度的个数
#define DEF_YLABEL_USED  0  //默认横轴使用第一个序列来显示文本

//协议方法，主要来处理x,y坐标值的显示
@protocol LabelTransform <NSObject>

@required
-(NSString *) verticalTransform:(float)valueY;      //纵坐标显示文本
-(NSString *) horizontalTransform:(float)valueX;    //横坐标显示文本
-(BOOL) labelDrawing:(float)valueY;                 //是否显示指定位置的坐标文本

@end

@interface ChartData : NSObject<LabelTransform>

@property (nonatomic,retain) NSMutableArray *lineList;  //曲线集合
@property (nonatomic,retain) NSMutableArray *xLabels;   //x坐标文本集合

@property (nonatomic,retain) NSMutableArray *yLabels;   //y坐标文本集合
@property (nonatomic,retain) NSMutableArray *titles;    //曲线标题集合

@property (nonatomic,assign) NSInteger maxValueY;       //y坐标最大值
@property (nonatomic,assign) NSInteger minValueY;       //y坐标最小值

@property (nonatomic,assign) NSInteger maxPointsCount;  //最大点数

@property (nonatomic,assign) NSInteger yLabelCount;     //纵坐标显示文本的数量
@property (nonatomic,assign) NSInteger xMaxLabelCount;  //横坐标显示文本的数量

@property (nonatomic,assign) NSInteger xLabelUsageSeries;//使用哪一个series的横坐标来显示横坐标文本

@property (nonatomic,assign) int mStartIndex;     //当前区域内的左索引
@property (nonatomic,assign) int mEndIndex;       //当前区域内的右索引


@property (nonatomic,assign) id labelTransform;         //自定义协议

-(void)resetYLabels;

@end
