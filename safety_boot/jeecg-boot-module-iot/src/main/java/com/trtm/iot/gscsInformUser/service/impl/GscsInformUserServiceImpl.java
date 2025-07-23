package com.trtm.iot.gscsInformUser.service.impl;

import com.trtm.iot.gscsInformUser.entity.GscsInformUser;
import com.trtm.iot.gscsInformUser.mapper.GscsInformUserMapper;
import com.trtm.iot.gscsInformUser.service.IGscsInformUserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 安全班组会通知与用户关联表
 * @Author: jeecg-boot
 * @Date: 2022-02-14
 * @Version: V1.0
 */
@Service
public class GscsInformUserServiceImpl extends ServiceImpl<GscsInformUserMapper, GscsInformUser> implements IGscsInformUserService {

    @Override
    public Integer selectJoinTestCount() {
        return baseMapper.selectJoinTestCount();
    }
}
