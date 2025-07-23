package com.trtm.iot.devicemixgrouted.service;

import com.trtm.iot.devicemixgrouted.entity.DeviceMixpileGroutedOne;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 灌注桩详情
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
public interface IDeviceMixpileGroutedOneService extends IService<DeviceMixpileGroutedOne> {

    DeviceMixpileGroutedOne getByUuid(String uuid);
}
