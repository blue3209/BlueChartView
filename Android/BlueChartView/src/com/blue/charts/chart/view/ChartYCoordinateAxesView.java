package com.blue.charts.chart.view;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.ViewGroup;

import com.blue.charts.chart.util.ChartCalculator;
import com.blue.charts.chart.util.ChartData.Label;
import com.blue.charts.chart.util.ChartStyle;

/**
 * Y坐标轴视图
 * 
 * Author:chengli3209@gmail.com 2016/08/25 10:20
 */
public class ChartYCoordinateAxesView extends View {
	public static final int POSITION_LEFT = 0;
	public static final int POSITION_RIGHT = 1;
	private int position = POSITION_RIGHT;
	private Paint paint;
	private ChartStyle style;
	private List<Label> labels;
	private ChartCalculator calculator;
	private boolean drawLine = false;

	public ChartYCoordinateAxesView(Context context, List<Label> labels,
			ChartStyle style, ChartCalculator calculator) {
		super(context);
		this.calculator = calculator;
		this.labels = labels;
		this.style = style;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setStyle(ChartStyle style) {
		this.style = style;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	public void setCalculator(ChartCalculator calculator) {
		this.calculator = calculator;
	}

	public boolean isDrawLine() {
		return drawLine;
	}

	public void setDrawLine(boolean drawLine) {
		this.drawLine = drawLine;
	}

	public void updateSize() {
		ViewGroup.LayoutParams lp = getLayoutParams();
		lp.height = calculator.yAxisHeight;
		lp.width = calculator.yAxisWidth;
		setLayoutParams(lp);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (labels.size() == 0)
			return;
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(style.getAxisLineWidth());
		paint.setColor(style.getVerticalLabelTextColor());
		paint.setTextSize(style.getVerticalLabelTextSize());
		paint.setTextAlign(Align.CENTER);

		int i = 0;
		for (Label label : labels) {
			float y = label.drawingY;
			if (i == 0) {
				y = y - 8;
			}
			canvas.drawText(label.text, label.x, y, paint);
			i++;
		}
		// 绘制竖线
		if (isDrawLine()) {
			float coordinateX = 0;
			if (position == POSITION_LEFT) {
				coordinateX = calculator.yAxisWidth - style.getAxisLineWidth();
			}
			float startCoordinateY = labels.get(0).y;
			canvas.drawLine(coordinateX, startCoordinateY, coordinateX,
					calculator.height, paint);
		}
	}

}
