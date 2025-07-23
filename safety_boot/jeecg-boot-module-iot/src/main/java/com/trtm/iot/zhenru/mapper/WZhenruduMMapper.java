package com.trtm.iot.zhenru.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.zhenru.entity.WZhenruduM;

import java.util.List;

/**
 * @Description: w_zhenrudu_m
 * @Author: jeecg-boot
 * @Date:   2021-04-26
 * @Version: V1.0
 */
public interface WZhenruduMMapper extends BaseMapper<WZhenruduM> {

    List<WZhenruduM> getListjt(String shebeilist, Integer curid);
}
