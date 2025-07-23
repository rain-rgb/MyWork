package com.trtm.iot.spreadandcrush_car.service.impl;

import com.trtm.iot.spreadandcrush_car.entity.SpreadandcrushCar;
import com.trtm.iot.spreadandcrush_car.mapper.SpreadandcrushCarMapper;
import com.trtm.iot.spreadandcrush_car.service.ISpreadandcrushCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 运输轨迹
 * @Author: jeecg-boot
 * @Date:   2023-04-20
 * @Version: V1.0
 */
@Service
public class SpreadandcrushCarServiceImpl extends ServiceImpl<SpreadandcrushCarMapper, SpreadandcrushCar> implements ISpreadandcrushCarService {

    @Autowired
    private SpreadandcrushCarMapper spreadandcrushCarMapper;

    @Override
    public List<SpreadandcrushCar> getList(String shebeino, Integer id) {
        return spreadandcrushCarMapper.getList(shebeino, id);
    }

    @Override
    public List<SpreadandcrushCar> getListjt(String shebeino, Integer id) {
        return spreadandcrushCarMapper.getListjt(shebeino, id);
    }
}
