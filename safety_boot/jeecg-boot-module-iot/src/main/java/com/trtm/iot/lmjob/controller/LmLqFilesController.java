package com.trtm.iot.lmjob.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.lmjob.entity.LmJobInfo;
import com.trtm.iot.lmjob.entity.LmSwFiles;
import com.trtm.iot.lmjob.service.ILmJobInfoService;
import com.trtm.iot.lmjob.service.ILmSwFilesService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.lmjob.entity.LmLqFiles;
import com.trtm.iot.lmjob.service.ILmLqFilesService;

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
 * @Description: lm_lq_files
 * @Author: jeecg-boot
 * @Date: 2023-11-15
 * @Version: V1.0
 */
@Api(tags = "lm_lq_files")
@RestController
@RequestMapping("/lmjob/lmLqFiles")
@Slf4j
public class LmLqFilesController extends JeecgController<LmLqFiles, ILmLqFilesService> {
    @Autowired
    private ILmLqFilesService lmLqFilesService;
    @Autowired
    private ILmSwFilesService lmSwFilesService;
    @Autowired
    private ILmJobInfoService lmJobInfoService;

    /**
     * 分页列表查询
     *
     * @param lmLqFiles
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "lm_lq_files-分页列表查询")
    @ApiOperation(value = "lm_lq_files-分页列表查询", notes = "lm_lq_files-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(LmLqFiles lmLqFiles,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<LmLqFiles> queryWrapper = QueryGenerator.initQueryWrapper(lmLqFiles, req.getParameterMap());
        Page<LmLqFiles> page = new Page<LmLqFiles>(pageNo, pageSize);
        IPage<LmLqFiles> pageList = lmLqFilesService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param lmLqFiles
     * @return
     */
    @AutoLog(value = "lm_lq_files-添加")
    @ApiOperation(value = "lm_lq_files-添加", notes = "lm_lq_files-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody LmLqFiles lmLqFiles) {
        lmLqFilesService.save(lmLqFiles);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param lmLqFiles
     * @return
     */
    @AutoLog(value = "lm_lq_files-编辑")
    @ApiOperation(value = "lm_lq_files-编辑", notes = "lm_lq_files-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody LmLqFiles lmLqFiles) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (lmLqFiles.getSgjd().contains("lq")) {
            if (lmLqFiles.getIsfile() == 0) {
                if (ObjectUtil.isNotEmpty(lmLqFiles.getFileurl())) {
                    lmLqFiles.setUpdateBy(loginUser.getRealname());
                    lmLqFiles.setUpdateTime(new Date());
                    lmLqFiles.setFilestatus(1);
                    lmLqFiles.setFilecount(1);
                    lmLqFilesService.updateById(lmLqFiles);
                }
            } else if (lmLqFiles.getIsfile() == 1) {
                lmLqFiles.setFilestatus(2);
                lmLqFiles.setFilecount(0);
                lmLqFilesService.updateById(lmLqFiles);
            }
        } else if (lmLqFiles.getSgjd().contains("sw")) {
            LmSwFiles swFiles = new LmSwFiles();
            BeanUtil.copyProperties(lmLqFiles, swFiles);
            if (swFiles.getIsfile() == 0) {
                if (ObjectUtil.isNotEmpty(swFiles.getFileurl())) {
                    swFiles.setUpdateBy(loginUser.getRealname());
                    swFiles.setUpdateTime(new Date());
                    swFiles.setFilestatus(1);
                    swFiles.setFilecount(1);
                    lmSwFilesService.updateById(swFiles);
                }
            } else if (swFiles.getIsfile() == 1) {
                swFiles.setFilestatus(2);
                swFiles.setFilecount(0);
                lmSwFilesService.updateById(swFiles);
            }
        }
        if (lmLqFiles.getIsfile()==0){
            if (ObjectUtil.isNotEmpty(lmLqFiles.getFileurl())) {
                QueryWrapper<LmJobInfo> lmJobInfoQueryWrapper = new QueryWrapper<>();
                lmJobInfoQueryWrapper.eq("id", lmLqFiles.getInfoid());
                LmJobInfo one = lmJobInfoService.getOne(lmJobInfoQueryWrapper);
                if (ObjectUtil.isNotEmpty(one.getFiles())) {
                    one.setFiles(one.getFiles() + 1);
                } else {
                    one.setFiles(1);
                }
                lmJobInfoService.updateById(one);
            }
        }
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "lm_lq_files-通过id删除")
    @ApiOperation(value = "lm_lq_files-通过id删除", notes = "lm_lq_files-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        lmLqFilesService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "lm_lq_files-批量删除")
    @ApiOperation(value = "lm_lq_files-批量删除", notes = "lm_lq_files-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.lmLqFilesService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "lm_lq_files-通过id查询")
    @ApiOperation(value = "lm_lq_files-通过id查询", notes = "lm_lq_files-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        LmLqFiles lmLqFiles = lmLqFilesService.getById(id);
        if (lmLqFiles == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(lmLqFiles);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param lmLqFiles
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LmLqFiles lmLqFiles) {
        return super.exportXls(request, lmLqFiles, LmLqFiles.class, "lm_lq_files");
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
        return super.importExcel(request, response, LmLqFiles.class);
    }

}
