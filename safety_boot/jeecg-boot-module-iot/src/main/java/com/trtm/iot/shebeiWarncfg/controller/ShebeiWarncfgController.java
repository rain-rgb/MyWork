package com.trtm.iot.shebeiWarncfg.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.shebeiWarncfg.entity.ShebeiWarncfg;
import com.trtm.iot.shebeiWarncfg.service.IShebeiWarncfgService;

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
 * @Description: 设备状态预警配置表
 * @Author: jeecg-boot
 * @Date: 2022-08-04
 * @Version: V1.0
 */
@Api(tags = "设备状态预警配置表")
@RestController
@RequestMapping("/shebeiWarncfg/shebeiWarncfg")
@Slf4j
public class ShebeiWarncfgController extends JeecgController<ShebeiWarncfg, IShebeiWarncfgService> {
    @Autowired
    private IShebeiWarncfgService shebeiWarncfgService;

    /**
     * 分页列表查询
     *
     * @param shebeiWarncfg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "设备状态预警配置表-分页列表查询")
    @ApiOperation(value = "设备状态预警配置表-分页列表查询", notes = "设备状态预警配置表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ShebeiWarncfg shebeiWarncfg,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        if (sys_depart_orgcode != null) {
            shebeiWarncfg.setSysOrgCode(sys_depart_orgcode + "*");//前模糊查询
        } else {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            shebeiWarncfg.setSysOrgCode(loginUser.getOrgCode() + "*");//前模糊查询
        }
        QueryWrapper<ShebeiWarncfg> queryWrapper = QueryGenerator.initQueryWrapper(shebeiWarncfg, req.getParameterMap());
        Page<ShebeiWarncfg> page = new Page<ShebeiWarncfg>(pageNo, pageSize);
        IPage<ShebeiWarncfg> pageList = shebeiWarncfgService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param shebeiWarncfg
     * @return
     */
    @AutoLog(value = "设备状态预警配置表-添加")
    @ApiOperation(value = "设备状态预警配置表-添加", notes = "设备状态预警配置表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ShebeiWarncfg shebeiWarncfg) {
        String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
        shebeiWarncfg.setUid(uuid);
        shebeiWarncfgService.save(shebeiWarncfg);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param shebeiWarncfg
     * @return
     */
    @AutoLog(value = "设备状态预警配置表-编辑")
    @ApiOperation(value = "设备状态预警配置表-编辑", notes = "设备状态预警配置表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ShebeiWarncfg shebeiWarncfg) {
        shebeiWarncfgService.updateById(shebeiWarncfg);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设备状态预警配置表-通过id删除")
    @ApiOperation(value = "设备状态预警配置表-通过id删除", notes = "设备状态预警配置表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        shebeiWarncfgService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "设备状态预警配置表-批量删除")
    @ApiOperation(value = "设备状态预警配置表-批量删除", notes = "设备状态预警配置表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.shebeiWarncfgService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设备状态预警配置表-通过id查询")
    @ApiOperation(value = "设备状态预警配置表-通过id查询", notes = "设备状态预警配置表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ShebeiWarncfg shebeiWarncfg = shebeiWarncfgService.getById(id);
        if (shebeiWarncfg == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(shebeiWarncfg);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param shebeiWarncfg
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ShebeiWarncfg shebeiWarncfg) {
        return super.exportXls(request, shebeiWarncfg, ShebeiWarncfg.class, "设备状态预警配置表");
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
        return super.importExcel(request, response, ShebeiWarncfg.class);
    }

}
