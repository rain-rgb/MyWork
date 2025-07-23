package com.trtm.iot.projectinfo.service;

import com.trtm.iot.projectinfo.entity.ProjectInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: project_info
 * @Author: jeecg-boot
 * @Date:   2022-12-16
 * @Version: V1.0
 */
public interface IProjectInfoService extends IService<ProjectInfo> {

    List<String> selectOrgCategory();

}
