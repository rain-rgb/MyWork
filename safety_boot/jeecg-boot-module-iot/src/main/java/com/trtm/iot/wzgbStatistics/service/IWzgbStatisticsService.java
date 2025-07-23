package com.trtm.iot.wzgbStatistics.service;

import com.trtm.iot.wzgbStatistics.entity.WzgbStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 原材料过磅进出场数据统计表
 * @Author: jeecg-boot
 * @Date:   2022-09-19
 * @Version: V1.0
 */
public interface IWzgbStatisticsService extends IService<WzgbStatistics> {

    WzgbStatistics selectonesData(String gongyingshang, String format, String shebeino, String cailiao);
}
