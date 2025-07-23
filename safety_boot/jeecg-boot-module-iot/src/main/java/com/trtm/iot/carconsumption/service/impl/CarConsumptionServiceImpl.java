package com.trtm.iot.carconsumption.service.impl;

import com.trtm.iot.carconsumption.entity.CarConsumption;
import com.trtm.iot.carconsumption.mapper.CarConsumptionMapper;
import com.trtm.iot.carconsumption.service.ICarConsumptionService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 车辆油费消耗信息表
 * @Author: jeecg-boot
 * @Date:   2022-01-12
 * @Version: V1.0
 */
@Service
public class CarConsumptionServiceImpl extends ServiceImpl<CarConsumptionMapper, CarConsumption> implements ICarConsumptionService {

}
