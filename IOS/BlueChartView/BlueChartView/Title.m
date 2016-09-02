//
//  Title.m
//  BlueChartView
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import "Title.h"

@implementation Title

-(instancetype)init:(NSString *)title color:(UIColor *)color{
    self = [super init];
    if(self){
        self.text = title;
        self.color = color;
    }
    return self;
}

-(void)updateTextRect:(float)fontSize maxWidth:(float)maxWidth{
    CGFloat textWidth = [self textCircleWidth:fontSize];
    if(textWidth <= maxWidth){
        return;
    }
    
    while (textWidth > maxWidth) {
        self.text = [self.text substringToIndex:(self.text.length - 1)];
        textWidth = [self textCircleWidth:fontSize];
    }
    
    self.text = [self.text substringToIndex:(self.text.length - 1)];
    self.text = [self.text stringByAppendingString:@"..."];
    [self textCircleWidth:fontSize];
}

-(CGFloat)textCircleWidth:(float)fontSize{
    CGFloat textWidth = [ChartUtils widthForString:self.text fontSize:fontSize];
    self.width = textWidth;
    self.height = [ChartUtils heightForString:self.text fontSize:fontSize];
    textWidth = textWidth + (self.radius + self.circleTextPadding)*2;
    return textWidth;
}

@end
