package org.jeecg.common.util;

import cn.hutool.core.util.StrUtil;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

/**
 * 转换工具类
 *
 * @author 李煜琨
 * @time 2019年6月1日
 */
public class ConverUtils {
	public static double pi = 3.1415926535897932384626;
	public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	public static double a = 6378245.0;
	public static double ee = 0.00669342162296594323;
	/**
	 * @Description:数组合并
	 * @param values
	 * @return
	 */
	public static byte[] byteMergerAll(byte[]... values) {
		int length_byte = 0;
		for (int i = 0; i < values.length; i++) {
			length_byte += values[i].length;
		}
		byte[] all_byte = new byte[length_byte];
		int countLength = 0;
		for (int i = 0; i < values.length; i++) {
			byte[] b = values[i];
			System.arraycopy(b, 0, all_byte, countLength, b.length);
			countLength += b.length;
		}
		return all_byte;
	}

	/**
	 * bytes转换成十六进制字符串
	 */
	public static String byte2HexStr(byte[] b) {
		String stmp = "";
		StringBuilder sb = new StringBuilder();
		for (byte aB : b) {
			stmp = Integer.toHexString(aB & 0xff);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
		}
		return sb.toString();
	}

	/***
	 * 16进制字符串转换为字节数组
	 *
	 * @param length 转换结果的byte长度
	 */
	public static byte[] hexString2Bytes(String hexString, int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = (byte) (Integer.parseInt(hexString.substring(i << 1, (i << 1) + 2), 16) & 0xff);
		}
		return result;
	}

	/**
	 * int转换为16进制字符串
	 *
	 * @param length 长度
	 */
	public static String intToHexStr(int i, int length) {
		StringBuilder b = new StringBuilder(Integer.toHexString(i));
		while (b.length() < (length << 1)) {
			b.insert(0, '0');
		}
		return b.toString().toUpperCase();
	}

	/**
	 * 两个字节合并成一个整数
	 */
	public static int byteToInt(byte hByte, byte lByte) {
		return ((hByte & 0xff) << 8) | (lByte & 0xff);
	}

	/**
	 * byte数组转换为无符号short整数
	 */
	public static int byte2ToUnsignedShort(byte[] bytes) {
		return byte2ToUnsignedShort(bytes, 0);
	}

	/**
	 * byte数组转换为无符号short整数
	 *
	 * @param off 开始位置
	 */
	public static int byte2ToUnsignedShort(byte[] bytes, int off) {
		int high = bytes[off];
		int low = bytes[off + 1];
		return (high << 8 & 0xff00) | (low & 0xff);
	}

	/**
	 * 将整数转换为byte数组并指定长度
	 * @param a 整数
	 * @param length 指定长度
	 * @return
	 */
	public static byte[] intToBytes(int a, int length) {
		byte[] bs = new byte[length];
		for (int i = bs.length - 1; i >= 0; i--) {
			bs[i] = (byte) (a % 255);
			a = a / 255;
		}
		return bs;
	}

	/**
	 * 将byte数组转换为整数
	 * @param bs
	 * @return
	 */
	public static int bytesToInt(byte[] bs) {
		int a = 0;
		for (int i = bs.length - 1; i >= 0; i--) {
			a += bs[i] * Math.pow(255, bs.length - i - 1);
		}
		return a;
	}

	public static int WORDtoInt(byte[] sourceArr){
		return sourceArr[0]<<8|sourceArr[1];
	}




	/**
	 * int转换1个字节byte数组
	 */
	public static byte[] hexIntU8(int i) {
		byte[] tb = new byte[1];
		tb[0] = (byte) i;
		return tb;
	}

	/**
	 * int转换2个字节byte数组
	 */
	public static byte[] hexIntU16(int i) {
		byte[] result = new byte[2];
		// 由高位到低位
		result[0] = (byte) ((i >> 8) & 0xff);
		result[1] = (byte) (i & 0xff);
		return result;
	}

	/**
	 * int转换4个字节byte数组
	 */
	public static byte[] hexIntU32(int i) {
		byte[] result = new byte[4];
		// 由高位到低位
		result[0] = (byte) ((i >> 24) & 0xff);
		result[1] = (byte) ((i >> 16) & 0xff);
		result[2] = (byte) ((i >> 8) & 0xff);
		result[3] = (byte) (i & 0xff);
		return result;
	}

	/**
	 * 计算经纬度
	 */
	public static String calcXY(int data) {
		float pdata = (float) (data / 30000.00);
		int x = (int) (pdata / 60);
		float y = pdata - 60 * x;
		return String.format("%d°%.5f'", x, y);
	}

	/**
	 * 经纬度字符串转换
	 */
	public static double calcStrXY(String data) {
		String[] value = data.split("°|'");
		int du = Integer.valueOf(value[0]);
		double fen = Double.valueOf(value[1]);
		DecimalFormat df = new DecimalFormat(".0000000");
		return Double.valueOf(df.format(du*100+fen));
		//return Double.valueOf(df.format((du + fen/60)*100));
	}
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
	 * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
	 * */
	public static double[] gcj02_To_Gps84(double lat, double lon) {
		double[] gps = gps84_To_Gcj02(lat, lon);
		double lontitude = lon * 2 - gps[1];
		double latitude = lat * 2 - gps[0];
		return new double[]{latitude, lontitude};
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
	/**
	 * 字符串转ascii
	 */
	public static byte[] strToAscii(String str) {
		char[] ch = str.toCharArray();
		byte[] by = new byte[ch.length];
		for (int i = 0; i < ch.length; i++) {
			by[i] = (byte) ch[i];
		}
		return by;
	}

	/**
	 * ascii转字符串
	 */
	public static String asciiToStr(byte[] ascii) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ascii.length; i++) {
			sb.append((char) ascii[i]);
		}
		return sb.toString();
	}

	/**
	 * 字符串转unicode字符串
	 */
	public static byte[] strToUnicode(String s) {
		if (StrUtil.isEmpty(s)) {
			return null;
		}
		try {
			StringBuffer out = new StringBuffer("");
			byte[] bytes = s.getBytes("unicode");

			for (int i = 0; i < bytes.length - 1; i += 2) {

				String str = Integer.toHexString(bytes[i] & 0xff);
				for (int j = str.length(); j < 2; j++) {
					out.append("0");
				}
				out.append(str);
				String str1 = Integer.toHexString(bytes[i + 1] & 0xff);
				for (int j = str1.length(); j < 2; j++) {
					out.append("0");
				}
				out.append(str1);
			}

			out.delete(0, "feff".length());
			String result = out.toString();
			return hexString2Bytes(result, result.length());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param v1
	 * @param v2
	 * @param scale 对结果保留几位小数
	 * @return
	 */
	public static double divide(double v1, double v2,int scale) {
		//BigDecimal b1 = new BigDecimal(Double.toString(v1));
		//BigDecimal b2 = new BigDecimal(Double.toString(v2));
		//return b1.divide(b2,scale).doubleValue();
		//return b1.divide(b1.add(b2), 2, RoundingMode.HALF_UP);
		double f1 = new BigDecimal(v1/v2).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}

	/**
	 * byte数组转hex
	 * @param bytes
	 * @return
	 */
	public static String byteToHex(byte[] bytes){
		String strHex = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < bytes.length; n++) {
			strHex = Integer.toHexString(bytes[n] & 0xFF);
			sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
		}
		return sb.toString().trim();
	}

	/**
	 * hex转byte数组
	 * @param hex
	 * @return
	 */
	public static byte[] hexToByte(String hex){
		int m = 0, n = 0;
		int byteLen = hex.length() / 2; // 每两个字符描述一个字节
		byte[] ret = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			m = i * 2 + 1;
			n = m + 1;
			int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
			ret[i] = Byte.valueOf((byte)intVal);
		}
		return ret;
	}

	/**
	 * hex转字符串
	 * @param hex
	 * @return
	 */
	public static String hexToString(String hex){
		int m = 0, n = 0;
		int byteLen = hex.length() / 2; // 每两个字符描述一个字节
		byte[] ret = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			m = i * 2 + 1;
			n = m + 1;
			int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
			ret[i] = (byte) intVal;
		}
		return new String(ret, StandardCharsets.UTF_8);
	}
	//字符串倒转
	public static String spiltRtoL(String s){

		StringBuilder sb = new StringBuilder();
		int length = s.length();
		char[] c = new char[length];
		for (int i = 0; i < length; i++) {
			c[i] = s.charAt(i);
		}
		for (int i = length - 1; i >= 0; i--) {
			sb.append(c[i]);
		}
		return sb.toString();
	}

	/**
	 * hex转整数
	 * @param hex
	 * @return
	 */
	public static int hexToInt(String hex){
		int m = 0, n = 0;
		int byteLen = hex.length() / 2; // 每两个字符描述一个字节
		byte[] ret = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			m = i * 2 + 1;
			n = m + 1;
			int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
			ret[i] = (byte) intVal;
		}
		int a = 0;
		for (int i = ret.length - 1; i >= 0; i--) {
			a += ret[i] * Math.pow(255, ret.length - i - 1);
		}
		return a;
	}


	public static double hextoDouble(String hex){
		long longBits = Long.valueOf(hex, 16);
		return Double.longBitsToDouble(longBits);
	}

	public static float hextoFloat(String hex){
		long l = Long.parseLong(hex, 16);
		return Float.intBitsToFloat((int) l);
	}

	/**
	 * 字节数组转float
	 * 采用IEEE754标准
	 * @param bytes
	 * @return
	 */
	public static float bytes2Float(byte[] bytes){
		//获取 字节数组转化成的16进制字符串
		String BinaryStr = bytes2BinaryStr(bytes);
		//符号位S
		Long s = Long.parseLong(BinaryStr.substring(0, 1));
		//指数位E
		Long e = Long.parseLong(BinaryStr.substring(1, 9),2);
		//位数M
		String M = BinaryStr.substring(9);
		float m = 0,a,b;
		for(int i=0;i<M.length();i++){
			a = Integer.valueOf(M.charAt(i)+"");
			b = (float) Math.pow(2, i+1);
			m =m + (a/b);
		}
		Float f = (float) ((Math.pow(-1, s)) * (1+m) * (Math.pow(2,(e-127))));
		return f;
	}
	/**
	 * 将字节数组转换成16进制字符串
	 * @param bytes
	 * @return
	 */
	public static String bytes2BinaryStr(byte[] bytes){
		StringBuffer binaryStr = new StringBuffer();
		for(int i=0;i<bytes.length;i++){
			String str = Integer.toBinaryString((bytes[i] & 0xFF) + 0x100).substring(1);
			binaryStr.append(str);
		}
		return binaryStr.toString();
	}

	// 从byte数组的index处的连续4个字节获得一个float
	public static float getFloat(byte[] arr, int index) {
		return Float.intBitsToFloat(getInt(arr, index));
	}

	// 从byte数组的index处的连续4个字节获得一个int
	public static int getInt(byte[] arr, int index) {
		return 	(0xff000000 	& (arr[index+0] << 24))  |
				(0x00ff0000 	& (arr[index+1] << 16))  |
				(0x0000ff00 	& (arr[index+2] << 8))   |
				(0x000000ff 	&  arr[index+3]);
	}


	public static float toFloat(byte[] bytes) throws IOException {
		ByteArrayInputStream mByteArrayInputStream = new ByteArrayInputStream(bytes);
		DataInputStream mDataInputStream = new DataInputStream(mByteArrayInputStream);
		try {
			return mDataInputStream.readFloat();
		} finally {
			mDataInputStream.close();
			mByteArrayInputStream.close();
		}
	}


	public static void main(String[] args) {
		/*System.out.println(ConverUtils.WORDtoInt(ConverUtils.hexToByte("FFE2")));
		Integer dec_num = Integer.parseInt("FFE2",16);*/
		try {
			System.out.println(toFloat(ConverUtils.hexString2Bytes("34dc08c2",4)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		//byte[] bytes = (ConverUtils.hexToByte("010500000000CDCA"));
		//System.out.println(ConverUtils.divide(Convert.toDouble("-17"),100,2));
	}
}
