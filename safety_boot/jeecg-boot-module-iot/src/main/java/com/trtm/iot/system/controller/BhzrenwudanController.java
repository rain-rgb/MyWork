package com.trtm.iot.system.controller;


import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.trtm.iot.car.entity.SchedulingCar;
import com.trtm.iot.car.service.ISchedulingCarService;
import com.trtm.iot.renwudan.entity.RenwudanSchedule;
import com.trtm.iot.renwudan.service.IRenwudanScheduleService;
import com.trtm.iot.system.entity.*;

import com.trtm.iot.system.mapper.ShebeiInfoMapper;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.system.service.IShigongphbService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import com.trtm.iot.system.service.IBhzrenwudanService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 任务单浇筑令
 * @Author: jeecg-boot
 * @Date: 2021-05-20
 * @Version: V1.0
 */
@Api(tags = "任务单浇筑令")
@RestController
@RequestMapping("/system/bhzrenwudan")
@Slf4j
public class BhzrenwudanController extends JeecgController<Bhzrenwudan, IBhzrenwudanService> {
    @Autowired
    private IBhzrenwudanService taskService;
    @Autowired
    private IRenwudanScheduleService scheduleService;
    @Autowired
    private ISchedulingCarService schedulingCarService;
    @Autowired
    private IShigongphbService shigongphbService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    /**
     * 分页列表查询
     *
     * @param bhzrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */

    @AutoLog(value = "任务单浇筑令-分页列表查询")
    @ApiOperation(value = "任务单浇筑令-分页列表查询", notes = "任务单浇筑令-分页列表查询")
    @GetMapping(value = "/list0")
    public Result<?> queryPageList0(Bhzrenwudan bhzrenwudan,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudan, req.getParameterMap());
        Page<Bhzrenwudan> page = new Page<>(pageNo, pageSize);
        IPage<Bhzrenwudan> pageList = taskService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    @AutoLog(value = "任务单浇筑令-分页列表查询")
    @ApiOperation(value = "任务单浇筑令-分页列表查询", notes = "任务单浇筑令-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "system/BhzrenwudanList")
    public Result<?> queryPageList(Bhzrenwudan bhzrenwudan,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息

        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }else{
            bhzrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        bhzrenwudan.setCode("*" + bhzrenwudan.getCode() + "*");
        bhzrenwudan.setIsdel(0);//查询未删除的数据
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudan, req.getParameterMap());
        Page<Bhzrenwudan> page = new Page<>(pageNo, pageSize);
        IPage<Bhzrenwudan> pageList = taskService.page(page, queryWrapper);
//        StatisticsAndPageVo sybase = taskService.getSybase(page, queryWrapper);
        return Result.OK(pageList);
    }

//    /**
//     * 分页列表查询
//     *
//     * @param bhzrenwudan
//     * @param pageNo
//     * @param pageSize
//     * @param req
//     * @return
//     */
//    @AutoLog(value = "浇筑令-分页列表查询")
//    @ApiOperation(value = "浇筑令-分页列表查询", notes = "智慧拌合")
//    @GetMapping(value = "/taskPageList")
//    @PermissionData(pageComponent = "system/BhzrenwudanList")
//    public Result<?> taskPageList(Bhzrenwudan bhzrenwudan,
//                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
//                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
//                                  String startTime,
//                                  String endTime,
//                                  HttpServletRequest req) {
//        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
//            bhzrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
//        }
//        if (bhzrenwudan.getConspos() != null && !"" .equals(bhzrenwudan.getConspos())) {
//            bhzrenwudan.setConspos("*" + bhzrenwudan.getConspos() + "*");
//        }
//        bhzrenwudan.setCode("*" + bhzrenwudan.getCode() + "*");
//        bhzrenwudan.setIsdel(0);//查询未删除的数据
//        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudan, req.getParameterMap());
//        queryWrapper.ge(startTime != null && endTime != null, "BegTim", startTime)
//                .le(startTime != null && endTime != null, "BegTim", endTime);
//        Page<Bhzrenwudan> page = new Page<>(pageNo, pageSize);
//        Page<Bhzrenwudan> sybase = taskService.getSybase(page, queryWrapper);
//        return Result.OK(sybase);
//    }

    /**
     * 浇筑令统计
     *
     * @param orgCode 组织机构id
     * @return
     */
    @AutoLog(value = "浇筑令统计")
    @ApiOperation(value = "浇筑令统计", notes = "智慧拌合")
    @GetMapping(value = "/taskSta")
    @PermissionData(pageComponent = "system/BhzrenwudanList")
    public Result<?> taskSta(@RequestParam("orgCode") String orgCode) {
        if (orgCode != null) {
            orgCode = orgCode + "%";
        }
        StatisticsAndPageVo taskSta = taskService.getTaskSta(orgCode);
        return Result.OK(taskSta);
    }

    /**
     * 浇筑令统计
     *
     * @param orgCode 组织机构id
     * @return
     */
    @AutoLog(value = "浇筑令统计")
    @ApiOperation(value = "浇筑令统计", notes = "智慧拌合")
    @GetMapping(value = "/taskSta3")
    public Result<?> taskSta3(@RequestParam("orgCode") String orgCode) {
        if (StringUtils.isBlank(orgCode)) {
            orgCode = "A05";
        }
        QueryWrapper<Bhzrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.select( " `status`, count(1) as iszjzl ");
        queryWrapper.likeRight("sys_org_code",orgCode);
        queryWrapper.groupBy("`status`");
        List<Bhzrenwudan> list = taskService.list(queryWrapper);

        return Result.OK(list);
    }


    /**
     * 浇筑令统计
     *
     * @param orgCode 组织机构id
     * @return
     */
    @AutoLog(value = "浇筑令统计")
    @ApiOperation(value = "浇筑令统计", notes = "智慧拌合")
    @GetMapping(value = "/taskSta2")
    @PermissionData(pageComponent = "system/BhzrenwudanList")
    public Result<?> taskSta2(@RequestParam("orgCode") String orgCode,
                              String conspos,
                              String dattim_begin,
                              String dattim_end
    ) {
        if (orgCode != null) {
            orgCode = orgCode + "%";
        }
        if (conspos != null) {
            conspos = "%" + conspos + "%";
        }
        if (dattim_begin.equals("")) {
            dattim_begin = null;
        }
        if (dattim_end.equals("")) {
            dattim_end = null;
        }
        StatisticsAndPageVo taskSta = taskService.getTaskSta2(orgCode, conspos, dattim_begin, dattim_end);
        return Result.OK(taskSta);
    }


    /**
     * 分页列表查询
     *
     * @param bhzrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单浇筑令-分页列表查询")
    @ApiOperation(value = "任务单浇筑令-分页列表查询", notes = "任务单浇筑令-分页列表查询")
    @GetMapping(value = "/list2")
    @PermissionData(pageComponent = "system/BhzrenwudanList")
    public Result<?> queryPageList2(Bhzrenwudan bhzrenwudan,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        bhzrenwudan.setCode("*" + bhzrenwudan.getCode() + "*");
        bhzrenwudan.setIsdel(0);//查询未删除的数据
        bhzrenwudan.setStatus(0);
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudan, req.getParameterMap());
        Page<Bhzrenwudan> page = new Page<>(pageNo, pageSize);
        IPage<Bhzrenwudan> pageList = taskService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "任务单浇筑令-配比单号查询")
    @ApiOperation(value = "任务单浇筑令-配比单号查询", notes = "任务单浇筑令-配比单号查询")
    @GetMapping(value = "/peibihaolist")
    public Result<?> queryPageList1(Bhzrenwudan bhzrenwudan,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            bhzrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
//		 bhzrenwudan.setCode("*" + bhzrenwudan.getCode() + "*");
        bhzrenwudan.setIsdel(0);
        bhzrenwudan.setStatus(1);
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudan, req.getParameterMap());
        queryWrapper.select("DISTINCT(Code) as code,DatTim,Attribute,Recipe,Recipes,ProjName,ConsPos,Pour,Variety,BetLev,Filter,Freeze,Lands,Cement,Stone,BnSize,MixLast,Mete,BegTim,EndTim,Attamper,isdel,status,sys_org_code,Station");
        queryWrapper.orderByDesc("id");
        List<Bhzrenwudan> pageList = taskService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param bhzrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单浇筑令-分页列表查询")
    @ApiOperation(value = "任务单浇筑令-分页列表查询", notes = "任务单浇筑令-分页列表查询")
    @GetMapping(value = "/rwdtoday")
    public Result<?> rwdtoday(Bhzrenwudan bhzrenwudan,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                              String shebeiNo,
                              HttpServletRequest req) {
        List<Map> toDayrwdInfo = taskService.getToDayrwdInfo(sys_depart_orgcode, shebeiNo);
        return Result.OK(toDayrwdInfo);
    }


    @AutoLog(value = "任务单浇筑令-分页列表查询")
    @ApiOperation(value = "任务单浇筑令-分页列表查询", notes = "任务单浇筑令-分页列表查询")
    @GetMapping(value = "/bhzrwdlist")
    public Result<?> queryPagebhzrwdList(Bhzrenwudan bhzrenwudan,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                         HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        bhzrenwudan.setConspos("*" + bhzrenwudan.getConspos() + "*");
        bhzrenwudan.setIsdel(0);//查询未删除的数据
        if (StringUtils.isBlank(bhzrenwudan.getCode())) {
            bhzrenwudan.setCode("RWD-" + "*");
        }
        bhzrenwudan.setStatus(1);
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudan, req.getParameterMap());
        Page<Bhzrenwudan> page = new Page<>(pageNo, pageSize);
        IPage<Bhzrenwudan> pageList = taskService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 分页列表查询
     *
     * @param bhzrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单浇筑令-分页列表查询")
    @ApiOperation(value = "任务单浇筑令-分页列表查询", notes = "任务单浇筑令-分页列表查询")
    @GetMapping(value = "/bhzrwdlists")
    public Result<?> queryPagebhzrwdLists(Bhzrenwudan bhzrenwudan,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                          HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
//        bhzrenwudan.setCode("*" + bhzrenwudan.getCode() + "*");
        bhzrenwudan.setConspos("*" + bhzrenwudan.getConspos() + "*");
        bhzrenwudan.setIsdel(0);//查询未删除的数据
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudan, req.getParameterMap());
        queryWrapper.likeRight("code", "RWD-");
        queryWrapper.eq("status", 1);
        List<Bhzrenwudan> pageList = taskService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 列表查询
     *
     * @param bhzrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单浇筑令-列表查询")
    @ApiOperation(value = "任务单浇筑令-列表查询", notes = "任务单浇筑令-列表查询")
    @GetMapping(value = "/bhzrwdlistss")
    public Result<?> queryPagebhzrwdListss(Bhzrenwudan bhzrenwudan,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                           HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            bhzrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        bhzrenwudan.setIsdel(0);//查询未删除的数据
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudan, req.getParameterMap());
        queryWrapper.likeRight("code", "RWD-");
//        queryWrapper.eq("status", 1);
        queryWrapper.eq("isfinish", 0);
        queryWrapper.orderByDesc("id");
        List<Bhzrenwudan> pageList = taskService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 列表查询(过滤RWD-开头的任务单编号)
     *
     * @param bhzrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单浇筑令-列表查询(过滤RRWD-开头的任务单编号)")
    @ApiOperation(value = "任务单浇筑令-列表查询", notes = "任务单浇筑令-列表查询(过滤RRWD-开头的任务单编号)")
    @GetMapping(value = "/bhzrwdlistss1")
    public Result<?> queryPagebhzrwdListss1(Bhzrenwudan bhzrenwudan,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                            HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            bhzrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        bhzrenwudan.setIsdel(0);//查询未删除的数据
        QueryWrapper<Bhzrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(bhzrenwudan, req.getParameterMap());
        queryWrapper.select("DISTINCT(Code) Code");
        queryWrapper.notLike("code", "RWD-");
        queryWrapper.eq("status", 1);
        queryWrapper.eq("isfinish", 0);
        queryWrapper.orderByDesc("id");
        List<Bhzrenwudan> pageList = taskService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *  任务单添加与编辑详见 SysDepartprojectController
     */

    /**
     * 添加
     *
     * @param bhzrenwudan
     * @return
     */
    @AutoLog(value = "任务单浇筑令-添加")
    @ApiOperation(value = "任务单浇筑令-添加", notes = "任务单浇筑令-添加")
    @PostMapping(value = "/add")//防止任务单修改提交两次数据
    public Result<?> add(@RequestBody Bhzrenwudan bhzrenwudan) {
        if (bhzrenwudan.getCode() == null)
            return Result.error("任务单号不能为空");
        if (bhzrenwudan.getSysOrgCode() == null)
            return Result.error("组织机构代码不能为空");
        QueryWrapper<Bhzrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", bhzrenwudan.getCode());
        queryWrapper.eq("station", bhzrenwudan.getStation());
        queryWrapper.eq("sys_org_code", bhzrenwudan.getSysOrgCode());
        List<Bhzrenwudan> bhzrenwudanList = taskService.list(queryWrapper);
        if (bhzrenwudanList.size()>0) {
            for (Bhzrenwudan bhzrenwudan1 : bhzrenwudanList) {
                bhzrenwudan.setId(bhzrenwudan1.getId());
                taskService.updateById(bhzrenwudan);
            }
        } else {
            taskService.save(bhzrenwudan);
        }
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param bhzrenwudan
     * @return
     */
    @AutoLog(value = "任务单浇筑令-添加")
    @ApiOperation(value = "任务单浇筑令-添加", notes = "任务单浇筑令-添加")
    @PostMapping(value = "/addOpen")//对外开放接口
    public Result<?> addOpen(@RequestBody Bhzrenwudan bhzrenwudan) {
        QueryWrapper<Bhzrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", bhzrenwudan.getCode());
        queryWrapper.eq("station", bhzrenwudan.getStation());
        Bhzrenwudan one = taskService.getOne(queryWrapper);
        if (one != null) {
            bhzrenwudan.setId(one.getId());
            taskService.updateById(bhzrenwudan);
        } else {
            taskService.save(bhzrenwudan);
        }
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bhzrenwudan
     * @return
     */
    @AutoLog(value = "任务单浇筑令-编辑")
    @ApiOperation(value = "任务单浇筑令-编辑", notes = "任务单浇筑令-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Bhzrenwudan bhzrenwudan) {
        if (bhzrenwudan.getStatus() == 1) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            bhzrenwudan.setShren(loginUser.getUsername());
            bhzrenwudan.setShshijian(new Date());
        }
        taskService.updateById(bhzrenwudan);
        return Result.OK("编辑成功!");

    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "任务单浇筑令-通过id删除")
    @ApiOperation(value = "任务单浇筑令-通过id删除", notes = "任务单浇筑令-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) Integer id, Bhzrenwudan bhzrenwudan) {
        //taskService.removeById(id);
        LambdaQueryWrapper<Bhzrenwudan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bhzrenwudan::getId, id);
        Bhzrenwudan one = taskService.getOne(queryWrapper);
        Integer status = one.getStatus();
        if (status!=0){
            return Result.error("当前状态不可删除！");
        }
        one.setIsdel(1);//删除方法是给isdel 加标记
        taskService.updateById(one);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "任务单浇筑令-批量删除")
    @ApiOperation(value = "任务单浇筑令-批量删除", notes = "任务单浇筑令-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.taskService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "任务单浇筑令-通过id查询")
    @ApiOperation(value = "任务单浇筑令-通过id查询", notes = "任务单浇筑令-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) Integer id) {
        Bhzrenwudan bhzrenwudan = taskService.getById(id);
        if (bhzrenwudan == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzrenwudan);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzrenwudan
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Bhzrenwudan bhzrenwudan) {
        return super.exportXls(request, bhzrenwudan, Bhzrenwudan.class, "任务单浇筑令");
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
        return super.importExcel(request, response, Bhzrenwudan.class);
    }

    @AutoLog(value = "浇筑令查询详情")
    @ApiOperation(value = "浇筑令查询详情", notes = "浇筑令查询详情")
    @GetMapping(value = "/getDetail")
    @PermissionData(pageComponent = "system/BhzrenwudanList")
    public Result<?> getDetail(@RequestParam("Code") String code, String orgCode) {
        QueryWrapper<Bhzrenwudan> queryWrapper = new QueryWrapper<>();
        //如果想要全局组织机构控制所显示的数据要加上这个
        queryWrapper.like(orgCode != null && !"".equals(orgCode), "sys_org_code", orgCode);
        queryWrapper.eq("Code", code);
        List<Bhzrenwudan> list = taskService.getList(queryWrapper);
        Bhzrenwudan task = list.get(0);
        List<Shigongphb> cSList = taskService.getChargerSheetList(task);
        List<RenwudanSchedule> tS = taskService.getTaskSchedule(task);
        task.setCSList(cSList);
        task.setTS(tS);
        return Result.OK(task);
    }

}
