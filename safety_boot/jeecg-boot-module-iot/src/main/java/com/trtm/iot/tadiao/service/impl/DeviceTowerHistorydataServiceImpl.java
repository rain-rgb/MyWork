package com.trtm.iot.tadiao.service.impl;

import com.trtm.iot.tadiao.entity.DeviceTowerHistorydata;
import com.trtm.iot.tadiao.mapper.DeviceTowerHistorydataMapper;
import com.trtm.iot.tadiao.service.IDeviceTowerHistorydataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: device_tower_historydata
 * @Author: jeecg-boot
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Service
public class DeviceTowerHistorydataServiceImpl extends ServiceImpl<DeviceTowerHistorydataMapper, DeviceTowerHistorydata> implements IDeviceTowerHistorydataService {

    @Autowired
    private DeviceTowerHistorydataMapper deviceTowerHistorydataMapper;

    @Override
    public List<Map<String, Object>> getYjList() {
        return deviceTowerHistorydataMapper.getYjList();
    }
}
