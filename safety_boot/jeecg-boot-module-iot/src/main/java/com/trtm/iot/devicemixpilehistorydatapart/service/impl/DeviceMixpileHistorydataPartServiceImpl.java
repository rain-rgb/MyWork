package com.trtm.iot.devicemixpilehistorydatapart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicemixpilehistorydatapart.entity.DeviceMixpileHistorydataPart;
import com.trtm.iot.devicemixpilehistorydatapart.mapper.DeviceMixpileHistorydataPartMapper;
import com.trtm.iot.devicemixpilehistorydatapart.service.IDeviceMixpileHistorydataPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 水泥搅拌桩成桩记录每段数据信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-19
 * @Version: V1.0
 */
@Service
public class DeviceMixpileHistorydataPartServiceImpl extends ServiceImpl<DeviceMixpileHistorydataPartMapper, DeviceMixpileHistorydataPart> implements IDeviceMixpileHistorydataPartService {

    @Autowired
    DeviceMixpileHistorydataPartMapper deviceMixpileHistorydataPartMapper;

    @Override
    public double selecmaxpile(String shebeino, String pileno, String pileMileage) {
        QueryWrapper<DeviceMixpileHistorydataPart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shebeino",shebeino);
        queryWrapper.eq("pile_no",pileno);
        queryWrapper.eq("pile_mileage",pileMileage);
        List<DeviceMixpileHistorydataPart> deviceMixpileHistorydataParts = deviceMixpileHistorydataPartMapper.selectList(queryWrapper);
        if (deviceMixpileHistorydataParts.size() > 0){
            double i = 0.0;
            for (DeviceMixpileHistorydataPart deviceMixpileHistorydataPart :deviceMixpileHistorydataParts){
                String partDep = deviceMixpileHistorydataPart.getPartDep();
                double v = Double.parseDouble(partDep);
                if (v > i){
                    i = v;
                }
            }
            return i;
        }else {
            return 0.0;
        }
    }

    @Override
    public double selecmaxpiles(String shebeino, String pileno, String date) {
        QueryWrapper<DeviceMixpileHistorydataPart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shebeino",shebeino);
        queryWrapper.eq("pile_no",pileno);
        queryWrapper.likeRight("part_endtime",date);
        List<DeviceMixpileHistorydataPart> deviceMixpileHistorydataParts = deviceMixpileHistorydataPartMapper.selectList(queryWrapper);
        if (deviceMixpileHistorydataParts.size() > 0){
            double i = 0.0;
            for (DeviceMixpileHistorydataPart deviceMixpileHistorydataPart :deviceMixpileHistorydataParts){
                String partDep = deviceMixpileHistorydataPart.getPartDep();
                double v = Double.parseDouble(partDep);
                if (v > i){
                    i = v;
                }
            }
            return i;
        }else {
            return 0.0;
        }
    }
}
