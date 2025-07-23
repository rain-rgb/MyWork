package com.trtm.iot.trzhanglastatistics.service.impl;

import com.trtm.iot.trzhanglastatistics.entity.TrZhanglaStatistics;
import com.trtm.iot.trzhanglastatistics.mapper.TrZhanglaStatisticsMapper;
import com.trtm.iot.trzhanglastatistics.service.ITrZhanglaStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

/**
 * @Description: 张拉统计表
 * @Author: jeecg-boot
 * @Date:   2022-06-08
 * @Version: V1.0
 */
@Service
public class TrZhanglaStatisticsServiceImpl extends ServiceImpl<TrZhanglaStatisticsMapper, TrZhanglaStatistics> implements ITrZhanglaStatisticsService {

    @Autowired
    TrZhanglaStatisticsMapper trZhanglaStatisticsMapper;

    @Override
    public TrZhanglaStatistics selectlimit( Date datanyr1, String gcmc, String yzlc, String gjbh, String shebeibianhao, String gjmc, String snsjqd,String infoSbjno) {
        return trZhanglaStatisticsMapper.selectlimit(datanyr1,gcmc,yzlc,gjbh,shebeibianhao,gjmc,snsjqd,infoSbjno);
    }

    @Override
    public void insert(TrZhanglaStatistics statistics) {
        trZhanglaStatisticsMapper.insert(statistics);
    }
}
