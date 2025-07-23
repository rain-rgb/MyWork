package com.trtm.iot.devicepowerhistorydata.mapper;

import java.util.List;

import com.trtm.iot.devicepowerhistorydata.entity.DevicePowerHistorydata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: device_power_historydata
 * @Author: jeecg-boot
 * @Date:   2022-12-29
 * @Version: V1.0
 */
public interface DevicePowerHistorydataMapper extends BaseMapper<DevicePowerHistorydata> {


    List<DevicePowerHistorydata> selectListToSHYJ(String shebeiNo, Integer id);

    List<DevicePowerHistorydata> selectListbim(String shebeiNo, Integer id);

    List<DevicePowerHistorydata> selectListtoJG(String shebeilist, Integer curid);
}
