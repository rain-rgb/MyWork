package com.trtm.iot.zlpzpeizhi.service;

import com.trtm.iot.zlpzpeizhi.entity.Zlpzpeizhi;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 浙路品质推送配置
 * @Author: jeecg-boot
 * @Date:   2023-12-12
 * @Version: V1.0
 */
public interface IZlpzpeizhiService extends IService<Zlpzpeizhi> {

    void saveMain(Zlpzpeizhi zlpzpeizhi);
}
