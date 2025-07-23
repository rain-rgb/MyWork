package com.trtm.iot.hc_tfstationdetail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_tfstationdetail.entity.HcTfstationdetail;
import com.trtm.iot.hc_tfstationdetail.mapper.HcTfstationdetailMapper;
import com.trtm.iot.hc_tfstationdetail.service.IHcTfstationdetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 土方工作区逐桩详情
 * @Author: jeecg-boot
 * @Date:   2023-10-10
 * @Version: V1.0
 */
@Service
public class HcTfstationdetailServiceImpl extends ServiceImpl<HcTfstationdetailMapper, HcTfstationdetail> implements IHcTfstationdetailService {

    @Autowired
    HcTfstationdetailMapper hcTfstationdetailMapper;

    @Override
    public List<HcTfstationdetail> selectlistbybaserid(String blockid) {
        QueryWrapper<HcTfstationdetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blockid",blockid);
        return this.list(queryWrapper);
    }
}
