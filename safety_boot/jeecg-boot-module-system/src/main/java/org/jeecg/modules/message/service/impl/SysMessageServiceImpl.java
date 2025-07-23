package org.jeecg.modules.message.service.impl;

import org.jeecg.common.system.base.service.impl.JeecgServiceImpl;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.mapper.SysMessageMapper;
import org.jeecg.modules.message.service.ISysMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date: 2019-04-09
 * @Version: V1.0
 */
@Service
public class SysMessageServiceImpl extends JeecgServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

    @Autowired
    private SysMessageMapper sysMessageMapper;

    @Override
    public List<SysMessage> SelectLists() {

        return sysMessageMapper.SelectLists();
    }

    @Override
    public List<SysMessage> SelectListToJT() {

        return sysMessageMapper.SelectListToJT();
    }

    @Override
    public SysMessage selectones(String shebeiNo, String no) {
        return sysMessageMapper.selectones(shebeiNo, no);
    }
}
