package com.trtm.iot.devicehistoryyujing.service;

import com.trtm.iot.devicehistoryyujing.entity.DevicehistoryYujing;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 预警配置表
 * @Author: jeecg-boot
 * @Date:   2022-06-23
 * @Version: V1.0
 */
public interface IDevicehistoryYujingService extends IService<DevicehistoryYujing> {

    DevicehistoryYujing selectone(String devaddr);
}
