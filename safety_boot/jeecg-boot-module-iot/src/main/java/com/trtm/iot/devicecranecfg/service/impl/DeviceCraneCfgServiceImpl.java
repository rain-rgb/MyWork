package com.trtm.iot.devicecranecfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicecranecfg.entity.DeviceCraneCfg;
import com.trtm.iot.devicecranecfg.mapper.DeviceCraneCfgMapper;
import com.trtm.iot.devicecranecfg.service.IDeviceCraneCfgService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 大型设备(提梁机/架桥机)配置数据表
 * @Author: jeecg-boot
 * @Date:   2021-12-03
 * @Version: V1.0
 */
@Service
public class DeviceCraneCfgServiceImpl extends ServiceImpl<DeviceCraneCfgMapper, DeviceCraneCfg> implements IDeviceCraneCfgService {

    @Override
    public DeviceCraneCfg selectcranecallone(String shebeino) {
        try {
            QueryWrapper<DeviceCraneCfg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("device_code",shebeino);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
