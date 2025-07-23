package com.trtm.iot.gqpx_employee_training.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.gqpx_employee_training.entity.GqpxEmployeeTraining;
import com.trtm.iot.gqpx_employee_training.entity.SysUser;
import com.trtm.iot.gqpx_employee_training.mapper.GqpxEmployeeTrainingMapper;
import com.trtm.iot.gqpx_employee_training.service.IGqpxEmployeeTrainingService;
import com.trtm.iot.gqpx_training_plan.entity.GqpxTrainingPlan;
import com.trtm.iot.gqpx_training_plan.mapper.GqpxTrainingPlanMapper;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 人员培训记录
 * @Author: jeecg-boot
 * @Date: 2024-11-29
 * @Version: V1.0
 */
@Service
public class GqpxEmployeeTrainingServiceImpl extends ServiceImpl<GqpxEmployeeTrainingMapper, GqpxEmployeeTraining> implements IGqpxEmployeeTrainingService {
    @Autowired
    private GqpxEmployeeTrainingMapper gqpxEmployeeTrainingMapper;
    @Autowired
    private GqpxTrainingPlanMapper gqpxtrainingPlanMapper;
    @Autowired
    private IGqpxEmployeeTrainingService gqpxEmployeeTrainingService;

    @Override
    public void savejl(GqpxTrainingPlan gqpxTrainingPlan) {
        String worktype = gqpxTrainingPlan.getWorktype();
        // 拆分字符串并去除空格
        List<String> worktypeList = Arrays.asList(worktype.split(","));
        String banzu = gqpxTrainingPlan.getBanzu();
        List<String> banzuList = Arrays.asList(banzu.split(","));
        Integer type = gqpxTrainingPlan.getType();
        List<SysUser> users = gqpxEmployeeTrainingMapper.getUsersByBanzu(banzuList, gqpxTrainingPlan.getOrgcode(), worktypeList);
        for (SysUser user : users) {
            String id = user.getId();
            String realname = user.getRealname();
            String name = gqpxTrainingPlan.getName();
            String guid = gqpxTrainingPlan.getGuid();
            String orgcode = gqpxTrainingPlan.getOrgcode();

            GqpxEmployeeTraining gqpxEmployeeTraining = new GqpxEmployeeTraining();
            gqpxEmployeeTraining.setUserId(id);
            gqpxEmployeeTraining.setUserName(realname);
            gqpxEmployeeTraining.setTraningGuid(guid);
            gqpxEmployeeTraining.setTraningName(name);
            Date sgstartTime = gqpxTrainingPlan.getSgstartTime();
            Date sgendTime = gqpxTrainingPlan.getSgendTime();
            gqpxEmployeeTraining.setExpirationTime(sgstartTime + "-->" + sgendTime);
            gqpxEmployeeTraining.setTraningType(type);
            gqpxEmployeeTraining.setOrgcode(orgcode);
            gqpxEmployeeTrainingMapper.insert(gqpxEmployeeTraining);
        }
    }

    @Override
    public void savepxjh(GqpxTrainingPlan gqpxTrainingPlan) {
        String name = gqpxTrainingPlan.getName();
        String guid = gqpxTrainingPlan.getGuid();
        String orgcode = gqpxTrainingPlan.getOrgcode();
        Integer type = gqpxTrainingPlan.getType();
        String staff = gqpxTrainingPlan.getStaff();
        if (staff != null) {
            String[] userids = staff.split(",");
            // 使用 HashSet 去重
            Set<String> uniqueUserids = new HashSet<>(Arrays.asList(userids));
            for (String userid : uniqueUserids) {
                LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
                sysUserLambdaQueryWrapper.eq(SysUser::getId, userid);
                SysUser sysUser = gqpxEmployeeTrainingMapper.getUsersById(userid);
                GqpxEmployeeTraining gqpxEmployeeTraining = new GqpxEmployeeTraining();
                gqpxEmployeeTraining.setUserId(userid);
                gqpxEmployeeTraining.setTraningGuid(guid);
                gqpxEmployeeTraining.setTraningName(name);
                gqpxEmployeeTraining.setOrgcode(orgcode);
                //时间格式化
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date sgstartTime = gqpxTrainingPlan.getSgstartTime();
                Date sgendTime = gqpxTrainingPlan.getSgendTime();
                if (sgstartTime != null && sgendTime != null) {
                    gqpxEmployeeTraining.setExpirationTime(dateFormat.format(sgstartTime) + "-->" + dateFormat.format(sgendTime));
                }else {
                    gqpxEmployeeTraining.setExpirationTime("");
                }
                gqpxEmployeeTraining.setTraningType(type);
                gqpxEmployeeTraining.setUserName(sysUser.getRealname());
                gqpxEmployeeTrainingMapper.insert(gqpxEmployeeTraining);
            }
        }
    }

    @Override
    public Integer updatejl(GqpxTrainingPlan gqpxTrainingPlan) {
        String worktype1 = gqpxTrainingPlan.getWorktype();
        LambdaQueryWrapper<GqpxTrainingPlan> gqpxTrainingPlanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        gqpxTrainingPlanLambdaQueryWrapper.eq(GqpxTrainingPlan::getGuid, gqpxTrainingPlan.getGuid());
        GqpxTrainingPlan gqpxTrainingPlanOne = gqpxtrainingPlanMapper.selectOne(gqpxTrainingPlanLambdaQueryWrapper);
        if (gqpxTrainingPlanOne == null) {
            return 202;
        }
        String worktype2 = gqpxTrainingPlanOne.getWorktype();
        if (!worktype1.equals(worktype2)) {
            String guid = gqpxTrainingPlan.getGuid();
            LambdaQueryWrapper<GqpxEmployeeTraining> gqpxEmployeeTrainingLambdaQueryWrapper = new LambdaQueryWrapper<>();
            gqpxEmployeeTrainingLambdaQueryWrapper.eq(GqpxEmployeeTraining::getTraningGuid, guid)
                    .eq(GqpxEmployeeTraining::getStatus, 1);
            List<GqpxEmployeeTraining> list = gqpxEmployeeTrainingService.list(gqpxEmployeeTrainingLambdaQueryWrapper);
            if (list.size() > 0) {
                return 201;
            }
            gqpxEmployeeTrainingMapper.delete(new LambdaQueryWrapper<GqpxEmployeeTraining>().eq(GqpxEmployeeTraining::getTraningGuid, guid));
            String worktype = gqpxTrainingPlan.getWorktype();
            // 拆分字符串并去除空格
            List<String> worktypeList = Arrays.asList(worktype.split(","));
            String banzu = gqpxTrainingPlan.getBanzu();
            Integer type = gqpxTrainingPlan.getType();
            List<String> banzuList = Arrays.asList(banzu.split(","));
            List<SysUser> users = gqpxEmployeeTrainingMapper.getUsersByBanzu(banzuList, gqpxTrainingPlan.getOrgcode(), worktypeList);
            for (SysUser user : users) {
                String id = user.getId();
                String realname = user.getRealname();
                String name = gqpxTrainingPlan.getName();
                String guid1 = gqpxTrainingPlan.getGuid();

                GqpxEmployeeTraining gqpxEmployeeTraining = new GqpxEmployeeTraining();
                gqpxEmployeeTraining.setUserId(id);
                gqpxEmployeeTraining.setUserName(realname);
                gqpxEmployeeTraining.setTraningGuid(guid1);
                gqpxEmployeeTraining.setTraningName(name);
                Date sgstartTime = gqpxTrainingPlan.getSgstartTime();
                Date sgendTime = gqpxTrainingPlan.getSgendTime();
                gqpxEmployeeTraining.setExpirationTime(sgstartTime + "-->" + sgendTime);
                gqpxEmployeeTraining.setTraningType(type);
                gqpxEmployeeTrainingMapper.insert(gqpxEmployeeTraining);
            }
        }
        return 200;
    }

    @Override
    public void deletejl(String id) {
        GqpxTrainingPlan gqpxTrainingPlan = gqpxtrainingPlanMapper.selectById(id);
        String guid = gqpxTrainingPlan.getGuid();
        gqpxEmployeeTrainingMapper.delete(new LambdaQueryWrapper<GqpxEmployeeTraining>().eq(GqpxEmployeeTraining::getTraningGuid, guid));
    }

    @Override
    public void updateUserStatus(GqpxEmployeeTraining gqpxEmployeeTrainingOne) {
        String userId = gqpxEmployeeTrainingOne.getUserId();
        Integer traningType = gqpxEmployeeTrainingOne.getTraningType();
        if (traningType == 1) {
            gqpxEmployeeTrainingMapper.updateUserStatus(userId, 2);
        }
        if (traningType == 2) {
            gqpxEmployeeTrainingMapper.updateUserStatus(userId, 3);
        }
    }
}
