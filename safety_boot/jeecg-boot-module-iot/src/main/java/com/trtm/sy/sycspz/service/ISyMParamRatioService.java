package com.trtm.sy.sycspz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.sy.sycspz.entity.SyMParamRatio;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: sy_m_param_ratio
 * @Author: jeecg-boot
 * @Date:   2023-12-07
 * @Version: V1.0
 */
public interface ISyMParamRatioService extends IService<SyMParamRatio> {

    IPage<Map<String, Object>> mparamratioList(Integer offset, Integer limit, String gatherUse, String mixType);
}
