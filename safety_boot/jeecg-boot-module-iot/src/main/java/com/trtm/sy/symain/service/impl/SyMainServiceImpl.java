package com.trtm.sy.symain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.sy.symain.entity.SyMain;
import com.trtm.sy.symain.mapper.SyMainMapper;
import com.trtm.sy.symain.service.ISyMainService;
import com.trtm.sy.symainprocess.entity.SyMainProcess;
import com.trtm.sy.symainprocess.mapper.SyMainProcessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: sy_main
 * @Author: jeecg-boot
 * @Date:   2022-09-08
 * @Version: V1.0
 */
@Service
public class SyMainServiceImpl extends ServiceImpl<SyMainMapper, SyMain> implements ISyMainService {

    @Autowired
    SyMainProcessMapper syMainProcessMapper;

    @Override
    public List<SyMainProcess> getDetails(String uuid) {
        QueryWrapper<SyMainProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid",uuid);
        return syMainProcessMapper.selectList(queryWrapper);
    }
}
