package com.trtm.iot.gongyistatistic.service.impl;

import com.trtm.iot.gongyistatistic.entity.GongyiStatistic;
import com.trtm.iot.gongyistatistic.mapper.GongyiStatisticMapper;
import com.trtm.iot.gongyistatistic.service.IGongyiStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: gongyi_statistic
 * @Author: jeecg-boot
 * @Date:   2023-03-06
 * @Version: V1.0
 */
@Service
public class GongyiStatisticServiceImpl extends ServiceImpl<GongyiStatisticMapper, GongyiStatistic> implements IGongyiStatisticService {

    @Autowired
    GongyiStatisticMapper gongyiStatisticMapper;

    @Override
    public List<GongyiStatistic> selectCountSum(Integer prontzhi,String prontTime) {
        String prontTime1 = prontTime + "%";
        return gongyiStatisticMapper.selectCountSum(prontzhi,prontTime1);
    }

    @Override
    public List<GongyiStatistic> selectByOrgcodeName(Integer prontzhi,String orgcodeName, String prontTime) {
        String prontTime1 = prontTime + "%";
        return gongyiStatisticMapper.selectByOrgcodeName(prontzhi,orgcodeName,prontTime1);
    }

    @Override
    public List<GongyiStatistic> selectCountSums(String sysOrgCode, String prontTime) {
        String prontTime1 = prontTime + "%";
        String sysOrgCode1 = sysOrgCode + "%";
        return gongyiStatisticMapper.selectCountSums(sysOrgCode1,prontTime1);
    }

    @Override
    public List<GongyiStatistic> selectByOrgcodeNames(Integer prontzhi,String sysOrgCode, String prontTime) {
        String prontTime1 = prontTime + "%";
        String sysOrgCode1 = sysOrgCode + "%";
        return gongyiStatisticMapper.selectByOrgcodeNames(prontzhi,sysOrgCode1,prontTime1);
    }

    @Override
    public List<GongyiStatistic> selectCountSumss(String sysOrgCode) {
        return gongyiStatisticMapper.selectCountSumss(sysOrgCode);
    }

    @Override
    public GongyiStatistic selectCountSumsss(String sysOrgCode,String prontTime) {
        return gongyiStatisticMapper.selectCountSumsss(sysOrgCode+"%",prontTime+"%");
    }
}
