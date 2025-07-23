package com.trtm.iot.gqpx_employee_training.service;

import com.trtm.iot.gqpx_employee_training.entity.GqpxEmployeeTraining;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.gqpx_training_plan.entity.GqpxTrainingPlan;

/**
 * @Description: 人员培训记录
 * @Author: jeecg-boot
 * @Date:   2024-11-29
 * @Version: V1.0
 */
public interface IGqpxEmployeeTrainingService extends IService<GqpxEmployeeTraining> {

    void savejl(GqpxTrainingPlan gqpxTrainingPlan);

    void deletejl(String id);

    Integer updatejl(GqpxTrainingPlan gqpxTrainingPlan);

    void updateUserStatus(GqpxEmployeeTraining gqpxEmployeeTrainingOne);

    void savepxjh(GqpxTrainingPlan gqpxTrainingPlan);
}
