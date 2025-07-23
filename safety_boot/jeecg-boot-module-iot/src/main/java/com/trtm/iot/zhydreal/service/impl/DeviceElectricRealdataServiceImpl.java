package com.trtm.iot.zhydreal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydreal.entity.DeviceElectricRealdata;
import com.trtm.iot.zhydreal.mapper.DeviceElectricRealdataMapper;
import com.trtm.iot.zhydreal.service.IDeviceElectricRealdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 智慧用电实时数据表
 * @Author: jeecg-boot
 * @Date: 2021-05-16
 * @Version: V1.0
 */
@Service
public class DeviceElectricRealdataServiceImpl extends ServiceImpl<DeviceElectricRealdataMapper, DeviceElectricRealdata> implements IDeviceElectricRealdataService {

    @Autowired
    DeviceElectricRealdataMapper deviceElectricRealdataMapper;

    /**
     * 根据条件查出对应的数据
     *
     * @param id
     * @param
     * @return
     */
    @Override
    public List<DeviceElectricRealdata> selectzhydone(Integer id) {
//        try {
//            QueryWrapper<DeviceElectricRealdata> queryWrapper = new QueryWrapper<>();
//            queryWrapper.ge("id", id);
//            queryWrapper.last("limit 100");
//            return this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
        return deviceElectricRealdataMapper.selectzhydone(id);
    }

    @Override
    public int updatezhydone(Integer id, Integer alertstate) {

        return deviceElectricRealdataMapper.updatealertstate(id, alertstate);
    }

    @Override
    public void updateStatus(Integer id, Integer status) {
        deviceElectricRealdataMapper.updateStatus(id, status);
    }

    /**
     * 根据设备编号查询智慧用电表数据
     *
     * @param imei
     * @return
     */
    @Override
    public List<DeviceElectricRealdata> selectzhydbaselist(String imei, Integer id) {
        try {
            QueryWrapper<DeviceElectricRealdata> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("imei", imei);
            queryWrapper.eq("id", id);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DeviceElectricRealdata> selectLists(List<String> shebeiNo, Integer id) {
        try {
            QueryWrapper<DeviceElectricRealdata> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("id", id);
            queryWrapper.eq("status", 2);
            queryWrapper.in("imei", shebeiNo);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DeviceElectricHistorydata> selectRealList(Integer curid, String shebeilist) {

        return deviceElectricRealdataMapper.selectRealList(curid,shebeilist);
    }
}
