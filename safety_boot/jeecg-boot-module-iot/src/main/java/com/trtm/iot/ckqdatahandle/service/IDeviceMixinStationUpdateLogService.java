package com.trtm.iot.ckqdatahandle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStation;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStationUpdateLog;
import com.trtm.iot.ckqdatahandle.vo.SelectDeviceMixinStationUpdateLogVo;
import com.trtm.iot.ckqdatahandle.vo.SelectDeviceMixinStationVo;
import org.jeecg.common.api.vo.Result;
import org.springframework.stereotype.Service;

public interface IDeviceMixinStationUpdateLogService extends IService<DeviceMixinStationUpdateLog> {

    IPage<DeviceMixinStationUpdateLog> selectMixinStationUpdateLogPage(SelectDeviceMixinStationUpdateLogVo selectDeviceMixinStationUpdateLogVo);

     Result<Boolean> saveMixinStationInfoUpdateLog(Integer id, String batchingProductionUpdate,String groutingTotalUpdate);
}
