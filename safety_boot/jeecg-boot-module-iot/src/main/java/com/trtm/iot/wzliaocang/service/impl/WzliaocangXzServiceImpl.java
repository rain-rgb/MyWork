package com.trtm.iot.wzliaocang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.entity.WzliaocangXz;
import com.trtm.iot.wzliaocang.mapper.WzliaocangXzMapper;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wzliaocang.service.IWzliaocangXzService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: wzliaocang_xz
 * @Author: jeecg-boot
 * @Date: 2022-11-29
 * @Version: V1.0
 */
@Service
public class WzliaocangXzServiceImpl extends ServiceImpl<WzliaocangXzMapper, WzliaocangXz> implements IWzliaocangXzService {

    @Autowired
    private IWzliaocangService wzliaocangService;

    @Override
    @Transactional
    public void add(WzliaocangXz wzliaocangXz) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        wzliaocangXz.setUpdateBy(loginUser.getUsername());
        wzliaocangXz.setUpdateTime(DateUtils.now());
        this.save(wzliaocangXz);
        LambdaQueryWrapper<Wzliaocang> wzliaocangWrapper = new QueryWrapper<Wzliaocang>().lambda()
                .select(Wzliaocang::getId, Wzliaocang::getGuid, Wzliaocang::getLjxiuzheng).eq(Wzliaocang::getGuid, wzliaocangXz.getGuid()).last("for update");
        Wzliaocang wzliaocang = wzliaocangService.getOne(wzliaocangWrapper);
        if (null == wzliaocang.getLjxiuzheng()){
            wzliaocang.setLjxiuzheng(wzliaocangXz.getUpdateValue());
        } else {
            wzliaocang.setLjxiuzheng(wzliaocang.getLjxiuzheng() + wzliaocangXz.getUpdateValue());
        }
        wzliaocangService.updateById(wzliaocang);
    }
}
