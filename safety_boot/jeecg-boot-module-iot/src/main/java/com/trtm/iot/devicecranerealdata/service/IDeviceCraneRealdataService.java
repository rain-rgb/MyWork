package com.trtm.iot.devicecranerealdata.service;

import com.trtm.iot.devicecranerealdata.entity.DeviceCraneRealdata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 龙门吊实时数据
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
public interface IDeviceCraneRealdataService extends IService<DeviceCraneRealdata> {

    DeviceCraneRealdata queryone(String shebeiNo);

    List<DeviceCraneRealdata> selectListBySB(List<String> shebeiList);
}
