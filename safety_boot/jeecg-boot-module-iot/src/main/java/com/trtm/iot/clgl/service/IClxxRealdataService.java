package com.trtm.iot.clgl.service;

import com.trtm.iot.car.entity.SchedulingCar;
import com.trtm.iot.clgl.entity.ClxxRealdata;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.device_mixer_trucks.entity.DeviceMixerTrucks;

import java.util.List;

/**
 * @Description: 车辆信息实时数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-13
 * @Version: V1.0
 */
public interface IClxxRealdataService extends IService<ClxxRealdata> {

    ClxxRealdata queryone(String shebeino);

    List<ClxxRealdata> querylist(Integer status);

    List<ClxxRealdata> selectList(Integer curid, List<String> strsToList);

    String getDepartName(String sysOrgCode);

    void updateStatusById(Integer id);

    void saveClxxRealdata(DeviceMixerTrucks deviceMixerTrucks);
}
