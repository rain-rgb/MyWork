package com.trtm.iot.wzcailiaonamedictall.service.impl;

import com.trtm.iot.wzcailiaonamedictall.entity.WzcailiaonamedictAll;
import com.trtm.iot.wzcailiaonamedictall.mapper.WzcailiaonamedictAllMapper;
import com.trtm.iot.wzcailiaonamedictall.service.IWzcailiaonamedictAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: wzcailiaonamedict_all
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
@Service
public class WzcailiaonamedictAllServiceImpl extends ServiceImpl<WzcailiaonamedictAllMapper, WzcailiaonamedictAll> implements IWzcailiaonamedictAllService {

    @Autowired
    WzcailiaonamedictAllMapper wzcailiaonamedictAllMapper;
    @Override
    public WzcailiaonamedictAll getcailiaoInfo(String cailiaonno) {
        return wzcailiaonamedictAllMapper.getcailiaoInfo(cailiaonno);
    }
}
