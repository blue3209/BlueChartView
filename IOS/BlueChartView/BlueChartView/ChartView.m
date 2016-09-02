//
//  ChartView.m
//  BlueChartView
//
//  Created by blue on 16/8/31.
//  Copyright © 2016年 chengli. All rights reserved.
//

#import "ChartView.h"

#define DRAG_RATE 0.5f
#define ANIMATE_SCROLL_DERATION 5
#define DEFAULT_VELOCITY 2000

@implementation ChartView

-(instancetype)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if(self){
        //长按手势
        UILongPressGestureRecognizer *longRecognizer = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(onLongTouch:)];
        [self addGestureRecognizer:longRecognizer];
        
        //滑动手势
        UIPanGestureRecognizer *panRecognizer = [[UIPanGestureRecognizer alloc]initWithTarget:self action:@selector(onPanTouch:)];
        [self addGestureRecognizer:panRecognizer];
        
        self.isLongTouchDown = false;
        self.longTouchMoveX = 0.0f;
        
        [panRecognizer requireGestureRecognizerToFail:longRecognizer];
        
        self.onChartListener = self;
    }
    return self;
}

-(void)setStyle:(ChartStyle *)style{
    _style = style;
    
    self.textFont = [UIFont systemFontOfSize:self.style.horizontalLabelTextSize - 1];
    self.textAttributes = @{NSFontAttributeName:self.textFont,NSForegroundColorAttributeName:self.style.verticalLabelTextColor};
}

#pragma mark 绘制
-(void)drawRect:(CGRect)rect{
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetAllowsAntialiasing(context,YES);
    CGContextSaveGState(context);
    
    if([ChartUtils isEmpty:self.data.lineList]){
        [self drawEmpty:context rect:rect];
        return;
    }
    
    [self drawHorLabelsAndLine:context rect:rect];
    [self drawCurveAndPoints:context rect:rect];
    
    if(self.isLongTouchDown){
        [self drawCrossLine:context rect:rect];
    }
    
    CGContextRestoreGState(context);
}

//绘制空
-(void)drawEmpty:(CGContextRef)context rect:(CGRect)rect{
    CGContextSetStrokeColorWithColor(context, self.style.gridColor.CGColor);
    CGContextSetLineWidth(context, 0.5);
    
    NSInteger count = self.data.yLabelCount;
    CGFloat vy = (self.calculator.chartHeight - self.calculator.xAxisHeight) / count;
    
    CGPoint points[2];
    for(int i = 0;i < count+1;i++){
        points[0] = CGPointMake(0, vy*i);
        points[1] = CGPointMake(self.calculator.xAxisWidth, vy*i);
        
        CGContextAddLines(context, points, 2);
    }
    CGContextDrawPath(context, kCGPathStroke);
}

//绘制曲线和结点
-(void)drawCurveAndPoints:(CGContextRef)context rect:(CGRect)rect{
    CGContextSetLineWidth(context, self.style.chartLineWidth);
    NSMutableArray *chartLines = self.data.lineList;
    for(ChartLine *line in chartLines){
        CGContextSetStrokeColorWithColor(context, line.lineColor.CGColor);
        if(line.isDrawBesselLine){
            //绘制贝塞尔曲线
            NSInteger count = line.besselPoints.count;
            for(int i = 0;i < count;i = i + 3){
                ChartLinePoint *p = [line.besselPoints objectAtIndex:i];
                if(i == 0){
                    CGContextMoveToPoint(context, p.x, p.y);
                }else{
                    ChartLinePoint *p1 = [line.besselPoints objectAtIndex:i - 1];
                    ChartLinePoint *p2 = [line.besselPoints objectAtIndex:i - 2];
                    CGContextAddCurveToPoint(context, p2.x, p2.y, p1.x, p1.y, p.x, p.y);
                }
            }
            
            CGContextSetLineCap(context, kCGLineCapRound);
            CGContextDrawPath(context, kCGPathStroke);
            
            for(int i = 0;i < count;i = i + 3){
                ChartLinePoint *p = [line.besselPoints objectAtIndex:i];
                if(i == 0){
                    CGContextMoveToPoint(context, p.x, p.y);
                }else{
                    ChartLinePoint *p1 = [line.besselPoints objectAtIndex:i - 1];
                    ChartLinePoint *p2 = [line.besselPoints objectAtIndex:i - 2];
                    CGContextAddCurveToPoint(context, p2.x, p2.y, p1.x, p1.y, p.x, p.y);
                }
            }
        }else{
            //绘制普通曲线
            for(int i = self.data.mStartIndex;i < self.data.mEndIndex;i++){
                ChartLinePoint *p = [line.points objectAtIndex:i];
                if(i == self.data.mStartIndex){
                    CGContextMoveToPoint(context, p.x, p.y);
                }else{
                    CGContextAddLineToPoint(context, p.x, p.y);
                }
            }
            
            CGContextSetLineCap(context, kCGLineCapRound);
            CGContextDrawPath(context, kCGPathStroke);
            
            for(int i = self.data.mStartIndex ;i < self.data.mEndIndex;i++){
                ChartLinePoint *point = [line.points objectAtIndex:i];
                if(i == self.data.mStartIndex){
                    CGContextMoveToPoint(context, point.x, point.y);
                }else{
                    CGContextAddLineToPoint(context, point.x, point.y);
                }
            }
        }
        
        UIColor *fillColor = [line.lineColor colorWithAlphaComponent:0.4f];
        CGContextSetFillColorWithColor(context, fillColor.CGColor);
        CGContextAddLineToPoint(context,self.calculator.maxX,self.calculator.yAxisHeight);
        CGContextAddLineToPoint(context,self.calculator.minX,self.calculator.yAxisHeight);
        CGContextClosePath(context);
        CGContextDrawPath(context, kCGPathFill);
    }
}

//绘制横坐标文字和横线
-(void)drawHorLabelsAndLine:(CGContextRef)context rect:(CGRect)rect{
    CGContextSetAllowsAntialiasing(context,true);
    CGContextSetTextDrawingMode(context, kCGTextFill);

    for(int i = self.data.mStartIndex;i < self.data.mEndIndex;i++){
        if(i % 6 == 0){
            Label *label = [self.data.xLabels objectAtIndex:i];
            [label.text drawInRect:CGRectMake(label.drawingX,label.drawingY,label.width, label.height) withAttributes:self.textAttributes];
        }
    }
    
    CGContextSetStrokeColorWithColor(context, self.style.gridColor.CGColor);
    CGContextSetLineWidth(context, 0.5);
    
    NSInteger count = self.data.yLabelCount;
    CGFloat vy = (self.calculator.chartHeight - self.calculator.xAxisHeight) / count;
    
    CGPoint points[2];
    for(int i = 0;i < count+1;i++){
        points[0] = CGPointMake(0, vy*i);
        points[1] = CGPointMake(self.calculator.xAxisWidth, vy*i);
        
        CGContextAddLines(context, points, 2);
    }
    CGContextDrawPath(context, kCGPathStroke);
}

//绘制十字线
-(void)drawCrossLine:(CGContextRef)context rect:(CGRect)rect{
    CGContextSetStrokeColorWithColor(context, self.style.verticalCrossLineColor.CGColor);
    CGContextSetLineWidth(context, self.style.verticalCrossLineWidth);
    
    CGFloat x = self.longTouchMoveX;
    CGPoint points[2];
    if(x > self.calculator.maxX){
        points[0] = CGPointMake(self.calculator.maxX, 0);
        points[1] = CGPointMake(self.calculator.maxX, self.calculator.yAxisHeight);
        
        CGContextAddLines(context, points, 2);
        CGContextDrawPath(context, kCGPathStroke);
        return;
    }
    
    if(x < self.calculator.minX){
        points[0] = CGPointMake(self.calculator.minX, 0);
        points[1] = CGPointMake(self.calculator.minX, self.calculator.yAxisHeight);
        
        CGContextAddLines(context, points, 2);
        CGContextDrawPath(context, kCGPathStroke);
        return;
    }
    
    Label *label = [self.calculator computeMoveIndex:x];
    if(nil != label){
        if(nil != self.onChartListener){
            [self.onChartListener onMove:label.index moveX:x];
        }
        
        points[0] = CGPointMake(label.x, 0);
        points[1] = CGPointMake(label.x, self.calculator.yAxisHeight);
        
        CGContextAddLines(context, points, 2);
        CGContextDrawPath(context, kCGPathStroke);
    }
}

#pragma mark 手势监听
-(void)onLongTouch:(UILongPressGestureRecognizer *)recognizer{
    CGPoint touchPoint = [recognizer locationInView:self];
    self.longTouchMoveX = touchPoint.x;
    switch(recognizer.state){
    case UIGestureRecognizerStatePossible:
        //可用
    case UIGestureRecognizerStateBegan:
        //开始
        self.isLongTouchDown = YES;
        break;
    case UIGestureRecognizerStateChanged:
        //改变
        break;
    case UIGestureRecognizerStateEnded:
        //结束
    case UIGestureRecognizerStateCancelled:
        //取消
    case UIGestureRecognizerStateFailed:
        //失败
        self.isLongTouchDown = NO;
        break;
    }
    [self setNeedsDisplay];
}

-(void)onPanTouch:(UIPanGestureRecognizer *)recognizer{
    //停止动画
    //[self.layer removeAllAnimations];
    
    //滑动速率
    CGPoint velocityPoint = [recognizer velocityInView:self];
    //NSLog(@"velocity = %f",velocityPoint.x);
    
    if(fabs(velocityPoint.x) > DEFAULT_VELOCITY){
        //当滑动速率大于2000时认为是fling操作
        [self animateScroll:velocityPoint.x];
        return;
    }
    //滑动位置
    CGPoint touchPoint = [recognizer translationInView:self];
    //NSLog(@"onPanTouch = %f",touchPoint.x);
    
    if(fabs(touchPoint.x * DRAG_RATE) >= 4.0f){
        [self.calculator move:touchPoint.x];
        [self setNeedsDisplay];
        if(nil != self.onDisplay){
            [self.onDisplay onNeedDisplay];
        }
    }
}

//动画运动
-(void)animateScroll:(CGFloat)velocity{
//    [UIView beginAnimations:@"Scroll" context:nil];
//    [UIView setAnimationDuration:ANIMATE_SCROLL_DERATION];
//    [UIView setAnimationDelegate:self];
//    //动画速度先快后慢
//    [UIView setAnimationCurve:UIViewAnimationCurveEaseOut];
//    
//    NSLog(@"-------%f",[UIView inheritedAnimationDuration]);
//    
//    
//    [UIView commitAnimations];
//    
//    [UIView animateWithDuration:ANIMATE_SCROLL_DERATION delay:0 options:UIViewAnimationOptionCurveEaseOut animations:^{
//        NSLog(@"-------%f",[UIView inheritedAnimationDuration]);
//    } completion:^(BOOL finished) {
//        
//    }];

//    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"transform.translation.x"];
//    animation.toValue = [NSNumber numberWithFloat:velocity];
//    animation.duration = ANIMATE_SCROLL_DERATION;
//    animation.repeatCount = 1;
//    animation.removedOnCompletion = YES;
//    animation.fillMode = kCAFillModeRemoved;
//    animation.delegate = self;
//
//    [self.layer addAnimation:animation forKey:@"move"];
}

-(void)animationDidStart:(CAAnimation *)anim{
    NSLog(@"--------开始");
}

-(void)animationDidStop:(CAAnimation *)anim finished:(BOOL)flag{
    NSLog(@"--------结束");
}

@end
