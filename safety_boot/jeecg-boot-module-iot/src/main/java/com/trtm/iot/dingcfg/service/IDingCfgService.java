package com.trtm.iot.dingcfg.service;

import com.trtm.iot.dingcfg.entity.DingCfg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 钉钉考勤机配置表
 * @Author: jeecg-boot
 * @Date:   2022-03-30
 * @Version: V1.0
 */
public interface IDingCfgService extends IService<DingCfg> {

    List<DingCfg> selectlist();

}
