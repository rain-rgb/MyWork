package com.trtm.iot.tadiao.service.impl;

import com.trtm.iot.tadiao.entity.Tadiao;
import com.trtm.iot.tadiao.mapper.TadiaoMapper;
import com.trtm.iot.tadiao.service.ITadiaoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 塔吊
 * @Author: jeecg-boot
 * @Date:   2021-12-03
 * @Version: V1.0
 */
@Service
public class TadiaoServiceImpl extends ServiceImpl<TadiaoMapper, Tadiao> implements ITadiaoService {
    @Override
    public Tadiao find(String devicesn) {
        return null;
    }
}
