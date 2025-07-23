package com.trtm.iot.zhanglassrealdata.service.impl;

import com.trtm.iot.zhanglassrealdata.entity.TrZhanglaSSRealdata;
import com.trtm.iot.zhanglassrealdata.mapper.TrZhanglaSSRealdataMapper;
import com.trtm.iot.zhanglassrealdata.service.ITrZhanglaSSRealdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: tr_zhangla_s_s_realdata
 * @Author: jeecg-boot
 * @Date:   2023-05-12
 * @Version: V1.0
 */
@Service
public class TrZhanglaSSRealdataServiceImpl extends ServiceImpl<TrZhanglaSSRealdataMapper, TrZhanglaSSRealdata> implements ITrZhanglaSSRealdataService {

    @Autowired
    private TrZhanglaSSRealdataMapper zhanglaSSRealdataMapper;

    @Override
    public List<String> getSbList() {
        return zhanglaSSRealdataMapper.getSbList();
    }
}
