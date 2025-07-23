package com.trtm.iot.devicemixpilehistorydatapart.service;

import com.trtm.iot.devicemixpilehistorydatapart.entity.DeviceMixpileHistorydataPart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 水泥搅拌桩成桩记录每段数据信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-19
 * @Version: V1.0
 */
public interface IDeviceMixpileHistorydataPartService extends IService<DeviceMixpileHistorydataPart> {

    double selecmaxpile(String shebeino, String pileno, String pileMileage);

    double selecmaxpiles(String shebeino, String pileno, String date);
}
