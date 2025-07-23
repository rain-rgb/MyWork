package com.trtm.iot.mxesy.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.mxesy.entity.WendingD;
import com.trtm.iot.mxesy.mapper.WendingDMapper;
import com.trtm.iot.mxesy.service.IWendingDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: w_wendingdu_m
 * @Author: jeecg-boot
 * @Date: 2021-04-28
 * @Version: V1.0
 */
@Service
public class WendingDServiceImpl extends ServiceImpl<WendingDMapper, WendingD> implements IWendingDService {
    @Autowired
    private WendingDMapper wendingDMapper;

    @Override
    public List<WendingD> getListjt(String shebeilist, Integer curid) {
        return wendingDMapper.getListjt(shebeilist, curid);
    }
}
