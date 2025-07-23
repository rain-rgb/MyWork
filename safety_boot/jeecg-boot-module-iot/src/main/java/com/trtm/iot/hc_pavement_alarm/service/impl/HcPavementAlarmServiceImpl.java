package com.trtm.iot.hc_pavement_alarm.service.impl;

import com.trtm.iot.hc_machine.entity.HcMachine;
import com.trtm.iot.hc_pavement_alarm.entity.HcPavementAlarm;
import com.trtm.iot.hc_pavement_alarm.mapper.HcPavementAlarmMapper;
import com.trtm.iot.hc_pavement_alarm.service.IHcPavementAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 摊铺碾压预警
 * @Author: jeecg-boot
 * @Date:   2023-04-26
 * @Version: V1.0
 */
@Service
public class HcPavementAlarmServiceImpl extends ServiceImpl<HcPavementAlarmMapper, HcPavementAlarm> implements IHcPavementAlarmService {

    @Autowired
    private HcPavementAlarmMapper hcPavementAlarmMapper;

    @Override
    public HcMachine getHcMachine(String machineid) {
        return hcPavementAlarmMapper.getHcMachine(machineid);
    }
}
