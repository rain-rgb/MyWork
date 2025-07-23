package com.trtm.sy.sycspz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sycspz.entity.SyMParamRatioAggregate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * @Description: sy_m_param_ratio_aggregate
 * @Author: jeecg-boot
 * @Date:   2023-12-07
 * @Version: V1.0
 */
public interface SyMParamRatioAggregateMapper extends BaseMapper<SyMParamRatioAggregate> {

    IPage<Map<String, Object>> mparamratioaggregeteList(Page page, String gatherUse, String mixType);
}
