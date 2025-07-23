package com.trtm.iot.gqpx_training_plan.service.impl;

import com.trtm.iot.gqpx_employee_training.entity.SysUser;
import com.trtm.iot.gqpx_training_plan.entity.GqpxTrainingPlan;
import com.trtm.iot.gqpx_training_plan.mapper.GqpxTrainingPlanMapper;
import com.trtm.iot.gqpx_training_plan.service.IGqpxTrainingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 岗前培训计划表
 * @Author: jeecg-boot
 * @Date:   2024-11-26
 * @Version: V1.0
 */
@Service
public class GqpxTrainingPlanServiceImpl extends ServiceImpl<GqpxTrainingPlanMapper, GqpxTrainingPlan> implements IGqpxTrainingPlanService {

    @Autowired
    private GqpxTrainingPlanMapper gqpxTrainingPlanMapper;

    @Override
    public SysUser getUsersByName(String lecturer) {
        return gqpxTrainingPlanMapper.getUsersByName(lecturer);
    }

    @Override
    public String selectProjNames(String orgCode) {
        return gqpxTrainingPlanMapper.selectProjNames(orgCode);
    }
}
