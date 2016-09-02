package com.blue.charts.chart.util;

import java.util.List;

import android.util.Log;

/**
 * 工具类
 * 
 * Author:chengli3209@gmail.com 2016/08/25 10:20
 */
public class ChartUtils {

	/**
	 * 检查List是否为空
	 * 
	 * @param list
	 * @return
	 */
	public static <T> boolean isEmpty(List<T> list) {
		if (null == list || list.size() <= 0) {
			return true;
		}
		return false;
	}

	public static void log(String tag, String msg) {
		Log.e(tag, msg);
	}
}
