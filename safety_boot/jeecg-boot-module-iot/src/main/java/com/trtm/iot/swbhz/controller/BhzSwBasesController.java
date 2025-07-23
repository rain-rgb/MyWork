package com.trtm.iot.swbhz.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhzStatistics.entity.BhzLqStatistics;
import com.trtm.iot.swbhz.entity.BhzSwWarnVO;
import com.trtm.iot.swbhzStatistics.entity.BhzSwStatistics;
import com.trtm.iot.swbhzStatistics.service.IBhzSwStatisticsService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.runtime.directive.Foreach;
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
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.vo.BhzSwBasesPage;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.swbhz.service.IBhzSwCailiaoService;
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
/*

 */

/**
 * @Description: 水稳主表
 * @Author: jeecg-boot
 * @Date: 2021-02-24
 * @Version: V1.0
 */

@Api(tags = "水稳主表")
@RestController
@RequestMapping("/swbhz/bhzSwBases")
@Slf4j
public class BhzSwBasesController {
    @Autowired
    private IBhzSwBasesService bhzSwBasesService;
    @Autowired
    private IBhzSwCailiaoService bhzSwCailiaoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBhzCementOverHandlerService iBhzCementOverHandlerService;
    @Autowired
    private IShebeiInfoService iShebeiInfoService;
    @Autowired
    private IBhzSwStatisticsService bhzSwStatisticsService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;

    /**
     * 分页列表查询
     *
     * @param bhzSwBases
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */

    @AutoLog(value = "水稳主表-分页列表查询")
    @ApiOperation(value = "水稳主表-分页列表查询", notes = "水稳主表-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "bhz/swbhz/BhzSwBasesList")//当前用户的权限查看当前用户的所有组织机构下的数据需要在菜单管理的数据规则里面去配置
    public Result<?> queryPageList(BhzSwBases bhzSwBases,
                                   String chuliaoshijian_begin,
                                   String chuliaoshijian_end,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        String[] split = shebei.split(",");
        List<String> list = Arrays.asList(split);

        //QueryGenerator.initQueryWrapper(bhzSwBases, req.getParameterMap()); 不能按照出料时间排序
//        if (bhzSwBases.getShebeibianhao() == null) {
//            if (!"null".equals(shebei)) {
//                bhzSwBases.setShebeibianhao(shebei);
//            } else {
//                bhzSwBases.setShebeibianhao("空");
//            }
//        }
//        QueryWrapper<BhzSwBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzSwBases, req.getParameterMap());
        QueryWrapper<BhzSwBases> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("chuliaoshijian");
        if (bhzSwBases.getShebeibianhao() != null){
            queryWrapper.eq("shebeibianhao",bhzSwBases.getShebeibianhao());
        }else {
            queryWrapper.in("shebeibianhao",list);
        }

        if (bhzSwBases.getChaobiaodengji() != null){
            queryWrapper.eq("chaobiaodengji",bhzSwBases.getChaobiaodengji());
        }
        if (chuliaoshijian_begin != null){
            queryWrapper.gt("chuliaoshijian",chuliaoshijian_begin);
        }
        if (chuliaoshijian_end != null){
            queryWrapper.lt("chuliaoshijian",chuliaoshijian_end);
        }
        Page<BhzSwBases> page = new Page<BhzSwBases>(pageNo, pageSize);
        IPage<BhzSwBases> pageList = bhzSwBasesService.page(page, queryWrapper);
        List<BhzSwBases> records = pageList.getRecords();
        for (BhzSwBases record : records) {
            String unit = bhzSwBasesService.getUnit(record.getShebeibianhao());
            record.setBeiy1(unit);
        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "水稳主表-分页超标查询")
    @ApiOperation(value = "水稳主表-分页超标查询", notes = "水稳主表-分页超标查询")
    @GetMapping(value = "/chaoBiaoList")
    @PermissionData(pageComponent = "bhz/swbhz/BhzSwBasesCBList")
    public Result<?> queryPageChaoBiaoList(BhzSwBases bhzSwBases,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                           HttpServletRequest req) {
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
        Page<BhzSwBases> page = new Page<BhzSwBases>(pageNo, pageSize);
        queryWrapper.gt("chaobiaodengji",0);
        IPage<BhzSwBases> pageList = bhzSwBasesService.page(page, queryWrapper);
        List<BhzSwBases> list = pageList.getRecords();//请求中指定的所有用户数据。
        List<Object> records1 = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        for (BhzSwBases role : list) {
            String id = role.getGuid();//拿到唯一id
            BhzSwBasesPage bhzSwBasesPage = new BhzSwBasesPage();
            BeanUtils.copyProperties(role, bhzSwBasesPage);
            List<BhzSwCailiao> selectcailiaolist = bhzSwCailiaoService.selectswbhzcailiao(id);
            if (selectcailiaolist.size() > 0) {
                bhzSwBasesPage.setBhzSwCailiaoList(selectcailiaolist);
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(role.getShebeibianhao());
            if (selectshebeione != null) {
                bhzSwBasesPage.setShebeibianhao(selectshebeione.getSbname());
            }
            BhzCementOverHandler selectlist = bhzCementOverHandlerService.selectlist(id);
            if (selectlist == null) {
                BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
                bhzCementOverHandler.setOverproofStatus(0);
                bhzSwBasesPage.setBhzCementOverHandler(bhzCementOverHandler);
            } else {
                bhzSwBasesPage.setBhzCementOverHandler(selectlist);
            }
            records1.add(bhzSwBasesPage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }

    @AutoLog(value = "水稳主表-分页超标查询处置审核")
    @ApiOperation(value = "水稳主表-分页超标查询处置审核", notes = "水稳主表-分页超标查询处置审核")
    @GetMapping(value = "/czshList")
    @PermissionData(pageComponent = "bhz/swbhz/BhzSwBasesCbClList")
    public Result<?> queryPageczshList(BhzSwBases bhzSwBases,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                       HttpServletRequest req) {
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
        queryWrapper.ne("overproof_status", 20);
        Page<BhzSwBases> page = new Page<BhzSwBases>(pageNo, pageSize);
        IPage<BhzSwBases> pageList = bhzSwBasesService.page(page, queryWrapper);
        List<BhzSwBases> list = pageList.getRecords();//请求中指定的所有用户数据。
        List<Object> records1 = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        for (BhzSwBases role : list) {
            String id = role.getGuid();//拿到唯一id
            BhzSwBasesPage bhzSwBasesPage = new BhzSwBasesPage();
            BeanUtils.copyProperties(role, bhzSwBasesPage);
            List<BhzSwCailiao> selectcailiaolist = bhzSwCailiaoService.selectswbhzcailiao(id);
            if (selectcailiaolist.size() > 0) {
                bhzSwBasesPage.setBhzSwCailiaoList(selectcailiaolist);
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(role.getShebeibianhao());
            if (selectshebeione != null) {
                bhzSwBasesPage.setShebeibianhao(selectshebeione.getSbname());
            }
            BhzCementOverHandler selectlist = bhzCementOverHandlerService.selectlist(id);
            if (selectlist == null) {
                BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
                bhzCementOverHandler.setOverproofStatus(0);
                bhzSwBasesPage.setBhzCementOverHandler(bhzCementOverHandler);
            } else {
                bhzSwBasesPage.setBhzCementOverHandler(selectlist);
            }
            records1.add(bhzSwBasesPage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }


    /**
     * 水稳拌合站首页统计总数以及合格超标数据以及本月超标率
     *
     * @param bhzSwStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "水稳拌合站首页统计")
    @ApiOperation(value = "水稳拌合站首页统计", notes = "水稳拌合站首页统计")
    @GetMapping(value = "/list4")
    public Result<?> queryPageList5(BhzSwStatistics bhzSwStatistics, HttpServletRequest req) {
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
            bhzSwStatistics.setShebeibianhao(shebei);
        } else {
            bhzSwStatistics.setShebeibianhao("空");
        }
        QueryWrapper<BhzSwStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzSwStatistics, req.getParameterMap());
        queryWrapper.select("sum(all_dish) as all_dish", "sum(all_overproof_dish) as all_overproof_dish");
        queryWrapper.ge("statistics_time", parse);
        queryWrapper.le("statistics_time", parse1);
        BhzSwStatistics one = bhzSwStatisticsService.getOne(queryWrapper);

        QueryWrapper<BhzSwStatistics> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzSwStatistics, req.getParameterMap());
        queryWrapper1.select("sum(all_dish) as all_dish", "sum(all_overproof_dish) as all_overproof_dish");
        BhzSwStatistics one1 = bhzSwStatisticsService.getOne(queryWrapper1);

        Double swsum = 0.0;
        Double swcb = 0.0;
        Double swcblv = 0.0;
        Double swysum = 0.0;
        Double swycb = 0.0;
        Double swycblv = 0.0;

        if (one != null) {
            swysum = swysum + one.getAllDish();
            swycb = swycb + one.getAllOverproofDish();
        }
        if (one1 != null) {
            swsum = swsum + one1.getAllDish();
            swcb = swcb + one1.getAllOverproofDish();
        }
        Double huncblv = (swcb / swsum) * 100;//总的超标率
        Double hunylv = (swycb / swysum) * 100;//当前月的超标率
        if (huncblv > 0) {
            BigDecimal b = new BigDecimal(huncblv);
            swcblv = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        if (hunylv > 0) {
            BigDecimal b1 = new BigDecimal(hunylv);
            swycblv = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        map.put("swcblv", swcblv);
        map.put("swsum", swsum);
        map.put("swcb", swcb);
        map.put("swycblv", swycblv);
        return Result.OK(map);
    }

    /**
     * 水稳拌合站首页统计图
     *
     * @param bhzSwStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "水稳拌合站首页统计")
    @ApiOperation(value = "水稳拌合站首页统计", notes = "水稳拌合站首页统计")
    @GetMapping(value = "/list5")
    public Result<?> queryPageList6(BhzSwStatistics bhzSwStatistics, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzSwStatistics.setShebeibianhao(shebei);
        } else {
            bhzSwStatistics.setShebeibianhao("空");
        }
        QueryWrapper<BhzSwStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzSwStatistics, req.getParameterMap());
        queryWrapper.orderByDesc("statistics_time");
        queryWrapper.last("limit 10");
        List<BhzSwStatistics> bhzCementStatisticsList = bhzSwStatisticsService.list(queryWrapper);
        return Result.OK(bhzCementStatisticsList);
    }

    /**
     * 水稳拌合站首页中间部分月统计
     *
     * @param bhzSwStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "水稳拌合站首页中间部分月统计")
    @ApiOperation(value = "水稳拌合站首页中间部分月统计", notes = "水稳拌合站首页中间部分月统计")
    @GetMapping(value = "/list6")
    public Result<?> queryPageList7(BhzSwStatistics bhzSwStatistics, HttpServletRequest req, String statisticsTime_begin, String statisticsTime_end) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        SimpleDateFormat format = new SimpleDateFormat("MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzSwStatistics.setShebeibianhao(shebei);
        } else {
            bhzSwStatistics.setShebeibianhao("空");
        }
        QueryWrapper<BhzSwStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzSwStatistics, req.getParameterMap());
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

        List<BhzSwStatistics> bhzCementStatisticsList = bhzSwStatisticsService.list(queryWrapper);
        List list = new ArrayList();

        for (BhzSwStatistics statistics : bhzCementStatisticsList) {
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


    @AutoLog(value = "水稳拌合站统计")
    @ApiOperation(value = "水稳拌合站统计-超标盘数/方量", notes = "水稳土拌合站统计-超标盘数/方量")
    @GetMapping(value = "/list7")
    public Result<?> queryPageList7(BhzSwStatistics bhzSwStatistics, HttpServletRequest req, Integer date) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        SimpleDateFormat format = new SimpleDateFormat("MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        QueryWrapper<BhzSwStatistics> queryWrapper = new QueryWrapper<>();
        if (bhzSwStatistics.getShebeibianhao() != null) {
            queryWrapper.in("shebeibianhao", bhzSwStatistics.getShebeibianhao());
        } else {
            queryWrapper.in("shebeibianhao", shebeilist);
        }
        if (date != null) {
            if (date == 0) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "statistics_time");
                queryWrapper.last(" GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y'))");
            } else if (date == 1) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "FLOOR((DATE_FORMAT(statistics_time,'%m')-1)/3)+1 as all_handle_dish");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT FLOOR((DATE_FORMAT(statistics_time,'%m')-1)/3)+1)");
            } else if (date == 2) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "statistics_time");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y-%m'))");
            } else if (date == 3) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number", "DATE_FORMAT(statistics_time,'第%u周') as project_name");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y%u'))");
            }
        } else {
            queryWrapper.orderByDesc("statistics_time");
            queryWrapper.last("limit 7");
            queryWrapper.groupBy("statistics_time", "statistics_time");
        }
        List<BhzSwStatistics> bhzSwStatisticsList = bhzSwStatisticsService.list(queryWrapper);
        List list = new ArrayList();
        for (BhzSwStatistics statistics : bhzSwStatisticsList) {
            Map map = new HashMap();
            String format3 = "";
            Date statisticsTime = statistics.getStatisticsTime();
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
                    format3 = String.valueOf(statistics.getAllHandleDish());
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

    @AutoLog(value = "水稳拌合站统计")
    @ApiOperation(value = "水稳拌合站统计-合格率", notes = "水稳拌合站统计-合格率")
    @GetMapping(value = "/list8")
    public Result<?> queryPageList8(BhzSwStatistics bhzSwStatistics, HttpServletRequest req, Integer date) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        Map map = new HashMap();
        //查询到他的设备编号
        SimpleDateFormat format = new SimpleDateFormat("MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM");
        String format2 = format1.format(new Date());
        String format3 = ft1.format(new Date());
        String format4 = ft.format(new Date());
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        QueryWrapper<BhzSwStatistics> queryWrapper = new QueryWrapper<>();
        if (bhzSwStatistics.getShebeibianhao() != null) {
            queryWrapper.in("shebeibianhao", bhzSwStatistics.getShebeibianhao());
        } else {
            queryWrapper.in("shebeibianhao", shebeilist);
        }
        queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number");
        if (date != null) {
            if (date == 0) {
                queryWrapper.last("and statistics_time like '" + format2 + "%'");
            } else if (date == 1) {
                queryWrapper.last("and QUARTER(statistics_time) = QUARTER(now())");
            } else if (date == 2) {
                queryWrapper.last("and statistics_time like '" + format3 + "%'");
            } else if (date == 3) {
                queryWrapper.last("and YEARWEEK(date_format(statistics_time,'%Y-%m-%d')) = YEARWEEK(now())");
            }
        } else {
            queryWrapper.last("and statistics_time like '" + format4 + "%'");
        }
        List<BhzSwStatistics> bhzSwStatisticsList = bhzSwStatisticsService.list(queryWrapper);

        int hntsum = 0;
        int hntcb = 0;
        int prisum = 0;
        int midsum = 0;
        int advsum = 0;
        int hegesum = 0;
        for (BhzSwStatistics statistics : bhzSwStatisticsList) {
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
     * 添加
     *
     * @param bhzSwBasesPage
     * @return
     */

    @AutoLog(value = "水稳主表-添加")
    @ApiOperation(value = "水稳主表-添加", notes = "水稳主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BhzSwBasesPage bhzSwBasesPage) {
        BhzSwBases bhzSwBases = new BhzSwBases();
        BeanUtils.copyProperties(bhzSwBasesPage, bhzSwBases);
        bhzSwBasesService.saveMain(bhzSwBases, bhzSwBasesPage.getBhzSwCailiaoList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bhzSwBasesPage
     * @return
     */

    @AutoLog(value = "水稳主表-编辑")
    @ApiOperation(value = "水稳主表-编辑", notes = "水稳主表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BhzSwBasesPage bhzSwBasesPage) {
        BhzSwBases bhzSwBases = new BhzSwBases();
        BeanUtils.copyProperties(bhzSwBasesPage, bhzSwBases);
        BhzSwBases bhzSwBasesEntity = bhzSwBasesService.getById(bhzSwBases.getId());
        if (bhzSwBasesEntity == null) {
            return Result.error("未找到对应数据");
        }
        bhzSwBasesService.updateMain(bhzSwBases, bhzSwBasesPage.getBhzSwCailiaoList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */

    @AutoLog(value = "水稳主表-通过id删除")
    @ApiOperation(value = "水稳主表-通过id删除", notes = "水稳主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzSwBasesService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */

    @AutoLog(value = "水稳主表-批量删除")
    @ApiOperation(value = "水稳主表-批量删除", notes = "水稳主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzSwBasesService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */

    @AutoLog(value = "水稳主表-通过id查询")
    @ApiOperation(value = "水稳主表-通过id查询", notes = "水稳主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BhzSwBases bhzSwBases = bhzSwBasesService.getById(id);
        if (bhzSwBases == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzSwBases);

    }

    /**
     * 通过id查询
     *
     * @param
     * @return
     */

    @AutoLog(value = "水稳材料表信息通过主表ID查询")
    @ApiOperation(value = "水稳材料表信息主表ID查询", notes = "水稳材料表信息-通主表ID查询")
    @GetMapping(value = "/queryBhzSwCailiaoByMainId")
    public Result<?> queryBhzSwCailiaoListByMainId(BhzSwCailiao bhzSwCailiaoList, HttpServletRequest req, @RequestParam(name = "baseGuid", required = true) String baseGuid) {
        QueryWrapper<BhzSwCailiao> queryWrapper = QueryGenerator.initQueryWrapper(bhzSwCailiaoList, req.getParameterMap());
        List<BhzSwCailiao> pageList = bhzSwCailiaoService.list(queryWrapper);
        for (BhzSwCailiao bhzSwCailiao : pageList) {
          if(bhzSwCailiao.getWucha() != null){
              String wc = String.format("%.2f", bhzSwCailiao.getWucha());
              bhzSwCailiao.setWucha(Double.parseDouble(wc));
          }

        }
        return Result.OK(pageList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzSwBases
     */

    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzSwBases bhzSwBases) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<BhzSwBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzSwBases, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<BhzSwBases> queryList = bhzSwBasesService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<BhzSwBases> bhzSwBasesList = new ArrayList<BhzSwBases>();
        if (oConvertUtils.isEmpty(selections)) {
            bhzSwBasesList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            bhzSwBasesList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<BhzSwBasesPage> pageList = new ArrayList<BhzSwBasesPage>();
        for (BhzSwBases main : bhzSwBasesList) {
            BhzSwBasesPage vo = new BhzSwBasesPage();
            BeanUtils.copyProperties(main, vo);
            List<BhzSwCailiao> bhzSwCailiaoList = bhzSwCailiaoService.selectByMainId(main.getGuid());
            vo.setBhzSwCailiaoList(bhzSwCailiaoList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "水稳主表列表");
        mv.addObject(NormalExcelConstants.CLASS, BhzSwBasesPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("水稳主表数据", "导出人:" + sysUser.getRealname(), "水稳主表"));
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
                List<BhzSwBasesPage> list = ExcelImportUtil.importExcel(file.getInputStream(), BhzSwBasesPage.class, params);
                for (BhzSwBasesPage page : list) {
                    BhzSwBases po = new BhzSwBases();
                    BeanUtils.copyProperties(page, po);
                    bhzSwBasesService.saveMain(po, page.getBhzSwCailiaoList());
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


    @GetMapping(value = "/phbList")
    public Result<?> queryPagephbList(BhzSwBases bhzSwBases,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String staTime, String endTime,
                                      HttpServletRequest req) {

        QueryWrapper<BhzSwBases> queryWrapper = new QueryWrapper<>();
        if (staTime != null) {
            queryWrapper.ge("chuliaoshijian", staTime);
        }
        if (endTime != null) {
            queryWrapper.le("chuliaoshijian", endTime);
        }
        queryWrapper.eq("shebeibianhao", "yd1bsw_01");
        queryWrapper.orderByDesc("chuliaoshijian");
        Page<BhzSwBases> page = new Page<BhzSwBases>(pageNo, pageSize);
        IPage<BhzSwBases> pageList = bhzSwBasesService.page(page, queryWrapper);
        List<BhzSwBases> list = pageList.getRecords();//请求中指定的所有用户数据。
        Map<Object, Object> map = new HashMap<>();
        List listadd = new ArrayList();
        for (BhzSwBases role : list) {
            Map<Object, Object> map1 = new HashMap<>();
            map1.put("date", role.getChuliaoshijian());
            map1.put("location", role.getPoureLocation());
            String id = role.getGuid();//拿到唯一id
            QueryWrapper<BhzSwCailiao> bhzSwCailiaoQueryWrapper = new QueryWrapper<>();
            bhzSwCailiaoQueryWrapper.eq("base_guid", id);
            List<BhzSwCailiao> list1 = bhzSwCailiaoService.list(bhzSwCailiaoQueryWrapper);
            map1.put("phb", list1);
            listadd.add(map1);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("data", listadd);
        return Result.OK(map);
    }

    @AutoLog(value = "拌合站主表-砼拌合预警")
    @ApiOperation(value = "拌合站主表-砼拌合预警", notes = "拌合站主表-砼拌合预警")
    @GetMapping(value = "/lqbhzWarn")
    public Result<?> hntbhzWarn(String orgCode) {
        if (orgCode == "" || orgCode == null) {
            orgCode = "A";
        }

        BhzSwWarnVO bhzSwWarnVO = bhzSwBasesService.selectWranCount(orgCode);
        Map<Object, Object> map = new HashMap<>();
        //初级超标
        if (bhzSwWarnVO.getPrimaryCount() == 0) {
            map.put("primaryBiheLv", "-");
        } else if (bhzSwWarnVO.getPrimaryCount() > 0 && bhzSwWarnVO.getPrimaryBiheCount() == 0) {
            map.put("primaryBiheLv", 0);
        } else {
            double i = (double) bhzSwWarnVO.getPrimaryBiheCount() / (double) bhzSwWarnVO.getPrimaryCount() * 100;
            map.put("primaryBiheLv", (double) Math.round(i * 100) / 100);
        }
        //中级超标
        if (bhzSwWarnVO.getMiddleCount() == 0) {
            map.put("middleBiheLv", "-");
        } else if (bhzSwWarnVO.getMiddleCount() > 0 && bhzSwWarnVO.getMiddleBiheCount() == 0) {
            map.put("middleBiheLv", 0);
        } else {
            double i = (double) bhzSwWarnVO.getMiddleBiheCount() / (double) bhzSwWarnVO.getMiddleCount() * 100;
            map.put("middleBiheLv", (double) Math.round(i * 100) / 100);
        }

        //高级超标
        if (bhzSwWarnVO.getAdvancedCount() == 0) {
            map.put("advancedBiheLv", "-");
        } else if (bhzSwWarnVO.getAdvancedCount() > 0 && bhzSwWarnVO.getAdvancedBiheCount() == 0) {
            map.put("advancedBiheLv", 0);
        } else {
            double i = (double) bhzSwWarnVO.getAdvancedBiheCount() / (double) bhzSwWarnVO.getAdvancedCount() * 100;
            map.put("advancedBiheLv", (double) Math.round(i * 100) / 100);
        }
        map.put("warnCount", bhzSwWarnVO.getWarnCount());
        map.put("primaryCount", bhzSwWarnVO.getPrimaryCount());
        map.put("middleCount", bhzSwWarnVO.getMiddleCount());
        map.put("advancedCount", bhzSwWarnVO.getAdvancedCount());


        return Result.OK(map);

    }

    @AutoLog(value = "路面看板（水稳统计）")
    @ApiOperation(value = "路面看板（水稳统计）", notes = "路面看板（水稳统计）")
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
        shebeiInfoQueryWrapper.eq("sbtype", 2);
        List<ShebeiInfo> shebei = shebeiInfoService.list(shebeiInfoQueryWrapper);
        if (shebei != null || shebei.size() != 0) {
            for (ShebeiInfo shebeiInfo : shebei) {
                shebeiList.add(shebeiInfo.getSbjno());
            }
        }
        if (shebeiList.size() == 0) {
            return Result.error("该组织机构：" + sys_org_code + "下没有水稳拌合站设备");
        }
        //沥青总数
        QueryWrapper<BhzSwStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("IF( sum( all_dish ), NULL, 0 ) AS all_dish");
        queryWrapper.in("shebeibianhao", shebeiList);
        BhzSwStatistics one = bhzSwStatisticsService.getOne(queryWrapper);
        Integer allSum = one.getAllDish();
        //沥青预警数
        QueryWrapper<BhzSwStatistics> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.select("IF( sum( all_overproof_dish ), NULL, 0 ) AS all_overproof_dish ");
        queryWrapper2.in("shebeibianhao", shebeiList);
        BhzSwStatistics one2 = bhzSwStatisticsService.getOne(queryWrapper2);
        Integer overSum = one2.getAllOverproofDish();
        //沥青闭合数
        QueryWrapper<BhzSwStatistics> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.select("IF( sum( biheDish ), NULL, 0 ) AS biheDish");
        queryWrapper3.in("shebeibianhao", shebeiList);
        BhzSwStatistics one3 = bhzSwStatisticsService.getOne(queryWrapper2);
        Integer biheDish = one3.getBiheDish();
        //沥青总量
        QueryWrapper<BhzSwStatistics> queryWrapper4 = new QueryWrapper<>();
        queryWrapper4.select("IF( sum( estimate_number ), NULL, 0 ) AS estimate_number ");
        queryWrapper4.in("shebeibianhao", shebeiList);
        BhzSwStatistics one4 = bhzSwStatisticsService.getOne(queryWrapper4);
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

    @GetMapping(value = "/sgbwlist")
    public Result<?> queryPagesgbwList(BhzSwBases bhzSwBases,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                       HttpServletRequest req) {
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
        queryWrapper.select("DISTINCT(poure_location) as poure_location");
        List<BhzSwBases> pageList = bhzSwBasesService.list(queryWrapper);
        return Result.OK(pageList);
    }
}
