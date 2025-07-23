package org.jeecg.modules.message.service;

import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.message.entity.SysMessage;

import java.util.List;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date: 2019-04-09
 * @Version: V1.0
 */
public interface ISysMessageService extends JeecgService<SysMessage> {


    List<SysMessage> SelectLists();
    List<SysMessage> SelectListToJT();

    SysMessage selectones(String shebeiNo, String no);
}
