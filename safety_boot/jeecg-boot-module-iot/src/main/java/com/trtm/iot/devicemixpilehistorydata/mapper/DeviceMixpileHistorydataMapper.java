package com.trtm.iot.devicemixpilehistorydata.mapper;

import java.util.List;

import com.trtm.iot.devicemixpilehistorydata.entity.DeviceMixpileHistorydata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: device_mixpile_historydata
 * @Author: jeecg-boot
 * @Date:   2021-10-20
 * @Version: V1.0
 */
public interface DeviceMixpileHistorydataMapper extends BaseMapper<DeviceMixpileHistorydata> {

    List<DeviceMixpileHistorydata> selectLists(String shebeilist, Integer curid);

    List<DeviceMixpileHistorydata> selectlist(String shebeino, String pileno, String datatime, String starttime);

    DeviceMixpileHistorydata selectOnes(String shebeino, String datatime, String ltd, String lgd, String pileno);
}
