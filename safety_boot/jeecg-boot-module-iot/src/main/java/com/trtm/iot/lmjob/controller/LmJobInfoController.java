package com.trtm.iot.lmjob.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.lmjob.entity.LmLqFiles;
import com.trtm.iot.lmjob.entity.LmSwFiles;
import com.trtm.iot.lmjob.entity.LmUploadFiles;
import com.trtm.iot.lmjob.service.ILmLqFilesService;
import com.trtm.iot.lmjob.service.ILmSwFilesService;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.lmjob.entity.LmJobInfo;
import com.trtm.iot.lmjob.service.ILmJobInfoService;

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
 * @Description: lm_job_info
 * @Author: jeecg-boot
 * @Date: 2023-08-31
 * @Version: V1.0
 */
@Api(tags = "lm_job_info")
@RestController
@RequestMapping("/lmjob/lmJobInfo")
@Slf4j
public class LmJobInfoController extends JeecgController<LmJobInfo, ILmJobInfoService> {
    @Autowired
    private ILmJobInfoService lmJobInfoService;
    @Autowired
    private ILmSwFilesService lmSwFilesService;
    @Autowired
    private ILmLqFilesService lmLqFilesService;

    /**
     * 分页列表查询
     *
     * @param lmJobInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "lm_job_info-分页列表查询")
    @ApiOperation(value = "lm_job_info-分页列表查询", notes = "lm_job_info-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(LmJobInfo lmJobInfo, String sys_depart_orgcode, String sys_depart_project,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        if (StringUtils.isNotBlank(sys_depart_orgcode)) {
            lmJobInfo.setSysOrgCode(sys_depart_orgcode + '*');
        }
        if (StringUtils.isNotBlank(sys_depart_project)) {
            lmJobInfo.setWbsOrgCode(sys_depart_project + '*');
        }

        QueryWrapper<LmJobInfo> queryWrapper = QueryGenerator.initQueryWrapper(lmJobInfo, req.getParameterMap());
        Page<LmJobInfo> page = new Page<LmJobInfo>(pageNo, pageSize);
        IPage<LmJobInfo> pageList = lmJobInfoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param lmJobInfo
     * @return
     */
    @AutoLog(value = "lm_job_info-添加")
    @ApiOperation(value = "lm_job_info-添加", notes = "lm_job_info-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody LmJobInfo lmJobInfo) {
        lmJobInfoService.save(lmJobInfo);
        return Result.OK("添加成功！");
    }

    /**
     * 添加1
     *
     * @param lmJobInfo
     * @return
     */
    @AutoLog(value = "lm_job_info-添加")
    @ApiOperation(value = "lm_job_info-添加", notes = "lm_job_info-添加")
    @PostMapping(value = "/add1")
    public Result<?> add1(@RequestBody LmJobInfo lmJobInfo) {
        lmJobInfoService.save(lmJobInfo);
        List<LmUploadFiles> lmUploadFiles = lmJobInfoService.sselectUploadFileList();
        for (LmUploadFiles lmUploadFile : lmUploadFiles) {
            if (lmUploadFile.getSgjdValue().contains("sw")) {
                LmSwFiles lmSwFile = new LmSwFiles();
                lmSwFile.setInfoid(lmJobInfo.getId());
                lmSwFile.setSgjd(lmUploadFile.getSgjdValue());
                lmSwFile.setFiletype(lmUploadFile.getTypeValue());
                lmSwFile.setSysOrgCode(lmJobInfo.getSysOrgCode());
                lmSwFile.setFilestatus(0);
                lmSwFile.setFilecount(0);
                lmSwFilesService.save(lmSwFile);
            } else if (lmUploadFile.getSgjdValue().contains("lq")) {
                LmLqFiles lmLqFile = new LmLqFiles();
                lmLqFile.setInfoid(lmJobInfo.getId());
                lmLqFile.setSgjd(lmUploadFile.getSgjdValue());
                lmLqFile.setFiletype(lmUploadFile.getTypeValue());
                lmLqFile.setSysOrgCode(lmUploadFile.getTypeValue());
                lmLqFile.setFilestatus(0);
                lmLqFile.setFilecount(0);
                lmLqFilesService.save(lmLqFile);
            }
        }
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param lmJobInfo
     * @return
     */
    @AutoLog(value = "lm_job_info-编辑")
    @ApiOperation(value = "lm_job_info-编辑", notes = "lm_job_info-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody LmJobInfo lmJobInfo) {
        lmJobInfoService.updateById(lmJobInfo);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "lm_job_info-通过id删除")
    @ApiOperation(value = "lm_job_info-通过id删除", notes = "lm_job_info-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        lmJobInfoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "lm_job_info-批量删除")
    @ApiOperation(value = "lm_job_info-批量删除", notes = "lm_job_info-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.lmJobInfoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "lm_job_info-通过id查询")
    @ApiOperation(value = "lm_job_info-通过id查询", notes = "lm_job_info-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        LmJobInfo lmJobInfo = lmJobInfoService.getById(id);
        if (lmJobInfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(lmJobInfo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param lmJobInfo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LmJobInfo lmJobInfo) {
        return super.exportXls(request, lmJobInfo, LmJobInfo.class, "lm_job_info");
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
        return super.importExcel(request, response, LmJobInfo.class);
    }

    /**
     * 通过项目查询各阶段上传文件数
     *
     * @param sysOrgCode
     * @return
     */
    @AutoLog(value = "通过项目查询各阶段上传文件数")
    @ApiOperation(value = "通过项目查询各阶段上传文件数", notes = "通过项目查询各阶段上传文件数")
    @GetMapping(value = "/getFileNum")
    public Result<?> getFileNum(@RequestParam(name = "sysOrgCode", required = true) String sysOrgCode) {
        List<Map<String, String>> maps = lmJobInfoService.selectLmFile(sysOrgCode);
        return Result.OK(maps);
    }

}
