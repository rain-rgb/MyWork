package com.trtm.iot.anquanfxgk.service;

import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkPingjia;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: anquan_fxfjgk_pingjia
 * @Author: jeecg-boot
 * @Date:   2024-09-04
 * @Version: V1.0
 */
public interface IAnquanFxfjgkPingjiaService extends IService<AnquanFxfjgkPingjia> {

    Integer saveAnquanFxfjgkPingjia(AnquanFxfjgkPingjia anquanFxfjgkPingjia);

    Integer updateAnquanFxfjgkPingjia(AnquanFxfjgkPingjia anquanFxfjgkPingjia);

}
