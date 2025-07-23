package com.trtm.iot.aiwarnmsg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.aiwarnmsg.entity.AiWarnMsg;
import com.trtm.iot.aiwarnmsg.mapper.AiWarnMsgMapper;
import com.trtm.iot.aiwarnmsg.service.IAiWarnMsgService;
import com.trtm.iot.aiwarnmsgcfg.entity.AiWarnMsgCfg;
import com.trtm.iot.aiwarnmsgcfg.service.IAiWarnMsgCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: AI识别预警
 * @Author: jeecg-boot
 * @Date: 2022-02-16
 * @Version: V1.0
 */
@Service
public class AiWarnMsgServiceImpl extends ServiceImpl<AiWarnMsgMapper, AiWarnMsg> implements IAiWarnMsgService {
    @Autowired
    private AiWarnMsgMapper aiWarnMsgMapper;
    @Autowired
    private IAiWarnMsgCfgService aiWarnMsgCfgService;

    @Override
    public List<AiWarnMsg> selectByShebeiNo(String sbList, Integer curid) {
        return aiWarnMsgMapper.selectByShebeiNo(sbList, curid);
    }

    @Override
    public List<AiWarnMsg> selectWarnData(Integer curid, String shebeilist) {
        return aiWarnMsgMapper.selectWarnData(curid, shebeilist);
    }

    @Override
    public String selectCfgByCid(String cid) {
        QueryWrapper<AiWarnMsgCfg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",cid);
        return null;
    }
}
