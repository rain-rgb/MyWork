package com.trtm.iot.tryajiangconfigure.service.impl;

import com.trtm.iot.tryajiangconfigure.entity.TrYajiangConfigure;
import com.trtm.iot.tryajiangconfigure.mapper.TrYajiangConfigureMapper;
import com.trtm.iot.tryajiangconfigure.service.ITrYajiangConfigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 压浆配置表
 * @Author: jeecg-boot
 * @Date:   2023-03-28
 * @Version: V1.0
 */
@Service
public class TrYajiangConfigureServiceImpl extends ServiceImpl<TrYajiangConfigureMapper, TrYajiangConfigure> implements ITrYajiangConfigureService {
    @Autowired
    TrYajiangConfigureMapper yajiangConfigureMapper;

    @Override
    public TrYajiangConfigure selectbyshebei(String shebeibianhao) {
        return yajiangConfigureMapper.selectbyshebei(shebeibianhao);
    }
}
