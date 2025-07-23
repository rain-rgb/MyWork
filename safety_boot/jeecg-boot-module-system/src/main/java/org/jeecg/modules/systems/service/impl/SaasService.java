package org.jeecg.modules.systems.service.impl;

import com.google.gson.Gson;
import org.jeecg.modules.systems.service.BaseRequest;
import com.znlh.kgl.openapi.constant.Options;

import com.znlh.kgl.openapi.service.connection.ConnectionFactory;
import com.znlh.kgl.openapi.service.connection.RestConnection;
import com.znlh.kgl.openapi.service.signature.ApiSignature;
import com.znlh.kgl.openapi.service.signature.UrlParamsBuilder;
import okhttp3.Request;
import okhttp3.Request.Builder;
import org.jeecg.modules.systems.service.SaasClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SaasService implements SaasClient {

  //接口api路径
  public static final String INTERFACE_PATH = "/api";

  private final Gson gson = new Gson();

  private final Options options;
  private final RestConnection restConnection;

  public SaasService(Options options) {
    this.options = options;
    this.restConnection = new RestConnection(options);
  }

  public <T> T execute(String method, Class<T> classOfT) {

    return executePostWithSignature(method, null,classOfT);

  }


  public <T> T execute(String method, BaseRequest request, Class<T> classOfT) {
    return executePostWithSignature(method,request,classOfT);
  }

  public <T> T  executePostWithSignature(String method, BaseRequest request, Class<T> classOfT) {

    Map paramMap = new HashMap();

    if(Objects.nonNull(request)){
      paramMap = gson.fromJson(gson.toJson(request), Map.class);
    }

    String payload = gson.toJson(paramMap);
    String gmt8Now = ApiSignature.gmt8Now();
    String apiSignature = ApiSignature.createSignature(options.getAppId(), options.getSecretKey(),
        method, options.getVersion(), gmt8Now, payload);
    UrlParamsBuilder builder = UrlParamsBuilder.build().putToPost("app_id", options.getAppId())
        .putToPost("app_token", options.getToken()).putToPost("method", method)
        .putToPost("timestamp", gmt8Now).putToPost("version", options.getVersion())
        .putToPost("biz_content", paramMap);
    builder.putToPost("sign", apiSignature);

    String requestUrl = options.getRestHost() + INTERFACE_PATH;
    Request executeRequest = (new Builder()).url(requestUrl).post(builder.buildPostBody())
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", options.getToken()).build();
    String resp = ConnectionFactory.execute(executeRequest);

    return gson.fromJson(resp, classOfT);
  }


}
