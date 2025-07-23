package com.qiyuesuo.sample.api.utils.http;

public class HttpHeader {

	private String contentType;
	private Long timestamp;
	private String accessToken;
	private String signature;
	private String nonce;
//	private String version;
	
	public HttpHeader(String accessToken, Long timestamp, String signature, String nonce) {
		this.accessToken = accessToken;
		this.timestamp = timestamp;
		this.signature = signature;
		this.nonce = nonce;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public String getSignature() {
		return signature;
	}

//	public String getVersion() {
//		return version;
//	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getNonce() {
		return nonce;
	}
}
