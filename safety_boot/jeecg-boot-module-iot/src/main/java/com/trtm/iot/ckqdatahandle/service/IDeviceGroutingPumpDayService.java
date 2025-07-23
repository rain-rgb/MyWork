package com.trtm.iot.ckqdatahandle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPump;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPumpDay;
import com.trtm.iot.ckqdatahandle.vo.DeviceGroutingPumpDayVo;
import com.trtm.iot.ckqdatahandle.vo.DeviceGroutingPumpVo;

import java.util.List;

public interface IDeviceGroutingPumpDayService extends IService<DeviceGroutingPumpDay> {

    IPage<DeviceGroutingPumpDay> selectDeviceGroutingPumpDayListPage(DeviceGroutingPumpVo deviceGroutingPumpVo);

    List<DeviceGroutingPumpDayVo> selectDeviceGroutingPumpDayVoList(String time);
}
