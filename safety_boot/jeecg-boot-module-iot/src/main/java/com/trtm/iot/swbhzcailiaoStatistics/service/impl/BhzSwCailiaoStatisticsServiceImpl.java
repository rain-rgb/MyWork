package com.trtm.iot.swbhzcailiaoStatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.swbhz.mapper.BhzSwCailiaoMapper;
import com.trtm.iot.swbhzcailiaoStatistics.entity.BhzSwCailiaoStatistics;
import com.trtm.iot.swbhzcailiaoStatistics.mapper.BhzSwCailiaoStatisticsMapper;
import com.trtm.iot.swbhzcailiaoStatistics.service.IBhzSwCailiaoStatisticsService;
import com.xkcoding.http.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: bhz_sw_cailiao_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Service
public class BhzSwCailiaoStatisticsServiceImpl extends ServiceImpl<BhzSwCailiaoStatisticsMapper, BhzSwCailiaoStatistics> implements IBhzSwCailiaoStatisticsService {

    @Autowired
    private BhzSwCailiaoStatisticsMapper bhzSwCailiaoStatisticsMapper;
    @Override
    public BhzSwCailiaoStatistics selectone(int cs_id, String cailiaoid) {
        try {
            QueryWrapper<BhzSwCailiaoStatistics> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("cs_id",cs_id);
            if (StringUtil.isNotEmpty(cailiaoid)){
                queryWrapper.eq("cailiaoid",cailiaoid);
            }
            queryWrapper.last("limit 1");
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int AddReality_numberOne(Integer id, Float reality_number) {
        return bhzSwCailiaoStatisticsMapper.AddReality_numberOne(id,reality_number);
    }

    @Override
    public int AddTheory_numberOne(Integer id, Float theory_number) {
        return bhzSwCailiaoStatisticsMapper.AddTheory_numberOne(id,theory_number);
    }

    @Override
    public int AddPrimary_numberOne(Integer id) {
        return bhzSwCailiaoStatisticsMapper.AddPrimary_numberOne(id);
    }

    @Override
    public int AddMiddle_numberOne(Integer id) {
        return bhzSwCailiaoStatisticsMapper.AddMiddle_numberOne(id);
    }

    @Override
    public int AddAdvanced_numberOne(Integer id) {
        return bhzSwCailiaoStatisticsMapper.AddAdvanced_numberOne(id);
    }
}
