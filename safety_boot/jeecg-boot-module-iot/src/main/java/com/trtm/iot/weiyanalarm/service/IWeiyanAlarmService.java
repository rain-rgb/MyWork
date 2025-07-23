package com.trtm.iot.weiyanalarm.service;

import com.trtm.iot.weiyanalarm.entity.WeiyanAlarm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.weiyanalarmhandler.entity.WeiyanAlarmHandler;

/**
 * @Description: 监控量测报警数据表
 * @Author: jeecg-boot
 * @Date:   2022-07-06
 * @Version: V1.0
 */
public interface IWeiyanAlarmService extends IService<WeiyanAlarm> {

    Integer saveMain(WeiyanAlarm weiyanAlarm, WeiyanAlarmHandler weiyanAlarmHandler);
}
