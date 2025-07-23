package com.trtm.iot.devicecranecfg.service;

import com.trtm.iot.devicecranecfg.entity.DeviceCraneCfg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 大型设备(提梁机/架桥机)配置数据表
 * @Author: jeecg-boot
 * @Date:   2021-12-03
 * @Version: V1.0
 */
public interface IDeviceCraneCfgService extends IService<DeviceCraneCfg> {

    DeviceCraneCfg selectcranecallone(String shebeino);
}
