package com.trtm.iot.outsource.service;

import com.trtm.iot.outsource.entity.FTrial;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 委外试验
 * @Author: lis1
 * @Date: 2022-12-09
 * @Version: V1.0
 */
public interface IFTrialService extends IService<FTrial> {
    //委外试验数据推送 (品茗)
    List<FTrial> getDataToPush(Integer id);
}
