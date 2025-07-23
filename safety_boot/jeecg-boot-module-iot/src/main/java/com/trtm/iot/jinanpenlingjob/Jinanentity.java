package com.trtm.iot.jinanpenlingjob;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Jinanentity {
    Integer deviceId;//设备ID
    String deviceNum;//设备编号吧
    String deviceName;//设备名称
    Integer deviceStatus;//设备状态：1在线，2离线
    Boolean forbidden;//设备禁用启用
    String remark;//备注
    Double tem;//温度
    Double hum;//湿度
    String createTime;//创建时间 yyyy-MM-dd HH:mm:ss
    String imei;//imei
    String iccid;//iccid
    String sysPhone;//用户手机号
}
