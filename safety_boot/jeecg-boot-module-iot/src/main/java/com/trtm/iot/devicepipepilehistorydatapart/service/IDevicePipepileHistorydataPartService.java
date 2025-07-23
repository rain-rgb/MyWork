package com.trtm.iot.devicepipepilehistorydatapart.service;

import com.trtm.iot.devicepipepilehistorydatapart.entity.DevicePipepileHistorydataPart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: device_pipepile_historydata_part
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
public interface IDevicePipepileHistorydataPartService extends IService<DevicePipepileHistorydataPart> {

    List<DevicePipepileHistorydataPart> selectListwcx(String shebeino, String pileNo);
}
