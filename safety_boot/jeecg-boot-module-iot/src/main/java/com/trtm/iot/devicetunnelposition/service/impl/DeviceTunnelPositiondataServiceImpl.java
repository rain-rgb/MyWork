package com.trtm.iot.devicetunnelposition.service.impl;

import com.trtm.iot.devicetunnelposition.entity.DeviceTunnelPositiondata;
import com.trtm.iot.devicetunnelposition.mapper.DeviceTunnelPositiondataMapper;
import com.trtm.iot.devicetunnelposition.service.IDeviceTunnelPositiondataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 隧道人员定位表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-26
 * @Version: V1.0
 */
@Service
public class DeviceTunnelPositiondataServiceImpl extends ServiceImpl<DeviceTunnelPositiondataMapper, DeviceTunnelPositiondata> implements IDeviceTunnelPositiondataService {

    @Autowired
    private DeviceTunnelPositiondataMapper deviceTunnelPositiondataMapper;

    @Override
    public List<DeviceTunnelPositiondata> queryLists(String jzwz,String she,String dataTime) {
        return deviceTunnelPositiondataMapper.queryLists(jzwz,she,dataTime);
    }
}
