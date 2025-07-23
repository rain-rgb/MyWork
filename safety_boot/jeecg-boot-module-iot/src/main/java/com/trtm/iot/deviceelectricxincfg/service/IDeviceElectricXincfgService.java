package com.trtm.iot.deviceelectricxincfg.service;

import com.trtm.iot.deviceelectricxincfg.entity.DeviceElectricXincfg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: device_electric_xincfg
 * @Author: jeecg-boot
 * @Date:   2023-06-25
 * @Version: V1.0
 */
public interface IDeviceElectricXincfgService extends IService<DeviceElectricXincfg> {

    DeviceElectricXincfg selectbyshebeione(String imei);
}
