package org.jeecg.common.util;

import org.jeecg.common.exception.JeecgBootException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ShtooneUtil {

	/**
	 * 生成样品/记录/报告编码
	 *
	 * @param codingRules
	 *            编码规则 来源sys_depart 组织机构表 codingRules
	 * @param codingDateFormat
	 *            编码时间格式 来源sys_depart 组织机构表 codingDateFormat
	 * @param codingFlowNumerDigit
	 *            流水位数 来源sys_depart 组织机构表 codingFlowNumerDigit
	 * @param FlowNumber
	 *            当前流水号 来源sys_depart 组织机构表 FlowNumber
	 * @param BDH
	 *            标段号 来源sys_depart 组织机构表 contractnumber 合同号
	 * @param SYLXBM
	 *            试验类型编码 来源：sy_dps_jc_testItemType#试验项目类型表 titSampleMark字段
	 * @return
	 * @throws ParseException
	 */
	public static String getCoding(String codingRules, String codingDateFormat, int codingFlowNumerDigit,
			String FlowNumber, String BDH, String SYLXBM, String date, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat(codingDateFormat);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ("NoFlowNumber".equals(FlowNumber)) {// 根据编码规则生成不需要带流水号的值
				/*
				 * 编码规则说明 编码规则例子：YP-LSSJ-BDH-SYLXBM-LSH 编码规则中可出现如下关键字，关键字符为动态替换键，关键字可自由调换位置
				 * LSSJ:流水时间 BDH:标段号 SYLXBM:试验类型编码 LSH:流水号 注意：编码规则制定完成后并已开始做资料，请勿随意更改
				 */
				codingRules = codingRules.replaceAll("-LSH", "");
				if (SYLXBM.equals("GJJ")) {
					if (type.equals("JB011002")) {
						SYLXBM = "GJH";
					}
					if (type.equals("JB011003")) {
						SYLXBM = "GJL";
					}
				}
				return codingRules.replace("LSSJ", sdf.format(sd.parse(date))).replace("BDH", BDH).replace("SYLXBM",
						SYLXBM);
			} else {
				/*
				 * 编码规则说明 编码规则例子：YP-LSSJ-BDH-SYLXBM-LSH 编码规则中可出现如下关键字，关键字符为动态替换键，关键字可自由调换位置
				 * LSSJ:流水时间 BDH:标段号 SYLXBM:试验类型编码 LSH:流水号 注意：编码规则制定完成后并已开始做资料，请勿随意更改
				 */
				if (SYLXBM.equals("GJJ")) {
					if (type.equals("JB011002")) {
						SYLXBM = "GJH";
					}
					if (type.equals("JB011003")) {
						SYLXBM = "GJL";
					}
				}
				return codingRules.replace("LSSJ", sdf.format(sd.parse(date))).replace("BDH", BDH).replace("SYLXBM", SYLXBM)
						.replace("LSH", getFlowNumber(codingFlowNumerDigit, FlowNumber));
			}
		} catch (Exception e) {
			throw new JeecgBootException("编号生成失败");
		}
	}

	/**
	 * 生成流水号
	 *
	 * @param codingFlowNumerDigit
	 *            流水号位数
	 * @param FlowNumber
	 *            当前流水号
	 * @return
	 */
	public static String getFlowNumber(int codingFlowNumerDigit, String FlowNumber) {
		try {
			// 如果当前流水号为空则从1开始
			if (null == FlowNumber || "".equals(FlowNumber)) {
				return autoGenericCode("1", codingFlowNumerDigit);
			} else {
				return autoGenericCode(FlowNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("生成流水号异常");
			return "";
		}
	}

	/**
	 * 不够位数的在前面补0，保留code的长度位数字
	 *
	 * @param code
	 * @return
	 */
	public static String autoGenericCode(String code) {
		String result = "";
		// 保留code的位数
		result = String.format("%0" + code.length() + "d", Integer.parseInt(code) + 1);

		return result;
	}

	/**
	 * 不够位数的在前面补0，保留num的长度位数字
	 *
	 * @param code
	 * @return
	 */
	public static String autoGenericCode(String code, int num) {
		String result = "";
		// 保留num的位数
		// 0 代表前面补充0
		// num 代表长度为4
		// d 代表参数为正数型
		result = String.format("%0" + num + "d", Integer.parseInt(code));

		return result;
	}

	// 首字母转大写
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	public static Object changeObject(String s, Field field) {
		if (field.getType().toString().equals("class java.math.BigDecimal")) {
			return new BigDecimal(s);
		} else if (field.getType().toString().equals("class java.lang.Integer")) {
			return Integer.parseInt(s);
		} else if (field.getType().toString().equals("class java.lang.Float")) {
			return Float.valueOf(s);
		} else {
			return s;
		}
	}
}
