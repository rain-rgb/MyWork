package com.trtm.iot.zhydstatistics.service;

import com.trtm.iot.zhydstatistics.entity.DeviceElectrcStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * @Description: 智慧用电历史数据统计表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-02
 * @Version: V1.0
 */
public interface IDeviceElectrcStatisticsService extends IService<DeviceElectrcStatistics> {

    DeviceElectrcStatistics selectlimit(Date date,String sbno);
}
