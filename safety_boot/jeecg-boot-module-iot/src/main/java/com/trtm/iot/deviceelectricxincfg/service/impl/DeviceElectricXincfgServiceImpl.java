package com.trtm.iot.deviceelectricxincfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.deviceelectricxincfg.entity.DeviceElectricXincfg;
import com.trtm.iot.deviceelectricxincfg.mapper.DeviceElectricXincfgMapper;
import com.trtm.iot.deviceelectricxincfg.service.IDeviceElectricXincfgService;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: device_electric_xincfg
 * @Author: jeecg-boot
 * @Date:   2023-06-25
 * @Version: V1.0
 */
@Service
public class DeviceElectricXincfgServiceImpl extends ServiceImpl<DeviceElectricXincfgMapper, DeviceElectricXincfg> implements IDeviceElectricXincfgService {
    @Autowired
    private DeviceElectricXincfgMapper electricXincfgMapper;

    @Override
    public DeviceElectricXincfg selectbyshebeione(String imei) {
        QueryWrapper<DeviceElectricXincfg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("imei",imei);
        DeviceElectricXincfg deviceElectricXincfg = electricXincfgMapper.selectOne(queryWrapper);
        return deviceElectricXincfg;
    }
}
