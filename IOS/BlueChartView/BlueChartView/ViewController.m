//
//  ViewController.m
//  BlueChartView
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import "ViewController.h"

#define kScreenWidth [UIScreen mainScreen].bounds.size.width  //屏幕宽度
#define kScreenHeight [UIScreen mainScreen].bounds.size.height //屏幕高度

#define UIColorFromRGB(rgbValue) [UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 \
green:((float)((rgbValue & 0xFF00) >> 8))/255.0 \
blue:((float)(rgbValue & 0xFF))/255.0 alpha:1.0]

#define COLOR_RED_NORMAL [[UIColor redColor] colorWithAlphaComponent:0.8]

// 页面背景
#define COLOR_VIEW_BG UIColorFromRGB(0xf3f3f3)
// 字体主色
#define COLOR_MAIN_TINTCOLOR UIColorFromRGB(0x6c6c6c)
// 主风格颜色
#define COLOR_MAIN_STYLECOLOR UIColorFromRGB(0xc0904b)
// 着重表现色
#define COLOR_KEY_COLOR UIColorFromRGB(0xea6201)
// 红色
#define COLOR_RED_COLOR UIColorFromRGB(0xff0000)
// 绿色
#define COLOR_GREEN_COLOR UIColorFromRGB(0x03a200)

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    CGFloat width = [UIScreen mainScreen].bounds.size.width;
    CGFloat height = [UIScreen mainScreen].bounds.size.height;
    self.chartViewLayout = [[ChartViewLayout alloc]initWithFrame:CGRectMake(50, 88, width - 100, 200)];
    self.chartViewLayout.onChartListener = self;
    [self.view addSubview:self.chartViewLayout];
    
    self.chartLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 44, kScreenWidth, 20)];
    self.chartLabel.textAlignment = NSTextAlignmentCenter;
    [self.view addSubview:self.chartLabel];
    
    [self getChartLines];
}

-(void)onMove:(NSInteger)index moveX:(CGFloat)moveX{
    self.chartLabel.text = [NSString stringWithFormat:@"index = %ld，moveX = %f",index,moveX];
}

-(void)getChartLines{
    NSMutableArray *chartLines = [NSMutableArray new];
    
    UIColor *color1 = [UIColor colorWithRed:234/255.0 green:98/255.0 blue:16/255.0 alpha:1];
    [chartLines addObject:[self getRandomChartLine:@"曲线1" color:color1]];
    UIColor *color2 = [UIColor colorWithRed:105/255.0 green:182/255.0 blue:234/255.0 alpha:1];
    [chartLines addObject:[self getRandomChartLine:@"曲线2" color:color2]];
    
    [self.chartViewLayout setChartLines:chartLines];
    [self.chartViewLayout refresh];
}

-(ChartLine *)getRandomChartLine:(NSString*)title color:(UIColor*)color {
    NSMutableArray *points = [NSMutableArray new];
    for (int i = 0; i < 6; i++) {
        int r = arc4random_uniform(10);
        if (i % 10 == 0) {
            r = -arc4random_uniform(15);
        }
        float yValue = 88 + 2.632*r;
        [points addObject:[[ChartLinePoint alloc]init:i+1 yValue:yValue drawPoint:NO]];
    }
    return [[ChartLine alloc]init:title lineColor:color points:points drawBessel:NO];
}

@end
