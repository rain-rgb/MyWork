package com.trtm.iot.wbshebeidetail.service.impl;

import com.trtm.iot.wbshebeidetail.entity.WbshebeiDetail;
import com.trtm.iot.wbshebeidetail.mapper.WbshebeiDetailMapper;
import com.trtm.iot.wbshebeidetail.service.IWbshebeiDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @Description: 电子锁详情数据表
 * @Author: jeecg-boot
 * @Date:   2022-02-22
 * @Version: V1.0
 */
@Service
public class WbshebeiDetailServiceImpl extends ServiceImpl<WbshebeiDetailMapper, WbshebeiDetail> implements IWbshebeiDetailService {

    @Autowired
    private WbshebeiDetailMapper wbshebeiDetailMapper;

    @Override
    public String getDepartName(String userdepartid) {
        return wbshebeiDetailMapper.getDepartName(userdepartid);
    }
}
