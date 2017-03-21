package com.blue.charts.chart.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.blue.charts.chart.util.ChartCalculator;
import com.blue.charts.chart.util.ChartData;
import com.blue.charts.chart.util.ChartLine;
import com.blue.charts.chart.util.ChartStyle;

/**
 * 曲线布局视图
 * 
 * Author:chengli3209@gmail.com 2016/08/25 10:20
 */
public class ChartViewLayout extends LinearLayout {
	/** 曲线图 */
	private ChartView chartView;
	/** 带纵轴的贝塞尔曲线图 */
	private LinearLayout chartLayout;
	/** 横向说明 */
	private ChartLegendView chartLegendView;
	/** 纵轴 */
	public ChartYCoordinateAxesView chartVerticalAxes;

	/** 动画对象 */
	private AnimateRunnable animateRunnable;

	/** 横轴的位置 */
	private int position = ChartYCoordinateAxesView.POSITION_LEFT;
	/** 曲线图绘制的计算信息 */
	private ChartCalculator calculator;
	/** 曲线图的样式 */
	private ChartStyle style;
	/** 曲线图的数据 */
	private ChartData data;

	public ChartViewLayout(Context context) {
		super(context);
		init();
	}

	public ChartViewLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setOrientation(LinearLayout.VERTICAL);
		data = new ChartData(this);
		style = new ChartStyle();
		calculator = new ChartCalculator(data, style);
		
		animateRunnable = new AnimateRunnable();
		chartLayout = new LinearLayout(getContext());
		chartView = new ChartView(getContext(), data, style, calculator);
		chartView.setDrawBesselPoint(true);

		chartVerticalAxes = new ChartYCoordinateAxesView(getContext(),
				data.getyLabels(), style, calculator);
		chartVerticalAxes.setPosition(position);
		chartLayout.setOrientation(LinearLayout.HORIZONTAL);
		chartLayout.addView(chartVerticalAxes, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		if (position == ChartYCoordinateAxesView.POSITION_LEFT) {
			chartLayout.addView(chartView, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
		} else {
			chartLayout.addView(chartView, 0, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
		}

		chartLegendView = new ChartLegendView(getContext(), data.getTitles(),
				style, calculator);

		addView(chartLayout, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		if (style.isShowLegendView()) {
			addView(chartLegendView, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

	private boolean isAdded(View view) {
		int count = this.getChildCount();
		for (int i = 0; i < count; i++) {
			if (view == this.getChildAt(i)) {
				return true;
			}
		}
		return false;
	}

	public void setChartLegendView(boolean isShowLegendView) {
		style.setShowLegendView(isShowLegendView);

		if (isShowLegendView) {
			if (!isAdded(chartLegendView)) {
				addView(chartLegendView, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			}
		} else {
			if (isAdded(chartLegendView)) {
				removeView(chartLegendView);
			}
		}
	}

	/**
	 * 设置曲线集合
	 * 
	 * @param chartLines
	 */
	public void setChartLines(List<ChartLine> chartLines) {
		this.data.setLineList(chartLines);
	}

	/**
	 * 刷新数据
	 * 
	 * @param isAnimation
	 */
	public void refresh(boolean isAnimation) {
		post(new Runnable() {
			@Override
			public void run() {
				calculator.computeIndex();
				calculator.compute(getWidth(), getHeight());// 重新计算图形信息
				chartView.updateSize();// 更新图形的
				chartVerticalAxes.updateSize();// 更新纵轴的宽高
				if (style.isShowLegendView()) {
					chartLegendView.updateHeight();// 更新标题的高度
				}
				invalidate();
				//chartView.animateScrollToEnd(100);
			}
		});
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		animateRunnable.run = false;// 取消动画
	}

	/**
	 * 自动滚动动画
	 */
	private class AnimateRunnable implements Runnable {
		private boolean run = false;

		public void run() {
			int i = 80;
			float k = 0.99f;
			float j = 1f;
			while (run) {
				try {
					if (i > 1) {
						i = (int) (i * Math.pow(k, 3));
						k = k - .01f;
					}
					Thread.sleep(i);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				calculator.move(j);
				run = !calculator.ensureTranslation();
				chartView.postInvalidate();
			}
		};
	}

	public void setOnChartListener(OnChartListener listener) {
		chartView.setOnChartListener(listener);
	}

	/**
	 * 监听事件
	 */
	public interface OnChartListener {
		void onMove(int index, float moveX);
	}

}
