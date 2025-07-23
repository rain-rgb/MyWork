package com.trtm.iot.entityprogresscheck.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.entityprogresscheck.entity.EntityCheckDetial;
import com.trtm.iot.entityprogresscheck.entity.EntityProgresscheck;
import com.trtm.iot.entityprogresscheck.vo.EntityProgresscheckPage;
import com.trtm.iot.entityprogresscheck.service.IEntityProgresscheckService;
import com.trtm.iot.entityprogresscheck.service.IEntityCheckDetialService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 实体进度清单数据表
 * @Author: jeecg-boot
 * @Date: 2022-07-01
 * @Version: V1.0
 */
@Api(tags = "实体进度清单数据表")
@RestController
@RequestMapping("/entityprogresscheck/entityProgresscheck")
@Slf4j
public class EntityProgresscheckController {
    @Autowired
    private IEntityProgresscheckService entityProgresscheckService;
    @Autowired
    private IEntityCheckDetialService entityCheckDetialService;

    /**
     * 分页列表查询
     *
     * @param entityProgresscheck
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "实体进度清单数据表-分页列表查询")
    @ApiOperation(value = "实体进度清单数据表-分页列表查询", notes = "实体进度清单数据表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(EntityProgresscheck entityProgresscheck,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            entityProgresscheck.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            entityProgresscheck.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        if (entityProgresscheck.getUnitProject() != null) {
            entityProgresscheck.setUnitProject("*" + entityProgresscheck.getUnitProject() + "*");
        }
        QueryWrapper<EntityProgresscheck> queryWrapper = QueryGenerator.initQueryWrapper(entityProgresscheck, req.getParameterMap());
        Page<EntityProgresscheck> page = new Page<EntityProgresscheck>(pageNo, pageSize);
        IPage<EntityProgresscheck> pageList = entityProgresscheckService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 实体进度统计
     *
     * @param entityProgresscheck
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "实体进度清单数据表-实体进度统计")
    @ApiOperation(value = "实体进度清单数据表-实体进度统计", notes = "实体进度清单数据表-实体进度统计")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(EntityProgresscheck entityProgresscheck,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req, Integer date) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            entityProgresscheck.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            entityProgresscheck.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        if (entityProgresscheck.getUnitProject() != null) {
            entityProgresscheck.setUnitProject("*" + entityProgresscheck.getUnitProject() + "*");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String format1 = format.format(new Date());
        QueryWrapper<EntityProgresscheck> queryWrapper = QueryGenerator.initQueryWrapper(entityProgresscheck, req.getParameterMap());
        List<EntityProgresscheck> pageList = entityProgresscheckService.list(queryWrapper);
        List<String> list = new ArrayList<>();
        if (pageList.size() > 0) {
            for (EntityProgresscheck entityProgresscheck1 : pageList) {
                list.add(entityProgresscheck1.getUuid());
            }
        }
        QueryWrapper<EntityCheckDetial> queryWrapper1 = new QueryWrapper<>();
        if (list.size() > 0) {
            queryWrapper1.in("uuid", list);
        } else {
            queryWrapper1.eq("uuid", "空");
        }
        if (date == 0) {//本年
            queryWrapper1.select("sum(plan_amount) plan_amount,sum(planned_amount) planned_amount,DATE_FORMAT(time,'%Y') time");
            queryWrapper1.last("and time like '" + format1 + "%' GROUP BY (SELECT DATE_FORMAT(time,'%Y')) ");
        } else if (date == 1) {//按月
            queryWrapper1.select("sum(plan_amount) plan_amount,sum(planned_amount) planned_amount,DATE_FORMAT(time,'%m') time");
            queryWrapper1.last("and time like '" + format1 + "%' GROUP BY (SELECT DATE_FORMAT(time,'%Y-%m')) ");
        } else if (date == 3) {//按季
            queryWrapper1.select("sum(plan_amount) plan_amount,sum(planned_amount) planned_amount,FLOOR((DATE_FORMAT(time,'%m')-1)/3)+1 time");
            queryWrapper1.last("and time like '" + format1 + "%' GROUP BY (SELECT FLOOR((DATE_FORMAT(time,'%m')-1)/3)+1)");
        }
        List<Map<String, Object>> list1 = entityCheckDetialService.listMaps(queryWrapper1);
        return Result.OK(list1);
    }

    /**
     * 本季度关键节点进度统计（各个单位工程）
     *
     * @param entityProgresscheck
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "实体进度清单数据表-本季度关键节点进度统计（各个单位工程）")
    @ApiOperation(value = "实体进度清单数据表-本季度关键节点进度统计（各个单位工程）", notes = "实体进度清单数据表-本季度关键节点进度统计（各个单位工程）")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(EntityProgresscheck entityProgresscheck,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req, Integer date) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            entityProgresscheck.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            entityProgresscheck.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        if (entityProgresscheck.getUnitProject() != null) {
            entityProgresscheck.setUnitProject("*" + entityProgresscheck.getUnitProject() + "*");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String format1 = format.format(new Date());
        QueryWrapper<EntityProgresscheck> queryWrapper = QueryGenerator.initQueryWrapper(entityProgresscheck, req.getParameterMap());
        queryWrapper.select("unit_project");
        queryWrapper.groupBy("unit_project");
        List<EntityProgresscheck> pageList = entityProgresscheckService.list(queryWrapper);
        List<Map<String, Object>> list1 = new ArrayList<>();
        if (pageList.size() > 0) {
            for (EntityProgresscheck entityProgresscheck1 : pageList) {
                Map<String, Object> map = new HashMap<>();
                map.put("unitProject",entityProgresscheck1.getUnitProject());
                List<Map<String, Object>> list2 = new ArrayList<>();
                QueryWrapper<EntityProgresscheck> queryWrapper3 = QueryGenerator.initQueryWrapper(entityProgresscheck, req.getParameterMap());
                queryWrapper3.eq("unit_project", entityProgresscheck1.getUnitProject());
                queryWrapper3.eq("iskey", 1);
                queryWrapper3.groupBy("project_type");
                List<EntityProgresscheck> pageLists = entityProgresscheckService.list(queryWrapper3);
                if (pageLists.size() > 0) {
                    for (EntityProgresscheck entityProgresscheck2 : pageLists) {
                        QueryWrapper<EntityProgresscheck> queryWrapper4 = QueryGenerator.initQueryWrapper(entityProgresscheck, req.getParameterMap());
                        queryWrapper4.eq("unit_project", entityProgresscheck2.getUnitProject());
                        queryWrapper4.eq("project_type", entityProgresscheck2.getProjectType());
                        List<EntityProgresscheck> pageList1 = entityProgresscheckService.list(queryWrapper4);
                        List<String> list = new ArrayList<>();
                        if (pageList1.size() > 0) {
                            for (EntityProgresscheck entityProgresscheck3 : pageList1) {
                                list.add(entityProgresscheck3.getUuid());
                            }
                        }
                        QueryWrapper<EntityCheckDetial> queryWrapper1 = new QueryWrapper<>();
                        queryWrapper1.select("sum(plan_amount) plan_amount,sum(planned_amount) planned_amount");
                        if (list.size() > 0) {
                            queryWrapper1.in("uuid", list);
                        } else {
                            queryWrapper1.eq("uuid", "空");
                        }
                        queryWrapper1.last("and time like '" + format1 + "%' and QUARTER(time) = QUARTER(now())");
                        EntityCheckDetial one = entityCheckDetialService.getOne(queryWrapper1);
                        double planAmount = 0.0;
                        double plannedAmount = 0.0;
                        double planProgress = 0.0;
                        if (one != null) {
                            if (one.getPlannedAmount() != null) {
                                plannedAmount = Double.parseDouble(one.getPlannedAmount());
                            }
                            if (one.getPlanAmount() != null) {
                                planAmount = Double.parseDouble(one.getPlanAmount());
                            }
                        }
                        if (planAmount != 0) {
                            planProgress = plannedAmount / planAmount * 100;
                        }
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("projectType", entityProgresscheck2.getProjectType());
                        map1.put("planProgress", String.format("%.2f", planProgress));
                        list2.add(map1);
                    }
                }
                map.put("data",list2);
                list1.add(map);
            }
        }
        return Result.OK(list1);
    }


    /**
     * 添加
     *
     * @param entityProgresscheckPage
     * @return
     */
    @AutoLog(value = "实体进度清单数据表-添加")
    @ApiOperation(value = "实体进度清单数据表-添加", notes = "实体进度清单数据表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody EntityProgresscheckPage entityProgresscheckPage) {
        EntityProgresscheck entityProgresscheck = new EntityProgresscheck();
        entityProgresscheckPage.setUuid(UUID.randomUUID().toString());
        BeanUtils.copyProperties(entityProgresscheckPage, entityProgresscheck);
        entityProgresscheckService.saveMain(entityProgresscheck, entityProgresscheckPage.getEntityCheckDetialList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param entityProgresscheckPage
     * @return
     */
    @AutoLog(value = "实体进度清单数据表-编辑")
    @ApiOperation(value = "实体进度清单数据表-编辑", notes = "实体进度清单数据表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody EntityProgresscheckPage entityProgresscheckPage) {
        EntityProgresscheck entityProgresscheck = new EntityProgresscheck();
        BeanUtils.copyProperties(entityProgresscheckPage, entityProgresscheck);
        EntityProgresscheck entityProgresscheckEntity = entityProgresscheckService.getById(entityProgresscheck.getId());
        if (entityProgresscheckEntity == null) {
            return Result.error("未找到对应数据");
        }
        entityProgresscheckService.updateMain(entityProgresscheck, entityProgresscheckPage.getEntityCheckDetialList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "实体进度清单数据表-通过id删除")
    @ApiOperation(value = "实体进度清单数据表-通过id删除", notes = "实体进度清单数据表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        entityProgresscheckService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "实体进度清单数据表-批量删除")
    @ApiOperation(value = "实体进度清单数据表-批量删除", notes = "实体进度清单数据表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.entityProgresscheckService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "实体进度清单数据表-通过id查询")
    @ApiOperation(value = "实体进度清单数据表-通过id查询", notes = "实体进度清单数据表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        EntityProgresscheck entityProgresscheck = entityProgresscheckService.getById(id);
        if (entityProgresscheck == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(entityProgresscheck);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "实体进度清单子表通过主表ID查询")
    @ApiOperation(value = "实体进度清单子表主表ID查询", notes = "实体进度清单子表-通主表ID查询")
    @GetMapping(value = "/queryEntityCheckDetialByMainId")
    public Result<?> queryEntityCheckDetialListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<EntityCheckDetial> entityCheckDetialList = entityCheckDetialService.selectByMainId(id);
        return Result.OK(entityCheckDetialList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param entityProgresscheck
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EntityProgresscheck entityProgresscheck) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<EntityProgresscheck> queryWrapper = QueryGenerator.initQueryWrapper(entityProgresscheck, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<EntityProgresscheck> queryList = entityProgresscheckService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<EntityProgresscheck> entityProgresscheckList = new ArrayList<EntityProgresscheck>();
        if (oConvertUtils.isEmpty(selections)) {
            entityProgresscheckList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            entityProgresscheckList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<EntityProgresscheckPage> pageList = new ArrayList<EntityProgresscheckPage>();
        for (EntityProgresscheck main : entityProgresscheckList) {
            EntityProgresscheckPage vo = new EntityProgresscheckPage();
            BeanUtils.copyProperties(main, vo);
            List<EntityCheckDetial> entityCheckDetialList = entityCheckDetialService.selectByMainId(main.getUuid());
            vo.setEntityCheckDetialList(entityCheckDetialList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "实体进度清单数据表列表");
        mv.addObject(NormalExcelConstants.CLASS, EntityProgresscheckPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("实体进度清单数据表数据", "导出人:" + sysUser.getRealname(), "实体进度清单数据表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
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
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<EntityProgresscheckPage> list = ExcelImportUtil.importExcel(file.getInputStream(), EntityProgresscheckPage.class, params);
                for (EntityProgresscheckPage page : list) {
                    EntityProgresscheck po = new EntityProgresscheck();
                    BeanUtils.copyProperties(page, po);
                    entityProgresscheckService.saveMain(po, page.getEntityCheckDetialList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

}
