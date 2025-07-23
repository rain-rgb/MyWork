package com.trtm.iot.util;

import java.text.DecimalFormat;

/**
 * 各地图API坐标系统比较与转换;
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
 * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
 * 谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
 * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。
 *
 *
 * WGS-84 地心坐标系，即GPS原始坐标体系
 * GCJ-02火星坐标系，国测局02年发布的坐标体系，它是一种对经纬度数据的加密算法，即加入随机的偏差。高德、腾讯、Google中国地图使用。国内最广泛使用的坐标体系；
 * 一般都是由GCJ-02进过偏移算法得到的。这种体系就根据每个公司的不同，坐标体系都不一样了。
 * 比如，百度的BD-09坐标、搜狗坐标等
 */
public class GPSUtil {

	public static double pi = 3.1415926535897932384626;
	public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	public static double a = 6378245.0;
	public static double ee = 0.00669342162296594323;



	public static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
				+ 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	public static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
				* Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
				* pi)) * 2.0 / 3.0;
		return ret;
	}

	public static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}
	/**
	 * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
	 *
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static double[] gps84_To_Gcj02(double lat, double lon) {
		if (outOfChina(lat, lon)) {
			return new double[]{lat,lon};
		}
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double mgLat = lat + dLat;
		double mgLon = lon + dLon;
		return new double[]{mgLat, mgLon};
	}

//	// WGS84=》GCJ02 地球坐标系=>火星坐标系
//	public static double[] wgs2GCJ(double wgLat, double wgLon) {
//		double[] latlon = new double[2];
//		if (outOfChina(wgLat, wgLon)) {
//			latlon[0] = wgLat;
//			latlon[1] = wgLon;
//			return latlon;
//		}
//		double[] deltaD = delta(wgLat, wgLon);
//		latlon[0] = wgLat + deltaD[0];
//		latlon[1] = wgLon + deltaD[1];
//		return latlon;
//	}
//
//	public static double[] delta(double wgLat, double wgLon) {
//		double[] latlng = new double[2];
//		double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
//		double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
//		double radLat = wgLat / 180.0 * PI;
//		double magic = Math.sin(radLat);
//		magic = 1 - OFFSET * magic * magic;
//		double sqrtMagic = Math.sqrt(magic);
//		dLat = (dLat * 180.0) / ((AXIS * (1 - OFFSET)) / (magic * sqrtMagic) * PI);
//		dLon = (dLon * 180.0) / (AXIS / sqrtMagic * Math.cos(radLat) * PI);
//		latlng[0] = dLat;
//		latlng[1] = dLon;
//		return latlng;
//	}


	/**
	 * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
	 * */
	public static double[] gcj02_To_Gps84(double lat, double lon) {
		double[] gps = gps84_To_Gcj02(lat, lon);
		double lontitude = lon * 2 - gps[1];
		double latitude = lat * 2 - gps[0];
		return new double[]{latitude, lontitude};
	}
	/**
	 * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
	 *
	 * @param lat
	 * @param lon
	 */
	public static double[] gcj02_To_Bd09(double lat, double lon) {
		double x = lon, y = lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double tempLon = z * Math.cos(theta) + 0.0065;
		double tempLat = z * Math.sin(theta) + 0.006;
		double[] gps = {tempLat,tempLon};
		return gps;
	}

	/**
	 * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param
	 * bd_lat * @param bd_lon * @return
	 */
	public static double[] bd09_To_Gcj02(double lat, double lon) {
		double x = lon - 0.0065, y = lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double tempLon = z * Math.cos(theta);
		double tempLat = z * Math.sin(theta);
		double[] gps = {tempLat,tempLon};
		return gps;
	}

	/**将gps84转为bd09
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static double[] gps84_To_bd09(double lat,double lon){
		double[] gcj02 = gps84_To_Gcj02(lat,lon);
		double[] bd09 = gcj02_To_Bd09(gcj02[0],gcj02[1]);
		return bd09;
	}
	public static double[] bd09_To_gps84(double lat,double lon){
		double[] gcj02 = bd09_To_Gcj02(lat, lon);
		double[] gps84 = gcj02_To_Gps84(gcj02[0], gcj02[1]);
		//保留小数点后六位
		gps84[0] = retain6(gps84[0]);
		gps84[1] = retain6(gps84[1]);
		return gps84;
	}

	/**保留小数点后六位
	 * @param num
	 * @return
	 */
	public static double retain6(double num){
		String result = String .format("%.6f", num);
		return Double.valueOf(result);
	}


	/**
     * 解析经纬度
     * 度 . 分 . 分 格式转【度 . 度 格式】
     * 经纬度格式分为三种：度、度-分、度-份-秒, R% O$ C- h: ^! Z5 b9 f& u6 U/ z

     * 1.） ddd.ddddd °【度 . 度 格式】的十进制小数部分（5位）

     * 2.） ddd°mm.mmm’ 【度 . 分 . 分 格式】的十进制小数部分（3位）
      " q' Z8 a1 d- M( \) L. u( Y1 E9 Y1 t: R
     * 3.)   ddd°mm’ss’’ 【度 . 分 . 秒 格式】
     * @param lnglatDoub
     * @return
     */
    public static double formatLnglat(double lnglatDoub){
        /*if("".equals(lnglatString) || null == lnglatString){
            return 0;
        }
        double lnglatDoub = Double.valueOf(lnglatString);*/
        int n = (int) (lnglatDoub/100);
        double m = lnglatDoub - n*100;
        double lnglat = n + m/60;
        DecimalFormat dcmFmt = new DecimalFormat("0.000000000");
        Double formatDoub=new Double(dcmFmt.format(lnglat));
        return formatDoub;
    }



}



