package org.jeecg.common.system.message.service;

import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.base.service.JeecgService;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date: 2019-04-09
 * @Version: V1.0
 */

public interface ISysMessageCoreService extends JeecgService<SysMessageCore> {


    SysMessageCore selectOne(String toString, String phone, String s);
}
