package com.trtm.iot.deviceTunnelEnvironmentdataReal.service;

import com.trtm.iot.deviceTunnelEnvironmentdataReal.entity.DeviceTunnelEnvironmentdataReal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: device_tunnel_environmentdata_real
 * @Author: jeecg-boot
 * @Date:   2022-08-18
 * @Version: V1.0
 */
public interface IDeviceTunnelEnvironmentdataRealService extends IService<DeviceTunnelEnvironmentdataReal> {

    List<DeviceTunnelEnvironmentdataReal> selectList1(String shebeilist, Integer curid);
}
