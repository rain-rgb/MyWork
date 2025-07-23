package com.trtm.iot.TbhzcailiaoStatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.TbhzcailiaoStatistics.mapper.BhzCementDetailStatisticsMapper;
import com.trtm.iot.TbhzcailiaoStatistics.service.IBhzCementDetailStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: bhz_cement_detail_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Service
public class BhzCementDetailStatisticsServiceImpl extends ServiceImpl<BhzCementDetailStatisticsMapper, BhzCementDetailStatistics> implements IBhzCementDetailStatisticsService {

    @Autowired
    private BhzCementDetailStatisticsMapper bhzCementDetailStatisticsMapper;
    @Override
    public BhzCementDetailStatistics selectone(int cs_id, String cailiaoid) {
        try {
            QueryWrapper<BhzCementDetailStatistics> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("cs_id",cs_id);
            queryWrapper.eq("cailiaoid",cailiaoid);
            queryWrapper.last("limit 1");
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int AddReality_numberOne(Integer id, Float reality_number) {
        return bhzCementDetailStatisticsMapper.AddReality_numberOne(id,reality_number);
    }

    @Override
    public int AddTheory_numberOne(Integer id, Float theory_number) {
        return bhzCementDetailStatisticsMapper.AddTheory_numberOne(id,theory_number);
    }

    @Override
    public int AddPrimary_numberOne(Integer id) {
        return bhzCementDetailStatisticsMapper.AddPrimary_numberOne(id);
    }

    @Override
    public int AddMiddle_numberOne(Integer id) {
        return bhzCementDetailStatisticsMapper.AddMiddle_numberOne(id);
    }

    @Override
    public int AddAdvanced_numberOne(Integer id) {
        return bhzCementDetailStatisticsMapper.AddAdvanced_numberOne(id);
    }

    @Override
    public List<BhzCementDetailStatistics> selectdetaillist(Integer id) {
        try {
            QueryWrapper<BhzCementDetailStatistics> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("cs_id",id);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BhzCementDetailStatistics> selectShebeiDetail(String statisticsTime_begin, String statisticsTime_end, List<String> shebeiNo) {
        return bhzCementDetailStatisticsMapper.selectShebeiDetail(statisticsTime_begin,statisticsTime_end,shebeiNo);
    }

}
