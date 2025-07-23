package com.trtm.iot.devicepipepilestatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicepipepilestatistics.entity.DevicePipepileStatistics;
import com.trtm.iot.devicepipepilestatistics.mapper.DevicePipepileStatisticsMapper;
import com.trtm.iot.devicepipepilestatistics.service.IDevicePipepileStatisticsService;
import com.trtm.iot.pipepileYujing.entity.PipepileYujing;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: device_pipepile_statistics
 * @Author: jeecg-boot
 * @Date:   2022-09-20
 * @Version: V1.0
 */
@Service
public class DevicePipepileStatisticsServiceImpl extends ServiceImpl<DevicePipepileStatisticsMapper, DevicePipepileStatistics> implements IDevicePipepileStatisticsService {

    @Override
    public DevicePipepileStatistics selectones(String datanyr1, String shebeino, String pileMileage) {
        try {
            QueryWrapper<DevicePipepileStatistics> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stadate",datanyr1);
            queryWrapper.eq("shebeino",shebeino);
            queryWrapper.eq("mileage",pileMileage);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DevicePipepileStatistics selectone(String datanyr1, String shebeino) {
        try {
            QueryWrapper<DevicePipepileStatistics> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stadate",datanyr1);
            queryWrapper.eq("shebeino",shebeino);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
