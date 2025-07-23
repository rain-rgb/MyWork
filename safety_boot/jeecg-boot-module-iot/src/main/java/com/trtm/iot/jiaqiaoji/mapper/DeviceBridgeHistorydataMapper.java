package com.trtm.iot.jiaqiaoji.mapper;

import java.util.List;
import java.util.Map;

import com.trtm.iot.message.entity.SysMessage;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.jiaqiaoji.entity.DeviceBridgeHistorydata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: device_bridge_historydata
 * @Author: jeecg-boot
 * @Date:   2023-03-13
 * @Version: V1.0
 */
public interface DeviceBridgeHistorydataMapper extends BaseMapper<DeviceBridgeHistorydata> {

    List<DeviceBridgeHistorydata> selectJQJList(String shebeilist, Integer curid);

    List<DeviceBridgeHistorydata> selectListbim(String shebeilist, Integer curid);

    List<Map<String, Object>> getYjList();

    void saveyjdx(SysMessage sysMessage);
}
