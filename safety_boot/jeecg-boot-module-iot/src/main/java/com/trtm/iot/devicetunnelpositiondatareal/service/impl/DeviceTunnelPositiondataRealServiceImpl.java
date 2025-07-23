package com.trtm.iot.devicetunnelpositiondatareal.service.impl;

import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicetunnelpositiondatareal.entity.DeviceTunnelPositiondataReal;
import com.trtm.iot.devicetunnelpositiondatareal.entity.DeviceTunnelPositiondataRealDto;
import com.trtm.iot.devicetunnelpositiondatareal.mapper.DeviceTunnelPositiondataRealMapper;
import com.trtm.iot.devicetunnelpositiondatareal.service.IDeviceTunnelPositiondataRealService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 人员定位实时表
 * @Author: jeecg-boot
 * @Date:   2021-09-27
 * @Version: V1.0
 */
@Service
public class DeviceTunnelPositiondataRealServiceImpl extends ServiceImpl<DeviceTunnelPositiondataRealMapper, DeviceTunnelPositiondataReal> implements IDeviceTunnelPositiondataRealService {
    @Autowired
    DeviceTunnelPositiondataRealMapper deviceTunnelPositiondataRealMapper;




    @Override
    public List<DeviceTunnelPositiondataRealDto> select(String orgCode, String sbname, String sbtype) {
        return deviceTunnelPositiondataRealMapper.select(orgCode,sbname,sbtype);
    }

    @Override
    public List<DeviceTunnelPositiondataReal> list(QueryWrapper<DeviceTunnelPositiondataRealDto> queryWrapper) {
        return null;
    }

    @Override
    public List<DeviceTunnelPositiondataReal> queryJiXinList(String shebeino) {
        return deviceTunnelPositiondataRealMapper.queryJiXinList(shebeino);
    }

    @Override
    public List<DeviceTunnelPositiondataReal> queryJiXinBanZuList(String shebeino) {
        return deviceTunnelPositiondataRealMapper.queryJiXinBanZuList(shebeino);
    }

    @Override
    public List<DeviceTunnelPositiondataReal> querylistAttendance(String shebeino) {
        return deviceTunnelPositiondataRealMapper.querylistAttendance(shebeino);
    }


}
