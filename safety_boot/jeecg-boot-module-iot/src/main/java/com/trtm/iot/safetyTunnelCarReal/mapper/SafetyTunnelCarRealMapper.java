package com.trtm.iot.safetyTunnelCarReal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.safetyTunnelCarReal.entity.SafetyTunnelCarReal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 车辆门禁考勤实时数据表
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
public interface SafetyTunnelCarRealMapper extends BaseMapper<SafetyTunnelCarReal> {

    List<SafetyTunnelCarReal> selectLists(String shebeino, Integer id);
}
