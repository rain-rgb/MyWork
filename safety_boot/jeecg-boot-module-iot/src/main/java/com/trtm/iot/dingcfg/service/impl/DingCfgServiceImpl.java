package com.trtm.iot.dingcfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.dingcfg.entity.DingCfg;
import com.trtm.iot.dingcfg.mapper.DingCfgMapper;
import com.trtm.iot.dingcfg.service.IDingCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 钉钉考勤机配置表
 * @Author: jeecg-boot
 * @Date:   2022-03-30
 * @Version: V1.0
 */
@Service
public class DingCfgServiceImpl extends ServiceImpl<DingCfgMapper, DingCfg> implements IDingCfgService {

    @Autowired
    private DingCfgMapper dingCfgMapper;

    @Override
    public List<DingCfg> selectlist() {
        QueryWrapper<DingCfg> queryWrapper=new QueryWrapper<>();
        return this.list(queryWrapper);
    }
}
