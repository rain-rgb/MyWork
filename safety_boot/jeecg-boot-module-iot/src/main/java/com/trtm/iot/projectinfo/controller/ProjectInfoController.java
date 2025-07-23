package com.trtm.iot.projectinfo.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.projectinfo.entity.ProjectInfo;
import com.trtm.iot.projectinfo.service.IProjectInfoService;

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
 * @Description: project_info
 * @Author: jeecg-boot
 * @Date: 2022-12-16
 * @Version: V1.0
 */
@Api(tags = "project_info")
@RestController
@RequestMapping("/projectinfo/projectInfo")
@Slf4j
public class ProjectInfoController extends JeecgController<ProjectInfo, IProjectInfoService> {
    @Autowired
    private IProjectInfoService projectInfoService;

    /**
     * 分页列表查询
     *
     * @param projectInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "project_info-分页列表查询")
    @ApiOperation(value = "project_info-分页列表查询", notes = "project_info-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ProjectInfo projectInfo,String sys_depart_orgcode,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {

        if(StringUtils.isNotBlank(sys_depart_orgcode)){
            projectInfo.setSysOrgCode(sys_depart_orgcode+"*");
        }
        QueryWrapper<ProjectInfo> queryWrapper = QueryGenerator.initQueryWrapper(projectInfo, req.getParameterMap());
        Page<ProjectInfo> page = new Page<ProjectInfo>(pageNo, pageSize);
        IPage<ProjectInfo> pageList = projectInfoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 统计场地类别的数量
     *
     * @param projectInfo
     * @param req
     * @return
     */
    @AutoLog(value = "project_info-统计场地类别的数量")
    @ApiOperation(value = "project_info-统计场地类别的数量", notes = "project_info-统计场地类别的数量")
    @GetMapping(value = "/list1")
    public Result<?> statistics(ProjectInfo projectInfo, HttpServletRequest req) {
//        List<String> orgCategoryList = projectInfoService.selectOrgCategory();
        String[] orgCategoryList = {"4", "9", "10", "11", "12", "13", "14", "100"};
        HashMap map = new HashMap();
        for (String orgcategory : orgCategoryList) {
            QueryWrapper<ProjectInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("count(*) as note");
            queryWrapper.eq("org_category", orgcategory);
            if(StringUtils.isNotBlank(projectInfo.getSysOrgCode())){
                queryWrapper.likeRight("sys_org_code",projectInfo.getSysOrgCode());
            }
            ProjectInfo one = projectInfoService.getOne(queryWrapper);
            String count = one.getNote();
            String orgcategoryName = "";
            switch (orgcategory) {
                case "4":
                    orgcategoryName = "xm";
                    break;
                case "9":
                    orgcategoryName = "zhbhz";
                    break;
                case "10":
                    orgcategoryName = "zhsd";
                    break;
                case "11":
                    orgcategoryName = "zhlc";
                    break;
                case "12":
                    orgcategoryName = "ljlm";
                    break;
                case "13":
                    orgcategoryName = "szsys";
                    break;
                case "14":
                    orgcategoryName = "jswlw";
                    break;
                case "100":
                    orgcategoryName = "qt";
                    break;
            }
            map.put(orgcategoryName,count);
        }
        return Result.OK(map);
    }

    /**
     * 添加
     *
     * @param projectInfo
     * @return
     */
    @AutoLog(value = "project_info-添加")
    @ApiOperation(value = "project_info-添加", notes = "project_info-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ProjectInfo projectInfo) {
        projectInfoService.save(projectInfo);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param projectInfo
     * @return
     */
    @AutoLog(value = "project_info-编辑")
    @ApiOperation(value = "project_info-编辑", notes = "project_info-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ProjectInfo projectInfo) {
        projectInfoService.updateById(projectInfo);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "project_info-通过id删除")
    @ApiOperation(value = "project_info-通过id删除", notes = "project_info-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        projectInfoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "project_info-批量删除")
    @ApiOperation(value = "project_info-批量删除", notes = "project_info-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.projectInfoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "project_info-通过id查询")
    @ApiOperation(value = "project_info-通过id查询", notes = "project_info-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ProjectInfo projectInfo = projectInfoService.getById(id);
        if (projectInfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(projectInfo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param projectInfo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProjectInfo projectInfo) {
        return super.exportXls(request, projectInfo, ProjectInfo.class, "project_info");
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
        return super.importExcel(request, response, ProjectInfo.class);
    }

}
