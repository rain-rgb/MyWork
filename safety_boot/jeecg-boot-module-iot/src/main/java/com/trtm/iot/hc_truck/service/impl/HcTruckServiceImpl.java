package com.trtm.iot.hc_truck.service.impl;

import com.trtm.iot.hc_truck.entity.HcTruck;
import com.trtm.iot.hc_truck.mapper.HcTruckMapper;
import com.trtm.iot.hc_truck.service.IHcTruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 获取运输车信息
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Service
public class HcTruckServiceImpl extends ServiceImpl<HcTruckMapper, HcTruck> implements IHcTruckService {
    @Autowired
    HcTruckMapper hcTruckMapper;

    @Override
    public List<String> selectbyorgcode(String sys_org_code, Integer sbType) {
        return hcTruckMapper.selectbyorgcode(sys_org_code,sbType);
    }
}
