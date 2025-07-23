package com.trtm.iot.devicepipepilereport.service;

import com.trtm.iot.devicepipepilereport.entity.DevicePipepileReport;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: device_pipepile_report
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
public interface IDevicePipepileReportService extends IService<DevicePipepileReport> {

    DevicePipepileReport selectone(String shebeino, String pileNo);
}
