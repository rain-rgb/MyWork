package com.trtm.iot.gscsDept.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.gscsDept.entity.GscsDept;
import com.trtm.iot.gscsDept.mapper.GscsDeptMapper;
import com.trtm.iot.gscsDept.service.IGscsDeptService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 班组安全管控系统班组表
 * @Author: jeecg-boot
 * @Date:   2022-01-25
 * @Version: V1.0
 */
@Service
public class GscsDeptServiceImpl extends ServiceImpl<GscsDeptMapper, GscsDept> implements IGscsDeptService {
    @Resource
    GscsDeptMapper gscsDeptMapper;
    @Override
    public String selectDeptName(String deptId) {
        QueryWrapper queryWrapper = new QueryWrapper<GscsDept>();
        queryWrapper.eq("dept_id",deptId);
        GscsDept gscsDept = gscsDeptMapper.selectOne(queryWrapper);
        return gscsDept.getDeptName();
    }
}
