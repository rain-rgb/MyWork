package com.trtm.iot.bhzcementwaterrate.controller;

import java.util.Arrays;
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
import com.trtm.iot.bhzcementwaterratereal.entity.BhzCementWaterrateReal;
import com.trtm.iot.bhzcementwaterratereal.service.IBhzCementWaterrateRealService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bhzcementwaterrate.entity.BhzCementWaterrate;
import com.trtm.iot.bhzcementwaterrate.service.IBhzCementWaterrateService;

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
 * @Description: bhz_cement_waterrate
 * @Author: jeecg-boot
 * @Date: 2023-07-13
 * @Version: V1.0
 */
@Api(tags = "bhz_cement_waterrate")
@RestController
@RequestMapping("/bhzcementwaterrate/bhzCementWaterrate")
@Slf4j
public class BhzCementWaterrateController extends JeecgController<BhzCementWaterrate, IBhzCementWaterrateService> {
    @Autowired
    private IBhzCementWaterrateService bhzCementWaterrateService;
    @Autowired
    private IBhzCementWaterrateRealService bhzCementWaterrateRealService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param bhzCementWaterrate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bhz_cement_waterrate-分页列表查询")
    @ApiOperation(value = "bhz_cement_waterrate-分页列表查询", notes = "bhz_cement_waterrate-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BhzCementWaterrate bhzCementWaterrate,
                                   String sys_depart_orgcode,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            bhzCementWaterrate.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            bhzCementWaterrate.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        QueryWrapper<BhzCementWaterrate> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementWaterrate, req.getParameterMap());
        queryWrapper.orderByDesc("id");
        Page<BhzCementWaterrate> page = new Page<BhzCementWaterrate>(pageNo, pageSize);
        IPage<BhzCementWaterrate> pageList = bhzCementWaterrateService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bhzCementWaterrate
     * @return
     */
    @AutoLog(value = "bhz_cement_waterrate-添加")
    @ApiOperation(value = "bhz_cement_waterrate-添加", notes = "bhz_cement_waterrate-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BhzCementWaterrate bhzCementWaterrate) {
        bhzCementWaterrateService.save(bhzCementWaterrate);
        QueryWrapper<BhzCementWaterrateReal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shebeiid",bhzCementWaterrate.getShebeiid());
        BhzCementWaterrateReal one = bhzCementWaterrateRealService.getOne(queryWrapper);
        bhzCementWaterrate.setId(null);
        BhzCementWaterrateReal bhzCementWaterrateReal = new BhzCementWaterrateReal();
        BeanUtil.copyProperties(bhzCementWaterrate, bhzCementWaterrateReal);
        if (ObjectUtil.isEmpty(one)){//实时表没有数据，添加
            bhzCementWaterrateRealService.save(bhzCementWaterrateReal);
            return Result.OK("历史数据添加成功，实时数据添加成功！");
        }else {//实时表有数据，更新
            bhzCementWaterrateReal.setId(one.getId());
            bhzCementWaterrateRealService.updateById(bhzCementWaterrateReal);
            return Result.OK("历史数据添加成功，实时数据更新成功！");
        }
    }

    /**
     * 编辑
     *
     * @param bhzCementWaterrate
     * @return
     */
    @AutoLog(value = "bhz_cement_waterrate-编辑")
    @ApiOperation(value = "bhz_cement_waterrate-编辑", notes = "bhz_cement_waterrate-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BhzCementWaterrate bhzCementWaterrate) {
        bhzCementWaterrateService.updateById(bhzCementWaterrate);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_cement_waterrate-通过id删除")
    @ApiOperation(value = "bhz_cement_waterrate-通过id删除", notes = "bhz_cement_waterrate-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzCementWaterrateService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "bhz_cement_waterrate-批量删除")
    @ApiOperation(value = "bhz_cement_waterrate-批量删除", notes = "bhz_cement_waterrate-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzCementWaterrateService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_cement_waterrate-通过id查询")
    @ApiOperation(value = "bhz_cement_waterrate-通过id查询", notes = "bhz_cement_waterrate-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BhzCementWaterrate bhzCementWaterrate = bhzCementWaterrateService.getById(id);
        if (bhzCementWaterrate == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzCementWaterrate);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzCementWaterrate
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzCementWaterrate bhzCementWaterrate) {
        return super.exportXls(request, bhzCementWaterrate, BhzCementWaterrate.class, "bhz_cement_waterrate");
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
        return super.importExcel(request, response, BhzCementWaterrate.class);
    }

}
