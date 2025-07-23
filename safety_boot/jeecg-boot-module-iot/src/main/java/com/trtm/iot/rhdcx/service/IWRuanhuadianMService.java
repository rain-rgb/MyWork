package com.trtm.iot.rhdcx.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.rhdcx.entity.WRuanhuadianM;

import java.util.List;

/**
 * @Description: w_ruanhuadian_m
 * @Author: jeecg-boot
 * @Date:   2021-04-26
 * @Version: V1.0
 */
public interface IWRuanhuadianMService extends IService<WRuanhuadianM> {

    List<WRuanhuadianM> getListjt(String shebeilist, Integer curid);
}
