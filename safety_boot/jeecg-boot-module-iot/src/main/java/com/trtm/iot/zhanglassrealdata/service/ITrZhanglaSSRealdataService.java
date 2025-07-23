package com.trtm.iot.zhanglassrealdata.service;

import com.trtm.iot.zhanglassrealdata.entity.TrZhanglaSSRealdata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: tr_zhangla_s_s_realdata
 * @Author: jeecg-boot
 * @Date:   2023-05-12
 * @Version: V1.0
 */
public interface ITrZhanglaSSRealdataService extends IService<TrZhanglaSSRealdata> {

    List<String> getSbList();
}
