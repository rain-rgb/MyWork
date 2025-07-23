package com.trtm.iot.devicetunnelposition.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.devicetunnelposition.entity.DeviceTunnelPositiondata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.checkerframework.common.aliasing.qual.MaybeAliased;

/**
 * @Description: 隧道人员定位表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-26
 * @Version: V1.0
 */
@Mapper
public interface DeviceTunnelPositiondataMapper extends BaseMapper<DeviceTunnelPositiondata> {

//    @Select("SELECT * FROM device_tunnel_positiondata INNER JOIN ( SELECT username, max( dataTime ) 'dataTime' FROM device_tunnel_positiondata GROUP BY username ) b ON\n" +
//            "device_tunnel_positiondata.username = b.username AND device_tunnel_positiondata.dataTime = b.dataTime and device_tunnel_positiondata.jzwz like concat('%',#{jzwz},'%') and device_tunnel_positiondata.shebeino in (${she})  ORDER BY\n" +
//            "\tdevice_tunnel_positiondata.dataTime DESC") 每个人最新的数据不一定是当天的
    @Select("select * from device_tunnel_positiondata where dataTime>#{dataTime} AND jzwz like concat('%',#{jzwz},'%') and shebeino in (${she}) GROUP BY username  ORDER BY dataTime DESC")
    List<DeviceTunnelPositiondata> queryLists(String jzwz,String she,String dataTime);
}
