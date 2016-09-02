//
//  ViewController.h
//  BlueChartView
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ChartViewLayout.h"

@interface ViewController : UIViewController<OnChartListener>

@property (nonatomic,strong) ChartViewLayout *chartViewLayout;

@property (nonatomic,strong) UILabel *chartLabel;

@end

