package com.trtm.iot.wzgbStatistics.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.wzgbStatistics.entity.WzgbStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 原材料过磅进出场数据统计表
 * @Author: jeecg-boot
 * @Date:   2022-09-19
 * @Version: V1.0
 */
public interface WzgbStatisticsMapper extends BaseMapper<WzgbStatistics> {

    WzgbStatistics selectonesData(String gongyingshang, String format, String shebeino, String cailiao);
}
