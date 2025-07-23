package com.trtm.iot.hc_section.service.impl;

import com.trtm.iot.hc_constructionresults.mapper.HcConstructionresultsMapper;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.mapper.HcSectionMapper;
import com.trtm.iot.hc_section.service.IHcSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 华测获取标段
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Service
public class HcSectionServiceImpl extends ServiceImpl<HcSectionMapper, HcSection> implements IHcSectionService {

    @Autowired
    HcSectionMapper hcSectionMapper;
    @Override
    public String listByOrgCode(String code) {
        return hcSectionMapper.listByOrgCode( code);
    }
}
