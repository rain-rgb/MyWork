package com.trtm.iot.gscsInformUser.service;

import com.trtm.iot.gscsInformUser.entity.GscsInformUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 安全班组会通知与用户关联表
 * @Author: jeecg-boot
 * @Date: 2022-02-14
 * @Version: V1.0
 */
public interface IGscsInformUserService extends IService<GscsInformUser> {
    Integer selectJoinTestCount();
}
