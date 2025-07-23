package com.trtm.iot.aiwarnmsgcfg.service;

import com.trtm.iot.aiwarnmsgcfg.entity.AiWarnMsgCfg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: ai视频设备任务id配置表
 * @Author: jeecg-boot
 * @Date:   2022-03-16
 * @Version: V1.0
 */
public interface IAiWarnMsgCfgService extends IService<AiWarnMsgCfg> {

    AiWarnMsgCfg selectByCid(String cid);
}
