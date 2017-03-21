package com.blue.charts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.blue.charts.chart.util.ChartLine;
import com.blue.charts.chart.util.ChartLinePoint;
import com.blue.charts.chart.view.ChartViewLayout;
import com.blue.charts.chart.view.ChartViewLayout.OnChartListener;

public class MainActivity extends Activity {

	private TextView tvDesc;
	private TextView tvChart;
	private ChartViewLayout chartViewLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvDesc = (TextView) findViewById(R.id.tv_desc);
		tvChart = (TextView) findViewById(R.id.tv_chart);

		chartViewLayout = (ChartViewLayout) findViewById(R.id.chartViewLayout);
		chartViewLayout.setOnChartListener(new OnChartListener() {

			@Override
			public void onMove(int index, float moveX) {
				tvChart.setText("index:" + index + "   moveX:" + moveX);
			}
		});

		getWindow().getDecorView().post(new Runnable() {

			@Override
			public void run() {
				tvDesc.setText("width = " + chartViewLayout.getWidth()
						+ ",height = " + chartViewLayout.getHeight());
				getChartLines();
			}
		});
	}

	public void getChartLines() {
		List<ChartLine> chartLines = new ArrayList<ChartLine>();
		chartLines.add(getRandomChartLine("曲线1", 0xffea6210));
		chartLines.add(getRandomChartLine("曲线2", 0xff69B6EA));

		chartViewLayout.setChartLines(chartLines);
		chartViewLayout.refresh(true);
	}

	private ChartLine getRandomChartLine(String title, int color) {
		List<ChartLinePoint> points = new ArrayList<ChartLinePoint>();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			int r = random.nextInt(10);
			if (i % 10 == 0) {
				r = -random.nextInt(15);
			}
			points.add(new ChartLinePoint(i + 1, 88 + 2.632f * r, true));
		}
		return new ChartLine(title, color, points, false);
	}
}
