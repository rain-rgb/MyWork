package com.trtm.iot.devicepowerhistorydata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicepowerhistorydata.entity.DevicePowerHistorydata;
import com.trtm.iot.devicepowerhistorydata.mapper.DevicePowerHistorydataMapper;
import com.trtm.iot.devicepowerhistorydata.service.IDevicePowerHistorydataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: device_power_historydata
 * @Author: jeecg-boot
 * @Date:   2022-12-29
 * @Version: V1.0
 */
@Service
public class DevicePowerHistorydataServiceImpl extends ServiceImpl<DevicePowerHistorydataMapper, DevicePowerHistorydata> implements IDevicePowerHistorydataService {

    @Autowired
    private DevicePowerHistorydataMapper devicePowerHistorydataMapper;

    @Override
    public List<DevicePowerHistorydata> selectListToSHYJ(String shebeiNo, Integer id) {
        return  devicePowerHistorydataMapper.selectListToSHYJ(shebeiNo,id);
    }

    @Override
    public List<DevicePowerHistorydata> sendMsg(Integer curid) {
        QueryWrapper<DevicePowerHistorydata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("devicestatus",3);
        queryWrapper.gt("id",curid);
        List<DevicePowerHistorydata> devicePowerHistorydata = devicePowerHistorydataMapper.selectList(queryWrapper);
        return devicePowerHistorydata;
    }

    @Override
    public List<DevicePowerHistorydata> selectListbim(String shebeiNo, Integer id) {
        return  devicePowerHistorydataMapper.selectListbim(shebeiNo,id);
    }

    @Override
    public List<DevicePowerHistorydata> selectListtoJG(String shebeilist, Integer curid) {
        return devicePowerHistorydataMapper.selectListtoJG(shebeilist,curid);
    }
}
