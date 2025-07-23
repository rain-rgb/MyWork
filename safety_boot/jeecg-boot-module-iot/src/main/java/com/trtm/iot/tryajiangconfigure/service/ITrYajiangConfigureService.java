package com.trtm.iot.tryajiangconfigure.service;

import com.trtm.iot.tryajiangconfigure.entity.TrYajiangConfigure;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 压浆配置表
 * @Author: jeecg-boot
 * @Date:   2023-03-28
 * @Version: V1.0
 */
public interface ITrYajiangConfigureService extends IService<TrYajiangConfigure> {

    TrYajiangConfigure selectbyshebei(String shebeibianhao);
}
