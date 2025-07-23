package com.trtm.iot.gqpx_training_plan.service;

import com.trtm.iot.gqpx_employee_training.entity.SysUser;
import com.trtm.iot.gqpx_training_plan.entity.GqpxTrainingPlan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 岗前培训计划表
 * @Author: jeecg-boot
 * @Date:   2024-11-26
 * @Version: V1.0
 */
public interface IGqpxTrainingPlanService extends IService<GqpxTrainingPlan> {

    SysUser getUsersByName(String lecturer);

    String selectProjNames(String orgCode);
}
