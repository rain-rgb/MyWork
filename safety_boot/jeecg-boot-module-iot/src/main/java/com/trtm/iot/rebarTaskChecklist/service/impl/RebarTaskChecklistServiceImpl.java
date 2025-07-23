package com.trtm.iot.rebarTaskChecklist.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.rebarComponent.entity.RebarComponent;
import com.trtm.iot.rebarComponent.mapper.RebarComponentMapper;
import com.trtm.iot.rebarComponent.vo.RebarComponentTaskVo;
import com.trtm.iot.rebarComponentTask.entity.RebarComponentTask;
import com.trtm.iot.rebarComponentTask.mapper.RebarComponentTaskMapper;
import com.trtm.iot.rebarTaskChecklist.entity.RebarTaskChecklist;
import com.trtm.iot.rebarTaskChecklist.mapper.RebarTaskChecklistMapper;
import com.trtm.iot.rebarTaskChecklist.service.IRebarTaskChecklistService;
import com.trtm.iot.rebarTaskChecklist.entity.TaskCheckVo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @Description: rebar_task_checklist
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
@Service
public class RebarTaskChecklistServiceImpl extends ServiceImpl<RebarTaskChecklistMapper, RebarTaskChecklist> implements IRebarTaskChecklistService {
    @Autowired
    private RebarTaskChecklistMapper rebarTaskChecklistMapper;
    @Autowired
    private RebarComponentTaskMapper rebarComponentTaskMapper;
    @Autowired
    private RebarComponentMapper rebarComponentMapper;

    @Override
    @Transactional
    public boolean addTaskCheck(TaskCheckVo taskCheckVo) {
        //新建一个任务
        RebarTaskChecklist rebarTaskChecklist = new RebarTaskChecklist();

        BeanUtils.copyProperties(taskCheckVo,rebarTaskChecklist);
        QueryWrapper<RebarTaskChecklist> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("limit 1");
        List<RebarTaskChecklist> rebarTaskChecklists = rebarTaskChecklistMapper.selectList(queryWrapper);
        String s = "";
//        for (RebarTaskChecklist taskChecklist : rebarTaskChecklists) {
////            taskId = taskChecklist.getTaskId();
//            s = taskChecklist.getCreateTime().toString();
//            s.replaceAll("-","");
//        }
        DateTime date = DateUtil.date();
        String s1 = date.toString();
        rebarTaskChecklist.setCreateTime(date);
//        s = taskCheckVo.getCreateTime().toString();
        s1.replaceAll("-","");
        RebarTaskChecklist rebarTaskChecklist1 = new RebarTaskChecklist();
        synchronized (rebarTaskChecklist1){
            rebarTaskChecklist1 = rebarTaskChecklist.setTaskId("RW-" + s1);
        }
        //任务对应的构件图片
        List<String> images = taskCheckVo.getImages();
        for (String image : images) {
            rebarTaskChecklist.setImages(image+",");

        }
        //rebarTaskChecklist.setTaskId("RW-"+s);
        int insert = rebarTaskChecklistMapper.insert(rebarTaskChecklist);
        //把构件列表数据添加到任务构件中间表
        List<RebarComponentTaskVo> rebarComponentTaskList = taskCheckVo.getRebarComponentTaskList();
        for (RebarComponentTaskVo componentTask : rebarComponentTaskList) {
            RebarComponentTask rebarComponentTask = new RebarComponentTask();
            rebarComponentTask.setTaskId(rebarTaskChecklist.getTaskId());
            rebarComponentTask.setComponentId(componentTask.getComponentId());
            rebarComponentTask.setComponentNumber(componentTask.getComponentNumber());
            rebarComponentTask.setOrgCode(componentTask.getOrgCode());
            rebarComponentTask.setOrgCodes(taskCheckVo.getOrgCodes());
            rebarComponentTaskMapper.insert(rebarComponentTask);
            componentTask.setTaskId(rebarTaskChecklist1.getTaskId());
            RebarComponent rebarComponent = new RebarComponent();
            rebarComponent.setStatus(1);
            BeanUtils.copyProperties(componentTask,rebarComponent);
            rebarComponentMapper.updateById(rebarComponent);
        }
        return true;
    }

    @Override
    public boolean deleteTaskById(String id) {
        return rebarTaskChecklistMapper.updateTaskById(id);
    }

    @Override
    public boolean updateTaskStatus(String taskId) {
        return rebarTaskChecklistMapper.updateTaskStatus(taskId);
    }

    @Override
    public QueryWrapper<RebarTaskChecklist> getRebarTaskChecklistQueryWrapper(RebarTaskChecklist rebarTaskChecklist, String sys_depart_orgcode, HttpServletRequest req,Integer a) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            rebarTaskChecklist.setOrgCode(sys_depart_orgcode + "*");
        } else {
            rebarTaskChecklist.setOrgCode("*" + sys_depart_orgcode + "*");
        }
//        if (rebarTaskChecklist.getOrgCodes() != null && rebarTaskChecklist.getOrgCodes().length() != 0) {
//            rebarTaskChecklist.setOrgCodes(rebarTaskChecklist.getOrgCodes() + "*");
//        }
//        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
//            rebarTaskChecklist.setOrgCodes(sys_depart_orgcode + "*");
//        } else {
//            rebarTaskChecklist.setOrgCodes("*" + sys_depart_orgcode + "*");
//        }

        QueryWrapper<RebarTaskChecklist> queryWrapper = QueryGenerator.initQueryWrapper(rebarTaskChecklist, req.getParameterMap());
        if (a==1){
            //根据生产完成的状态来分页查询
            String status1 = "0";
            String status2 = "1";
            String status3 = "2";
//            String status4 = "3";
            queryWrapper.and(wrapper -> wrapper.eq("status", status1).or().eq("status", status2).or().eq("status", status3));
        }else if (a==2){
            //根据生产完成的状态来分页查询
            String status1 = "3";
            String status2 = "4";
            String status3 = "5";
            queryWrapper.and(wrapper-> wrapper.eq("status",status1).or().eq("status",status2).or().eq("status",status3));
        }
        //queryWrapper.and(wrapper-> wrapper.eq("status",4).or().eq("status",5));
        return queryWrapper;
    }
}
