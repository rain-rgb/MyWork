package org.jeecg.modules.systems.service;

import com.znlh.kgl.openapi.constant.Options;
import org.jeecg.modules.systems.service.impl.SaasService;


public interface SaasClient {
  static SaasClient create(Options options) {
    return new SaasService(options);
  }

  public  <T> T execute(String method, Class<T> classOfT);

  public <T> T execute(String method, BaseRequest request, Class<T> classOfT);
}
