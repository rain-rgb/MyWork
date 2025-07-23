package com.trtm.iot.lqbhz.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.bhzrecipe.entity.BhzRecipepb;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.hc_pavement_alarm.entity.HcPavementAlarm;
import com.trtm.iot.hc_transport_pave.service.IHcTransportPaveService;
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecords;
import com.trtm.iot.hc_transportrecords.service.IHcTransportrecordsService;
import com.trtm.iot.hc_truck.entity.HcTruck;
import com.trtm.iot.hc_truck.service.IHcTruckService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.mapper.BhzCementBaseMapper;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.hntbhz.vo.BhzCementBasePageRC;
import com.trtm.iot.hntbhz.vo.BhzCementBaseRC;
import com.trtm.iot.hntbhz.vo.BhzCementDetailRC;
import com.trtm.iot.hntbhz.vo.BhzCementTongJi;
import com.trtm.iot.lqbhz.entity.*;
import com.trtm.iot.lqbhz.vo.*;
import com.trtm.iot.lqbhzStatistics.entity.BhzLqStatistics;
import com.trtm.iot.lqbhzStatistics.service.IBhzLqStatisticsService;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqPhbZibiao;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqRecipe;
import com.trtm.iot.lqbhzrecipe.service.IBhzLqPhbZibiaoService;
import com.trtm.iot.lqbhzrecipe.service.IBhzLqRecipeService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.swbhz.service.IBhzSwCailiaoService;
import com.trtm.iot.swbhz.vo.BhzSwBasesPageRC;
import com.trtm.iot.swbhzStatistics.entity.BhzSwStatistics;
import com.trtm.iot.swbhzStatistics.service.IBhzSwStatisticsService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.xkcoding.http.util.StringUtil;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.util.RedisUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 沥青主表
 * @Author: jeecg-boot
 * @Date: 2021-02-22
 * @Version: V1.0
 */
@Api(tags = "沥青主表")
@RestController
@RequestMapping("/lqbhz/bhzLqBases")
@Slf4j
public class BhzLqBasesController {
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;
    @Autowired
    private IBhzLqCailiaoService bhzLqCailiaoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBhzCementOverHandlerService iBhzCementOverHandlerService;
    @Autowired
    private IShebeiInfoService iShebeiInfoService;
    @Autowired
    private IBhzLqStatisticsService iBhzLqStatisticsService;
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzSwStatisticsService bhzSwStatisticsService;
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    private IBhzSwCailiaoService bhzSwCailiaoService;
    @Autowired
    private IBhzSwBasesService bhzSwBasesService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private BhzCementBaseMapper bhzCementBaseMapper;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IHcTruckService hcTruckService;
    @Autowired
    private IHcTransportrecordsService transportrecordsService;
    @Autowired
    private IBhzLqRecipeService lqRecipeService;
    @Autowired
    private IBhzLqPhbZibiaoService bhzLqPhbZibiaoService;


    /**
     * 分页列表查询
     *
     * @param bhzLqBases
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "沥青主表-分页列表查询")
    @ApiOperation(value = "沥青主表-分页列表查询", notes = "沥青主表-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "bhz/lqbhz/BhzLqBasesList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageList(BhzLqBases bhzLqBases,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (StringUtils.isBlank(bhzLqBases.getShebeibianhao())) {
            if (!"null".equals(shebei)) {
                bhzLqBases.setShebeibianhao(shebei);
            } else {
                bhzLqBases.setShebeibianhao("空");
            }
        }
        QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        Page<BhzLqBases> page = new Page<BhzLqBases>(pageNo, pageSize);
        IPage<BhzLqBases> pageList = bhzLqBasesService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "沥青主表-分页列表查询")
    @ApiOperation(value = "沥青主表-分页列表查询", notes = "沥青主表-分页列表查询")
    @GetMapping(value = "/listjx")
    @PermissionData(pageComponent = "bhz/lqbhz/BhzLqBasesList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageListjx(BhzLqBases bhzLqBases,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        // 判断是否为查询嘉兴地区数据，是的话按照配方号进行查询
        if(sys_depart_orgcode.startsWith("A05A01A08")  ){
            String phbidsByCode = lqRecipeService.getPhbidsByCode(sys_depart_orgcode);
            if(StringUtils.isNotBlank(phbidsByCode)){
                bhzLqBases.setFormulaNo(phbidsByCode);
            }else{
                bhzLqBases.setFormulaNo("phbidsByCode");
            }

        }else{

            String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
            if (StringUtils.isBlank(bhzLqBases.getShebeibianhao())) {
                if (!"null".equals(shebei)) {
                    bhzLqBases.setShebeibianhao(shebei);
                } else {
                    bhzLqBases.setShebeibianhao("空");
                }
            }
        }

        QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        Page<BhzLqBases> page = new Page<BhzLqBases>(pageNo, pageSize);
        IPage<BhzLqBases> pageList = bhzLqBasesService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param bhzLqBases
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "沥青主表-分页列表查询（超标）")
    @ApiOperation(value = "沥青主表-分页列表查询（超标）", notes = "沥青主表-分页列表查询（超标）")
    @GetMapping(value = "/list1")
    @PermissionData(pageComponent = "bhz/lqbhz/BhzLqBasesList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageList1(BhzLqBases bhzLqBases,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzLqBases.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqBases.setShebeibianhao(shebei);
            } else {
                bhzLqBases.setShebeibianhao("空");
            }
        }

        QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper.ge("chaobiaodengji", 1);
        Page<BhzLqBases> page = new Page<BhzLqBases>(pageNo, pageSize);
        IPage<BhzLqBases> pageList = bhzLqBasesService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-超标分页列表查询")
    @ApiOperation(value = "拌合站主表-超标分页列表查询", notes = "拌合站主表-超标分页列表查询")
    @GetMapping(value = "/ChaoBiaolist")
    @PermissionData(pageComponent = "bhz/lqbhz/BhzLqBasesCBList")
    public Result<?> queryPageChaoBiaoList(BhzLqBases bhzLqBases,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           String sys_depart_orgcode,
                                           HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // 判断是否为查询嘉兴地区数据，是的话按照配方号进行查询
        if(sys_depart_orgcode.startsWith("A05A01A08")  ){
            String phbidsByCode = lqRecipeService.getPhbidsByCode(sys_depart_orgcode);
            if(StringUtils.isNotBlank(phbidsByCode)){
                bhzLqBases.setFormulaNo(phbidsByCode);
            }else{
                bhzLqBases.setFormulaNo("phbidsByCode");
            }

        }else{
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzLqBases.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqBases.setShebeibianhao(shebei);
            } else {
                bhzLqBases.setShebeibianhao("空");
            }
        }
        }
        QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        Page<BhzLqBases> page = new Page<BhzLqBases>(pageNo, pageSize);
        IPage<BhzLqBases> pageList = bhzLqBasesService.page(page, queryWrapper);
        List<BhzLqBases> list = pageList.getRecords();//请求中指定的所有用户数据。
        List<Object> records1 = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        for (BhzLqBases role : list) {
            String id = role.getGuid();//拿到唯一id
            BhzLqBasesPage bhzLqBasesPage = new BhzLqBasesPage();
            BeanUtils.copyProperties(role, bhzLqBasesPage);
            List<BhzLqCailiao> selectcailiaolist = bhzLqCailiaoService.selectcailiaolist(id);
            if (selectcailiaolist.size() > 0) {
                bhzLqBasesPage.setBhzLqCailiaoList(selectcailiaolist);
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(role.getShebeibianhao());
            if (selectshebeione != null) {
                bhzLqBasesPage.setShebeibianhao(selectshebeione.getSbname());
            }
            BhzCementOverHandler selectlist = bhzCementOverHandlerService.selectlist(id);
            if (selectlist == null) {
                BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
                bhzCementOverHandler.setOverproofStatus(0);
                bhzLqBasesPage.setBhzCementOverHandler(bhzCementOverHandler);
            } else {
                bhzLqBasesPage.setBhzCementOverHandler(selectlist);
            }
            records1.add(bhzLqBasesPage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }

    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站主表-超标分页列表查询处置审核")
    @ApiOperation(value = "拌合站主表-超标分页列表查询处置审核", notes = "拌合站主表-超标分页列表查询处置审核")
    @GetMapping(value = "/czshlist")
    @PermissionData(pageComponent = "bhz/lqbhz/BhzLqBasesCBClList")
    public Result<?> queryPageczshList(BhzLqBases bhzLqBases,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       String sys_depart_orgcode,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // 判断是否为查询嘉兴地区数据，是的话按照配方号进行查询
        if(sys_depart_orgcode.startsWith("A05A01A08")  ){
            String phbidsByCode = lqRecipeService.getPhbidsByCode(sys_depart_orgcode);
            if(StringUtils.isNotBlank(phbidsByCode)){
                bhzLqBases.setFormulaNo(phbidsByCode);
            }else{
                bhzLqBases.setFormulaNo("phbidsByCode");
            }

        }else{
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzLqBases.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqBases.setShebeibianhao(shebei);
            } else {
                bhzLqBases.setShebeibianhao("空");
            }
        }
        }
        QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        Page<BhzLqBases> page = new Page<BhzLqBases>(pageNo, pageSize);
        IPage<BhzLqBases> pageList = bhzLqBasesService.page(page, queryWrapper);
        List<BhzLqBases> list = pageList.getRecords();//请求中指定的所有用户数据。
        List<Object> records1 = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        for (BhzLqBases role : list) {
            String id = role.getGuid();//拿到唯一id
            BhzLqBasesPage bhzLqBasesPage = new BhzLqBasesPage();
            BeanUtils.copyProperties(role, bhzLqBasesPage);
            List<BhzLqCailiao> selectcailiaolist = bhzLqCailiaoService.selectcailiaolist(id);
            if (selectcailiaolist.size() > 0) {
                bhzLqBasesPage.setBhzLqCailiaoList(selectcailiaolist);
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(role.getShebeibianhao());
            if (selectshebeione != null) {
                bhzLqBasesPage.setShebeibianhao(selectshebeione.getSbname());
            }
            BhzCementOverHandler selectlist = bhzCementOverHandlerService.selectlist(id);
            if (selectlist == null) {
                BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
                bhzCementOverHandler.setOverproofStatus(0);
                bhzLqBasesPage.setBhzCementOverHandler(bhzCementOverHandler);
            } else {
                bhzLqBasesPage.setBhzCementOverHandler(selectlist);
            }
            records1.add(bhzLqBasesPage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }

    /**
     * 沥青拌合站首页统计总数以及合格超标数据以及本月超标率
     *
     * @param bhzLqStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "沥青拌合站首页统计")
    @ApiOperation(value = "沥青拌合站首页统计", notes = "沥青拌合站首页统计")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList5(BhzLqStatistics bhzLqStatistics, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        Map map = new HashMap();
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
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzLqStatistics.setShebeibianhao(shebei);
        } else {
            bhzLqStatistics.setShebeibianhao("空");
        }
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper.select("sum(all_dish) as all_dish", "sum(all_overproof_dish) as all_overproof_dish");
        queryWrapper.ge("statistics_time", parse);
        queryWrapper.le("statistics_time", parse1);
        BhzLqStatistics one = iBhzLqStatisticsService.getOne(queryWrapper);
        QueryWrapper<BhzLqStatistics> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper1.select("sum(all_dish) as all_dish", "sum(all_overproof_dish) as all_overproof_dish");
        BhzLqStatistics one1 = iBhzLqStatisticsService.getOne(queryWrapper1);
        Double lqsum = 0.0;
        Double lqcb = 0.0;
        Double lqcblv = 0.0;
        Double lqysum = 0.0;
        Double lqycb = 0.0;
        Double lqycblv = 0.0;
        if (one != null) {
            lqysum = lqysum + one.getAllDish();
            lqycb = lqycb + one.getAllOverproofDish();
        }
        if (one1 != null) {
            lqsum = lqsum + one1.getAllDish();
            lqcb = lqcb + one1.getAllOverproofDish();
        }

        Double huncblv = (lqcb / lqsum) * 100;//总的超标率
        Double hunylv = (lqycb / lqysum) * 100;//当前月的超标率
        if (huncblv > 0) {
            BigDecimal b = new BigDecimal(huncblv);
            lqcblv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        if (hunylv > 0) {
            BigDecimal b1 = new BigDecimal(hunylv);
            lqycblv = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        map.put("lqcblv", lqcblv);
        map.put("lqsum", lqsum);
        map.put("lqcb", lqcb);
        map.put("lqycblv", lqycblv);
        return Result.OK(map);
    }

    /**
     * 沥青拌合站首页统计图
     *
     * @param bhzLqStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "水稳拌合站首页统计")
    @ApiOperation(value = "水稳拌合站首页统计", notes = "水稳拌合站首页统计")
    @GetMapping(value = "/list5")
    public Result<?> queryPageList6(BhzLqStatistics bhzLqStatistics, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzLqStatistics.setShebeibianhao(shebei);
        } else {
            bhzLqStatistics.setShebeibianhao("空");
        }
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper.orderByDesc("statistics_time");
        queryWrapper.last("limit 10");
        List<BhzLqStatistics> bhzCementStatisticsList = iBhzLqStatisticsService.list(queryWrapper);
        return Result.OK(bhzCementStatisticsList);
    }

    /**
     * 沥青拌合站首页中间部分月统计
     *
     * @param bhzLqStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "沥青拌合站首页中间部分月统计")
    @ApiOperation(value = "沥青拌合站首页中间部分月统计", notes = "沥青拌合站首页中间部分月统计")
    @GetMapping(value = "/list6")
    public Result<?> queryPageList7(BhzLqStatistics bhzLqStatistics, HttpServletRequest req, String statisticsTime_begin, String statisticsTime_end) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        SimpleDateFormat format = new SimpleDateFormat("MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzLqStatistics.setShebeibianhao(shebei);
        } else {
            bhzLqStatistics.setShebeibianhao("空");
        }
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper.select("sum(all_dish) as all_dish", "statistics_time");
        if (StringUtils.isNotBlank(statisticsTime_begin) && StringUtils.isNotBlank(statisticsTime_end)) {
            try {
                Date parse = ft.parse(statisticsTime_begin);
                Date parse1 = ft.parse(statisticsTime_end);
                queryWrapper.ge("statistics_time", parse);
                queryWrapper.le("statistics_time", parse1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            queryWrapper.last("GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y-%m'))");
        } else {
            queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y-%m'))");
        }
        List<BhzLqStatistics> bhzLqStatisticsList = iBhzLqStatisticsService.list(queryWrapper);
        List list = new ArrayList();

        for (BhzLqStatistics statistics : bhzLqStatisticsList) {
            Map map = new HashMap();
            Date statisticsTime = statistics.getStatisticsTime();
            Integer allDish = statistics.getAllDish();
            String format3 = format.format(statisticsTime);
            map.put("statisticsTime", format3);
            map.put("allDish", allDish);
            list.add(map);
        }
        return Result.OK(list);
    }

    @AutoLog(value = "沥青拌合站统计分析")
    @ApiOperation(value = "沥青拌合站统计-超标盘数/方量", notes = "沥青土拌合站统计-超标盘数/方量")
    @GetMapping(value = "/list7")
    public Result<?> queryPageList7(BhzLqStatistics bhzLqStatistics, HttpServletRequest req, Integer date,String year) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));

        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        QueryWrapper<BhzLqStatistics> queryWrapper = new QueryWrapper<>();
        if (bhzLqStatistics.getShebeibianhao() != null) {
            queryWrapper.in("shebeibianhao", bhzLqStatistics.getShebeibianhao());
        } else {
            queryWrapper.in("shebeibianhao", shebeilist);
        }
        if(year != null){
            format2 = year;
        }
        if (date != null) {
            if (date == 0) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "statistics_time");
                if(year!=null){
                    queryWrapper.apply(" statistics_time like '" + format2 + "%' ");
                }
                queryWrapper.last(" GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y'))   ");
            } else if (date == 1) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "FLOOR((DATE_FORMAT(statistics_time,'%m')-1)/3)+1 as all_handle_dish");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT FLOOR((DATE_FORMAT(statistics_time,'%m')-1)/3)+1) ");
            } else if (date == 2) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "statistics_time");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y-%m'))  ");
            } else if (date == 3) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "DATE_FORMAT(statistics_time,'第%u周') as project_name");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y%u')) order by DATE_FORMAT(statistics_time,'%Y%u') desc  limit 10 ");
            }
        } else {
            queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                    "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "statistics_time");
            if(year!=null){
                queryWrapper.apply(" statistics_time like '" + format2 + "%' ");
            }
            queryWrapper.groupBy("statistics_time");
            queryWrapper.orderByDesc("statistics_time");
            queryWrapper.last("limit 10");
        }
        List<BhzLqStatistics> bhzlqStatisticsList = iBhzLqStatisticsService.list(queryWrapper);
        List list = new ArrayList();
        for (BhzLqStatistics statistics : bhzlqStatisticsList) {
            Map map = new HashMap();
            Date statisticsTime = statistics.getStatisticsTime();
            String format3 = "";
            Integer allDish = statistics.getAllDish();
            Integer allDallOverproofDishish = statistics.getAllOverproofDish();
            Integer primaryDish = statistics.getPrimaryDish();//初级超标盘数
            Integer middleDish = statistics.getMiddleDish();//中极超标盘数
            Integer advancedDish = statistics.getAdvancedDish();//高级超标盘数
            Double estimateNumber = statistics.getEstimateNumber();//方量
            Integer hegeDish = allDish - allDallOverproofDishish;//合格盘数
            if (date != null) {
                if (date == 0) {
                    format3 = format1.format(statisticsTime);
                } else if (date == 1) {
                    format3 = "第" + String.valueOf(statistics.getAllHandleDish()) + "季度";
                } else if (date == 3) {
                    format3 = statistics.getProjectName();
                } else {
                    format3 = format.format(statisticsTime);
                }
            } else {
                format3 = ft.format(statisticsTime);
            }
            map.put("statisticsTime", format3);
            map.put("primaryDish", primaryDish);
            map.put("middleDish", middleDish);
            map.put("advancedDish", advancedDish);
            map.put("estimateNumber", estimateNumber);
            map.put("hegeDish", hegeDish);
            map.put("date", date);
            list.add(map);
        }
        return Result.OK(list);
    }



    @AutoLog(value = "沥青拌合站统计分析")
    @ApiOperation(value = "沥青拌合站统计-超标盘数/方量", notes = "沥青土拌合站统计-超标盘数/方量")
    @GetMapping(value = "/list7s")
    public Result<?> queryPageList7s(BhzLqStatistics bhzLqStatistics, HttpServletRequest req, Integer date,String year,String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息

        // 判断是否为查询嘉兴地区数据，是的话按照配方号进行查询
         String phbidsByCode = lqRecipeService.getPhbidsByCode(sys_depart_orgcode);
        if(StringUtils.isNotBlank(phbidsByCode)){
            bhzLqStatistics.setFormulaNo(phbidsByCode);
        }else{
            bhzLqStatistics.setFormulaNo("phbidsByCode");
        }
        QueryWrapper<BhzLqStatistics> queryWrapper =  QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());

        if(year != null){
            format2 = year;
        }
        if (date != null) {
            if (date == 0) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "statistics_time");
                if(year!=null){
                    queryWrapper.apply(" statistics_time like '" + format2 + "%' ");
                }
                queryWrapper.last(" GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y'))   ");
            } else if (date == 1) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "FLOOR((DATE_FORMAT(statistics_time,'%m')-1)/3)+1 as all_handle_dish");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT FLOOR((DATE_FORMAT(statistics_time,'%m')-1)/3)+1) ");
            } else if (date == 2) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "statistics_time");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y-%m'))  ");
            } else if (date == 3) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "DATE_FORMAT(statistics_time,'第%u周') as project_name");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y%u')) order by DATE_FORMAT(statistics_time,'%Y%u') desc  limit 10 ");
            }
        } else {
            queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                    "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "statistics_time");
            if(year!=null){
                queryWrapper.apply(" statistics_time like '" + format2 + "%' ");
            }
            queryWrapper.groupBy("statistics_time");
            queryWrapper.orderByDesc("statistics_time");
            queryWrapper.last("limit 10");
        }
        List<BhzLqStatistics> bhzlqStatisticsList = iBhzLqStatisticsService.list(queryWrapper);
        List list = new ArrayList();
        for (BhzLqStatistics statistics : bhzlqStatisticsList) {
            Map map = new HashMap();
            Date statisticsTime = statistics.getStatisticsTime();
            String format3 = "";
            Integer allDish = statistics.getAllDish();
            Integer allDallOverproofDishish = statistics.getAllOverproofDish();
            Integer primaryDish = statistics.getPrimaryDish();//初级超标盘数
            Integer middleDish = statistics.getMiddleDish();//中极超标盘数
            Integer advancedDish = statistics.getAdvancedDish();//高级超标盘数
            Double estimateNumber = statistics.getEstimateNumber();//方量
            Integer hegeDish = allDish - allDallOverproofDishish;//合格盘数
            if (date != null) {
                if (date == 0) {
                    format3 = format1.format(statisticsTime);
                } else if (date == 1) {
                    format3 = "第" + String.valueOf(statistics.getAllHandleDish()) + "季度";
                } else if (date == 3) {
                    format3 = statistics.getProjectName();
                } else {
                    format3 = format.format(statisticsTime);
                }
            } else {
                format3 = ft.format(statisticsTime);
            }
            map.put("statisticsTime", format3);
            map.put("primaryDish", primaryDish);
            map.put("middleDish", middleDish);
            map.put("advancedDish", advancedDish);
            map.put("estimateNumber", estimateNumber);
            map.put("hegeDish", hegeDish);
            map.put("date", date);
            list.add(map);
        }
        return Result.OK(list);
    }


    @AutoLog(value = "沥青拌合站统计分析")
    @ApiOperation(value = "沥青拌合站统计-合格率", notes = "沥青拌合站统计-合格率")
    @GetMapping(value = "/list8")
    public Result<?> queryPageList8(BhzLqStatistics bhzLqStatistics, HttpServletRequest req, Integer date,String year) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        Map map = new HashMap();
        SimpleDateFormat format = new SimpleDateFormat("MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM");
        String format2 = format1.format(new Date());
        String format3 = ft1.format(new Date());
        String format4 = ft.format(new Date());
        if(year != null){
            format2 = year;
        }
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        QueryWrapper<BhzLqStatistics> queryWrapper = new QueryWrapper<>();
        if (bhzLqStatistics.getShebeibianhao() != null) {
            queryWrapper.in("shebeibianhao", bhzLqStatistics.getShebeibianhao());
        } else {
            queryWrapper.in("shebeibianhao", shebeilist);
        }
        queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number");
        if (date != null) {
            if (date == 0) {
                queryWrapper.last("and statistics_time like '" + format2 + "%'");
            } else if (date == 1) {
                if(year!=null){
                    queryWrapper.apply(" statistics_time like '" + format2 + "%' ");
                }
                queryWrapper.last("and QUARTER(statistics_time) = QUARTER(now())");
            } else if (date == 2) {
                queryWrapper.last("and statistics_time like '" + format2+format3.substring(4) + "%'");

            } else if (date == 3) {
                if(year!=null){
                    queryWrapper.apply(" statistics_time like '" + format2 + "%' ");
                }
                queryWrapper.last("and YEARWEEK(date_format(statistics_time,'%Y-%m-%d')) = YEARWEEK(now())");
            }
        } else {
            queryWrapper.last("and statistics_time like '" + format2+format4.substring(4) + "%'");
        }
        List<BhzLqStatistics> bhzlqStatisticsList = iBhzLqStatisticsService.list(queryWrapper);

        int hntsum = 0;
        int hntcb = 0;
        int prisum = 0;
        int midsum = 0;
        int advsum = 0;
        int hegesum = 0;
        for (BhzLqStatistics statistics : bhzlqStatisticsList) {
            if (statistics != null) {
                prisum = prisum + statistics.getPrimaryDish();//初级超标总盘数
                midsum = midsum + statistics.getMiddleDish();//中级超标总盘数
                advsum = advsum + statistics.getAdvancedDish();//高级超标总盘数
                hntsum = hntsum + statistics.getAllDish();//总盘数
                hntcb = hntcb + statistics.getAllOverproofDish();//超标总盘数
                hegesum = hntsum - hntcb;//合格总盘数
            }
        }

        map.put("prisum", prisum);
        map.put("midsum", midsum);
        map.put("advsum", advsum);
        map.put("hegesum", hegesum);
        map.put("date", date);
        return Result.OK(map);
    }

    /**
     * 沥青拌合站首页超标等级盘数/未处置/已处置盘数
     *
     * @param bhzLqStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "沥青拌合站首页超标等级盘数/未处置/已处置盘数")
    @ApiOperation(value = "沥青拌合站首页超标等级盘数/未处置/已处置盘数", notes = "沥青拌合站首页超标等级盘数/未处置/已处置盘数")
    @GetMapping(value = "/list9")
    public Result<?> queryPageList9(BhzLqStatistics bhzLqStatistics, BhzLqBases bhzLqBases, HttpServletRequest req ) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (bhzLqStatistics.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqStatistics.setShebeibianhao(shebei);
            } else {
                bhzLqStatistics.setShebeibianhao("空");
            }
        }
        if (bhzLqBases.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqBases.setShebeibianhao(shebei);
            } else {
                bhzLqBases.setShebeibianhao("空");
            }
        }
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper.select("ifnull(sum(primary_dish),0) as primary_dish,ifnull(sum(middle_dish),0) as middle_dish," +
                "ifnull(sum(advanced_dish),0) as advanced_dish,ifnull(sum(all_dish),0) as all_dish," +
                "ifnull(sum(all_overproof_dish),0) as all_overproof_dish");
        BhzLqStatistics bhzLqStatisticsList = iBhzLqStatisticsService.getOne(queryWrapper);

        QueryWrapper<BhzLqBases> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper1.select("count(*) as id");
        queryWrapper1.lt("overproof_status", 0);
        BhzLqBases one = bhzLqBasesService.getOne(queryWrapper1);

        QueryWrapper<BhzLqBases> queryWrapper2 = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper2.select("count(*) as id");
        queryWrapper2.eq("chaobiaodengji", 1);
        queryWrapper2.lt("overproof_status", 0);
        BhzLqBases one1 = bhzLqBasesService.getOne(queryWrapper2);
        QueryWrapper<BhzLqBases> queryWrapper3 = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper3.select("count(*) as id");
        queryWrapper3.eq("chaobiaodengji", 2);
        queryWrapper3.lt("overproof_status", 0);
        BhzLqBases one2 = bhzLqBasesService.getOne(queryWrapper3);
        QueryWrapper<BhzLqBases> queryWrapper4 = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper4.select("count(*) as id");
        queryWrapper4.eq("chaobiaodengji", 3);
        queryWrapper4.lt("overproof_status", 0);
        BhzLqBases one3 = bhzLqBasesService.getOne(queryWrapper2);

        double lqsum = 0.0;//总盘数
        double lqprimary = 0.0;//初级超标盘数
        double lqmiddle = 0.0;//中级超标盘数
        double lqadvance = 0.0;//高级超标盘数
        double lqhege = 0.0;//合格盘数
        double lqcb = 0.0;//超标总盘数

        double lqhandle = 0.0;//超标已处置盘数
        double lqnohandle = 0.0;//超标未处置盘数
        double handlelv = 0.0;//处置率
        double nohandlelv = 0.0;//未处置率

        double primarylv = 0.0;//初级超标率
        double middlelv = 0.0;//中级超标率
        double advancedlv = 0.0;//高级超标率
        double hegelv = 0.0;//合格率

        double primaryHandle = 0.0;//初级超标处置盘数
        double middleHandle = 0.0;//中级超标处置盘数
        double advanceHandle = 0.0;//高级超标处置盘数
        double primaryHandlelv = 0.0;//初级超标处置率
        double middleHandlelv = 0.0;//中级超标处置率
        double advanceHandlelv = 0.0;//高级超标处置率

        if (bhzLqStatisticsList != null) {
            lqsum = bhzLqStatisticsList.getAllDish();
            lqprimary = bhzLqStatisticsList.getPrimaryDish();
            lqmiddle = bhzLqStatisticsList.getMiddleDish();
            lqadvance = bhzLqStatisticsList.getAdvancedDish();
            lqcb = bhzLqStatisticsList.getAllOverproofDish();
            lqhege = lqsum - lqcb;
            if (lqsum != 0) {
                primarylv = Double.parseDouble(String.format("%.2f", (lqprimary / lqsum) * 100));
                middlelv = Double.parseDouble(String.format("%.2f", (lqmiddle / lqsum) * 100));
                advancedlv = Double.parseDouble(String.format("%.2f", (lqadvance / lqsum) * 100));
                hegelv = Double.parseDouble(String.format("%.2f", (lqhege / lqsum) * 100));
            }
        }
        if (one != null) {
            lqhandle = one.getId();
            lqnohandle = lqcb - lqhandle;
        }
        if (one1 != null) {
            primaryHandle = one1.getId();
        }
        if (one2 != null) {
            middleHandle = one2.getId();
        }
        if (one3 != null) {
            advanceHandle = one3.getId();
        }
        if (lqcb != 0) {
            primaryHandlelv = Double.parseDouble(String.format("%.2f", (primaryHandle / lqcb) * 100));
            middleHandlelv = Double.parseDouble(String.format("%.2f", (middleHandle / lqcb) * 100));
            advanceHandlelv = Double.parseDouble(String.format("%.2f", (advanceHandle / lqcb) * 100));
            handlelv = Double.parseDouble(String.format("%.2f", (lqhandle / lqcb) * 100));
            nohandlelv = Double.parseDouble(String.format("%.2f", (lqnohandle / lqcb) * 100));

        }
        Map map = new HashMap();
        map.put("lqprimary", lqprimary);
        map.put("lqmiddle", lqmiddle);
        map.put("lqadvance", lqadvance);
        map.put("lqhege", lqhege);
        map.put("lqhandle", lqhandle);
        map.put("lqnohandle", lqnohandle);
        map.put("primarylv", primarylv);
        map.put("middlelv", middlelv);
        map.put("advancedlv", advancedlv);
        map.put("hegelv", hegelv);
        map.put("primaryHandle", primaryHandle);
        map.put("middleHandle", middleHandle);
        map.put("advanceHandle", advanceHandle);
        map.put("primaryHandlelv", primaryHandlelv);
        map.put("middleHandlelv", middleHandlelv);
        map.put("advanceHandlelv", advanceHandlelv);
        map.put("handlelv", handlelv);
        map.put("nohandlelv", nohandlelv);
        return Result.OK(map);
    }

    @AutoLog(value = "沥青拌合站首页超标等级盘数/未处置/已处置盘数")
    @ApiOperation(value = "沥青拌合站首页超标等级盘数/未处置/已处置盘数", notes = "沥青拌合站首页超标等级盘数/未处置/已处置盘数")
    @GetMapping(value = "/list9s")
    public Result<?> queryPageList9s(BhzLqStatistics bhzLqStatistics, BhzLqBases bhzLqBases,String sys_depart_orgcode, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String phbidsByCode = lqRecipeService.getPhbidsByCode(sys_depart_orgcode);
        if(StringUtils.isNotBlank(phbidsByCode)){
            bhzLqBases.setFormulaNo(phbidsByCode);
            bhzLqStatistics.setFormulaNo(phbidsByCode);
        }else{
            bhzLqBases.setFormulaNo("phbidsByCode");
            bhzLqStatistics.setFormulaNo("phbidsByCode");
        }

        //查询到他的设备编号
//        if (bhzLqStatistics.getShebeibianhao() == null) {
//            if (!"null".equals(shebei)) {
//                bhzLqStatistics.setShebeibianhao(shebei);
//            } else {
//                bhzLqStatistics.setShebeibianhao("空");
//            }
//        }
//        if (bhzLqBases.getShebeibianhao() == null) {
//            if (!"null".equals(phbidsByCode)) {
//                bhzLqBases.setShebeibianhao(shebei);
//            } else {
//                bhzLqBases.setShebeibianhao("空");
//            }
//        }
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper.select("ifnull(sum(primary_dish),0) as primary_dish,ifnull(sum(middle_dish),0) as middle_dish," +
                "ifnull(sum(advanced_dish),0) as advanced_dish,ifnull(sum(all_dish),0) as all_dish," +
                "ifnull(sum(all_overproof_dish),0) as all_overproof_dish");
        BhzLqStatistics bhzLqStatisticsList = iBhzLqStatisticsService.getOne(queryWrapper);

        QueryWrapper<BhzLqBases> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper1.select("count(*) as id");
        queryWrapper1.lt("overproof_status", 0);
        BhzLqBases one = bhzLqBasesService.getOne(queryWrapper1);

        QueryWrapper<BhzLqBases> queryWrapper2 = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper2.select("count(*) as id");
        queryWrapper2.eq("chaobiaodengji", 1);
        queryWrapper2.lt("overproof_status", 0);
        BhzLqBases one1 = bhzLqBasesService.getOne(queryWrapper2);
        QueryWrapper<BhzLqBases> queryWrapper3 = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper3.select("count(*) as id");
        queryWrapper3.eq("chaobiaodengji", 2);
        queryWrapper3.lt("overproof_status", 0);
        BhzLqBases one2 = bhzLqBasesService.getOne(queryWrapper3);
        QueryWrapper<BhzLqBases> queryWrapper4 = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper4.select("count(*) as id");
        queryWrapper4.eq("chaobiaodengji", 3);
        queryWrapper4.lt("overproof_status", 0);
        BhzLqBases one3 = bhzLqBasesService.getOne(queryWrapper2);

        double lqsum = 0.0;//总盘数
        double lqprimary = 0.0;//初级超标盘数
        double lqmiddle = 0.0;//中级超标盘数
        double lqadvance = 0.0;//高级超标盘数
        double lqhege = 0.0;//合格盘数
        double lqcb = 0.0;//超标总盘数

        double lqhandle = 0.0;//超标已处置盘数
        double lqnohandle = 0.0;//超标未处置盘数
        double handlelv = 0.0;//处置率
        double nohandlelv = 0.0;//未处置率

        double primarylv = 0.0;//初级超标率
        double middlelv = 0.0;//中级超标率
        double advancedlv = 0.0;//高级超标率
        double hegelv = 0.0;//合格率

        double primaryHandle = 0.0;//初级超标处置盘数
        double middleHandle = 0.0;//中级超标处置盘数
        double advanceHandle = 0.0;//高级超标处置盘数
        double primaryHandlelv = 0.0;//初级超标处置率
        double middleHandlelv = 0.0;//中级超标处置率
        double advanceHandlelv = 0.0;//高级超标处置率

        if (bhzLqStatisticsList != null) {
            lqsum = bhzLqStatisticsList.getAllDish();
            lqprimary = bhzLqStatisticsList.getPrimaryDish();
            lqmiddle = bhzLqStatisticsList.getMiddleDish();
            lqadvance = bhzLqStatisticsList.getAdvancedDish();
            lqcb = bhzLqStatisticsList.getAllOverproofDish();
            lqhege = lqsum - lqcb;
            if (lqsum != 0) {
                primarylv = Double.parseDouble(String.format("%.2f", (lqprimary / lqsum) * 100));
                middlelv = Double.parseDouble(String.format("%.2f", (lqmiddle / lqsum) * 100));
                advancedlv = Double.parseDouble(String.format("%.2f", (lqadvance / lqsum) * 100));
                hegelv = Double.parseDouble(String.format("%.2f", (lqhege / lqsum) * 100));
            }
        }
        if (one != null) {
            lqhandle = one.getId();
            lqnohandle = lqcb - lqhandle;
        }
        if (one1 != null) {
            primaryHandle = one1.getId();
        }
        if (one2 != null) {
            middleHandle = one2.getId();
        }
        if (one3 != null) {
            advanceHandle = one3.getId();
        }
        if (lqcb != 0) {
            primaryHandlelv = Double.parseDouble(String.format("%.2f", (primaryHandle / lqcb) * 100));
            middleHandlelv = Double.parseDouble(String.format("%.2f", (middleHandle / lqcb) * 100));
            advanceHandlelv = Double.parseDouble(String.format("%.2f", (advanceHandle / lqcb) * 100));
            handlelv = Double.parseDouble(String.format("%.2f", (lqhandle / lqcb) * 100));
            nohandlelv = Double.parseDouble(String.format("%.2f", (lqnohandle / lqcb) * 100));

        }
        Map map = new HashMap();
        map.put("lqprimary", lqprimary);
        map.put("lqmiddle", lqmiddle);
        map.put("lqadvance", lqadvance);
        map.put("lqhege", lqhege);
        map.put("lqhandle", lqhandle);
        map.put("lqnohandle", lqnohandle);
        map.put("primarylv", primarylv);
        map.put("middlelv", middlelv);
        map.put("advancedlv", advancedlv);
        map.put("hegelv", hegelv);
        map.put("primaryHandle", primaryHandle);
        map.put("middleHandle", middleHandle);
        map.put("advanceHandle", advanceHandle);
        map.put("primaryHandlelv", primaryHandlelv);
        map.put("middleHandlelv", middleHandlelv);
        map.put("advanceHandlelv", advanceHandlelv);
        map.put("handlelv", handlelv);
        map.put("nohandlelv", nohandlelv);
        return Result.OK(map);
    }
    /**
     * 沥青拌合站首页中间部分月产能统计
     *
     * @param bhzLqStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "沥青拌合站首页中间部分月产能统计")
    @ApiOperation(value = "沥青拌合站首页中间部分月产能统计", notes = "沥青拌合站首页中间部分月产能统计")
    @GetMapping(value = "/list11")
    public Result<?> queryPageList11(BhzLqStatistics bhzLqStatistics, HttpServletRequest req, String statisticsTime_begin, String statisticsTime_end ,String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        SimpleDateFormat format = new SimpleDateFormat("MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        //查询到他的设备编号
        if (bhzLqStatistics.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqStatistics.setShebeibianhao(shebei);
            } else {
                bhzLqStatistics.setShebeibianhao("空");
            }
        }
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper.select(
                "ifnull(sum(all_dish),0) as all_dish", "ifnull(sum(primary_dish),0) as primary_dish",
                "ifnull(sum(middle_dish),0) as middle_dish", "ifnull(sum(advanced_dish),0) as advanced_dish",
                "ifnull(sum(estimate_number),0) as estimate_number", "ifnull(sum(all_overproof_dish),0) as all_overproof_dish",
                "statistics_time");
        if (StringUtils.isNotBlank(statisticsTime_begin) && StringUtils.isNotBlank(statisticsTime_end)) {
            try {
                Date parse = ft.parse(statisticsTime_begin);
                Date parse1 = ft.parse(statisticsTime_end);
                queryWrapper.ge("statistics_time", parse);
                queryWrapper.le("statistics_time", parse1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            queryWrapper.likeRight("statistics_time", format2);
        }
        queryWrapper.last("GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y-%m'))");
        List<BhzLqStatistics> bhzCementStatisticsList = iBhzLqStatisticsService.list(queryWrapper);
        List list = new ArrayList<>();

        for (BhzLqStatistics statistics : bhzCementStatisticsList) {
            Map map = new HashMap<>();
            Date statisticsTime = statistics.getStatisticsTime();
            double allDish = statistics.getAllDish();
            double primaryDish = statistics.getPrimaryDish();
            double middleDish = statistics.getMiddleDish();
            double advancedDish = statistics.getAdvancedDish();
            double allOverproofDish = statistics.getAllOverproofDish();
            double hegeDish = allDish - allOverproofDish;
            double estimateNumber = statistics.getEstimateNumber();
            double primarylv = Double.parseDouble(String.format("%.2f", (primaryDish / allDish) * 100));
            double middlelv = Double.parseDouble(String.format("%.2f", (middleDish / allDish) * 100));
            double advancedlv = Double.parseDouble(String.format("%.2f", (advancedDish / allDish) * 100));
            double hegelv = Double.parseDouble(String.format("%.2f", (hegeDish / allDish) * 100));
            String format3 = format.format(statisticsTime);
            map.put("statisticsTime", format3);
            map.put("estimateNumber", estimateNumber);
            map.put("primarylv", primarylv);
            map.put("middlelv", middlelv);
            map.put("advancedlv", advancedlv);
            map.put("hegelv", hegelv);
            list.add(map);
        }
        return Result.OK(list);
    }

    @AutoLog(value = "沥青拌合站首页中间部分月产能统计")
    @ApiOperation(value = "沥青拌合站首页中间部分月产能统计", notes = "沥青拌合站首页中间部分月产能统计")
    @GetMapping(value = "/list11s")
    public Result<?> queryPageList11s(BhzLqStatistics bhzLqStatistics, HttpServletRequest req, String statisticsTime_begin, String statisticsTime_end ,String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        SimpleDateFormat format = new SimpleDateFormat("MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        String phbidsByCode = lqRecipeService.getPhbidsByCode(sys_depart_orgcode);
        if(StringUtils.isNotBlank(phbidsByCode)){
            bhzLqStatistics.setFormulaNo(phbidsByCode);
        }else{
            bhzLqStatistics.setFormulaNo("phbidsByCode");
        }

        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper.select(
                "ifnull(sum(all_dish),0) as all_dish", "ifnull(sum(primary_dish),0) as primary_dish",
                "ifnull(sum(middle_dish),0) as middle_dish", "ifnull(sum(advanced_dish),0) as advanced_dish",
                "ifnull(sum(estimate_number),0) as estimate_number", "ifnull(sum(all_overproof_dish),0) as all_overproof_dish",
                "statistics_time");
        if (StringUtils.isNotBlank(statisticsTime_begin) && StringUtils.isNotBlank(statisticsTime_end)) {
            try {
                Date parse = ft.parse(statisticsTime_begin);
                Date parse1 = ft.parse(statisticsTime_end);
                queryWrapper.ge("statistics_time", parse);
                queryWrapper.le("statistics_time", parse1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            queryWrapper.likeRight("statistics_time", format2);
        }
        queryWrapper.last("GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y-%m'))");
        List<BhzLqStatistics> bhzCementStatisticsList = iBhzLqStatisticsService.list(queryWrapper);
        List list = new ArrayList<>();

        for (BhzLqStatistics statistics : bhzCementStatisticsList) {
            Map map = new HashMap<>();
            Date statisticsTime = statistics.getStatisticsTime();
            double allDish = statistics.getAllDish();
            double primaryDish = statistics.getPrimaryDish();
            double middleDish = statistics.getMiddleDish();
            double advancedDish = statistics.getAdvancedDish();
            double allOverproofDish = statistics.getAllOverproofDish();
            double hegeDish = allDish - allOverproofDish;
            double estimateNumber = statistics.getEstimateNumber();
            double primarylv = Double.parseDouble(String.format("%.2f", (primaryDish / allDish) * 100));
            double middlelv = Double.parseDouble(String.format("%.2f", (middleDish / allDish) * 100));
            double advancedlv = Double.parseDouble(String.format("%.2f", (advancedDish / allDish) * 100));
            double hegelv = Double.parseDouble(String.format("%.2f", (hegeDish / allDish) * 100));
            String format3 = format.format(statisticsTime);
            map.put("statisticsTime", format3);
            map.put("estimateNumber", estimateNumber);
            map.put("primarylv", primarylv);
            map.put("middlelv", middlelv);
            map.put("advancedlv", advancedlv);
            map.put("hegelv", hegelv);
            list.add(map);
        }
        return Result.OK(list);
    }

    /**
     * 未处理数据分页列表查询
     *
     * @param bhzLqBases
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "沥青主表-未处理数据分页列表查询")
    @ApiOperation(value = "沥青主表-未处理数据分页列表查询", notes = "沥青主表-未处理数据分页列表查询")
    @GetMapping(value = "/list12")
    public Result<?> queryPageList12(BhzLqBases bhzLqBases,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzLqBases.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqBases.setShebeibianhao(shebei);
            } else {
                bhzLqBases.setShebeibianhao("空");
            }
        }
        QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper.gt("chaobiaodengji", 0);
        queryWrapper.lt("overproof_status", 20);
        Page<BhzLqBases> page = new Page<BhzLqBases>(pageNo, pageSize);
        IPage<BhzLqBases> pageList = bhzLqBasesService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
    @AutoLog(value = "沥青主表-未处理数据分页列表查询")
    @ApiOperation(value = "沥青主表-未处理数据分页列表查询", notes = "沥青主表-未处理数据分页列表查询")
    @GetMapping(value = "/list12s")
    public Result<?> queryPageList12s(BhzLqBases bhzLqBases,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String phbidsByCode = lqRecipeService.getPhbidsByCode(sys_depart_orgcode);
        if(StringUtils.isNotBlank(phbidsByCode)){
            bhzLqBases.setFormulaNo(phbidsByCode);
        }else{
            bhzLqBases.setFormulaNo("phbidsByCode");
        }

//        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
//        if (bhzLqBases.getShebeibianhao() == null) {
//            if (!"null".equals(shebei)) {
//                bhzLqBases.setShebeibianhao(shebei);
//            } else {
//                bhzLqBases.setShebeibianhao("空");
//            }
//        }
        QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper.gt("chaobiaodengji", 0);
        queryWrapper.lt("overproof_status", 20);
        Page<BhzLqBases> page = new Page<BhzLqBases>(pageNo, pageSize);
        IPage<BhzLqBases> pageList = bhzLqBasesService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "沥青主表-未处理的数据分页查询")
    @ApiOperation(value = "沥青主表-未处理的数据分页查询", notes = "沥青主表-未处理的数据分页查询")
    @GetMapping(value = "/selectNotHandleList")
    public Result<?> selectNotHandleList(BhzLqBases bhzLqBases,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req) {
        //获取当前用户信息
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzLqBases.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqBases.setShebeibianhao(shebei);
            } else {
                bhzLqBases.setShebeibianhao("null");
            }
        }
        QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper.gt("chaobiandengji", 0);
        queryWrapper.le("overproof_status", 20);
        Page<BhzLqBases> page = new Page<>(pageNo, pageSize);
        Page<BhzLqBases> pageList = bhzLqBasesService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 沥青拌合站首页统计本月超标率/合格率/处置率/中间部分饼图
     *
     * @param bhzLqStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "沥青拌合站首页统计本月超标率/合格率/处置率/中间部分饼图")
    @ApiOperation(value = "沥青拌合站首页统计本月超标率/合格率/处置率/中间部分饼图", notes = "沥青拌合站首页统计本月超标率/合格率/处置率/中间部分饼图")
    @GetMapping(value = "/list13")
    public Result<?> queryPageList13(BhzLqStatistics bhzLqStatistics, HttpServletRequest req, String statisticsTime_begin, String statisticsTime_end) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        Map map = new HashMap();
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
        //查询到他的设备编号
        if (bhzLqStatistics.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqStatistics.setShebeibianhao(shebei);
            } else {
                bhzLqStatistics.setShebeibianhao("空");
            }
        }
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper.select("ifnull(sum(primary_dish),0) as primary_dish,ifnull(sum(middle_dish),0) as middle_dish," +
                "ifnull(sum(advanced_dish),0) as advanced_dish,ifnull(sum(all_dish),0) as all_dish," +
                "ifnull(sum(all_handle_dish),0) as all_handle_dish,ifnull(sum(all_overproof_dish),0) as all_overproof_dish," +
                "ifnull(sum(primary_handle),0) as primary_handle,ifnull(sum(middle_handle),0) as middle_handle," +
                "ifnull(sum(advanced_handle),0) as advanced_handle");
        if (StringUtils.isNotBlank(statisticsTime_begin) && StringUtils.isNotBlank(statisticsTime_end)) {
            try {
                Date parse2 = format.parse(statisticsTime_begin);
                Date parse3 = format.parse(statisticsTime_end);
                queryWrapper.ge("statistics_time", parse2);
                queryWrapper.le("statistics_time", parse3);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            queryWrapper.ge("statistics_time", parse);
            queryWrapper.le("statistics_time", parse1);
        }
        List<BhzLqStatistics> bhzLqStatisticsList = iBhzLqStatisticsService.list(queryWrapper);
        double lqsums = 0.0;//总盘数
        double lqprimarys = 0.0;//初级超标盘数
        double lqmiddles = 0.0;//中级超标盘数
        double lqadvances = 0.0;//高级超标盘数
        double lqheges = 0.0;//合格盘数
        double lqcbs = 0.0;//超标总盘数

        double lqhandles = 0.0;//超标已处置盘数
        double lqnohandles = 0.0;//超标未处置盘数
        double handlelvs = 0.0;//处置率
        double nohandlelvs = 0.0;//未处置率

        double primarylvs = 0.0;//初级超标率
        double middlelvs = 0.0;//中级超标率
        double advancedlvs = 0.0;//高级超标率
        double hegelvs = 0.0;//合格率

        for (BhzLqStatistics bhzLqStatistics1 : bhzLqStatisticsList) {
            lqsums = bhzLqStatistics1.getAllDish();
            lqprimarys = bhzLqStatistics1.getPrimaryDish();
            lqmiddles = bhzLqStatistics1.getMiddleDish();
            lqadvances = bhzLqStatistics1.getAdvancedDish();
            lqcbs = bhzLqStatistics1.getAllOverproofDish();
            lqheges = lqsums - lqcbs;
            lqhandles = bhzLqStatistics1.getAllHandleDish();
            lqnohandles = lqcbs - lqhandles;
            if (lqsums != 0) {
                primarylvs = Double.parseDouble(String.format("%.2f", (lqprimarys / lqsums) * 100));
                middlelvs = Double.parseDouble(String.format("%.2f", (lqmiddles / lqsums) * 100));
                advancedlvs = Double.parseDouble(String.format("%.2f", (lqadvances / lqsums) * 100));
                hegelvs = Double.parseDouble(String.format("%.2f", (lqheges / lqsums) * 100));
            }
            if (lqcbs != 0) {
                handlelvs = Double.parseDouble(String.format("%.2f", (lqhandles / lqcbs) * 100));
                nohandlelvs = Double.parseDouble(String.format("%.2f", (lqnohandles / lqcbs) * 100));

            }
        }
        map.put("primarylvs", primarylvs);
        map.put("middlelvs", middlelvs);
        map.put("advancedlvs", advancedlvs);
        map.put("hegelvs", hegelvs);
        map.put("handlelvs", handlelvs);
        map.put("nohandlelvs", nohandlelvs);
        map.put("lqprimarys", lqprimarys);
        map.put("lqmiddles", lqmiddles);
        map.put("lqadvances", lqadvances);
        map.put("lqheges", lqheges);
        return Result.OK(map);
    }


    @AutoLog(value = "砼/沥青/水稳拌合站统计分析")
    @ApiOperation(value = "砼/沥青/水稳拌合站统计分析-今日/累计预警统计", notes = "砼/沥青/水稳拌合站统计分析-今日/累计预警统计")
    @GetMapping(value = "/liststa")
    public Result<?> queryPageListsta(BhzLqStatistics bhzLqStatistics, BhzSwStatistics bhzSwStatistics, BhzCementStatistics bhzCementStatistics, HttpServletRequest req, Integer date) {
//        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        Map map = new HashMap();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format = ft.format(new Date());
        //查询到他的设备编号
//        if (bhzCementStatistics.getShebeiNo() == null) {
//            if (!"null".equals(shebei)) {
//                bhzCementStatistics.setShebeiNo(shebei);
//            } else {
//                bhzCementStatistics.setShebeiNo(shebei);
//            }
//        }
//        if (bhzLqStatistics.getShebeibianhao() == null) {
//            if (!"null".equals(shebei)) {
//                bhzLqStatistics.setShebeibianhao(shebei);
//            } else {
//                bhzLqStatistics.setShebeibianhao("空");
//            }
//        }
//        if (bhzSwStatistics.getShebeibianhao() == null) {
//            if (!"null".equals(shebei)) {
//                bhzSwStatistics.setShebeibianhao(shebei);
//            } else {
//                bhzSwStatistics.setShebeibianhao("空");
//            }
//        }
        int lqyjdishDay = 0;
        int lqyjdishSum = 0;
        int swyjdishDay = 0;
        int swyjdishSum = 0;
        int hntyjdishDay = 0;
        int hntyjdishSum = 0;
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper.select("sum(all_overproof_dish) as all_overproof_dish");
        queryWrapper.likeRight("statistics_time", format);
        BhzLqStatistics statistics = iBhzLqStatisticsService.getOne(queryWrapper);
        if (statistics != null) {
            lqyjdishDay = statistics.getAllOverproofDish();//沥青今日超标总盘数
        }
        QueryWrapper<BhzLqStatistics> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper1.select("sum(all_overproof_dish) as all_overproof_dish");
        BhzLqStatistics statisticss = iBhzLqStatisticsService.getOne(queryWrapper1);
        if (statisticss != null) {
            lqyjdishSum = statisticss.getAllOverproofDish();//沥青累计超标总盘数
        }
        QueryWrapper<BhzSwStatistics> queryWrapper2 = QueryGenerator.initQueryWrapper(bhzSwStatistics, req.getParameterMap());
        queryWrapper2.select("sum(all_overproof_dish) as all_overproof_dish");
        BhzSwStatistics statisticss1 = bhzSwStatisticsService.getOne(queryWrapper2);
        if (statisticss1 != null) {
            swyjdishSum = statisticss1.getAllOverproofDish();//水稳累计超标总盘数
        }
        QueryWrapper<BhzSwStatistics> queryWrapper3 = QueryGenerator.initQueryWrapper(bhzSwStatistics, req.getParameterMap());
        queryWrapper3.select("sum(all_overproof_dish) as all_overproof_dish");
        queryWrapper3.likeRight("statistics_time", format);
        BhzSwStatistics statistics1 = bhzSwStatisticsService.getOne(queryWrapper3);
        if (statistics1 != null) {
            swyjdishDay = statistics1.getAllOverproofDish();//水稳今日超标总盘数
        }
        QueryWrapper<BhzCementStatistics> queryWrapper4 = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper4.select("sum(all_overproof_dish) as all_overproof_dish");
        BhzCementStatistics statistics2 = bhzCementStatisticsService.getOne(queryWrapper4);
        if (statistics2 != null) {
            hntyjdishSum = statistics2.getAllOverproofDish();
        }
        QueryWrapper<BhzCementStatistics> queryWrapper5 = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper5.select("sum(all_overproof_dish) as all_overproof_dish");
        queryWrapper5.likeRight("statistics_time", format);
        BhzCementStatistics statisticss2 = bhzCementStatisticsService.getOne(queryWrapper5);
        if (statisticss2 != null) {
            hntyjdishDay = statisticss2.getAllOverproofDish();
        }
        map.put("hntyjdishDay", hntyjdishDay);
        map.put("hntyjdishSum", hntyjdishSum);
        map.put("lqyjdishDay", lqyjdishDay);
        map.put("lqyjdishSum", lqyjdishSum);
        map.put("swyjdishSum", swyjdishSum);
        map.put("swyjdishDay", swyjdishDay);
        return Result.OK(map);
    }

    /**
     * 分页列表查询
     *
     * @param bhzLqBases
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "沥青主表-分页列表查询")
    @ApiOperation(value = "沥青主表-分页列表查询", notes = "沥青主表-分页列表查询")
    @GetMapping(value = "/sgbwlist")
    public Result<?> queryPagesgbwList(BhzLqBases bhzLqBases,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                       HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzLqBases.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqBases.setShebeibianhao(shebei);
            } else {
                bhzLqBases.setShebeibianhao("空");
            }
        }
        QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper.select("DISTINCT(poure_location) as poure_location");
        List<BhzLqBases> pageList = bhzLqBasesService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bhzLqBasesPage
     * @return
     */
    @AutoLog(value = "沥青主表-添加")
    @ApiOperation(value = "沥青主表-添加", notes = "沥青主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BhzLqBasesPage bhzLqBasesPage) {
        BhzLqBases bhzLqBases = new BhzLqBases();
        BeanUtils.copyProperties(bhzLqBasesPage, bhzLqBases);
        bhzLqBasesService.saveMain(bhzLqBases, bhzLqBasesPage.getBhzLqCailiaoList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bhzLqBasesPage
     * @return
     */
    @AutoLog(value = "沥青主表-编辑")
    @ApiOperation(value = "沥青主表-编辑", notes = "沥青主表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BhzLqBasesPage bhzLqBasesPage) {
        BhzLqBases bhzLqBases = new BhzLqBases();
        BeanUtils.copyProperties(bhzLqBasesPage, bhzLqBases);
        BhzLqBases bhzLqBasesEntity = bhzLqBasesService.getById(bhzLqBases.getId());
        if (bhzLqBasesEntity == null) {
            return Result.error("未找到对应数据");
        }
        bhzLqBasesService.updateMain(bhzLqBases, bhzLqBasesPage.getBhzLqCailiaoList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "沥青主表-通过id删除")
    @ApiOperation(value = "沥青主表-通过id删除", notes = "沥青主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzLqBasesService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "沥青主表-批量删除")
    @ApiOperation(value = "沥青主表-批量删除", notes = "沥青主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzLqBasesService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "沥青主表-通过id查询")
    @ApiOperation(value = "沥青主表-通过id查询", notes = "沥青主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BhzLqBases bhzLqBases = bhzLqBasesService.getById(id);
        if (bhzLqBases == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzLqBases);

    }

    /**
     * 通过id查询
     *
     * @param
     * @return
     */
    @AutoLog(value = "沥青子表信息通过主表ID查询")
    @ApiOperation(value = "沥青子表信息主表ID查询", notes = "沥青子表信息-通主表ID查询")
    @GetMapping(value = "/queryBhzLqCailiaoByMainId")
//	@PermissionData(pageComponent = "bhz/lqbhz/BhzLqBasesCBList")
    public Result<?> queryBhzLqCailiaoListByMainId(BhzLqCailiao bhzLqCailiaoList, HttpServletRequest req, @RequestParam(name = "baseGuid", required = true) String baseGuid) {
        QueryWrapper<BhzLqCailiao> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqCailiaoList, req.getParameterMap());
        List<BhzLqCailiao> pageList = bhzLqCailiaoService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzLqBases
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzLqBases bhzLqBases) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqBases, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<BhzLqBases> queryList = bhzLqBasesService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<BhzLqBases> bhzLqBasesList = new ArrayList<BhzLqBases>();
        if (oConvertUtils.isEmpty(selections)) {
            bhzLqBasesList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            bhzLqBasesList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<BhzLqBasesPage> pageList = new ArrayList<BhzLqBasesPage>();
        for (BhzLqBases main : bhzLqBasesList) {
            BhzLqBasesPage vo = new BhzLqBasesPage();
            BeanUtils.copyProperties(main, vo);
            List<BhzLqCailiao> bhzLqCailiaoList = bhzLqCailiaoService.selectByMainId(main.getGuid());
            vo.setBhzLqCailiaoList(bhzLqCailiaoList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "沥青主表列表");
        mv.addObject(NormalExcelConstants.CLASS, BhzLqBasesPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("沥青主表数据", "导出人:" + sysUser.getRealname(), "沥青主表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
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
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<BhzLqBasesPage> list = ExcelImportUtil.importExcel(file.getInputStream(), BhzLqBasesPage.class, params);
                for (BhzLqBasesPage page : list) {
                    BhzLqBases po = new BhzLqBases();
                    BeanUtils.copyProperties(page, po);
                    bhzLqBasesService.saveMain(po, page.getBhzLqCailiaoList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
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
        return Result.OK("文件导入失败！");
    }


    @AutoLog(value = "拌合站主表-分页列表查询")
    @ApiOperation(value = "拌合站主表-分页列表查询", notes = "拌合站主表-分页列表查询")
    @GetMapping(value = "/youshibilist")
    @PermissionData(pageComponent = "bhz/lqbhz/ShengChanGuanLi")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageyoushibiList(BhzLqBases bhzLqBases,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                           HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzLqBases.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqBases.setShebeibianhao(shebei);
            } else {
                bhzLqBases.setShebeibianhao("空");
            }
        }
        QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper.orderByDesc("chuliaoshijian");
        queryWrapper.last("limit 50");
        List<BhzLqBases> pageList = bhzLqBasesService.list(queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "拌合站主表-砼拌合预警")
    @ApiOperation(value = "拌合站主表-砼拌合预警", notes = "拌合站主表-砼拌合预警")
    @GetMapping(value = "/lqbhzWarn")
    public Result<?> hntbhzWarn(String orgCode) {
        if (orgCode == "" || orgCode == null) {
            orgCode = "A";
        }
        BhzLqWarnVO bhzLqWarnVO = bhzLqBasesService.selectWranCount(orgCode);
        Map<Object, Object> map = new HashMap<>();

        if (bhzLqWarnVO.getTimeCount() == 0) {
            map.put("timeBiheLv", "-");
        } else if (bhzLqWarnVO.getTimeCount() > 0 && bhzLqWarnVO.getTimeBiheCount() == 0) {
            map.put("timeBiheLv", 0);
        } else {
            double i = (double) bhzLqWarnVO.getTimeBiheCount() / (double) bhzLqWarnVO.getTimeCount() * 100;
            map.put("timeBiheLv", (double) Math.round(i * 100) / 100);

        }

        if (bhzLqWarnVO.getPrimaryCount() == 0) {
            map.put("primaryBiheLv", "-");
        } else if (bhzLqWarnVO.getPrimaryCount() > 0 && bhzLqWarnVO.getPrimaryBiheCount() == 0) {
            map.put("primaryBiheLv", 0);
        } else {
            double i = (double) bhzLqWarnVO.getPrimaryBiheCount() / (double) bhzLqWarnVO.getPrimaryCount() * 100;
            map.put("primaryBiheLv", (double) Math.round(i * 100) / 100);
        }

        if (bhzLqWarnVO.getMiddleCount() == 0) {
            map.put("middleBihelv", "-");
        } else if (bhzLqWarnVO.getMiddleCount() > 0 && bhzLqWarnVO.getMiddleBiheCount() == 0) {
            map.put("middleBihelv", 0);
        } else {
            double i = (double) bhzLqWarnVO.getMiddleBiheCount() / (double) bhzLqWarnVO.getMiddleCount() * 100;
            map.put("middleBihelv", (double) Math.round(i * 100) / 100);
        }

        if (bhzLqWarnVO.getAdvancedCount() == 0) {
            map.put("advancedBihelv", "-");
        } else if (bhzLqWarnVO.getAdvancedCount() > 0 && bhzLqWarnVO.getAdvancedBiheCount() == 0) {
            map.put("advancedBihelv", 0);
        } else {
            double i = (double) bhzLqWarnVO.getAdvancedBiheCount() / (double) bhzLqWarnVO.getAdvancedCount() * 100;
            map.put("advancedBihelv", (double) Math.round(i * 100) / 100);
        }

        if (bhzLqWarnVO.getWenduWarnCount() == 0) {
            map.put("wenduBiheCount", "-");
        } else if (bhzLqWarnVO.getWenduWarnCount() > 0 && bhzLqWarnVO.getWenduBiheCount() == 0) {
            map.put("wenduBiheCount", 0);
        } else {
            double i = (double) bhzLqWarnVO.getWenduBiheCount() / (double) bhzLqWarnVO.getWenduWarnCount() * 100;
            map.put("wenduBiheCount", (double) Math.round(i * 100) / 100);
        }

        map.put("warnCount", bhzLqWarnVO.getWarnCount());
        map.put("primaryCount", bhzLqWarnVO.getPrimaryCount());
        map.put("middleCount", bhzLqWarnVO.getMiddleCount());
        map.put("advancedCount", bhzLqWarnVO.getAdvancedCount());
        map.put("timeCount", bhzLqWarnVO.getTimeCount());
        map.put("wenduWarnCount", bhzLqWarnVO.getWenduWarnCount());
        map.put("lqTempCount", 0);
        map.put("lqTempBihelv", "-");
        map.put("glTempCount", 0);
        map.put("glTempBihelv", "-");
        map.put("ysbCount", 0);
        map.put("ysbBihelv", "-");
        return Result.OK(map);


    }


    @AutoLog(value = "获取沥青拌合站温度曲线图数据")
    @ApiOperation(value = "沥青拌合站温度曲线图数据", notes = "沥青拌合站温度曲线图数据")
    @GetMapping(value = "/getPitchGraphData")
    public Result<?> getPitchGraphData(BhzLqBases pitchBase) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String device = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (pitchBase.getShebeibianhao() == null) {
            if (!"null".equals(device)) {
                pitchBase.setShebeibianhao(device);
            } else {
                pitchBase.setShebeibianhao("空");
            }
        }
        List<String> pitchGraphData = bhzLqBasesService.getPitchGraphData(pitchBase, 51);
        List<String> modifiedPitchGraphData = bhzLqBasesService.getPitchGraphData(pitchBase, 49);
        Map<String, List<String>> map = new HashMap<>();
        map.put("pitchGraphData", pitchGraphData);
        map.put("modifiedPitchGraphData", modifiedPitchGraphData);
        return Result.OK(map);
    }

    @AutoLog(value = "获取设备超标数据")
    @ApiOperation(value = "获取设备超标数据", notes = "获取设备超标数据")
    @GetMapping(value = "/getOverData")
    public Result<?> getOverData(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            String shebeiNo,
            HttpServletRequest req) {
        Map map = new HashMap();
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
        if (selectshebeione != null && selectshebeione.getSbtype() != null && selectshebeione.getSbtype() == 1) {
            BhzLqBases bhzLqBases = new BhzLqBases();
            bhzLqBases.setShebeibianhao(shebeiNo);
            List<Map<String, Object>> clomnData = new ArrayList<>();
            Map<String, Object> map1 = new HashMap<>(2);
            map1.put("prop", "shebeibianhao");
            map1.put("label", "设备名称");
            Map<String, Object> map2 = new HashMap<>(2);
            map2.put("prop", "chuliaoshijian");
            map2.put("label", "出料时间");
            Map<String, Object> map3 = new HashMap<>(2);
            map3.put("prop", "projectName");
            map3.put("label", "工程名称");
            Map<String, Object> map4 = new HashMap<>(2);
            map4.put("prop", "youshibi");
            map4.put("label", "油石比");
            Map<String, Object> map5 = new HashMap<>(2);
            map5.put("prop", "llysb");
            map5.put("label", "理论油石比");
            Map<String, Object> map6 = new HashMap<>(2);
            map6.put("prop", "chuliaowd");
            map6.put("label", "出料温度(/℃)");
            Map<String, Object> map7 = new HashMap<>(2);
            map7.put("prop", "zongchanliang");
            map7.put("label", "总产量(/kg)");
            Map<String, Object> map8 = new HashMap<>(2);
            map8.put("prop", "overlevelName");
            map8.put("label", "超标等级");
            Map<String, Object> map9 = new HashMap<>(2);
            map9.put("prop", "overproofStatusName");
            map9.put("label", "处理状态");
            Map<String, Object> map10 = new HashMap<>(2);
            map10.put("prop", "handlePerson");
            map10.put("label", "处置人");
            Map<String, Object> map11 = new HashMap<>(2);
            map11.put("prop", "approvalPerson");
            map11.put("label", "审核人");
            Collections.addAll(clomnData, map1, map2, map3, map4, map5, map6, map7, map8, map9, map10, map11);
            map.put("clomnData", clomnData);

            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
            if (bhzLqBases.getShebeibianhao() == null) {
                if (!"null".equals(shebei)) {
                    bhzLqBases.setShebeibianhao(shebei);
                } else {
                    bhzLqBases.setShebeibianhao("空");
                }
            }
            QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
            queryWrapper.gt("chaobiaodengji", 0);
            Page<BhzLqBases> page = new Page<BhzLqBases>(pageNo, pageSize);
            IPage<BhzLqBases> pageList = bhzLqBasesService.page(page, queryWrapper);
            List<BhzLqBases> list = pageList.getRecords();//请求中指定的所有用户数据。
            List<Object> records1 = new ArrayList<>();
            Map<Object, Object> result = new HashMap<>();
            for (BhzLqBases role : list) {
                String id = role.getGuid();//拿到唯一id
                BhzLqBasesPageRC bhzLqBasesPage = new BhzLqBasesPageRC();
                BeanUtils.copyProperties(role, bhzLqBasesPage);
                if (bhzLqBasesPage.getChaobiaodengji() != null) {
                    if (bhzLqBasesPage.getChaobiaodengji() == 0) {
                        bhzLqBasesPage.setOverlevelName("合格");
                    } else if (bhzLqBasesPage.getChaobiaodengji() == 1) {
                        bhzLqBasesPage.setOverlevelName("初级超标");
                    } else if (bhzLqBasesPage.getChaobiaodengji() == 2) {
                        bhzLqBasesPage.setOverlevelName("中级超标");
                    } else if (bhzLqBasesPage.getChaobiaodengji() == 3) {
                        bhzLqBasesPage.setOverlevelName("高级超标");
                    }
                }
                if (bhzLqBasesPage.getOverproofStatus() != null) {
                    if (bhzLqBasesPage.getOverproofStatus() == 0) {
                        bhzLqBasesPage.setOverproofStatusName("未处理");
                    } else if (bhzLqBasesPage.getOverproofStatus() == 10) {
                        bhzLqBasesPage.setOverproofStatusName("施工方已处理");
                    } else if (bhzLqBasesPage.getOverproofStatus() == 20) {
                        bhzLqBasesPage.setOverproofStatusName("监理方已处理");
                    } else if (bhzLqBasesPage.getOverproofStatus() == 30) {
                        bhzLqBasesPage.setOverproofStatusName("驳回");
                    }
                }
                List<BhzCementDetail> selectcailiaolist = bhzLqCailiaoService.selectdetail(id);
                if (selectcailiaolist.size() > 0) {
                    bhzLqBasesPage.setBhzCementDetailList(selectcailiaolist);
                }
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(role.getShebeibianhao());
                if (shebeiInfo != null) {
                    bhzLqBasesPage.setShebeibianhao(shebeiInfo.getSbname());
                }
                BhzCementOverHandler selectlist = bhzCementOverHandlerService.selectlist(id);
                if (selectlist == null) {
                    BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
                    bhzCementOverHandler.setOverproofStatus(0);
                    bhzLqBasesPage.setBhzCementOverHandler(bhzCementOverHandler);
                } else {
                    if (selectlist.getHandlePerson() != null) {
                        String name = bhzCementBaseService.selectName(selectlist.getHandlePerson());
                        selectlist.setHandlePerson(name);
                    }
                    if (selectlist.getApprovalPerson() != null) {
                        String name = bhzCementBaseService.selectName(selectlist.getApprovalPerson());
                        selectlist.setApprovalPerson(name);
                    }
                    bhzLqBasesPage.setBhzCementOverHandler(selectlist);
                }
                records1.add(bhzLqBasesPage);
            }
            result.put("current", pageList.getCurrent());
            result.put("pages", pageList.getPages());
            result.put("size", pageList.getSize());
            result.put("total", pageList.getTotal());
            result.put("records", records1);

            map.put("tableData", result);
            return Result.OK(map);
        } else if (selectshebeione != null && selectshebeione.getSbtype() != null && selectshebeione.getSbtype() == 2) {
            BhzSwBases bhzSwBases = new BhzSwBases();
            bhzSwBases.setShebeibianhao(shebeiNo);
            List<Map<String, Object>> clomnData = new ArrayList<>();
            Map<String, Object> map1 = new HashMap<>(2);
            map1.put("prop", "shebeibianhao");
            map1.put("label", "设备名称");
            Map<String, Object> map2 = new HashMap<>(2);
            map2.put("prop", "chuliaoshijian");
            map2.put("label", "出料时间");
            Map<String, Object> map3 = new HashMap<>(2);
            map3.put("prop", "zongchanliang");
            map3.put("label", "总产量");
            Map<String, Object> map4 = new HashMap<>(2);
            map4.put("prop", "formulaNo");
            map4.put("label", "配方号");
            Map<String, Object> map5 = new HashMap<>(2);
            map5.put("prop", "overlevelName");
            map5.put("label", "超标等级");
            Collections.addAll(clomnData, map1, map2, map3, map4, map5);
            map.put("clomnData", clomnData);
            String s = null;

            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
            if (bhzSwBases.getShebeibianhao() == null) {
                if (!"null".equals(shebei)) {
                    bhzSwBases.setShebeibianhao(shebei);
                } else {
                    bhzSwBases.setShebeibianhao("空");
                }
            }
            QueryWrapper<BhzSwBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzSwBases, req.getParameterMap());
            queryWrapper.gt("chaobiaodengji", 0);
            Page<BhzSwBases> page = new Page<BhzSwBases>(pageNo, pageSize);
            IPage<BhzSwBases> pageList = bhzSwBasesService.page(page, queryWrapper);
            List<BhzSwBases> list = pageList.getRecords();//请求中指定的所有用户数据。
            List<Object> records1 = new ArrayList<>();
            Map<Object, Object> result = new HashMap<>();
            for (BhzSwBases role : list) {
                String id = role.getGuid();//拿到唯一id
                BhzSwBasesPageRC bhzSwBasesPage = new BhzSwBasesPageRC();
                BeanUtils.copyProperties(role, bhzSwBasesPage);
                if (bhzSwBasesPage.getChaobiaodengji() != null) {
                    if (bhzSwBasesPage.getChaobiaodengji() == 0) {
                        bhzSwBasesPage.setOverlevelName("合格");
                    } else if (bhzSwBasesPage.getChaobiaodengji() == 1) {
                        bhzSwBasesPage.setOverlevelName("初级超标");
                    } else if (bhzSwBasesPage.getChaobiaodengji() == 2) {
                        bhzSwBasesPage.setOverlevelName("中级超标");
                    } else if (bhzSwBasesPage.getChaobiaodengji() == 3) {
                        bhzSwBasesPage.setOverlevelName("高级超标");
                    }
                }
                if (bhzSwBasesPage.getOverproofStatus() != null) {
                    if (bhzSwBasesPage.getOverproofStatus() == 0) {
                        bhzSwBasesPage.setOverproofStatusName("未处理");
                    } else if (bhzSwBasesPage.getOverproofStatus() == 10) {
                        bhzSwBasesPage.setOverproofStatusName("施工方已处理");
                    } else if (bhzSwBasesPage.getOverproofStatus() == 20) {
                        bhzSwBasesPage.setOverproofStatusName("监理方已处理");
                    } else if (bhzSwBasesPage.getOverproofStatus() == 30) {
                        bhzSwBasesPage.setOverproofStatusName("驳回");
                    }
                }
                List<BhzCementDetail> selectcailiaolist = bhzSwCailiaoService.selectdetail(id);
                if (selectcailiaolist.size() > 0) {
                    bhzSwBasesPage.setBhzCementDetailList(selectcailiaolist);
                }
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(role.getShebeibianhao());
                if (shebeiInfo != null) {
                    bhzSwBasesPage.setShebeibianhao(shebeiInfo.getSbname());
                }
                BhzCementOverHandler selectlist = bhzCementOverHandlerService.selectlist(id);
                if (selectlist == null) {
                    BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
                    bhzCementOverHandler.setOverproofStatus(0);
                    bhzSwBasesPage.setBhzCementOverHandler(bhzCementOverHandler);
                } else {
                    if (selectlist.getHandlePerson() != null) {
                        String name = bhzCementBaseService.selectName(selectlist.getHandlePerson());
                        selectlist.setHandlePerson(name);
                    }
                    if (selectlist.getApprovalPerson() != null) {
                        String name = bhzCementBaseService.selectName(selectlist.getApprovalPerson());
                        selectlist.setApprovalPerson(name);
                    }
                    bhzSwBasesPage.setBhzCementOverHandler(selectlist);
                }
                records1.add(bhzSwBasesPage);
            }
            result.put("current", pageList.getCurrent());
            result.put("pages", pageList.getPages());
            result.put("size", pageList.getSize());
            result.put("total", pageList.getTotal());
            result.put("records", records1);

            map.put("tableData", result);
            return Result.OK(map);
        } else if (selectshebeione != null && selectshebeione.getSbtype() != null && selectshebeione.getSbtype() == 0) {
            BhzCementBase bhzCementBase = new BhzCementBase();
            bhzCementBase.setShebeiNo(shebeiNo);
            List<Map<String, Object>> clomnData = new ArrayList<>();
            Map<String, Object> map1 = new HashMap<>(2);
            map1.put("prop", "shebeiNo");
            map1.put("label", "设备名称");
            Map<String, Object> map2 = new HashMap<>(2);
            map2.put("prop", "projectName");
            map2.put("label", "工程名称");
            Map<String, Object> map3 = new HashMap<>(2);
            map3.put("prop", "formulaNo");
            map3.put("label", "配方号/配比单号");
            Map<String, Object> map4 = new HashMap<>(2);
            map4.put("prop", "jobLocation");
            map4.put("label", "施工地点");
            Map<String, Object> map5 = new HashMap<>(2);
            map5.put("prop", "poureLocation");
            map5.put("label", "浇筑部位");
            Map<String, Object> map6 = new HashMap<>(2);
            map6.put("prop", "productDatetime");
            map6.put("label", "出料时间");
            Map<String, Object> map7 = new HashMap<>(2);
            map7.put("prop", "strengthRank");
            map7.put("label", "强度等级");
            Map<String, Object> map8 = new HashMap<>(2);
            map8.put("prop", "handlers");
            map8.put("label", "操作者");
            Map<String, Object> map9 = new HashMap<>(2);
            map9.put("prop", "estimateNumber");
            map9.put("label", "方量(m²)");
            Map<String, Object> map10 = new HashMap<>(2);
            map10.put("prop", "slump");
            map10.put("label", "坍落度");
            Map<String, Object> map11 = new HashMap<>(2);
            map11.put("prop", "overlevelName");
            map11.put("label", "超标等级");
            Map<String, Object> map12 = new HashMap<>(2);
            map12.put("prop", "overproofStatusName");
            map12.put("label", "处理状态");

            Collections.addAll(clomnData, map1, map2, map3, map4, map5, map6, map7, map8, map9, map10, map11, map12);
            map.put("clomnData", clomnData);
            String s = null;
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
            if (bhzCementBase.getShebeiNo() == null) {
                if (!"null".equals(shebei)) {
                    bhzCementBase.setShebeiNo(shebei);
                } else {
                    bhzCementBase.setShebeiNo("空");
                }
            }
            List<Object> records1 = new ArrayList<>();
            Map<Object, Object> result = new HashMap<>();
            bhzCementBase.setIsdel(0);
            QueryWrapper<BhzCementBase> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
            queryWrapper.gt("over_level", 0);
            queryWrapper.orderByDesc("over_level");
            Page<BhzCementBase> page = new Page<BhzCementBase>(pageNo, pageSize);
            IPage<BhzCementBase> pageList = bhzCementBaseService.page(page, queryWrapper);
            for (BhzCementBase record : pageList.getRecords()) {
                BhzCementBasePageRC bhzCementBasePage = new BhzCementBasePageRC();
                BeanUtils.copyProperties(record, bhzCementBasePage);
                if (bhzCementBasePage.getOverLevel() != null) {
                    if (bhzCementBasePage.getOverLevel() == 0) {
                        bhzCementBasePage.setOverlevelName("合格");
                    } else if (bhzCementBasePage.getOverLevel() == 1) {
                        bhzCementBasePage.setOverlevelName("初级超标");
                    } else if (bhzCementBasePage.getOverLevel() == 2) {
                        bhzCementBasePage.setOverlevelName("中级超标");
                    } else if (bhzCementBasePage.getOverLevel() == 3) {
                        bhzCementBasePage.setOverlevelName("高级超标");
                    }
                }
                if (bhzCementBasePage.getOverproofStatus() != null) {
                    if (bhzCementBasePage.getOverproofStatus() == 0) {
                        bhzCementBasePage.setOverproofStatusName("未处理");
                    } else if (bhzCementBasePage.getOverproofStatus() == 10) {
                        bhzCementBasePage.setOverproofStatusName("施工方已处理");
                    } else if (bhzCementBasePage.getOverproofStatus() == 20) {
                        bhzCementBasePage.setOverproofStatusName("监理方已处理");
                    } else if (bhzCementBasePage.getOverproofStatus() == 30) {
                        bhzCementBasePage.setOverproofStatusName("驳回");
                    }
                }
                String batchNo = record.getBatchNo();
                List<BhzCementDetailRC> selectcementlist = bhzCementDetailService.selectcementlists(batchNo);
                if (selectcementlist.size() > 0) {
                    for (BhzCementDetailRC bhzCementDetailRC : selectcementlist) {
                        if (bhzCementDetailRC.getMaterialeOverLevel() != null) {
                            if (bhzCementDetailRC.getMaterialeOverLevel() == 0) {
                                bhzCementDetailRC.setMaterialeOverLevelName("合格");
                            } else if (bhzCementDetailRC.getMaterialeOverLevel() == 1) {
                                bhzCementDetailRC.setMaterialeOverLevelName("初级超标");
                            } else if (bhzCementDetailRC.getMaterialeOverLevel() == 2) {
                                bhzCementDetailRC.setMaterialeOverLevelName("中级超标");
                            } else if (bhzCementDetailRC.getMaterialeOverLevel() == 3) {
                                bhzCementDetailRC.setMaterialeOverLevelName("高级超标");
                            }
                        }
                    }
                    bhzCementBasePage.setBhzCementDetailList(selectcementlist);
                }
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(record.getShebeiNo());
                if (shebeiInfo != null) {
                    bhzCementBasePage.setShebeiNo(shebeiInfo.getSbname());
                }
                BhzCementOverHandler selectlist = bhzCementOverHandlerService.selectlist(batchNo);
                if (selectlist == null) {
                    BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
                    bhzCementOverHandler.setOverproofStatus(0);
                    bhzCementBasePage.setBhzCementOverHandler(bhzCementOverHandler);
                } else {
                    if (selectlist.getHandlePerson() != null) {
                        String name = bhzCementBaseService.selectName(selectlist.getHandlePerson());
                        selectlist.setHandlePerson(name);
                    }
                    if (selectlist.getApprovalPerson() != null) {
                        String name = bhzCementBaseService.selectName(selectlist.getApprovalPerson());
                        selectlist.setApprovalPerson(name);
                    }
                    bhzCementBasePage.setBhzCementOverHandler(selectlist);
                }
                records1.add(bhzCementBasePage);
            }
            result.put("current", pageList.getCurrent());
            result.put("pages", pageList.getPages());
            result.put("size", pageList.getSize());
            result.put("total", pageList.getTotal());
            result.put("records", records1);

            map.put("tableData", result);
            return Result.OK(map);
        } else {
            return Result.error("该设备:" + shebeiNo + "无超标数据");
        }
    }

    @AutoLog(value = "路面看板（沥青统计）")
    @ApiOperation(value = "路面看板（沥青统计）", notes = "路面看板（沥青统计）")
    @GetMapping(value = "/getLqStatistics")
    public Result<?> getLqStatistics(
            String sys_org_code,
            HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_org_code == null) {
            sys_org_code = loginUser.getOrgCode();
        }
        Map result = new HashMap();
        List<String> shebeiList = new ArrayList<>();
        QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
        shebeiInfoQueryWrapper.likeRight("sys_org_code", sys_org_code);
        shebeiInfoQueryWrapper.eq("sbtype", 1);
        List<ShebeiInfo> shebei = shebeiInfoService.list(shebeiInfoQueryWrapper);
        if (shebei != null || shebei.size() != 0) {
            for (ShebeiInfo shebeiInfo : shebei) {
                shebeiList.add(shebeiInfo.getSbjno());
            }
        }
        if (shebeiList.size() == 0) {
            return Result.error("该组织机构：" + sys_org_code + "下没有沥青拌合站设备");
        }
        //沥青总数
        QueryWrapper<BhzLqStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("IF( sum( all_dish ), NULL, 0 ) AS all_dish ");
        queryWrapper.in("shebeibianhao", shebeiList);
        BhzLqStatistics one = iBhzLqStatisticsService.getOne(queryWrapper);
        Integer allSum = one.getAllDish();
        //沥青预警数
        QueryWrapper<BhzLqStatistics> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.select(" IF( sum( all_overproof_dish ), NULL, 0 ) AS all_overproof_dish ");
        queryWrapper2.in("shebeibianhao", shebeiList);
        BhzLqStatistics one2 = iBhzLqStatisticsService.getOne(queryWrapper2);
        Integer overSum = one2.getAllOverproofDish();
        //沥青闭合数
        QueryWrapper<BhzLqStatistics> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.select(" IF( sum( biheDish ), NULL, 0 ) AS biheDish");
        queryWrapper3.in("shebeibianhao", shebeiList);
        BhzLqStatistics one3 = iBhzLqStatisticsService.getOne(queryWrapper2);
        Integer biheDish = one3.getBiheDish();
        //沥青总量
        QueryWrapper<BhzLqStatistics> queryWrapper4 = new QueryWrapper<>();
        queryWrapper4.select("IF( sum( estimate_number ), NULL, 0 ) AS estimate_number ");
        queryWrapper4.in("shebeibianhao", shebeiList);
        BhzLqStatistics one4 = iBhzLqStatisticsService.getOne(queryWrapper4);
        Double estimateNumber = one4.getEstimateNumber();

        //预警率
        double yujinLv = 0.0;
        if (allSum == 0) {
            yujinLv = 100.0;
        } else if (allSum > 0 && overSum == 0) {
            yujinLv = (double) 0;
        } else {
            yujinLv = ((double) overSum / (double) allSum);
            BigDecimal b = new BigDecimal(yujinLv * 100);
            yujinLv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        //闭合率
        double biheLv = 0.0;
        if (overSum == 0) {
            biheLv = 100.0;
        } else if (overSum > 0 && biheDish == 0) {
            biheLv = (double) 0;
        } else {
            biheLv = ((double) biheDish / (double) overSum);
            BigDecimal b = new BigDecimal(biheLv * 100);
            biheLv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        result.put("allSum", estimateNumber);
        result.put("overSum", overSum);
        result.put("yujinLv", yujinLv);
        result.put("biheLv", biheLv);
        return Result.OK(result);
    }

    @AutoLog(value = "路面看板（预警通知）")
    @ApiOperation(value = "路面看板（预警通知）", notes = "路面看板（预警通知）")
    @GetMapping(value = "/warnContent")
    public Result<?> warnContent(
            String sys_org_code,
            HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        List<Map> resultListMap = new ArrayList<>();
        if (sys_org_code == null) {
            sys_org_code = loginUser.getOrgCode();
        }
        //水稳预警
        QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
        shebeiInfoQueryWrapper.likeRight("sys_org_code", sys_org_code);
        shebeiInfoQueryWrapper.eq("sbtype", 2);
        List<ShebeiInfo> shebei = shebeiInfoService.list(shebeiInfoQueryWrapper);
        if (shebei != null || shebei.size() != 0) {
            for (ShebeiInfo shebeiInfo : shebei) {
                Map swMap = bhzLqBasesService.selectwarnContent("bhz_sw_bases", shebeiInfo.getSbjno());
                Map map = new HashMap();
                if (swMap == null) {
                    map.put("info", shebeiInfo.getSbname() + "  暂无预警");
                } else {
                    map.put("info", shebeiInfo.getSbname() + "  " + swMap.get("chuliaoshijian") + "  " + swMap.get("over_reason"));
                }
                map.put("type", 2);
                resultListMap.add(map);
            }
        }
        //沥青预警
        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("sys_org_code", sys_org_code);
        queryWrapper.eq("sbtype", 1);
        List<ShebeiInfo> lqshebei = shebeiInfoService.list(queryWrapper);
        if (lqshebei != null || lqshebei.size() != 0) {
            for (ShebeiInfo shebeiInfo : lqshebei) {
                Map swMap = bhzLqBasesService.selectwarnContent("bhz_lq_bases", shebeiInfo.getSbjno());
                Map map = new HashMap();
                if (swMap == null) {
                    map.put("info", shebeiInfo.getSbname() + "  暂无预警");
                } else {
                    map.put("info", shebeiInfo.getSbname() + "  " + swMap.get("chuliaoshijian") + "  " + swMap.get("over_reason"));
                }
                map.put("type", 1);
                resultListMap.add(map);
            }
        }
        //拌合站
        QueryWrapper<ShebeiInfo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.likeRight("sys_org_code", sys_org_code);
        queryWrapper1.eq("sbtype", 0);
        List<ShebeiInfo> lqshebei1 = shebeiInfoService.list(queryWrapper1);
        if (lqshebei1 != null || lqshebei1.size() != 0) {
            for (ShebeiInfo shebeiInfo : lqshebei1) {
                Map swMap = bhzCementBaseService.selectwarnContent("bhz_cement_base", shebeiInfo.getSbjno());
                Map map = new HashMap();
                if (swMap == null) {
                    map.put("info", shebeiInfo.getSbname() + "  暂无预警");
                } else {
                    map.put("info", shebeiInfo.getSbname() + "  " + swMap.get("collect_datetime") + "  " + swMap.get("over_reason"));
                }
                map.put("type", 0);
                resultListMap.add(map);
            }
        }
        return Result.OK(resultListMap);
    }


    @AutoLog(value = "路面看板（项目质量状态）预警超标")
    @ApiOperation(value = "路面看板（项目质量状态）预警超标", notes = "路面看板（项目质量状态）预警超标")
    @GetMapping(value = "/ProjectQualityStatus")
    public Result<?> ProjectQualityStatus(
            String sys_org_code,
            Integer type,
            HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_org_code == null) {
            sys_org_code = loginUser.getOrgCode();
        }
        String sql = "";
        if (type == 0) {
            sql = "sum(all_overproof_dish) as count ";
        } else if (type == 1) {
            sql = "sum(estimate_number) as count ";
        } else {
            return Result.error("type值传输错误");
        }
        Map result = new HashMap();
        List<Map> swMapList = new ArrayList<>();
        List<Map> lqMapList = new ArrayList<>();
        List<Map> mapList = bhzLqBasesService.selectProjectList(sys_org_code, 4);
        if (mapList.size() == 0 || mapList == null) {
            Map swMap = new HashMap();
            Map lqMap = new HashMap();
            String depart_name = bhzCementBaseMapper.selectbysysorgcode(sys_org_code);
            Double lqStatistics = ProjectQuality(sys_org_code, "bhz_lq_statistics", 1, sql);
            Double swStatistics = ProjectQuality(sys_org_code, "bhz_sw_statistics", 2, sql);
            if (lqStatistics == null) lqStatistics = (double) 0;
            if (swStatistics == null) swStatistics = (double) 0;
            swMap.put("departName", depart_name);
            swMap.put("count", swStatistics);
            lqMap.put("departName", depart_name);
            lqMap.put("count", lqStatistics);
            swMapList.add(swMap);
            lqMapList.add(lqMap);
        } else {
            for (Map map : mapList) {
                String org_code = map.get("org_code").toString();
                String depart_name = map.get("depart_name").toString();
                Map swMap = new HashMap();
                Map lqMap = new HashMap();
                Double lqStatistics = ProjectQuality(org_code, "bhz_lq_statistics", 1, sql);
                Double swStatistics = ProjectQuality(org_code, "bhz_sw_statistics", 2, sql);
                if (lqStatistics == null) lqStatistics = (double) 0;
                if (swStatistics == null) swStatistics = (double) 0;
                swMap.put("departName", depart_name);
                swMap.put("count", swStatistics);
                lqMap.put("departName", depart_name);
                lqMap.put("count", lqStatistics);
                swMapList.add(swMap);
                lqMapList.add(lqMap);

            }
        }
        result.put("sw", swMapList);
        result.put("lq", lqMapList);
        return Result.OK(result);
    }

    public Double ProjectQuality(String sys_org_code, String tableName, Integer SbType, String sql) {
        List<String> shebeiList = new ArrayList<>();
        QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
        shebeiInfoQueryWrapper.likeRight("sys_org_code", sys_org_code);
        shebeiInfoQueryWrapper.eq("sbtype", SbType);
        List<ShebeiInfo> shebei = shebeiInfoService.list(shebeiInfoQueryWrapper);
        if (shebei != null || shebei.size() != 0) {
            for (ShebeiInfo shebeiInfo : shebei) {
                shebeiList.add(shebeiInfo.getSbjno());
            }
        }
        if (shebeiList.size() > 0) {
            return bhzLqBasesService.selectOverCount(shebeiList, tableName, sql);
        }
        double v = 0;
        return v;
    }


    @AutoLog(value = "路面看板（项目质量状态）预警超标")
    @ApiOperation(value = "路面看板（项目质量状态）预警超标", notes = "路面看板（项目质量状态）预警超标")
    @GetMapping(value = "/ProjectQuali")
    public Result<?> ProjectQuali(
            String sys_org_code,
            HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (sys_org_code == null) {
            sys_org_code = loginUser.getOrgCode();
        }
        String sql = "sum(estimate_number) as count ";
        Map result = new HashMap();
        List<Map> swMapList = new ArrayList<>();
        List<Map> lqMapList = new ArrayList<>();
        List<Map> mapList = bhzLqBasesService.selectProjectList(sys_org_code, 4);
        if (mapList.size() == 0 || mapList == null) {
            Map swMap = new HashMap();
            Map lqMap = new HashMap();
            String depart_name = bhzCementBaseMapper.selectbysysorgcode(sys_org_code);
            Double lqStatistics = ProjectQuality(sys_org_code, "bhz_lq_statistics", 1, sql);
            Double swStatistics = ProjectQuality(sys_org_code, "bhz_sw_statistics", 2, sql);
            Map<Object, Object> map1 = ProjectQuali(sys_org_code, 51);
            Map<Object, Object> map2 = ProjectQuali(sys_org_code, 52);//沥青
            if (lqStatistics == null) lqStatistics = (double) 0;
            if (swStatistics == null) swStatistics = (double) 0;
            swMap.put("departName", depart_name);
            swMap.put("count", swStatistics);
            swMap.put("swlqcs", map1.get("swlqcs"));
            swMap.put("swlqysccs", map1.get("swlqysccs"));
            swMap.put("yjs", 0);
            lqMap.put("departName", depart_name);
            lqMap.put("count", lqStatistics);
            lqMap.put("swlqcs", map2.get("swlqcs"));
            lqMap.put("swlqysccs", map2.get("swlqysccs"));
            lqMap.put("yjs", 0);
            swMapList.add(swMap);
            lqMapList.add(lqMap);
        } else {
            for (Map map : mapList) {
                String org_code = map.get("org_code").toString();
                String depart_name = map.get("depart_name").toString();
                Map swMap = new HashMap();
                Map lqMap = new HashMap();
                Double lqStatistics = ProjectQuality(org_code, "bhz_lq_statistics", 1, sql);
                Double swStatistics = ProjectQuality(org_code, "bhz_sw_statistics", 2, sql);
                Map<Object, Object> map1 = ProjectQuali(org_code, 51);
                Map<Object, Object> map2 = ProjectQuali(org_code, 52);//沥青
                if (lqStatistics == null) lqStatistics = (double) 0;
                if (swStatistics == null) swStatistics = (double) 0;
                swMap.put("departName", depart_name);
                swMap.put("count", swStatistics);
                swMap.put("swlqcs", map1.get("swlqcs"));
                swMap.put("swlqysccs", map1.get("swlqysccs"));
                swMap.put("yjs", 0);
                lqMap.put("departName", depart_name);
                lqMap.put("count", lqStatistics);
                lqMap.put("swlqcs", map2.get("swlqcs"));
                lqMap.put("swlqysccs", map2.get("swlqysccs"));
                lqMap.put("yjs", 0);
                swMapList.add(swMap);
                lqMapList.add(lqMap);

            }
        }
        result.put("sw", swMapList);
        result.put("lq", lqMapList);
        return Result.OK(result);
    }

    public Map<Object, Object> ProjectQuali(String sys_org_code, Integer SbType) {
        List<String> list = hcTruckService.selectbyorgcode(sys_org_code, SbType);
        Map<Object, Object> map = new HashMap<>();
        map.put("swlqcs", list.size());
        if (list.size() > 0) {
            QueryWrapper<HcTransportrecords> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("truckId", list);
            List<HcTransportrecords> list1 = transportrecordsService.list(queryWrapper);
            map.put("swlqysccs", list1.size());
        } else {
            map.put("swlqysccs", 0);
        }
        return map;
    }

    @RequestMapping(value = "/getYSB", method = RequestMethod.POST)
    public Result<?> getYSB(HttpServletRequest request) {

        List bhzLqBasesYSBList = new ArrayList();
        QueryWrapper<BhzLqBases> lqBasesQueryWrapper = new QueryWrapper<>();
        lqBasesQueryWrapper.select("CASE formula_no\n" +
                        "        WHEN '33' THEN 'SMA-13'\n" +
                        "        WHEN '36' THEN 'SMA-16'\n" +
                        "        WHEN '34' THEN 'SUP-20'\n" +
                        "        WHEN '75' THEN 'SUP-25'\n" +
                        "    END AS formulaNo,DATE(chuliaoshijian) AS chuliaoshijian", "ROUND(AVG(youshibi), 2) AS youshibi",
                "ROUND(AVG(guliaowd), 2) AS guliaowd", "ROUND(AVG(liqingwd), 2) AS liqingwd",
                "ROUND(AVG(chuliaowd), 2) AS chuliaowd")
                .isNotNull("youshibi")
                .in("formula_no", Arrays.asList("33", "34", "36", "75"))
                .ge("CAST(guliaowd AS DECIMAL(10, 2))", "175")
                .le("CAST(guliaowd AS DECIMAL(10, 2))", "205")
                .ge("CAST(liqingwd AS DECIMAL(10, 2))", "165")
                .le("CAST(liqingwd AS DECIMAL(10, 2))", "175")
                .eq("shebeibianhao", "ydlm1blq_01")
                .ge("chuliaoshijian", "2023-05-01")
                .groupBy("DATE(chuliaoshijian), formula_no")
                .orderByDesc("DATE(chuliaoshijian)");

        List<BhzLqBases> list = bhzLqBasesService.list(lqBasesQueryWrapper);
        for (BhzLqBases bhzLqBases : list) {
            BhzLqBasesYSB bhzLqBasesYSB = new BhzLqBasesYSB();
            bhzLqBasesYSB.setChuliaoshijian(bhzLqBases.getChuliaoshijian());
            bhzLqBasesYSB.setYoushibi(bhzLqBases.getYoushibi());
            String chuliaowd = bhzLqBases.getChuliaowd();
            double v = Double.parseDouble(chuliaowd);
            if (v > 185 || v < 170) {
                v = 175;
            }
            bhzLqBasesYSB.setChuliaowd(String.valueOf(v));
            bhzLqBasesYSB.setLiqingwd(bhzLqBases.getLiqingwd());
            bhzLqBasesYSB.setGuliaowd(bhzLqBases.getGuliaowd());
            bhzLqBasesYSB.setFormulaName(bhzLqBases.getFormulaNo());
            bhzLqBasesYSBList.add(bhzLqBasesYSB);
        }

        return Result.OK("请求成功！", bhzLqBasesYSBList);
    }

    @RequestMapping(value = "/getDay", method = RequestMethod.POST)
    public Result<?> getDay() {
        QueryWrapper<BhzLqBases> bhzLqBasesQueryWrapper = new QueryWrapper<>();
        bhzLqBasesQueryWrapper.select("DATE(chuliaoshijian) AS chuliaoshijian")
                .eq("shebeibianhao", "ydlm1blq_01")
                .in("formula_no", Arrays.asList("33", "34", "36", "75"))
                .groupBy("DATE(chuliaoshijian)")
                .orderByDesc("chuliaoshijian");
        List<BhzLqBases> list = bhzLqBasesService.list(bhzLqBasesQueryWrapper);

        List sendList = new ArrayList();
        for (BhzLqBases bhzLqBases : list) {
            Map sendMap = new HashMap();
            sendMap.put("date", bhzLqBases.getChuliaoshijian());
            sendList.add(sendMap);
        }

        return Result.OK("请求成功！", sendList);
    }

    @RequestMapping(value = "/getDosage", method = RequestMethod.POST)
    public Result<?> getDosage(HttpServletRequest request) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String date = request.getParameter("date");
        QueryWrapper<BhzLqBases> bhzLqBasesQueryWrapper = new QueryWrapper<>();
        bhzLqBasesQueryWrapper.select("formula_no")
                .eq("shebeibianhao", "ydlm1blq_01")
                .in("formula_no", Arrays.asList("33", "34", "36", "75"))
                .eq("DATE(chuliaoshijian)", date)
                .groupBy("formula_no");
        List<BhzLqBases> bhzLqBasesList = bhzLqBasesService.list(bhzLqBasesQueryWrapper);
        List sendList = new ArrayList();

        for (BhzLqBases bhzLqBases : bhzLqBasesList) {
            Double phb = 0.0;
            Double llphb = 0.0;

            Map map = new HashMap();
            String formulaNo = bhzLqBases.getFormulaNo();
            //配比（不包含粉料）
            List<BhzLqBasesDayYL> bhzLqBasesDayYLList = bhzLqBasesService.getDosage(formulaNo, date);
            String formulaName = bhzLqBasesDayYLList.get(0).getFormulaName();
            for (BhzLqBasesDayYL bhzLqBasesDayYL : bhzLqBasesDayYLList) {
                QueryWrapper<BhzLqRecipe> lqRecipeQueryWrapper = new QueryWrapper<>();
                lqRecipeQueryWrapper.eq("phbid", formulaNo)
                        .eq("shebeibianhao", "ydlm1blq_01");
                BhzLqRecipe bhzLqRecipe = lqRecipeService.getOne(lqRecipeQueryWrapper);
                String zhuziid = bhzLqRecipe.getZhuziid();

                QueryWrapper<BhzLqPhbZibiao> zibiaoQueryWrapper = new QueryWrapper<>();
                String materialName = bhzLqBasesDayYL.getMaterialName();
                zibiaoQueryWrapper.eq("zhuziid", zhuziid)
                        .eq("cailiaono", materialName);
                BhzLqPhbZibiao lqPhbZibiao = bhzLqPhbZibiaoService.getOne(zibiaoQueryWrapper);
                if ("34".equals(formulaNo)) {
                    if (date.compareTo("2023-07-26") < 0) {
                        switch (materialName) {
                            case "石料1":
                                bhzLqBasesDayYL.setGglx("石灰岩0-3mm");
                                bhzLqBasesDayYL.setPhb("22");
                                break;

                            case "石料2":
                                bhzLqBasesDayYL.setGglx("石灰岩3-6mm");
                                bhzLqBasesDayYL.setPhb("11.52");
                                break;

                            case "石料3":
                                bhzLqBasesDayYL.setGglx("石灰岩6-11mm");
                                bhzLqBasesDayYL.setPhb("14.37");
                                break;

                            case "石料4":
                                bhzLqBasesDayYL.setGglx("石灰岩11-17mm");
                                bhzLqBasesDayYL.setPhb("23");
                                break;

                            case "石料5":
                                bhzLqBasesDayYL.setGglx("石灰岩17-23mm");
                                bhzLqBasesDayYL.setPhb("21.07");
                                break;

                            case "粉料1":
                                bhzLqBasesDayYL.setGglx("矿粉");
                                bhzLqBasesDayYL.setPhb("3.83");
                                break;

                            case "粉料2":
                                bhzLqBasesDayYL.setGglx("矿粉");
                                bhzLqBasesDayYL.setPhb("3.83");
                                break;

                            case "沥青":
                                bhzLqBasesDayYL.setGglx("SBS改性沥青");
                                bhzLqBasesDayYL.setPhb("4.21");
                                break;
                        }
                    } else if (date.compareTo("2023-07-26") >= 0 && date.compareTo("2023-08-11") < 0) {
                        switch (materialName) {
                            case "石料1":
                                bhzLqBasesDayYL.setGglx("石灰岩0-3mm");
                                bhzLqBasesDayYL.setPhb("21.07");
                                break;

                            case "石料2":
                                bhzLqBasesDayYL.setGglx("石灰岩3-6mm");
                                bhzLqBasesDayYL.setPhb("9.58");
                                break;

                            case "石料3":
                                bhzLqBasesDayYL.setGglx("石灰岩6-11mm");
                                bhzLqBasesDayYL.setPhb("16.28");
                                break;

                            case "石料4":
                                bhzLqBasesDayYL.setGglx("石灰岩11-17mm");
                                bhzLqBasesDayYL.setPhb("23.95");
                                break;

                            case "石料5":
                                bhzLqBasesDayYL.setGglx("石灰岩17-23mm");
                                bhzLqBasesDayYL.setPhb("22.03");
                                break;

                            case "粉料1":
                                bhzLqBasesDayYL.setGglx("矿粉");
                                bhzLqBasesDayYL.setPhb("2.87");
                                break;

                            case "粉料2":
                                bhzLqBasesDayYL.setGglx("矿粉");
                                bhzLqBasesDayYL.setPhb("2.87");
                                break;

                            case "沥青":
                                bhzLqBasesDayYL.setGglx("SBS改性沥青");
                                bhzLqBasesDayYL.setPhb("4.21");
                                break;
                        }
                    } else if (date.compareTo("2023-08-11") >= 0) {
                        bhzLqBasesDayYL.setGglx(lqPhbZibiao.getGgxh());
                        bhzLqBasesDayYL.setPhb(lqPhbZibiao.getTianjiaji());
                    }
                } else {
                    bhzLqBasesDayYL.setGglx(lqPhbZibiao.getGgxh());
                    bhzLqBasesDayYL.setPhb(lqPhbZibiao.getTianjiaji());
                }

                //实际配比
                String actualScale = bhzLqBasesDayYL.getActualScale();
                //理论配比
                String llphb1 = bhzLqBasesDayYL.getPhb();
                phb += Double.parseDouble(actualScale);
                llphb += Double.parseDouble(llphb1);
            }
            phb = 100 - phb;
            llphb = 100 - llphb;

            if (phb - llphb >= 1) {
                phb = llphb + 0.99;
            }
            if (phb - llphb <= -1) {
                phb = llphb - 0.99;
            }
            BhzLqBasesDayYL bhzLqBasesDayYL = new BhzLqBasesDayYL();
            bhzLqBasesDayYL.setMaterialName("粉料1");
            bhzLqBasesDayYL.setGglx("矿粉");
            bhzLqBasesDayYL.setChuliaoshijian(date);
            bhzLqBasesDayYL.setFormulaNo(formulaNo);
            bhzLqBasesDayYL.setActualScale(decimalFormat.format(phb));
            bhzLqBasesDayYL.setPhb(decimalFormat.format(llphb));

            bhzLqBasesDayYLList.add(bhzLqBasesDayYL);

            map.put("formulaName", formulaName);
            map.put("details", bhzLqBasesDayYLList);

            sendList.add(map);
        }

        return Result.OK("请求成功！", sendList);
    }

    /**
     * 沥青拌合站首页超标等级盘数/未处置/已处置盘数
     *
     * @param
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "沥青拌合站首页超标等级盘数/未处置/已处置盘数")
    @ApiOperation(value = "沥青拌合站首页超标等级盘数/未处置/已处置盘数", notes = "沥青拌合站首页超标等级盘数/未处置/已处置盘数")
    @GetMapping(value = "/list999")
    public Result<?> queryPageList999(BhzLqBases bhzLqBases, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzLqBases.setShebeibianhao(shebei);
        } else {
            bhzLqBases.setShebeibianhao("空");
        }

//        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementBase, req.getParameterMap());
//        queryWrapper.select("ifnull(sum(primary_dish),0) as primary_dish,ifnull(sum(middle_dish),0) as middle_dish," +
//                "ifnull(sum(advanced_dish),0) as advanced_dish,ifnull(sum(all_dish),0) as all_dish," +
//                "ifnull(sum(all_handle_dish),0) as all_handle_dish,ifnull(sum(all_overproof_dish),0) as all_overproof_dish," +
//                "ifnull(sum(primary_handle),0) as primary_handle,ifnull(sum(middle_handle),0) as middle_handle," +
//                "ifnull(sum(advanced_handle),0) as advanced_handle");
//        List<BhzCementStatistics> bhzLqStatisticsList = bhzCementStatisticsService.list(queryWrapper);


        String[] split = shebei.split(",");
        List<String> List = Arrays.asList(split);
        String shebeilist = null;
        for (int i = 0; i < List.size(); i++) {
            if (i == List.size() - 1) {
                shebeilist = shebeilist + "," + "'" + List.get(i) + "'";
            } else if (i == 0) {
                shebeilist = "'" + List.get(i) + "'";
            } else {
                shebeilist = shebeilist + "," + "'" + List.get(i) + "'";
            }
        }
        List<BhzCementBaseRC> bhzCementBaseList = bhzLqBasesService.selectTongjiData(shebeilist);
        double count = 0.0;//总盘数
        double primaryCount = 0.0;//初级超标盘数
        double middleCount = 0.0;//中级超标盘数
        double advanceCount = 0.0;//高级超标盘数
        double qualifiedCount = 0.0;//合格盘数
        double overProofCount = 0.0;//超标总盘数

        double handleCount = 0.0;//超标已处置盘数
        double untreatedCount = 0.0;//超标未处置盘数
        double handleCent = 0.0;//处置率
        double untreatedCent = 0.0;//未处置率

        double primaryCent = 0.0;//初级超标率
        double middleCent = 0.0;//中级超标率
        double advancedCent = 0.0;//高级超标率
        double qualifiedCent = 0.0;//合格率

        double primaryHandle = 0.0;//初级超标处置盘数
        double middleHandle = 0.0;//中级超标处置盘数
        double advanceHandle = 0.0;//高级超标处置盘数
        double primaryHandleCent = 0.0;//初级超标处置率
        double middleHandleCent = 0.0;//中级超标处置率
        double advanceHandleCent = 0.0;//高级超标处置率
//        double primaryNotHandle = 0.0;//初级超标未处置
//        double middleNotHandle = 0.0;//中级超标未处置
//        double advanceNotHandle = 0.0;//高级超标未处置

        for (BhzCementBaseRC bhzCS : bhzCementBaseList) {
            count = bhzCS.getAllDish();
            primaryCount = bhzCS.getPrimaryCount();
            middleCount = bhzCS.getMiddleCount();
            advanceCount = bhzCS.getAdvancedCount();
            double v = primaryCount + middleCount + advanceCount;
            overProofCount = v;
            qualifiedCount = count - overProofCount;
            handleCount = bhzCS.getAllHandleDish();
            untreatedCount = bhzCS.getAllNotHandleDish();
//            primaryNotHandle = bhzCS.getPrimaryNotHandle();
//            middleNotHandle = bhzCS.getMiddleNotHandle();
//            advanceNotHandle = bhzCS.getAdvanceNotHandle();
            if (count != 0) {
                primaryCent = Double.parseDouble(String.format("%.2f", (primaryCount / count) * 100));
                middleCent = Double.parseDouble(String.format("%.2f", (middleCount / count) * 100));
                advancedCent = Double.parseDouble(String.format("%.2f", (advanceCount / count) * 100));
                qualifiedCent = Double.parseDouble(String.format("%.2f", (qualifiedCount / count) * 100));
            }
//            primaryHandle = bhzCS.getPrimaryHandle();
//            middleHandle = bhzCS.getMiddleHandle();
//            advanceHandle = bhzCS.getAdvancedHandle();
//            if (overProofCount != 0) {
//                primaryHandleCent = Double.parseDouble(String.format("%.2f", (primaryHandle / overProofCount) * 100));
//                middleHandleCent = Double.parseDouble(String.format("%.2f", (middleHandle / overProofCount) * 100));
//                advanceHandleCent = Double.parseDouble(String.format("%.2f", (advanceHandle / overProofCount) * 100));
//                handleCent = Double.parseDouble(String.format("%.2f", (handleCount / overProofCount) * 100));
//                untreatedCent = Double.parseDouble(String.format("%.2f", (untreatedCount / overProofCount) * 100));
//
//            }
        }
        QueryWrapper<BhzLqBases> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        queryWrapper1.select("count(1) AS id ,chaobiaodengji ");
        queryWrapper1.gt("overproof_status", 0);
        queryWrapper1.gt("chaobiaodengji", 0);
        queryWrapper1.groupBy("chaobiaodengji");
        List<BhzLqBases> list = bhzLqBasesService.list(queryWrapper1);
        for (BhzLqBases bases : list) {
            if (bases.getChaobiaodengji() == 1) {
                primaryHandle = Double.valueOf(bases.getId());
            }
            if (bases.getChaobiaodengji() == 2) {
                middleHandle = Double.valueOf(bases.getId());
            }
            if (bases.getChaobiaodengji() == 3) {
                advanceHandle = Double.valueOf(bases.getId());
            }
        }
        Map map = new HashMap();
        map.put("lqprimary", primaryCount);
        map.put("lqmiddle", middleCount);
        map.put("lqadvance", advanceCount);
        map.put("lqhege", qualifiedCount);
        map.put("lqhandle", handleCount);
        map.put("lqnohandle", untreatedCount);
        map.put("primarylv", primaryCent);
        map.put("middlelv", middleCent);
        map.put("advancedlv", advancedCent);
        map.put("hegelv", qualifiedCent);
        map.put("primaryHandle", primaryHandle);
        map.put("middleHandle", middleHandle);
        map.put("advanceHandle", advanceHandle);
        map.put("primaryHandlelv", primaryHandleCent);
        map.put("middleHandlelv", middleHandleCent);
        map.put("advanceHandlelv", advanceHandleCent);
        map.put("handlelv", handleCent);
        map.put("nohandlelv", untreatedCent);
        return Result.OK(map);
    }

    @AutoLog(value = "沥青拌合站主表-原材料用量导出")
    @ApiOperation(value = "沥青拌合站主表-原材料用量导出", notes = "沥青拌合站主表-原材料用量导出")
    @GetMapping(value = "/listUseInfo")
    public Result<?> listUseInfo(BhzLqBases bhzLqBases,//设备和时间
                                 @RequestParam(value = "shebeibianhao", required = false) String shebeino,
                                 @RequestParam(value = "chuliaoshijian_begin", required = false) String start,
                                 @RequestParam(value = "chuliaoshijian_end", required = false) String end,
                                 HttpServletRequest req) {
        List data = new ArrayList<>();
        if (ObjectUtil.isEmpty(end)) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            end = simpleDateFormat.format(date);
        }
        String[] split = shebeino.split(",");
        String sbjno = "";
        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                sbjno = "'" + split[i] + "'";
            } else if (i == split.length - 1) {
                sbjno = sbjno + "," + "'" + split[i] + "'";
            } else {
                sbjno = sbjno + "," + "'" + split[i] + "'";
            }
        }

        Integer id = 1;
        List<BhzLqTongJi> bhzLqTongJis = bhzLqBasesService.selectCailiaoUse(sbjno, start, end);
        for (int i = 0; i < bhzLqTongJis.size(); i++) {
            BhzLqTongJi tongJiOne = new BhzLqTongJi();
            tongJiOne.setId(id);
            tongJiOne.setShebeiName(bhzLqTongJis.get(i).getShebeiName());
            tongJiOne.setCailiaoName(bhzLqTongJis.get(i).getCailiaoName());
            tongJiOne.setLilunUse(bhzLqTongJis.get(i).getLilunUse());
            tongJiOne.setRealUse(bhzLqTongJis.get(i).getRealUse());
            data.add(tongJiOne);
            id++;
        }
        return Result.OKs(data);
    }

    @AutoLog(value = "沥青拌合站主表-原材料用量导出")
    @ApiOperation(value = "沥青拌合站主表-原材料用量导出", notes = "沥青拌合站主表-原材料用量导出")
    @GetMapping(value = "/listUseInfo1")
    public Result<?> listUseInfo1(BhzLqBases bhzLqBases,//设备和时间
                                  @RequestParam(value = "chuliaoshijian_begin", required = false) String start,
                                  @RequestParam(value = "chuliaoshijian_end", required = false) String end,
                                  HttpServletRequest req) {
        List data = new ArrayList<>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (ObjectUtil.isEmpty(bhzLqBases.getShebeibianhao())) {
            if (!"null".equals(shebei)) {
                bhzLqBases.setShebeibianhao(shebei);
            } else {
                bhzLqBases.setShebeibianhao("空");
            }
        }
        if (ObjectUtil.isEmpty(end)) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            end = simpleDateFormat.format(date);
        }
        String[] split = bhzLqBases.getShebeibianhao().split(",");
        String sbjno = "";
        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                sbjno = "'" + split[i] + "'";
            } else if (i == split.length - 1) {
                sbjno = sbjno + "," + "'" + split[i] + "'";
            } else {
                sbjno = sbjno + "," + "'" + split[i] + "'";
            }
        }

        Integer id = 1;
        List<BhzLqTongJi> bhzLqTongJis = bhzLqBasesService.selectCailiaoUse(sbjno, start, end);
        for (int i = 0; i < bhzLqTongJis.size(); i++) {
            BhzLqTongJi tongJiOne = new BhzLqTongJi();
            tongJiOne.setId(id);
            tongJiOne.setShebeiName(bhzLqTongJis.get(i).getShebeiName());
            tongJiOne.setCailiaoName(bhzLqTongJis.get(i).getCailiaoName());
            tongJiOne.setLilunUse(bhzLqTongJis.get(i).getLilunUse());
            tongJiOne.setRealUse(bhzLqTongJis.get(i).getRealUse());
            data.add(tongJiOne);
            id++;
        }
        return Result.OKs(data);
    }

    @AutoLog(value = "沥青拌合站主表-原材料用量导出")
    @ApiOperation(value = "沥青拌合站主表-原材料用量导出", notes = "沥青拌合站主表-原材料用量导出")
    @GetMapping(value = "/listUseInfoDate")
    public Result<?> listUseInfoDate(@RequestParam(value = "chuliaoshijian_begin", required = false) String start,
                                     @RequestParam(value = "chuliaoshijian_end", required = false) String end) {
        List data = new ArrayList<>();
        if (ObjectUtil.isEmpty(end) || "undefined".equals(end)) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            end = simpleDateFormat.format(date);
        }
        BhzLqTongJi one = new BhzLqTongJi();
        one.setCailiaoName(start + "~" + end);
        data.add(one);
        return Result.OKs(data);
    }

    @AutoLog(value = "沥青拌合站主表-原材料用量导出")
    @ApiOperation(value = "沥青拌合站主表-原材料用量导出API", notes = "沥青拌合站主表-原材料用量导出API")
    @GetMapping(value = "/listDateDC")
    public Result<?> listDateDC(HttpServletRequest request) {

        String chuliaoshijian_begin = request.getParameter("chuliaoshijian_begin");
        String chuliaoshijian_end = request.getParameter("chuliaoshijian_end");
        String projectName = request.getParameter("projectName");
        String hunheliaoid = request.getParameter("hunheliaoid");
        String shebeibianhao = request.getParameter("shebeibianhao");

        List<BhzLqCLDC> bhzLqCLDCList = bhzLqBasesService.selectdcdata(shebeibianhao, chuliaoshijian_begin, chuliaoshijian_end, projectName, hunheliaoid);
        List data = new ArrayList<>();
        for (BhzLqCLDC bhzLqCLDC : bhzLqCLDCList) {
            data.add(bhzLqCLDC);
        }
        return Result.OKs(data);
    }

    @AutoLog(value = "拌合站统计表-原材料消耗查询")
    @ApiOperation(value = "拌合站统计表-原材料消耗查询导入api", notes = "拌合站统计表-原材料消耗查询导入api")
    @GetMapping(value = "/exportApi")
    public Result<?> exportApi(HttpServletRequest request) {
        List data = new ArrayList<>();
        String chuliaoshijian_begin = request.getParameter("chuliaoshijian_begin");
        String chuliaoshijian_end = request.getParameter("chuliaoshijian_end");
        String projectName = request.getParameter("projectName");
        String hunheliaoid = request.getParameter("hunheliaoid");
        String shebeibianhao = request.getParameter("shebeibianhao");

        QueryWrapper<BhzLqBases> bhzLqBasesQueryWrapper = new QueryWrapper<>();
        bhzLqBasesQueryWrapper.select("hunheliaoid, project_name, shebeibianhao, sum(zongchanliang) zongchanliang");
        if (StringUtil.isNotEmpty(chuliaoshijian_begin)) {
            bhzLqBasesQueryWrapper.ge("chuliaoshijian", chuliaoshijian_begin);
        }
        if (StringUtil.isNotEmpty(chuliaoshijian_end)) {
            bhzLqBasesQueryWrapper.le("chuliaoshijian", chuliaoshijian_end);
        }
        if (StringUtil.isNotEmpty(projectName)) {
            bhzLqBasesQueryWrapper.eq("project_name", projectName);
        }
        if (StringUtil.isNotEmpty(hunheliaoid)) {
            bhzLqBasesQueryWrapper.eq("hunheliaoid", hunheliaoid);
        }
        if (StringUtil.isNotEmpty(shebeibianhao)) {
            bhzLqBasesQueryWrapper.eq("shebeibianhao", shebeibianhao);
        }
        bhzLqBasesQueryWrapper.last("GROUP BY hunheliaoid, project_name, shebeibianhao");

        List<BhzLqBases> bhzLqBasesList = bhzLqBasesService.list(bhzLqBasesQueryWrapper);

        for (BhzLqBases bhzLqBases : bhzLqBasesList) {

            BhzLqdaochu bhzLqdaochu = new BhzLqdaochu();
            bhzLqdaochu.setShebeiName(bhzLqBases.getShebeibianhao());
            bhzLqdaochu.setProjectName(bhzLqBases.getProjectName());
            bhzLqdaochu.setHunheliaoid(bhzLqBases.getHunheliaoid());
            bhzLqdaochu.setZongchanliang(bhzLqBases.getZongchanliang());

            String hunheliaoid1 = bhzLqBases.getHunheliaoid();
            String projectName1 = bhzLqBases.getProjectName();
            String shebeibianhao1 = bhzLqBases.getShebeibianhao();

            List<BhzLqCailiao> bhzLqCailiaos = bhzLqCailiaoService.getcailiaoList(chuliaoshijian_begin, chuliaoshijian_end, hunheliaoid1, projectName1, shebeibianhao1);
            for (BhzLqCailiao bhzLqCailiao : bhzLqCailiaos) {
                String cailiaoming = bhzLqCailiao.getCailiaoming();
                Double shijiyongliang = bhzLqCailiao.getShijiyongliang();
                Double theoryNumber = bhzLqCailiao.getTheoryNumber();

                bhzLqdaochu.setCailiaoNamelq("沥青");
                bhzLqdaochu.setCailiaoNames1("石料1");
                bhzLqdaochu.setCailiaoNames2("石料2");
                bhzLqdaochu.setCailiaoNames3("石料3");
                bhzLqdaochu.setCailiaoNames4("石料4");
                bhzLqdaochu.setCailiaoNames5("石料5");
                bhzLqdaochu.setCailiaoNames6("石料6");
                bhzLqdaochu.setCailiaoNamef1("粉料1");
                bhzLqdaochu.setCailiaoNamef2("粉料2");
                bhzLqdaochu.setCailiaoNamef3("粉料3");
                bhzLqdaochu.setCailiaoNamecl("粗料");
                bhzLqdaochu.setCailiaoNamexl("细料");
                if ("沥青".equals(cailiaoming)) {
                    bhzLqdaochu.setReallq(shijiyongliang);
                    bhzLqdaochu.setLilunlq(theoryNumber);
                }

                if ("石料1".equals(cailiaoming)) {
                    bhzLqdaochu.setReals1(shijiyongliang);
                    bhzLqdaochu.setLiluns1(theoryNumber);
                }

                if ("石料2".equals(cailiaoming)) {
                    bhzLqdaochu.setReals2(shijiyongliang);
                    bhzLqdaochu.setLiluns2(theoryNumber);
                }

                if ("石料3".equals(cailiaoming)) {
                    bhzLqdaochu.setReals3(shijiyongliang);
                    bhzLqdaochu.setLiluns3(theoryNumber);
                }

                if ("石料4".equals(cailiaoming)) {
                    bhzLqdaochu.setReals4(shijiyongliang);
                    bhzLqdaochu.setLiluns4(theoryNumber);
                }

                if ("石料5".equals(cailiaoming)) {
                    bhzLqdaochu.setReals5(shijiyongliang);
                    bhzLqdaochu.setLiluns5(theoryNumber);
                }

                if ("石料6".equals(cailiaoming)) {
                    bhzLqdaochu.setReals6(shijiyongliang);
                    bhzLqdaochu.setLiluns6(theoryNumber);
                }

                if ("粉料1".equals(cailiaoming)) {
                    bhzLqdaochu.setRealf1(shijiyongliang);
                    bhzLqdaochu.setLilunf1(theoryNumber);
                }

                if ("粉料2".equals(cailiaoming)) {
                    bhzLqdaochu.setRealf2(shijiyongliang);
                    bhzLqdaochu.setLilunf2(theoryNumber);
                }

                if ("粉料3".equals(cailiaoming)) {
                    bhzLqdaochu.setRealf3(shijiyongliang);
                    bhzLqdaochu.setLilunf3(theoryNumber);
                }

                if ("粗料".equals(cailiaoming)) {
                    bhzLqdaochu.setRealcl(shijiyongliang);
                    bhzLqdaochu.setLiluncl(theoryNumber);
                }

                if ("细料".equals(cailiaoming)) {
                    bhzLqdaochu.setRealxl(shijiyongliang);
                    bhzLqdaochu.setLilunxl(theoryNumber);
                }
            }
            data.add(bhzLqdaochu);
        }

        return Result.OKs(data);
    }
}
