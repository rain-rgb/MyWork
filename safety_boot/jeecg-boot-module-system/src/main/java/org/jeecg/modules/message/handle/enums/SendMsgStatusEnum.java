package org.jeecg.modules.message.handle.enums;

/**
 * 推送状态枚举
 */
public enum SendMsgStatusEnum {

//推送状态 0未推送 1推送成功 2推送失败 3加密失败 4超时推送
	WAIT("0"), SUCCESS("1"), FAIL("2"), ENCRYPT_FAIL("3"), TIME_OUT("4");

	private String code;

	private SendMsgStatusEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setStatusCode(String code) {
		this.code = code;
	}

}