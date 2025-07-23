package com.trtm.iot.hc_project.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.trtm.iot.hc_project.entity.HcProject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 华测获取项目
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
public interface IHcProjectService extends IService<HcProject> {

    Map<String, Object> selectByProjectid(String projectid);
}
