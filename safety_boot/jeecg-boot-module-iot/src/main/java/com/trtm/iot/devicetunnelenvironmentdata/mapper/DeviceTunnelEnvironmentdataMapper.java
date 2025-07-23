package com.trtm.iot.devicetunnelenvironmentdata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.devicetunnelenvironmentdata.entity.DeviceTunnelEnvironmentdata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 隧道环境（有害气体）监测主表
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
public interface DeviceTunnelEnvironmentdataMapper extends BaseMapper<DeviceTunnelEnvironmentdata> {

    List<DeviceTunnelEnvironmentdata> selectLists(String shebeilist, Integer curid);
}
