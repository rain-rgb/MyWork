package com.trtm.iot.devicetunnelenvironmentdata.service;

import com.trtm.iot.devicetunnelenvironmentdata.entity.DeviceTunnelEnvironmentdata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 隧道环境（有害气体）监测主表
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
public interface IDeviceTunnelEnvironmentdataService extends IService<DeviceTunnelEnvironmentdata> {

    List<DeviceTunnelEnvironmentdata> selectLists(String shebeilist, Integer curid);
}
