package com.trtm.iot.hc_project.service.impl;

import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.mapper.HcProjectMapper;
import com.trtm.iot.hc_project.service.IHcProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 华测获取项目
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Service
public class HcProjectServiceImpl extends ServiceImpl<HcProjectMapper, HcProject> implements IHcProjectService {

    @Autowired
    HcProjectMapper hcProjectMapper;

    @Override
    public Map<String, Object> selectByProjectid(String projectid) {
        return hcProjectMapper.selectByProjectid(projectid);
    }
}
