package com.trtm.iot.devicepowerstatusdata.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.devicepowerstatusdata.entity.DevicePowerStatusdata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: device_power_statusdata
 * @Author: jeecg-boot
 * @Date:   2022-12-29
 * @Version: V1.0
 */
@InterceptorIgnore(tenantLine = "true")
public interface DevicePowerStatusdataMapper extends BaseMapper<DevicePowerStatusdata> {

}
