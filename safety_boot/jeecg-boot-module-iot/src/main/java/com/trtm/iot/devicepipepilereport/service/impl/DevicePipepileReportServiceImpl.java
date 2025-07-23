package com.trtm.iot.devicepipepilereport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicepipepilereport.entity.DevicePipepileReport;
import com.trtm.iot.devicepipepilereport.mapper.DevicePipepileReportMapper;
import com.trtm.iot.devicepipepilereport.service.IDevicePipepileReportService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: device_pipepile_report
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
@Service
public class DevicePipepileReportServiceImpl extends ServiceImpl<DevicePipepileReportMapper, DevicePipepileReport> implements IDevicePipepileReportService {

    @Override
    public DevicePipepileReport selectone(String shebeino, String pileNo) {
        QueryWrapper<DevicePipepileReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shebeino",shebeino);
        queryWrapper.eq("pileno",pileNo);
        return this.getOne(queryWrapper);
    }
}
