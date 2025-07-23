package com.trtm.iot.devicehistorystatic.service;

import com.trtm.iot.devicehistorystatic.entity.DevicehistoryStatic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * @Description: 环境监测数据统计表
 * @Author: jeecg-boot
 * @Date:   2022-07-09
 * @Version: V1.0
 */
public interface IDevicehistoryStaticService extends IService<DevicehistoryStatic> {

    DevicehistoryStatic selectone(String shebeino, Date date);
}
