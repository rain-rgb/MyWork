package com.trtm.iot.tryajiangconfigure.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.tryajiangconfigure.entity.TrYajiangConfigure;
import com.trtm.iot.tryajiangconfigure.service.ITrYajiangConfigureService;

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
 * @Description: 压浆配置表
 * @Author: jeecg-boot
 * @Date: 2023-03-28
 * @Version: V1.0
 */
@Api(tags = "压浆配置表")
@RestController
@RequestMapping("/tryajiangconfigure/trYajiangConfigure")
@Slf4j
public class TrYajiangConfigureController extends JeecgController<TrYajiangConfigure, ITrYajiangConfigureService> {
    @Autowired
    private ITrYajiangConfigureService trYajiangConfigureService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 分页列表查询
     *
     * @param trYajiangConfigure
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "压浆配置表-分页列表查询")
    @ApiOperation(value = "压浆配置表-分页列表查询", notes = "压浆配置表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrYajiangConfigure trYajiangConfigure,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (trYajiangConfigure.getSysOrgCode() == null) {
            trYajiangConfigure.setSysOrgCode(loginUser.getOrgCode()+"*");
        }
        QueryWrapper<TrYajiangConfigure> queryWrapper = QueryGenerator.initQueryWrapper(trYajiangConfigure, req.getParameterMap());
        Page<TrYajiangConfigure> page = new Page<TrYajiangConfigure>(pageNo, pageSize);
        IPage<TrYajiangConfigure> pageList = trYajiangConfigureService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trYajiangConfigure
     * @return
     */
    @AutoLog(value = "压浆配置表-添加")
    @ApiOperation(value = "压浆配置表-添加", notes = "压浆配置表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrYajiangConfigure trYajiangConfigure) {
        if(StringUtils.isNotBlank(trYajiangConfigure.getNames())){
            BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(trYajiangConfigure.getNames());
            trYajiangConfigure.setNames(bhzPhone.getNames());
            trYajiangConfigure.setPhones(bhzPhone.getPhones());
        }
        if (trYajiangConfigure.getShebeino() != null) {
            TrYajiangConfigure one = trYajiangConfigureService.selectbyshebei(trYajiangConfigure.getShebeino());
            if (one != null) {
                trYajiangConfigure.setId(one.getId());
                trYajiangConfigureService.updateById(trYajiangConfigure);
            } else {
                trYajiangConfigureService.save(trYajiangConfigure);
            }
        }
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trYajiangConfigure
     * @return
     */
    @AutoLog(value = "压浆配置表-编辑")
    @ApiOperation(value = "压浆配置表-编辑", notes = "压浆配置表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrYajiangConfigure trYajiangConfigure) {
        if(StringUtils.isNotBlank(trYajiangConfigure.getNames())){
            BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(trYajiangConfigure.getNames());
            trYajiangConfigure.setNames(bhzPhone.getNames());
            trYajiangConfigure.setPhones(bhzPhone.getPhones());
        }

        trYajiangConfigureService.updateById(trYajiangConfigure);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "压浆配置表-通过id删除")
    @ApiOperation(value = "压浆配置表-通过id删除", notes = "压浆配置表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trYajiangConfigureService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "压浆配置表-批量删除")
    @ApiOperation(value = "压浆配置表-批量删除", notes = "压浆配置表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trYajiangConfigureService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "压浆配置表-通过id查询")
    @ApiOperation(value = "压浆配置表-通过id查询", notes = "压浆配置表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrYajiangConfigure trYajiangConfigure = trYajiangConfigureService.getById(id);
        if (trYajiangConfigure == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trYajiangConfigure);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trYajiangConfigure
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrYajiangConfigure trYajiangConfigure) {
        return super.exportXls(request, trYajiangConfigure, TrYajiangConfigure.class, "压浆配置表");
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
        return super.importExcel(request, response, TrYajiangConfigure.class);
    }

}
