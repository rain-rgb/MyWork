package com.trtm.iot.aiwarnmsgcfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.aiwarnmsgcfg.entity.AiWarnMsgCfg;
import com.trtm.iot.aiwarnmsgcfg.mapper.AiWarnMsgCfgMapper;
import com.trtm.iot.aiwarnmsgcfg.service.IAiWarnMsgCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: ai视频设备任务id配置表
 * @Author: jeecg-boot
 * @Date:   2022-03-16
 * @Version: V1.0
 */
@Service
public class AiWarnMsgCfgServiceImpl extends ServiceImpl<AiWarnMsgCfgMapper, AiWarnMsgCfg> implements IAiWarnMsgCfgService {
    @Autowired
    private AiWarnMsgCfgMapper aiWarnMsgCfgMapper;

    @Override
    public AiWarnMsgCfg selectByCid(String cid) {
        QueryWrapper<AiWarnMsgCfg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid",cid);
        return aiWarnMsgCfgMapper.selectOne(queryWrapper);

    }
}
