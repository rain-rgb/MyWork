package com.trtm.iot.bhzlqjipeistatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzlqjipeistatistics.entity.BhzLqJipeiStatistics;
import com.trtm.iot.bhzlqjipeistatistics.mapper.BhzLqJipeiStatisticsMapper;
import com.trtm.iot.bhzlqjipeistatistics.service.IBhzLqJipeiStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 沥青级配统计信息表
 * @Author: jeecg-boot
 * @Date:   2022-05-16
 * @Version: V1.0
 */
@Service
public class BhzLqJipeiStatisticsServiceImpl extends ServiceImpl<BhzLqJipeiStatisticsMapper, BhzLqJipeiStatistics> implements IBhzLqJipeiStatisticsService {
    @Autowired
    private BhzLqJipeiStatisticsMapper bhzLqJipeiStatisticsMapper;
    @Override
    public BhzLqJipeiStatistics selectone(String baseGuid, String shebeibianhao, String shaikong) {
        try {
            QueryWrapper<BhzLqJipeiStatistics> queryWrapper = new QueryWrapper<>();
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
    public List<BhzLqJipeiStatistics> getList1(String baseid) {
        return bhzLqJipeiStatisticsMapper.getList1(baseid);
    }

    @Override
    public List<BhzLqJipeiStatistics> selectList(String shebeino, Integer id) {
        return bhzLqJipeiStatisticsMapper.selectList(shebeino, id);
    }
}
