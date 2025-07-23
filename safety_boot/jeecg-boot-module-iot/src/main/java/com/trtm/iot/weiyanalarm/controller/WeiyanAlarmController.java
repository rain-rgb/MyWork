package com.trtm.iot.weiyanalarm.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.weiyanalarm.vo.WeiyanAlarmPage;
import com.trtm.iot.weiyanalarmhandler.entity.WeiyanAlarmHandler;
import com.trtm.iot.weiyanalarmhandler.service.IWeiyanAlarmHandlerService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.weiyanalarm.entity.WeiyanAlarm;
import com.trtm.iot.weiyanalarm.service.IWeiyanAlarmService;

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
 * @Description: 监控量测报警数据表
 * @Author: jeecg-boot
 * @Date: 2022-07-06
 * @Version: V1.0
 */
@Api(tags = "监控量测报警数据表")
@RestController
@RequestMapping("/weiyanalarm/weiyanAlarm")
@Slf4j
public class WeiyanAlarmController extends JeecgController<WeiyanAlarm, IWeiyanAlarmService> {
    @Autowired
    private IWeiyanAlarmService weiyanAlarmService;
    @Autowired
    private IWeiyanAlarmHandlerService weiyanAlarmHandlerService;
    /**
     * 分页列表查询
     *
     * @param weiyanAlarm
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "监控量测报警数据表-分页列表查询")
    @ApiOperation(value = "监控量测报警数据表-分页列表查询", notes = "监控量测报警数据表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WeiyanAlarm weiyanAlarm,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WeiyanAlarm> queryWrapper = QueryGenerator.initQueryWrapper(weiyanAlarm, req.getParameterMap());
        Page<WeiyanAlarm> page = new Page<WeiyanAlarm>(pageNo, pageSize);
        IPage<WeiyanAlarm> pageList = weiyanAlarmService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 列表查询（对外开放接口）
     *
     * @param weiyanAlarm
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "监控量测报警数据表-列表查询（对外开放接口）")
    @ApiOperation(value = "监控量测报警数据表-列表查询（对外开放接口）", notes = "监控量测报警数据表-列表查询（对外开放接口）")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(WeiyanAlarm weiyanAlarm,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        if (weiyanAlarm.getSysOrgCode()!=null){
            weiyanAlarm.setSysOrgCode(weiyanAlarm.getSysOrgCode()+"*");
        }
        QueryWrapper<WeiyanAlarm> queryWrapper = QueryGenerator.initQueryWrapper(weiyanAlarm, req.getParameterMap());
        List<WeiyanAlarm> pageList = weiyanAlarmService.list(queryWrapper);
        List<WeiyanAlarmPage> list = new ArrayList<>();
        for (WeiyanAlarm weiyanAlarm1 : pageList){
            WeiyanAlarmPage weiyanAlarmPage = new WeiyanAlarmPage();
            BeanUtils.copyProperties(weiyanAlarm1, weiyanAlarmPage);
            QueryWrapper<WeiyanAlarmHandler> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("batch_no",weiyanAlarm1.getBatchNo());
            WeiyanAlarmHandler weiyanAlarmHandler = weiyanAlarmHandlerService.getOne(queryWrapper1);
            weiyanAlarmPage.setWeiyanAlarmHandler(weiyanAlarmHandler);
            list.add(weiyanAlarmPage);
        }
        return Result.OK(list);
    }


    /**
     * 添加
     *
     * @param weiyanAlarm
     * @return
     */
    @AutoLog(value = "监控量测报警数据表-添加")
    @ApiOperation(value = "监控量测报警数据表-添加", notes = "监控量测报警数据表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WeiyanAlarm weiyanAlarm) {
        weiyanAlarmService.save(weiyanAlarm);
        return Result.OK("添加成功！");
    }

    /**
     * 添加(对外开放)
     *
     * @param weiyanAlarmPage
     * @return
     */
    @AutoLog(value = "监控量测报警数据表-添加(对外开放)")
    @ApiOperation(value = "监控量测报警数据表-添加(对外开放)", notes = "监控量测报警数据表-添加(对外开放)")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody WeiyanAlarmPage weiyanAlarmPage) {
        WeiyanAlarm weiyanAlarm = new WeiyanAlarm();
        BeanUtils.copyProperties(weiyanAlarmPage, weiyanAlarm);
//        weiyanAlarmService.save(weiyanAlarm);
        Integer code = weiyanAlarmService.saveMain(weiyanAlarm, weiyanAlarmPage.getWeiyanAlarmHandler());
        if (code==200) {
            return Result.OK("添加成功！");
        }else{
            return Result.error("数据重复/添加失败！");
        }
    }

    /**
     * 编辑
     *
     * @param weiyanAlarm
     * @return
     */
    @AutoLog(value = "监控量测报警数据表-编辑")
    @ApiOperation(value = "监控量测报警数据表-编辑", notes = "监控量测报警数据表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WeiyanAlarm weiyanAlarm) {
        weiyanAlarmService.updateById(weiyanAlarm);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "监控量测报警数据表-通过id删除")
    @ApiOperation(value = "监控量测报警数据表-通过id删除", notes = "监控量测报警数据表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        weiyanAlarmService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "监控量测报警数据表-批量删除")
    @ApiOperation(value = "监控量测报警数据表-批量删除", notes = "监控量测报警数据表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.weiyanAlarmService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "监控量测报警数据表-通过id查询")
    @ApiOperation(value = "监控量测报警数据表-通过id查询", notes = "监控量测报警数据表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WeiyanAlarm weiyanAlarm = weiyanAlarmService.getById(id);
        if (weiyanAlarm == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(weiyanAlarm);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param weiyanAlarm
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WeiyanAlarm weiyanAlarm) {
        return super.exportXls(request, weiyanAlarm, WeiyanAlarm.class, "监控量测报警数据表");
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
        return super.importExcel(request, response, WeiyanAlarm.class);
    }

}
