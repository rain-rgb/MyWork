package com.trtm.sy.syjcxm.service.impl;

import com.trtm.sy.syjcxm.entity.SyDpsYyJcxm;
import com.trtm.sy.syjcxm.mapper.SyDpsYyJcxmMapper;
import com.trtm.sy.syjcxm.service.ISyDpsYyJcxmService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: sy_dps_yy_jcxm
 * @Author: jeecg-boot
 * @Date:   2023-06-28
 * @Version: V1.0
 */
@Service
public class SyDpsYyJcxmServiceImpl extends ServiceImpl<SyDpsYyJcxmMapper, SyDpsYyJcxm> implements ISyDpsYyJcxmService {
    @Autowired
    private SyDpsYyJcxmMapper jcxmMapper;

    @Override
    public Result<?> getJcxm() {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<Map<String, Object>> jcxm = jcxmMapper.getJcxmByDepartId(user.getOrgCode());
        return Result.OK(jcxm);
    }
}
