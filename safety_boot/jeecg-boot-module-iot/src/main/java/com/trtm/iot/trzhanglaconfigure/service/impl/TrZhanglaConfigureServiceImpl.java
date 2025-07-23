package com.trtm.iot.trzhanglaconfigure.service.impl;

import com.trtm.iot.trzhanglaconfigure.entity.TrZhanglaConfigure;
import com.trtm.iot.trzhanglaconfigure.mapper.TrZhanglaConfigureMapper;
import com.trtm.iot.trzhanglaconfigure.service.ITrZhanglaConfigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 张拉预警配置表
 * @Author: jeecg-boot
 * @Date:   2023-02-14
 * @Version: V1.0
 */
@Service
public class TrZhanglaConfigureServiceImpl extends ServiceImpl<TrZhanglaConfigureMapper, TrZhanglaConfigure> implements ITrZhanglaConfigureService {

    @Autowired
    TrZhanglaConfigureMapper zhanglaConfigureMapper;
    @Override
    public TrZhanglaConfigure selectbyshebei(String shebeibianhao) {
        return zhanglaConfigureMapper.selectbyshebei(shebeibianhao);
    }
}
