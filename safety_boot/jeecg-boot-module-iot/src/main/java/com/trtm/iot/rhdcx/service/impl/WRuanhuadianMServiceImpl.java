package com.trtm.iot.rhdcx.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.rhdcx.entity.WRuanhuadianM;
import com.trtm.iot.rhdcx.mapper.WRuanhuadianMMapper;
import com.trtm.iot.rhdcx.service.IWRuanhuadianMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: w_ruanhuadian_m
 * @Author: jeecg-boot
 * @Date:   2021-04-26
 * @Version: V1.0
 */
@Service
public class WRuanhuadianMServiceImpl extends ServiceImpl<WRuanhuadianMMapper, WRuanhuadianM> implements IWRuanhuadianMService {
    @Autowired
    private WRuanhuadianMMapper wRuanhuadianMMapper;
    @Override
    public List<WRuanhuadianM> getListjt(String shebeilist, Integer curid) {
        return wRuanhuadianMMapper.getListjt(shebeilist, curid);
    }
}
