package com.trtm.sy.sysb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.sysb.entity.SyDpsJcShebei;

/**
 * @Description: sy_dps_jc_shebei
 * @Author: jeecg-boot
 * @Date: 2023-10-16
 * @Version: V1.0
 */
public interface ISyDpsJcShebeiService extends IService<SyDpsJcShebei> {

    void add(SyDpsJcShebei syDpsJcShebei);
}
