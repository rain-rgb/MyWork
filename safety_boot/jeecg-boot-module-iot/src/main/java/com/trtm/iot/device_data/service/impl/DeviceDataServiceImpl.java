package com.trtm.iot.device_data.service.impl;

import com.trtm.iot.device_data.entity.DeviceData;
import com.trtm.iot.device_data.mapper.DeviceDataMapper;
import com.trtm.iot.device_data.service.IDeviceDataService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 搅拌车监控
 * @Author: jeecg-boot
 * @Date:   2025-05-06
 * @Version: V1.0
 */
@Service
public class DeviceDataServiceImpl extends ServiceImpl<DeviceDataMapper, DeviceData> implements IDeviceDataService {

}
