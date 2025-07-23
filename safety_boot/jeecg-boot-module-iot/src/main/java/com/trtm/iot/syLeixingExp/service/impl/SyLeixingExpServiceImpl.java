package com.trtm.iot.syLeixingExp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.syLeixingExp.entity.SyLeixingExp;
import com.trtm.iot.syLeixingExp.mapper.SyLeixingExpMapper;
import com.trtm.iot.syLeixingExp.service.ISyLeixingExpService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 试验类型和试验关联表
 * @Author: jeecg-boot
 * @Date: 2022-03-22
 * @Version: V1.0
 */
@Service
public class SyLeixingExpServiceImpl extends ServiceImpl<SyLeixingExpMapper, SyLeixingExp> implements ISyLeixingExpService {

    @Override
    public List getExpList(String typeId) {
        List<SyLeixingExp> expList = baseMapper.selectList(new QueryWrapper<SyLeixingExp>().eq("parentId", typeId));
        return expList;
    }
}
