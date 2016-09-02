package com.blue.charts.chart.view;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.ViewGroup;

import com.blue.charts.chart.util.ChartCalculator;
import com.blue.charts.chart.util.ChartData.Title;
import com.blue.charts.chart.util.ChartStyle;

/**
 * 示意图
 * 
 * Author:chengli3209@gmail.com 2016/08/25 10:20
 */
public class ChartLegendView extends View {

	private Paint paint;
	private ChartStyle style;
	private List<Title> titles;
	private ChartCalculator calculator;

	public ChartLegendView(Context context, List<Title> titles,
			ChartStyle style, ChartCalculator calculator) {
		super(context);
		this.titles = titles;
		this.style = style;
		this.calculator = calculator;
		this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (titles.size() == 0)
			return;
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(style.getHorizontalTitleTextSize());
		for (Title title : titles) {
			paint.setColor(title.color);
			paint.setTextAlign(Align.CENTER);
			paint.setTextSize(style.getHorizontalTitleTextSize());
			canvas.drawCircle(title.circleX, title.circleY, title.radius, paint);
			paint.setAlpha(255);
			canvas.drawText(title.text, title.textX, title.textY, paint);
		}
	}

	public void updateHeight() {
		ViewGroup.LayoutParams lp = getLayoutParams();
		lp.height = calculator.xTitleHeight;
		setLayoutParams(lp);
	}
}
