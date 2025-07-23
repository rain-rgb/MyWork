package com.trtm.iot.devicetunnelenvironmentdata.service.impl;

import com.trtm.iot.devicetunnelenvironmentdata.entity.DeviceTunnelEnvironmentdata;
import com.trtm.iot.devicetunnelenvironmentdata.mapper.DeviceTunnelEnvironmentdataMapper;
import com.trtm.iot.devicetunnelenvironmentdata.service.IDeviceTunnelEnvironmentdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 隧道环境（有害气体）监测主表
 * @Author: jeecg-boot
 * @Date: 2021-09-01
 * @Version: V1.0
 */
@Service
public class DeviceTunnelEnvironmentdataServiceImpl extends ServiceImpl<DeviceTunnelEnvironmentdataMapper, DeviceTunnelEnvironmentdata> implements IDeviceTunnelEnvironmentdataService {
    @Autowired
    private DeviceTunnelEnvironmentdataMapper deviceTunnelEnvironmentdataMapper;

    @Override
    public List<DeviceTunnelEnvironmentdata> selectLists(String shebeilist, Integer curid) {
        return deviceTunnelEnvironmentdataMapper.selectLists(shebeilist, curid);
    }
}
