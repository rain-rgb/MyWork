package com.trtm.iot.wzgbStatistics.service.impl;

import com.trtm.iot.wzgbStatistics.entity.WzgbStatistics;
import com.trtm.iot.wzgbStatistics.mapper.WzgbStatisticsMapper;
import com.trtm.iot.wzgbStatistics.service.IWzgbStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 原材料过磅进出场数据统计表
 * @Author: jeecg-boot
 * @Date:   2022-09-19
 * @Version: V1.0
 */
@Service
public class WzgbStatisticsServiceImpl extends ServiceImpl<WzgbStatisticsMapper, WzgbStatistics> implements IWzgbStatisticsService {

    @Autowired WzgbStatisticsMapper wzgbStatisticsMapper;
    @Override
    public WzgbStatistics selectonesData(String gongyingshang, String format, String shebeino, String cailiao) {
        return wzgbStatisticsMapper.selectonesData(gongyingshang,format,shebeino,cailiao);
    }
}
