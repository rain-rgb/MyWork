package com.trtm.iot.tadiao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.tadiao.entity.DeviceTowerHistorydata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: device_tower_historydata
 * @Author: jeecg-boot
 * @Date:   2023-02-27
 * @Version: V1.0
 */
public interface DeviceTowerHistorydataMapper extends BaseMapper<DeviceTowerHistorydata> {

    List<Map<String, Object>> getYjList();
}
