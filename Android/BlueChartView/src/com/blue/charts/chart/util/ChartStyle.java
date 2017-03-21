package com.blue.charts.chart.util;

import android.graphics.Color;

/**
 * 曲线图整体的样式
 * 
 * Author:chengli3209@gmail.com 2016/08/25 10:20
 */
public class ChartStyle {
	/** 网格线颜色 */
	private int gridColor;
	/** 坐标轴分隔线宽度 */
	private int axisLineWidth;

	/** 横坐标文本大小 */
	private float horizontalLabelTextSize;
	/** 横坐标文本颜色 */
	private int horizontalLabelTextColor;
	/** 横坐标标题文本大小 */
	private float horizontalTitleTextSize;
	/** 横坐标标题文本颜色 */
	private int horizontalTitleTextColor;
	/** 横坐标标题文本左间距 */
	private int horizontalTitlePaddingLeft;
	/** 横坐标标题文本右间距 */
	private int horizontalTitlePaddingRight;

	/** 纵坐标文本大小 */
	private float verticalLabelTextSize;
	/** 纵坐标文本颜色 */
	private int verticalLabelTextColor;
	/** 竖向十字线颜色 */
	private int verticalCrossLineColor;
	/** 竖向十字线宽度 */
	private int verticalCrossLineWidth;
	/** 圆的半径 */
	public float radius;
	/** 是否添加示例图 */
	private boolean isShowLegendView = false;

	public ChartStyle() {
		gridColor = Color.LTGRAY;
		horizontalTitleTextSize = 26;
		horizontalTitleTextColor = Color.GRAY;

		horizontalLabelTextSize = 24;
		horizontalLabelTextColor = Color.GRAY;

		verticalLabelTextSize = 24;
		verticalLabelTextColor = Color.GRAY;

		axisLineWidth = 5;
		radius = 10;

		horizontalTitlePaddingLeft = 20;
		horizontalTitlePaddingRight = 10;

		verticalCrossLineColor = Color.DKGRAY;
		verticalCrossLineWidth = 2;

		isShowLegendView = false;
	}

	public int getGridColor() {
		return gridColor;
	}

	public void setGridColor(int gridColor) {
		this.gridColor = gridColor;
	}

	public int getAxisLineWidth() {
		return axisLineWidth;
	}

	public void setAxisLineWidth(int axisLineWidth) {
		this.axisLineWidth = axisLineWidth;
	}

	public float getHorizontalLabelTextSize() {
		return horizontalLabelTextSize;
	}

	public void setHorizontalLabelTextSize(float horizontalLabelTextSize) {
		this.horizontalLabelTextSize = horizontalLabelTextSize;
	}

	public int getHorizontalLabelTextColor() {
		return horizontalLabelTextColor;
	}

	public void setHorizontalLabelTextColor(int horizontalLabelTextColor) {
		this.horizontalLabelTextColor = horizontalLabelTextColor;
	}

	public float getHorizontalTitleTextSize() {
		return horizontalTitleTextSize;
	}

	public void setHorizontalTitleTextSize(float horizontalTitleTextSize) {
		this.horizontalTitleTextSize = horizontalTitleTextSize;
	}

	public int getHorizontalTitleTextColor() {
		return horizontalTitleTextColor;
	}

	public void setHorizontalTitleTextColor(int horizontalTitleTextColor) {
		this.horizontalTitleTextColor = horizontalTitleTextColor;
	}

	public int getHorizontalTitlePaddingLeft() {
		return horizontalTitlePaddingLeft;
	}

	public void setHorizontalTitlePaddingLeft(int horizontalTitlePaddingLeft) {
		this.horizontalTitlePaddingLeft = horizontalTitlePaddingLeft;
	}

	public int getHorizontalTitlePaddingRight() {
		return horizontalTitlePaddingRight;
	}

	public void setHorizontalTitlePaddingRight(int horizontalTitlePaddingRight) {
		this.horizontalTitlePaddingRight = horizontalTitlePaddingRight;
	}

	public float getVerticalLabelTextSize() {
		return verticalLabelTextSize;
	}

	public void setVerticalLabelTextSize(float verticalLabelTextSize) {
		this.verticalLabelTextSize = verticalLabelTextSize;
	}

	public int getVerticalLabelTextColor() {
		return verticalLabelTextColor;
	}

	public void setVerticalLabelTextColor(int verticalLabelTextColor) {
		this.verticalLabelTextColor = verticalLabelTextColor;
	}

	public int getVerticalCrossLineColor() {
		return verticalCrossLineColor;
	}

	public void setVerticalCrossLineColor(int verticalCrossLineColor) {
		this.verticalCrossLineColor = verticalCrossLineColor;
	}

	public int getVerticalCrossLineWidth() {
		return verticalCrossLineWidth;
	}

	public void setVerticalCrossLineWidth(int verticalCrossLineWidth) {
		this.verticalCrossLineWidth = verticalCrossLineWidth;
	}

	public boolean isShowLegendView() {
		return isShowLegendView;
	}

	public void setShowLegendView(boolean isShowLegendView) {
		this.isShowLegendView = isShowLegendView;
	}
}
