package com.trtm.iot.zhydstatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.zhydstatistics.entity.DeviceElectrcStatistics;
import com.trtm.iot.zhydstatistics.mapper.DeviceElectrcStatisticsMapper;
import com.trtm.iot.zhydstatistics.service.IDeviceElectrcStatisticsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description: 智慧用电历史数据统计表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-02
 * @Version: V1.0
 */
@Service
public class DeviceElectrcStatisticsServiceImpl extends ServiceImpl<DeviceElectrcStatisticsMapper, DeviceElectrcStatistics> implements IDeviceElectrcStatisticsService {

    @Resource
    DeviceElectrcStatisticsMapper deviceElectrcStatisticsMapper;
    @Override
    public DeviceElectrcStatistics selectlimit(Date date,String sbno) {
//        try{
//            QueryWrapper<DeviceElectrcStatistics> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("statistics_time",date);
//            queryWrapper.eq("imei",sbno);
//            return this.getOne(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
        return deviceElectrcStatisticsMapper.selectlimit(date,sbno);
    }
}
