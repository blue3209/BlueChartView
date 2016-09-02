//
//  Label.m
//  BlueChartView
//
//  Created by blue on 16/8/30.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import "Label.h"

@implementation Label

-(instancetype)init:(float)value text:(NSString *)text{
    self = [super init];
    
    if(self){
        self.value = value;
        self.text = text;
    }
    
    return self;
}

@end
