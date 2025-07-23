package com.trtm.iot.zhydreal.service;

import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydreal.entity.DeviceElectricRealdata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 智慧用电实时数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-16
 * @Version: V1.0
 */
public interface IDeviceElectricRealdataService extends IService<DeviceElectricRealdata> {

    /**
     * 根据条件查出对应的数据
     */
     List<DeviceElectricRealdata> selectzhydone(Integer id );

     int updatezhydone(Integer id, Integer alertstate);



    void updateStatus(Integer id, Integer status);

    List<DeviceElectricRealdata> selectzhydbaselist(String imei,Integer id);


    List<DeviceElectricRealdata> selectLists(List<String> shebeiNo,Integer id);

    List<DeviceElectricHistorydata> selectRealList(Integer curid, String shebeilist);
}
