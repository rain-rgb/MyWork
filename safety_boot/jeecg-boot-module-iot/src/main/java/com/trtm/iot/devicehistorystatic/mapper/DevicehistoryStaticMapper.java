package com.trtm.iot.devicehistorystatic.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.devicehistorystatic.entity.DevicehistoryStatic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 环境监测数据统计表
 * @Author: jeecg-boot
 * @Date:   2022-07-09
 * @Version: V1.0
 */
public interface DevicehistoryStaticMapper extends BaseMapper<DevicehistoryStatic> {

    DevicehistoryStatic selectone(String shebeino, Date date);
}
