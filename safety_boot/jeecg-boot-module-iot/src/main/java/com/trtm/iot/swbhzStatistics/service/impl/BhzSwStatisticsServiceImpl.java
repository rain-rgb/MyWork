package com.trtm.iot.swbhzStatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.swbhzStatistics.entity.BhzSwStatistics;
import com.trtm.iot.swbhzStatistics.mapper.BhzSwStatisticsMapper;
import com.trtm.iot.swbhzStatistics.service.IBhzSwStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

/**
 * @Description: bhz_sw_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Service
public class BhzSwStatisticsServiceImpl extends ServiceImpl<BhzSwStatisticsMapper, BhzSwStatistics> implements IBhzSwStatisticsService {

    @Autowired
    private BhzSwStatisticsMapper bhzSwStatisticsMapper;
    @Override
    public BhzSwStatistics selectone(Date date, String shebeibianhao) {
        try {
            QueryWrapper<BhzSwStatistics> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("statistics_time",date)
                    .eq("shebeibianhao",shebeibianhao);
            return  this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BhzSwStatistics selectbyStatistic(Date format, String shebeibianhao, String projectName, String jobLocation, String formulaNo, String strengthRank, String poureLocation) {
       return  bhzSwStatisticsMapper.selectbyStatistic(format,shebeibianhao,projectName,jobLocation,formulaNo,strengthRank,poureLocation);
    }
}
