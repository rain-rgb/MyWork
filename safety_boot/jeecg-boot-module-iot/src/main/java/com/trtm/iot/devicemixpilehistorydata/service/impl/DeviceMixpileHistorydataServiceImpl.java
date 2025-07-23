package com.trtm.iot.devicemixpilehistorydata.service.impl;

import com.trtm.iot.devicemixpilehistorydata.entity.DeviceMixpileHistorydata;
import com.trtm.iot.devicemixpilehistorydata.mapper.DeviceMixpileHistorydataMapper;
import com.trtm.iot.devicemixpilehistorydata.service.IDeviceMixpileHistorydataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: device_mixpile_historydata
 * @Author: jeecg-boot
 * @Date:   2021-10-20
 * @Version: V1.0
 */
@Service
public class DeviceMixpileHistorydataServiceImpl extends ServiceImpl<DeviceMixpileHistorydataMapper, DeviceMixpileHistorydata> implements IDeviceMixpileHistorydataService {

    @Autowired DeviceMixpileHistorydataMapper mixpileHistorydataMapper;

    @Override
    public List<DeviceMixpileHistorydata> selectLists(String shebeilist, Integer curid) {
        return mixpileHistorydataMapper.selectLists(shebeilist,curid);
    }

    @Override
    public List<DeviceMixpileHistorydata> selectlist(String shebeino, String pileno, String datatime, String starttime) {
        return mixpileHistorydataMapper.selectlist(shebeino,pileno,datatime,starttime);
    }

    @Override
    public DeviceMixpileHistorydata selectone(String shebeino, String datatime, String ltd, String lgd, String pileno) {
        return mixpileHistorydataMapper.selectOnes(shebeino,datatime,ltd,lgd,pileno);
    }
}
