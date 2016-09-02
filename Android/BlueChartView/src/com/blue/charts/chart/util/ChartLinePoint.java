package com.blue.charts.chart.util;

/**
 * 曲线Line中的点
 * 
 * Author:chengli3209@gmail.com 2016/08/25 10:20
 */
public class ChartLinePoint {
	/**
	 * 绘制图的x坐标
	 */
	public float x;
	/**
	 * 绘制图的y坐标
	 */
	public float y;

	/**
	 * 实际的x值
	 */
	private float xValue;
	/**
	 * 实际的y值
	 */
	private float yValue;

	/**
	 * 是否绘制结点
	 */
	private boolean isDrawPoint;

	public ChartLinePoint() {

	}

	public ChartLinePoint(float xValue, float yValue, boolean isDrawPoint) {
		this.xValue = xValue;
		this.yValue = yValue;
		this.isDrawPoint = isDrawPoint;
	}

	public ChartLinePoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getxValue() {
		return xValue;
	}

	public void setxValue(float xValue) {
		this.xValue = xValue;
	}

	public float getyValue() {
		return yValue;
	}

	public void setyValue(float yValue) {
		this.yValue = yValue;
	}

	public boolean isDrawPoint() {
		return isDrawPoint;
	}

	public void setDrawPoint(boolean isDrawPoint) {
		this.isDrawPoint = isDrawPoint;
	}

}
