package com.trtm.sy.sysb.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.trtm.sy.sysb.entity.SyDpsJcShebei;
import com.trtm.sy.sysb.mapper.SyDpsJcShebeiMapper;
import com.trtm.sy.sysb.service.ISyDpsJcShebeiService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: sy_dps_jc_shebei
 * @Author: jeecg-boot
 * @Date:   2023-10-16
 * @Version: V1.0
 */
@Service
public class SyDpsJcShebeiServiceImpl extends ServiceImpl<SyDpsJcShebeiMapper, SyDpsJcShebei> implements ISyDpsJcShebeiService {

    @Override
    public void add(SyDpsJcShebei syDpsJcShebei) {
        if (oConvertUtils.isEmpty(syDpsJcShebei.getShebeino())) {
            throw new JeecgBootException("请输入设备编号");
        }
        SyDpsJcShebei dpsJcShebei = this.baseMapper.selectOne(
                Wrappers.lambdaQuery(new SyDpsJcShebei()).eq(SyDpsJcShebei::getShebeino, syDpsJcShebei.getShebeino()).eq(SyDpsJcShebei::getShebeiisdel, 0));
        if (oConvertUtils.isNotEmpty(dpsJcShebei)) {
            throw new JeecgBootException("设备编号已存在");
        }
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        syDpsJcShebei.setChuangjianren(user.getId());
        syDpsJcShebei.setChuangjianriqi(DateUtils.getDate("yyyy-MM-dd"));
        syDpsJcShebei.setChuangjianleibie(0);
        syDpsJcShebei.setShebeiisdel(0);
        this.save(syDpsJcShebei);
    }
}
