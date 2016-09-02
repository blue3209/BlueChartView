package com.blue.charts.chart.util;

import java.util.ArrayList;
import java.util.List;

import com.blue.charts.chart.view.ChartViewLayout;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 数据集合
 * 
 * Author:chengli3209@gmail.com 2016/08/25 10:20
 */
public class ChartData {
	/**
	 * 默认的y坐标文本刻度的个数
	 */
	private static final int DEF_YLABEL_COUNT = 6;
	/**
	 * 默认横轴使用第一个序列来显示文本
	 */
	private static final int DEF_YLABEL_USED = 0;

	/**
	 * 曲线集合
	 */
	private List<ChartLine> lineList;
	/**
	 * x坐标文本集合
	 */
	private List<Label> xLabels;
	/**
	 * y坐标文本集合
	 */
	private List<Label> yLabels;
	private List<Title> titles;

	/**
	 * y坐标最大值
	 */
	private int maxValueY;
	/**
	 * y坐标最小值
	 */
	private int minValueY;
	private int maxPointsCount;
	private LabelTransform labelTransform;
	/** 纵坐标显示文本的数量 */
	private int yLabelCount;
	/** 横坐标显示文本的数量 */
	private int xMaxLabelCount;

	/** 使用哪一个series的横坐标来显示横坐标文本 */
	private int xLabelUsageSeries;

	/** 最左边的索引和最右边的索引 */
	private int mStartIndex, mEndIndex;

	public ChartViewLayout chartViewLayout;

	public ChartData(ChartViewLayout layout) {
		this.chartViewLayout = layout;

		xLabels = new ArrayList<Label>();
		yLabels = new ArrayList<Label>();
		lineList = new ArrayList<ChartLine>();
		titles = new ArrayList<Title>();

		labelTransform = new LabelTransform() {
			@Override
			public String verticalTransform(float valueY) {
				return String.valueOf(valueY);
			}

			@Override
			public String horizontalTransform(float valueX) {
				return String.valueOf(valueX);
			}

			@Override
			public boolean labelDrawing(float valueX) {
				return true;
			}
		};

		maxPointsCount = 50;
		yLabelCount = DEF_YLABEL_COUNT;
		xMaxLabelCount = 50;
		xLabelUsageSeries = DEF_YLABEL_USED;
	}

	public void setLineList(List<ChartLine> lineList) {
		this.lineList.clear();
		if (!ChartUtils.isEmpty(lineList)) {
			this.lineList.addAll(lineList);
			resetData();
		}
	}

	public void resetData() {
		if (!ChartUtils.isEmpty(lineList)) {
			if (this.lineList.size() <= xLabelUsageSeries)
				throw new IllegalArgumentException(
						"xLabelUsageSeries should greater than seriesList.size()");

			resetXLabels();
			resetYLabels();

			titles.clear();

			for (ChartLine line : lineList) {
				titles.add(line.getTitle());
				if (line.getPoints().size() > maxPointsCount)
					maxPointsCount = line.getPoints().size();
			}
		}
	}

	/** 重新生成X坐标轴文本 */
	private void resetXLabels() {
		xLabels.clear();
		for (ChartLinePoint point : this.lineList.get(xLabelUsageSeries)
				.getPoints()) {
			if (labelTransform.labelDrawing(point.getxValue()))
				xLabels.add(new Label(point.getxValue(), labelTransform
						.horizontalTransform(point.getxValue())));
		}
	}

	/** 重新生成Y坐标轴文本 */
	public void resetYLabels() {
		maxValueY = 0;
		minValueY = Integer.MAX_VALUE;
		for (ChartLine line : lineList) {

			List<ChartLinePoint> points = line.getPoints();
			for (int i = mStartIndex; i < mEndIndex; i++) {
				ChartLinePoint point = points.get(i);
				if (point.getyValue() > maxValueY)
					maxValueY = (int) point.getyValue();

				if (point.getyValue() > 0 && point.getyValue() < minValueY)
					minValueY = (int) point.getyValue();
			}
		}

		int step = (maxValueY - minValueY) / (yLabelCount - 1);
		yLabels.clear();
		minValueY = minValueY - step;
		maxValueY = maxValueY + step;
		step = (maxValueY - minValueY) / (yLabelCount - 1);
		int value = 0;
		for (int i = 0; i < yLabelCount; i++) {
			value = minValueY + step * i;
			yLabels.add(0,
					new Label(value, labelTransform.verticalTransform(value)));
		}
		maxValueY = value;
	}

	public List<Title> getTitles() {
		return titles;
	}

	public void setTitles(List<Title> titles) {
		this.titles = titles;
	}

	public List<ChartLine> getLineList() {
		return lineList;
	}

	public LabelTransform getLabelTransform() {
		return labelTransform;
	}

	public void setLabelTransform(LabelTransform labelTransform) {
		this.labelTransform = labelTransform;
	}

	public List<Label> getxLabels() {
		return xLabels;
	}

	public void setxLabels(List<Label> xLabels) {
		this.xLabels = xLabels;
	}

	public List<Label> getyLabels() {
		return yLabels;
	}

	public void setyLabels(List<Label> yLabels) {
		this.yLabels = yLabels;
	}

	public int getMaxValueY() {
		return maxValueY;
	}

	public void setMaxValueY(int maxValueY) {
		this.maxValueY = maxValueY;
	}

	public int getMinValueY() {
		return minValueY;
	}

	public void setMinValueY(int minValueY) {
		this.minValueY = minValueY;
	}

	public int getMaxPointsCount() {
		return maxPointsCount;
	}

	public void setMaxPointsCount(int maxPointsCount) {
		this.maxPointsCount = maxPointsCount;
	}

	public int getxMaxLabelCount() {
		return xMaxLabelCount;
	}

	public void setxMaxLabelCount(int xMaxLabelCount) {
		this.xMaxLabelCount = xMaxLabelCount;
	}

	public int getyLabelCount() {
		return yLabelCount;
	}

	public void setyLabelCount(int yLabelCount) {
		this.yLabelCount = yLabelCount;
	}

	public int getxLabelUsageSeries() {
		return xLabelUsageSeries;
	}

	public void setxLabelUsageSeries(int xLabelUsageSeries) {
		this.xLabelUsageSeries = xLabelUsageSeries;
	}

	public int getmStartIndex() {
		return mStartIndex;
	}

	public void setmStartIndex(int mStartIndex) {
		this.mStartIndex = mStartIndex;
	}

	public int getmEndIndex() {
		return mEndIndex;
	}

	public void setmEndIndex(int mEndIndex) {
		this.mEndIndex = mEndIndex;
	}

	public interface LabelTransform {
		/** 纵坐标显示的文本 */
		String verticalTransform(float valueY);

		/** 横坐标显示的文本 */
		String horizontalTransform(float valueX);

		/** 是否显示指定位置的横坐标文本 */
		boolean labelDrawing(float valueX);
	}

	public class Label {
		/** 文本对应的坐标X */
		public float x;
		/** 文本对应的坐标Y */
		public float y;
		/** 文本对应的绘制坐标X */
		public float drawingX;
		/** 文本对应的绘制坐标Y */
		public float drawingY;
		/** 文本对应的实际数值 */
		public float value;
		/** 文本对应的宽度 */
		public float width;
		/** 文本对应的高度 */
		public float height;
		/** 文本 */
		public String text;
		/** 索引位置 */
		public int index;

		public Label(float value, String text) {
			this.value = value;
			this.text = text;
		}
	}

	public static class Title {
		/** 文本对应的坐标X */
		public float textX;
		/** 文本对应的坐标Y */
		public float textY;
		/** 文本 */
		public String text;
		/** 圆点对应的坐标X */
		public float circleX;
		/** 圆点对应的坐标Y */
		public float circleY;
		/** 颜色 */
		public int color;
		/** 圆点的半径 */
		public int radius;
		/** 图形标注与文本的间距 */
		public int circleTextPadding;
		/** 文本区域 */
		public Rect textRect = new Rect();

		public Title(String text, int color) {
			this.text = text;
			this.color = color;
		}

		public void updateTextRect(Paint paint, float maxWidth) {
			int textWidth = textCircleWidth(paint);
			if (textWidth <= maxWidth)
				return;
			while (textWidth > maxWidth) {
				text = text.substring(0, text.length() - 1);
				textWidth = textCircleWidth(paint);
			}
			text = text.substring(0, text.length() - 1) + "...";
			textCircleWidth(paint);
		}

		private int textCircleWidth(Paint paint) {
			paint.getTextBounds(text, 0, text.length(), textRect);
			return textRect.width() + (radius + circleTextPadding) * 2;
		}
	}
}
