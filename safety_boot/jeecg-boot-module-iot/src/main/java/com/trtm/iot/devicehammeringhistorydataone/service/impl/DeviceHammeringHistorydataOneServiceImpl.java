package com.trtm.iot.devicehammeringhistorydataone.service.impl;

import com.trtm.iot.devicehammeringhistorydataone.entity.DeviceHammeringHistorydataOne;
import com.trtm.iot.devicehammeringhistorydataone.mapper.DeviceHammeringHistorydataOneMapper;
import com.trtm.iot.devicehammeringhistorydataone.service.IDeviceHammeringHistorydataOneService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: device_hammering_historydata_one
 * @Author: jeecg-boot
 * @Date:   2024-03-08
 * @Version: V1.0
 */
@Service
public class DeviceHammeringHistorydataOneServiceImpl extends ServiceImpl<DeviceHammeringHistorydataOneMapper, DeviceHammeringHistorydataOne> implements IDeviceHammeringHistorydataOneService {
    private DeviceHammeringHistorydataOneMapper deviceHammeringHistorydataOneMapper;

    @Override
    public List<DeviceHammeringHistorydataOne> selectjbzzone(Integer curid, int i) {
        return deviceHammeringHistorydataOneMapper.selectjbzzone(curid,i);
    }
}
