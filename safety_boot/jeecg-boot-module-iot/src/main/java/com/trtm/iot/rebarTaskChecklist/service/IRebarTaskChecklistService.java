package com.trtm.iot.rebarTaskChecklist.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.iot.rebarTaskChecklist.entity.RebarTaskChecklist;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.rebarTaskChecklist.entity.TaskCheckVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: rebar_task_checklist
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
public interface IRebarTaskChecklistService extends IService<RebarTaskChecklist> {

    boolean addTaskCheck(TaskCheckVo taskCheckVo);

    boolean deleteTaskById(String id);

    boolean updateTaskStatus(String taskId);


    QueryWrapper<RebarTaskChecklist> getRebarTaskChecklistQueryWrapper(RebarTaskChecklist rebarTaskChecklist, String sys_depart_orgcode, HttpServletRequest req,Integer a);

}
