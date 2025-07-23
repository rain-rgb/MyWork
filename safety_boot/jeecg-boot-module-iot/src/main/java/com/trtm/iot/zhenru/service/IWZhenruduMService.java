package com.trtm.iot.zhenru.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.zhenru.entity.WZhenruduM;

import java.util.List;

/**
 * @Description: w_zhenrudu_m
 * @Author: jeecg-boot
 * @Date:   2021-04-26
 * @Version: V1.0
 */
public interface IWZhenruduMService extends IService<WZhenruduM> {

    List<WZhenruduM> getListjt(String shebeilist, Integer curid);
}
