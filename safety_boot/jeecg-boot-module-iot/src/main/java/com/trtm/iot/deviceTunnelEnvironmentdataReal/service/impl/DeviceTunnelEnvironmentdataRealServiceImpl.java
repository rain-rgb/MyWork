package com.trtm.iot.deviceTunnelEnvironmentdataReal.service.impl;

import com.trtm.iot.deviceTunnelEnvironmentdataReal.entity.DeviceTunnelEnvironmentdataReal;
import com.trtm.iot.deviceTunnelEnvironmentdataReal.mapper.DeviceTunnelEnvironmentdataRealMapper;
import com.trtm.iot.deviceTunnelEnvironmentdataReal.service.IDeviceTunnelEnvironmentdataRealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: device_tunnel_environmentdata_real
 * @Author: jeecg-boot
 * @Date:   2022-08-18
 * @Version: V1.0
 */
@Service
public class DeviceTunnelEnvironmentdataRealServiceImpl extends ServiceImpl<DeviceTunnelEnvironmentdataRealMapper, DeviceTunnelEnvironmentdataReal> implements IDeviceTunnelEnvironmentdataRealService {

    @Autowired
    private DeviceTunnelEnvironmentdataRealMapper deviceTunnelEnvironmentdataRealMapper;
    @Override
    public List<DeviceTunnelEnvironmentdataReal> selectList1(String shebeilist, Integer curid) {
        return deviceTunnelEnvironmentdataRealMapper.selectList1(shebeilist,curid);
    }
}
