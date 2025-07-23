package com.trtm.iot.devicetunnelpositiondatareal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.trtm.iot.devicetunnelpositiondatareal.entity.DeviceTunnelPositiondataRealDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.devicetunnelpositiondatareal.entity.DeviceTunnelPositiondataReal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 人员定位实时表
 * @Author: jeecg-boot
 * @Date:   2021-09-27
 * @Version: V1.0
 */
@Mapper
public interface DeviceTunnelPositiondataRealMapper extends BaseMapper<DeviceTunnelPositiondataReal> {

    List<DeviceTunnelPositiondataRealDto> select(@Param("orgCode") String orgCode, @Param("sbname") String sbname, @Param("sbtype") String sbtype);

    List<DeviceTunnelPositiondataReal> queryJiXinList(@Param("shebeino") String shebeino);

    List<DeviceTunnelPositiondataReal> queryJiXinBanZuList(@Param("shebeino") String shebeino);

    List<DeviceTunnelPositiondataReal> querylistAttendance(@Param("shebeino")String shebeino);
}
