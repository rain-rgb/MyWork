package com.trtm.iot.anquanfxgk.service;

import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkBaseDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: anquan_fxfjgk_base_detail
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
public interface IAnquanFxfjgkBaseDetailService extends IService<AnquanFxfjgkBaseDetail> {
    List<AnquanFxfjgkBaseDetail> getDetailList(String guid);

}
