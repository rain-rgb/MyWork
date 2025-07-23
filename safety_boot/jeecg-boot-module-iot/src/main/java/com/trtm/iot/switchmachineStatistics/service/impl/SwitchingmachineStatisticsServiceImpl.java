package com.trtm.iot.switchmachineStatistics.service.impl;

import com.trtm.iot.switchmachineStatistics.entity.SwitchingmachineStatistics;
import com.trtm.iot.switchmachineStatistics.mapper.SwitchingmachineStatisticsMapper;
import com.trtm.iot.switchmachineStatistics.service.ISwitchingmachineStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 拌合站设备开关机统计表
 * @Author: jeecg-boot
 * @Date:   2022-08-22
 * @Version: V1.0
 */
@Service
public class SwitchingmachineStatisticsServiceImpl extends ServiceImpl<SwitchingmachineStatisticsMapper, SwitchingmachineStatistics> implements ISwitchingmachineStatisticsService {

    @Autowired SwitchingmachineStatisticsMapper switchingmachineStatisticsMapper;

    @Override
    public SwitchingmachineStatistics getones(String shebeibianhao) {
        return switchingmachineStatisticsMapper.getones(shebeibianhao);
    }

    @Override
    public List<SwitchingmachineStatistics> getlists() {
        return switchingmachineStatisticsMapper.getlists();
    }
}
