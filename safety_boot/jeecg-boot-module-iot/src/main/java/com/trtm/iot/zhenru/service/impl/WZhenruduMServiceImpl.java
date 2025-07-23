package com.trtm.iot.zhenru.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.zhenru.entity.WZhenruduM;
import com.trtm.iot.zhenru.mapper.WZhenruduMMapper;
import com.trtm.iot.zhenru.service.IWZhenruduMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: w_zhenrudu_m
 * @Author: jeecg-boot
 * @Date:   2021-04-26
 * @Version: V1.0
 */
@Service
public class WZhenruduMServiceImpl extends ServiceImpl<WZhenruduMMapper, WZhenruduM> implements IWZhenruduMService {
    @Autowired
    private WZhenruduMMapper wZhenruduMMapper;

    @Override
    public List<WZhenruduM> getListjt(String shebeilist, Integer curid) {
        return wZhenruduMMapper.getListjt(shebeilist, curid);
    }
}
