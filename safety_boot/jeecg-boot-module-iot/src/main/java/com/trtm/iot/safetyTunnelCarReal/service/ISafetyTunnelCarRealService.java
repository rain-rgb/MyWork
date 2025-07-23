package com.trtm.iot.safetyTunnelCarReal.service;

import com.trtm.iot.safetyTunnelCarReal.entity.SafetyTunnelCarReal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 车辆门禁考勤实时数据表
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
public interface ISafetyTunnelCarRealService extends IService<SafetyTunnelCarReal> {

    List<SafetyTunnelCarReal> selectLists(String shebeino, Integer id);
}
