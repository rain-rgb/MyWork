package com.trtm.iot.ckqdatahandle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPump;
import com.trtm.iot.ckqdatahandle.vo.DeviceGroutingPumpVo;

public interface IDeviceGroutingPumpService extends IService<DeviceGroutingPump> {

    IPage<DeviceGroutingPump> selectDeviceGroutingPumpListPage(DeviceGroutingPumpVo deviceGroutingPumpVo);
}
