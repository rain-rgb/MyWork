package com.trtm.iot.rebarComponentTask.mapper;

import java.util.List;

import com.trtm.iot.rebarComponentMaterial.entity.RebarComponentMaterial;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.rebarComponentTask.entity.RebarComponentTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: rebar_component_task
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
public interface RebarComponentTaskMapper extends BaseMapper<RebarComponentTask> {

    String queryComponentNumber(@Param("componentId") String componentId, @Param("taskId") String taskId);

    RebarComponentTask getComponentTask(@Param("componentId") String componentId,@Param("taskId") String taskId);

    String getComponentNumberBycomponentId(@Param("componentId") String componentId,@Param("orgCodes") String orgCodes);

    RebarComponentTask getComponentMaterial(@Param("componentId") String componentId,@Param("taskId") String taskId);

    boolean deleteByComponentIdAndTaskId(@Param("componentId") String componentId,@Param("taskId") String taskId);

    boolean deleteByTaskId(@Param("taskId")  String taskId);

    List<RebarComponentTask> getComponentTaskListByTaskId(@Param("taskId") String taskId);
}
