package com.trtm.iot.zhydstatistics.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.zhydstatistics.entity.DeviceElectrcStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 智慧用电历史数据统计表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-02
 * @Version: V1.0
 */
public interface DeviceElectrcStatisticsMapper extends BaseMapper<DeviceElectrcStatistics> {

    DeviceElectrcStatistics selectlimit(Date date, String sbno);
}
