package com.trtm.iot.tryajiangstatistics.service.impl;

import com.trtm.iot.tryajiangstatistics.entity.TrYajiangStatistics;
import com.trtm.iot.tryajiangstatistics.mapper.TrYajiangStatisticsMapper;
import com.trtm.iot.tryajiangstatistics.service.ITrYajiangStatisticsService;
import com.trtm.iot.yajiangm.entity.TrYajiangSM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

/**
 * @Description: 压浆统计表
 * @Author: jeecg-boot
 * @Date:   2022-06-10
 * @Version: V1.0
 */
@Service
public class TrYajiangStatisticsServiceImpl extends ServiceImpl<TrYajiangStatisticsMapper, TrYajiangStatistics> implements ITrYajiangStatisticsService {

    @Autowired
    TrYajiangStatisticsMapper yajiangStatisticsMapper;

    @Override
    public TrYajiangStatistics selectlimit(Date datanyr1, String htbh, String gcmc, String zhbw, String sgbw, String gjjg, String gjbh, String yjsbbh, String lblx, String infoSbjno) {
        return yajiangStatisticsMapper.selectlimit(datanyr1,htbh,gcmc,zhbw,sgbw,gjjg,gjbh,yjsbbh,lblx,infoSbjno);
    }

    @Override
    public void insert(TrYajiangStatistics statistics) {
        yajiangStatisticsMapper.insert(statistics);
    }
}
