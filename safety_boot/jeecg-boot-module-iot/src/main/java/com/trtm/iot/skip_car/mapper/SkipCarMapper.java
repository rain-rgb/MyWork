package com.trtm.iot.skip_car.mapper;

import java.util.List;

import com.trtm.iot.skip_car.entity.SkipCarTem;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.skip_car.entity.SkipCar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 运输温度
 * @Author: jeecg-boot
 * @Date:   2023-05-31
 * @Version: V1.0
 */
public interface SkipCarMapper extends BaseMapper<SkipCar> {

    List<SkipCarTem> getTem(String carNumber, String date);
}
