package com.trtm.iot.rebarComponentTask.service.impl;

import com.trtm.iot.rebarComponentMaterial.entity.RebarComponentMaterial;
import com.trtm.iot.rebarComponentTask.entity.RebarComponentTask;
import com.trtm.iot.rebarComponentTask.mapper.RebarComponentTaskMapper;
import com.trtm.iot.rebarComponentTask.service.IRebarComponentTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: rebar_component_task
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
@Service
public class RebarComponentTaskServiceImpl extends ServiceImpl<RebarComponentTaskMapper, RebarComponentTask> implements IRebarComponentTaskService {
    @Autowired
    private RebarComponentTaskMapper rebarComponentTaskMapper;
    @Override
    public RebarComponentTask getComponentTask(String componentId, String taskId) {
        return rebarComponentTaskMapper.getComponentTask(componentId,taskId);
    }

    @Override
    public String getComponentNumberBycomponentId(String componentId,String orgCodes) {
        return rebarComponentTaskMapper.getComponentNumberBycomponentId(componentId,orgCodes);
    }

    @Override
    public RebarComponentTask getComponentMaterial(String componentId, String taskId) {
        return rebarComponentTaskMapper.getComponentMaterial(componentId, taskId);
    }

    @Override
    public boolean deleteByComponentIdAndTaskId(String componentId, String taskId) {
        return rebarComponentTaskMapper.deleteByComponentIdAndTaskId(componentId, taskId);
    }

    @Override
    public boolean deleteByTaskId(String taskId) {
        return rebarComponentTaskMapper.deleteByTaskId(taskId);
    }

    @Override
    public List<RebarComponentTask> getComponentTaskListByTaskId(String taskId) {
        return rebarComponentTaskMapper.getComponentTaskListByTaskId(taskId);
    }
}
