package com.trtm.iot.gualan.service.impl;

import com.trtm.iot.gualan.entity.GualanBase;
import com.trtm.iot.gualan.mapper.GualanBaseMapper;
import com.trtm.iot.gualan.service.IGualanBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 挂篮数据主表
 * @Author: jeecg-boot
 * @Date: 2021-04-19
 * @Version: V1.0
 */
@Service
public class GualanBaseServiceImpl extends ServiceImpl<GualanBaseMapper, GualanBase> implements IGualanBaseService {

    @Autowired
    private GualanBaseMapper gualanBaseMapper;

    @Override
    public List<Map<String, Object>> getYjList() {
        return gualanBaseMapper.getYjList();
    }

    @Override
    public Map<String, Object> getMap2Port(String sb1, String sb2) {
        return gualanBaseMapper.getMap2Port();
    }

    @Override
    public List<GualanBase> selectGuaLanList(String shebeilist, Integer curid) {
        return gualanBaseMapper.selectGuaLanList(shebeilist, curid);
    }
}
