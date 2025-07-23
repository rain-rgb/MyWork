package com.trtm.iot.tsyjzbStatistics.service.impl;

import com.trtm.iot.tsyjzbStatistics.entity.TSyjzbStatistics;
import com.trtm.iot.tsyjzbStatistics.mapper.TSyjzbStatisticsMapper;
import com.trtm.iot.tsyjzbStatistics.service.ITSyjzbStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

/**
 * @Description: t_syjzb_statistics
 * @Author: jeecg-boot
 * @Date:   2022-06-08
 * @Version: V1.0
 */
@Service
public class TSyjzbStatisticsServiceImpl extends ServiceImpl<TSyjzbStatisticsMapper, TSyjzbStatistics> implements ITSyjzbStatisticsService {
    @Autowired
    private TSyjzbStatisticsMapper syjzbStatisticsMapper;

    @Override
    public TSyjzbStatistics selectLimit(String sbbh, String sylx, String wtbh, String sjqd, Date stringnyr) {
        return syjzbStatisticsMapper.selectLimit(sbbh, sylx, wtbh, sjqd, stringnyr);
    }

    @Override
    public boolean updatestatisticsone(int id, Integer allsums, Integer allOversums) {
        return syjzbStatisticsMapper.updatestatisticsone(id,allsums,allOversums);
    }
}
