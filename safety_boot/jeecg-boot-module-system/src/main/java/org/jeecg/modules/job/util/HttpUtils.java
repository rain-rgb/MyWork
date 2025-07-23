package org.jeecg.modules.job.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 发送HTTP请求
 *
 * @author mlu
 *
 */
public class HttpUtils {

	/**
	 * 发送post请求--用于接口接收的参数为JSON字符串
	 *
	 * @param url
	 *            请求地址
	 * @param params
	 *            json格式的字符串
	 * @return
	 */
	public static String httpPost(String url, String params) {
		String result = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			/*
			 * 发送json字符串，这两句需要设置
			 */
			httpPost.addHeader("Content-type", "application/json; charset=utf-8");
			httpPost.setHeader("Accept", "application/json");

			httpPost.setEntity(new StringEntity(params, "UTF-8"));

			HttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				// Read the response body
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送post请求--用于接口接收的参数为键值对
	 *
	 * @param url
	 *            请求地址
	 * @param nameValuePairs
	 *            键值对
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String url, List<NameValuePair> nameValuePairs) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		String strResult = "";

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				strResult = EntityUtils.toString(response.getEntity());
				// System.out.println("conResult:"+conResult);
			} else {
				strResult += "发送失败:" + response.getStatusLine().getStatusCode();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		return strResult;
	}

	public static String httpGet(String url, List<NameValuePair> nameValuePairs) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();
		String sb = "";
		String result = "";
		try {
			for (NameValuePair nvp : nameValuePairs) {
				sb += nvp.getName() + "=" + nvp.getValue() + "&";
			}
			HttpGet httpGet = null;
			if (sb.length() > 0) {
				sb = sb.substring(0, sb.length() - 1);
				sb = URLDecoder.decode(sb, "UTF-8");
				httpGet = new HttpGet(url + "?" + sb);
			} else {
				httpGet = new HttpGet(url);
			}
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				result = EntityUtils.toString(response.getEntity());
			} else {
				result += "发送失败:" + response.getStatusLine().getStatusCode();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public static String httpGet2(String url, List<NameValuePair> nameValuePairs,String token) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();
		String sb = "";
		String result = "";
		try {
			for (NameValuePair nvp : nameValuePairs) {
				sb += nvp.getName() + "=" + nvp.getValue() + "&";
			}
			HttpGet httpGet = null;

			if (sb.length() > 0) {
				sb = sb.substring(0, sb.length() - 1);
				sb = URLDecoder.decode(sb, "UTF-8");
				httpGet = new HttpGet(url + "?" + sb);
			} else {
				httpGet = new HttpGet(url);
			}
			httpGet.setHeader("Authorization", String.format("%s %s", "Bearer", token));
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				result = EntityUtils.toString(response.getEntity());
			} else {
				result += "发送失败:" + response.getStatusLine().getStatusCode();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public static String httpPost(String url, String params,String xRioSeq,String signature,String timestamp) {
		String result = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();

			String Pagination = URLEncoder.encode("["+params+"]", "UTF-8");
			url=url+"params="+Pagination;
			//System.out.println(url);
			HttpPost httpPost = new HttpPost(url);
			/*
			 * 发送json字符串，这两句需要设置
			 */
			httpPost.addHeader("Content-type", "application/json");
			httpPost.addHeader("x-rio-seq",xRioSeq);
			httpPost.addHeader("signature",signature);
			httpPost.addHeader("timestamp",timestamp);
			//httpPost.setEntity(new StringEntity(params, "UTF-8"));

			HttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				// Read the response body
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String postWithParamsForString(String url, List<NameValuePair> params) {
		HttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		String msg = "";
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
			HttpResponse response = client.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				msg = EntityUtils.toString(entity);
//                System.out.println(msg);
//                String conStrValue=response.get
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}

}
