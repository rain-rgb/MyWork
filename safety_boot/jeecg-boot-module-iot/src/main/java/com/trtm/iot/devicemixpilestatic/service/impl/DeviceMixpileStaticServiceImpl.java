package com.trtm.iot.devicemixpilestatic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicemixpilestatic.entity.DeviceMixpileStatic;
import com.trtm.iot.devicemixpilestatic.mapper.DeviceMixpileStaticMapper;
import com.trtm.iot.devicemixpilestatic.service.IDeviceMixpileStaticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: device_mixpile_static
 * @Author: jeecg-boot
 * @Date:   2022-01-24
 * @Version: V1.0
 */
@Service
public class DeviceMixpileStaticServiceImpl extends ServiceImpl<DeviceMixpileStaticMapper, DeviceMixpileStatic> implements IDeviceMixpileStaticService {

    @Autowired DeviceMixpileStaticMapper deviceMixpileStaticMapper;
    @Override
    public DeviceMixpileStatic selectones(String datanyr1, String shebeino, String pileMileage) {
        return deviceMixpileStaticMapper.selectOnes(datanyr1,shebeino,pileMileage);
    }

    @Override
    public DeviceMixpileStatic selectone(String datanyr1, String shebeino) {
        try {
           QueryWrapper<DeviceMixpileStatic> queryWrapper = new QueryWrapper<>();
           queryWrapper.eq("stadate",datanyr1);
           queryWrapper.eq("shebeino",shebeino);
           return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DeviceMixpileStatic> selectbyshebeilist(String sbjno) {
        QueryWrapper<DeviceMixpileStatic> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("stadate,sum(pilecount) pilecount,sum(worklength) worklength,sum(allash) allash,sum(allbeton) allbeton");
        queryWrapper.eq("shebeino",sbjno);
        queryWrapper.groupBy("stadate");
        return deviceMixpileStaticMapper.selectList(queryWrapper);
    }

    @Override
    public DeviceMixpileStatic selecshebeilist(String sbjno) {
        QueryWrapper<DeviceMixpileStatic> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(pilecount) pilecount,sum(worklength) worklength,sum(allash) allash,sum(allbeton) allbeton");
        queryWrapper.eq("shebeino",sbjno);
        queryWrapper.groupBy("shebeino");
        return deviceMixpileStaticMapper.selectOne(queryWrapper);
    }
}
