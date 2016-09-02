//
//  Label.h
//  BlueChartView 曲线图的Label主要针对x、y坐标的文字
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

@interface Label : NSObject

@property (nonatomic,assign) CGFloat    x;          //文本对应的坐标x
@property (nonatomic,assign) CGFloat    y;          //文本对应的坐标y
@property (nonatomic,assign) CGFloat    drawingX;   //文本对应的绘制坐标X
@property (nonatomic,assign) CGFloat    drawingY;   //文本对应的绘制坐标y
@property (nonatomic,assign) CGFloat    width;      //文本对应的宽度
@property (nonatomic,assign) CGFloat    height;     //文本对应的高度

@property (nonatomic,copy) NSString     *text;      //文本内容
@property (nonatomic,assign) float      value;      //文本对应的实际值
@property (nonatomic,assign) NSInteger  index;      //索引位置

-(instancetype)init:(float)value text:(NSString *)text;

@end
