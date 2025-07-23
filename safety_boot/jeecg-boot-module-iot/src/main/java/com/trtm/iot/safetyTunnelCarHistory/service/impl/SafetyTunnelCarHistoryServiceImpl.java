package com.trtm.iot.safetyTunnelCarHistory.service.impl;

import com.trtm.iot.safetyTunnelCarHistory.entity.SafetyTunnelCarHistory;
import com.trtm.iot.safetyTunnelCarHistory.mapper.SafetyTunnelCarHistoryMapper;
import com.trtm.iot.safetyTunnelCarHistory.service.ISafetyTunnelCarHistoryService;
import com.trtm.iot.safetyTunnelCarReal.entity.SafetyTunnelCarReal;
import com.trtm.iot.safetyTunnelCarReal.mapper.SafetyTunnelCarRealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 车辆门禁考勤历史数据表
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
@Service
public class SafetyTunnelCarHistoryServiceImpl extends ServiceImpl<SafetyTunnelCarHistoryMapper, SafetyTunnelCarHistory> implements ISafetyTunnelCarHistoryService {
    @Autowired
    private SafetyTunnelCarHistoryMapper safetyTunnelCarRealMapper;
    @Override
    public List<SafetyTunnelCarHistory> selectLists(String shebeino, Integer id) {
        return safetyTunnelCarRealMapper.selectLists(shebeino, id);
    }
}
