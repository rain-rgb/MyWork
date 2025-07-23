package org.jeecg.common.util;

import cn.hutool.core.util.StrUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.security.Key;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

public class mathUtil {

	//标准差σ=sqrt(s^2)
	public static double StandardDiviation( List<Double> x) {
		if(x.size()==0){
			return 0;
		}
		int m=x.size();
		double sum=0;
		for(int i=0;i<m;i++){//求和
			sum+=x.get(i);
		}
		double dAve=sum/m;//求平均值
		double dVar=0;
		for(int i=0;i<m;i++){//求方差
			dVar+=(x.get(i)-dAve)*(x.get(i)-dAve);
		}
		//reture Math.sqrt(dVar/(m-1));
		return Math.sqrt(dVar/m);
	}

	/*
	 * 计算平均值
	 */
	public static double AVERAGE(List arrary){
		double  average = 0;
		double  sum =0;

		try{      if(arrary!=null){
			for(int i=0 ;i<arrary.size();i++){
				Double a=0.0;

				if(StrUtil.isNotEmpty(arrary.get(i).toString())){
					a=  Double.valueOf(arrary.get(i).toString());
				}
				sum = sum+a;

			}
			average = sum/arrary.size();
			average = (double)Math.round(average*100)/100;
		}
		}catch(Exception e){
				System.out.println("求平均值时错误");
			average =-1;
			return average;
		}
		return average;
	}

	/*
	 * 计算标准差
	 */
	public static double STDEV(List arrary){
		double  stdev = 0;
		double  sum =0;
		double  average = 0;
		try{
			if(arrary!=null&&arrary.size()>1){
				average = AVERAGE(arrary);
				for(int i=0 ;i<arrary.size();i++){

					Double a=0.0;

					if(StrUtil.isNotEmpty(arrary.get(i).toString())){
						a=  Double.valueOf(arrary.get(i).toString());
					}
					sum = sum+(a-average)*(a-average);
				}
				stdev = (double)Math.round(Math.sqrt(sum/(arrary.size()-1))*1000)/1000;
			}
		}catch(Exception e){
				System.out.println("求标准差时错误");
			stdev =-1;
			return stdev;
		}
		return stdev;
	}

	/*
	 * 计算变异系数
	 */
	public static double CV(List arrary){
		double  stdev = 0;
		double  cv =0;
		double  average = 0;
		try{
			if(arrary!=null&&arrary.size()>1){
				average = AVERAGE(arrary);
				stdev = STDEV(arrary);
				cv = stdev*100/average;
				cv = (double)Math.round(cv*1000)/1000;
			}
		}catch(Exception e){
				System.out.println("求变异系数时错误");
			stdev =-1;
			return cv;
		}
		return cv;
	}

	public static double  MaxMum(List arrary){
		double  max = 0;
		try{
			if(arrary!=null&&arrary.size()>=1){
				for(int i=0 ;i<arrary.size();i++){
					Double a=0.0;

					if(StrUtil.isNotEmpty(arrary.get(i).toString())){
						a=  Double.valueOf(arrary.get(i).toString());
					}

					if(max<a){max=a;}
				}
			}

		}catch(Exception e){
				System.out.println("求最大值时错误");
			max =0;
			return max;
		}

		return max;
	}

	public static double  MinMum(List arrary){
		double  max = 0;
		try{
			if(arrary!=null&&arrary.size()>=1){
				max= Double.valueOf(arrary.get(0).toString());


				for(int i=0 ;i<arrary.size();i++){

					Double a=0.0;

					if(StrUtil.isNotEmpty(arrary.get(i).toString())){
						a=  Double.valueOf(arrary.get(i).toString());
					}

					if(max>a){max=a;}
				}
			}

		}catch(Exception e){
			System.out.println( "求最小值时错误");
			max =0;
			return max;
		}
		return max;
	}
}
