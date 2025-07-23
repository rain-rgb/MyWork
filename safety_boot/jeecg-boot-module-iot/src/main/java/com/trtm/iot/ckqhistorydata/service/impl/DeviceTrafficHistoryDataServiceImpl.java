package com.trtm.iot.ckqhistorydata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.ckqhistorydata.entity.DeviceTrafficHistoryData;
import com.trtm.iot.ckqhistorydata.mapper.DeviceTrafficHistoryDataMapper;
import com.trtm.iot.ckqhistorydata.service.IDeviceTrafficHistoryDataService;
import org.springframework.stereotype.Service;

@Service
public class DeviceTrafficHistoryDataServiceImpl extends ServiceImpl<DeviceTrafficHistoryDataMapper, DeviceTrafficHistoryData> implements IDeviceTrafficHistoryDataService {
}
