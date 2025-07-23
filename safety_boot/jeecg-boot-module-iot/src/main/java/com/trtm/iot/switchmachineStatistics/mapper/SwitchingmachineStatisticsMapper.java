package com.trtm.iot.switchmachineStatistics.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.switchmachineStatistics.entity.SwitchingmachineStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 拌合站设备开关机统计表
 * @Author: jeecg-boot
 * @Date:   2022-08-22
 * @Version: V1.0
 */
public interface SwitchingmachineStatisticsMapper extends BaseMapper<SwitchingmachineStatistics> {

    SwitchingmachineStatistics getones(String shebeibianhao);

    List<SwitchingmachineStatistics> getlists();
}
