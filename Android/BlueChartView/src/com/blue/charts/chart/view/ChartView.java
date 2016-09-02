package com.blue.charts.chart.view;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Path;
import android.support.v4.view.ViewCompat;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

import com.blue.charts.chart.util.ChartCalculator;
import com.blue.charts.chart.util.ChartData;
import com.blue.charts.chart.util.ChartData.Label;
import com.blue.charts.chart.util.ChartLine;
import com.blue.charts.chart.util.ChartLinePoint;
import com.blue.charts.chart.util.ChartStyle;
import com.blue.charts.chart.util.ChartUtils;
import com.blue.charts.chart.view.ChartViewLayout.OnChartListener;

/**
 * ChartView绘制视图
 * 
 * Author:chengli3209@gmail.com 2016/08/25 10:20
 */
public class ChartView extends View {
	private static final float DRAG_RATE = 0.5f;
	private static final int ANIMATE_SCROLL_DURATION = 300;

	/** 通用画笔 */
	private Paint paint;
	/** 曲线的路径，用于绘制曲线 */
	private Path curvePath;

	/** 曲线图绘制的计算信息 */
	private ChartCalculator calculator;
	/** 曲线图的样式 */
	private ChartStyle style;
	/** 曲线图的数据 */
	private ChartData data;

	/** 手势解析 */
	private GestureDetector detector;
	/** 曲线图事件监听器 */
	private OnChartListener chartListener;

	/** 线的宽度 */
	private float lineWidth = 3f;

	/** 是否绘制全部贝塞尔结点 */
	public boolean drawBesselPoint;

	private boolean isLongTouchDown;
	private float moveX;

	/** 线性加速 */
	private DecelerateInterpolator mDecelerateInterpolator = null;
	/** 滚动的起始位置 */
	private float mFrom;

	public ChartView(Context context, ChartData data, ChartStyle style,
			final ChartCalculator calculator) {
		super(context);
		this.calculator = calculator;
		this.data = data;
		this.style = style;
		this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.paint.setAntiAlias(true);
		this.curvePath = new Path();
		this.drawBesselPoint = true;

		this.mDecelerateInterpolator = new DecelerateInterpolator(2f);
		this.detector = new GestureDetector(getContext(),
				new SimpleOnGestureListener() {
					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,
							float distanceX, float distanceY) {
						// ChartUtils.log(
						// "onScroll",
						// distanceX + "//"
						// + Math.abs(distanceX * DRAG_RATE));
						if (Math.abs(distanceX * DRAG_RATE) >= 4) {
							getParent()
									.requestDisallowInterceptTouchEvent(true);
							ChartView.this.calculator.move(distanceX
									* DRAG_RATE);
							invalidateView();
							return true;
						}
						return false;
					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// 参数解释：
						// e1：第1个ACTION_DOWN MotionEvent
						// e2：最后一个ACTION_MOVE MotionEvent
						// velocityX：X轴上的移动速度，像素/秒
						// velocityY：Y轴上的移动速度，像素/秒
						animateScroll(-velocityX, calculator.yAxisWidth);
						invalidateView();
						return true;
					}

					@Override
					public boolean onDown(MotionEvent e) {
						// 停止滚动的动画
						clearAnimation();
						invalidateView();
						return true;
					}

					@Override
					public void onLongPress(MotionEvent e) {
						super.onLongPress(e);
						isLongTouchDown = true;
						moveX = e.getX();
						invalidateView();
					}
				});
	}

	/**
	 * 动画主要是用来进行平滑滚动的
	 */
	private final Animation mAnimateScrollPosition = new Animation() {
		@Override
		public void applyTransformation(float interpolatedTime, Transformation t) {
			float targetTop = mFrom * (interpolatedTime);

			calculator.move(targetTop);
			invalidateView();
		}
	};

	private void invalidateView() {
		ViewCompat.postInvalidateOnAnimation(this);
	}

	/**
	 * 从from位置移动到to的位置的动画
	 * 
	 * @param from
	 * @param to
	 * @param listener
	 */
	private void animateScroll(float from, float to) {
		mFrom = from * DRAG_RATE;
		mAnimateScrollPosition.reset();
		mAnimateScrollPosition.setDuration(ANIMATE_SCROLL_DURATION);
		mAnimateScrollPosition.setInterpolator(mDecelerateInterpolator);

		clearAnimation();
		startAnimation(mAnimateScrollPosition);
	}

	public void setOnChartListener(OnChartListener chartListener) {
		this.chartListener = chartListener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isLongTouchDown) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				moveX = event.getX();
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				isLongTouchDown = false;
				break;
			default:
				break;
			}
			invalidateView();
			return true;
		} else {
			return detector.onTouchEvent(event);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (ChartUtils.isEmpty(data.getLineList())) {
			drawEmpty(canvas);
			return;
		}
		calculator.ensureTranslation();
		canvas.translate(calculator.getTranslateX(), 0);
		drawCurveAndPoints(canvas);
		drawHorLabelsAndLine(canvas);

		if (isLongTouchDown) {
			drawCrossLine(canvas);
		}
	}

	/**
	 * 绘制十字线
	 * 
	 * @param canvas
	 */
	private void drawCrossLine(Canvas canvas) {
		paint.setColor(style.getVerticalCrossLineColor());
		paint.setStrokeWidth(style.getVerticalCrossLineWidth());

		float x = moveX - calculator.getTranslateX();
		if (x > calculator.maxX) {
			canvas.drawLine(calculator.maxX, 0, calculator.maxX,
					calculator.yAxisHeight, paint);
			return;
		}

		if (x < calculator.minX) {
			canvas.drawLine(calculator.minX, 0, calculator.minX,
					calculator.yAxisHeight, paint);
			return;
		}

		Label label = calculator.computeMoveIndex(x);
		if (null == label) {
			return;
		}
		if (null != chartListener) {
			chartListener.onMove(label.index, x);
		}
		// 绘制竖线
		canvas.drawLine(label.x, 0, label.x, calculator.yAxisHeight, paint);
	}

	/** 绘制曲线和结点 */
	private void drawCurveAndPoints(Canvas canvas) {
		paint.setStrokeWidth(lineWidth);
		for (ChartLine line : data.getLineList()) {
			paint.setColor(line.getLineColor());
			paint.setAlpha(255);
			curvePath.reset();
			List<ChartLinePoint> list = null;
			if (line.isDrawBesselLine()) {
				// 绘制贝塞尔曲线
				list = line.getBesselPoints();
				int size = list.size();
				for (int i = 0; i < size; i = i + 3) {
					if (i == 0) {
						curvePath.moveTo(list.get(i).x, list.get(i).y);
					} else {
						curvePath.cubicTo(list.get(i - 2).x, list.get(i - 2).y,
								list.get(i - 1).x, list.get(i - 1).y,
								list.get(i).x, list.get(i).y);
					}
				}
			} else {
				// 绘制普通曲线
				list = line.getPoints();
				for (int i = data.getmStartIndex(); i < data.getmEndIndex(); i++) {
					if (i == data.getmStartIndex()) {
						curvePath.moveTo(list.get(i).x, list.get(i).y);
					} else {
						curvePath.lineTo(list.get(i).x, list.get(i).y);
					}
				}
			}
			paint.setStrokeCap(Cap.ROUND);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawPath(curvePath, paint);// 绘制光滑曲线

			paint.setAlpha(102);
			curvePath.lineTo(calculator.maxX, calculator.yAxisHeight);
			curvePath.lineTo(calculator.minX, calculator.yAxisHeight);
			curvePath.close();
			paint.setStyle(Paint.Style.FILL);
			canvas.drawPath(curvePath, paint);// 绘制区域

			/*
			 * paint.setStyle(Paint.Style.FILL); if (drawBesselPoint) { // 绘制结点
			 * for (ChartLinePoint point : line.getPoints()) { if
			 * (point.isDrawPoint()) { canvas.drawCircle(point.x, point.y, 5,
			 * paint); paint.setAlpha(80); canvas.drawCircle(point.x, point.y,
			 * 10, paint); paint.setAlpha(255); } } // 绘制贝塞尔控制结点 for
			 * (ChartLinePoint point : list) { if
			 * (!line.getPoints().contains(point)) { paint.setColor(Color.BLUE);
			 * paint.setAlpha(255); canvas.drawCircle(point.x, point.y, 5,
			 * paint); } } }
			 */
		}
		paint.setAlpha(255);
	}

	/** 绘制横轴 */
	private void drawHorLabelsAndLine(Canvas canvas) {
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(1);
		paint.setColor(style.getHorizontalLabelTextColor());
		paint.setTextSize(style.getHorizontalLabelTextSize());
		paint.setTextAlign(Align.CENTER);

		int size = data.getxLabels().size();
		for (int i = data.getmStartIndex(); i < data.getmEndIndex(); i++) {
			if (i % 6 == 0) {
				// 绘制橫坐标文本
				Label label = data.getxLabels().get(i);
				canvas.drawText(label.text, label.x, label.y, paint);
			}
		}

		paint.setColor(style.getGridColor());
		paint.setStrokeWidth(1);
		float endCoordinateX = calculator.xAxisWidth;
		float coordinateY = getHeight() - calculator.xAxisHeight;
		canvas.drawLine(0, coordinateY, endCoordinateX, coordinateY, paint);

		size = data.getyLabels().size();
		for (int i = 0; i < size; i++) {
			Label label = data.getyLabels().get(i);
			float y = (i == 0) ? 0 : label.y;
			// 绘制橫线
			canvas.drawLine(0, y, endCoordinateX, y, paint);
		}
	}

	/**
	 * 绘制空数据的情况
	 */
	private void drawEmpty(Canvas canvas) {
		paint.setColor(style.getGridColor());
		paint.setStrokeWidth(1);

		int count = data.getyLabelCount();
		float vy = (getHeight() - 48) / count;
		for (int i = 0; i < count; i++) {
			float y = vy * i;
			// 绘制橫线
			canvas.drawLine(48, y, getWidth(), y, paint);
		}

		String empty = "暂无数据";
		paint.setTextSize(52);
		canvas.drawText(empty, (getWidth() - paint.measureText(empty)) / 2,
				(getHeight() - 48 - 52) / 2, paint);
	}

	public void updateSize() {
		LayoutParams lp = getLayoutParams();
		lp.height = calculator.chartHeight;
		lp.width = calculator.xAxisWidth;
		setLayoutParams(lp);
	}

	public void setDrawBesselPoint(boolean drawBesselPoint) {
		this.drawBesselPoint = drawBesselPoint;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		clearAnimation();
	}
}
