package com.trtm.iot.devicemixpilehistorydata.service;

import com.trtm.iot.devicemixpilehistorydata.entity.DeviceMixpileHistorydata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: device_mixpile_historydata
 * @Author: jeecg-boot
 * @Date: 2021-10-20
 * @Version: V1.0
 */
public interface IDeviceMixpileHistorydataService extends IService<DeviceMixpileHistorydata> {

    List<DeviceMixpileHistorydata> selectLists(String shebeilist, Integer curid);

    List<DeviceMixpileHistorydata> selectlist(String shebeino, String pileno, String datatime, String starttime);

    DeviceMixpileHistorydata selectone(String shebeino, String datatime, String ltd, String lgd, String pileno);
}
