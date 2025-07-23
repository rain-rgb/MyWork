package com.trtm.sy.sycspz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sycspz.entity.SyMParamRatio;
import com.trtm.sy.sycspz.mapper.SyMParamRatioMapper;
import com.trtm.sy.sycspz.service.ISyMParamRatioService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: sy_m_param_ratio
 * @Author: jeecg-boot
 * @Date:   2023-12-07
 * @Version: V1.0
 */
@Service
public class SyMParamRatioServiceImpl extends ServiceImpl<SyMParamRatioMapper, SyMParamRatio> implements ISyMParamRatioService {

    @Override
    public IPage<Map<String, Object>> mparamratioList(Integer offset, Integer limit, String gatherUse, String mixType) {
        Page Page = new Page(offset, limit);
        IPage<Map<String, Object>> result = this.baseMapper.mparamratioList(Page, gatherUse, mixType);
        return result;
    }
}
