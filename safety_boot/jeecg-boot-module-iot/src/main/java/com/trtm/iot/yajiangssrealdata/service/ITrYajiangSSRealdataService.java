package com.trtm.iot.yajiangssrealdata.service;

import com.trtm.iot.yajiangssrealdata.entity.TrYajiangSSRealdata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: tr_yajiang_s_s_realdata
 * @Author: jeecg-boot
 * @Date:   2023-05-11
 * @Version: V1.0
 */
public interface ITrYajiangSSRealdataService extends IService<TrYajiangSSRealdata> {

    List<String> getSbList();
}
