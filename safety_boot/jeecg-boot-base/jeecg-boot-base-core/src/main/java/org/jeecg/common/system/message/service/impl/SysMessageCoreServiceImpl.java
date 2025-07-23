package org.jeecg.common.system.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.mapper.SysMessageCoreMapper;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.system.base.service.impl.JeecgServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date: 2019-04-09
 * @Version: V1.0
 */
@Service
public class SysMessageCoreServiceImpl extends JeecgServiceImpl<SysMessageCoreMapper, SysMessageCore> implements ISysMessageCoreService {


    @Override
    public SysMessageCore selectOne(String toString, String phone, String s) {
        QueryWrapper<SysMessageCore> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("es_content",toString);
        queryWrapper.eq("es_title",s);
        queryWrapper.eq("es_receiver",phone);
        return getOne(queryWrapper);
    }
}
