package com.trtm.iot.frontDeviceWeilan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.frontDeviceWeilan.entity.FrontDeviceWeilan;
import com.trtm.iot.frontDeviceWeilan.mapper.FrontDeviceWeilanMapper;
import com.trtm.iot.frontDeviceWeilan.service.IFrontDeviceWeilanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 运输车电子围栏数据
 * @Author: jeecg-boot
 * @Date:   2021-06-28
 * @Version: V1.0
 */
@Service
public class FrontDeviceWeilanServiceImpl extends ServiceImpl<FrontDeviceWeilanMapper, FrontDeviceWeilan> implements IFrontDeviceWeilanService {

    @Autowired
    private FrontDeviceWeilanMapper frontDeviceWeilanMapper;
    @Override
    public List<FrontDeviceWeilan> querylist(Integer status,Integer isdel,Integer delstatus) {
//        try {
//            QueryWrapper<FrontDeviceWeilan> queryWrapper=new QueryWrapper<>();
//            queryWrapper.eq("status",status);
//            queryWrapper.eq("isdel",isdel);
//            queryWrapper.eq("delstatus",delstatus);
//            return this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return frontDeviceWeilanMapper.querylist(status,isdel,delstatus);
    }
}
