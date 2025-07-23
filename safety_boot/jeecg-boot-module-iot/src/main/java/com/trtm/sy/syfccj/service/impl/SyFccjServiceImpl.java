package com.trtm.sy.syfccj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.sy.syfccj.entity.SyFccj;
import com.trtm.sy.syfccj.mapper.SyFccjMapper;
import com.trtm.sy.syfccj.service.ISyFccjService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 发车抽检
 * @Author: jeecg-boot
 * @Date:   2022-09-20
 * @Version: V1.0
 */
@Service
public class SyFccjServiceImpl extends ServiceImpl<SyFccjMapper, SyFccj> implements ISyFccjService {

    @Override
    public SyFccj findByFcid(String fcid,String post) {
        try {
            QueryWrapper<SyFccj> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("fcid",fcid).eq("cjdw",post);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
