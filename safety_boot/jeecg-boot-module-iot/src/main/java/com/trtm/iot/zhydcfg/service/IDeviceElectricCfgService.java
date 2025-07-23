package com.trtm.iot.zhydcfg.service;

import com.trtm.iot.zhydcfg.entity.DeviceElectricCfg;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.zhydreal.entity.DeviceElectricRealdata;

import java.util.List;

/**
 * @Description: 智慧用电配置主表
 * @Author: jeecg-boot
 * @Date:   2021-05-18
 * @Version: V1.0
 */
public interface IDeviceElectricCfgService extends IService<DeviceElectricCfg> {

    List<DeviceElectricCfg> selectzhydlist(String imei);

    DeviceElectricCfg selectzhydcallone(String imei);

}
