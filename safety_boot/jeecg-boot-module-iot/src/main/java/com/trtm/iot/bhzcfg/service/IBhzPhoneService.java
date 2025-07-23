package com.trtm.iot.bhzcfg.service;

import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 拌合站手机号码配置
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
public interface IBhzPhoneService extends IService<BhzPhone> {

    /**
     * 根据拌合站超标配置去查询相对应的手机号码
     * @param uid
     * @return
     */
    BhzPhone selectBhzPhone(String uid);

    /**
     * 根据设备以及类型去查询手机号码
     * @param sysorgcode
     * @return
     */
    List<BhzPhone> selectBhzPhoneList(String sysorgcode, Integer phonesname);

    BhzPhone getBhzPhone(String names);
}
