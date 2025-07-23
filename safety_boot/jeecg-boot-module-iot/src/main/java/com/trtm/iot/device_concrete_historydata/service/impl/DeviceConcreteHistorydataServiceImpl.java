package com.trtm.iot.device_concrete_historydata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.device_concrete_historydata.entity.DeviceConcreteHistorydata;
import com.trtm.iot.device_concrete_historydata.mapper.DeviceConcreteHistorydataMapper;
import com.trtm.iot.device_concrete_historydata.service.IDeviceConcreteHistorydataService;
import com.trtm.iot.device_concrete_realdata.entity.DeviceConcreteRealdata;
import com.trtm.iot.device_concrete_realdata.service.IDeviceConcreteRealdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * @Description: 混凝土成熟度监测历史表
 * @Author: jeecg-boot
 * @Date:   2024-10-22
 * @Version: V1.0
 */
@Service
public class DeviceConcreteHistorydataServiceImpl extends ServiceImpl<DeviceConcreteHistorydataMapper, DeviceConcreteHistorydata> implements IDeviceConcreteHistorydataService {

    @Autowired
    IDeviceConcreteHistorydataService deviceConcreteHistorydataService;
    @Autowired
    IDeviceConcreteRealdataService deviceConcreteRealdataService;
    @Override
    public List<DeviceConcreteHistorydata> getBySbbh(String sbbh) {
        // 获取当前时间
        LambdaQueryWrapper<DeviceConcreteRealdata> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(DeviceConcreteRealdata::getSbbh,sbbh);
        DeviceConcreteRealdata one = deviceConcreteRealdataService.getOne(queryWrapper1);
        Date updatetime = one.getUpdatetime();
        // 计算七天前的时间
        LocalDateTime now = updatetime.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime sevenDaysAgo = now.minus(7, ChronoUnit.DAYS);
        LambdaQueryWrapper<DeviceConcreteHistorydata> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceConcreteHistorydata::getSbbh,sbbh)
                .between(DeviceConcreteHistorydata::getUpdatetime, sevenDaysAgo, now)
                .orderByAsc(DeviceConcreteHistorydata::getUpdatetime);
        return deviceConcreteHistorydataService.list(queryWrapper);
    }
}
