package com.trtm.iot.yajiangrenwudan.controller;

import java.util.Arrays;
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
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;

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
 * @Description: 压浆任务单
 * @Author: jeecg-boot
 * @Date: 2021-09-08
 * @Version: V1.0
 */
@Api(tags = "压浆任务单")
@RestController
@RequestMapping("/yajiangrenwudan/trYajiangRenwudan")
@Slf4j
public class TrYajiangRenwudanController extends JeecgController<TrYajiangRenwudan, ITrYajiangRenwudanService> {
    @Autowired
    private ITrYajiangRenwudanService trYajiangRenwudanService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param trYajiangRenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "压浆任务单-分页列表查询")
    @ApiOperation(value = "压浆任务单-分页列表查询", notes = "压浆任务单-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrYajiangRenwudan trYajiangRenwudan,
                                   String sys_depart_project,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trYajiangRenwudan.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                trYajiangRenwudan.setShebeibianhao(shebei);
            } else {
                trYajiangRenwudan.setShebeibianhao("空");
            }
        }
//        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
//            trYajiangRenwudan.setSysOrgCode("*" + sys_depart_orgcode + "*");
//        }
        trYajiangRenwudan.setSgbwname("*" + trYajiangRenwudan.getSgbwname() + "*");
        trYajiangRenwudan.setProjectname("*" + trYajiangRenwudan.getProjectname() + "*");
        QueryWrapper<TrYajiangRenwudan> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangRenwudan, req.getParameterMap());
        queryWrapper.likeRight(sys_depart_project != null,"sgbwUuid", sys_depart_project);
        Page<TrYajiangRenwudan> page = new Page<TrYajiangRenwudan>(pageNo, pageSize);
        IPage<TrYajiangRenwudan> pageList = trYajiangRenwudanService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 任务单列表查询
     *
     * @param trYajiangRenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "压浆任务单-分页列表查询")
    @ApiOperation(value = "压浆任务单-分页列表查询", notes = "压浆任务单-分页列表查询")
    @PostMapping(value = "/rwdlist")
    public Result<?> queryrwdList(TrYajiangRenwudan trYajiangRenwudan,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                  HttpServletRequest req) {
        if (trYajiangRenwudan.getStatus() == null) {
            trYajiangRenwudan.setStatus(0);
        }
        QueryWrapper<TrYajiangRenwudan> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangRenwudan, req.getParameterMap());
        queryWrapper.orderByDesc("id");
        List<TrYajiangRenwudan> pageList = trYajiangRenwudanService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trYajiangRenwudan
     * @return
     */
    @AutoLog(value = "压浆任务单-添加")
    @ApiOperation(value = "压浆任务单-添加", notes = "压浆任务单-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrYajiangRenwudan trYajiangRenwudan) {
        trYajiangRenwudanService.save(trYajiangRenwudan);
        return Result.OK("添加成功！");
    }

    /**
     * 压浆任务单对外开放添加数据
     *
     * @param trYajiangRenwudan
     * @return
     */
    @AutoLog(value = "压浆任务单-添加")
    @ApiOperation(value = "压浆任务单-添加", notes = "压浆任务单-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody TrYajiangRenwudan trYajiangRenwudan) {
        trYajiangRenwudanService.save(trYajiangRenwudan);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trYajiangRenwudan
     * @return
     */
    @AutoLog(value = "压浆任务单-编辑")
    @ApiOperation(value = "压浆任务单-编辑", notes = "压浆任务单-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrYajiangRenwudan trYajiangRenwudan) {
        trYajiangRenwudanService.updateById(trYajiangRenwudan);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "压浆任务单-通过id删除")
    @ApiOperation(value = "压浆任务单-通过id删除", notes = "压浆任务单-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trYajiangRenwudanService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "压浆任务单-批量删除")
    @ApiOperation(value = "压浆任务单-批量删除", notes = "压浆任务单-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trYajiangRenwudanService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "压浆任务单-通过id查询")
    @ApiOperation(value = "压浆任务单-通过id查询", notes = "压浆任务单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrYajiangRenwudan trYajiangRenwudan = trYajiangRenwudanService.getById(id);
        if (trYajiangRenwudan == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trYajiangRenwudan);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trYajiangRenwudan
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrYajiangRenwudan trYajiangRenwudan) {
        return super.exportXls(request, trYajiangRenwudan, TrYajiangRenwudan.class, "压浆任务单");
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
        return super.importExcel(request, response, TrYajiangRenwudan.class);
    }

}
