package com.trtm.iot.devicemixpilestatic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.devicemixpilestatic.entity.DeviceMixpileStatic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: device_mixpile_static
 * @Author: jeecg-boot
 * @Date:   2022-01-24
 * @Version: V1.0
 */
public interface DeviceMixpileStaticMapper extends BaseMapper<DeviceMixpileStatic> {

    DeviceMixpileStatic selectOnes(String datanyr1, String shebeino, String pileMileage);
}
