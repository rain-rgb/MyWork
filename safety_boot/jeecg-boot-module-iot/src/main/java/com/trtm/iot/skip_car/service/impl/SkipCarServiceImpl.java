package com.trtm.iot.skip_car.service.impl;

import com.trtm.iot.skip_car.entity.SkipCar;
import com.trtm.iot.skip_car.entity.SkipCarTem;
import com.trtm.iot.skip_car.mapper.SkipCarMapper;
import com.trtm.iot.skip_car.service.ISkipCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 运输温度
 * @Author: jeecg-boot
 * @Date:   2023-05-31
 * @Version: V1.0
 */
@Service
public class SkipCarServiceImpl extends ServiceImpl<SkipCarMapper, SkipCar> implements ISkipCarService {
    @Autowired SkipCarMapper skipCarMapper;
    @Override
    public List<SkipCarTem> getTem(String carNumber, String date) {
        return skipCarMapper.getTem(carNumber, date);
    }
}
