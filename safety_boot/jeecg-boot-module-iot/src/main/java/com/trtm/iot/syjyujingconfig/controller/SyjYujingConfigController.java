package com.trtm.iot.syjyujingconfig.controller;

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

import cn.hutool.core.util.ObjectUtil;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.syjyujingconfig.entity.SyjYujingConfig;
import com.trtm.iot.syjyujingconfig.service.ISyjYujingConfigService;

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
import org.springframework.beans.BeanUtils;
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
 * @Description: syj_yujing_config
 * @Author: jeecg-boot
 * @Date: 2023-11-23
 * @Version: V1.0
 */
@Api(tags = "syj_yujing_config")
@RestController
@RequestMapping("/syjyujingconfig/syjYujingConfig")
@Slf4j
public class SyjYujingConfigController extends JeecgController<SyjYujingConfig, ISyjYujingConfigService> {
    @Autowired
    private ISyjYujingConfigService syjYujingConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;

    /**
     * 分页列表查询
     *
     * @param syjYujingConfig
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "syj_yujing_config-分页列表查询")
    @ApiOperation(value = "syj_yujing_config-分页列表查询", notes = "syj_yujing_config-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyjYujingConfig syjYujingConfig,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SyjYujingConfig> queryWrapper = QueryGenerator.initQueryWrapper(syjYujingConfig, req.getParameterMap());
        Page<SyjYujingConfig> page = new Page<SyjYujingConfig>(pageNo, pageSize);
        IPage<SyjYujingConfig> pageList = syjYujingConfigService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syjYujingConfig
     * @return
     */
    @AutoLog(value = "syj_yujing_config-添加")
    @ApiOperation(value = "syj_yujing_config-添加", notes = "syj_yujing_config-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyjYujingConfig syjYujingConfig) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ShebeiInfo sbj = shebeiInfoService.selectshebeione(syjYujingConfig.getShebeiNo());
        if (ObjectUtil.isEmpty(sbj)) {
            return Result.error("设备未找到!");
        }
        SyjYujingConfig syjYJ = new SyjYujingConfig();
        syjYJ.setSysOrgCode(syjYujingConfig.getSysOrgCode());
        syjYJ.setSbType(sbj.getSbtype());
        syjYJ.setShebeiNo(sbj.getSbjno());
        syjYJ.setUuid(syjYujingConfig.getNames());
        syjYJ.setIsCall(syjYujingConfig.getIsCall());
        BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(syjYujingConfig.getNames());
        syjYJ.setNames(bhzPhone.getNames());
        syjYJ.setPhones(bhzPhone.getPhones());
        syjYJ.setCreateBy(loginUser.getRealname());
        syjYJ.setCreateTime(new Date());
        syjYujingConfigService.save(syjYJ);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param syjYujingConfig
     * @return
     */
    @AutoLog(value = "syj_yujing_config-编辑")
    @ApiOperation(value = "syj_yujing_config-编辑", notes = "syj_yujing_config-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyjYujingConfig syjYujingConfig) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SyjYujingConfig syjYJ = new SyjYujingConfig();
        BeanUtils.copyProperties(syjYujingConfig, syjYJ);
        BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(syjYujingConfig.getNames());
        if (ObjectUtil.isEmpty(bhzPhone)) {
            bhzPhone = bhzPhoneService.selectBhzPhone(syjYujingConfig.getUuid());
        }
        syjYJ.setUuid(syjYujingConfig.getNames());
        syjYJ.setNames(bhzPhone.getNames());
        syjYJ.setPhones(bhzPhone.getPhones());
        syjYJ.setUpdateBy(loginUser.getRealname());
        syjYJ.setUpdateTime(new Date());
        syjYujingConfigService.updateById(syjYJ);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "syj_yujing_config-通过id删除")
    @ApiOperation(value = "syj_yujing_config-通过id删除", notes = "syj_yujing_config-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        syjYujingConfigService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "syj_yujing_config-批量删除")
    @ApiOperation(value = "syj_yujing_config-批量删除", notes = "syj_yujing_config-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syjYujingConfigService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "syj_yujing_config-通过id查询")
    @ApiOperation(value = "syj_yujing_config-通过id查询", notes = "syj_yujing_config-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SyjYujingConfig syjYujingConfig = syjYujingConfigService.getById(id);
        if (syjYujingConfig == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(syjYujingConfig);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syjYujingConfig
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyjYujingConfig syjYujingConfig) {
        return super.exportXls(request, syjYujingConfig, SyjYujingConfig.class, "syj_yujing_config");
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
        return super.importExcel(request, response, SyjYujingConfig.class);
    }

}
