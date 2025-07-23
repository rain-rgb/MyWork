package com.trtm.iot.safetyTunnelCarHistory.service;

import com.trtm.iot.safetyTunnelCarHistory.entity.SafetyTunnelCarHistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 车辆门禁考勤历史数据表
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
public interface ISafetyTunnelCarHistoryService extends IService<SafetyTunnelCarHistory> {
    List<SafetyTunnelCarHistory> selectLists(String shebeino, Integer id);
}
