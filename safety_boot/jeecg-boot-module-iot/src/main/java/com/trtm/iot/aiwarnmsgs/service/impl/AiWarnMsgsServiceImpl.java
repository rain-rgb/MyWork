package com.trtm.iot.aiwarnmsgs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.aiwarnmsgs.entity.AiWarnMsgs;
import com.trtm.iot.aiwarnmsgs.mapper.AiWarnMsgsMapper;
import com.trtm.iot.aiwarnmsgs.service.IAiWarnMsgsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: ai_warn_msgs
 * @Author: jeecg-boot
 * @Date:   2022-03-24
 * @Version: V1.0
 */
@Service
public class AiWarnMsgsServiceImpl extends ServiceImpl<AiWarnMsgsMapper, AiWarnMsgs> implements IAiWarnMsgsService {
    @Autowired
    private AiWarnMsgsMapper  aiWarnMsgsMapper;
    @Override
    public List<AiWarnMsgs> getlists(List<String> strsToList1, Integer curid) {
        try {
            QueryWrapper<AiWarnMsgs> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("id", curid);
            queryWrapper.eq("cid", 8);
            queryWrapper.in("shebeiid", strsToList1);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AiWarnMsgs> getlists1(String strsToList1, Integer curid) {
        return aiWarnMsgsMapper.getlists1(strsToList1,curid);
    }
}
