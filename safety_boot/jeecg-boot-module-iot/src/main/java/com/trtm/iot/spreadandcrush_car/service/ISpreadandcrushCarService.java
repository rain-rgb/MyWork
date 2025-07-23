package com.trtm.iot.spreadandcrush_car.service;

import com.trtm.iot.spreadandcrush_car.entity.SpreadandcrushCar;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 运输轨迹
 * @Author: jeecg-boot
 * @Date:   2023-04-20
 * @Version: V1.0
 */
public interface ISpreadandcrushCarService extends IService<SpreadandcrushCar> {

    List<SpreadandcrushCar> getList(String shebeino, Integer id);

    List<SpreadandcrushCar> getListjt(String shebeino, Integer id);
}
