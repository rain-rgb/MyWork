package com.trtm.iot.ckqdatahandle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStationUpdateLog;

public interface DeviceMixinStationUpdateLogMapper extends BaseMapper<DeviceMixinStationUpdateLog> {

    Double countGroutingTotalUpdate(String sid);

}
