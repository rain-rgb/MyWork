package com.qiyuesuo.sample.api.utils.http;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

public class HttpConnection {
	
	private static boolean ignoreSSLCheck = false; // 忽略SSL检查
	private static HostnameVerifier hostnameVerifier = new TrustAllHostnameVerifier();
	
	public static HttpURLConnection getConnection(URL url, HttpMethod method, HttpHeader header) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (conn instanceof HttpsURLConnection) {
			HttpsURLConnection httpsConnection = (HttpsURLConnection) conn;
			httpsConnection.setHostnameVerifier(hostnameVerifier);
			if (ignoreSSLCheck) {
				SSLSocketFactory sslSocketFactory = TrustAllTrustManager.getSSLSocketFactory();
				httpsConnection.setSSLSocketFactory(sslSocketFactory);
			} 
			conn = httpsConnection;
		}

		conn.setRequestMethod(method.name());
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Host", url.getHost());
		conn.setRequestProperty("Accept", "text/plain,application/json");
		conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		conn.setRequestProperty("User-Agent", "privateapp-java-client");
		conn.setRequestProperty("x-qys-timestamp", Long.toString(header.getTimestamp()));
		conn.setRequestProperty("x-qys-signature", header.getSignature());
		conn.setRequestProperty("x-qys-accesstoken", header.getAccessToken());
		conn.setRequestProperty("x-qys-nonce", header.getNonce());
//		conn.setRequestProperty("version", header.getVersion());
		return conn;
	}

	public static void close(HttpURLConnection conn) {
		if (conn != null) {
			conn.disconnect();
		}
	}
	
	private static class TrustAllHostnameVerifier implements HostnameVerifier {
		
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

}
