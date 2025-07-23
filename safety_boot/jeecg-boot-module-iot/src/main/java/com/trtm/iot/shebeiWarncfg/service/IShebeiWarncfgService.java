package com.trtm.iot.shebeiWarncfg.service;

import com.trtm.iot.shebeiWarncfg.entity.ShebeiWarncfg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 设备状态预警配置表
 * @Author: jeecg-boot
 * @Date:   2022-08-04
 * @Version: V1.0
 */
public interface IShebeiWarncfgService extends IService<ShebeiWarncfg> {

    ShebeiWarncfg getcfgdata(String sbjno);
}
