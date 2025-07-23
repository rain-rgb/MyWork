package com.trtm.iot.hc_pavement_alarm.service;

import com.trtm.iot.hc_machine.entity.HcMachine;
import com.trtm.iot.hc_pavement_alarm.entity.HcPavementAlarm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 摊铺碾压预警
 * @Author: jeecg-boot
 * @Date:   2023-04-26
 * @Version: V1.0
 */
public interface IHcPavementAlarmService extends IService<HcPavementAlarm> {

    HcMachine getHcMachine(String machineid);
}
