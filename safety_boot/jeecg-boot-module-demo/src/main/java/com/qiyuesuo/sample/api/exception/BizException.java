package com.qiyuesuo.sample.api.exception;


public class BizException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	protected Integer code;

	public Integer getCode() {
		return code;
	}
	
	public BizException() {
		super();
	}

//	public PrivateAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
//		super(message, cause, enableSuppression, writableStackTrace);
//	}

	public BizException(String message, Throwable cause) {
		super(message, cause);
	}

	public BizException(String message) {
		super(message);
	}

	public BizException(Throwable cause) {
		super(cause);
	}

	public BizException(Integer code, String message) {
		super(message);
		this.code = code == null ? 1000000 : code;
	}
}
