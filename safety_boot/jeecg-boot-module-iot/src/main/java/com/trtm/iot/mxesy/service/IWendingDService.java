package com.trtm.iot.mxesy.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.mxesy.entity.WendingD;

import java.util.List;

/**
 * @Description: w_wendingdu_m
 * @Author: jeecg-boot
 * @Date:   2021-04-28
 * @Version: V1.0
 */
public interface IWendingDService extends IService<WendingD> {

    List<WendingD> getListjt(String shebeilist, Integer curid);
}
