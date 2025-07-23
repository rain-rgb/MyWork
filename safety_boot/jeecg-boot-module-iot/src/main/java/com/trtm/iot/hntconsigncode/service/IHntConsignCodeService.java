package com.trtm.iot.hntconsigncode.service;

import com.trtm.iot.hntconsigncode.entity.HntConsignCode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 混凝土见证取样二维码表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-02
 * @Version: V1.0
 */
public interface IHntConsignCodeService extends IService<HntConsignCode> {

    List<HntConsignCode> selectcodelist(String wtid);
}
