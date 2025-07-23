package com.trtm.iot.gongyistatistic.service;

import com.trtm.iot.gongyistatistic.entity.GongyiStatistic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: gongyi_statistic
 * @Author: jeecg-boot
 * @Date:   2023-03-06
 * @Version: V1.0
 */
public interface IGongyiStatisticService extends IService<GongyiStatistic> {

    List<GongyiStatistic> selectCountSum(Integer prontzhi,String prontTime);

    List<GongyiStatistic> selectByOrgcodeName(Integer prontzhi,String orgcodeName, String prontTime);

    List<GongyiStatistic> selectCountSums(String sysOrgCode, String prontTime);

    List<GongyiStatistic> selectByOrgcodeNames(Integer prontzhi,String sysOrgCode, String prontTime);

    List<GongyiStatistic> selectCountSumss(String sysOrgCode);

    GongyiStatistic selectCountSumsss(String sysOrgCode,String prontTime);
}
