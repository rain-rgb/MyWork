package com.trtm.iot.tpny.service;

import cn.hutool.json.JSONObject;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 摊铺碾压数据监测主表
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
public interface IFrontDeviceRealdataService extends IService<FrontDeviceRealdata> {
    boolean updateFrontdeviceData(JSONObject json);

    FrontDeviceRealdata queryone(String shebeino);

    void saveOrUpdateByProjectId(FrontDeviceRealdata frontDeviceRealdata);
}
