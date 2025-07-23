package com.trtm.iot.zhydhistory.service;

import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 智慧用电历史数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-16
 * @Version: V1.0
 */
public interface IDeviceElectricHistorydataService extends IService<DeviceElectricHistorydata> {

    List<DeviceElectricHistorydata> selectzhydhistoryone(Integer curid, Integer alertstate);

    int updatezhydone(Integer id, Integer alertstate);


    List<DeviceElectricHistorydata> selectzhydhistorybaselist(String imei,Integer id);

    void updateStatus(Integer id, Integer status,Integer alertstate);

    DeviceElectricHistorydata selectzhydhistorysta(Integer id);

    List<DeviceElectricHistorydata> selectLists(List<String> strsToList1, Integer curid);

    List<DeviceElectricHistorydata> selectListToSHYJ(String shebeiNo, Integer id);

    List<DeviceElectricHistorydata> selectHisList(Integer curid, String shebeilist);
}
