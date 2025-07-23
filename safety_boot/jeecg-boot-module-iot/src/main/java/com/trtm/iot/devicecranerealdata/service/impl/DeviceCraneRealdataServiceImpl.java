package com.trtm.iot.devicecranerealdata.service.impl;

import com.trtm.iot.devicecranerealdata.entity.DeviceCraneRealdata;
import com.trtm.iot.devicecranerealdata.mapper.DeviceCraneRealdataMapper;
import com.trtm.iot.devicecranerealdata.service.IDeviceCraneRealdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 龙门吊实时数据
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
@Service
public class DeviceCraneRealdataServiceImpl extends ServiceImpl<DeviceCraneRealdataMapper, DeviceCraneRealdata> implements IDeviceCraneRealdataService {

    @Autowired
    DeviceCraneRealdataMapper deviceCraneRealdataMapper;
    @Override
    public DeviceCraneRealdata queryone(String shebeiNo) {
        return deviceCraneRealdataMapper.queryone(shebeiNo);
    }

    @Override
    public List<DeviceCraneRealdata> selectListBySB(List<String> shebeiList) {
        return deviceCraneRealdataMapper.selectListBySB(shebeiList);
    }
}
