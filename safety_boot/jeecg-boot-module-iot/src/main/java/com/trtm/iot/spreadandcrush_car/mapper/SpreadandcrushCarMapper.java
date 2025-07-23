package com.trtm.iot.spreadandcrush_car.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.spreadandcrush_car.entity.SpreadandcrushCar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 运输轨迹
 * @Author: jeecg-boot
 * @Date:   2023-04-20
 * @Version: V1.0
 */
public interface SpreadandcrushCarMapper extends BaseMapper<SpreadandcrushCar> {

    List<SpreadandcrushCar> getList(String shebeino, Integer id);

    List<SpreadandcrushCar> getListjt(String shebeino, Integer id);
}
