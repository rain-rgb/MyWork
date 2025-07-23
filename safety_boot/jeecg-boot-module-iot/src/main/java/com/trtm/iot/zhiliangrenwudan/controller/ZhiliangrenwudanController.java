package com.trtm.iot.zhiliangrenwudan.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.trtm.iot.cunliangprocedureconfig.entity.CunliangProcedureConfig;
import com.trtm.iot.cunliangprocedureconfig.service.ICunliangProcedureConfigService;
import com.trtm.iot.kanbaninfo.entity.Kanbaninfo;
import com.trtm.iot.kanbaninfo.service.IKanbaninfoService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhilianggongxu.service.IZhiliangGongxuService;
import com.trtm.iot.zhiliangqrcode.entity.Zhiliangqrcode;
import com.trtm.iot.zhiliangqrcode.service.IZhiliangqrcodeService;
import com.trtm.iot.zhiliangrenwudan.vo.ZhiliangRWD;
import com.trtm.iot.zhiliangtaizuocfg.entity.ZhiliangTaizuoCfg;
import com.trtm.iot.zhiliangtaizuocfg.service.IZhiliangTaizuoCfgService;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ConverUtils;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 任务单（制梁）表信息
 * @Author: jeecg-boot
 * @Date: 2021-08-13
 * @Version: V1.0
 */
@Api(tags = "任务单（制梁）表信息")
@RestController
@RequestMapping("/zhiliangrenwudan/zhiliangrenwudan")
@Slf4j
public class ZhiliangrenwudanController extends JeecgController<Zhiliangrenwudan, IZhiliangrenwudanService> {
    @Autowired
    private IZhiliangrenwudanService zhiliangrenwudanService;
    @Autowired
    private IZhiliangGongxuService zhiliangGongxuService;
    @Autowired
    private ICunliangProcedureConfigService cunliangProcedureConfigService;
    @Autowired
    private IZhiliangqrcodeService zhiliangqrcodeService;
    @Autowired
    private IZhiliangTaizuoCfgService zhiliangTaizuoCfgService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IKanbaninfoService kanbaninfoService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;

    /**
     * 分页列表查询
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-分页列表查询")
    @ApiOperation(value = "任务单（制梁）表信息-分页列表查询", notes = "任务单（制梁）表信息-分页列表查询")
    @GetMapping(value = "/listlcyj")
    public Result<?> queryPageListlcyj(Zhiliangrenwudan zhiliangrenwudan,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String orgCode = loginUser.getOrgCode();
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }else {
            zhiliangrenwudan.setSysOrgCode(orgCode + "*");
        }
        zhiliangrenwudan.setIsdel(0);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 分页列表查询
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-分页列表查询")
    @ApiOperation(value = "任务单（制梁）表信息-分页列表查询", notes = "任务单（制梁）表信息-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "zhiliang/zhiliangrenwudan/ZhiliangrenwudanList")
    public Result<?> queryPageList(Zhiliangrenwudan zhiliangrenwudan,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setCode("*" + zhiliangrenwudan.getCode() + "*");
        zhiliangrenwudan.setConspos("*" + zhiliangrenwudan.getConspos() + "*");
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);

        if (zhiliangrenwudan.getProjgrade() != null) {
            zhiliangrenwudan.setProjgrade(zhiliangrenwudan.getProjgrade() + "*");
        }
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
//        queryWrapper.ne("create_by","18658530015");
        Page<Zhiliangrenwudan> page = new Page<Zhiliangrenwudan>(pageNo, pageSize);
        IPage<Zhiliangrenwudan> pageList = zhiliangrenwudanService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 架梁系统
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-分页列表查询")
    @ApiOperation(value = "任务单（制梁）表信息-分页列表查询", notes = "任务单（制梁）表信息-分页列表查询")
    @GetMapping(value = "/listjlxt")
    public Result<?> queryPageListjlxt(Zhiliangrenwudan zhiliangrenwudan,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
//        queryWrapper.ne("create_by","18658530015");
        Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
        ZhiliangRWD zhiliangRWD = new ZhiliangRWD();
        zhiliangRWD.setZhiliangrenwudan(one);
        if (one != null){
            List<ZhiliangGongxu> selectgongxu = zhiliangGongxuService.selectgongxu(one.getUuid());
            zhiliangRWD.setZhiliangGongxuList(selectgongxu);
        }
        return Result.OK(zhiliangRWD);
    }

    /**
     * 金华大屏未生产梁数
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-分页列表查询")
    @ApiOperation(value = "任务单（制梁）表信息-分页列表查询", notes = "任务单（制梁）表信息-分页列表查询")
    @GetMapping(value = "/listWsc")
    @PermissionData(pageComponent = "zhiliang/zhiliangrenwudan/ZhiliangrenwudanList")
    public Result<?> queryPageListWsc(Zhiliangrenwudan zhiliangrenwudan,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                      HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);

        QueryWrapper<Kanbaninfo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("sys_org_code", sys_depart_orgcode);
        queryWrapper1.eq("type", 1);
        Kanbaninfo one = kanbaninfoService.getOne(queryWrapper1);
        String allcount = one.getAllcount();
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        int sum = Integer.parseInt(allcount) - list.size();
        return Result.OK(sum);
    }

    /**
     * 金华已生产梁数
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-分页列表查询")
    @ApiOperation(value = "任务单（制梁）表信息-分页列表查询", notes = "任务单（制梁）表信息-分页列表查询")
    @GetMapping(value = "/listYsc")
    @PermissionData(pageComponent = "zhiliang/zhiliangrenwudan/ZhiliangrenwudanList")
    public Result<?> queryPageListYsc(Zhiliangrenwudan zhiliangrenwudan,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                      HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);

        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        return Result.OK(list.size());
    }



    @AutoLog(value = "任务单（制梁）表信息-分页列表查询")
    @ApiOperation(value = "任务单（制梁）表信息-分页列表查询", notes = "任务单（制梁）表信息-分页列表查询")
    @GetMapping(value = "/listYscxc")
    public Result<?> queryPageListYscxc(String sys_depart_orgcode) {
        List<ZhiliangGongxu> selectgongxu = null;
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            Zhiliangrenwudan zhiliangrenwudan = zhiliangrenwudanService.selectbyorgCode(sys_depart_orgcode);
            if (zhiliangrenwudan.getUuid() != null){
                selectgongxu = zhiliangGongxuService.selectgongxu(zhiliangrenwudan.getUuid());
            }
        }
        return Result.OK(selectgongxu);
    }
    /**
     * 平台确认工序
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-分页列表查询")
    @ApiOperation(value = "任务单（制梁）表信息-分页列表查询", notes = "任务单（制梁）表信息-分页列表查询")
    @GetMapping(value = "/GXlist")
    @PermissionData(pageComponent = "zhiliang/zhiliangrenwudan/ZhiliangrenwudanList")
    public Result<?> queryPageGXlist(Zhiliangrenwudan zhiliangrenwudan,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setCode("*" + zhiliangrenwudan.getCode() + "*");
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);

        if (zhiliangrenwudan.getProjgrade() != null) {
            zhiliangrenwudan.setProjgrade(zhiliangrenwudan.getProjgrade() + "*");
        }
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);

        List<String> list1 = new ArrayList<>();
        for (Zhiliangrenwudan l : list) {
            String uuid = "";
            uuid = l.getUuid();
            list1.add(uuid);
        }
        Page<ZhiliangGongxu> page1 = null;
        try {
            QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.in("uuid", list1);
            queryWrapper1.ne("status", 2);
            queryWrapper1.ne("create_by", "18658530015");
            Page<ZhiliangGongxu> page = new Page<ZhiliangGongxu>(pageNo, pageSize);
            page1 = zhiliangGongxuService.page(page, queryWrapper1);
        } catch (Exception e) {
            Page<ZhiliangGongxu> page = new Page<ZhiliangGongxu>(pageNo, pageSize);
            return Result.OK(page);
        }
        return Result.OK(page1);
    }

    /**
     * 分页列表查询(微信扫码)
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-分页列表查询(微信扫码)")
    @ApiOperation(value = "任务单（制梁）表信息-分页列表查询(微信扫码)", notes = "任务单（制梁）表信息-分页列表查询(微信扫码)")
    @GetMapping(value = "/hlist")
    public Result<?> queryPagehList(Zhiliangrenwudan zhiliangrenwudan,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        Page<Zhiliangrenwudan> page = new Page<Zhiliangrenwudan>(pageNo, pageSize);
        IPage<Zhiliangrenwudan> pageList = zhiliangrenwudanService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询(微信扫码2.0)
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-分页列表查询(微信扫码)")
    @ApiOperation(value = "任务单（制梁）表信息-分页列表查询(微信扫码)", notes = "任务单（制梁）表信息-分页列表查询(微信扫码)")
    @GetMapping(value = "/zyhhlist")
    public Result<?> queryPagehzyhList(Zhiliangrenwudan zhiliangrenwudan,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        Page<Zhiliangrenwudan> page = new Page<Zhiliangrenwudan>(pageNo, pageSize);
        IPage<Zhiliangrenwudan> pageList = zhiliangrenwudanService.page(page, queryWrapper);
        List<Zhiliangrenwudan> records = pageList.getRecords();
        if (records.size() > 0){
            Zhiliangrenwudan zhiliangrenwudan1 = records.get(0);
            QueryWrapper<Bhzrenwudan> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("ConsPos",zhiliangrenwudan1.getConspos());
            List<Bhzrenwudan> list = bhzrenwudanService.list(queryWrapper1);
            if (list.size() > 0){
                Bhzrenwudan bhzrenwudan = list.get(0);
                zhiliangrenwudan1.setBegtim(bhzrenwudan.getBegtim());
            }
            ZhiliangGongxu zhangla = zhiliangGongxuService.selectuuid(zhiliangrenwudan1.getUuid(), 5);
            ZhiliangGongxu yajiang = zhiliangGongxuService.selectuuid(zhiliangrenwudan1.getUuid(), 10);
            if (zhangla.getFinishtime() != null && zhangla.getResponsible() != null){
                zhiliangrenwudan1.setProductiontime(zhangla.getFinishtime());
                zhiliangrenwudan1.setAttamper(zhangla.getResponsible());
            }
            if (yajiang.getFinishtime() != null && yajiang.getResponsible() != null){
                zhiliangrenwudan1.setEndtim(yajiang.getFinishtime());
                zhiliangrenwudan1.setFlag(yajiang.getResponsible());
            }
            String conspos = zhiliangrenwudan1.getConspos();
            StringBuilder stringBuilder = new StringBuilder(conspos);
            StringBuilder reverse = stringBuilder.reverse();
            String qiao = reverse.substring(reverse.indexOf("桥"));
            if (qiao.length() > 0){
                StringBuilder substring = new StringBuilder(qiao.substring(0, qiao.indexOf(" ")));
                StringBuilder reverse1 = substring.reverse();
                zhiliangrenwudan1.setCreateBy(String.valueOf(reverse1));
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-分页列表查询")
    @ApiOperation(value = "任务单（制梁）表信息-分页列表查询", notes = "任务单（制梁）表信息-分页列表查询")
    @GetMapping(value = "/ydlist")
    @PermissionData(pageComponent = "zhiliang/zhiliangrenwudan/ZhiliangrenwudanYdList")
    public Result<?> queryPageListyd(Zhiliangrenwudan zhiliangrenwudan,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setCode("*" + zhiliangrenwudan.getCode() + "*");
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        Page<Zhiliangrenwudan> page = new Page<Zhiliangrenwudan>(pageNo, pageSize);
        IPage<Zhiliangrenwudan> pageList = zhiliangrenwudanService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 审核分页列表查询
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-审核分页列表查询")
    @ApiOperation(value = "任务单（制梁）表信息-审核分页列表查询", notes = "任务单（制梁）表信息-审核分页列表查询")
    @GetMapping(value = "/shlist")
    @PermissionData(pageComponent = "zhiliang/zhiliangrenwudan/ZhiliangrenwudanSHList")
    public Result<?> shqueryPageList(Zhiliangrenwudan zhiliangrenwudan,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {

        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setCode("*" + zhiliangrenwudan.getCode() + "*");
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(0);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        Page<Zhiliangrenwudan> page = new Page<Zhiliangrenwudan>(pageNo, pageSize);
        IPage<Zhiliangrenwudan> pageList = zhiliangrenwudanService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 审核分页列表查询
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-审核分页列表查询")
    @ApiOperation(value = "任务单（制梁）表信息-审核分页列表查询", notes = "任务单（制梁）表信息-审核分页列表查询")
    @GetMapping(value = "/ydshlist")
    @PermissionData(pageComponent = "zhiliang/zhiliangrenwudan/ZhiliangrenwudanYdSHList")
    public Result<?> shqueryPageydList(Zhiliangrenwudan zhiliangrenwudan,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                       HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode("*" + sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setCode("*" + zhiliangrenwudan.getCode() + "*");
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(0);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        Page<Zhiliangrenwudan> page = new Page<Zhiliangrenwudan>(pageNo, pageSize);
        IPage<Zhiliangrenwudan> pageList = zhiliangrenwudanService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 金华大屏张拉压浆统计接口
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-审核分页列表查询")
    @ApiOperation(value = "任务单（制梁）表信息-审核分页列表查询", notes = "任务单（制梁）表信息-审核分页列表查询")
    @GetMapping(value = "/jhlist")
    @PermissionData(pageComponent = "zhiliang/zhiliangrenwudan/ZhiliangrenwudanYdSHList")
    public Result<?> shqueryPageydListjh(Zhiliangrenwudan zhiliangrenwudan,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                         HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());

        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        int i = 0;
        for (Zhiliangrenwudan l : list) {
            QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("uuid", l.getUuid()).eq("xuhao", 10).eq("status", 2);
            ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper1);
            if (one != null) {
                i++;
            }
        }
        return Result.OK(i);
    }

    /**
     * 当前任务单查询(总数)
     * /**
     * 分页列表查询
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-任务单查询(总数)")
    @ApiOperation(value = "任务单（制梁）表信息-任务单查询(总数)", notes = "任务单（制梁）表信息-任务单查询(总数)")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(Zhiliangrenwudan zhiliangrenwudan, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            zhiliangrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        zhiliangrenwudan.setCode("*" + zhiliangrenwudan.getCode() + "*");
        zhiliangrenwudan.setIsdel(0);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        int zong = 0;
        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        for (Zhiliangrenwudan zhiliangrenwudan1 : list) {
            String uuid = zhiliangrenwudan1.getUuid();
            QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("uuid", uuid);
            queryWrapper1.eq("xuhao", 7);
            ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper1);
            if (one != null) {
                Integer status = one.getStatus();
                if (status == 1 || status == 2) {
                    zong += 1;
                }
            }

        }
        return Result.OK(zong);
    }

    @AutoLog(value = "任务单（制梁）表信息-任务单查询(总数)")
    @ApiOperation(value = "任务单（制梁）表信息-任务单查询(总数)", notes = "任务单（制梁）表信息-任务单查询(总数)")
    @GetMapping(value = "/list13")
    public Result<?> queryPageList13(Zhiliangrenwudan zhiliangrenwudan, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            zhiliangrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        zhiliangrenwudan.setCode("*" + zhiliangrenwudan.getCode() + "*");
        zhiliangrenwudan.setIsdel(0);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        queryWrapper.select("count(*) as Flag");
        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        return Result.OK(list);
    }

    @AutoLog(value = "任务单（制梁）表信息-进度查询")
    @ApiOperation(value = "任务单（制梁）表信息-进度查询", notes = "任务单（制梁）表信息-进度查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(Zhiliangrenwudan zhiliangrenwudan,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            zhiliangrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        zhiliangrenwudan.setCode("*" + zhiliangrenwudan.getCode() + "*");
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        queryWrapper.orderByDesc("id");
        Page<Zhiliangrenwudan> page = new Page<Zhiliangrenwudan>(pageNo, pageSize);
        IPage<Zhiliangrenwudan> pageList = zhiliangrenwudanService.page(page, queryWrapper);
        List<Zhiliangrenwudan> zhiliangrenwudanList = pageList.getRecords();
        for (Zhiliangrenwudan zhiliangrenwudan1 : zhiliangrenwudanList) {
            QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("uuid", zhiliangrenwudan1.getUuid());
            queryWrapper1.ne("xuhao", 8);
            queryWrapper1.select("count(*) as status");
            ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper1);
            Double status = Double.valueOf(one.getStatus());
            QueryWrapper<ZhiliangGongxu> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("uuid", zhiliangrenwudan1.getUuid());
            queryWrapper2.eq("status", 2);
            queryWrapper2.select("count(*) as status1");
            ZhiliangGongxu one1 = zhiliangGongxuService.getOne(queryWrapper2);
            Double status1 = Double.valueOf(one1.getStatus1());
            Double flag = 0.0;
            if (status != 0) {
                flag = (status1 / status) * 100;
            }
            zhiliangrenwudan1.setFlag(String.format("%.2f", flag));
        }
        return Result.OK(pageList);
    }


    /**
     * 当前任务单查询(本月总数)
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-任务单查询(本月总数)")
    @ApiOperation(value = "任务单（制梁）表信息-任务单查询(本月总数)", notes = "任务单（制梁）表信息-任务单查询(本月总数)")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(Zhiliangrenwudan zhiliangrenwudan,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            zhiliangrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        zhiliangrenwudan.setCode("*" + zhiliangrenwudan.getCode() + "*");
        zhiliangrenwudan.setIsdel(0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
        String day1 = format.format(c.getTime());
        Date parse = null;//本月第一天
        Date parse1 = null;//本月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String day2 = format.format(ca.getTime());
        try {
            parse = format.parse(day1);
            parse1 = format.parse(day2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
//        queryWrapper.select("count(*) as Flag");
        queryWrapper.ge("DatTim", parse);
        queryWrapper.le("DatTim", parse1);
        Integer zong = 0;
        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        for (Zhiliangrenwudan zhiliangrenwudan1 : list) {
            String uuid = zhiliangrenwudan1.getUuid();
            QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("uuid", uuid);
            queryWrapper1.eq("xuhao", 7);
            ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper1);
            if (one != null) {
                Integer status = one.getStatus();
                if (status == 1 || status == 2) {
                    zong += 1;
                }
            }

        }
        return Result.OK(zong);
    }

    /**
     * 制梁混凝土(本月总数)
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-制梁混凝土(本月总数)")
    @ApiOperation(value = "任务单（制梁）表信息-制梁混凝土(本月总数)", notes = "任务单（制梁）表信息-制梁混凝土(本月总数)")
    @GetMapping(value = "/listHnt")
    public Result<?> queryPageListHnt(Zhiliangrenwudan zhiliangrenwudan,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        zhiliangrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        zhiliangrenwudan.setCode("*" + zhiliangrenwudan.getCode() + "*");
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM");
        String format3 = ft1.format(new Date());
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        queryWrapper.select("round(sum(Mete),2) as Mete");
        queryWrapper.likeRight("productiontime", format3);
        Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
        if (one == null) {
            Zhiliangrenwudan one1 = new Zhiliangrenwudan();
            one1.setMete(0.0);
            return Result.OK(one1);
        }
        return Result.OK(one);
    }

    /**
     * 当前任务单查询(已审核总数)
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-任务单查询(已审核未审核总数)")
    @ApiOperation(value = "任务单（制梁）表信息-任务单查询(已审核未审核总数)", notes = "任务单（制梁）表信息-任务单查询(已审核未审核总数)")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList4(Zhiliangrenwudan zhiliangrenwudan,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            zhiliangrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        zhiliangrenwudan.setCode("*" + zhiliangrenwudan.getCode() + "*");
        zhiliangrenwudan.setIsdel(0);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        queryWrapper.select("count(*) as Flag");
        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 当前任务单查询(当月已审核总数)
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-任务单查询(当月已审核未审核总数)")
    @ApiOperation(value = "任务单（制梁）表信息-任务单查询(当月已审核未审核总数)", notes = "任务单（制梁）表信息-任务单查询(当月已审核未审核总数)")
    @GetMapping(value = "/list5")
    public Result<?> queryPageList5(Zhiliangrenwudan zhiliangrenwudan,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            zhiliangrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        zhiliangrenwudan.setCode("*" + zhiliangrenwudan.getCode() + "*");
        zhiliangrenwudan.setIsdel(0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
        String day1 = format.format(c.getTime());
        Date parse = null;//本月第一天
        Date parse1 = null;//本月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String day2 = format.format(ca.getTime());
        try {
            parse = format.parse(day1);
            parse1 = format.parse(day2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        queryWrapper.select("count(*) as Flag");
        queryWrapper.ge("DatTim", parse);
        queryWrapper.le("DatTim", parse1);
        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 当前任务单查询首页中间部分月统计
     *
     * @param zhiliangrenwudan
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "任务单查询首页中间部分月统计")
    @ApiOperation(value = "任务单查询首页中间部分月统计", notes = "任务单查询首页中间部分月统计")
    @GetMapping(value = "/list6")
    public Result<?> queryPageList6(Zhiliangrenwudan zhiliangrenwudan, HttpServletRequest req, String dattim_begin, String dattim_end, String sys_depart_orgcode) {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        zhiliangrenwudan.setIsdel(0);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            zhiliangrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        queryWrapper.select("count(*) as Flag", "DatTim");
        if (dattim_begin != null && dattim_end != null) {
            try {
                Date parse = ft.parse(dattim_begin);
                Date parse1 = ft.parse(dattim_end);
                queryWrapper.ge("DatTim", parse);
                queryWrapper.le("DatTim", parse1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        queryWrapper.last(" and DatTim like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(DatTim,'%Y-%m'))");
        List<Zhiliangrenwudan> bhzCementStatisticsList = zhiliangrenwudanService.list(queryWrapper);
        List list = new ArrayList();
        for (Zhiliangrenwudan statistics : bhzCementStatisticsList) {
            Map map = new HashMap();
            Date statisticsTime = statistics.getDattim();
            Integer allDish = Integer.valueOf(statistics.getFlag());
            String format3 = format.format(statisticsTime);
            map.put("Dattim", format3);
            map.put("Flag", allDish);
            list.add(map);
        }
        return Result.OK(list);
    }

    /**
     * 任务单APP各月制梁进度
     *
     * @param zhiliangrenwudan
     * @param req
     * @param dattim_begin
     * @param dattim_end
     * @return
     */
    @AutoLog(value = "任务单APP各月制梁进度")
    @ApiOperation(value = "任务单APP各月制梁进度", notes = "任务单APP各月制梁进度")
    @GetMapping(value = "/list7")
    public Result<?> queryPageList7(Zhiliangrenwudan zhiliangrenwudan, HttpServletRequest req, String dattim_begin, String dattim_end, String sys_depart_orgcode) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        QueryWrapper<ZhiliangGongxu> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("xuhao", 1);
        queryWrapper2.eq("status", 2);
        queryWrapper2.eq("isdel", 0);
        List<ZhiliangGongxu> zhiliangGongxuList = zhiliangGongxuService.list(queryWrapper2);
        List<String> uuidList = new ArrayList();
        if (zhiliangGongxuList.size() > 0) {
            for (ZhiliangGongxu zhiliangGongxu1 : zhiliangGongxuList) {
                uuidList.add(zhiliangGongxu1.getUuid());
            }
        }
        zhiliangrenwudan.setIsdel(0);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("MM");
        SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM");
        String format2 = format1.format(new Date());
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        queryWrapper.select("count(*) as station", "productiontime");
        queryWrapper.last("and productiontime like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(productiontime,'%Y-%m'))");
        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        Map map = new HashMap();
        List timelist = new ArrayList();//时间年月
        List listjh = new ArrayList();//计划生产
        List listsj = new ArrayList();//实际生产
        for (Zhiliangrenwudan zhiliangrenwudan1 : list) {
            Integer station = zhiliangrenwudan1.getStation(); //计划生产榀
            Date productiontime = zhiliangrenwudan1.getProductiontime();
            String format = ft.format(productiontime);
            String format3 = ft1.format(productiontime);
            timelist.add(format);
            listjh.add(station);
            QueryWrapper<Zhiliangrenwudan> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("count(*) as station", "productiontime");
            queryWrapper1.likeRight("productiontime", format3);
            queryWrapper1.eq("isdel", 0);
            queryWrapper1.in("uuid", uuidList);
            if (sys_depart_orgcode != null) {
                queryWrapper1.likeRight("sys_org_code", sys_depart_orgcode);
            }
            Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper1);
            Integer station1 = one.getStation();
            listsj.add(station1);
        }
        map.put("time", timelist);
        map.put("sjlist", listsj);
        map.put("jhlist", listjh);
        return Result.OK(map);
    }


    /**
     * 任务单APP当前月每天制梁进度
     *
     * @param zhiliangrenwudan
     * @param req
     * @param dattim_begin
     * @param dattim_end
     * @return
     */
    @AutoLog(value = "任务单APP当前月每天制梁进度")
    @ApiOperation(value = "任务单APP当前月每天制梁进度", notes = "任务单APP当前月每天制梁进度")
    @GetMapping(value = "/list8")
    public Result<?> queryPageList8(Zhiliangrenwudan zhiliangrenwudan, HttpServletRequest req, String dattim_begin, String dattim_end, String sys_depart_orgcode) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        QueryWrapper<ZhiliangGongxu> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("xuhao", 1);
        queryWrapper2.eq("status", 2);
        queryWrapper2.eq("isdel", 0);
        List<ZhiliangGongxu> zhiliangGongxuList = zhiliangGongxuService.list(queryWrapper2);
        List<String> uuidList = new ArrayList();
        if (zhiliangGongxuList.size() > 0) {
            for (ZhiliangGongxu zhiliangGongxu1 : zhiliangGongxuList) {
                uuidList.add(zhiliangGongxu1.getUuid());
            }
        }
        zhiliangrenwudan.setIsdel(0);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat ft1 = new SimpleDateFormat("dd");
        String format2 = format1.format(new Date());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = ft1.format(c.getTime());//当前月第一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = ft1.format(ca.getTime());//当前月最后一天
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        queryWrapper.select("count(*) as station", "productiontime");
        queryWrapper.ge("productiontime", first);
        queryWrapper.le("productiontime", last);
        queryWrapper.last("GROUP BY (SELECT DATE_FORMAT(productiontime,'%Y-%m-%d'))");
        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        Map map = new HashMap();
        List timelist = new ArrayList();//时间年月
        List listjh = new ArrayList();//计划生产
        List listsj = new ArrayList();//实际生产
        for (Zhiliangrenwudan zhiliangrenwudan1 : list) {
            Integer station = zhiliangrenwudan1.getStation(); //计划生产榀
            Date productiontime = zhiliangrenwudan1.getProductiontime();
            String format = ft1.format(productiontime);
            String format3 = ft.format(productiontime);
            timelist.add(format);
            listjh.add(station);
            QueryWrapper<Zhiliangrenwudan> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("count(*) as station", "productiontime");
            queryWrapper1.likeRight("productiontime", format3);
            queryWrapper1.eq("isdel", 0);
            queryWrapper1.in("uuid", uuidList);
            if (sys_depart_orgcode != null) {
                queryWrapper1.likeRight("sys_org_code", sys_depart_orgcode);
            }
            Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper1);
            Integer station1 = one.getStation();
            listsj.add(station1);
        }
        map.put("time", timelist);
        map.put("sjlist", listsj);
        map.put("jhlist", listjh);
        return Result.OK(map);
    }

    /**
     * 列表查询
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-列表查询")
    @ApiOperation(value = "任务单（制梁）表信息-列表查询", notes = "任务单（制梁）表信息-列表查询")
    @GetMapping(value = "/list9")
    public Result<?> queryPageList9(Zhiliangrenwudan zhiliangrenwudan,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        List<Zhiliangrenwudan> pageList = zhiliangrenwudanService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 列表查询-施工部位对应台座
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-列表查询-施工部位对应台座")
    @ApiOperation(value = "任务单（制梁）表信息-列表查询-施工部位对应台座", notes = "任务单（制梁）表信息-列表查询-施工部位对应台座")
    @GetMapping(value = "/list10")
    public Result<?> queryPageList10(Zhiliangrenwudan zhiliangrenwudan,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        queryWrapper.ne("cunliangstatus",3);
        List<Zhiliangrenwudan> pageList = zhiliangrenwudanService.list(queryWrapper);
        List list = new ArrayList();
        for (Zhiliangrenwudan zhiliangrenwudan1 : pageList) {
            list.add(zhiliangrenwudan1);
        }
        return Result.OK(list);
    }

    /**
     * 列表查询-施工部位对应台座
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-列表查询-施工部位对应台座")
    @ApiOperation(value = "任务单（制梁）表信息-列表查询-施工部位对应台座", notes = "任务单（制梁）表信息-列表查询-施工部位对应台座")
    @GetMapping(value = "/listcu")
    public Result<?> queryPageListcu(Zhiliangrenwudan zhiliangrenwudan,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setXuhao("7");
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        queryWrapper.gt("cunliangstatus",1);
        List<Zhiliangrenwudan> pageList = zhiliangrenwudanService.list(queryWrapper);

        return Result.OK(pageList.size());
    }

    /**
     * 列表查询-施工部位对应台座
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-列表查询-施工部位对应台座")
    @ApiOperation(value = "任务单（制梁）表信息-列表查询-施工部位对应台座", notes = "任务单（制梁）表信息-列表查询-施工部位对应台座")
    @GetMapping(value = "/listjxcu")
    public Result<?> queryPageListjxcu(Zhiliangrenwudan zhiliangrenwudan,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        zhiliangrenwudan.setXuhao("7");
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        List<Zhiliangrenwudan> pageList = zhiliangrenwudanService.list(queryWrapper);
        int i = 0;
        for (Zhiliangrenwudan p :pageList){
            ZhiliangGongxu selectuuid = zhiliangGongxuService.selectuuid(p.getUuid(), 7);
            if (selectuuid != null && selectuuid.getStatus() == 2){
                i++;
            }
        }

        return Result.OK(i);
    }

    /**
     * 金华制梁空闲和生产中的台座
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-列表查询-施工部位对应台座")
    @ApiOperation(value = "任务单（制梁）表信息-列表查询-施工部位对应台座", notes = "任务单（制梁）表信息-列表查询-施工部位对应台座")
    @GetMapping(value = "/listZlkx")
    public Result<?> queryPageListZlkx(Zhiliangrenwudan zhiliangrenwudan,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sysOrgCode,
                                       HttpServletRequest req) {
        if (sysOrgCode != null && sysOrgCode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sysOrgCode + "*");
        }
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        zhiliangrenwudan.setCunliangstatus(1);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        queryWrapper.ne("xuhao", "10");
        queryWrapper.ne("xuhao", "7");
        List<Zhiliangrenwudan> pageList = zhiliangrenwudanService.list(queryWrapper);

        QueryWrapper<ZhiliangTaizuoCfg> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.likeRight("sys_org_code", sysOrgCode);
        List<ZhiliangTaizuoCfg> list1 = zhiliangTaizuoCfgService.list(queryWrapper1);
        Map<String, Integer> map = new HashMap<>();
        if (list1.size() - pageList.size() < 0){
            map.put("taizuoKx", 0);
        }else {
            map.put("taizuoKx", list1.size() - pageList.size());
        }
        map.put("taizuoZy", pageList.size());
        List<Map> list = new ArrayList<>();
        list.add(map);
        return Result.OK(list);
    }

    /**
     * 金华制梁台座对应工序数量
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-列表查询-施工部位对应台座")
    @ApiOperation(value = "任务单（制梁）表信息-列表查询-施工部位对应台座", notes = "任务单（制梁）表信息-列表查询-施工部位对应台座")
    @GetMapping(value = "/listZlgx")
    public Result<?> queryPageListZlgx(Zhiliangrenwudan zhiliangrenwudan,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                       HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(11);
        list.add(2);
        list.add(4);
        list.add(5);
        list.add(10);
        list.add(7);
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        if (Objects.equals(zhiliangrenwudan.getXuhao(), "1")) {
            zhiliangrenwudan.setCunliangstatus(0);
            QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
            List<Zhiliangrenwudan> pageList = zhiliangrenwudanService.list(queryWrapper);
            return Result.OK(pageList.size());
        }
        zhiliangrenwudan.setCunliangstatus(1);
        zhiliangrenwudan.setXuhao((list.get(list.indexOf(Integer.parseInt(zhiliangrenwudan.getXuhao())) - 1)).toString());
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        List<Zhiliangrenwudan> pageList = zhiliangrenwudanService.list(queryWrapper);

        return Result.OK(pageList.size());
    }

    /**
     * 滨淮制梁台座对应工序数量
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-列表查询-施工部位对应台座")
    @ApiOperation(value = "任务单（制梁）表信息-列表查询-施工部位对应台座", notes = "任务单（制梁）表信息-列表查询-施工部位对应台座")
    @GetMapping(value = "/listBhgx")
    public Result<?> queryPageListBhgx(Zhiliangrenwudan zhiliangrenwudan,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                       HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        if (Objects.equals(zhiliangrenwudan.getXuhao(), "11")) {
            zhiliangrenwudan.setCunliangstatus(0);
            QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
            List<Zhiliangrenwudan> pageList = zhiliangrenwudanService.list(queryWrapper);
            return Result.OK(pageList.size());
        }
        zhiliangrenwudan.setCunliangstatus(1);
//        zhiliangrenwudan.setXuhao((list.get(list.indexOf(Integer.parseInt(zhiliangrenwudan.getXuhao()))-1)).toString());
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        List<Zhiliangrenwudan> pageList = zhiliangrenwudanService.list(queryWrapper);

        return Result.OK(pageList.size());
    }

    /**
     * 当前任务单查询(总数)
     * /**
     * 分页列表查询
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-任务单查询(总数)")
    @ApiOperation(value = "任务单（制梁）表信息-任务单查询(总数)", notes = "任务单（制梁）表信息-任务单查询(总数)")
    @GetMapping(value = "/list11")
    public Result<?> queryPageList11(Zhiliangrenwudan zhiliangrenwudan, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req, Integer status, Integer xuhao) {
//        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
//            zhiliangrenwudan.setSysOrgCode("*" + sys_depart_orgcode + "*");
//        }
        QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("status", status);
        queryWrapper1.eq("xuhao", xuhao);
        queryWrapper1.eq("isdel", 0);
        List<ZhiliangGongxu> zhiliangGongxuList = zhiliangGongxuService.list(queryWrapper1);
        List<String> uuidList = new ArrayList<>();
        if (zhiliangGongxuList.size() > 0) {
            for (ZhiliangGongxu zhiliangGongxu1 : zhiliangGongxuList) {
                uuidList.add(zhiliangGongxu1.getUuid());
            }
            QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("count(*) as Flag");
            queryWrapper.in("uuid", uuidList);
            queryWrapper.likeRight("sys_org_code", zhiliangrenwudan.getSysOrgCode());
            queryWrapper.eq("status", 1);
            queryWrapper.eq("isdel", 0);
            Zhiliangrenwudan zhiliangrenwudan1 = zhiliangrenwudanService.getOne(queryWrapper);
            return Result.OK(zhiliangrenwudan1);
        } else {
            Zhiliangrenwudan zhiliangrenwudan1 = new Zhiliangrenwudan();
            zhiliangrenwudan1.setFlag("0");
            return Result.OK(zhiliangrenwudan1);
        }
    }

    /**
     * 金华空闲台座数量
     * /**
     * 分页列表查询
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-任务单查询(总数)")
    @ApiOperation(value = "任务单（制梁）表信息-任务单查询(总数)", notes = "任务单（制梁）表信息-任务单查询(总数)")
    @GetMapping(value = "/listKx")
    public Result<?> queryPageListKx(Zhiliangrenwudan zhiliangrenwudan, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req, Integer status, Integer xuhao) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        // 台座数量
        CunliangProcedureConfig config = new CunliangProcedureConfig();
        if (config.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                config.setShebeino(shebei);
            } else {
                config.setShebeino("空");
            }
        }
        QueryWrapper<CunliangProcedureConfig> queryWrappers = QueryGenerator.initQueryWrapper(config, req.getParameterMap());
        queryWrappers.likeRight("sys_org_code", sys_depart_orgcode);
        List<CunliangProcedureConfig> list1 = cunliangProcedureConfigService.list(queryWrappers);

        QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("status", 1);
        queryWrapper1.eq("xuhao", 7);
        queryWrapper1.eq("isdel", 0);
        List<ZhiliangGongxu> zhiliangGongxuList = zhiliangGongxuService.list(queryWrapper1);
        List<String> uuidList = new ArrayList<>();
        if (zhiliangGongxuList.size() > 0) {
            for (ZhiliangGongxu zhiliangGongxu1 : zhiliangGongxuList) {
                uuidList.add(zhiliangGongxu1.getUuid());
            }
            // 梁数量
            QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("uuid", uuidList);
            queryWrapper.likeRight("sys_org_code", sys_depart_orgcode);
            queryWrapper.eq("status", 1);
            queryWrapper.eq("isdel", 0);
            List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);

            return Result.OK(list1.size() - list.size());
        } else {
            return Result.OK(list1.size());
        }
    }


    /**
     * 金华存梁台座，梁工序信息
     *
     * @param zhiliangrenwudan
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-任务单查询(总数)")
    @ApiOperation(value = "任务单（制梁）表信息-任务单查询(总数)", notes = "任务单（制梁）表信息-任务单查询(总数)")
    @GetMapping(value = "/listGx")
    public Result<?> queryPageListGx(Zhiliangrenwudan zhiliangrenwudan, String sys_depart_orgcode, HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode("*" + sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        Zhiliangrenwudan one = zhiliangrenwudanService.getOne(queryWrapper);
        if (one == null) {
            return Result.error("该任务单不存在，或已被删除");
        }
        List<ZhiliangGongxu> selectgongxu = zhiliangGongxuService.selectgongxu(one.getUuid());

        for (ZhiliangGongxu l : selectgongxu) {
            if (l.getXuhao() == 1) {
                l.setCluuid("钢筋绑扎");
            } else if (l.getXuhao() == 2) {
                l.setCluuid("混凝土浇筑");
            } else if (l.getXuhao() == 3) {
                l.setCluuid("收面静置");
            } else if (l.getXuhao() == 4) {
                l.setCluuid("养生");
            } else if (l.getXuhao() == 5) {
                l.setCluuid("张拉");
            } else if (l.getXuhao() == 6) {
                l.setCluuid("封端");
            } else if (l.getXuhao() == 7) {
                l.setCluuid("存梁/取梁");
            } else if (l.getXuhao() == 8) {
                l.setCluuid("提梁");
            } else if (l.getXuhao() == 9) {
                l.setCluuid("蒸养");
            } else if (l.getXuhao() == 10) {
                l.setCluuid("压浆");
            } else if (l.getXuhao() == 11) {
                l.setCluuid("模板安装");
            } else {
                l.setCluuid("蒸养二区");
            }
        }
        return Result.OK(selectgongxu);
    }

    /**
     * 当前任务单查询(总数)
     * /**
     * 分页列表查询
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-任务单查询(总数)")
    @ApiOperation(value = "任务单（制梁）表信息-任务单查询(总数)", notes = "任务单（制梁）表信息-任务单查询(总数)")
    @GetMapping(value = "/listSu")
    public Result<?> queryPageListSu(Zhiliangrenwudan zhiliangrenwudan, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req, Integer statuss, Integer xuhaos) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode("*" + sys_depart_orgcode + "*");
        }
        zhiliangrenwudan.setIsdel(0);
        zhiliangrenwudan.setStatus(1);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());

        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        int i = 0;
        for (Zhiliangrenwudan l : list) {
            QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("uuid", l.getUuid()).eq("xuhao", xuhaos).eq("status", statuss);
            ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper1);
            if (one != null) {
                i++;
            }
        }
        return Result.OK(i);
    }

    /**
     * 当前任务单查询(总数)
     * /**
     * 分页列表查询
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-任务单查询(总数)")
    @ApiOperation(value = "任务单（制梁）表信息-任务单查询(总数)", notes = "任务单（制梁）表信息-任务单查询(总数)")
    @GetMapping(value = "/listbridge")
    public Result<?> queryBridgePageList(Zhiliangrenwudan zhiliangrenwudan, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                         HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode("*" + sys_depart_orgcode + "*");
        }
        QueryWrapper<Zhiliangrenwudan> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("status", 1);
        queryWrapper1.eq("isdel", 0);
        List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.list();
        List<String> uuidList = new ArrayList<>();
        if (zhiliangrenwudanList.size() > 0) {
            for (Zhiliangrenwudan zhiliangrenwudan1 : zhiliangrenwudanList) {
                uuidList.add(zhiliangrenwudan1.getUuid());
            }
            QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("count(*) as Flag");
            queryWrapper.in("uuid", uuidList);
            queryWrapper.likeRight("sys_org_code", zhiliangrenwudan.getSysOrgCode());
            queryWrapper.eq("status", 1);
            queryWrapper.eq("isdel", 0);
            Zhiliangrenwudan zhiliangrenwudan1 = zhiliangrenwudanService.getOne(queryWrapper);
            return Result.OK(zhiliangrenwudan1);
        } else {
            Zhiliangrenwudan zhiliangrenwudan1 = new Zhiliangrenwudan();
            zhiliangrenwudan1.setFlag("0");
            return Result.OK(zhiliangrenwudan1);
        }
    }

    /**
     * 当前任务单查询(本月总数)
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-任务单查询(本月总数)")
    @ApiOperation(value = "任务单（制梁）表信息-任务单查询(本月总数)", notes = "任务单（制梁）表信息-任务单查询(本月总数)")
    @GetMapping(value = "/list12")
    public Result<?> queryPageList12(Zhiliangrenwudan zhiliangrenwudan,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req, Integer xuhao, Integer status) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode("*" + sys_depart_orgcode + "*");
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String format3 = format.format(new Date());
        QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(*) as Flag");
        queryWrapper.eq("status", 1);
        queryWrapper.eq("isdel", 0);
        queryWrapper.last(" and productiontime like '" + format3 + "%'");
        queryWrapper.likeRight("sys_org_code", zhiliangrenwudan.getSysOrgCode());
        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
        if (list.size() == 0) {
            Zhiliangrenwudan zhiliangrenwudan1 = new Zhiliangrenwudan();
            zhiliangrenwudan1.setFlag("0");
            return Result.OK(zhiliangrenwudan1);
        }
        return Result.OK(list);
    }

    /**
     * 分页列表查询
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-分页列表查询")
    @ApiOperation(value = "任务单（制梁）表信息-分页列表查询", notes = "任务单（制梁）表信息-分页列表查询")
    @GetMapping(value = "/applist")
//    @PermissionData(pageComponent = "zhiliang/zhiliangrenwudan/ZhiliangrenwudanList")
    public Result<?> queryPageappList(Zhiliangrenwudan zhiliangrenwudan,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                      HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            zhiliangrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        zhiliangrenwudan.setCode("*" + zhiliangrenwudan.getCode() + "*");
        zhiliangrenwudan.setIsdel(0);
        if (zhiliangrenwudan.getConspos() != null) {
            zhiliangrenwudan.setConspos("*" + zhiliangrenwudan.getConspos() + "*");
        }
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        Page<Zhiliangrenwudan> page = new Page<Zhiliangrenwudan>(pageNo, pageSize);
        IPage<Zhiliangrenwudan> pageList = zhiliangrenwudanService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *  制梁任务单添加与编辑详见 SysDepartprojectController
     */
//    /**
//     *   添加
//     *
//     * @param zhiliangrenwudan
//     * @return
//     */
//    @AutoLog(value = "任务单（制梁）表信息-添加")
//    @ApiOperation(value="任务单（制梁）表信息-添加", notes="任务单（制梁）表信息-添加")
//    @PostMapping(value = "/zlrenwudanadd")
//    public Result<?> add1(@RequestBody Zhiliangrenwudan zhiliangrenwudan) {
//        Date date=new Date();
//        zhiliangrenwudan.setDattim(date);
//        String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
//        DateFormat format=new SimpleDateFormat("yyyyMMdd-HHmmss");
//        if (zhiliangrenwudan.getCode() == null){
//            zhiliangrenwudan.setCode("ZLRWD-"+format.format(date));
//        }else {
//            zhiliangrenwudan.setCode("ZLRWD-"+zhiliangrenwudan.getCode());
//        }
//        zhiliangrenwudan.setUuid(uuid);
//        zhiliangrenwudanService.save(zhiliangrenwudan);
//        return Result.OK("添加成功！");
//    }

    /**
     * 添加
     *
     * @param zhiliangrenwudan
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-添加")
    @ApiOperation(value = "任务单（制梁）表信息-添加", notes = "任务单（制梁）表信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Zhiliangrenwudan zhiliangrenwudan) {
        String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
        zhiliangrenwudan.setUuid(uuid);
        zhiliangrenwudanService.save(zhiliangrenwudan);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param zhiliangrenwudan
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-编辑")
    @ApiOperation(value = "任务单（制梁）表信息-编辑", notes = "任务单（制梁）表信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Zhiliangrenwudan zhiliangrenwudan) {
        String qcode1 = zhiliangrenwudan.getQcode();//2-193-1e0377ce617a4e469fe03afea5ade8732-193
        if (StrUtil.isNotBlank(qcode1)) {
//            int index1 = qcode1.indexOf("-");
//            int index2 = qcode1.indexOf("-", index1 + 1);
//            String qcode = qcode1.substring(index2+1 ,qcode1.length()-index2);
//            Zhiliangqrcode zhiliangqrcode = zhiliangqrcodeService.selectOne(qcode);
//            QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("qcode", qcode1);
//            queryWrapper.eq("isdel", 0);
//            Zhiliangrenwudan zhiliangrenwudan1 = zhiliangrenwudanService.getOne(queryWrapper);
//            if (null == zhiliangqrcode) {
//                return Result.error("二维码库不存在该二维码！");
//            } else {
//                if (null != zhiliangrenwudan1) {
//                    return Result.error("该二维码已被使用！");
//                }
//                zhiliangrenwudan.setFengduanstatus(1);
//                zhiliangrenwudanService.updateById(zhiliangrenwudan);
//                return Result.OK("二维码验证成功！");
//            }
            QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("qcode",qcode1);
            queryWrapper.eq("isdel", 0);
            List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper);
            if (list.size() > 0){
                return Result.error("该二维码已被使用！");
            }else {
                zhiliangrenwudan.setFengduanstatus(1);
                zhiliangrenwudanService.updateById(zhiliangrenwudan);
                return Result.OK("二维码验证成功！");
            }
        }
        zhiliangrenwudanService.updateById(zhiliangrenwudan);
        return Result.OK("编辑成功!");
    }

    /**
     * 二维码验证
     *
     * @param zhiliangrenwudan
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-二维码验证")
    @ApiOperation(value = "任务单（制梁）表信息-二维码验证", notes = "任务单（制梁）表信息-二维码验证")
    @PutMapping(value = "/qredit")
    public Result<?> qredit(@RequestBody Zhiliangrenwudan zhiliangrenwudan) {
        String qcode1 = zhiliangrenwudan.getQcode();
        if (StrUtil.isNotBlank(qcode1)) {
            String s = ConverUtils.spiltRtoL(qcode1);
            String qcode = ConverUtils.spiltRtoL(s.substring(0, s.indexOf("-")));
            Zhiliangqrcode zhiliangqrcode = zhiliangqrcodeService.selectOne(qcode);
            QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("qcode", qcode1);
            queryWrapper.eq("isdel", 0);
            Zhiliangrenwudan zhiliangrenwudan1 = zhiliangrenwudanService.getOne(queryWrapper);
            if (null == zhiliangqrcode) {
                return Result.error("二维码库不存在该二维码！");
            } else {
                if (null != zhiliangrenwudan1) {
                    return Result.error("该二维码已被使用！");
                }
                return Result.OK("二维码可以使用！");
            }
        }
        return Result.error("二维码为空!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-通过id删除")
    @ApiOperation(value = "任务单（制梁）表信息-通过id删除", notes = "任务单（制梁）表信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) Integer id) {
        Zhiliangrenwudan zhiliangrenwudan = zhiliangrenwudanService.getById(id);
        QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("uuid", zhiliangrenwudan.getUuid());
        queryWrapper1.eq("xuhao", 7);
        queryWrapper1.eq("status", 1);
        ZhiliangGongxu zhiliangGongxu = zhiliangGongxuService.getOne(queryWrapper1);
        if (zhiliangGongxu != null) {
            return Result.error("该制梁任务单不可删除!");
//            QueryWrapper<CunliangProcedureConfig> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("shebeino", zhiliangGongxu.getShebeino());
//            queryWrapper.eq("lianghang", zhiliangGongxu.getCunlianghang());
//            queryWrapper.eq("lianglie", zhiliangGongxu.getCunlianglie());
//            queryWrapper.eq("dangqianceng", zhiliangGongxu.getLiangceng());
//            CunliangProcedureConfig cunliangProcedureConfig = cunliangProcedureConfigService.getOne(queryWrapper);
//            if (cunliangProcedureConfig != null) {
//                CunliangProcedureConfig cunliangProcedureConfig1 = new CunliangProcedureConfig();
//                cunliangProcedureConfig1.setId(cunliangProcedureConfig.getId());
//                if ("1".equals(cunliangProcedureConfig.getDangqianceng())) {
//                    cunliangProcedureConfig1.setStatus(0);
//                }
//                if ("2".equals(cunliangProcedureConfig.getDangqianceng())) {
//                    cunliangProcedureConfig1.setStatus1(0);
//                }
//                cunliangProcedureConfigService.updateById(cunliangProcedureConfig1);
//            }
        } else {
            zhiliangrenwudan.setIsdel(1);
            zhiliangrenwudanService.updateById(zhiliangrenwudan);
            zhiliangGongxuService.updateones(zhiliangrenwudan.getUuid());

            //修改台座状态
            ZhiliangTaizuoCfg selectzltaizuos = zhiliangTaizuoCfgService.selectzltaizuos(zhiliangrenwudan.getTaizuono());
            selectzltaizuos.setStatus(0);
            zhiliangTaizuoCfgService.updateById(selectzltaizuos);
            return Result.OK("删除成功!");
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-批量删除")
    @ApiOperation(value = "任务单（制梁）表信息-批量删除", notes = "任务单（制梁）表信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.zhiliangrenwudanService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-通过id查询")
    @ApiOperation(value = "任务单（制梁）表信息-通过id查询", notes = "任务单（制梁）表信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Zhiliangrenwudan zhiliangrenwudan = zhiliangrenwudanService.getById(id);
        if (zhiliangrenwudan == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(zhiliangrenwudan);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "砼拌合站理论配合比子表通过主表ID查询")
    @ApiOperation(value = "砼拌合站理论配合比子表主表ID查询", notes = "砼拌合站理论配合比子表-通主表ID查询")
    @GetMapping(value = "/queryZhiliangGongxuByMainId")
    public Result<?> queryBhzRecipedetailListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<ZhiliangGongxu> zhiliangGongxuList = zhiliangGongxuService.selectByMainId(id);
        return Result.OK(zhiliangGongxuList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param zhiliangrenwudan
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Zhiliangrenwudan zhiliangrenwudan) {
        return super.exportXls(request, zhiliangrenwudan, Zhiliangrenwudan.class, "任务单（制梁）表信息");
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
        return importExcelzlrwd(request, response, Zhiliangrenwudan.class);
    }
    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @param clazz
     * @return
     */
    protected Result<?> importExcelzlrwd(HttpServletRequest request, HttpServletResponse response, Class<Zhiliangrenwudan> clazz) {
        UUID uuid = UUID.randomUUID();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<Zhiliangrenwudan> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                //update-begin-author:taoyan date:20190528 for:批量插入数据
                long start = System.currentTimeMillis();
                System.out.println(list);
                for (Zhiliangrenwudan l :list){
                    if (l.getSysOrgCode() != null){
                        l.setUuid(String.valueOf(uuid));
                        l.setStatus(1);
                        setgongxu(String.valueOf(uuid),l.getSysOrgCode(),l);
                    }
                }
                zhiliangrenwudanService.saveBatch(list);
                //400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
                //1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                //update-end-author:taoyan date:20190528 for:批量插入数据
                return Result.ok("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    private void setgongxu(String uuid,String orgcode,Zhiliangrenwudan l){
        if (orgcode.equals("A05A01A08A12A07")){
            for (int i = 0; i < 7; i++) {
                if (i == 0) {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setUuid(uuid);
                    zhiliangGongxu.setXuhao(1);
                    zhiliangGongxu.setRemark("1");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(l.getCreateBy());
                    zhiliangGongxuService.save(zhiliangGongxu);
                } else if (i == 1) {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setUuid(uuid);
                    zhiliangGongxu.setXuhao(11);
                    zhiliangGongxu.setRemark("1");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(l.getCreateBy());
                    zhiliangGongxuService.save(zhiliangGongxu);
                } else if (i == 2) {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setUuid(uuid);
                    zhiliangGongxu.setXuhao(2);
                    zhiliangGongxu.setRemark("2");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(l.getCreateBy());
                    zhiliangGongxuService.save(zhiliangGongxu);
                } else if (i == 3) {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setUuid(uuid);
                    zhiliangGongxu.setXuhao(4);
                    zhiliangGongxu.setRemark("240");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(l.getCreateBy());
                    zhiliangGongxuService.save(zhiliangGongxu);
                } else if (i == 4) {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setUuid(uuid);
                    zhiliangGongxu.setXuhao(5);
                    zhiliangGongxu.setRemark("12");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(l.getCreateBy());
                    zhiliangGongxuService.save(zhiliangGongxu);
                } else if (i == 5) {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setUuid(uuid);
                    zhiliangGongxu.setXuhao(10);
                    zhiliangGongxu.setRemark("12");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(l.getCreateBy());
                    zhiliangGongxuService.save(zhiliangGongxu);
                } else {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setUuid(uuid);
                    zhiliangGongxu.setXuhao(7);
                    zhiliangGongxu.setRemark("1");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(l.getCreateBy());
                    zhiliangGongxuService.save(zhiliangGongxu);
                }
            }
        }
    }
}
