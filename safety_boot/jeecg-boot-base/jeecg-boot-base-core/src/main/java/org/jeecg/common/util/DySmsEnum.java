package org.jeecg.common.util;

import org.apache.commons.lang.StringUtils;

public enum DySmsEnum {


	SMS_SHRWD_CODE("SMS_253100522","杭州高讯","shrwd,content"),

	SMS_KEY_CODE("SMS_216705688","杭州高讯","code"),
	/**超标短信**/
	//BHZ_CB_CODE("SMS_213530264","杭州高讯","shebeiname,time,strengthrank,pourelocation,finalcontent"),
	SMS_CB_CODE("SMS_217225702","杭州高讯","sbname,time,content"),
	SMS_CB_TFYS("SMS_464116476","杭州高讯","sbname,time,startstation,content,interval"),
	SMS_CB_TFYSPZ("SMS_467425245","杭州高讯","sbname,time,startstation,interval,content"),
	SMS_CB_TFYSS("SMS_465605467","杭州高讯","sbname,time,startstation,content"),
	SMS_CB_TFYSS2("SMS_465970999","杭州高讯","sbname,time,startstation,content1,content2"),
	SMS_CB_TFYSS3("SMS_465966055","杭州高讯","sbname,time,startstation,content1,content2,content3"),
	SMS_CB_TFYSS4("SMS_465891045","杭州高讯","sbname,time,startstation,content1,content2,content3,content4"),
	SMS_CB_TFYSS5("SMS_465921097","杭州高讯","sbname,time,startstation,content1,content2,content3,content4,content5"),
	SMS_CB_TFYSS6("SMS_465981032","杭州高讯","sbname,time,startstation,content1,content2,content3,content4,content5,content6"),
	SMS_CB_TFYSS7("SMS_465951082","杭州高讯","sbname,time,startstation,content1,content2,content3,content4,content5,content6,content7"),

	SMS_CB_TFYSSS("SMS_465955460","杭州高讯","sbname,time,startstation,content,contents"),
	SMS_CB_TFYSSSs("SMS_465955508","杭州高讯","sbname,time,startstation,content,contents,contentss"),
	LOGIN_TEMPLATE_CODE("SMS_175435174","JEECG","code"),
	FORGET_PASSWORD_TEMPLATE_CODE("SMS_175435174","JEECG","code"),
	REGISTER_TEMPLATE_CODE("SMS_175430166","JEECG","code"),
	/**会议通知*/
	MEET_NOTICE_TEMPLATE_CODE("SMS_201480469","H5活动之家","username,title,minute,time"),
	/**我的计划通知*/
	PLAN_NOTICE_TEMPLATE_CODE("SMS_201470515","H5活动之家","username,title,time");

	/**
	 * 短信模板编码
	 */
	private String templateCode;
	/**
	 * 签名
	 */
	private String signName;
	/**
	 * 短信模板必需的数据名称，多个key以逗号分隔，此处配置作为校验
	 */
	private String keys;

	private DySmsEnum(String templateCode,String signName,String keys) {
		this.templateCode = templateCode;
		this.signName = signName;
		this.keys = keys;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public static DySmsEnum toEnum(String templateCode) {
		if(StringUtils.isEmpty(templateCode)){
			return null;
		}
		for(DySmsEnum item : DySmsEnum.values()) {
			if(item.getTemplateCode().equals(templateCode)) {
				return item;
			}
		}
		return null;
	}
}

