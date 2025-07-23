package com.trtm.iot.devicepipepilehistorydatapart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicepipepilehistorydatapart.entity.DevicePipepileHistorydataPart;
import com.trtm.iot.devicepipepilehistorydatapart.mapper.DevicePipepileHistorydataPartMapper;
import com.trtm.iot.devicepipepilehistorydatapart.service.IDevicePipepileHistorydataPartService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: device_pipepile_historydata_part
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
@Service
public class DevicePipepileHistorydataPartServiceImpl extends ServiceImpl<DevicePipepileHistorydataPartMapper, DevicePipepileHistorydataPart> implements IDevicePipepileHistorydataPartService {

    @Override
    public List<DevicePipepileHistorydataPart> selectListwcx(String shebeino, String pileNo) {
        QueryWrapper<DevicePipepileHistorydataPart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shebeino",shebeino);
        queryWrapper.eq("pile_no",pileNo);
        return this.list(queryWrapper);

    }
}
