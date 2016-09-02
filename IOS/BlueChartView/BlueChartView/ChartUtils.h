//
//  ChartUtils.h
//  BlueChartView
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

@interface ChartUtils : NSObject

+(BOOL) isEmpty:(NSMutableArray *) array;

+(float) widthForString:(NSString *)value fontSize:(float)fontSize;
+(float) heightForString:(NSString *)value fontSize:(float)fontSize;

@end
