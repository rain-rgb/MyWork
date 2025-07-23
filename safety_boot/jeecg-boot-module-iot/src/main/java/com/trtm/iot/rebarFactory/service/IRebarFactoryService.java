package com.trtm.iot.rebarFactory.service;

import com.trtm.iot.rebarFactory.entity.RebarFactory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: rebar_factory
 * @Author: jeecg-boot
 * @Date:   2024-11-14
 * @Version: V1.0
 */
public interface IRebarFactoryService extends IService<RebarFactory> {

    String getFactoryName(String factoryId);
}
