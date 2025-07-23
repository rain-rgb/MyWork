package com.trtm.iot.bhzyuanliaoStatistic.service.impl;

import com.trtm.iot.bhzyuanliaoStatistic.entity.BhzYuanliaoStatistic;
import com.trtm.iot.bhzyuanliaoStatistic.mapper.BhzYuanliaoStatisticMapper;
import com.trtm.iot.bhzyuanliaoStatistic.service.IBhzYuanliaoStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 拌合站原材料统计
 * @Author: jeecg-boot
 * @Date:   2022-10-10
 * @Version: V1.0
 */
@Service
public class BhzYuanliaoStatisticServiceImpl extends ServiceImpl<BhzYuanliaoStatisticMapper, BhzYuanliaoStatistic> implements IBhzYuanliaoStatisticService {

    @Autowired
    BhzYuanliaoStatisticMapper bhzYuanliaoStatisticMapper;

    @Override
    public BhzYuanliaoStatistic selectByShebei(String shebeiNo, String materialeName) {
        return bhzYuanliaoStatisticMapper.selectByShebei(shebeiNo, materialeName);
    }
}
