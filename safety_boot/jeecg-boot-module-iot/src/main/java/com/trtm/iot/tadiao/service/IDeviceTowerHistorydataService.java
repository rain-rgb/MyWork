package com.trtm.iot.tadiao.service;

import com.trtm.iot.tadiao.entity.DeviceTowerHistorydata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: device_tower_historydata
 * @Author: jeecg-boot
 * @Date:   2023-02-27
 * @Version: V1.0
 */
public interface IDeviceTowerHistorydataService extends IService<DeviceTowerHistorydata> {

    List<Map<String, Object>> getYjList();
}
