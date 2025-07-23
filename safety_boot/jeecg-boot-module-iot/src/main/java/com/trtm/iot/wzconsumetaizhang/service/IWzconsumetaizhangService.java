package com.trtm.iot.wzconsumetaizhang.service;

import com.trtm.iot.wzconsumetaizhang.entity.Wzconsumetaizhang;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 物资原材料消耗台账主表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
public interface IWzconsumetaizhangService extends IService<Wzconsumetaizhang> {

    Wzconsumetaizhang selectwzxiaohao(String sysOrgCode, String projectName, String poureLocation, String strengthRank);
}
