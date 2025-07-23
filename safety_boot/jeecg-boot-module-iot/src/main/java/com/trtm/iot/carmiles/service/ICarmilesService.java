package com.trtm.iot.carmiles.service;

import com.trtm.iot.carmiles.entity.Carmiles;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 车辆里程
 * @Author: jeecg-boot
 * @Date:   2022-03-08
 * @Version: V1.0
 */
public interface ICarmilesService extends IService<Carmiles> {

    Carmiles getones(String shebei);
}
