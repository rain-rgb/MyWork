package com.trtm.iot.kongjingbasicinfo.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.chaoshengbo.entity.ChaoshengboSyjbsj;
import com.trtm.iot.kongjingbasicinfo.vo.KongjingBasicinfoPage;
import com.trtm.iot.system.entity.ShebeiInfo;
import org.apache.shiro.SecurityUtils;
import org.checkerframework.checker.units.qual.K;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.kongjingbasicinfo.entity.KongjingBasicinfo;
import com.trtm.iot.kongjingbasicinfo.service.IKongjingBasicinfoService;

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
 * @Description: 孔径基本信息数据
 * @Author: jeecg-boot
 * @Date: 2022-03-01
 * @Version: V1.0
 */
@Api(tags = "孔径基本信息数据")
@RestController
@RequestMapping("/kongjingbasicinfo/kongjingBasicinfo")
@Slf4j
public class KongjingBasicinfoController extends JeecgController<KongjingBasicinfo, IKongjingBasicinfoService> {
    @Autowired
    private IKongjingBasicinfoService kongjingBasicinfoService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param kongjingBasicinfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "孔径基本信息数据-分页列表查询")
    @ApiOperation(value = "孔径基本信息数据-分页列表查询", notes = "孔径基本信息数据-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(KongjingBasicinfo kongjingBasicinfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (kongjingBasicinfo.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                kongjingBasicinfo.setShebeino(shebei);
            } else {
                kongjingBasicinfo.setShebeino("空");
            }
        }
        kongjingBasicinfo.setDetloc("*" + kongjingBasicinfo.getDetloc() + "*");
        QueryWrapper<KongjingBasicinfo> queryWrapper = QueryGenerator.initQueryWrapper(kongjingBasicinfo, req.getParameterMap());
        Page<KongjingBasicinfo> page = new Page<KongjingBasicinfo>(pageNo, pageSize);
        IPage<KongjingBasicinfo> pageList = kongjingBasicinfoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param kongjingBasicinfo
     * @return
     */
    @AutoLog(value = "孔径基本信息数据-添加")
    @ApiOperation(value = "孔径基本信息数据-添加", notes = "孔径基本信息数据-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody KongjingBasicinfo kongjingBasicinfo) {
        kongjingBasicinfoService.save(kongjingBasicinfo);
        return Result.OK("添加成功！");
    }

    /**
     * 对外开放添加数据接口
     *
     * @param kongjingBasicinfoPage
     * @return
     */
    @AutoLog(value = "孔径基本信息数据-添加")
    @ApiOperation(value = "孔径基本信息数据-添加", notes = "孔径基本信息数据-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody KongjingBasicinfoPage kongjingBasicinfoPage) {
        KongjingBasicinfo kongjingBasicinfo = new KongjingBasicinfo();
        BeanUtils.copyProperties(kongjingBasicinfoPage, kongjingBasicinfo);
        int code = kongjingBasicinfoService.saveMain(kongjingBasicinfo, kongjingBasicinfoPage.getKongjingTongdaoList(), kongjingBasicinfoPage.getKongjingPointsdataList(), kongjingBasicinfoPage.getKongjingAnalysisdataList());
        if (code == 200) {
            return Result.OK("添加成功！");
        } else {
            return Result.error("数据重复/添加失败！");
        }

    }

    /**
     * 编辑
     *
     * @param kongjingBasicinfo
     * @return
     */
    @AutoLog(value = "孔径基本信息数据-编辑")
    @ApiOperation(value = "孔径基本信息数据-编辑", notes = "孔径基本信息数据-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody KongjingBasicinfo kongjingBasicinfo) {
        KongjingBasicinfo basicInfo = kongjingBasicinfoService.getOne(Wrappers.lambdaQuery(new KongjingBasicinfo()).eq(KongjingBasicinfo::getId, kongjingBasicinfo.getId()));
        if (ObjectUtil.isEmpty(kongjingBasicinfo.getFile())) {
            kongjingBasicinfo.setFile(basicInfo.getFile());
        }
        if (ObjectUtil.isEmpty(kongjingBasicinfo.getLevel())) {
            kongjingBasicinfo.setLevel(basicInfo.getLevel());
        }
        kongjingBasicinfoService.updateById(kongjingBasicinfo);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "孔径基本信息数据-通过id删除")
    @ApiOperation(value = "孔径基本信息数据-通过id删除", notes = "孔径基本信息数据-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        kongjingBasicinfoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "孔径基本信息数据-批量删除")
    @ApiOperation(value = "孔径基本信息数据-批量删除", notes = "孔径基本信息数据-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.kongjingBasicinfoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "孔径基本信息数据-通过id查询")
    @ApiOperation(value = "孔径基本信息数据-通过id查询", notes = "孔径基本信息数据-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        KongjingBasicinfo kongjingBasicinfo = kongjingBasicinfoService.getById(id);
        if (kongjingBasicinfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(kongjingBasicinfo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param kongjingBasicinfo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, KongjingBasicinfo kongjingBasicinfo) {
        return super.exportXls(request, kongjingBasicinfo, KongjingBasicinfo.class, "孔径基本信息数据");
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
        return super.importExcel(request, response, KongjingBasicinfo.class);
    }

    /**
     * 孔径各评级数
     *
     * @param kongjingBasicinfo
     * @param date
     * @return
     */
    @AutoLog(value = "孔径各评级数")
    @ApiOperation(value = "孔径各评级数", notes = "孔径各评级数")
    @GetMapping(value = "/statisticsKJData")
    public Result<?> statisticsKJData(KongjingBasicinfo kongjingBasicinfo,
                                      Integer date) {
        String shebei = null;
        if (ObjectUtil.isEmpty(kongjingBasicinfo.getShebeino())) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        } else {
            shebei = kongjingBasicinfo.getShebeino();
        }
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        Map map = kongjingBasicinfoService.statisticsLevelZB(shebeilist, date);
        return Result.OK(map);
    }

    /**
     * 孔径各评级数统计
     *
     * @param kongjingBasicinfo
     * @param date
     * @return
     */
    @AutoLog(value = "孔径各评级数统计")
    @ApiOperation(value = "孔径各评级数统计", notes = "孔径各评级数统计")
    @GetMapping(value = "/getListKJStatistics")
    public Result<?> getListKJStatistics(KongjingBasicinfo kongjingBasicinfo,
                                         Integer date) {
        String shebei = null;
        if (ObjectUtil.isEmpty(kongjingBasicinfo.getShebeino())) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        } else {
            shebei = kongjingBasicinfo.getShebeino();
        }
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        List list = kongjingBasicinfoService.tongjiKJLevel(shebeilist, date);
        return Result.OK(list);
    }

}
