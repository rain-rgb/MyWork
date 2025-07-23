package com.trtm.iot.hc_truck.service;

import com.trtm.iot.hc_truck.entity.HcTruck;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 获取运输车信息
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
public interface IHcTruckService extends IService<HcTruck> {

    List<String> selectbyorgcode(String sys_org_code, Integer sbType);
}
