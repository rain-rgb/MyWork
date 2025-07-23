package com.trtm.iot.ckqdatahandle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStation;
import com.trtm.iot.ckqdatahandle.vo.SelectDeviceMixinStationVo;
import org.jeecg.common.api.vo.Result;

import javax.servlet.http.HttpServletRequest;

public interface IDeviceMixinStationService extends IService<DeviceMixinStation> {

    IPage<DeviceMixinStation> selectMixinStationListPage(SelectDeviceMixinStationVo selectDeviceMixinStationVo);

}
