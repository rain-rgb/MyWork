package com.trtm.iot.zhydcfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.zhydcfg.entity.DeviceElectricCfg;
import com.trtm.iot.zhydcfg.mapper.DeviceElectricCfgMapper;
import com.trtm.iot.zhydcfg.service.IDeviceElectricCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 智慧用电配置主表
 * @Author: jeecg-boot
 * @Date:   2021-05-18
 * @Version: V1.0
 */
@Service
public class DeviceElectricCfgServiceImpl extends ServiceImpl<DeviceElectricCfgMapper, DeviceElectricCfg> implements IDeviceElectricCfgService {


    @Autowired
    DeviceElectricCfgMapper DeviceElectricCfgMapper;

    @Override
    public List<DeviceElectricCfg> selectzhydlist(String imei){
        try {
            QueryWrapper<DeviceElectricCfg> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("imei",imei);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public DeviceElectricCfg selectzhydcallone(String imei){
        try {
            QueryWrapper<DeviceElectricCfg> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("imei",imei);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

}
