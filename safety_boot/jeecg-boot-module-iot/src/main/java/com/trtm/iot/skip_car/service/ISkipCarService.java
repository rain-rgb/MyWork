package com.trtm.iot.skip_car.service;

import com.trtm.iot.skip_car.entity.SkipCar;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.skip_car.entity.SkipCarTem;

import java.util.List;

/**
 * @Description: 运输温度
 * @Author: jeecg-boot
 * @Date:   2023-05-31
 * @Version: V1.0
 */
public interface ISkipCarService extends IService<SkipCar> {

    List<SkipCarTem> getTem(String carNumber, String date);
}
