package com.trtm.iot.deviceTunnelEnvironmentdataReal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.deviceTunnelEnvironmentdataReal.entity.DeviceTunnelEnvironmentdataReal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: device_tunnel_environmentdata_real
 * @Author: jeecg-boot
 * @Date:   2022-08-18
 * @Version: V1.0
 */
public interface DeviceTunnelEnvironmentdataRealMapper extends BaseMapper<DeviceTunnelEnvironmentdataReal> {

    List<DeviceTunnelEnvironmentdataReal> selectList1(String shebeilist, Integer curid);
}
