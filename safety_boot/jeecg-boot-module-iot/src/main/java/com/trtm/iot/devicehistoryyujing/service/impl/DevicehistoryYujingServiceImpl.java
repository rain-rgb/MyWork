package com.trtm.iot.devicehistoryyujing.service.impl;

import com.trtm.iot.devicehistoryyujing.entity.DevicehistoryYujing;
import com.trtm.iot.devicehistoryyujing.mapper.DevicehistoryYujingMapper;
import com.trtm.iot.devicehistoryyujing.service.IDevicehistoryYujingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 预警配置表
 * @Author: jeecg-boot
 * @Date:   2022-06-23
 * @Version: V1.0
 */
@Service
public class DevicehistoryYujingServiceImpl extends ServiceImpl<DevicehistoryYujingMapper, DevicehistoryYujing> implements IDevicehistoryYujingService {

    @Autowired DevicehistoryYujingMapper devicehistoryYujingMapper;
    @Override
    public DevicehistoryYujing selectone(String devaddr) {
        return devicehistoryYujingMapper.selectone(devaddr);
    }
}
