package com.trtm.iot.devicehammeringhistorydataone.service;

import com.trtm.iot.devicehammeringhistorydataone.entity.DeviceHammeringHistorydataOne;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: device_hammering_historydata_one
 * @Author: jeecg-boot
 * @Date:   2024-03-08
 * @Version: V1.0
 */
public interface IDeviceHammeringHistorydataOneService extends IService<DeviceHammeringHistorydataOne> {

    List<DeviceHammeringHistorydataOne> selectjbzzone(Integer curid, int i);
}
