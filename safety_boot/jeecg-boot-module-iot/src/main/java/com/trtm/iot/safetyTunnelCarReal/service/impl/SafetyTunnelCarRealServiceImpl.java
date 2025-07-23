package com.trtm.iot.safetyTunnelCarReal.service.impl;

import com.trtm.iot.safetyTunnelCarReal.entity.SafetyTunnelCarReal;
import com.trtm.iot.safetyTunnelCarReal.mapper.SafetyTunnelCarRealMapper;
import com.trtm.iot.safetyTunnelCarReal.service.ISafetyTunnelCarRealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 车辆门禁考勤实时数据表
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
@Service
public class SafetyTunnelCarRealServiceImpl extends ServiceImpl<SafetyTunnelCarRealMapper, SafetyTunnelCarReal> implements ISafetyTunnelCarRealService {
    @Autowired
    private SafetyTunnelCarRealMapper safetyTunnelCarRealMapper;
    @Override
    public List<SafetyTunnelCarReal> selectLists(String shebeino, Integer id) {
        return safetyTunnelCarRealMapper.selectLists(shebeino, id);
    }
}
