package com.trtm.iot.devicemixpilerealdata.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicemixpilerealdata.entity.DeviceMixpileRealdata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: device_mixpile_realdata
 * @Author: jeecg-boot
 * @Date:   2021-10-20
 * @Version: V1.0
 */
public interface IDeviceMixpileRealdataService extends IService<DeviceMixpileRealdata> {


    List<Map<String, Object>> countOnline(String sysOrgCode);

    DeviceMixpileRealdata queryone(String shebeiNo);
    List<DeviceMixpileRealdata> queryones(String shebeiNo);

    List<DeviceMixpileRealdata> listByshebei(String shebeilist);
}
