package com.trtm.iot.hctfysworkareapeiz.service;

import com.trtm.iot.hctfysworkareapeiz.entity.HcTfysworkareapeiz;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 土方压实配置表
 * @Author: jeecg-boot
 * @Date:   2024-04-09
 * @Version: V1.0
 */
public interface IHcTfysworkareapeizService extends IService<HcTfysworkareapeiz> {


    List<HcTfysworkareapeiz> tfys(String shebeilist, Integer curid);
}
