package com.trtm.iot.hc_pavement_stationdata.service.impl;

import com.trtm.iot.hc_pavement_stationdata.entity.HcPavementStationdata;
import com.trtm.iot.hc_pavement_stationdata.entity.HcPavementStationdataP;
import com.trtm.iot.hc_pavement_stationdata.mapper.HcPavementStationdataMapper;
import com.trtm.iot.hc_pavement_stationdata.service.IHcPavementStationdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 获取逐桩数据
 * @Author: jeecg-boot
 * @Date:   2023-04-24
 * @Version: V1.0
 */
@Service
public class HcPavementStationdataServiceImpl extends ServiceImpl<HcPavementStationdataMapper, HcPavementStationdata> implements IHcPavementStationdataService {
    @Autowired HcPavementStationdataMapper hcPavementStationdataMapper;
    @Override
    public List<HcPavementStationdataP> getData(String carNumber) {
        return hcPavementStationdataMapper.getData(carNumber);
    }

    @Override
    public HcPavementStationdata getavg(String start, String end) {
        return hcPavementStationdataMapper.getavg(start, end);
    }
}
