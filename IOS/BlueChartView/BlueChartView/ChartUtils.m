//
//  ChartUtils.m
//  BlueChartView
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import "ChartUtils.h"

@implementation ChartUtils

+(BOOL)isEmpty:(NSMutableArray *)array{
    if(nil == array || [array count] <= 0){
        return YES;
    }
    return NO;
}

#pragma mark 获取字符串的宽高
//获取字符串的宽度
+(float) widthForString:(NSString *)value fontSize:(float)fontSize {
    CGSize sizeToFit = [value sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:fontSize weight:UIFontWeightLight]}];
    
    return sizeToFit.width;
}

//获得字符串的高度
+(float) heightForString:(NSString *)value fontSize:(float)fontSize {
    CGSize sizeToFit = [value sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:fontSize weight:UIFontWeightLight]}];
    return sizeToFit.height;
}

@end
