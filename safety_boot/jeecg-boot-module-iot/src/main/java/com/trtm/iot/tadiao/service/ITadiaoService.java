package com.trtm.iot.tadiao.service;

import com.trtm.iot.tadiao.entity.Tadiao;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 塔吊
 * @Author: jeecg-boot
 * @Date:   2021-12-03
 * @Version: V1.0
 */
public interface ITadiaoService extends IService<Tadiao> {

    Tadiao find(String devicesn);
}
