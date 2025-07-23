package com.trtm.sy.wtgl.rwd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.wtgl.rwd.entity.SyDpsYyRenwudan;

import java.util.Map;

/**
 * @Description: sy_dps_yy_renwudan
 * @Author: jeecg-boot
 * @Date: 2023-08-31
 * @Version: V1.0
 */
public interface ISyDpsYyRenwudanService extends IService<SyDpsYyRenwudan> {

    void add(SyDpsYyRenwudan syDpsYyRenwudan);

    Map<String, Object> getRwdSaveData(String yuancaijinchangdengjiId);

    Map<String, Object> getXcwtRelation(Integer xcwtdId);
}
