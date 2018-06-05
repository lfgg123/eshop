package com.eshop.util;

public class DistanceUtil {
	// 地球平均半径
	private static final double EARTH_RADIUS = 6378137;

	// 把经纬度转为度
	private static double rad(double d) {
		return d * Math.PI / 180;
	}

	/**
	 * 根据两点间经纬度坐标，计算两点见距离 单位为米
	 * 
	 * @param double
	 *            lng1
	 * @param double
	 *            lat1
	 * @param double
	 *            lng2
	 * @param double
	 *            lat2
	 * @return
	 */
	public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	/**
	 * test
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		double distance = getDistance(121.491909, 31.233234, 121.411994, 31.206134);
		System.out.println("Distance is:" + distance);
	}

}
