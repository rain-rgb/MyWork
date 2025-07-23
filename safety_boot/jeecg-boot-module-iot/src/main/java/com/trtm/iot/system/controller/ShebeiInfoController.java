package com.trtm.iot.system.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.clgl.entity.ClxxRealdata;
import com.trtm.iot.clgl.service.IClxxRealdataService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;

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
 * @Description: 设备审核表
 * @Author: jeecg-boot
 * @Date: 2021-03-15
 * @Version: V1.0
 */
@Api(tags = "设备审核表")
@RestController
@RequestMapping("/system/shebeiInfo")
@Slf4j
public class ShebeiInfoController extends JeecgController<ShebeiInfo, IShebeiInfoService> {
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IClxxRealdataService clxxRealdataService;

    /**
     * 分页列表查询
     *
     * @param shebeiInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "设备审核表-分页列表查询")
    @ApiOperation(value = "设备审核表-分页列表查询", notes = "设备审核表-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "system/ShebeiInfoList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageList(ShebeiInfo shebeiInfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            shebeiInfo.setSysOrgCode(sys_depart_orgcode + "*");
        }
//		else{
//			LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息 此处不需要了因为上面配置了@PermissionData这个权限
//			shebeiInfo.setSysOrgCode("*"+loginUser.getOrgCode()+"*");
//		}
        shebeiInfo.setSbjno("*" + shebeiInfo.getSbjno() + "*");
        QueryWrapper<ShebeiInfo> queryWrapper = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        Page<ShebeiInfo> page = new Page<ShebeiInfo>(pageNo, pageSize);
        IPage<ShebeiInfo> pageList = shebeiInfoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * AI智能设备分页列表查询
     *
     * @param shebeiInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "设备审核表-AI智能设备分页列表查询")
    @ApiOperation(value = "设备审核表-AI智能设备分页列表查询", notes = "设备审核表-AI智能设备分页列表查询")
    @GetMapping(value = "/ailist")
    public Result<?> queryPageaiList(ShebeiInfo shebeiInfo,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            shebeiInfo.setSysOrgCode(sys_depart_orgcode + "*");
        } else {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            shebeiInfo.setSysOrgCode("*" + loginUser.getOrgCode() + "*");
        }
        shebeiInfo.setSbjno("*" + shebeiInfo.getSbjno() + "*");
        shebeiInfo.setSbtype(57);
        QueryWrapper<ShebeiInfo> queryWrapper = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        Page<ShebeiInfo> page = new Page<ShebeiInfo>(pageNo, pageSize);
        IPage<ShebeiInfo> pageList = shebeiInfoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "设备表-查询所有设备")
    @ApiOperation(value = "设备表-查询所有设备", notes = "设备表-查询所有设备")
    @GetMapping(value = "/SBlist")
    @PermissionData(pageComponent = "system/ShebeiInfoList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryList(ShebeiInfo shebeiInfo, HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (shebeiInfo.getSbjno() == null) {
            if (shebei != null) {
                shebeiInfo.setSbjno(shebei);
            }
        }
        List<ShebeiInfo> shebeiInfos = shebeiInfoService.SBlist();
        return Result.OK(shebeiInfos);
    }

    /**
     * 当前用户获取当前设备列表(当前用户下的所有运输车设备 判断是否在线离线)
     *
     * @param ShebeiInfo
     * @param req
     * @return
     */
    @AutoLog(value = "运输车设备列表(在线 离线)")
    @ApiOperation(value = "运输车设备列表(在线 离线)", notes = "运输车设备列表(在线 离线)")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList4(String sys_depart_orgcode, ShebeiInfo ShebeiInfo, HttpServletRequest req, String sbtypes) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String[] split = sbtypes.split(",");
        List list = new ArrayList();
        for (int i = 0; i < split.length; i++) {
            list.add(split[i]);
        }
        List<ShebeiInfo> pageList = new ArrayList<ShebeiInfo>();
        if (StringUtils.isNotBlank(sys_depart_orgcode)) {
            pageList = shebeiInfoService.usershebeiList(sys_depart_orgcode, list);
        } else {
            pageList = shebeiInfoService.usershebeiList(loginUser.getOrgCode(), list);
        }

        if (pageList.size() > 0) {
            for (com.trtm.iot.system.entity.ShebeiInfo shebeiInfo : pageList) {
                String sbjno = shebeiInfo.getSbjno();
                ClxxRealdata queryone = clxxRealdataService.queryone(sbjno);
                if (queryone != null) {
                    Date datatime = queryone.getDatatime();
                    long time1 = datatime.getTime();//
                    Long s = (System.currentTimeMillis() - time1) / (1000 * 60);
                    if (s > 30) {
                        shebeiInfo.setStatus(0);
                    } else {
                        shebeiInfo.setStatus(1);
                    }
                }
            }
        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "设备表-根据设备名称查询设备经纬度")
    @ApiOperation(value = "设备表-根据设备名称查询设备经纬度", notes = "设备表-根据设备名称查询设备经纬度")
    @GetMapping(value = "/SBJWD")
    @PermissionData(pageComponent = "system/ShebeiInfoList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> SBJWD(ShebeiInfo shebeiInfo,
                           @RequestParam(name = "sbjno") String sbjno,
                           HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (shebeiInfo.getSbjno() == null) {
            if (shebei != null) {
                shebeiInfo.setSbjno(shebei);
            }
        }
        ShebeiInfo sbjwd = shebeiInfoService.SBJWD(sbjno);
        return Result.OK(sbjwd);
    }

    /**
     * 分页列表查询
     *
     * @param shebeiInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "设备审核表-设备数量统计")
    @ApiOperation(value = "设备审核表-设备数量统计", notes = "设备审核表-设备数量统计")
    @GetMapping(value = "/list5")
    public Result<?> queryPageList5(ShebeiInfo shebeiInfo,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, String sbtypes) {
        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
        List<Integer> shebeilist = new ArrayList<>();
        queryWrapper.select("ifnull(count(*),0) as testid");
        if (sbtypes != null) {
            String[] split = sbtypes.split(",");
            for (String s : split) {
                shebeilist.add(Integer.valueOf(s));
            }
            queryWrapper.in("sbtype", shebeilist);
        }
        queryWrapper.likeRight("sys_org_code", shebeiInfo.getSysOrgCode());
        ShebeiInfo pageList = shebeiInfoService.getOne(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param shebeiInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "设备审核表-设备数量统计")
    @ApiOperation(value = "设备审核表-设备数量统计", notes = "设备审核表-设备数量统计")
    @GetMapping(value = "/list8")
    public Result<?> queryPageList8(ShebeiInfo shebeiInfo,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req, String sbtypes) {
        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
        List<Integer> shebeilist = new ArrayList<>();
        queryWrapper.select("count(*) as testid");
        if (sbtypes != null) {
            String[] split = sbtypes.split(",");
            for (String s : split) {
                shebeilist.add(Integer.valueOf(s));
            }
            queryWrapper.in("sbtype", shebeilist);
        }
        queryWrapper.likeRight("sys_org_code", shebeiInfo.getSysOrgCode());
        ShebeiInfo pageList = shebeiInfoService.getOne(queryWrapper);
        Map<String, Object> map = new HashMap<>();
        double total = 0;
        double online = 0;
        double onlv = 0.0;
        if (pageList != null) {
            total = pageList.getTestid();
        }
        QueryWrapper<ShebeiInfo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("count(*) as testid");
        queryWrapper1.in("sbtype", shebeilist);
        queryWrapper1.likeRight("sys_org_code", shebeiInfo.getSysOrgCode());
        queryWrapper1.eq("status", 3);
        ShebeiInfo shebeiInfo1 = shebeiInfoService.getOne(queryWrapper1);
        if (shebeiInfo1 != null) {
            online = shebeiInfo1.getTestid();
        }
        if (total != 0) {
            onlv = online / total * 100;
        }
        map.put("total", total);
        map.put("online", online);
        map.put("onlv", onlv);
        return Result.OK(map);
    }

    /**
     * 分页列表查询
     *
     * @param shebeiInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "设备审核表-分页列表查询")
    @ApiOperation(value = "设备审核表-分页列表查询", notes = "设备审核表-分页列表查询")
    @GetMapping(value = "/list6")
    public Result<?> queryPageList6(ShebeiInfo shebeiInfo,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        QueryWrapper<ShebeiInfo> queryWrapper = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        Page<ShebeiInfo> page = new Page<ShebeiInfo>(pageNo, pageSize);
        IPage<ShebeiInfo> pageList = shebeiInfoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 按设备类型统计设备
     *
     * @param shebeiInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "设备审核表-按设备类型统计设备")
    @ApiOperation(value = "设备审核表-按设备类型统计设备", notes = "设备审核表-按设备类型统计设备")
    @GetMapping(value = "/list7")
    public Result<?> queryPageList7(ShebeiInfo shebeiInfo,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (!"null".equals(shebei)) {
            shebeiInfo.setSbjno(shebei);
        } else {
            shebeiInfo.setSbjno("空");
        }
        int snjbzsum = 0;
        int bhzsum = 0;
        int syjsum = 0;
        int byssum = 0;
        int zljsum = 0;
        int yjsum = 0;
        int spsum = 0;
        int hjsum = 0;
        int tzsbsum = 0;
        List<Map> list = new ArrayList<>();
        QueryWrapper<ShebeiInfo> queryWrapper = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper.select("count(*) as testid");
        queryWrapper.eq("status", 3);
        queryWrapper.eq("sbtype", 16);
        ShebeiInfo one = shebeiInfoService.getOne(queryWrapper);
        Map<String, Object> map = new HashMap<>();
        if (one != null) {
            snjbzsum = one.getTestid();
        }
        map.put("snjbzsum", snjbzsum);
        map.put("sbtype", "搅拌桩");
        list.add(map);
        QueryWrapper<ShebeiInfo> queryWrapper1 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper1.select("count(*) as testid,sbtype");
        queryWrapper1.eq("status", 3);
        queryWrapper1.eq("sbtype", 0);
        ShebeiInfo one1 = shebeiInfoService.getOne(queryWrapper1);
        if (one1 != null) {
            bhzsum = one1.getTestid();
        }
        Map<String, Object> map1 = new HashMap<>();
        map1.put("bhzsum", bhzsum);
        map1.put("sbtype", "拌合站");
        list.add(map1);
        QueryWrapper<ShebeiInfo> queryWrapper2 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper2.select("count(*) as testid,sbtype");
        queryWrapper2.eq("status", 3);
        queryWrapper2.in("sbtype", 3, 4, 12);
        ShebeiInfo one2 = shebeiInfoService.getOne(queryWrapper2);
        if (one2 != null) {
            syjsum = one2.getTestid();
        }
        Map<String, Object> map2 = new HashMap<>();
        map2.put("syjsum", syjsum);
        map2.put("sbtype", "试验机");
        list.add(map2);
        QueryWrapper<ShebeiInfo> queryWrapper3 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper3.select("count(*) as testid,sbtype");
        queryWrapper3.eq("status", 3);
        queryWrapper3.eq("sbtype", 11);
        ShebeiInfo one3 = shebeiInfoService.getOne(queryWrapper3);
        if (one3 != null) {
            byssum = one3.getTestid();
        }
        Map<String, Object> map3 = new HashMap<>();
        map3.put("byssum", byssum);
        map3.put("sbtype", "标养室");
        list.add(map3);
        QueryWrapper<ShebeiInfo> queryWrapper4 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper4.select("count(*) as testid,sbtype");
        queryWrapper4.eq("status", 3);
        queryWrapper4.eq("sbtype", 9);
        ShebeiInfo one4 = shebeiInfoService.getOne(queryWrapper4);
        if (one4 != null) {
            zljsum = one4.getTestid();
        }
        Map<String, Object> map4 = new HashMap<>();
        map4.put("zljsum", zljsum);
        map4.put("sbtype", "张拉设备");
        list.add(map4);
        QueryWrapper<ShebeiInfo> queryWrapper5 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper5.select("count(*) as testid,sbtype");
        queryWrapper5.eq("status", 3);
        queryWrapper5.eq("sbtype", 10);
        ShebeiInfo one5 = shebeiInfoService.getOne(queryWrapper5);
        if (one5 != null) {
            yjsum = one5.getTestid();
        }
        Map<String, Object> map5 = new HashMap<>();
        map5.put("yjsum", yjsum);
        map5.put("sbtype", "压浆设备");
        list.add(map5);
        QueryWrapper<ShebeiInfo> queryWrapper6 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper6.select("count(*) as testid,sbtype");
        queryWrapper6.eq("status", 3);
        queryWrapper6.in("sbtype", 57, 21, 23, 16);
        ShebeiInfo one6 = shebeiInfoService.getOne(queryWrapper6);
        if (one6 != null) {
            spsum = one6.getTestid();
        }
        Map<String, Object> map6 = new HashMap<>();
        map6.put("spsum", spsum);
        map6.put("sbtype", "视频监控");
        list.add(map6);
        QueryWrapper<ShebeiInfo> queryWrapper7 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper7.select("count(*) as testid,sbtype");
        queryWrapper7.eq("status", 3);
        queryWrapper7.eq("sbtype", 15);
        ShebeiInfo one7 = shebeiInfoService.getOne(queryWrapper7);
        if (one7 != null) {
            hjsum = one7.getTestid();
        }
        Map<String, Object> map7 = new HashMap<>();
        map7.put("hjsum", hjsum);
        map7.put("sbtype", "环境监测");
        list.add(map7);
        QueryWrapper<ShebeiInfo> queryWrapper8 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper8.select("count(*) as testid,sbtype");
        queryWrapper8.eq("status", 3);
        queryWrapper8.in("sbtype", 21, 23);
        ShebeiInfo one8 = shebeiInfoService.getOne(queryWrapper8);
        if (one8 != null) {
            tzsbsum = one8.getTestid();
        }
        Map<String, Object> map8 = new HashMap<>();
        map8.put("tzsbsum", tzsbsum);
        map8.put("sbtype", "特种设备");
        list.add(map8);
        return Result.OK(list);
    }


    /**
     * 按设备类型统计设备
     *
     * @param shebeiInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "设备审核表-按设备类型统计设备")
    @ApiOperation(value = "设备审核表-按设备类型统计设备", notes = "设备审核表-按设备类型统计设备")
    @GetMapping(value = "/list7s")
    public Result<?> queryPageList7s(ShebeiInfo shebeiInfo,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (!"null".equals(shebei)) {
            shebeiInfo.setSbjno(shebei);
        } else {
            shebeiInfo.setSbjno("空");
        }
        int snjbzsum = 0;
        int bhzsum = 0;
        int syjsum = 0;
        int byssum = 0;
        int zljsum = 0;
        int yjsum = 0;
        int spsum = 0;
        int hjsum = 0;
        int tzsbsum = 0;
        List<Map> list = new ArrayList<>();
        QueryWrapper<ShebeiInfo> queryWrapper = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper.select("count(*) as testid");
        queryWrapper.eq("status", 3);
        queryWrapper.eq("sbtype", 16);
        ShebeiInfo one = shebeiInfoService.getOne(queryWrapper);
        Map<String, Object> map = new HashMap<>();
        if (one != null) {
            snjbzsum = one.getTestid();
        }
        map.put("snjbzsum", snjbzsum);
        map.put("sbtype", "搅拌桩");
        list.add(map);
        QueryWrapper<ShebeiInfo> queryWrapper1 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper1.select("count(*) as testid,sbtype");
        queryWrapper1.eq("sbtype", 0);
        ShebeiInfo one1 = shebeiInfoService.getOne(queryWrapper1);
        if (one1 != null) {
            bhzsum = one1.getTestid();
        }
        Map<String, Object> map1 = new HashMap<>();
        map1.put("bhzsum", bhzsum);
        map1.put("sbtype", "拌合站");
        list.add(map1);
        QueryWrapper<ShebeiInfo> queryWrapper2 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper2.select("count(*) as testid,sbtype");
        queryWrapper2.in("sbtype", 3, 4, 12);
        ShebeiInfo one2 = shebeiInfoService.getOne(queryWrapper2);
        if (one2 != null) {
            syjsum = one2.getTestid();
        }
        Map<String, Object> map2 = new HashMap<>();
        map2.put("syjsum", syjsum);
        map2.put("sbtype", "试验机");
        list.add(map2);
        QueryWrapper<ShebeiInfo> queryWrapper3 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper3.select("count(*) as testid,sbtype");
        queryWrapper3.eq("sbtype", 11);
        ShebeiInfo one3 = shebeiInfoService.getOne(queryWrapper3);
        if (one3 != null) {
            byssum = one3.getTestid();
        }
        Map<String, Object> map3 = new HashMap<>();
        map3.put("byssum", byssum);
        map3.put("sbtype", "标养室");
        list.add(map3);
        QueryWrapper<ShebeiInfo> queryWrapper4 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper4.select("count(*) as testid,sbtype");
        queryWrapper4.eq("sbtype", 9);
        ShebeiInfo one4 = shebeiInfoService.getOne(queryWrapper4);
        if (one4 != null) {
            zljsum = one4.getTestid();
        }
        Map<String, Object> map4 = new HashMap<>();
        map4.put("zljsum", zljsum);
        map4.put("sbtype", "张拉设备");
        list.add(map4);
        QueryWrapper<ShebeiInfo> queryWrapper5 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper5.select("count(*) as testid,sbtype");
        queryWrapper5.eq("sbtype", 10);
        ShebeiInfo one5 = shebeiInfoService.getOne(queryWrapper5);
        if (one5 != null) {
            yjsum = one5.getTestid();
        }
        Map<String, Object> map5 = new HashMap<>();
        map5.put("yjsum", yjsum);
        map5.put("sbtype", "压浆设备");
        list.add(map5);
        QueryWrapper<ShebeiInfo> queryWrapper6 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper6.select("count(*) as testid,sbtype");
        queryWrapper6.in("sbtype", 75);
        ShebeiInfo one6 = shebeiInfoService.getOne(queryWrapper6);
        if (one6 != null) {
            spsum = one6.getTestid();
        }
        Map<String, Object> map6 = new HashMap<>();
        map6.put("spsum", spsum);
        map6.put("sbtype", "基坑监测");
        list.add(map6);
        QueryWrapper<ShebeiInfo> queryWrapper7 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper7.select("count(*) as testid,sbtype");
        queryWrapper7.eq("sbtype", 15);
        ShebeiInfo one7 = shebeiInfoService.getOne(queryWrapper7);
        if (one7 != null) {
            hjsum = one7.getTestid();
        }
        Map<String, Object> map7 = new HashMap<>();
        map7.put("hjsum", hjsum);
        map7.put("sbtype", "环境监测");
        list.add(map7);
        QueryWrapper<ShebeiInfo> queryWrapper8 = QueryGenerator.initQueryWrapper(shebeiInfo, req.getParameterMap());
        queryWrapper8.select("count(*) as testid,sbtype");
        queryWrapper8.in("sbtype", 21, 23);
        ShebeiInfo one8 = shebeiInfoService.getOne(queryWrapper8);
        if (one8 != null) {
            tzsbsum = one8.getTestid();
        }
        Map<String, Object> map8 = new HashMap<>();
        map8.put("tzsbsum", tzsbsum);
        map8.put("sbtype", "特种设备");
        list.add(map8);

//        Map<String, Object> map9 = new HashMap<>();
//        map9.put("jkjcsum", 3);
//        map9.put("sbtype", "基坑监测");
//        list.add(map9);
        return Result.OK(list);
    }

    /**
     * 添加
     *
     * @param shebeiInfo
     * @return
     */
    @AutoLog(value = "设备审核表-添加")
    @ApiOperation(value = "设备审核表-添加", notes = "设备审核表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ShebeiInfo shebeiInfo) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录人信息不在前端页面去选择
        shebeiInfo.setCreateBy(sysUser.getUsername());
        shebeiInfo.setCreateTime(new Date());
        String sysOrgCode = shebeiInfo.getSysOrgCode();
        Integer sbtype = shebeiInfo.getSbtype();
        /**下面是根据id直接拼接设备编号**/
        String shesbjno = null;
        if (shebeiInfo.getSbjno() == null || shebeiInfo.getSbjno().equals("默认不写会自动生成编号")) {
            String shebeisbjno = String.valueOf(FillRuleUtil.shebeisbjno(sysOrgCode, sbtype));
            List<ShebeiInfo> shebeiInfos = shebeiInfoService.arrayOneshebeis();
            Integer id = 0;
            for (ShebeiInfo info : shebeiInfos) {
                id = info.getTestid();
            }
            id = id + 1;
            shebeiInfo.setTestid(id);
            String format = String.format("%04d", id);
            shesbjno = shebeisbjno + "_" + format;
        } else {
            shesbjno = shebeiInfo.getSbjno();
        }

        /**上面是根据id直接拼接设备编号**/
        shebeiInfo.setSbjno(shesbjno);
        shebeiInfoService.save(shebeiInfo);
        return Result.OK("添加成功！");
    }

    /**
     * 审核驳回
     *
     * @param shebeiInfo
     * @return
     */
    @AutoLog(value = "设备审核表-审核驳回")
    @ApiOperation(value = "设备审核表-审核驳回", notes = "设备审核表-审核驳回")
    @PostMapping(value = "/backShebei")
    public Result<?> backShebei(@RequestBody ShebeiInfo shebeiInfo) {
        if (shebeiInfo.getShebeiStatus() == 1) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录人信息不在前端页面去选择
            shebeiInfo.setReviewBy(sysUser.getUsername());
            shebeiInfo.setReviewTime(new Date());
        } else if (shebeiInfo.getShebeiStatus() == 2) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录人信息不在前端页面去选择
            shebeiInfo.setTurndownBy(sysUser.getUsername());
            shebeiInfo.setTurndownTime(new Date());
        }

        shebeiInfoService.updateById(shebeiInfo);
        return Result.OK("成功！");
    }

    /**
     * 编辑
     *
     * @param shebeiInfo
     * @return
     */
    @AutoLog(value = "设备审核表-编辑")
    @ApiOperation(value = "设备审核表-编辑", notes = "设备审核表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ShebeiInfo shebeiInfo) {
        shebeiInfoService.updateById(shebeiInfo);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设备审核表-通过id删除")
    @ApiOperation(value = "设备审核表-通过id删除", notes = "设备审核表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        shebeiInfoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "设备审核表-批量删除")
    @ApiOperation(value = "设备审核表-批量删除", notes = "设备审核表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.shebeiInfoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设备审核表-通过id查询")
    @ApiOperation(value = "设备审核表-通过id查询", notes = "设备审核表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ShebeiInfo shebeiInfo = shebeiInfoService.getById(id);
        if (shebeiInfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(shebeiInfo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param shebeiInfo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ShebeiInfo shebeiInfo) {
        return super.exportXls(request, shebeiInfo, ShebeiInfo.class, "设备审核表");
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
        return super.importExcel(request, response, ShebeiInfo.class);
    }

    /**
     * 根据设备编号返回设备类型
     *
     * @param sbjno 设备机编号
     * @return
     */
    @AutoLog(value = "根据设备编号返回设备类型")
    @ApiOperation(value = "根据设备编号返回设备类型", notes = "根据设备编号返回设备类型")
    @GetMapping(value = "/getSbtypeByNo")
    public Result<?> getSbtypeByNo(String sbjno) {
        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sbjno", sbjno);
        ShebeiInfo shebei = shebeiInfoService.getOne(queryWrapper);
        return Result.OK(shebei.getSbtype());
    }

    @AutoLog(value = "设备表-查询用户下所有设备")
    @ApiOperation(value = "设备表-查询用户下所有设备", notes = "设备表-查询用户下所有设备")
    @GetMapping(value = "/getSBList")
    @PermissionData(pageComponent = "system/ShebeiInfoList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> getSBList(ShebeiInfo shebeiInfo, HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (shebeiInfo.getSbjno() == null) {
            if (shebei != null) {
                shebeiInfo.setSbjno(shebei);
            }
        }
        QueryWrapper<ShebeiInfo> queryWrapper = QueryGenerator.initQueryWrapper(shebeiInfo, request.getParameterMap());
        List<ShebeiInfo> shebeiInfos = shebeiInfoService.list(queryWrapper);
        return Result.OK(shebeiInfos);
    }
}
