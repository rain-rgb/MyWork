package com.trtm.sy.sycspz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.sy.sycspz.entity.SyMParamRatioAggregate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: sy_m_param_ratio_aggregate
 * @Author: jeecg-boot
 * @Date:   2023-12-07
 * @Version: V1.0
 */
public interface ISyMParamRatioAggregateService extends IService<SyMParamRatioAggregate> {

    IPage<Map<String, Object>> mparamratioaggregeteList(Integer offset, Integer limit, String gatherUse, String mixType);
}
