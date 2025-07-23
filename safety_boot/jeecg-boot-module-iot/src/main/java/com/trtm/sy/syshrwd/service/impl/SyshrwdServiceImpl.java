package com.trtm.sy.syshrwd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.sy.syshrwd.entity.Syshrwd;
import com.trtm.sy.syshrwd.mapper.SyshrwdMapper;
import com.trtm.sy.syshrwd.service.ISyshrwdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 试验收货任务单
 * @Author: jeecg-boot
 * @Date:   2022-09-08
 * @Version: V1.0
 */
@Service
public class SyshrwdServiceImpl extends ServiceImpl<SyshrwdMapper, Syshrwd> implements ISyshrwdService {

    @Autowired
    private SyshrwdMapper syshrwdMapper;

    @Override
    public Syshrwd findByrwd(String shrwd) {
        LambdaQueryWrapper<Syshrwd> queryWrapper = new LambdaQueryWrapper<>();
          queryWrapper.eq(Syshrwd::getShrwd, shrwd);
         return this.getOne(queryWrapper);

    }

    @Override
    public List<Syshrwd> sendMsg( ) {
        return syshrwdMapper.sendMsg();
    }

    @Override
    public Integer updateIschaoshiqueren(Integer id) {
        Syshrwd syshrwd = new Syshrwd();
        syshrwd.setId(id);
        syshrwd.setIschaoshiqueren(1);
        int i = syshrwdMapper.updateById(syshrwd);
        return i;
    }
}
