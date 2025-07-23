package com.trtm.iot.rebarComponentTask.service;

import com.trtm.iot.rebarComponentMaterial.entity.RebarComponentMaterial;
import com.trtm.iot.rebarComponentTask.entity.RebarComponentTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: rebar_component_task
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
public interface IRebarComponentTaskService extends IService<RebarComponentTask> {

    RebarComponentTask getComponentTask(String componentId, String taskId);

    String getComponentNumberBycomponentId(String componentId,String orgCodes);

    RebarComponentTask getComponentMaterial(String componentId, String taskId);

    boolean deleteByComponentIdAndTaskId(String componentId, String taskId);

    boolean deleteByTaskId(String taskId);

    List<RebarComponentTask> getComponentTaskListByTaskId(String taskId);

}
