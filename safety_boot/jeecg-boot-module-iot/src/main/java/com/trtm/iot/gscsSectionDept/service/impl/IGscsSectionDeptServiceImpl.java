package com.trtm.iot.gscsSectionDept.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.gscsSectionDept.entity.GscsSectionDept;
import com.trtm.iot.gscsSectionDept.mapper.GscsSectionDeptMapper;
import com.trtm.iot.gscsSectionDept.service.IGscsSectionDeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class IGscsSectionDeptServiceImpl extends ServiceImpl<GscsSectionDeptMapper, GscsSectionDept> implements IGscsSectionDeptService {
    @Resource
    GscsSectionDeptMapper gscsSectionDeptMapper;
    @Override
    public List<GscsSectionDept> getDeptList(String section) {
        QueryWrapper queryWrapper = new QueryWrapper<GscsSectionDept>();
        queryWrapper.select("dept_id").like("section",section);
        List<GscsSectionDept> deptList = gscsSectionDeptMapper.selectList(queryWrapper);
        return deptList;
    }
}
