package com.trtm.iot.device_concrete_historydata.service;

import com.trtm.iot.device_concrete_historydata.entity.DeviceConcreteHistorydata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 混凝土成熟度监测历史表
 * @Author: jeecg-boot
 * @Date:   2024-10-22
 * @Version: V1.0
 */
public interface IDeviceConcreteHistorydataService extends IService<DeviceConcreteHistorydata> {

    List<DeviceConcreteHistorydata> getBySbbh(String sbbh);
}
