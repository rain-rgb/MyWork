package com.trtm.iot.switchmachineStatistics.service;

import com.trtm.iot.switchmachineStatistics.entity.SwitchingmachineStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 拌合站设备开关机统计表
 * @Author: jeecg-boot
 * @Date:   2022-08-22
 * @Version: V1.0
 */
public interface ISwitchingmachineStatisticsService extends IService<SwitchingmachineStatistics> {

    SwitchingmachineStatistics getones(String shebeibianhao);

    List<SwitchingmachineStatistics> getlists();
}
