package com.trtm.sy.syshrwd.service;

import com.trtm.sy.syshrwd.entity.Syshrwd;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 试验收货任务单
 * @Author: jeecg-boot
 * @Date:   2022-09-08
 * @Version: V1.0
 */
public interface ISyshrwdService extends IService<Syshrwd> {
    Syshrwd findByrwd(String shrwd );

    List<Syshrwd> sendMsg();

    Integer updateIschaoshiqueren(Integer id);
}
