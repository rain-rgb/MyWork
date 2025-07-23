package com.trtm.iot.safetyTunnelCarHistory.mapper;

import java.util.List;

import com.trtm.iot.safetyTunnelCarReal.entity.SafetyTunnelCarReal;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.safetyTunnelCarHistory.entity.SafetyTunnelCarHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 车辆门禁考勤历史数据表
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
public interface SafetyTunnelCarHistoryMapper extends BaseMapper<SafetyTunnelCarHistory> {
    List<SafetyTunnelCarHistory> selectLists(String shebeino, Integer id);
}
