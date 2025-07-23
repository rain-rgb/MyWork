package com.trtm.iot.anquanfxgk.service;

import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkPingjiaDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: anquan_fxfjgk_pingjia_detail
 * @Author: jeecg-boot
 * @Date:   2024-09-04
 * @Version: V1.0
 */
public interface IAnquanFxfjgkPingjiaDetailService extends IService<AnquanFxfjgkPingjiaDetail> {

    Integer deleteAnquanFxfjgkPingjiaDetailByParent(String parentId);

    List<AnquanFxfjgkPingjiaDetail> getAnquanFxfjgkPingjiaDetail(String parentId);

}
