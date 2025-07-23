package com.trtm.iot.bhzyclcalculate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.TbhzcailiaoStatistics.service.IBhzCementDetailStatisticsService;
import com.trtm.iot.bhzyclcalculate.entity.BhzYclcalculate;
import com.trtm.iot.bhzyclcalculate.mapper.BhzYclcalculateMapper;
import com.trtm.iot.bhzyclcalculate.service.IBhzYclcalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: bhz_yclcalculate
 * @Author: jeecg-boot
 * @Date: 2023-10-24
 * @Version: V1.0
 */
@Service
public class BhzYclcalculateServiceImpl extends ServiceImpl<BhzYclcalculateMapper, BhzYclcalculate> implements IBhzYclcalculateService {

    @Autowired
    private BhzYclcalculateMapper bhzYclcalculateMapper;
    @Autowired
    private IBhzCementDetailStatisticsService bhzCementDetailStatisticsService;

    @Override
    public List<Map<String, Object>> getTJInfoBySbjno(String shebeiNo, String statisticsTime_begin, String statisticsTime_end) {
        return bhzYclcalculateMapper.getTJInfoBySbjno(shebeiNo, statisticsTime_begin, statisticsTime_end);
    }

}
