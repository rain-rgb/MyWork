package com.trtm.iot.renwudan.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.renwudan.entity.RenwudanSchedule;
import com.trtm.iot.renwudan.service.IRenwudanScheduleService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 拌合站任务单进度
 * @Author: jeecg-boot
 * @Date: 2021-06-16
 * @Version: V1.0
 */
@Api(tags = "拌合站任务单进度")
@RestController
@RequestMapping("/renwudan/renwudanSchedule")
@Slf4j
public class RenwudanScheduleController extends JeecgController<RenwudanSchedule, IRenwudanScheduleService> {
    @Autowired
    private IRenwudanScheduleService renwudanScheduleService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param renwudanSchedule
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站任务单进度-分页列表查询")
    @ApiOperation(value = "拌合站任务单进度-分页列表查询", notes = "拌合站任务单进度-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(RenwudanSchedule renwudanSchedule,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, String sys_depart_project) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (renwudanSchedule.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                renwudanSchedule.setShebeiNo(shebei);
            } else {
                renwudanSchedule.setShebeiNo("空");
            }
        }
        if (sys_depart_project != null && sys_depart_project.length() != 0) {  //如果想要全局分部分项控制所显示的数据要加上这个
            renwudanSchedule.setSysDepartProject("*" + sys_depart_project + "*");
        }
        QueryWrapper<RenwudanSchedule> queryWrapper = QueryGenerator.initQueryWrapper(renwudanSchedule, req.getParameterMap());
        Page<RenwudanSchedule> page = new Page<RenwudanSchedule>(pageNo, pageSize);
        IPage<RenwudanSchedule> pageList = renwudanScheduleService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param renwudanSchedule
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站任务单进度-分页列表查询")
    @ApiOperation(value = "拌合站任务单进度-分页列表查询", notes = "拌合站任务单进度-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(RenwudanSchedule renwudanSchedule,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, String sys_depart_project) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (renwudanSchedule.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                renwudanSchedule.setShebeiNo(shebei);
            } else {
                renwudanSchedule.setShebeiNo("空");
            }
        }
        if (sys_depart_project != null && sys_depart_project.length() != 0) {  //如果想要全局分部分项控制所显示的数据要加上这个
            renwudanSchedule.setSysDepartProject(sys_depart_project + "*");
        }
        QueryWrapper<RenwudanSchedule> queryWrapper = QueryGenerator.initQueryWrapper(renwudanSchedule, req.getParameterMap());
        queryWrapper.select("Projectname,BetLev,Mete,SUM(Metes) AS Metes,SUM(bfb) AS bfb,code,conspos,endtim,id,isdel,lands,pour,starttim,station");
        queryWrapper.groupBy("Code");
        queryWrapper.orderByDesc("id");
        queryWrapper.last("LIMIT 10 ");
        List<RenwudanSchedule> pageList = renwudanScheduleService.list(queryWrapper);
        for (RenwudanSchedule schedule : pageList) {
            Double bfb = schedule.getBfb();
            if (bfb > 100.0) {
                schedule.setBfb(100.0);
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param renwudanSchedule
     * @return
     */
    @AutoLog(value = "拌合站任务单进度-添加")
    @ApiOperation(value = "拌合站任务单进度-添加", notes = "拌合站任务单进度-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody RenwudanSchedule renwudanSchedule) {
        renwudanScheduleService.save(renwudanSchedule);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param renwudanSchedule
     * @return
     */
    @AutoLog(value = "拌合站任务单进度-编辑")
    @ApiOperation(value = "拌合站任务单进度-编辑", notes = "拌合站任务单进度-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody RenwudanSchedule renwudanSchedule) {
        renwudanScheduleService.updateById(renwudanSchedule);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "拌合站任务单进度-通过id删除")
    @ApiOperation(value = "拌合站任务单进度-通过id删除", notes = "拌合站任务单进度-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        renwudanScheduleService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "拌合站任务单进度-批量删除")
    @ApiOperation(value = "拌合站任务单进度-批量删除", notes = "拌合站任务单进度-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.renwudanScheduleService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "拌合站任务单进度-通过id查询")
    @ApiOperation(value = "拌合站任务单进度-通过id查询", notes = "拌合站任务单进度-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        RenwudanSchedule renwudanSchedule = renwudanScheduleService.getById(id);
        if (renwudanSchedule == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(renwudanSchedule);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param renwudanSchedule
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RenwudanSchedule renwudanSchedule) {
        return super.exportXls(request, renwudanSchedule, RenwudanSchedule.class, "拌合站任务单进度");
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
        return super.importExcel(request, response, RenwudanSchedule.class);
    }
    /**
     * 分页列表查询
     *
     * @param renwudanSchedule
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站任务单进度-分页列表查询")
    @ApiOperation(value = "拌合站任务单进度-分页列表查询", notes = "拌合站任务单进度-分页列表查询")
    @GetMapping(value = "/listHome")
    public Result<?> listHome(RenwudanSchedule renwudanSchedule,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, String sys_depart_project,Integer type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String starttim_begin = null;
        String starttim_end = sdf.format(calendar.getTime());
        if (type == 1) {
            calendar.set(Calendar.DAY_OF_YEAR, 1);//1:本年第一天
            starttim_begin = sdf.format(calendar.getTime());
        } else if (type == 2) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
            starttim_begin = sdf.format(calendar.getTime());
        }
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (renwudanSchedule.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                renwudanSchedule.setShebeiNo(shebei);
            } else {
                renwudanSchedule.setShebeiNo("空");
            }
        }
        if (sys_depart_project != null && sys_depart_project.length() != 0) {  //如果想要全局分部分项控制所显示的数据要加上这个
            renwudanSchedule.setSysDepartProject("*" + sys_depart_project + "*");
        }
        QueryWrapper<RenwudanSchedule> queryWrapper = QueryGenerator.initQueryWrapper(renwudanSchedule, req.getParameterMap());
        if(starttim_begin!=null){
            queryWrapper.ge("starttim",starttim_begin);
            queryWrapper.le("starttim",starttim_end);

        }
        Page<RenwudanSchedule> page = new Page<RenwudanSchedule>(pageNo, pageSize);
        IPage<RenwudanSchedule> pageList = renwudanScheduleService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
}
