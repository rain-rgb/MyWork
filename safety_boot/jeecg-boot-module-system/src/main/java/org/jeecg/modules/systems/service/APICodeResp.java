package org.jeecg.modules.systems.service;



import lombok.Data;

/**
 * 返回项目数据
 */
@Data
public class APICodeResp extends BaseRequest {


  /**
   * 项目编码
   */
  private String code;

  /**
   * 项目名称
   */
  private String time_expire;

  private String tenant_id="1004800000003922977";
  private String mobile="15358111713";
}

