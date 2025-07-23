package com.trtm.iot.devicehammeringhistorydataone.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.devicehammeringhistorydataone.entity.DeviceHammeringHistorydataOne;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: device_hammering_historydata_one
 * @Author: jeecg-boot
 * @Date:   2024-03-08
 * @Version: V1.0
 */
public interface DeviceHammeringHistorydataOneMapper extends BaseMapper<DeviceHammeringHistorydataOne> {

    @Select("select * from device_hammering_historydata_one where id > #{curid} and alertstate = #{i}")
    List<DeviceHammeringHistorydataOne> selectjbzzone(Integer curid, int i);
}
