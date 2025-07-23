package com.trtm.sy.sycspz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sycspz.entity.SyMParamRatioAggregate;
import com.trtm.sy.sycspz.mapper.SyMParamRatioAggregateMapper;
import com.trtm.sy.sycspz.service.ISyMParamRatioAggregateService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: sy_m_param_ratio_aggregate
 * @Author: jeecg-boot
 * @Date:   2023-12-07
 * @Version: V1.0
 */
@Service
public class SyMParamRatioAggregateServiceImpl extends ServiceImpl<SyMParamRatioAggregateMapper, SyMParamRatioAggregate> implements ISyMParamRatioAggregateService {

    @Override
    public IPage<Map<String, Object>> mparamratioaggregeteList(Integer offset, Integer limit, String gatherUse, String mixType) {
        Page Page = new Page(offset, limit);
        IPage<Map<String, Object>> result = this.baseMapper.mparamratioaggregeteList(Page, gatherUse, mixType);
        return result;
    }
}
