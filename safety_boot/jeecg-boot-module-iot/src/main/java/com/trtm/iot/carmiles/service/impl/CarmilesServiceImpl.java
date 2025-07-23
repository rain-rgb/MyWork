package com.trtm.iot.carmiles.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.carmiles.entity.Carmiles;
import com.trtm.iot.carmiles.mapper.CarmilesMapper;
import com.trtm.iot.carmiles.service.ICarmilesService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 车辆里程
 * @Author: jeecg-boot
 * @Date:   2022-03-08
 * @Version: V1.0
 */
@Service
public class CarmilesServiceImpl extends ServiceImpl<CarmilesMapper, Carmiles> implements ICarmilesService {

    @Override
    public Carmiles getones(String shebei) {

        try {
            QueryWrapper<Carmiles> queryWrapper=new QueryWrapper<>();
            queryWrapper.select("sum(miles) as miles");
            queryWrapper.eq("shebei_no",shebei);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
