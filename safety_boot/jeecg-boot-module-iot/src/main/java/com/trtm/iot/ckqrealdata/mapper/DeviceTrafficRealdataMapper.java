package com.trtm.iot.ckqrealdata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.ckqrealdata.entity.DeviceTrafficRealdata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 采空区实时表信息
 * @Author: jeecg-boot
 * @Date: 2021-05-25
 * @Version: V1.0
 */
public interface DeviceTrafficRealdataMapper extends BaseMapper<DeviceTrafficRealdata> {


    List<String> selectMixinStationOrGroutingPump(@Param("topic") String topic);

    List<String> selectGroutingPumpBySid(@Param("sid") String sid, @Param("pid") String pid);
}
