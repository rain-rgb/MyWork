package com.trtm.iot.devicepipepilestatistics.service;

import com.trtm.iot.devicepipepilestatistics.entity.DevicePipepileStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: device_pipepile_statistics
 * @Author: jeecg-boot
 * @Date:   2022-09-20
 * @Version: V1.0
 */
public interface IDevicePipepileStatisticsService extends IService<DevicePipepileStatistics> {

    DevicePipepileStatistics selectones(String datanyr1, String shebeino, String pileMileage);

    DevicePipepileStatistics selectone(String datanyr1, String shebeino);
}
