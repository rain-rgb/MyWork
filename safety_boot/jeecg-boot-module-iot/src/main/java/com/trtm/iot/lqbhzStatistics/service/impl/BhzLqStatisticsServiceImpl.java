package com.trtm.iot.lqbhzStatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.lqbhzStatistics.entity.BhzLqStatistics;
import com.trtm.iot.lqbhzStatistics.mapper.BhzLqStatisticsMapper;
import com.trtm.iot.lqbhzStatistics.service.IBhzLqStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @Description: bhz_lq_statistics
 * @Author: jeecg-boot
 * @Date: 2021-03-30
 * @Version: V1.0
 */
@Service
public class BhzLqStatisticsServiceImpl extends ServiceImpl<BhzLqStatisticsMapper, BhzLqStatistics> implements IBhzLqStatisticsService {

    @Autowired
    private BhzLqStatisticsMapper bhzLqStatisticsMapper;
    @Override
    public BhzLqStatistics selectlimit(Date date) {
        try {
            QueryWrapper<BhzLqStatistics> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("statistics_time", date);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BhzLqStatistics selectlimitone(String shebeibianhao, String formulaNo, Date date,String strengthRank,String projectName,String jobLocation,String poureLocation) {

        return bhzLqStatisticsMapper.selectlimitone(shebeibianhao, formulaNo, date,strengthRank,projectName,jobLocation,poureLocation);
    }


    /**
     * 查询指定的数据
     *
     * @return
     */
    @Override
    public List<BhzLqStatistics> seleceZong() {
        try {
            QueryWrapper<BhzLqStatistics> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("all_dish", "statistics_time");
            queryWrapper.orderByAsc("statistics_time");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BhzLqStatistics selectbyStatistic(Date format, String shebeibianhao, String projectName, String jobLocation, String formulaNo, String strengthRank, String poureLocation) {
        return bhzLqStatisticsMapper.selectbyStatistic(format,shebeibianhao,projectName,jobLocation,formulaNo,strengthRank,poureLocation);
    }
}
