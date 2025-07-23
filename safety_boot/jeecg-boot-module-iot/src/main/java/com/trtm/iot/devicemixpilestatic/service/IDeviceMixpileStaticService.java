package com.trtm.iot.devicemixpilestatic.service;

import com.trtm.iot.devicemixpilestatic.entity.DeviceMixpileStatic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: device_mixpile_static
 * @Author: jeecg-boot
 * @Date:   2022-01-24
 * @Version: V1.0
 */
public interface IDeviceMixpileStaticService extends IService<DeviceMixpileStatic> {

    DeviceMixpileStatic selectones(String datanyr1, String shebeino, String pileMileage);

    DeviceMixpileStatic selectone(String datanyr1, String shebeino);

    List<DeviceMixpileStatic> selectbyshebeilist(String sbjno);

    DeviceMixpileStatic selecshebeilist(String sbjno);
}
