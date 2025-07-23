package com.trtm.iot.yajiangssrealdata.service.impl;

import com.trtm.iot.yajiangssrealdata.entity.TrYajiangSSRealdata;
import com.trtm.iot.yajiangssrealdata.mapper.TrYajiangSSRealdataMapper;
import com.trtm.iot.yajiangssrealdata.service.ITrYajiangSSRealdataService;
import com.trtm.iot.yj.mapper.TrYajiangMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: tr_yajiang_s_s_realdata
 * @Author: jeecg-boot
 * @Date:   2023-05-11
 * @Version: V1.0
 */
@Service
public class TrYajiangSSRealdataServiceImpl extends ServiceImpl<TrYajiangSSRealdataMapper, TrYajiangSSRealdata> implements ITrYajiangSSRealdataService {

    @Autowired
    private TrYajiangSSRealdataMapper realdataMapper;

    @Override
    public List<String> getSbList() {
        return realdataMapper.getSbList();
    }
}
