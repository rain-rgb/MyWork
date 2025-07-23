package com.trtm.iot.weiyan.service;

import com.trtm.iot.weiyan.entity.WeiyanBase;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 围岩量测主表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
public interface IWeiyanBaseService extends IService<WeiyanBase> {
    public String weiyanSave(String json);
}
