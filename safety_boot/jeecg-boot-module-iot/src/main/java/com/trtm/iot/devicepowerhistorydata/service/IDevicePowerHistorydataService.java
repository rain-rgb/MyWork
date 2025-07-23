package com.trtm.iot.devicepowerhistorydata.service;

import com.trtm.iot.devicepowerhistorydata.entity.DevicePowerHistorydata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: device_power_historydata
 * @Author: jeecg-boot
 * @Date:   2022-12-29
 * @Version: V1.0
 */
public interface IDevicePowerHistorydataService extends IService<DevicePowerHistorydata> {


    List<DevicePowerHistorydata> selectListToSHYJ(String shebeiNo, Integer id);

    List<DevicePowerHistorydata> sendMsg(Integer curid);

    List<DevicePowerHistorydata> selectListbim(String shebeiNo, Integer id);

    List<DevicePowerHistorydata> selectListtoJG(String shebeilist, Integer curid);
}
