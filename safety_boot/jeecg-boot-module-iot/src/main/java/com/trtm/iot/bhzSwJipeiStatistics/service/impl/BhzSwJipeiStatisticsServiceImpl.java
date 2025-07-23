package com.trtm.iot.bhzSwJipeiStatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzSwJipeiStatistics.entity.BhzSwJipeiStatistics;
import com.trtm.iot.bhzSwJipeiStatistics.mapper.BhzSwJipeiStatisticsMapper;
import com.trtm.iot.bhzSwJipeiStatistics.service.IBhzSwJipeiStatisticsService;
import com.trtm.iot.bhzlqjipeistatistics.entity.BhzLqJipeiStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: bhz_sw_jipei_statistics
 * @Author: jeecg-boot
 * @Date:   2023-06-14
 * @Version: V1.0
 */
@Service
public class BhzSwJipeiStatisticsServiceImpl extends ServiceImpl<BhzSwJipeiStatisticsMapper, BhzSwJipeiStatistics> implements IBhzSwJipeiStatisticsService {
    @Autowired
    private BhzSwJipeiStatisticsMapper bhzSwJipeiStatisticsMapper;

    @Override
    public BhzSwJipeiStatistics selectone(String baseGuid, String shebeibianhao, String shaikong) {
        try {
            QueryWrapper<BhzSwJipeiStatistics> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("baseid",baseGuid);
            queryWrapper.eq("sbjno",shebeibianhao);
            queryWrapper.eq("shaikong",shaikong);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<BhzSwJipeiStatistics> selectList1(String shebeilist, Integer curid) {
        return bhzSwJipeiStatisticsMapper.selectList1(shebeilist, curid);
    }

    @Override
    public List<BhzSwJipeiStatistics> getList1(String baseid1) {
        return bhzSwJipeiStatisticsMapper.getList1(baseid1);
    }
}
