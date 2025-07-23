package com.trtm.iot.devicemixgrouted.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicemixgrouted.entity.DeviceMixpileGroutedOne;
import com.trtm.iot.devicemixgrouted.mapper.DeviceMixpileGroutedOneMapper;
import com.trtm.iot.devicemixgrouted.service.IDeviceMixpileGroutedOneService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 灌注桩详情
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
@Service
public class DeviceMixpileGroutedOneServiceImpl extends ServiceImpl<DeviceMixpileGroutedOneMapper, DeviceMixpileGroutedOne> implements IDeviceMixpileGroutedOneService {
    @Resource
    DeviceMixpileGroutedOneMapper deviceMixpileGroutedOneMapper;
    @Override
    public DeviceMixpileGroutedOne getByUuid(String uuid) {
        QueryWrapper<DeviceMixpileGroutedOne> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid",uuid);
        return  deviceMixpileGroutedOneMapper.selectOne(queryWrapper);
    }
}
