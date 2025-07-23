package com.trtm.iot.deviceLargeOverHandler.service;

import com.trtm.iot.deviceLargeOverHandler.entity.DeviceLargeOverHandler;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: device_large_over_handler
 * @Author: jeecg-boot
 * @Date:   2022-10-24
 * @Version: V1.0
 */
public interface IDeviceLargeOverHandlerService extends IService<DeviceLargeOverHandler> {

    DeviceLargeOverHandler selectOne(String baseid);
}
