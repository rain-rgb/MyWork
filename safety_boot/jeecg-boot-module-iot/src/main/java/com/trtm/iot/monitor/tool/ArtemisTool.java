package com.trtm.iot.monitor.tool;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.trtm.iot.monitor.entity.MonitorControlling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ArtemisTool {


	private static String host = "123.60.82.172";
	private static String appKey = "23740806";
	private static String appSecret = "z7zSirX0SWc44WmQ9xxm";


	private static String tokenAppKey = "2162dac9ea384e198e03f077491b610f";
	private static String tokenAppSecret = "ac4474f013f44191b670c134ddf05a36";


//	public static void main(String[] args) throws Exception {
//		Object ac3899050 = getPreviewURLs("12479cd027ea47b892967fddff6ae7ff","hls");
//		Object hls = controlling("12479cd027ea47b892967fddff6ae7ff", "left",0);
//		System.out.println(hls);
//		Object camerasList = getCamerasList();
//		System.out.println(camerasList);
//	}

	public static String getToken(){
		String url = "https://open.ys7.com/api/lapp/token/get?appKey="+ tokenAppKey + "&appSecret=" + tokenAppSecret;
		String result = HttpRequest.post(url).execute().body();// 请求并返回结果
		if (null != result) {
			JSONObject json = JSONObject.parseObject(result);// 结果转换
			String code = json.getString("code");
			if ("200".equals(code)) {
				String accessToken = json.getJSONObject("data").getString("accessToken");
				return accessToken;
			}
		}
		return null;
	}

	public static String request(String api, JSONObject jsonBody ) throws Exception {

		/**
		 * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
		 */
		ArtemisConfig artemisConfig = new ArtemisConfig();
		artemisConfig.setHost(host);
		artemisConfig.setAppKey(appKey);
		artemisConfig.setAppSecret(appSecret);

		Map<String, String> map = new HashMap<>();
		map.put("host",host);
		map.put("appKey",appKey);
		map.put("appSecret",appSecret);

		/**
		 * STEP2：设置OpenAPI接口的上下文
		 */
		final String ARTEMIS_PATH = "/artemis";

		/**
		 * STEP3：设置接口的URI地址
		 */
		final String previewURLsApi = ARTEMIS_PATH + api;
		Map<String, String> path = new HashMap<String, String>(2) {
			{
				put("https://", previewURLsApi);//根据现场环境部署确认是http还是https
			}
		};

		/**
		 * STEP4：设置参数提交方式
		 */
		String contentType = "application/json";

		/**
		 * STEP5：组装请求参数
		 */
		String body = jsonBody.toJSONString();

		/**
		 * STEP6：调用接口
		 */
		String result = ArtemisHttpUtil.doPostStringArtemis( path, body, null, null,
				contentType , map);// post请求application/json类型参数

		return result;
	}

	/**
	 *
	 * 获取区域
	 *
	 */
	public static String[] regionNodesByParams(String regionName) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resourceType", "region");
		jsonObject.put("isSubRegion", true);
		jsonObject.put("pageNo", 1);
		jsonObject.put("pageSize", 100);
		jsonObject.put("regionName", regionName);

		String result = request("/api/irds/v2/region/nodesByParams", jsonObject);
		if(null != result){
			JSONObject json = JSONObject.parseObject(result);// 结果转换
			String code = json.getString("code");
			if ("0".equals(code)) {
				JSONArray list = json.getJSONObject("data").getJSONArray("list");
				if(null != list && list.size() > 0){
					String[] strs = new String[list.size()];
					for(int i=0;i<list.size();i++){
						strs[i] = list.getJSONObject(i).getString("indexCode");
					}
					return strs;
				}
			}
		}

		return null;

	}

	/**
	 *
	 * 获取监控点列表
	 *
	 */
	public static JSONArray getCameraList(String regionName) throws Exception {
		String[] regionIndexCodes = regionNodesByParams(regionName);
		if(null != regionIndexCodes){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("regionIndexCodes", regionIndexCodes);
			jsonObject.put("isSubRegion", true);
			jsonObject.put("pageNo", 1);
			jsonObject.put("pageSize", 10);

			String result = request("/api/resource/v2/camera/search", jsonObject);

			if(null != result){
				JSONObject json = JSONObject.parseObject(result);// 结果转换
				String code = json.getString("code");
				if ("0".equals(code)) {
					JSONArray list = json.getJSONObject("data").getJSONArray("list");
					return list;
				}
			}
		}

		return null;
	}

	/**
	 *
	 * 预览取流
	 *
	 */
	public static Object getVideoList(String regionName) throws Exception {
		JSONArray cameraList = getCameraList(regionName);
		if(null != cameraList){
			for(int i=0;i<cameraList.size();i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("cameraIndexCode", cameraList.getJSONObject(i).getString("indexCode"));
				jsonObject.put("protocol", "hls");

				String result = request("/api/video/v2/cameras/previewURLs", jsonObject);

				if (null != result) {
					JSONObject json = JSONObject.parseObject(result);// 结果转换
					String code = json.getString("code");
					if ("0".equals(code)) {
						String url = json.getJSONObject("data").getString("url");
						cameraList.getJSONObject(i).put("url", url);
					}
				}
			}
		}

		return cameraList;
	}



	/**
	 *
	 * 预览取流
	 *
	 */
	public static Object getPreviewURLs(String cameraCode,String protocol) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("cameraIndexCode", cameraCode);
		jsonObject.put("protocol",protocol );

		String result = request("/api/video/v2/cameras/previewURLs", jsonObject);

		if (null != result) {
			JSONObject json = JSONObject.parseObject(result);// 结果转换
			String code = json.getString("code");
			if ("0".equals(code)) {
				String url = json.getJSONObject("data").getString("url");
				return url;
			}
		}
		return null;
	}




	/**
	 *
	 * 根据监控点编号进行云台操作接口
	 * https://open.hikvision.com/docs/docId?productId=5c67f1e2f05948198c909700&tagPath=API%E5%88%97%E8%A1%A8-%E8%A7%86%E9%A2%91%E4%B8%9A%E5%8A%A1-%E8%A7%86%E9%A2%91%E8%83%BD%E5%8A%9B&version=%2F60df74fdc6f24041ac3d2d7f81c32325
	 */
	public static Object controlling(String cameraCode, String command,Integer action) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("cameraIndexCode", cameraCode);
		jsonObject.put("action", action);// 0-开始 1-停止 注：GOTO_PRESET命令下填任意值均可转到预置点,建议填0即可
		jsonObject.put("command",command );// 命令
//		jsonObject.put("speed",20 );// 台速度，取值范围为1-100，默认50
//		jsonObject.put("presetIndex",20 );// 预置点编号，整数，通常在300以内
		String result = request("/api/video/v1/ptzs/controlling", jsonObject);

		if (null != result) {
			JSONObject json = JSONObject.parseObject(result);// 结果转换
			String code = json.getString("code");
			if ("0".equals(code)) {
				return json;
			}else{
				return json;
			}
		}
		return null;
	}


	/**
	 *
	 * 该接口用于控制监控点进行3D电子放大
	 * https://open.hikvision.com/docs/docId?productId=5c67f1e2f05948198c909700&tagPath=API%E5%88%97%E8%A1%A8-%E8%A7%86%E9%A2%91%E4%B8%9A%E5%8A%A1-%E8%A7%86%E9%A2%91%E8%83%BD%E5%8A%9B&version=%2F60df74fdc6f24041ac3d2d7f81c32325
	 *
	 * 开始放大的X坐标，范围：0-255。由于设备比例限制，以及实际场景屏幕比例大小不同，请按照如下坐标位计算方式计算入参：屏幕X坐标/屏幕宽 * 255，即该坐标位X坐标占总屏幕宽的比例*255。监控点会对startX、startY、endX 、endY四点围成的区域进行放大。
	 */
	public static Object sel3DZoom(String cameraCode,Integer startX,Integer startY,Integer endX,Integer endY) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("cameraIndexCode", cameraCode);
		jsonObject.put("startX", startX);// 0-开始 1-停止 注：GOTO_PRESET命令下填任意值均可转到预置点,建议填0即可
		jsonObject.put("startY",startY );// 命令
		jsonObject.put("endX",endX );// 台速度，取值范围为1-100，默认50
		jsonObject.put("endY",endY);// 预置点编号，整数，通常在300以内
		String result = request("/api/video/v1/ptzs/selZoom", jsonObject);

		if (null != result) {
			JSONObject json = JSONObject.parseObject(result);// 结果转换
			String code = json.getString("code");
			if ("0".equals(code)) {
				return json;
			}else{
				return json;
			}
		}
		return null;
	}

	/**
	 *
	 * 获取摄像机
	 *
	 */
	public static Object getCamerasList() throws Exception {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pageNo", 1);
		jsonObject.put("pageSize", 500);

		String result = request("/api/resource/v1/cameras", jsonObject);

		if (null != result) {
			JSONObject json = JSONObject.parseObject(result);// 结果转换
			String code = json.getString("code");
			if ("0".equals(code)) {
				JSONArray list = json.getJSONObject("data").getJSONArray("list");
				return list;
			}
		}

		return null;
	}

}
