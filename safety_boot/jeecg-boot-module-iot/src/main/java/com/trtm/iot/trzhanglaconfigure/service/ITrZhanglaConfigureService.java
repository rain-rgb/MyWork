package com.trtm.iot.trzhanglaconfigure.service;

import com.trtm.iot.trzhanglaconfigure.entity.TrZhanglaConfigure;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 张拉预警配置表
 * @Author: jeecg-boot
 * @Date:   2023-02-14
 * @Version: V1.0
 */
public interface ITrZhanglaConfigureService extends IService<TrZhanglaConfigure> {

    TrZhanglaConfigure selectbyshebei(String shebeibianhao);
}
