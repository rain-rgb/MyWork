package com.trtm.iot.projectinfo.service.impl;

import com.trtm.iot.projectinfo.entity.ProjectInfo;
import com.trtm.iot.projectinfo.mapper.ProjectInfoMapper;
import com.trtm.iot.projectinfo.service.IProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: project_info
 * @Author: jeecg-boot
 * @Date:   2022-12-16
 * @Version: V1.0
 */
@Service
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo> implements IProjectInfoService {

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Override
    public List<String> selectOrgCategory() {
        return projectInfoMapper.selectOrgCategory();
    }
}
