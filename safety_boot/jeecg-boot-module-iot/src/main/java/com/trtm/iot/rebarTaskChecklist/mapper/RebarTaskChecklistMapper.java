package com.trtm.iot.rebarTaskChecklist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.rebarTaskChecklist.entity.RebarTaskChecklist;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: rebar_task_checklist
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
public interface RebarTaskChecklistMapper extends BaseMapper<RebarTaskChecklist> {

    boolean updateTaskById(@Param("id") String id);

    RebarTaskChecklist selectByTaskId(@Param("taskId") String taskId);

    boolean updateTaskStatus(@Param("taskId") String taskId);
}
