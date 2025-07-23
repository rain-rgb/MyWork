package com.trtm.iot.gqpx_training_plan.mapper;

import java.util.List;

import com.trtm.iot.gqpx_employee_training.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.gqpx_training_plan.entity.GqpxTrainingPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 岗前培训计划表
 * @Author: jeecg-boot
 * @Date:   2024-11-26
 * @Version: V1.0
 */
public interface GqpxTrainingPlanMapper extends BaseMapper<GqpxTrainingPlan> {

    SysUser getUsersByName(String realname);

    @Select("select depart_name from sys_depart where org_code = #{orgCode}")
    String selectProjNames(String orgCode);
}
