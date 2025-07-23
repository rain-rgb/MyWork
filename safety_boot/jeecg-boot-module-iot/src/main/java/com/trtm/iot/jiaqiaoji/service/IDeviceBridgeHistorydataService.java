package com.trtm.iot.jiaqiaoji.service;

import com.trtm.iot.jiaqiaoji.entity.DeviceBridgeHistorydata;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.jiaqiaoji.entity.DeviceBridgeRealdata;

import java.util.List;
import java.util.Map;

/**
 * @Description: device_bridge_historydata
 * @Author: jeecg-boot
 * @Date:   2023-03-13
 * @Version: V1.0
 */
public interface IDeviceBridgeHistorydataService extends IService<DeviceBridgeHistorydata> {

    List<DeviceBridgeHistorydata> selectJQJList(String shebeilist, Integer curid);

    List<DeviceBridgeHistorydata> selectListbim(String shebeilist, Integer curid);

    List<Map<String, Object>> getYjList();

    void cbpanduan(DeviceBridgeRealdata deviceBridgeRealdata);
}
