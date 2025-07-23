package com.trtm.iot.deviceLargeOverHandler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.deviceLargeOverHandler.entity.DeviceLargeOverHandler;
import com.trtm.iot.deviceLargeOverHandler.mapper.DeviceLargeOverHandlerMapper;
import com.trtm.iot.deviceLargeOverHandler.service.IDeviceLargeOverHandlerService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: device_large_over_handler
 * @Author: jeecg-boot
 * @Date:   2022-10-24
 * @Version: V1.0
 */
@Service
public class DeviceLargeOverHandlerServiceImpl extends ServiceImpl<DeviceLargeOverHandlerMapper, DeviceLargeOverHandler> implements IDeviceLargeOverHandlerService {


    @Override
    public DeviceLargeOverHandler selectOne(String baseid) {
        QueryWrapper<DeviceLargeOverHandler> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("baseid",baseid);
        return this.getOne(queryWrapper);
    }
}
