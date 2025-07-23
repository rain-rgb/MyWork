package com.trtm.iot.ckqdatahandle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStation;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStationDetails;
import com.trtm.iot.ckqdatahandle.vo.SelectDeviceMixinStationVo;

import java.util.Date;
import java.util.Map;

public interface IDeviceMixinStationDetailsService extends IService<DeviceMixinStationDetails> {

    IPage<DeviceMixinStationDetails> selectMixinStationDetailsListPage(SelectDeviceMixinStationVo selectDeviceMixinStationVo);

    Map<String,Object> selectMixinStationDetails(String sid, String uploadTime);

}
