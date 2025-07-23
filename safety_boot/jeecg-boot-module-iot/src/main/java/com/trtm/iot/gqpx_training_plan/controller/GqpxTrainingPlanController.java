package com.trtm.iot.gqpx_training_plan.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.gqpx_employee_training.entity.SysUser;
import com.trtm.iot.gqpx_employee_training.service.IGqpxEmployeeTrainingService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import com.trtm.iot.gqpx_training_plan.entity.GqpxTrainingPlan;
import com.trtm.iot.gqpx_training_plan.service.IGqpxTrainingPlanService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 岗前培训计划表
 * @Author: jeecg-boot
 * @Date: 2024-11-26
 * @Version: V1.0
 */
@Api(tags = "岗前培训计划表")
@RestController
@RequestMapping("/gqpx_training_plan/gqpxTrainingPlan")
@Slf4j
public class GqpxTrainingPlanController extends JeecgController<GqpxTrainingPlan, IGqpxTrainingPlanService> {
    @Autowired
    private IGqpxTrainingPlanService gqpxTrainingPlanService;
    @Autowired
    private IGqpxEmployeeTrainingService gqpxEmployeeTrainingService;

    /**
     * 分页列表查询
     *
     * @param gqpxTrainingPlan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "岗前培训计划表-分页列表查询")
    @ApiOperation(value = "岗前培训计划表-分页列表查询", notes = "岗前培训计划表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GqpxTrainingPlan gqpxTrainingPlan,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (gqpxTrainingPlan.getOrgcode() == null) {
            gqpxTrainingPlan.setOrgcode(loginUser.getOrgCode() + "*");
        } else {
            gqpxTrainingPlan.setOrgcode(gqpxTrainingPlan.getOrgcode() + "*");
        }
        QueryWrapper<GqpxTrainingPlan> queryWrapper = QueryGenerator.initQueryWrapper(gqpxTrainingPlan, req.getParameterMap());
        Page<GqpxTrainingPlan> page = new Page<GqpxTrainingPlan>(pageNo, pageSize);
        IPage<GqpxTrainingPlan> pageList = gqpxTrainingPlanService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param gqpxTrainingPlan
     * @return
     */
    @AutoLog(value = "岗前培训计划表-添加")
    @ApiOperation(value = "岗前培训计划表-添加", notes = "岗前培训计划表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GqpxTrainingPlan gqpxTrainingPlan) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        gqpxTrainingPlan.setCreateTime(new java.util.Date());
        gqpxTrainingPlan.setCreatePerson(loginUser.getRealname());
        gqpxTrainingPlan.setGuid(UUID.randomUUID().toString());
        gqpxTrainingPlan.setOrgcode(loginUser.getOrgCode());
        Date sgstartTime = gqpxTrainingPlan.getSgstartTime();
        Date sgendTime = gqpxTrainingPlan.getSgendTime();
        if (sgstartTime != null && sgendTime != null) {
            gqpxTrainingPlan.setExpirationTime(sgstartTime + "-->" + sgendTime);
        }
        gqpxTrainingPlanService.save(gqpxTrainingPlan);
        gqpxEmployeeTrainingService.savejl(gqpxTrainingPlan);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param gqpxTrainingPlan
     * @return
     */
    @AutoLog(value = "岗前培训计划表-添加")
    @ApiOperation(value = "岗前培训计划表-添加", notes = "岗前培训计划表-添加")
    @PostMapping(value = "/add1")
    public Result<?> add1(@RequestBody GqpxTrainingPlan gqpxTrainingPlan) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        gqpxTrainingPlan.setCreateTime(new java.util.Date());
        gqpxTrainingPlan.setCreatePerson(loginUser.getRealname());
        gqpxTrainingPlan.setGuid(UUID.randomUUID().toString());
        String orgCode = loginUser.getOrgCode();
        gqpxTrainingPlan.setOrgcode(orgCode);
        String orgname = gqpxTrainingPlanService.selectProjNames(orgCode);
        gqpxTrainingPlan.setOrgname(orgname);

        String worktype = gqpxTrainingPlan.getWorktype();
        if (worktype != null) {
            String[] split = worktype.split(",");
            HashSet<String> strings = new HashSet<>(Arrays.asList(split));
            gqpxTrainingPlan.setWorktype(String.join(",", strings));
        }
        String banzu = gqpxTrainingPlan.getBanzu();
        if (banzu != null) {
            Set<String> strings1 = new HashSet<>(Arrays.asList(banzu.split(",")));
            gqpxTrainingPlan.setBanzu(String.join(",", strings1));
        }

        Date sgstartTime = gqpxTrainingPlan.getSgstartTime();
        Date sgendTime = gqpxTrainingPlan.getSgendTime();
        if (sgstartTime != null && sgendTime != null) {
            gqpxTrainingPlan.setExpirationTime(sgstartTime + "-->" + sgendTime);
        }

        gqpxTrainingPlanService.save(gqpxTrainingPlan);
        gqpxEmployeeTrainingService.savepxjh(gqpxTrainingPlan);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gqpxTrainingPlan
     * @return
     */
    @AutoLog(value = "岗前培训计划表-编辑")
    @ApiOperation(value = "岗前培训计划表-编辑", notes = "岗前培训计划表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GqpxTrainingPlan gqpxTrainingPlan) {
        Integer updatejl = gqpxEmployeeTrainingService.updatejl(gqpxTrainingPlan);
        if (updatejl == 200) {
            gqpxTrainingPlanService.updateById(gqpxTrainingPlan);
            return Result.OK("编辑成功!");
        } else if (updatejl == 201) {
            return Result.error("编辑失败!培训已进行!");
        } else {
            return Result.error("编辑失败!");
        }
    }

    @AutoLog(value = "岗前培训计划表-审核")
    @ApiOperation(value = "岗前培训计划表-审核", notes = "岗前培训计划表-审核")
    @PutMapping(value = "/shenhe")
    public Result<?> shenhe(@RequestBody GqpxTrainingPlan gqpxTrainingPlan) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String guid = gqpxTrainingPlan.getGuid();
        if (guid == null) {
            return Result.error("guid为空!");
        }
        LambdaQueryWrapper<GqpxTrainingPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GqpxTrainingPlan::getGuid, guid);
        GqpxTrainingPlan gqpxTrainingPlanOne = gqpxTrainingPlanService.getOne(queryWrapper);
        if (gqpxTrainingPlanOne == null) {
            return Result.error("无对应的培训计划!");
        }
        switch (gqpxTrainingPlan.getStatus()) {
            case 0:
                break;
            case 10:
                gqpxTrainingPlanOne.setSupervisorApproval(gqpxTrainingPlan.getSupervisorApproval());
                gqpxTrainingPlanOne.setSupervisorPerson(loginUser.getRealname());
                gqpxTrainingPlanOne.setSupervisorHandleTime(new java.util.Date());
                break;
            case 15:
                gqpxTrainingPlanOne.setSupervisorApproval(gqpxTrainingPlan.getSupervisorApproval());
                gqpxTrainingPlanOne.setSupervisorPerson(loginUser.getRealname());
                gqpxTrainingPlanOne.setSupervisorHandleTime(new java.util.Date());
                break;
            case 20:
                gqpxTrainingPlanOne.setHeadquartersApproval(gqpxTrainingPlan.getHeadquartersApproval());
                gqpxTrainingPlanOne.setHeadquartersPerson(loginUser.getRealname());
                gqpxTrainingPlanOne.setHeadquartersHandleTime(new java.util.Date());
                String lecturer = gqpxTrainingPlan.getLecturer();
                SysUser sysUser = gqpxTrainingPlanService.getUsersByName(lecturer);
                gqpxTrainingPlanOne.setLecturer(lecturer);
                if (sysUser != null) {
                    gqpxTrainingPlanOne.setLocation(sysUser.getDanwei());
                }
                break;
            case 25:
                gqpxTrainingPlanOne.setHeadquartersApproval(gqpxTrainingPlan.getHeadquartersApproval());
                gqpxTrainingPlanOne.setHeadquartersPerson(loginUser.getRealname());
                gqpxTrainingPlanOne.setHeadquartersHandleTime(new java.util.Date());
                gqpxTrainingPlanOne.setLecturer(gqpxTrainingPlan.getLecturer());
                break;
            case 30:
                gqpxTrainingPlanOne.setTrafficPoliceApproval(gqpxTrainingPlan.getTrafficPoliceApproval());
                gqpxTrainingPlanOne.setTrafficPolicePerson(loginUser.getRealname());
                gqpxTrainingPlanOne.setTrafficPoliceHandleTime(new java.util.Date());
                gqpxTrainingPlanOne.setInstructor(gqpxTrainingPlan.getInstructor());
                break;
            case 35:
                gqpxTrainingPlanOne.setTrafficPoliceApproval(gqpxTrainingPlan.getTrafficPoliceApproval());
                gqpxTrainingPlanOne.setTrafficPolicePerson(loginUser.getRealname());
                gqpxTrainingPlanOne.setTrafficPoliceHandleTime(new java.util.Date());
                gqpxTrainingPlanOne.setInstructor(gqpxTrainingPlan.getInstructor());
                break;
            default:
                return Result.error("状态错误!");
        }
        gqpxTrainingPlanOne.setStatus(gqpxTrainingPlan.getStatus());
        gqpxTrainingPlanService.updateById(gqpxTrainingPlanOne);
        return Result.OK("审核成功!");
    }

    @AutoLog(value = "岗前培训计划表-确认")
    @ApiOperation(value = "岗前培训计划表-确认", notes = "岗前培训计划表-确认")
    @PutMapping(value = "/queren")
    public Result<?> queren(@RequestBody GqpxTrainingPlan gqpxTrainingPlan) {
        String guid = gqpxTrainingPlan.getGuid();
        if (guid == null) {
            return Result.error("guid为空!");
        }
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        LambdaQueryWrapper<GqpxTrainingPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GqpxTrainingPlan::getGuid, guid);
        GqpxTrainingPlan gqpxTrainingPlanOne = gqpxTrainingPlanService.getOne(queryWrapper);
        if (gqpxTrainingPlanOne == null) {
            return Result.error("无对应的培训计划!");
        }
        switch (gqpxTrainingPlan.getCompleteStatus()) {
            case 0:
                break;
            case 10:
                gqpxTrainingPlanOne.setSupervisorConfirmApproval(gqpxTrainingPlan.getSupervisorConfirmApproval());
                gqpxTrainingPlanOne.setSupervisorConfirmPerson(loginUser.getRealname());
                gqpxTrainingPlanOne.setSupervisorConfirmTime(new java.util.Date());
                break;
            case 15:
                gqpxTrainingPlanOne.setSupervisorConfirmApproval(gqpxTrainingPlan.getSupervisorConfirmApproval());
                gqpxTrainingPlanOne.setSupervisorConfirmPerson(loginUser.getRealname());
                gqpxTrainingPlanOne.setSupervisorConfirmTime(new java.util.Date());
                break;
            case 20:
                gqpxTrainingPlanOne.setHeadquartersConfirmApproval(gqpxTrainingPlan.getHeadquartersConfirmApproval());
                gqpxTrainingPlanOne.setHeadquartersConfirmPerson(loginUser.getRealname());
                gqpxTrainingPlanOne.setHeadquartersConfirmTime(new java.util.Date());
                break;
            case 25:
                gqpxTrainingPlanOne.setHeadquartersConfirmApproval(gqpxTrainingPlan.getHeadquartersConfirmApproval());
                gqpxTrainingPlanOne.setHeadquartersConfirmPerson(loginUser.getRealname());
                gqpxTrainingPlanOne.setHeadquartersConfirmTime(new java.util.Date());
                break;
//            case 30:
//                gqpxTrainingPlanOne.setOperationsConfirmApproval(gqpxTrainingPlan.getOperationsConfirmApproval());
//                gqpxTrainingPlanOne.setOperationsConfirmPerson(loginUser.getRealname());
//                gqpxTrainingPlanOne.setOperationsConfirmTime(new java.util.Date());
//                break;
//            case 35:
//                gqpxTrainingPlanOne.setOperationsConfirmApproval(gqpxTrainingPlan.getOperationsConfirmApproval());
//                gqpxTrainingPlanOne.setOperationsConfirmPerson(loginUser.getRealname());
//                gqpxTrainingPlanOne.setOperationsConfirmTime(new java.util.Date());
//                break;
            case 30:
                gqpxTrainingPlanOne.setTrafficPoliceConfirmApproval(gqpxTrainingPlan.getTrafficPoliceConfirmApproval());
                gqpxTrainingPlanOne.setTrafficPoliceConfirmPerson(loginUser.getRealname());
                gqpxTrainingPlanOne.setTrafficPoliceConfirmTime(new java.util.Date());
                break;
            case 35:
                gqpxTrainingPlanOne.setTrafficPoliceConfirmApproval(gqpxTrainingPlan.getTrafficPoliceConfirmApproval());
                gqpxTrainingPlanOne.setTrafficPoliceConfirmPerson(loginUser.getRealname());
                gqpxTrainingPlanOne.setTrafficPoliceConfirmTime(new java.util.Date());
                break;
            default:
                return Result.error("状态错误!");
        }
        gqpxTrainingPlanOne.setCompleteStatus(gqpxTrainingPlan.getCompleteStatus());
        gqpxTrainingPlanService.updateById(gqpxTrainingPlanOne);
        return Result.OK("确认成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "岗前培训计划表-通过id删除")
    @ApiOperation(value = "岗前培训计划表-通过id删除", notes = "岗前培训计划表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        gqpxTrainingPlanService.removeById(id);
        gqpxEmployeeTrainingService.deletejl(id);
        return Result.OK("删除成功!");
    }

    @AutoLog(value = "岗前培训计划表-通过id删除")
    @ApiOperation(value = "岗前培训计划表-通过id删除", notes = "岗前培训计划表-通过id删除")
    @DeleteMapping(value = "/getPerson")
    public Result<?> getPerson(@RequestParam(name = "personType", required = true) String personType) {


        return Result.OK("!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "岗前培训计划表-批量删除")
    @ApiOperation(value = "岗前培训计划表-批量删除", notes = "岗前培训计划表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gqpxTrainingPlanService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "岗前培训计划表-通过id查询")
    @ApiOperation(value = "岗前培训计划表-通过id查询", notes = "岗前培训计划表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GqpxTrainingPlan gqpxTrainingPlan = gqpxTrainingPlanService.getById(id);
        if (gqpxTrainingPlan == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gqpxTrainingPlan);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gqpxTrainingPlan
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GqpxTrainingPlan gqpxTrainingPlan) {
        return super.exportXls(request, gqpxTrainingPlan, GqpxTrainingPlan.class, "岗前培训计划表");
    }

    /**
     * 打印
     *
     * @param request
     * @param gqpxTrainingPlan
     */
    @RequestMapping(value = "/export")
    public Result<?> export(HttpServletRequest request, GqpxTrainingPlan gqpxTrainingPlan, Integer id) {
        List data = new ArrayList<>();
        LambdaQueryWrapper<GqpxTrainingPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GqpxTrainingPlan::getId, id);
        GqpxTrainingPlan one = gqpxTrainingPlanService.getOne(queryWrapper);
        data.add(one);
        return Result.OKs(data);
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GqpxTrainingPlan.class);
    }

}
