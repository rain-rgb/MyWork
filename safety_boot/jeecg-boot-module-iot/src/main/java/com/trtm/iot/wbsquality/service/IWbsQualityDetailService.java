package com.trtm.iot.wbsquality.service;

import com.trtm.iot.wbsquality.entity.WbsQualityDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: wbs_quality_detail
 * @Author: jeecg-boot
 * @Date:   2024-11-27
 * @Version: V1.0
 */
public interface IWbsQualityDetailService extends IService<WbsQualityDetail> {

    String saveBatchBywbsid(String wbsid, String treeid,String temid);
}
