package com.trtm.iot.devicetunnelposition.service;

import com.trtm.iot.devicetunnelposition.entity.DeviceTunnelPositiondata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 隧道人员定位表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-26
 * @Version: V1.0
 */
public interface IDeviceTunnelPositiondataService extends IService<DeviceTunnelPositiondata> {


    List<DeviceTunnelPositiondata> queryLists(String jzwz,String she,String dataTime);

}
