package com.trtm.iot.gualan.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.trtm.iot.gualan.entity.GualanBase;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 挂篮数据主表
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
public interface IGualanBaseService extends IService<GualanBase> {

    List<Map<String, Object>> getYjList();


    Map<String, Object> getMap2Port(String sb1,String sb2);

    List<GualanBase> selectGuaLanList(String shebeilist, Integer curid);
}
