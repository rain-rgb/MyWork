package com.trtm.iot.wzliaocangcommand.service;

import com.trtm.iot.wzliaocangcommand.entity.Wzliaocangcommand;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 料仓门禁表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-01
 * @Version: V1.0
 */
public interface IWzliaocangcommandService extends IService<Wzliaocangcommand> {

    Wzliaocangcommand selectone(Integer index, String code,String sysOrgCode);

    int updateone(Integer id,Integer index,String code);

    int updateone1(Integer id,Integer index,String code);
}
