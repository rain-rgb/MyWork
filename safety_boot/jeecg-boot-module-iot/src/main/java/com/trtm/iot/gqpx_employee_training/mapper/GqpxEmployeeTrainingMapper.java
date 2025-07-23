package com.trtm.iot.gqpx_employee_training.mapper;

import java.util.List;

import com.trtm.iot.gqpx_employee_training.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.gqpx_employee_training.entity.GqpxEmployeeTraining;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 人员培训记录
 * @Author: jeecg-boot
 * @Date:   2024-11-29
 * @Version: V1.0
 */
public interface GqpxEmployeeTrainingMapper extends BaseMapper<GqpxEmployeeTraining> {

    List<SysUser> getUsers(String worktype, String orgCode);

    void updateUserStatus(String userId, int status);

    List<SysUser> getUsersByBanzu(List banzu, String orgCode, List worktype);

    SysUser getUsersById(String userid);

}
