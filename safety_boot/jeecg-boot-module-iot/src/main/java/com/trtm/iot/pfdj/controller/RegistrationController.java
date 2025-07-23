package com.trtm.iot.pfdj.controller;

import java.util.*;
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
import com.trtm.iot.pfdj.entity.Registration;
import com.trtm.iot.pfdj.service.IRegistrationService;

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
 * @Description: registration
 * @Author: jeecg-boot
 * @Date: 2023-04-23
 * @Version: V1.0
 */
@Api(tags = "registration")
@RestController
@RequestMapping("/pfdj/registration")
@Slf4j
public class RegistrationController extends JeecgController<Registration, IRegistrationService> {
    @Autowired
    private IRegistrationService registrationService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param registration
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "registration-分页列表查询")
    @ApiOperation(value = "registration-分页列表查询", notes = "registration-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Registration registration,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        registration.setSysName("*" + registration.getSysName() + "*");
        QueryWrapper<Registration> queryWrapper = QueryGenerator.initQueryWrapper(registration, req.getParameterMap());
        Page<Registration> page = new Page<Registration>(pageNo, pageSize);
        IPage<Registration> pageList = registrationService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param registration
     * @return
     */
    @AutoLog(value = "registration-添加")
    @ApiOperation(value = "registration-添加", notes = "registration-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Registration registration) {
        registration.setDataTime(new Date());
        registrationService.save(registration);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param registration
     * @return
     */
    @AutoLog(value = "registration-编辑")
    @ApiOperation(value = "registration-编辑", notes = "registration-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Registration registration) {
        registrationService.updateById(registration);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "registration-通过id删除")
    @ApiOperation(value = "registration-通过id删除", notes = "registration-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        registrationService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "registration-批量删除")
    @ApiOperation(value = "registration-批量删除", notes = "registration-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.registrationService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "registration-通过id查询")
    @ApiOperation(value = "registration-通过id查询", notes = "registration-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Registration registration = registrationService.getById(id);
        if (registration == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(registration);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param registration
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Registration registration) {
        return super.exportXls(request, registration, Registration.class, "registration");
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
        return super.importExcel(request, response, Registration.class);
    }

    /**
     * 根据组织机构编码获取最新一条数据
     * @param sysOrgCode 组织机构编码
     * @return
     */
    @AutoLog(value = "registration-根据组织机构编码获取最新一条数据")
    @ApiOperation(value = "registration-根据组织机构编码获取最新一条数据", notes = "registration-根据组织机构编码获取最新一条数据")
    @GetMapping(value = "/getNewData")
    public Result<?> getNewData(String sysOrgCode){
        QueryWrapper<Registration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sys_org_code",sysOrgCode);
        queryWrapper.orderByDesc("data_time");
        queryWrapper.last("limit 1");
        Registration one = registrationService.getOne(queryWrapper);
        return Result.OK(one);
    }

}
