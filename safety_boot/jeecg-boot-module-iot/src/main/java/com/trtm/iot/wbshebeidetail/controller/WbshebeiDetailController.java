package com.trtm.iot.wbshebeidetail.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.vehiclemanagement.entity.VehicleManagement;
import com.trtm.iot.wbshebeireal.entity.WbshebeiReal;
import com.trtm.iot.wbshebeireal.service.IWbshebeiRealService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.sy.syshrwd.entity.Syshrwd;
import com.trtm.sy.syshrwd.service.ISyshrwdService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wbshebeidetail.entity.WbshebeiDetail;
import com.trtm.iot.wbshebeidetail.service.IWbshebeiDetailService;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 电子锁详情数据表
 * @Author: jeecg-boot
 * @Date: 2022-02-22
 * @Version: V1.0
 */
@Api(tags = "电子锁详情数据表")
@RestController
@RequestMapping("/wbshebeidetail/wbshebeiDetail")
@Slf4j
public class WbshebeiDetailController extends JeecgController<WbshebeiDetail, IWbshebeiDetailService> {
    @Autowired
    private IWbshebeiDetailService wbshebeiDetailService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IWbshebeiRealService wbshebeiRealService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;

    @Autowired
    private ISyshrwdService syshrwdService;

    @Autowired
    private ISysMessageCoreService sysMessageService;

    /**
     * 分页列表查询
     *
     * @param wbshebeiDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-分页列表查询")
    @ApiOperation(value = "电子锁详情数据表-分页列表查询", notes = "电子锁详情数据表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WbshebeiDetail wbshebeiDetail,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        if (wbshebeiDetail.getSbbh() == null) {
//            if (!"null".equals(shebei)) {
//                wbshebeiDetail.setSbbh(shebei);
//            } else {
//                wbshebeiDetail.setSbbh("空");
//            }
//        }
        wbshebeiDetail.setCph("*" + wbshebeiDetail.getCph() + "*");
        if(!StringUtils.isEmpty(sys_depart_orgcode)){
            wbshebeiDetail.setUserdepartid(sys_depart_orgcode + "*");
        }else{
            wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        }

        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
        Page<WbshebeiDetail> page = new Page<WbshebeiDetail>(pageNo, pageSize);
        IPage<WbshebeiDetail> pageList = wbshebeiDetailService.page(page, queryWrapper);
        List<WbshebeiDetail> records = pageList.getRecords();
        for (WbshebeiDetail wbshebeiDetail1 : records) {
            int fcjd = 0;
            if (wbshebeiDetail1.getIsjiesuo() == 0) {
                fcjd = 25;
            }
            if (wbshebeiDetail1.getIsjiesuo() == 1) {
                if (StringUtils.isEmpty(wbshebeiDetail1.getSfhg())) {
                    fcjd = 50;
                } else {
                    if ("合格".equals(wbshebeiDetail1.getSfhg())) {
                        fcjd = 75;
                    }
                    if ("不合格".equals(wbshebeiDetail1.getSfhg())) {
                        fcjd = 100;
                    }
                }
            }
            if (wbshebeiDetail1.getIsjiesuo() == 2) {
                fcjd = 100;
            }
            wbshebeiDetail1.setSjxhsl(String.valueOf(fcjd));
            wbshebeiDetail1.setUserdepartid(wbshebeiDetailService.getDepartName(wbshebeiDetail1.getUserdepartid()));
        }
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param wbshebeiDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-发车单进度")
    @ApiOperation(value = "电子锁详情数据表-发车单进度", notes = "电子锁详情数据表-发车单进度")
    @GetMapping(value = "/jdlist")
    public Result<?> queryPagejdList(WbshebeiDetail wbshebeiDetail,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//        if (wbshebeiDetail.getSbbh() == null) {
//            if (!"null".equals(shebei)) {
//                wbshebeiDetail.setSbbh(shebei);
//            } else {
//                wbshebeiDetail.setSbbh("空");
//            }
//        }
        wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
        WbshebeiDetail wbshebeiDetail1 = wbshebeiDetailService.getOne(queryWrapper);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map.put("title", "解锁");
        map.put("status", 0);
        map.put("content", "");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("title", "审核");
        map1.put("status", 1);
        if (wbshebeiDetail1.getReviewtime() != null) {
            map1.put("content", "车辆待解锁，材料已审核，审核人" + wbshebeiDetail1.getReviewer() + "，审核时间" + sdf.format(wbshebeiDetail1.getReviewtime()));
        } else {
            map1.put("content", "车辆待解锁，材料已审核");
        }
        Map<String, Object> map3 = new HashMap<>();
        map3.put("title", "到达");
        map3.put("status", 1);
        if (wbshebeiDetail1.getDdtime() != null) {
            map3.put("content", "车辆已到达目的地，到达时间" + sdf.format(wbshebeiDetail1.getDdtime()));
        } else {
            map3.put("content", "车辆已到达目的地");
        }
        Map<String, Object> map4 = new HashMap<>();
        map4.put("title", "运输中");
        map4.put("status", 1);
        map4.put("content", "车辆运输中");
        Map<String, Object> map5 = new HashMap<>();
        map5.put("title", "发车申报");
        map5.put("status", 1);
        if (wbshebeiDetail1.getCftime() != null) {
            map5.put("content", "申报人" + wbshebeiDetail1.getFzr() + "，申报时间" + sdf.format(wbshebeiDetail1.getCftime()));
        }
        if (wbshebeiDetail1.getIsjiesuo() == 2) {
            map.put("status", 1);
            if (wbshebeiDetail1.getTimestatime() != null && wbshebeiDetail1.getName() != null) {
                map.put("content", "已完成，解锁人" + wbshebeiDetail1.getName() + "，解锁时间" + sdf.format(wbshebeiDetail1.getTimestatime()));
            } else {
                map.put("content", "已完成");
            }
        } else {
            if (StringUtils.isEmpty(wbshebeiDetail1.getSfhg())) {
                map1.put("status", 0);
                map1.put("content", "");
            }
            if (wbshebeiDetail1.getIsjiesuo() == 1) {
                if ("不合格".equals(wbshebeiDetail1.getSfhg())) {
                    map2.put("title", "退场");
                    map2.put("staus", 1);
                    if (wbshebeiDetail1.getDdtime() != null && wbshebeiDetail1.getReviewer() != null) {
                        map2.put("content", "材料不合格，审核人" + wbshebeiDetail1.getReviewer() + "，审核时间" + sdf.format(wbshebeiDetail1.getReviewtime()));
                    } else {
                        map2.put("content", "材料不合格");
                    }
                }
            } else if (wbshebeiDetail1.getIsjiesuo() == 0) {
                map3.put("status", 0);
                map3.put("content", "");
            }
        }
        if (!"不合格".equals(wbshebeiDetail1.getSfhg())) {
            list.add(map);
            list.add(map1);
        } else {
            list.add(map2);
        }
        list.add(map3);
        list.add(map4);
        list.add(map5);
        return Result.OK(list);
    }

    /**
     * 分页列表查询
     *
     * @param wbshebeiDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-分页列表查询")
    @ApiOperation(value = "电子锁详情数据表-分页列表查询", notes = "电子锁详情数据表-分页列表查询")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(WbshebeiDetail wbshebeiDetail,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//        if (wbshebeiDetail.getSbbh() == null) {
//            if (!"null".equals(shebei)) {
//                wbshebeiDetail.setSbbh(shebei);
//            } else {
//                wbshebeiDetail.setSbbh("空");
//            }
//        }
        wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
        Page<WbshebeiDetail> page = new Page<WbshebeiDetail>(pageNo, pageSize);
        IPage<WbshebeiDetail> pageList = wbshebeiDetailService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询审核
     *
     * @param wbshebeiDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-审核")
    @ApiOperation(value = "电子锁详情数据表-审核", notes = "电子锁详情数据表-审核")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(WbshebeiDetail wbshebeiDetail,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        if (wbshebeiDetail.getSbbh() == null) {
//            if (!"null".equals(shebei)) {
//                wbshebeiDetail.setSbbh(shebei);
//            } else {
//                wbshebeiDetail.setSbbh("空");
//            }
//        }
        wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
        queryWrapper.isNull("SFHG");
        Page<WbshebeiDetail> page = new Page<WbshebeiDetail>(pageNo, pageSize);
        IPage<WbshebeiDetail> pageList = wbshebeiDetailService.page(page, queryWrapper);
        List<WbshebeiDetail> records = pageList.getRecords();
        for (WbshebeiDetail wbshebeiDetail1 : records) {
            int fcjd = 0;
            if (wbshebeiDetail1.getIsjiesuo() == 0) {
                fcjd = 25;
            }
            if (wbshebeiDetail1.getIsjiesuo() == 1) {
                if (StringUtils.isEmpty(wbshebeiDetail1.getSfhg())) {
                    fcjd = 50;
                } else {
                    if ("合格".equals(wbshebeiDetail1.getSfhg())) {
                        fcjd = 75;
                    }
                    if ("不合格".equals(wbshebeiDetail1.getSfhg())) {
                        fcjd = 100;
                    }
                }
            }
            if (wbshebeiDetail1.getIsjiesuo() == 2) {
                fcjd = 100;
            }
            wbshebeiDetail1.setSjxhsl(String.valueOf(fcjd));
        }
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询统计
     *
     * @param wbshebeiDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-统计")
    @ApiOperation(value = "电子锁详情数据表-统计", notes = "电子锁详情数据表-统计")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(WbshebeiDetail wbshebeiDetail,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        if (wbshebeiDetail.getSbbh() == null) {
//            if (!"null".equals(shebei)) {
//                wbshebeiDetail.setSbbh(shebei);
//            } else {
//                wbshebeiDetail.setSbbh("空");
//            }
//        }
        wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
        queryWrapper.select("sum(case when isjiesuo =1 and SFHG is null then 1 else 0 end) as arrivesum," +
                "sum(case when SFHG = '合格' and isjiesuo = 1 then 1 else 0 end) as hege," +
                "sum(case when isjiesuo = 2 then 1 else 0 end) as finish," +
                "sum(case when isjiesuo = 0 then 1 else 0 end) as noarrive," +
                "sum(case when SFHG = '不合格' then 1 else 0 end) as failed," +
                "ifnull(count(*),0) as arrivednosh");
        Map<String, Object> pageList = wbshebeiDetailService.getMap(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param wbshebeiDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-分页列表查询")
    @ApiOperation(value = "电子锁详情数据表-分页列表查询", notes = "电子锁详情数据表-分页列表查询")
    @GetMapping(value = "/reallist")
    public Result<?> queryPagerealList(WbshebeiDetail wbshebeiDetail,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        if (wbshebeiDetail.getSbbh() == null) {
//            if (!"null".equals(shebei)) {
//                wbshebeiDetail.setSbbh(shebei);
//            } else {
//                wbshebeiDetail.setSbbh("空");
//            }
//        }
        wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
        List<WbshebeiDetail> pageList = wbshebeiDetailService.list(queryWrapper);
        for (WbshebeiDetail wbshebeiDetail1 : pageList) {
            QueryWrapper<WbshebeiReal> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("teid", wbshebeiDetail1.getSbbh());
            WbshebeiReal one = wbshebeiRealService.getOne(queryWrapper1);
            if (one != null) {
                wbshebeiDetail1.setDdlat(one.getLat());
                wbshebeiDetail1.setDdlng(one.getLng());
            }
        }

        queryWrapper.select("ifnull(sum(jcsl),0) as jcsl," +
                "sum(case when isjiesuo = 0 then 1 else 0 end) as noarrive," +
                "sum(case when isjiesuo > 0 then 1 else 0 end) as arrived");
        Map<String, Object> data = wbshebeiDetailService.getMap(queryWrapper);
        Result<Object> r = new Result<>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setResult(pageList);
        r.setData(data);
        return r;
    }

    /**
     * 电子锁发车单信息统计
     *
     * @param wbshebeiDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "电子锁发车单信息统计")
    @ApiOperation(value = "电子锁发车单信息统计", notes = "电子锁发车单信息统计")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList4(WbshebeiDetail wbshebeiDetail,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
        queryWrapper.select("sum(jcsl) as jcsl,cailiao,mdd");
        queryWrapper.groupBy("cailiao,mdd");
        List<WbshebeiDetail> list = wbshebeiDetailService.list(queryWrapper);
        return Result.OK(list);
    }

    @AutoLog(value = "电子锁发车单信息统计")
    @ApiOperation(value = "电子锁发车单信息统计", notes = "电子锁发车单信息统计")
    @GetMapping(value = "/list5")
    public Result<?> queryPageList5(WbshebeiDetail wbshebeiDetail,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
        queryWrapper.select("sum(jcsl) as jcsl,cailiao,mdd");
        queryWrapper.groupBy("mdd");
        List<WbshebeiDetail> list = wbshebeiDetailService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 发车和已解锁发车单数据查询
     *
     * @param wbshebeiDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-发车和已解锁发车单数据查询")
    @ApiOperation(value = "电子锁详情数据表-发车和已解锁发车单数据查询", notes = "电子锁详情数据表-发车和已解锁发车单数据查询")
    @GetMapping(value = "/newlist")
    public Result<?> queryPagenewList(WbshebeiDetail wbshebeiDetail,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        if (wbshebeiDetail.getSbbh() == null) {
//            if (!"null".equals(shebei)) {
//                wbshebeiDetail.setSbbh(shebei);
//            } else {
//                wbshebeiDetail.setSbbh("空");
//            }
//        }
        wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
        queryWrapper.eq("isjiesuo", 0);
        queryWrapper.last("or (isjiesuo = 2 and timestatime >NOW()-INTERVAL 1 HOUR)  ORDER BY id");
        List<WbshebeiDetail> pageList = wbshebeiDetailService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 发车单数据统计
     *
     * @param wbshebeiDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-发车单数据统计")
    @ApiOperation(value = "电子锁详情数据表-发车单数据统计", notes = "电子锁详情数据表-发车单数据统计")
    @GetMapping(value = "/stalist")
    public Result<?> queryPagestaList(WbshebeiDetail wbshebeiDetail,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        if (wbshebeiDetail.getSbbh() == null) {
//            if (!"null".equals(shebei)) {
//                wbshebeiDetail.setSbbh(shebei);
//            } else {
//                wbshebeiDetail.setSbbh("空");
//            }
//        }
        wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
        queryWrapper.select("sum(case when isjiesuo = 0 and zzstatus = 1 then 1 else 0 end) as zzarrived," +
                "sum(case when isjiesuo = 0 then 1 else 0 end) as noarrive," +
                "sum(case when isjiesuo > 0 then 1 else 0 end) as arrived," +
                "sum(case when SFHG = '不合格' then 1 else 0 end) as failed," +
                "sum(case when isjiesuo =1 and SFHG is null then 1 else 0 end) as nosharrived," +
                "sum(case when SFHG = '合格' then 1 else 0 end) as nojiesuo");
        Map<String, Object> data = wbshebeiDetailService.getMap(queryWrapper);
        return Result.OK(data);
    }

    @AutoLog(value = "电子锁发车单信息统计-根据材料类型统计运输数量")
    @ApiOperation(value = "电子锁发车单信息统计-根据材料类型统计运输数量", notes = "电子锁发车单信息统计-根据材料类型统计运输数量")
    @GetMapping(value = "/cailiaolist")
    public Result<?> queryPagecailiaoList(WbshebeiDetail wbshebeiDetail,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
        queryWrapper.select("ifnull(sum(jcsl),0) as jcsl,cailiao");
        queryWrapper.groupBy("cailiao");
        List<WbshebeiDetail> list = wbshebeiDetailService.list(queryWrapper);
        return Result.OK(list);
    }

    @AutoLog(value = "电子锁发车单信息统计-根据材料/供应商统计运输数量")
    @ApiOperation(value = "电子锁发车单信息统计-根据材料/供应商统计运输数量", notes = "电子锁发车单信息统计-根据材料/供应商统计运输数量")
    @GetMapping(value = "/liststa")
    public Result<?> queryPageListSta(WbshebeiDetail wbshebeiDetail,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
        queryWrapper.select("ifnull(sum(jcsl),0) as jcsl,cailiao,ghdw");
        queryWrapper.groupBy("cailiao,ghdw");
        List<WbshebeiDetail> list = wbshebeiDetailService.list(queryWrapper);
        return Result.OK(list);
    }

    @AutoLog(value = "电子锁发车单信息统计-根据材料类型统计运输数量")
    @ApiOperation(value = "电子锁发车单信息统计-根据材料类型统计运输数量", notes = "电子锁发车单信息统计-根据材料类型统计运输数量")
    @GetMapping(value = "/cailiaolists")
    public Result<?> queryPagecailiaoLists(WbshebeiDetail wbshebeiDetail,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req, Integer date) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        String format3 = format.format(new Date());
        String format4 = ft.format(new Date());
        QueryWrapper<WbshebeiDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("ifnull(sum(jcsl),0) as jcsl,cailiao");
        if (!StringUtils.isEmpty(wbshebeiDetail.getUserdepartid())) {
            queryWrapper.likeRight("userdepartid", wbshebeiDetail.getUserdepartid());
        } else {
            queryWrapper.likeRight("userdepartid", loginUser.getOrgCode());
        }
        if (!StringUtils.isEmpty(wbshebeiDetail.getGhdw())) {
            queryWrapper.eq("ghdw", wbshebeiDetail.getGhdw());
        }
        if (!StringUtils.isEmpty(wbshebeiDetail.getMdd())) {
            queryWrapper.eq("mdd", wbshebeiDetail.getMdd());
        }
        if (!StringUtils.isEmpty(wbshebeiDetail.getCailiao())) {
            queryWrapper.eq("cailiao", wbshebeiDetail.getCailiao());
        }
        if (StringUtils.isEmpty(date)) {
            queryWrapper.groupBy("cailiao");
        } else if (date == 0) {
            queryWrapper.last("and CFTime like '" + format4 + "%' group by cailiao");//当天
        } else if (date == 1) {
            queryWrapper.last("and CFTime like '" + format2 + "%' and YEARWEEK(date_format(CFTime,'%Y-%m-%d')) = YEARWEEK(now()) group by cailiao");//本周
        } else if (date == 2) {
            queryWrapper.last("and CFTime like '" + format3 + "%' group by cailiao");//本月
        }
        List<WbshebeiDetail> list = wbshebeiDetailService.list(queryWrapper);
        return Result.OK(list);
    }

    @AutoLog(value = "电子锁发车单信息统计-根据材料类型统计运输数量")
    @ApiOperation(value = "电子锁发车单信息统计-根据材料类型统计运输数量", notes = "电子锁发车单信息统计-根据材料类型统计运输数量")
    @GetMapping(value = "/list6")
    public Result<?> queryPageList6(WbshebeiDetail wbshebeiDetail,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        wbshebeiDetail.setUserdepartid(loginUser.getOrgCode() + "*");
        QueryWrapper<Wzcailiaonamedict> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.likeRight("sys_org_code", loginUser.getOrgCode());
        queryWrapper3.eq("iselocks", 1);
        List<Wzcailiaonamedict> list1 = wzcailiaonamedictService.list(queryWrapper3);
        List list = new ArrayList();
        for (Wzcailiaonamedict wzcailiaonamedict : list1) {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map1 = new HashMap<>();
            QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
            queryWrapper.select("ifnull(sum(jcsl),0) as jcsl");
            queryWrapper.eq("SFHG", "不合格");
            queryWrapper.eq("cailiao", wzcailiaonamedict.getCailiaoname());
            WbshebeiDetail one = wbshebeiDetailService.getOne(queryWrapper);
            QueryWrapper<WbshebeiDetail> queryWrapper1 = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
            queryWrapper1.select("ifnull(sum(jcsl),0) as jcsl");
            queryWrapper1.eq("SFHG", "合格");
            queryWrapper1.eq("cailiao", wzcailiaonamedict.getCailiaoname());
            WbshebeiDetail one1 = wbshebeiDetailService.getOne(queryWrapper1);
            if (one != null) {
                map1.put("buhege", one.getJcsl());
            } else {
                map1.put("buhege", 0);
            }
            if (one1 != null) {
                map1.put("hege", one1.getJcsl());
            } else {
                map1.put("hege", 0);
            }
            map.put("cailiao", wzcailiaonamedict.getCailiaoname());
            map.put("data", map1);
            list.add(map);
        }
        return Result.OK(list);
    }

    /**
     * 车单统计到达次数
     *
     * @param wbshebeiDetail
     * @param sys_depart_orgcode
     * @return
     */
    @AutoLog(value = "车单统计车辆到达次数")
    @ApiOperation(value = "车单统计车辆到达次数", notes = "车单统计车辆到达次数")
    @GetMapping(value = "/list7")
    public Result<?> queryPageList7(WbshebeiDetail wbshebeiDetail,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wbshebeiDetail.getSbbh() == null) {
            if (!"null".equals(shebei)) {
                wbshebeiDetail.setSbbh(shebei);
            } else {
                wbshebeiDetail.setSbbh("空");
            }
        }
        QueryWrapper<WbshebeiDetail> queryWrapper = QueryGenerator.initQueryWrapper(wbshebeiDetail, req.getParameterMap());
        queryWrapper.select("id,userdepartid,date_format(cftime,'%Y-%m-%d') as days,cftime,fzr,mdd,cph,count(zhuangtai) as fctype");
        queryWrapper.eq("zhuangtai", 1);
        queryWrapper.groupBy("days,cph");
        queryWrapper.orderByDesc("days");
        Page<WbshebeiDetail> objectPage = new Page<>(pageNo, pageSize);
        IPage<WbshebeiDetail> page = wbshebeiDetailService.page(objectPage, queryWrapper);
        return Result.OK(page);
    }

    /**
     * 添加
     *
     * @param wbshebeiDetail
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-添加")
    @ApiOperation(value = "电子锁详情数据表-添加", notes = "电子锁详情数据表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WbshebeiDetail wbshebeiDetail) {
        wbshebeiDetailService.save(wbshebeiDetail);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param wbshebeiDetail
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-添加")
    @ApiOperation(value = "电子锁详情数据表-添加", notes = "电子锁详情数据表-添加")
    @PostMapping(value = "/add1")
    public Result<?> add1(@RequestBody WbshebeiDetail wbshebeiDetail) {
        wbshebeiDetail.setCftime(new Date());
        String strDateFormat = "yyyyMMddHHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        wbshebeiDetail.setDanhao("FC" + sdf.format(new Date()));
        wbshebeiDetailService.save(wbshebeiDetail);
        // 追加短信发送（需关联发货任务单号）
        if (null != wbshebeiDetail.getFctype() && wbshebeiDetail.getFctype() == 1) {
            Syshrwd byrwd = syshrwdService.findByrwd(wbshebeiDetail.getShrwdh());
            // 发货时给施工单位发送短信
            if (!StringUtils.isEmpty(byrwd.getCreatephone())) {
                SysMessageCore sysMessageCore = new SysMessageCore();
                sysMessageCore.setEsTitle("原材发车");
                sysMessageCore.setEsType("1");
                sysMessageCore.setEsSendStatus("0");
                sysMessageCore.setEsReceiver(byrwd.getCreatephone());
                JSONObject obj = new JSONObject();
                obj.put("sbname", wbshebeiDetail.getGhdw());
                obj.put("time", wbshebeiDetail.getEstimatetime());
                obj.put("content", wbshebeiDetail.getCailiao() + wbshebeiDetail.getGuige() + "已发货一车" + "请做好收货检查准备");
                sysMessageCore.setEsContent(obj.toString());
                sysMessageService.save(sysMessageCore);
            }
            // 发货时给监理单位发送短信
            if (!StringUtils.isEmpty(byrwd.getCreatephone())) {
                SysMessageCore sysMessageCore = new SysMessageCore();
                sysMessageCore.setEsTitle("原材发车");
                sysMessageCore.setEsType("1");
                sysMessageCore.setEsSendStatus("0");
                sysMessageCore.setEsReceiver(byrwd.getJlphone());
                JSONObject obj = new JSONObject();
                obj.put("sbname", wbshebeiDetail.getGhdw());
                obj.put("time", wbshebeiDetail.getEstimatetime());
                obj.put("content", wbshebeiDetail.getCailiao() + wbshebeiDetail.getGuige() + "已发货一车" + "可进行抽检委托确认");
                sysMessageCore.setEsContent(obj.toString());
                sysMessageService.save(sysMessageCore);
            }

        }
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wbshebeiDetail
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-编辑")
    @ApiOperation(value = "电子锁详情数据表-编辑", notes = "电子锁详情数据表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WbshebeiDetail wbshebeiDetail) {
        wbshebeiDetailService.updateById(wbshebeiDetail);
        return Result.OK("编辑成功!");
    }

    /**
     * 编辑
     *
     * @param wbshebeiDetail
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-编辑")
    @ApiOperation(value = "电子锁详情数据表-编辑", notes = "电子锁详情数据表-编辑")
    @PutMapping(value = "/edit1")
    public Result<?> edit1(@RequestBody WbshebeiDetail wbshebeiDetail) {
        Integer zhuangtai = wbshebeiDetail.getZhuangtai();
        if (zhuangtai != null) {
            if (zhuangtai > 0) {//到达时赋予到达时间
                wbshebeiDetail.setDdtime(new Date());
            }
        }
        wbshebeiDetailService.updateById(wbshebeiDetail);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-通过id删除")
    @ApiOperation(value = "电子锁详情数据表-通过id删除", notes = "电子锁详情数据表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wbshebeiDetailService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-批量删除")
    @ApiOperation(value = "电子锁详情数据表-批量删除", notes = "电子锁详情数据表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wbshebeiDetailService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "电子锁详情数据表-通过id查询")
    @ApiOperation(value = "电子锁详情数据表-通过id查询", notes = "电子锁详情数据表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WbshebeiDetail wbshebeiDetail = wbshebeiDetailService.getById(id);
        if (wbshebeiDetail == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wbshebeiDetail);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wbshebeiDetail
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WbshebeiDetail wbshebeiDetail) {
        return super.exportXls(request, wbshebeiDetail, WbshebeiDetail.class, "电子锁详情数据表");
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
        return super.importExcel(request, response, WbshebeiDetail.class);
    }

}
