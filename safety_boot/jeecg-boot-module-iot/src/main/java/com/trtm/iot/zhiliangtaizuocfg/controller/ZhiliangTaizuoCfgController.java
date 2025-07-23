package com.trtm.iot.zhiliangtaizuocfg.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhilianggongxu.service.IZhiliangGongxuService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import com.trtm.iot.zhiliangrenwudan.vo.ZhiliangRWD;
import com.trtm.iot.zhiliangtaizuocfg.vo.ZhiLiangTjVO;
import com.trtm.iot.zhiliangtaizuocfg.vo.ZhiliangTaizuoRWD;
import com.trtm.iot.zhiliangtaizuocfg.vo.tjVO;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.zhiliangtaizuocfg.entity.ZhiliangTaizuoCfg;
import com.trtm.iot.zhiliangtaizuocfg.service.IZhiliangTaizuoCfgService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 制梁台座配置表信息
 * @Author: jeecg-boot
 * @Date: 2021-09-13
 * @Version: V1.0
 */
@Api(tags = "制梁台座配置表信息")
@RestController
@RequestMapping("/zhiliangtaizuocfg/zhiliangTaizuoCfg")
@Slf4j
public class ZhiliangTaizuoCfgController extends JeecgController<ZhiliangTaizuoCfg, IZhiliangTaizuoCfgService> {
    @Autowired
    private IZhiliangTaizuoCfgService zhiliangTaizuoCfgService;
    @Autowired
    private IZhiliangrenwudanService zhiliangrenwudanService;
    @Autowired
    private IZhiliangGongxuService zhiliangGongxuService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param zhiliangTaizuoCfg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-分页列表查询")
    @ApiOperation(value = "制梁台座配置表信息-分页列表查询", notes = "制梁台座配置表信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ZhiliangTaizuoCfg zhiliangTaizuoCfg,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (zhiliangTaizuoCfg.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                zhiliangTaizuoCfg.setShebeino(shebei);
            } else {
                zhiliangTaizuoCfg.setShebeino("空");
            }
        }
        zhiliangTaizuoCfg.setTaizuono("*" + zhiliangTaizuoCfg.getTaizuono() + "*");
        zhiliangTaizuoCfg.setTaizuoname("*" + zhiliangTaizuoCfg.getTaizuoname() + "*");
        QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangTaizuoCfg, req.getParameterMap());
        Page<ZhiliangTaizuoCfg> page = new Page<ZhiliangTaizuoCfg>(pageNo, pageSize);
        IPage<ZhiliangTaizuoCfg> pageList = zhiliangTaizuoCfgService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 列表查询
     *
     * @param zhiliangTaizuoCfg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-列表查询")
    @ApiOperation(value = "制梁台座配置表信息-列表查询", notes = "制梁台座配置表信息-列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(ZhiliangTaizuoCfg zhiliangTaizuoCfg,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (zhiliangTaizuoCfg.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                zhiliangTaizuoCfg.setShebeino(shebei);
            } else {
                zhiliangTaizuoCfg.setShebeino("空");
            }
        }
//        List<String> list1 = new ArrayList<>();
//        QueryWrapper<Zhiliangrenwudan> queryWrapper1 = new QueryWrapper<>();
//        queryWrapper1.eq("cunliangstatus",1);
//        queryWrapper1.eq("sys_org_code",loginUser.getOrgCode());
//        List<Zhiliangrenwudan> list = zhiliangrenwudanService.list(queryWrapper1);
//        for(Zhiliangrenwudan l :list){
//            list1.add(l.getTaizuono());
//        }

        QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangTaizuoCfg, req.getParameterMap());
//        queryWrapper.notIn("taizuono",list1);
        List<ZhiliangTaizuoCfg> pageList = zhiliangTaizuoCfgService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 台座对应制梁任务单数据查询
     *
     * @param zhiliangTaizuoCfg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @ApiOperation(value = "制梁台座配置表信息-台座对应制梁任务单数据查询", notes = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @GetMapping(value = "/list2")
    public Result<?> queryPageList2(ZhiliangTaizuoCfg zhiliangTaizuoCfg,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (zhiliangTaizuoCfg.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                zhiliangTaizuoCfg.setShebeino(shebei);
            } else {
                zhiliangTaizuoCfg.setShebeino("空");
            }
        }
        QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangTaizuoCfg, req.getParameterMap());
        List<ZhiliangTaizuoCfg> pageList = zhiliangTaizuoCfgService.list(queryWrapper);
        List list1 = new ArrayList();
        for (ZhiliangTaizuoCfg zhiliangTaizuoCfg1 : pageList) {
            ZhiliangTaizuoRWD zhiliangTaizuoRWD = new ZhiliangTaizuoRWD();
            String taizuono = zhiliangTaizuoCfg1.getTaizuono();
            List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.selectone(taizuono);
            zhiliangTaizuoRWD.setId(zhiliangTaizuoCfg1.getId());
            zhiliangTaizuoRWD.setTaizuoname(zhiliangTaizuoCfg1.getTaizuoname());
            zhiliangTaizuoRWD.setTaizuono(zhiliangTaizuoCfg1.getTaizuono());
            if (zhiliangrenwudanList.size() > 0) {
                for (Zhiliangrenwudan zhiliangrenwudan : zhiliangrenwudanList) {
                    QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("uuid", zhiliangrenwudan.getUuid());
                    queryWrapper1.eq("xuhao", 7);
                    queryWrapper1.eq("status", 0);
                    queryWrapper1.eq("isdel", 0);
                    ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper1);
                    if (one != null) {
                        QueryWrapper<ZhiliangGongxu> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.eq("uuid", one.getUuid());
                        queryWrapper2.eq("xuhao", 1);
                        queryWrapper2.eq("status", 2);
                        queryWrapper2.eq("isdel", 0);
                        ZhiliangGongxu one1 = zhiliangGongxuService.getOne(queryWrapper2);
                        if (one1 != null) {
                            QueryWrapper<ZhiliangGongxu> queryWrapper4 = new QueryWrapper<>();
                            queryWrapper4.eq("uuid", one1.getUuid());
                            queryWrapper4.eq("isdel", 0);
                            List<ZhiliangGongxu> one3 = zhiliangGongxuService.list(queryWrapper4);
                            zhiliangTaizuoRWD.setZhiliangrenwudan(zhiliangrenwudan);
                            zhiliangTaizuoRWD.setZhiliangGongxu(one3);
                        }
                    }
                }
            }
            list1.add(zhiliangTaizuoRWD);
        }
        return Result.OK(list1);
    }

    /**
     * 台座对应制梁任务单数据查询
     *
     * @param zhiliangTaizuoCfg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @ApiOperation(value = "制梁台座配置表信息-台座对应制梁任务单数据查询", notes = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @GetMapping(value = "/listxg2")
    public Result<?> queryPageListxg2(ZhiliangTaizuoCfg zhiliangTaizuoCfg,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (zhiliangTaizuoCfg.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                zhiliangTaizuoCfg.setShebeino(shebei);
            } else {
                zhiliangTaizuoCfg.setShebeino("空");
            }
        }
        if (zhiliangTaizuoCfg.getTaizuoname() != null){
            zhiliangTaizuoCfg.setTaizuoname("*"+zhiliangTaizuoCfg.getTaizuoname()+"*");
        }
        QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangTaizuoCfg, req.getParameterMap());
        List<ZhiliangTaizuoCfg> pageList = zhiliangTaizuoCfgService.list(queryWrapper);
        List list1 = new ArrayList();
        for (ZhiliangTaizuoCfg zhiliangTaizuoCfg1 : pageList) {
            ZhiliangTaizuoRWD zhiliangTaizuoRWD = new ZhiliangTaizuoRWD();
            String taizuono = zhiliangTaizuoCfg1.getTaizuono();
            List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.selectone(taizuono);
            zhiliangTaizuoRWD.setId(zhiliangTaizuoCfg1.getId());
            zhiliangTaizuoRWD.setTaizuoname(zhiliangTaizuoCfg1.getTaizuoname());
            zhiliangTaizuoRWD.setTaizuono(zhiliangTaizuoCfg1.getTaizuono());
            if (zhiliangrenwudanList.size() > 0) {
                for (Zhiliangrenwudan zhiliangrenwudan : zhiliangrenwudanList) {
                    QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("uuid", zhiliangrenwudan.getUuid());
                    queryWrapper1.eq("xuhao", 7);
                    queryWrapper1.eq("status", 0);
                    queryWrapper1.eq("isdel", 0);
                    ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper1);
                    if (one != null) {
                        QueryWrapper<ZhiliangGongxu> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.eq("uuid", one.getUuid());
                        queryWrapper2.eq("xuhao", 1);
                        queryWrapper2.eq("status", 2);
                        queryWrapper2.eq("isdel", 0);
                        ZhiliangGongxu one1 = zhiliangGongxuService.getOne(queryWrapper2);
                        if (one1 != null) {
                            QueryWrapper<ZhiliangGongxu> queryWrapper4 = new QueryWrapper<>();
                            queryWrapper4.eq("uuid", one1.getUuid());
                            queryWrapper4.eq("isdel", 0);
                            List<ZhiliangGongxu> one3 = zhiliangGongxuService.list(queryWrapper4);
                            zhiliangTaizuoRWD.setZhiliangrenwudan(zhiliangrenwudan);
                            zhiliangTaizuoRWD.setZhiliangGongxu(one3);
                        }
                    }
                }
            }
            list1.add(zhiliangTaizuoRWD);
        }
        return Result.OK(list1);
    }
    /**
     * 台座对应制梁任务单数据查询
     *
     * @param zhiliangTaizuoCfg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @ApiOperation(value = "制梁台座配置表信息-台座对应制梁任务单数据查询", notes = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList4(ZhiliangTaizuoCfg zhiliangTaizuoCfg,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (zhiliangTaizuoCfg.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                zhiliangTaizuoCfg.setShebeino(shebei);
            } else {
                zhiliangTaizuoCfg.setShebeino("空");
            }
        }
        if (zhiliangTaizuoCfg.getTaizuoname() != null) {
            zhiliangTaizuoCfg.setTaizuoname(zhiliangTaizuoCfg.getTaizuoname() + "*");
        }
        QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangTaizuoCfg, req.getParameterMap());
        List<ZhiliangTaizuoCfg> pageList = zhiliangTaizuoCfgService.list(queryWrapper);
        List list1 = new ArrayList();
        for (ZhiliangTaizuoCfg zhiliangTaizuoCfg1 : pageList) {
            ZhiliangTaizuoRWD zhiliangTaizuoRWD = new ZhiliangTaizuoRWD();
            String taizuono = zhiliangTaizuoCfg1.getTaizuono();
            List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.selectone1(taizuono);
            zhiliangTaizuoRWD.setId(zhiliangTaizuoCfg1.getId());
            zhiliangTaizuoRWD.setTaizuoname(zhiliangTaizuoCfg1.getTaizuoname());
            zhiliangTaizuoRWD.setTaizuono(zhiliangTaizuoCfg1.getTaizuono());
            zhiliangTaizuoRWD.setTestid(0);
            zhiliangTaizuoRWD.setLzplzt(zhiliangTaizuoCfg1.getLzplzt());
            if (zhiliangrenwudanList.size() > 0) {
                for (Zhiliangrenwudan zhiliangrenwudan : zhiliangrenwudanList) {
                    LambdaQueryWrapper<ZhiliangGongxu> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan.getUuid());
                    queryWrapper1.eq(ZhiliangGongxu::getXuhao, 7);
                    queryWrapper1.eq(ZhiliangGongxu::getStatus, 0);
                    queryWrapper1.eq(ZhiliangGongxu::getIsdel, 0);
                    ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper1);
                    if (one != null) {
                        LambdaQueryWrapper<ZhiliangGongxu> queryWrapper2 = new LambdaQueryWrapper<>();
                        queryWrapper2.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan.getUuid());
                        queryWrapper2.eq(ZhiliangGongxu::getXuhao, 1);
                        queryWrapper2.eq(ZhiliangGongxu::getStatus, 2);
                        queryWrapper2.eq(ZhiliangGongxu::getIsdel, 0);
                        ZhiliangGongxu one1 = zhiliangGongxuService.getOne(queryWrapper2);
                        if (one1 != null) {
                            LambdaQueryWrapper<ZhiliangGongxu> queryWrapper3 = new LambdaQueryWrapper<>();
                            queryWrapper3.eq(ZhiliangGongxu::getUuid, one1.getUuid());
                            queryWrapper3.eq(ZhiliangGongxu::getIsdel, 0);
                            List<ZhiliangGongxu> one3 = zhiliangGongxuService.list(queryWrapper3);
                            int i = 0;
                            for(ZhiliangGongxu l :one3){
                                if(l.getStatus() == 2){
                                    i++;
                                }
                            }
                            Integer i1 = (i* 100 )/ 6 ;
                            zhiliangTaizuoRWD.setTestid(i1);
                            zhiliangTaizuoRWD.setZhiliangrenwudan(zhiliangrenwudan);
                            zhiliangTaizuoRWD.setZhiliangGongxu(one3);
                        }
                    }
                }
            }
            list1.add(zhiliangTaizuoRWD);
        }
        return Result.OK(list1);
    }


    /**
     * 视频监控修改工序完成状态
     *
     * @param zhiliangTaizuoCfg
     * @param req
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @ApiOperation(value = "制梁台座配置表信息-台座对应制梁任务单数据查询", notes = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @PostMapping(value = "/listjk")
    public Result<?> queryPageListjk(@RequestBody ZhiliangTaizuoCfg zhiliangTaizuoCfg,
                                    HttpServletRequest req) {
        QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("aijk",zhiliangTaizuoCfg.getTaizuono());
        ZhiliangTaizuoCfg zhiliangTaizuoCfg1 = zhiliangTaizuoCfgService.getOne(queryWrapper);//利用台座名称和orgcode查询唯一台座

        String taizuono = zhiliangTaizuoCfg1.getTaizuono();
        List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.selectone1(taizuono);
        if (zhiliangrenwudanList.size() > 0) {
            Zhiliangrenwudan zhiliangrenwudan = zhiliangrenwudanList.get(zhiliangrenwudanList.size() - 1);
            LambdaQueryWrapper<ZhiliangGongxu> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan.getUuid());
            List<ZhiliangGongxu> list = zhiliangGongxuService.list(queryWrapper1);
            if (list.size() > 0) {
                for (ZhiliangGongxu l :list){
                    if (l.getXuhao().equals(zhiliangTaizuoCfg.getXuhao())){
                        l.setStatus(zhiliangTaizuoCfg.getStatus());
                        l.setResponsible(zhiliangTaizuoCfg.getTaizuoname() + "确认");
                        l.setFinishtime(zhiliangTaizuoCfg.getCreateTime());
                        if(StringUtils.isNotBlank(zhiliangTaizuoCfg.getBeamno())){
                            l.setPicurl(zhiliangTaizuoCfg.getBeamno());
                        }
                        zhiliangGongxuService.updateById(l);
                        return Result.OK("修改成功！！！");
                    }
                }
            }else {
                return Result.error("该片梁没有对应的工序");
            }
        }
        return Result.error("未找到台座对应的梁信息");
    }

    /**
     * 视频监控修改工序完成状态(任务单版)
     *
     * @param zhiliangTaizuoCfg
     * @param req
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @ApiOperation(value = "制梁台座配置表信息-台座对应制梁任务单数据查询", notes = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @PostMapping(value = "/listjkrwd")
    public Result<?> queryPageListjkrwd(@RequestBody ZhiliangTaizuoCfg zhiliangTaizuoCfg,
                                     HttpServletRequest req) {
        QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Code",zhiliangTaizuoCfg.getTaizuono());
        Zhiliangrenwudan zhiliangrenwudan = zhiliangrenwudanService.getOne(queryWrapper);//利用台座名称和orgcode查询唯一台座

        LambdaQueryWrapper<ZhiliangGongxu> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan.getUuid());
        List<ZhiliangGongxu> list = zhiliangGongxuService.list(queryWrapper1);
        if (list.size() > 0) {
            for (ZhiliangGongxu l :list){
                if (l.getXuhao().equals(zhiliangTaizuoCfg.getXuhao())){
                    l.setStatus(zhiliangTaizuoCfg.getStatus());
                    l.setResponsible(zhiliangTaizuoCfg.getTaizuoname() + "确认");
                    l.setFinishtime(zhiliangTaizuoCfg.getCreateTime());
                    zhiliangGongxuService.updateById(l);
                    return Result.OK("修改成功！！！");
                }
            }
        }
        return Result.error("该片梁没有对应的工序");
    }

    /**
     * 台座对应信息查询页面接口
     *
     * @param zhiliangTaizuoCfg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @ApiOperation(value = "制梁台座配置表信息-台座对应制梁任务单数据查询", notes = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @GetMapping(value = "/listym4")
    public Result<?> queryPageListym4(ZhiliangTaizuoCfg zhiliangTaizuoCfg,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (zhiliangTaizuoCfg.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                zhiliangTaizuoCfg.setShebeino(shebei);
            } else {
                zhiliangTaizuoCfg.setShebeino("空");
            }
        }
        QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangTaizuoCfg, req.getParameterMap());
        Page<ZhiliangTaizuoCfg> page = new Page<ZhiliangTaizuoCfg>(pageNo, pageSize);
        IPage<ZhiliangTaizuoCfg> pageList1 = zhiliangTaizuoCfgService.page(page, queryWrapper);
        List<ZhiliangTaizuoCfg> pageList = pageList1.getRecords();
        for (ZhiliangTaizuoCfg zhiliangTaizuoCfg1 : pageList) {
            zhiliangTaizuoCfg1.setCreateBy(null);
            zhiliangTaizuoCfg1.setBeamno(null);
            zhiliangTaizuoCfg1.setXuhao(0);
            String taizuono = zhiliangTaizuoCfg1.getTaizuono();
            List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.selectone1(taizuono);
            if (zhiliangrenwudanList.size() > 0) {
                for (Zhiliangrenwudan zhiliangrenwudan : zhiliangrenwudanList) {
                    LambdaQueryWrapper<ZhiliangGongxu> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan.getUuid());
                    queryWrapper1.eq(ZhiliangGongxu::getXuhao, 7);
                    queryWrapper1.eq(ZhiliangGongxu::getStatus, 0);
                    queryWrapper1.eq(ZhiliangGongxu::getIsdel, 0);
                    ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper1);
                    if (one != null) {
                        LambdaQueryWrapper<ZhiliangGongxu> queryWrapper2 = new LambdaQueryWrapper<>();
                        queryWrapper2.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan.getUuid());
                        queryWrapper2.eq(ZhiliangGongxu::getXuhao, 1);
                        queryWrapper2.eq(ZhiliangGongxu::getStatus, 2);
                        queryWrapper2.eq(ZhiliangGongxu::getIsdel, 0);
                        ZhiliangGongxu one1 = zhiliangGongxuService.getOne(queryWrapper2);
                        if (one1 != null) {
                            LambdaQueryWrapper<ZhiliangGongxu> queryWrapper3 = new LambdaQueryWrapper<>();
                            queryWrapper3.eq(ZhiliangGongxu::getUuid, one1.getUuid());
                            queryWrapper3.eq(ZhiliangGongxu::getIsdel, 0);
                            List<ZhiliangGongxu> one3 = zhiliangGongxuService.list(queryWrapper3);
                            for(ZhiliangGongxu l :one3){
                                if(l.getStatus() != 2){
                                    zhiliangTaizuoCfg1.setCreateBy(zhiliangrenwudan.getConspos());
                                    zhiliangTaizuoCfg1.setBeamno(zhiliangrenwudan.getCode());
                                    zhiliangTaizuoCfg1.setXuhao(l.getXuhao());
                                    continue;
                                }
                            }
                        }
                    }
                }
            }

        }
        return Result.OK(pageList1);
    }
    /**
     * 台座对应制梁任务单数据查询
     *
     * @param zhiliangTaizuoCfg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @ApiOperation(value = "制梁台座配置表信息-台座对应制梁任务单数据查询", notes = "制梁台座配置表信息-台座对应制梁任务单数据查询")
    @GetMapping(value = "/list5")
    public Result<?> queryPageList5(ZhiliangTaizuoCfg zhiliangTaizuoCfg,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (zhiliangTaizuoCfg.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                zhiliangTaizuoCfg.setShebeino(shebei);
            } else {
                zhiliangTaizuoCfg.setShebeino("空");
            }
        }
        if (zhiliangTaizuoCfg.getTaizuoname() != null) {
            zhiliangTaizuoCfg.setTaizuoname(zhiliangTaizuoCfg.getTaizuoname() + "*");
        }
        QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangTaizuoCfg, req.getParameterMap());
        Page<ZhiliangTaizuoCfg> page = new Page<ZhiliangTaizuoCfg>(pageNo, pageSize);
        IPage<ZhiliangTaizuoCfg> pageList = zhiliangTaizuoCfgService.page(page, queryWrapper);
//        List list1 = new ArrayList();
//        for (ZhiliangTaizuoCfg zhiliangTaizuoCfg1 : pageList) {
//            ZhiliangTaizuoRWD zhiliangTaizuoRWD = new ZhiliangTaizuoRWD();
//            String taizuono = zhiliangTaizuoCfg1.getTaizuono();
//            List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.selectone(taizuono);
//            zhiliangTaizuoRWD.setId(zhiliangTaizuoCfg1.getId());
//            zhiliangTaizuoRWD.setTaizuoname(zhiliangTaizuoCfg1.getTaizuoname());
//            zhiliangTaizuoRWD.setTaizuono(zhiliangTaizuoCfg1.getTaizuono());
//            if (zhiliangrenwudanList.size() > 0) {
//                for (Zhiliangrenwudan zhiliangrenwudan : zhiliangrenwudanList) {
//                    LambdaQueryWrapper<ZhiliangGongxu> queryWrapper1 = new LambdaQueryWrapper<>();
//                    queryWrapper1.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan.getUuid());
//                    queryWrapper1.eq(ZhiliangGongxu::getXuhao, 7);
//                    queryWrapper1.eq(ZhiliangGongxu::getStatus, 0);
//                    queryWrapper1.eq(ZhiliangGongxu::getIsdel, 0);
//                    ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper1);
//                    if (one != null) {
//                        LambdaQueryWrapper<ZhiliangGongxu> queryWrapper2 = new LambdaQueryWrapper<>();
//                        queryWrapper2.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan.getUuid());
//                        queryWrapper2.eq(ZhiliangGongxu::getXuhao, 1);
//                        queryWrapper2.eq(ZhiliangGongxu::getStatus, 2);
//                        queryWrapper2.eq(ZhiliangGongxu::getIsdel, 0);
//                        ZhiliangGongxu one1 = zhiliangGongxuService.getOne(queryWrapper2);
//                        if (one1 != null) {
//                            LambdaQueryWrapper<ZhiliangGongxu> queryWrapper3 = new LambdaQueryWrapper<>();
//                            queryWrapper3.eq(ZhiliangGongxu::getUuid, one1.getUuid());
//                            queryWrapper3.eq(ZhiliangGongxu::getIsdel, 0);
//                            List<ZhiliangGongxu> one3 = zhiliangGongxuService.list(queryWrapper3);
//                            zhiliangTaizuoRWD.setZhiliangrenwudan(zhiliangrenwudan);
//                            zhiliangTaizuoRWD.setZhiliangGongxu(one3);
//                        }
//                    }
//                }
//            }
//            list1.add(zhiliangTaizuoRWD);
//        }
        return Result.OK(pageList);
    }

    /**
     * 台座数量（生产中、空闲）
     *
     * @param zhiliangTaizuoCfg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-台座数量（生产中、空闲）")
    @ApiOperation(value = "制梁台座配置表信息-台座数量（生产中、空闲）", notes = "制梁台座配置表信息-台座数量（生产中、空闲）")
    @GetMapping(value = "/list3")
    public Result<?> queryPageList3(ZhiliangTaizuoCfg zhiliangTaizuoCfg,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (zhiliangTaizuoCfg.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                zhiliangTaizuoCfg.setShebeino(shebei);
            } else {
                zhiliangTaizuoCfg.setShebeino("空");
            }
        }
        if (StringUtils.isNotBlank(zhiliangTaizuoCfg.getSysOrgCode())){
            zhiliangTaizuoCfg.setSysOrgCode(zhiliangTaizuoCfg.getSysOrgCode()+"*");
        }

        QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangTaizuoCfg, req.getParameterMap());
        List<ZhiliangTaizuoCfg> pageList = zhiliangTaizuoCfgService.list(queryWrapper);
        List list = new ArrayList();
        int tznostatus = 0;
        String sta = "空闲";
        String sta1 = "生产中";
        for (ZhiliangTaizuoCfg zhiliangTaizuoCfg1 : pageList) {
            ZhiliangRWD zhiliangRWD = new ZhiliangRWD();
            zhiliangRWD.setStationname(zhiliangTaizuoCfg1.getTaizuoname());
            String taizuono = zhiliangTaizuoCfg1.getTaizuono();
            zhiliangRWD.setStationstatus(sta);
            List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.selectone(taizuono);
            if (zhiliangrenwudanList.size() > 0) {
                for (Zhiliangrenwudan zhiliangrenwudan : zhiliangrenwudanList) {
                    QueryWrapper<ZhiliangGongxu> queryWrapper3 = new QueryWrapper<>();
                    queryWrapper3.eq("uuid", zhiliangrenwudan.getUuid());
                    List<Integer> list1 = new ArrayList<>();
                    List<ZhiliangGongxu> list2 = zhiliangGongxuService.list(queryWrapper3);
                    if(list2.size() == 0){
                        continue;
                    }
                    for (ZhiliangGongxu l :list2){
                        list1.add(l.getXuhao());
                    }
                    LambdaQueryWrapper<ZhiliangGongxu> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan.getUuid());
                    queryWrapper1.eq(ZhiliangGongxu::getXuhao, 7);
                    queryWrapper1.eq(ZhiliangGongxu::getStatus, 0);
                    queryWrapper1.eq(ZhiliangGongxu::getIsdel, 0);
                    ZhiliangGongxu one = zhiliangGongxuService.getOne(queryWrapper1);
                    if (one != null) {
                        LambdaQueryWrapper<ZhiliangGongxu> queryWrapper2 = new LambdaQueryWrapper<>();
                        queryWrapper2.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan.getUuid());
                        queryWrapper2.eq(ZhiliangGongxu::getXuhao, 1);
                        queryWrapper2.eq(ZhiliangGongxu::getStatus, 2);
                        queryWrapper2.eq(ZhiliangGongxu::getIsdel, 0);
                        ZhiliangGongxu one1 = zhiliangGongxuService.getOne(queryWrapper2);
                        String s = null;
                        if(!Objects.equals(zhiliangrenwudan.getXuhao(), "1")){
                            if(zhiliangrenwudan.getXuhao() == null || "null".equals(zhiliangrenwudan.getXuhao())){
                                continue;
                            }
                            int i = list1.indexOf(Integer.parseInt(zhiliangrenwudan.getXuhao()));
                            s = (list1.get(i + 1)).toString();
                        }
                        if (one1 != null) {
                            if(Objects.equals(zhiliangrenwudan.getXuhao(), "1")){
                                zhiliangRWD.setXuhao("钢筋绑扎");
                            } else if (Objects.equals(s, "2")){
                                zhiliangRWD.setXuhao("混凝土浇筑");
                            } else if (Objects.equals(s, "3")){
                                zhiliangRWD.setXuhao("收面静置");
                            } else if (Objects.equals(s, "4")){
                                zhiliangRWD.setXuhao("养生");
                            } else if (Objects.equals(s, "5")){
                                zhiliangRWD.setXuhao("张拉");
                            } else if (Objects.equals(s, "6")){
                                zhiliangRWD.setXuhao("封端");
                            } else if (Objects.equals(s, "7")){
                                zhiliangRWD.setXuhao("存梁/取梁");
                            } else if (Objects.equals(s, "8")){
                                zhiliangRWD.setXuhao("提梁");
                            } else if (Objects.equals(s, "9")){
                                zhiliangRWD.setXuhao("蒸养");
                            } else if (Objects.equals(s, "10")){
                                zhiliangRWD.setXuhao("压浆");
                            } else if (Objects.equals(s, "11")){
                                zhiliangRWD.setXuhao("模板安装");
                            } else {
                                zhiliangRWD.setXuhao("蒸养二区");
                            }
                            zhiliangRWD.setStationstatus(sta1);
                        }
                    }
                }
            }
            list.add(zhiliangRWD);
        }
        return Result.OK(list);
    }

    /**
     * 台座数量（生产中、空闲）
     *
     * @param zhiliangTaizuoCfg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-台座数量（生产中、空闲）")
    @ApiOperation(value = "制梁台座配置表信息-台座数量（生产中、空闲）", notes = "制梁台座配置表信息-台座数量（生产中、空闲）")
    @GetMapping(value = "/listxj")
    public Result<?> queryPageListxj(ZhiliangTaizuoCfg zhiliangTaizuoCfg,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (zhiliangTaizuoCfg.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                zhiliangTaizuoCfg.setShebeino(shebei);
            } else {
                zhiliangTaizuoCfg.setShebeino("空");
            }
        }
        if (StringUtils.isNotBlank(zhiliangTaizuoCfg.getSysOrgCode())){
            zhiliangTaizuoCfg.setSysOrgCode(zhiliangTaizuoCfg.getSysOrgCode()+"*");
        }
        QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangTaizuoCfg, req.getParameterMap());
        queryWrapper.likeRight("taizuoname","台座A").or().likeRight("taizuoname","台座B");
        List<ZhiliangTaizuoCfg> pageList = zhiliangTaizuoCfgService.list(queryWrapper);
        List list = new ArrayList();
        int tznostatus = 0;
        String sta = "空闲";
        String sta1 = "生产中";
        for (ZhiliangTaizuoCfg zhiliangTaizuoCfg1 : pageList) {
            ZhiliangTaizuoCfg zhiliangTaizuoCfg2 = new ZhiliangTaizuoCfg();
            zhiliangTaizuoCfg2.setId(zhiliangTaizuoCfg1.getId());
            zhiliangTaizuoCfg2.setTaizuoname(zhiliangTaizuoCfg1.getTaizuoname());
            zhiliangTaizuoCfg2.setTaizuono(zhiliangTaizuoCfg1.getTaizuono());
            String taizuono = zhiliangTaizuoCfg1.getTaizuono();
            if (zhiliangTaizuoCfg1.getTaizuono().equals("A-02") || zhiliangTaizuoCfg1.getTaizuono().equals("A-08")
                    || zhiliangTaizuoCfg1.getTaizuono().equals("A-17")|| zhiliangTaizuoCfg1.getTaizuono().equals("A-18")
                    || zhiliangTaizuoCfg1.getTaizuono().equals("A-19")|| zhiliangTaizuoCfg1.getTaizuono().equals("B-16")
                    || zhiliangTaizuoCfg1.getTaizuono().equals("B-24")|| zhiliangTaizuoCfg1.getTaizuono().equals("B-26")
                    || zhiliangTaizuoCfg1.getTaizuono().equals("B-28")|| zhiliangTaizuoCfg1.getTaizuono().equals("B-01")
                    || zhiliangTaizuoCfg1.getTaizuono().equals("B-02")|| zhiliangTaizuoCfg1.getTaizuono().equals("B-03")
                    || zhiliangTaizuoCfg1.getTaizuono().equals("B-04")|| zhiliangTaizuoCfg1.getTaizuono().equals("B-05")
                    || zhiliangTaizuoCfg1.getTaizuono().equals("B-06")|| zhiliangTaizuoCfg1.getTaizuono().equals("B-07")
                    || zhiliangTaizuoCfg1.getTaizuono().equals("B-08")|| zhiliangTaizuoCfg1.getTaizuono().equals("B-09")
                    || zhiliangTaizuoCfg1.getTaizuono().equals("B-10")){
                zhiliangTaizuoCfg2.setCreateBy(sta);
                list.add(zhiliangTaizuoCfg2);
            }else {
                zhiliangTaizuoCfg2.setCreateBy(sta);
                List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.selectone(taizuono);
                if (zhiliangrenwudanList.size() > 0) {
                    for (Zhiliangrenwudan zhiliangrenwudan : zhiliangrenwudanList) {
                        LambdaQueryWrapper<ZhiliangGongxu> queryWrapper1 = new LambdaQueryWrapper<>();
                        queryWrapper1.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan.getUuid());
                        queryWrapper1.eq(ZhiliangGongxu::getIsdel, 0);
                        List<ZhiliangGongxu> one = zhiliangGongxuService.list(queryWrapper1);
                        if (one.size() > 0) {
                            for (ZhiliangGongxu zhiliangGongxu : one) {
                                if (zhiliangGongxu.getXuhao() == 1 && zhiliangGongxu.getStatus() == 0) {
                                    zhiliangTaizuoCfg2.setCreateBy(sta);
                                } else if (zhiliangGongxu.getXuhao() == 1 && zhiliangGongxu.getStatus() == 2) {
                                    tznostatus = 1;
                                }
                                if (zhiliangGongxu.getXuhao() == 7 && zhiliangGongxu.getStatus() == 0) {
                                    if (tznostatus == 0) {
                                        zhiliangTaizuoCfg2.setCreateBy(sta);
                                    } else {
                                        zhiliangTaizuoCfg2.setCreateBy(sta1);
                                    }
                                } else if (zhiliangGongxu.getXuhao() == 7 && zhiliangGongxu.getStatus() == 1) {
                                    zhiliangTaizuoCfg2.setCreateBy(sta);
                                } else if (zhiliangGongxu.getXuhao() == 7 && zhiliangGongxu.getStatus() == 2) {
                                    zhiliangTaizuoCfg2.setCreateBy(sta);
                                }
                            }
                        }
                    }
                }
                list.add(zhiliangTaizuoCfg2);
            }
        }
        return Result.OK(list);
    }

    /**
     * 制梁统计
     *
     * @param zhiliangTaizuoCfg
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-台座数量（生产中、空闲）")
    @ApiOperation(value = "制梁台座配置表信息-台座数量（生产中、空闲）", notes = "制梁台座配置表信息-台座数量（生产中、空闲）")
    @GetMapping(value = "/querytj")
    public Result<?> querytj(ZhiliangTaizuoCfg zhiliangTaizuoCfg,
                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                             HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (zhiliangTaizuoCfg.getShebeino() == null) {
            if (!"null".equals(shebei)) {
                zhiliangTaizuoCfg.setShebeino(shebei);
            } else {
                zhiliangTaizuoCfg.setShebeino("空");
            }
        }
        QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangTaizuoCfg, req.getParameterMap());
        List<ZhiliangTaizuoCfg> pageList = zhiliangTaizuoCfgService.list(queryWrapper);
        int chuchangTJ = 0;
        ZhiLiangTjVO zhiLiangTjVO = new ZhiLiangTjVO();
        zhiLiangTjVO.setSum(pageList.size());
        int zhiliang = 0;
        for (ZhiliangTaizuoCfg zhiliangTaizuoCfg1 : pageList) {
            String taizuono = zhiliangTaizuoCfg1.getTaizuono();
            LambdaQueryWrapper<Zhiliangrenwudan> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(Zhiliangrenwudan::getTaizuono, taizuono);
            queryWrapper2.eq(Zhiliangrenwudan::getIsdel, 0);
            queryWrapper2.eq(Zhiliangrenwudan::getStatus, 1);
            List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.list(queryWrapper2);

            if (zhiliangrenwudanList.size() > 0) {
                for (Zhiliangrenwudan zhiliangrenwudan : zhiliangrenwudanList) {
                    LambdaQueryWrapper<ZhiliangGongxu> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan.getUuid());
                    queryWrapper1.eq(ZhiliangGongxu::getIsdel, 0);
                    List<ZhiliangGongxu> one = zhiliangGongxuService.list(queryWrapper1);
                    boolean boo = false;
                    boolean boo1 = false;
                    if (one.size() > 0) {
                        for (ZhiliangGongxu zhiliangGongxu : one) {
                            if (zhiliangGongxu.getXuhao() == 1) {
                                if (zhiliangGongxu.getStatus() == 2) {
                                    boo = true;
                                }
                            }
                            if (zhiliangGongxu.getXuhao() == 7) {
                                if (zhiliangGongxu.getStatus() == 0) {
                                    boo1 = true;
                                }
                                if (zhiliangGongxu.getStatus() == 2) {
                                    chuchangTJ += 1;
                                    boo = false;
                                    boo1 = false;
                                }
                            }
                            if (boo && boo1) {
                                zhiliang += 1;
                                boo = false;
                                boo1 = false;
                            }
                        }
                    }
                }
            }
        }
        zhiLiangTjVO.setZhiliang(zhiliang);
        zhiLiangTjVO.setChuchangTJ(chuchangTJ);
        return Result.OK(zhiLiangTjVO);
    }

    /**
     * 重点工序制梁统计
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "重点工序制梁统计")
    @ApiOperation(value = "重点工序制梁统计", notes = "重点工序制梁统计")
    @GetMapping(value = "/querytj1")
    public Result<?> querytj1(Zhiliangrenwudan zhiliangrenwudan,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                              HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            zhiliangrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        List<Zhiliangrenwudan> pageList = zhiliangrenwudanService.list(queryWrapper);
        ZhiLiangTjVO vo = new ZhiLiangTjVO();
        List<tjVO> list = new ArrayList<>();
        tjVO tj = new tjVO();
        tjVO tj1 = new tjVO();
        tjVO tj2 = new tjVO();
        tjVO tj3 = new tjVO();
        tjVO tj4 = new tjVO();
        tjVO tj5 = new tjVO();
        tjVO tj6 = new tjVO();
        for (Zhiliangrenwudan zhiliangrenwudan1 : pageList) {
            LambdaQueryWrapper<ZhiliangGongxu> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan1.getUuid());
            queryWrapper1.eq(ZhiliangGongxu::getIsdel, 0);
            List<ZhiliangGongxu> one = zhiliangGongxuService.list(queryWrapper1);
            if (one.size() > 0) {
                for (ZhiliangGongxu zhiliangGongxu : one) {
                    if (zhiliangGongxu.getXuhao() == 1 && zhiliangGongxu.getStatus() == 0) {
                        // 钢筋
                        vo.setGangjingTJ(vo.getGangjingTJ() + 1);
                    } else if (zhiliangGongxu.getXuhao() == 2 && zhiliangGongxu.getStatus() == 0) {
                        // 混凝土
                        vo.setHunningtuTJ(vo.getHunningtuTJ() + 1);
                    } else if (zhiliangGongxu.getXuhao() == 4 && zhiliangGongxu.getStatus() == 0) {
                        // 养生
                        vo.setYangshengTJ(vo.getYangshengTJ() + 1);
                    } else if (zhiliangGongxu.getXuhao() == 5 && zhiliangGongxu.getStatus() == 0) {
                        // 张拉
                        vo.setZhanglaTJ(vo.getZhanglaTJ() + 1);
                    } else if (zhiliangGongxu.getXuhao() == 10 && zhiliangGongxu.getStatus() == 0) {
                        // 压浆
                        vo.setYajiangTJ(vo.getYajiangTJ() + 1);
                    } else if (zhiliangGongxu.getXuhao() == 6 && zhiliangGongxu.getStatus() == 0) {
                        // 封端
                        vo.setFengduanTJ(vo.getFengduanTJ() + 1);
                    } else if (zhiliangGongxu.getXuhao() == 7 && zhiliangGongxu.getStatus() == 1) {
                        // 验收
                        vo.setYanshouTJ(vo.getYanshouTJ() + 1);
                    }
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        tj.setMingchen("钢筋捆扎");
        tj.setSum(vo.getGangjingTJ());
        tj1.setMingchen("混凝土浇筑");
        tj1.setSum(vo.getHunningtuTJ());
        tj2.setMingchen("养生中");
        tj2.setSum(vo.getYangshengTJ());
        tj3.setMingchen("张拉");
        tj3.setSum(vo.getZhanglaTJ());
        tj4.setMingchen("压浆");
        tj4.setSum(vo.getYajiangTJ());
        tj5.setMingchen("封端");
        tj5.setSum(vo.getFengduanTJ());
        tj6.setMingchen("验收移梁");
        tj6.setSum(vo.getYanshouTJ());
        list.add(tj);
        list.add(tj1);
        list.add(tj2);
        list.add(tj3);
        list.add(tj4);
        list.add(tj5);
        list.add(tj6);
        map.put("gongxu", list);
        return Result.OK(map);
    }
    /**
     * 重点工序制梁统计
     *
     * @param zhiliangrenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "重点工序制梁统计")
    @ApiOperation(value = "重点工序制梁统计", notes = "重点工序制梁统计")
    @GetMapping(value = "/querytj3")
    public Result<?> querytj3(Zhiliangrenwudan zhiliangrenwudan,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                              HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            zhiliangrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        List<Zhiliangrenwudan> pageList = zhiliangrenwudanService.list(queryWrapper);
        ZhiLiangTjVO vo = new ZhiLiangTjVO();
        Map<String, Integer> map = new LinkedHashMap<>();
        List<Map<String,Integer>> list = new ArrayList<>();
        for (Zhiliangrenwudan zhiliangrenwudan1 : pageList) {
            LambdaQueryWrapper<ZhiliangGongxu> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(ZhiliangGongxu::getUuid, zhiliangrenwudan1.getUuid());
            queryWrapper1.eq(ZhiliangGongxu::getIsdel, 0);
            List<ZhiliangGongxu> one = zhiliangGongxuService.list(queryWrapper1);
            if (one.size() > 0) {
                for (ZhiliangGongxu zhiliangGongxu : one) {
                    if (zhiliangGongxu.getXuhao() == 1 && zhiliangGongxu.getStatus() == 0) {
                        // 钢筋
                        vo.setGangjingTJ(vo.getGangjingTJ() + 1);
                    } else if (zhiliangGongxu.getXuhao() == 2 && zhiliangGongxu.getStatus() == 0) {
                        // 混凝土
                        vo.setHunningtuTJ(vo.getHunningtuTJ() + 1);
                    } else if (zhiliangGongxu.getXuhao() == 4 && zhiliangGongxu.getStatus() == 0) {
                        // 养生
                        vo.setYangshengTJ(vo.getYangshengTJ() + 1);
                    } else if (zhiliangGongxu.getXuhao() == 5 && zhiliangGongxu.getStatus() == 0) {
                        // 张拉
                        vo.setZhanglaTJ(vo.getZhanglaTJ() + 1);
                    } else if (zhiliangGongxu.getXuhao() == 10 && zhiliangGongxu.getStatus() == 0) {
                        // 压浆
                        vo.setYajiangTJ(vo.getYajiangTJ() + 1);
                    } else if (zhiliangGongxu.getXuhao() == 6 && zhiliangGongxu.getStatus() == 0) {
                        // 封端
                        vo.setFengduanTJ(vo.getFengduanTJ() + 1);
                    } else if (zhiliangGongxu.getXuhao() == 7 && zhiliangGongxu.getStatus() == 1) {
                        // 验收
                        vo.setYanshouTJ(vo.getYanshouTJ() + 1);
                    }
                }
            }
        }

        map.put("钢筋捆扎",vo.getGangjingTJ());
        map.put("混凝土浇筑",vo.getHunningtuTJ());
        map.put("养生中",vo.getYangshengTJ());
        map.put("张拉",vo.getZhanglaTJ());
        map.put("压浆",vo.getYajiangTJ());
        map.put("封端",vo.getFengduanTJ());
        map.put("验收移梁",vo.getYanshouTJ());

        list.add(map);
        return Result.OK(list);
    }
    /**
     * 混凝土强度
     *
     * @param zhiliangrenwudan
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土强度统计")
    @ApiOperation(value = "混凝土强度统计", notes = "混凝土强度统计")
    @GetMapping(value = "/querytj2")
    public Result<?> querytj2(Zhiliangrenwudan zhiliangrenwudan, HttpServletRequest req, String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            zhiliangrenwudan.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            zhiliangrenwudan.setSysOrgCode(loginUser.getOrgCode() + "*");
        }
        QueryWrapper<Zhiliangrenwudan> queryWrapper = QueryGenerator.initQueryWrapper(zhiliangrenwudan, req.getParameterMap());
        queryWrapper.select("count(BetLev) betlevsum,BetLev as betLev");
        queryWrapper.isNotNull("BetLev");
        queryWrapper.ne("BetLev","");
        queryWrapper.groupBy("BetLev");
        List<Map<String, Object>> pageList = zhiliangrenwudanService.listMaps(queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 添加
     *
     * @param zhiliangTaizuoCfg
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-添加")
    @ApiOperation(value = "制梁台座配置表信息-添加", notes = "制梁台座配置表信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ZhiliangTaizuoCfg zhiliangTaizuoCfg) {
        zhiliangTaizuoCfgService.save(zhiliangTaizuoCfg);
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param zhiliangTaizuoCfg
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-添加")
    @ApiOperation(value = "制梁台座配置表信息-添加", notes = "制梁台座配置表信息-添加")
    @PostMapping(value = "/add1")
    public Result<?> add1(@RequestBody ZhiliangTaizuoCfg zhiliangTaizuoCfg) {

            ZhiliangTaizuoCfg zhiliangTaizuoCfg1 = zhiliangTaizuoCfgService.selectzltaizuone();
            Integer id = 0;
            if (zhiliangTaizuoCfg1 == null) {
                id = id + 1;
            } else {
                id = zhiliangTaizuoCfg1.getId() + 1;
            }
            zhiliangTaizuoCfg.setTestid(id);
            if (StringUtils.isBlank(zhiliangTaizuoCfg.getTaizuono())) {
                String format = String.format("%03d", id);
                String taizuono = "ZLTZ-" + format;
                zhiliangTaizuoCfg.setTaizuono(taizuono);
            }
        zhiliangTaizuoCfgService.save(zhiliangTaizuoCfg);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param zhiliangTaizuoCfg
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-编辑")
    @ApiOperation(value = "制梁台座配置表信息-编辑", notes = "制梁台座配置表信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ZhiliangTaizuoCfg zhiliangTaizuoCfg) {
        zhiliangTaizuoCfgService.updateById(zhiliangTaizuoCfg);
        return Result.OK("编辑成功!");
    }

    /**
     * 编辑
     *
     * @param zhiliangTaizuoCfg
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-编辑")
    @ApiOperation(value = "制梁台座配置表信息-编辑", notes = "制梁台座配置表信息-编辑")
    @PutMapping(value = "/edit1")
    public Result<?> edit1(@RequestBody ZhiliangTaizuoCfg zhiliangTaizuoCfg) {
        QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("taizuono", zhiliangTaizuoCfg.getTaizuono());
        List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.list(queryWrapper);
        if (zhiliangrenwudanList.size() > 0) {
            return Result.error("该台座不可修改!");
        } else {
            zhiliangTaizuoCfgService.updateById(zhiliangTaizuoCfg);
            return Result.OK("编辑成功!");
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-通过id删除")
    @ApiOperation(value = "制梁台座配置表信息-通过id删除", notes = "制梁台座配置表信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) Integer id) {
        ZhiliangTaizuoCfg zhiliangTaizuoCfg = zhiliangTaizuoCfgService.getById(id);
        QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("taizuono", zhiliangTaizuoCfg.getTaizuono());
        List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.list(queryWrapper);
        if (zhiliangrenwudanList.size() > 0) {
            return Result.error("该台座不可删除!");
        } else {
            zhiliangTaizuoCfgService.removeById(id);
            return Result.OK("删除成功!");
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-批量删除")
    @ApiOperation(value = "制梁台座配置表信息-批量删除", notes = "制梁台座配置表信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.zhiliangTaizuoCfgService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "制梁台座配置表信息-通过id查询")
    @ApiOperation(value = "制梁台座配置表信息-通过id查询", notes = "制梁台座配置表信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ZhiliangTaizuoCfg zhiliangTaizuoCfg = zhiliangTaizuoCfgService.getById(id);
        if (zhiliangTaizuoCfg == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(zhiliangTaizuoCfg);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param zhiliangTaizuoCfg
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZhiliangTaizuoCfg zhiliangTaizuoCfg) {
        return super.exportXls(request, zhiliangTaizuoCfg, ZhiliangTaizuoCfg.class, "制梁台座配置表信息");
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
        return super.importExcel(request, response, ZhiliangTaizuoCfg.class);
    }

}
