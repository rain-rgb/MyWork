package com.trtm.iot.devicehistorystatic.service.impl;

import com.trtm.iot.devicehistorystatic.entity.DevicehistoryStatic;
import com.trtm.iot.devicehistorystatic.mapper.DevicehistoryStaticMapper;
import com.trtm.iot.devicehistorystatic.service.IDevicehistoryStaticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

/**
 * @Description: 环境监测数据统计表
 * @Author: jeecg-boot
 * @Date:   2022-07-09
 * @Version: V1.0
 */
@Service
public class DevicehistoryStaticServiceImpl extends ServiceImpl<DevicehistoryStaticMapper, DevicehistoryStatic> implements IDevicehistoryStaticService {

    @Autowired DevicehistoryStaticMapper devicehistoryStaticMapper;
    @Override
    public DevicehistoryStatic selectone(String shebeino, Date date) {
        return devicehistoryStaticMapper.selectone(shebeino,date);
    }
}
