package com.blue.charts.chart.util;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;

import com.blue.charts.chart.util.ChartData.Label;
import com.blue.charts.chart.util.ChartData.Title;

/**
 * 数据处理
 * 
 * Author:chengli3209@gmail.com 2016/08/25 10:20
 */
public class ChartCalculator {
	/** 纵坐标文本矩形 */
	public Rect verticalTextRect;
	/** 横坐标文本矩形 */
	public Rect horizontalTextRect;
	/** 横坐标标题文本矩形 */
	public Rect horizontalTitleRect;

	/** 图形的高度 */
	public int height;
	/** 图形的宽度 */
	public int width;

	/** 绘制曲线图的高度 */
	public int chartHeight;

	/** 纵轴的宽度 */
	public int yAxisWidth;
	/** 纵轴的高度 */
	public int yAxisHeight;

	/** 横轴的高度 */
	public int xAxisHeight;
	/** 横轴的长度 */
	public int xAxisWidth;

	/** 横轴的标题的高度 */
	public int xTitleHeight;

	/** 灰色竖线顶点 */
	public ChartLinePoint[] gridChartLinePoints;

	/** 画布X轴的平移，用于实现曲线图的滚动效果 */
	private float translateX;

	/** 用于测量文本区域长宽的画笔 */
	private Paint paint;

	private ChartStyle style;
	private ChartData data;
	/** 光滑因子 */
	private float smoothness;

	/** 最左侧和最右侧坐标 */
	public float minX, maxX;
	public int maxRightX;
	/** 点与点之间的距离 */
	public float labelWidth;

	public ChartCalculator(ChartData data, ChartStyle style) {
		this.data = data;
		this.style = style;
		this.translateX = 0f;
		this.smoothness = 0.5f;
		this.paint = new Paint();
		this.verticalTextRect = new Rect();
		this.horizontalTextRect = new Rect();
		this.horizontalTitleRect = new Rect();
	}

	/**
	 * 计算图形绘制的参数信息
	 * 
	 * @param width
	 *            曲线图区域的宽度
	 * @param height
	 *            曲线图区域的高度
	 */
	public void compute(int width, int height) {
		this.width = width;
		this.height = height;
		this.translateX = 0;
		data.resetYLabels();
		computeAllWidthAndHeight(width, height);

		if (ChartUtils.isEmpty(data.getLineList())) {
			return;
		}
		computeVertcalAxisInfo();// 计算纵轴参数
		computeHorizontalAxisInfo();// 计算横轴参数
		computeTitlesInfo();// 计算标题参数
		computeChartLineCoordinate();// 计算纵轴参数
		computeBesselChartLinePoints();// 计算贝塞尔结点
		computeGridChartLinePoints();// 计算网格顶点
	}

	public void computeIndex() {
		if (!ChartUtils.isEmpty(data.getLineList())) {
			if (this.data.getLineList().size() <= data.getxLabelUsageSeries())
				throw new IllegalArgumentException(
						"xLabelUsageSeries should greater than seriesList.size()");

			int size = data.getLineList().get(data.getxLabelUsageSeries())
					.getPoints().size();
			if (size <= data.getxMaxLabelCount()) {
				data.setmStartIndex(0);
				data.setmEndIndex(size);
			} else {
				data.setmStartIndex(size - data.getxMaxLabelCount());
				data.setmEndIndex(data.getmStartIndex()
						+ data.getxMaxLabelCount());
			}
		}
	}

	/**
	 * 计算所有视图的宽高
	 */
	public void computeAllWidthAndHeight(int width, int height) {
		// y轴的宽度
		paint.setTextSize(style.getVerticalLabelTextSize());
		List<Label> yLabels = data.getyLabels();
		String maxText = getMaxText(yLabels);
		paint.getTextBounds(maxText, 0, maxText.length(), verticalTextRect);
		// 计算y坐标文本的宽度
		yAxisWidth = (int) (verticalTextRect.width() * 1.5f);

		// 横轴的高度
		paint.setTextSize(style.getHorizontalLabelTextSize());
		String rectText = "测";
		paint.getTextBounds(rectText, 0, rectText.length(), horizontalTextRect);
		xAxisHeight = horizontalTextRect.height() * 2;

		// 计算示例图的高度
		xTitleHeight = 0;
		if (style.isShowLegendView()) {
			paint.setTextSize(style.getHorizontalTitleTextSize());
			String titleText = data.getLineList().get(0).getLineTitle();
			paint.getTextBounds(titleText, 0, titleText.length(),
					horizontalTitleRect);
			xTitleHeight = horizontalTitleRect.height() * 2;
		}

		labelWidth = ((width - yAxisWidth) * 1.0f) / data.getxMaxLabelCount();
		xAxisWidth = width - yAxisWidth;
		maxRightX = (width - yAxisWidth) - xAxisWidth;

		// 绘制区域的高度
		chartHeight = height - xTitleHeight;
		// 计算纵轴的高度
		yAxisHeight = chartHeight - xAxisHeight;
	}

	/** 计算纵轴参数 */
	private void computeVertcalAxisInfo() {
		paint.setTextSize(style.getVerticalLabelTextSize());
		// 计算y坐标文本的宽度
		float x = yAxisWidth * 0.5f;
		float vh = verticalTextRect.height();

		List<Label> yLabels = data.getyLabels();
		int yLabelCount = yLabels.size();
		float padding = yAxisHeight / yLabelCount;

		for (int i = 0; i < yLabelCount; i++) {
			Label label = yLabels.get(i);
			label.index = i;
			label.x = x;

			if (i == 0) {
				label.y = vh;
			} else {
				label.y = padding * i;
			}
			label.drawingY = label.y + vh;
		}
	}

	/** 计算横轴参数 */
	private void computeHorizontalAxisInfo() {
		List<Label> xLabels = data.getxLabels();
		paint.setTextSize(style.getHorizontalLabelTextSize());
		// int size = xLabels.size();
		for (int i = data.getmStartIndex(); i < data.getmEndIndex(); i++) {
			Label label = xLabels.get(i);
			label.index = i;
			// label.width = paint.measureText(label.text);
			// label.height = horizontalTextRect.height();

			label.x = labelWidth * ((i - data.getmStartIndex()) + 0.3f);
			label.y = chartHeight - horizontalTextRect.height() * 0.5f;

			/*
			 * label.drawingY = label.y; if (i == 0 || (i == data.getmEndIndex()
			 * - 1 && i == size - 1)) { label.drawingX = label.x; } else {
			 * label.drawingY = label.x - label.width / 2.0f; }
			 */
		}

		minX = xLabels.get(data.getmStartIndex()).x;
		maxX = xLabels.get(data.getmEndIndex() - 1).x;
	}

	/** 计算标题的坐标信息 */
	private void computeTitlesInfo() {
		if (style.isShowLegendView()) {
			paint.setTextSize(style.getHorizontalTitleTextSize());

			List<Title> titles = data.getTitles();
			int count = titles.size();
			float stepX = (width - style.getHorizontalTitlePaddingLeft() - style
					.getHorizontalTitlePaddingRight()) / count;
			for (Title title : titles) {
				title.radius = 10;
				title.circleTextPadding = 10;
				title.updateTextRect(paint, stepX);
				title.textX = style.getHorizontalTitlePaddingLeft()
						+ (titles.indexOf(title) + 0.5f) * stepX;
				title.textY = xTitleHeight * 0.75f;
				title.circleX = title.textX - title.textRect.width() / 2
						- title.circleTextPadding - title.radius;
				title.circleY = title.textY - horizontalTitleRect.height()
						* 0.5f + 5;
			}
		}
	}

	/** 计算序列的坐标信息 */
	private void computeChartLineCoordinate() {
		List<Label> yLabels = data.getyLabels();
		float minCoordinateY = yLabels.get(0).y;
		float maxCoordinateY = yLabels.get(yLabels.size() - 1).y;

		for (ChartLine chartLine : data.getLineList()) {
			List<ChartLinePoint> chartLinePoints = chartLine.getPoints();
			// float chartLinePointWidth = xAxisWidth / chartLinePoints.size();
			float chartLinePointWidth = labelWidth;
			for (int i = data.getmStartIndex(); i < data.getmEndIndex(); i++) {
				ChartLinePoint chartLinePoint = chartLinePoints.get(i);
				// 计算数据点的坐标
				chartLinePoint.x = chartLinePointWidth
						* ((i - data.getmStartIndex()) + 0.3f);
				float ratio = (chartLinePoint.getyValue() - data.getMinValueY())
						/ (float) (data.getMaxValueY() - data.getMinValueY());
				chartLinePoint.y = maxCoordinateY
						- (maxCoordinateY - minCoordinateY) * ratio;
			}
		}
	}

	/**
	 * 获取label中最长的文本
	 * 
	 * @param labels
	 * @return
	 */
	private String getMaxText(List<Label> labels) {
		String maxText = "";
		for (Label label : labels) {
			if (label.text.length() > maxText.length())
				maxText = label.text;
		}
		return maxText;
	}

	/** 计算网格顶点 */
	private void computeGridChartLinePoints() {
		gridChartLinePoints = new ChartLinePoint[data.getMaxPointsCount()];
		List<ChartLine> chartLineList = data.getLineList();
		for (ChartLine chartLine : chartLineList) {
			for (int i = data.getmStartIndex(); i < data.getmEndIndex(); i++) {
				ChartLinePoint chartLinePoint = chartLine.getPoints().get(i);
				int index = chartLine.getPoints().indexOf(chartLinePoint);
				if (gridChartLinePoints[index] == null
						|| gridChartLinePoints[index].getyValue() < chartLinePoint
								.getyValue()) {
					gridChartLinePoints[index] = chartLinePoint;
				}
			}
		}
	}

	/** 计算贝塞尔结点 */
	private void computeBesselChartLinePoints() {
		for (ChartLine chartLine : data.getLineList()) {
			List<ChartLinePoint> besselChartLinePoints = chartLine
					.getBesselPoints();
			List<ChartLinePoint> chartLinePoints = new ArrayList<ChartLinePoint>();
			for (int i = data.getmStartIndex(); i < data.getmEndIndex(); i++) {
				ChartLinePoint chartLinePoint = chartLine.getPoints().get(i);
				if (chartLinePoint.getyValue() > 0)
					chartLinePoints.add(chartLinePoint);
			}

			int count = chartLinePoints.size();
			if (count < 2)
				continue;

			besselChartLinePoints.clear();
			for (int i = 0; i < count; i++) {
				if (i == 0 || i == count - 1) {
					computeUnMonotoneChartLinePoints(i, chartLinePoints,
							besselChartLinePoints);
				} else {
					ChartLinePoint p0 = chartLinePoints.get(i - 1);
					ChartLinePoint p1 = chartLinePoints.get(i);
					ChartLinePoint p2 = chartLinePoints.get(i + 1);
					if ((p1.y - p0.y) * (p1.y - p2.y) >= 0) {// 极值点
						computeUnMonotoneChartLinePoints(i, chartLinePoints,
								besselChartLinePoints);
					} else {
						computeMonotoneChartLinePoints(i, chartLinePoints,
								besselChartLinePoints);
					}
				}
			}
		}
	}

	/** 计算非单调情况的贝塞尔结点 */
	private void computeUnMonotoneChartLinePoints(int i,
			List<ChartLinePoint> chartLinePoints,
			List<ChartLinePoint> besselChartLinePoints) {
		if (i == 0) {
			ChartLinePoint p1 = chartLinePoints.get(0);
			ChartLinePoint p2 = chartLinePoints.get(1);
			besselChartLinePoints.add(p1);
			besselChartLinePoints.add(new ChartLinePoint(p1.x + (p2.x - p1.x)
					* smoothness, p1.y));
		} else if (i == chartLinePoints.size() - 1) {
			ChartLinePoint p0 = chartLinePoints.get(i - 1);
			ChartLinePoint p1 = chartLinePoints.get(i);
			besselChartLinePoints.add(new ChartLinePoint(p1.x - (p1.x - p0.x)
					* smoothness, p1.y));
			besselChartLinePoints.add(p1);
		} else {
			ChartLinePoint p0 = chartLinePoints.get(i - 1);
			ChartLinePoint p1 = chartLinePoints.get(i);
			ChartLinePoint p2 = chartLinePoints.get(i + 1);
			besselChartLinePoints.add(new ChartLinePoint(p1.x - (p1.x - p0.x)
					* smoothness, p1.y));
			besselChartLinePoints.add(p1);
			besselChartLinePoints.add(new ChartLinePoint(p1.x + (p2.x - p1.x)
					* smoothness, p1.y));
		}
	}

	/**
	 * 计算单调情况的贝塞尔结点
	 * 
	 * @param i
	 * @param ChartLinePoints
	 * @param besselChartLinePoints
	 */
	private void computeMonotoneChartLinePoints(int i,
			List<ChartLinePoint> ChartLinePoints,
			List<ChartLinePoint> besselChartLinePoints) {
		ChartLinePoint p0 = ChartLinePoints.get(i - 1);
		ChartLinePoint p1 = ChartLinePoints.get(i);
		ChartLinePoint p2 = ChartLinePoints.get(i + 1);
		float k = (p2.y - p0.y) / (p2.x - p0.x);
		float b = p1.y - k * p1.x;
		ChartLinePoint p01 = new ChartLinePoint();
		p01.x = p1.x - (p1.x - (p0.y - b) / k) * smoothness;
		p01.y = k * p01.x + b;
		besselChartLinePoints.add(p01);
		besselChartLinePoints.add(p1);
		ChartLinePoint p11 = new ChartLinePoint();
		p11.x = p1.x + (p2.x - p1.x) * smoothness;
		p11.y = k * p11.x + b;
		besselChartLinePoints.add(p11);
	}

	public void setSmoothness(float smoothness) {
		this.smoothness = smoothness;
	}

	/**
	 * 计算移动在当前哪个point
	 * 
	 * @param moveX
	 */
	public Label computeMoveIndex(float moveX) {
		if (labelWidth <= 0) {
			return null;
		}
		int index = (int) (moveX / labelWidth) + data.getmStartIndex();
		int size = data.getxLabels().size();
		if (size > 0 && index >= 0 && index < size) {
			return data.getxLabels().get(index);
		}
		return null;
	}

	/**
	 * 平移画布
	 * 
	 * @param distanceX
	 * @param velocityX
	 */
	public void move(float distanceX) {
		int moveIndex = (int) (Math.abs(distanceX) / labelWidth);
		if (moveIndex <= 0) {
			moveIndex = 1;
		} else if (moveIndex > 1) {
			moveIndex = 1;
		}
		if (ChartUtils.isEmpty(data.getLineList())) {
			return;
		}
		int size = data.getLineList().get(0).getPoints().size();
		if (size <= data.getxMaxLabelCount()) {
			return;
		}

		int start = data.getmStartIndex();
		int end = data.getmEndIndex();
		if (distanceX > 0) {
			start = start + moveIndex;
		} else {
			start = start - moveIndex;
			if (start <= 0) {
				start = 0;
			}
		}

		if (start >= size - data.getxMaxLabelCount()) {
			start = size - data.getxMaxLabelCount();
		}
		end = start + data.getxMaxLabelCount();

		data.setmStartIndex(start);
		data.setmEndIndex(end);
		compute(width, height);
		ViewCompat
				.postInvalidateOnAnimation(data.chartViewLayout.chartVerticalAxes);
		translateX = translateX - distanceX;
	}

	/**
	 * 平移画布
	 * 
	 * @param distanceX
	 * @param velocityX
	 */
	public void moveTo(int x) {
		translateX = x;
	}

	public float getTranslateX() {
		return translateX;
	}

	/***
	 * 确保画布的移动不会超出范围
	 * 
	 * @return true,超出范围；false，未超出范围
	 */
	public boolean ensureTranslation() {
		if (translateX >= 0) {
			translateX = 0;
			return true;
		} else if (translateX < 0) {
			if (yAxisWidth != 0 && translateX < maxRightX) {
				translateX = maxRightX;
				return true;
			}
		}

		ChartUtils.log("ensureTranslation", "translateX = " + translateX
				+ ",maxRightX = " + maxRightX);
		return false;
	}
}
