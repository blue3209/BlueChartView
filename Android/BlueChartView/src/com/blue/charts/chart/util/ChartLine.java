package com.blue.charts.chart.util;

import java.util.ArrayList;
import java.util.List;

import com.blue.charts.chart.util.ChartData.Title;

/**
 * 曲线Line
 * 
 * Author:chengli3209@gmail.com 2016/08/25 10:20
 */
public class ChartLine {
	/**
	 * 标题
	 */
	private String lineTitle;
	/**
	 * 线的index值
	 */
	private int lineIndex;
	/**
	 * 线的颜色
	 */
	private int lineColor;
	/**
	 * 线的宽度
	 */
	private int lineWidth;

	/**
	 * 曲线中点的集合
	 */
	private List<ChartLinePoint> points;
	private List<ChartLinePoint> besselPoints;

	/** 序列曲线的标题 */
	private Title title;
	/** 是否是绘制贝塞尔曲线 */
	private boolean isDrawBesselLine;

	public ChartLine(String lineTitle, int lineColor,
			List<ChartLinePoint> points, boolean isDrawBesselLine) {
		this.lineTitle = lineTitle;
		this.lineColor = lineColor;
		this.points = points;
		this.isDrawBesselLine = isDrawBesselLine;

		this.title = new Title(this.lineTitle, this.lineColor);
		this.besselPoints = new ArrayList<ChartLinePoint>();
	}

	public String getLineTitle() {
		return lineTitle;
	}

	public void setLineTitle(String lineTitle) {
		this.lineTitle = lineTitle;
	}

	public int getLineIndex() {
		return lineIndex;
	}

	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}

	public int getLineColor() {
		return lineColor;
	}

	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public List<ChartLinePoint> getPoints() {
		return points;
	}

	public void setPoints(List<ChartLinePoint> points) {
		this.points = points;
	}

	public List<ChartLinePoint> getBesselPoints() {
		return besselPoints;
	}

	public void setBesselPoints(List<ChartLinePoint> besselPoints) {
		this.besselPoints = besselPoints;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public boolean isDrawBesselLine() {
		return isDrawBesselLine;
	}

	public void setDrawBesselLine(boolean isDrawBesselLine) {
		this.isDrawBesselLine = isDrawBesselLine;
	}
}
