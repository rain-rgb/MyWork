package com.trtm.iot.device.service.impl;

import com.trtm.iot.device.entity.Device;
import com.trtm.iot.device.mapper.DeviceMapper;
import com.trtm.iot.device.service.IDeviceService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 环境监测实时数据
 * @Author: jeecg-boot
 * @Date:   2022-04-11
 * @Version: V1.0
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

}
