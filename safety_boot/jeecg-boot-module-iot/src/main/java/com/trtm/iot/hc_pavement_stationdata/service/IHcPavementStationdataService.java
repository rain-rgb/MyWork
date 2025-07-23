package com.trtm.iot.hc_pavement_stationdata.service;

import com.trtm.iot.hc_pavement_stationdata.entity.HcPavementStationdata;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.hc_pavement_stationdata.entity.HcPavementStationdataP;

import java.util.List;

/**
 * @Description: 获取逐桩数据
 * @Author: jeecg-boot
 * @Date:   2023-04-24
 * @Version: V1.0
 */
public interface IHcPavementStationdataService extends IService<HcPavementStationdata> {

    List<HcPavementStationdataP> getData(String carNumber);

    HcPavementStationdata getavg(String start, String end);
}
