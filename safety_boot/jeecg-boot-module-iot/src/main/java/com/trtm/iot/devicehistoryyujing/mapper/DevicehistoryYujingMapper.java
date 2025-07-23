package com.trtm.iot.devicehistoryyujing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.devicehistoryyujing.entity.DevicehistoryYujing;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 预警配置表
 * @Author: jeecg-boot
 * @Date:   2022-06-23
 * @Version: V1.0
 */
public interface DevicehistoryYujingMapper extends BaseMapper<DevicehistoryYujing> {

    DevicehistoryYujing selectone(String devaddr);
}
