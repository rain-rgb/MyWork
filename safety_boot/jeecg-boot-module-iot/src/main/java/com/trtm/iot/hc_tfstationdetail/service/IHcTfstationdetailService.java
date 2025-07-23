package com.trtm.iot.hc_tfstationdetail.service;

import com.trtm.iot.hc_tfstationdetail.entity.HcTfstationdetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 土方工作区逐桩详情
 * @Author: jeecg-boot
 * @Date:   2023-10-10
 * @Version: V1.0
 */
public interface IHcTfstationdetailService extends IService<HcTfstationdetail> {

    List<HcTfstationdetail> selectlistbybaserid(String blockid);
}
