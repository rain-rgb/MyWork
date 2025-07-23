package com.trtm.iot.rebarFactory.service.impl;

import com.trtm.iot.rebarFactory.entity.RebarFactory;
import com.trtm.iot.rebarFactory.mapper.RebarFactoryMapper;
import com.trtm.iot.rebarFactory.service.IRebarFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: rebar_factory
 * @Author: jeecg-boot
 * @Date:   2024-11-14
 * @Version: V1.0
 */
@Service
public class RebarFactoryServiceImpl extends ServiceImpl<RebarFactoryMapper, RebarFactory> implements IRebarFactoryService {

    @Autowired
    private RebarFactoryMapper rebarFactoryMapper;
    @Override
    public String getFactoryName(String factoryId) {
        String factoryName = rebarFactoryMapper.getFactoryName(factoryId);
        return factoryName;
    }
}
