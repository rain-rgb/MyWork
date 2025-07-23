package com.trtm.iot.lqbhzcailiaoStatistics.service.impl;

import com.trtm.iot.lqbhzcailiaoStatistics.entity.BhzLqCailiaoStatistics;
import com.trtm.iot.lqbhzcailiaoStatistics.mapper.BhzLqCailiaoStatisticsMapper;
import com.trtm.iot.lqbhzcailiaoStatistics.service.IBhzLqCailiaoStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: bhz_lq_cailiao_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Service
public class BhzLqCailiaoStatisticsServiceImpl extends ServiceImpl<BhzLqCailiaoStatisticsMapper, BhzLqCailiaoStatistics> implements IBhzLqCailiaoStatisticsService {
@Autowired
private BhzLqCailiaoStatisticsMapper bhzLqCailiaoStatisticsMapper;
    @Override
    public BhzLqCailiaoStatistics selectId(int cs_id, String cailiaoid) {
        return bhzLqCailiaoStatisticsMapper.selectId(cs_id,cailiaoid);
    }

    @Override
    public void AddPrimary_numberOne(int id) {
        bhzLqCailiaoStatisticsMapper.AddPrimary_numberOne(id);
    }

    @Override
    public void AddMiddle_numberOne(int id) {
        bhzLqCailiaoStatisticsMapper.AddMiddle_numberOne(id);
    }

    @Override
    public void AddAdvanced_numberOne(int id) {
        bhzLqCailiaoStatisticsMapper.AddAdvanced_numberOne(id);
    }

//    @Override
//    public void updateRealityById(float reality_number, int id) {
//        bhzLqCailiaoStatisticsMapper.updateRealityById(reality_number,id);
//    }

}
