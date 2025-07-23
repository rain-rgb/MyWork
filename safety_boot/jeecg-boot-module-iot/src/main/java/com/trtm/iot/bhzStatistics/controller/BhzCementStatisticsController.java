package com.trtm.iot.bhzStatistics.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.TbhzcailiaoStatistics.service.IBhzCementDetailStatisticsService;
import com.trtm.iot.bhzStatistics.vo.BhzCementStatisticsDC;
import com.trtm.iot.bhzStatistics.vo.BhzCementStatisticsPage;
import com.trtm.iot.bhzStatistics.vo.bhzdate;
import com.trtm.iot.devicemixpilestatic.entity.DeviceMixpileStatic;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.system.service.impl.ShebeiInfoServiceImpl;
import com.xkcoding.http.util.StringUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;

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

import static cn.hutool.core.date.DateUtil.formatTime;


/**
 * @Description: 拌合站统计表
 * @Author: jeecg-boot
 * @Date: 2021-03-22
 * @Version: V1.0
 */
@Api(tags = "拌合站统计表")
@RestController
@RequestMapping("/bhzStatistics/bhzCementStatistics")
@Slf4j
public class BhzCementStatisticsController extends JeecgController<BhzCementStatistics, IBhzCementStatisticsService> {
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementDetailStatisticsService bhzCementDetailStatisticsService;

    /**
     * 分页列表查询
     *
     * @param bhzCementStatistics
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站统计表-分页列表查询")
    @ApiOperation(value = "拌合站统计表-分页列表查询", notes = "拌合站统计表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BhzCementStatistics bhzCementStatistics,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }
        bhzCementStatistics.setProjectName("*" + bhzCementStatistics.getProjectName() + "*");
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        Page<BhzCementStatistics> page = new Page<BhzCementStatistics>(pageNo, pageSize);
        IPage<BhzCementStatistics> pageList = bhzCementStatisticsService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 金华大屏材料消耗每日
     *
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土拌合站统计")
    @ApiOperation(value = "混凝土拌合站统计-总产能/本年度/当前季度/当前月/当前周/当天", notes = "混凝土拌合站统计-超标盘数/方量")
    @GetMapping(value = "/tbhcnlistJh")
    public Result<?> querytbhcnPageListJh(BhzCementStatistics bhzCementStatistics, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号

        if (!"null".equals(shebei)) {
            bhzCementStatistics.setShebeiNo(shebei);
        } else {
            bhzCementStatistics.setShebeiNo("空");
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format4 = ft.format(new Date());
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("id");
        queryWrapper.last(" and statistics_time like '" + format4 + "%'");
        List<BhzCementStatistics> list = bhzCementStatisticsService.list(queryWrapper);
        List<Integer> list1 = new ArrayList<>();
        BhzCementDetailStatistics bhzCementDetailStatistics = new BhzCementDetailStatistics();
        List<com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics> list3 = new ArrayList<>();
        list3.add(bhzCementDetailStatistics);
        if (list.size() == 0) {
            return Result.OK(list3);
        }
        for (BhzCementStatistics l : list) {
            list1.add(l.getId());
        }
        QueryWrapper<com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("sum(reality_number) as reality_number,materiale_name");
        queryWrapper1.in("cs_id", list1);
        queryWrapper1.groupBy("materiale_name");
        List<com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics> list2 = bhzCementDetailStatisticsService.list(queryWrapper1);
        return Result.OK(list2);
    }

    /**
     * 金华大屏材料消耗
     *
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土拌合站统计")
    @ApiOperation(value = "混凝土拌合站统计-总产能/本年度/当前季度/当前月/当前周/当天", notes = "混凝土拌合站统计-超标盘数/方量")
    @GetMapping(value = "/tbhcnlist")
    public Result<?> querytbhcnPageList(BhzCementStatistics bhzCementStatistics, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号

        if (!"null".equals(shebei)) {
            bhzCementStatistics.setShebeiNo(shebei);
        } else {
            bhzCementStatistics.setShebeiNo("空");
        }

        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("id");
        List<BhzCementStatistics> list = bhzCementStatisticsService.list(queryWrapper);
        List<Integer> list1 = new ArrayList<>();

        if (list.size() == 0) {
            return Result.error("今天暂无材料消耗");
        }
        for (BhzCementStatistics l : list) {
            list1.add(l.getId());
        }
        QueryWrapper<com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("sum(reality_number) as reality_number,materiale_name");
        queryWrapper1.in("cs_id", list1);
        queryWrapper1.groupBy("materiale_name");
        List<com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics> list2 = bhzCementDetailStatisticsService.list(queryWrapper1);
        return Result.OK(list2);
    }

    /**
     * 混凝土统计按月份
     *
     * @param bhzCementStatistics
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站统计表-分页列表查询")
    @ApiOperation(value = "拌合站统计表-分页列表查询", notes = "拌合站统计表-分页列表查询")
    @GetMapping(value = "/listJh")
    public Result<?> queryPageListJh(BhzCementStatistics bhzCementStatistics,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String format3 = format.format(new Date());
        int estimateNumberM = 0;
        QueryWrapper<BhzCementStatistics> queryWrapper3 = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper3.select("sum(estimate_number) as estimate_number");
        queryWrapper3.last(" and statistics_time like '" + format3 + "%'");
        BhzCementStatistics one3 = bhzCementStatisticsService.getOne(queryWrapper3);
        if (one3 != null) {
            estimateNumberM = one3.getEstimateNumber().intValue();//当前月
        }
        return Result.OK(estimateNumberM);
    }

    @AutoLog(value = "拌合站统计表-累计方量")
    @ApiOperation(value = "拌合站统计表-累计方量", notes = "拌合站统计表-累计方量")
    @GetMapping(value = "/ljfllist")
    public Result<?> ljflPageList(BhzCementStatistics bhzCementStatistics,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("ifnull(sum(estimate_number),0) as estimateNumber", "strength_rank");
        queryWrapper.isNotNull("strength_rank");
        queryWrapper.ne("strength_rank", "");
        queryWrapper.groupBy("strength_rank");
        List<BhzCementStatistics> pageList = bhzCementStatisticsService.list(queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "拌合站统计表-产能情况表")
    @ApiOperation(value = "拌合站统计表-产能情况表", notes = "拌合站统计表-产能情况表")
    @GetMapping(value = "/cnlist")
    public Result<?> cnPageList(BhzCementStatistics bhzCementStatistics,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("sum(estimate_number) as estimateNumber", "statistics_time", "shebei_no");
        queryWrapper.orderByDesc("statistics_time");
        queryWrapper.groupBy("statistics_time", "shebei_no");
        Page<BhzCementStatistics> page = new Page<BhzCementStatistics>(pageNo, pageSize);
        IPage<BhzCementStatistics> pageList = bhzCementStatisticsService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "拌合站统计表-产能情况表")
    @ApiOperation(value = "拌合站统计表-产能情况表", notes = "拌合站统计表-产能情况表")
    @GetMapping(value = "/cnlist1")
    public Result<?> cnPageList1(BhzCementStatistics bhzCementStatistics,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }
        List<BhzCementStatistics> records = bhzCementStatisticsService.selectbydays(bhzCementStatistics.getShebeiNo());
        for (BhzCementStatistics record : records) {
            String shebeiNo = record.getShebeiNo();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            if (selectshebeione != null) {
                record.setShebeiNo(selectshebeione.getSbname());
            }
        }
        return Result.OK(records);
    }

    @AutoLog(value = "拌合站统计表-本月产能情况表")
    @ApiOperation(value = "拌合站统计表-产能情况表", notes = "拌合站统计表-产能情况表")
    @GetMapping(value = "/monthcnlist")
    public Result<?> cnPageList1(BhzCementStatistics bhzCementStatistics,
                                 HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }
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
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("sum(estimate_number) as estimateNumber");
        queryWrapper.ge("statistics_time", parse);
        queryWrapper.le("statistics_time", parse1);
        List<BhzCementStatistics> pageList = bhzCementStatisticsService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 混凝土超标总盘数
     *
     * @param bhzCementStatistics
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站统计表-混凝土超标总盘数")
    @ApiOperation(value = "拌合站统计表-混凝土超标总盘数", notes = "拌合站统计表-混凝土超标总盘数")
    @GetMapping(value = "/totallist")
    public Result<?> totalPageList(BhzCementStatistics bhzCementStatistics,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("sum(all_overproof_dish) as allOverproofDish");
        Page<BhzCementStatistics> page = new Page<BhzCementStatistics>(pageNo, pageSize);
        IPage<BhzCementStatistics> pageList = bhzCementStatisticsService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param bhzCementStatistics
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站统计表-原材料消耗查询")
    @ApiOperation(value = "拌合站统计表-原材料消耗查询", notes = "拌合站统计表-原材料消耗查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(BhzCementStatistics bhzCementStatistics,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sys_depart_orgcode,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        Page<BhzCementStatistics> page = new Page<BhzCementStatistics>(pageNo, pageSize);
        IPage<BhzCementStatistics> pageList = bhzCementStatisticsService.page(page, queryWrapper);
        List<BhzCementStatistics> records = pageList.getRecords();
        List records1 = new ArrayList<>();
        Map map = new HashMap();
        for (BhzCementStatistics record : records) {
            BhzCementStatisticsPage bhzCementStatisticsPage = new BhzCementStatisticsPage();
            Integer id = record.getId();
            String poureLocation = record.getPoureLocation();
            String projectName = record.getProjectName();
            String shebeiNo = record.getShebeiNo();
            String jobLocation = record.getJobLocation();
            String strengthRank = record.getStrengthRank();
            Double estimateNumber = record.getEstimateNumber();
            Date statisticsTime = record.getStatisticsTime();
            List<BhzCementDetailStatistics> selectdetaillist = bhzCementDetailStatisticsService.selectdetaillist(id);
            if (selectdetaillist.size() > 0) {
                bhzCementStatisticsPage.setBhzCementDetailStatisticsList(selectdetaillist);
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            bhzCementStatisticsPage.setShebeiNo(selectshebeione.getSbname());
            bhzCementStatisticsPage.setPoureLocation(poureLocation);
            bhzCementStatisticsPage.setProjectName(projectName);
            bhzCementStatisticsPage.setJobLocation(jobLocation);
            bhzCementStatisticsPage.setStrengthRank(strengthRank);
            bhzCementStatisticsPage.setEstimateNumber(Double.valueOf(String.format("%.2f", estimateNumber)));
            bhzCementStatisticsPage.setId(id);
            bhzCementStatisticsPage.setStatisticsTime(statisticsTime);
            records1.add(bhzCementStatisticsPage);
        }
        map.put("current", pageList.getCurrent());
        map.put("pages", pageList.getPages());
        map.put("size", pageList.getSize());
        map.put("total", pageList.getTotal());
        map.put("records", records1);
        return Result.OK(map);
    }

    @AutoLog(value = "拌合站统计表-原材料消耗查询")
    @ApiOperation(value = "拌合站统计表-原材料消耗查询导入api", notes = "拌合站统计表-原材料消耗查询导入api")
    @GetMapping(value = "/exportApi")
    public Result<?> exportApi(BhzCementStatistics bhzCementStatistics, HttpServletRequest req) {
        List data = new ArrayList<>();
        QueryWrapper<BhzCementStatistics> statisticsQueryWrapper = new QueryWrapper<>();
        String shebeiNo1 = bhzCementStatistics.getShebeiNo();
        String statisticsTime_begin = req.getParameter("statisticsTime_begin");
        String statisticsTime_end = req.getParameter("statisticsTime_end");
        String projectName1 = bhzCementStatistics.getProjectName();
        String poureLocation1 = bhzCementStatistics.getPoureLocation();
        String strengthRank1 = bhzCementStatistics.getStrengthRank();
        if (StringUtil.isNotEmpty(shebeiNo1)) {
            statisticsQueryWrapper.eq("shebei_no", shebeiNo1);
        }
        if (StringUtil.isNotEmpty(projectName1)) {
            statisticsQueryWrapper.eq("project_name", projectName1);
        }
        if (StringUtil.isNotEmpty(poureLocation1)) {
            statisticsQueryWrapper.eq("poure_location", poureLocation1);
        }
        if (StringUtil.isNotEmpty(strengthRank1)) {
            statisticsQueryWrapper.eq("strength_rank", strengthRank1);
        }
        if (StringUtil.isNotEmpty(statisticsTime_begin)) {
            statisticsQueryWrapper.ge("statistics_time", statisticsTime_begin);
        }
        if (StringUtil.isNotEmpty(statisticsTime_end)) {
            statisticsQueryWrapper.le("statistics_time", statisticsTime_end);
        }
        List<BhzCementStatistics> list = bhzCementStatisticsService.list(statisticsQueryWrapper);
        for (BhzCementStatistics cementStatistics : list) {
            BhzCementStatisticsDC bhzCementStatisticsPage = new BhzCementStatisticsDC();
            Integer id = cementStatistics.getId();
            String poureLocation = cementStatistics.getPoureLocation();
            String projectName = cementStatistics.getProjectName();
            String shebeiNo = cementStatistics.getShebeiNo();
            String jobLocation = cementStatistics.getJobLocation();
            String strengthRank = cementStatistics.getStrengthRank();
            Double estimateNumber = cementStatistics.getEstimateNumber();
            Date statisticsTime = cementStatistics.getStatisticsTime();
            List<BhzCementDetailStatistics> selectdetaillist = bhzCementDetailStatisticsService.selectdetaillist(id);
            for (BhzCementDetailStatistics bhzCementDetailStatistics : selectdetaillist) {
                String materialeName = bhzCementDetailStatistics.getMaterialeName();
                Double realityNumber = bhzCementDetailStatistics.getRealityNumber();
                Double theoryNumber = bhzCementDetailStatistics.getTheoryNumber();
                switch (materialeName) {
                    case "0-4.75砂":
                        bhzCementStatisticsPage.setShi1rea(theoryNumber);
                        bhzCementStatisticsPage.setShi1the(realityNumber);
                        break;
                    case "4.75-9.5碎石":
                        bhzCementStatisticsPage.setShi2rea(theoryNumber);
                        bhzCementStatisticsPage.setShi2the(realityNumber);
                        break;
                    case "5-16碎石":
                        bhzCementStatisticsPage.setShi3rea(theoryNumber);
                        bhzCementStatisticsPage.setShi3the(realityNumber);
                        break;
                    case "9.5-19碎石":
                        bhzCementStatisticsPage.setShi4rea(theoryNumber);
                        bhzCementStatisticsPage.setShi4the(realityNumber);
                        break;
                    case "16-31.5碎石":
                        bhzCementStatisticsPage.setShi5rea(theoryNumber);
                        bhzCementStatisticsPage.setShi5the(realityNumber);
                        break;
                    case "19-31.5碎石":
                        bhzCementStatisticsPage.setShi6rea(theoryNumber);
                        bhzCementStatisticsPage.setShi6the(realityNumber);
                        break;
                    case "水泥":
                        bhzCementStatisticsPage.setShini1rea(theoryNumber);
                        bhzCementStatisticsPage.setShini1the(realityNumber);
                        break;
                    case "水泥2":
                        bhzCementStatisticsPage.setShini2rea(theoryNumber);
                        bhzCementStatisticsPage.setShini2the(realityNumber);
                        break;
                    case "水泥5":
                        bhzCementStatisticsPage.setShini3rea(theoryNumber);
                        bhzCementStatisticsPage.setShini3the(realityNumber);
                        break;
                    case "煤灰1":
                        bhzCementStatisticsPage.setFmh1rea(theoryNumber);
                        bhzCementStatisticsPage.setFmh1the(realityNumber);
                        break;
                    case "矿粉4":
                        bhzCementStatisticsPage.setKf1rea(theoryNumber);
                        bhzCementStatisticsPage.setKf1the(realityNumber);
                        break;
                    case "水M1":
                        bhzCementStatisticsPage.setShui1rea(theoryNumber);
                        bhzCementStatisticsPage.setShui1the(realityNumber);
                        break;
                    case "水M2":
                        bhzCementStatisticsPage.setShui2rea(theoryNumber);
                        bhzCementStatisticsPage.setShui2the(realityNumber);
                        break;
                    case "废水M2":
                        bhzCementStatisticsPage.setShui3rea(theoryNumber);
                        bhzCementStatisticsPage.setShui3the(realityNumber);
                        break;
                    case "外加剂11":
                        bhzCementStatisticsPage.setWjj1rea(theoryNumber);
                        bhzCementStatisticsPage.setWjj1the(realityNumber);
                        break;
                    case "外加剂12":
                        bhzCementStatisticsPage.setWjj2rea(theoryNumber);
                        bhzCementStatisticsPage.setWjj2the(realityNumber);
                        break;
                    case "外加剂21":
                        bhzCementStatisticsPage.setWjj3rea(theoryNumber);
                        bhzCementStatisticsPage.setWjj3the(realityNumber);
                        break;
                    case "膨胀剂3":
                        bhzCementStatisticsPage.setWjj4rea(theoryNumber);
                        bhzCementStatisticsPage.setWjj4the(realityNumber);
                        break;
                    default:
                        // 处理未知类型的情况
                        break;
                }
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            bhzCementStatisticsPage.setShebeiNo(selectshebeione.getSbname());
            bhzCementStatisticsPage.setPoureLocation(poureLocation);
            bhzCementStatisticsPage.setProjectName(projectName);
            bhzCementStatisticsPage.setJobLocation(jobLocation);
            bhzCementStatisticsPage.setStrengthRank(strengthRank);
            bhzCementStatisticsPage.setEstimateNumber(Double.valueOf(String.format("%.2f", estimateNumber)));
            bhzCementStatisticsPage.setId(id);
            bhzCementStatisticsPage.setStatisticsTime(statisticsTime);
            data.add(bhzCementStatisticsPage);
        }
        return Result.OKs(data);
    }

    @SneakyThrows
    @AutoLog(value = "拌合站统计表-原材料消耗查询")
    @ApiOperation(value = "拌合站统计表-原材料消耗时间api", notes = "拌合站统计表-原材料消耗时间api")
    @GetMapping(value = "/exportDateApi")
    public Result<?> exportDateApi(HttpServletRequest req, String statisticsTime_begin, String statisticsTime_end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        List data = new ArrayList<>();
        bhzdate bhzdate = new bhzdate();
        if (StringUtil.isNotEmpty(statisticsTime_begin)) {
            Date parsedDate = format.parse(statisticsTime_begin);
            bhzdate.setStadate(format1.format(parsedDate));
        }
        if (StringUtil.isNotEmpty(statisticsTime_end)) {
            Date parsedDate = format1.parse(statisticsTime_end);
            bhzdate.setEnddate(format1.format(parsedDate));
        }
        data.add(bhzdate);
        return Result.OKs(data);
    }

    /**
     * 混凝土超标总盘数
     *
     * @param bhzCementStatistics
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拌合站统计表-按设备统计每月超标率导出")
    @ApiOperation(value = "拌合站统计表-按设备统计每月超标率导出", notes = "拌合站统计表-按设备统计每月超标率导出")
    @GetMapping(value = "/liststadc")
    public Result<?> liststadcPageList(BhzCementStatistics bhzCementStatistics,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req, String statisticsTime_begin, String statisticsTime_end) throws ParseException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
        String day1 = format1.format(c.getTime());
        Date parse = null;//本月第一天
        Date parse1 = null;//本月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String day2 = format1.format(ca.getTime());
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, req.getParameterMap());
        queryWrapper.select("id,shebei_no,sum(all_dish) as all_dish,sum(all_overproof_dish) as all_overproof_dish,primary_percent,formula_no");
        if (null == statisticsTime_begin && null == statisticsTime_end) {
            statisticsTime_begin = day1;
            statisticsTime_end = day2;
            queryWrapper.ge("statistics_time", day1);
            queryWrapper.le("statistics_time", day2);
        }
        queryWrapper.groupBy("shebei_no");
        Page<BhzCementStatistics> page = new Page<BhzCementStatistics>(pageNo, pageSize);
        IPage<BhzCementStatistics> pageList = bhzCementStatisticsService.page(page, queryWrapper);
        List<BhzCementStatistics> records = pageList.getRecords();
        double chaolv = 0;
        for (BhzCementStatistics bhzCementStatistics1 : records) {
            if (null != bhzCementStatistics1) {
                if (null != statisticsTime_begin && null != statisticsTime_end) {
                    bhzCementStatistics1.setFormulaNo(format1.format(format1.parse(statisticsTime_begin)) + "~" + format1.format(format1.parse(statisticsTime_end)));
                }
                if (bhzCementStatistics1.getAllDish() != 0) {
                    double allDish = bhzCementStatistics1.getAllDish();
                    double allOverproofDish = bhzCementStatistics1.getAllOverproofDish();
                    chaolv = allOverproofDish * 100 / allDish;
                    bhzCementStatistics1.setPrimaryPercent(Double.parseDouble(String.format("%.2f", chaolv)));
                }
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bhzCementStatistics
     * @return
     */
    @AutoLog(value = "拌合站统计表-添加")
    @ApiOperation(value = "拌合站统计表-添加", notes = "拌合站统计表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BhzCementStatistics bhzCementStatistics) {
        bhzCementStatisticsService.save(bhzCementStatistics);
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "拌合站统计表-添加")
    @ApiOperation(value = "拌合站统计表-添加", notes = "拌合站统计表-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> add1(@RequestBody BhzCementStatisticsPage bhzCementStatisticsPage) {
        BhzCementStatistics bhzCementStatistics = new BhzCementStatistics();
        BeanUtils.copyProperties(bhzCementStatisticsPage, bhzCementStatistics);
        bhzCementStatisticsService.saveMain(bhzCementStatistics, bhzCementStatisticsPage.getBhzCementDetailStatisticsList());
        return Result.OK("添加成功！");
    }


    /**
     * 编辑
     *
     * @param bhzCementStatistics
     * @return
     */
    @AutoLog(value = "拌合站统计表-编辑")
    @ApiOperation(value = "拌合站统计表-编辑", notes = "拌合站统计表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BhzCementStatistics bhzCementStatistics) {
        bhzCementStatisticsService.updateById(bhzCementStatistics);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "拌合站统计表-通过id删除")
    @ApiOperation(value = "拌合站统计表-通过id删除", notes = "拌合站统计表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzCementStatisticsService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "拌合站统计表-批量删除")
    @ApiOperation(value = "拌合站统计表-批量删除", notes = "拌合站统计表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzCementStatisticsService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "拌合站统计表-通过id查询")
    @ApiOperation(value = "拌合站统计表-通过id查询", notes = "拌合站统计表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BhzCementStatistics bhzCementStatistics = bhzCementStatisticsService.getById(id);
        if (bhzCementStatistics == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzCementStatistics);
    }

    /**
     * 通过id查询
     *
     * @param
     * @return
     */
    @AutoLog(value = "拌合站子表材料信息通过主表ID查询")
    @ApiOperation(value = "拌合站子表材料信息主表ID查询", notes = "拌合站子表材料信息-通主表ID查询")
    @GetMapping(value = "/queryBhzCementDetailStatisticsByMainId")
    public Result<?> queryBhzCementDetailStatisticsListByMainId(BhzCementDetailStatistics bhzCementDetailStatisticsList, HttpServletRequest req, @RequestParam(name = "csId", required = true) Integer csId) {
        QueryWrapper<BhzCementDetailStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementDetailStatisticsList, req.getParameterMap());
        List<BhzCementDetailStatistics> pageList = bhzCementDetailStatisticsService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 导出拌合站材料消耗excel
     *
     * @param request
     * @param bhzCementStatistics
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzCementStatistics bhzCementStatistics) {
        return super.exportXls(request, bhzCementStatistics, BhzCementStatistics.class, "拌合站统计表");
    }

    /**
     * 统计导出导出excel
     *
     * @param request
     * @param bhzCementStatistics
     */
    @RequestMapping(value = "/exportXlsta")
    public ModelAndView exportXlsta(HttpServletRequest request, BhzCementStatistics bhzCementStatistics, String statisticsTime_begin, String statisticsTime_end) throws ParseException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//1:本月第一天
        String day1 = format1.format(c.getTime());
        Date parse = null;//本月第一天
        Date parse1 = null;//本月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String day2 = format1.format(ca.getTime());
        QueryWrapper<BhzCementStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzCementStatistics, request.getParameterMap());
        queryWrapper.select("shebei_no,sum(all_dish) as all_dish,sum(all_overproof_dish) as all_overproof_dish,primary_percent,formula_no");
        if (null == statisticsTime_begin && null == statisticsTime_end) {
            statisticsTime_begin = day1;
            statisticsTime_end = day2;
            queryWrapper.ge("statistics_time", day1);
            queryWrapper.le("statistics_time", day2);
        }
        queryWrapper.groupBy("shebei_no");
        List<BhzCementStatistics> pageList = bhzCementStatisticsService.list(queryWrapper);
        double chaolv = 0;
        for (BhzCementStatistics bhzCementStatistics1 : pageList) {
            if (null != bhzCementStatistics1) {
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(bhzCementStatistics1.getShebeiNo());
                bhzCementStatistics1.setShebeiNo(shebeiInfo.getSbname());
                if (null != statisticsTime_begin && null != statisticsTime_end) {
                    bhzCementStatistics1.setFormulaNo(format1.format(format1.parse(statisticsTime_begin)) + "~" + format1.format(format1.parse(statisticsTime_end)));
                }
                if (bhzCementStatistics1.getAllDish() != 0) {
                    double allDish = bhzCementStatistics1.getAllDish();
                    double allOverproofDish = bhzCementStatistics1.getAllOverproofDish();
                    chaolv = allOverproofDish / allDish * 100;
                    bhzCementStatistics1.setPrimaryPercent(Double.parseDouble(String.format("%.2f", chaolv)));
                }
            }
        } // 过滤选中数据
        String selections = request.getParameter("selections");
        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "混凝土拌合站超标率统计表");
        mv.addObject(NormalExcelConstants.CLASS, BhzCementStatistics.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("混凝土拌合站超标率统计表", "导出人:" + loginUser.getRealname(), "混凝土拌合站超标率统计表"));
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
        return super.importExcel(request, response, BhzCementStatistics.class);
    }

    /**
     * 通过id查询
     *
     * @param
     * @return
     */
    @AutoLog(value = "拌合机当前月超标排名")
    @ApiOperation(value = "拌合机当前月超标排名", notes = "拌合机当前月超标排名")
    @GetMapping(value = "/countList")
    public Result<?> countList() {
        List<Map<String, Object>> bhzCementStatistics = bhzCementStatisticsService.countList();
        return Result.OK(bhzCementStatistics);
    }

    @GetMapping(value = "/bhzCNCount")
    public Result<?> bhzCNCount(BhzCementStatistics bhzCementStatistics, HttpServletRequest req) {
        List<BhzCementStatistics> list = new ArrayList<>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息

        final String orgCode = loginUser.getOrgCode();
        final List<Map<Object, String>> maps = bhzCementStatisticsService.selectOrgCode(orgCode);
        for (int i = 0; i < maps.size(); i++) {
            QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
            final String org_code = maps.get(i).get("org_code");
            final String depart_name = maps.get(i).get("depart_name");
            queryWrapper.select("ifnull(sum(estimate_number),0) as estimateNumber,'" + depart_name + "' as projectName");
            queryWrapper.last("a join shebei_info b on a.shebei_no = b.sbjno where b.sys_org_code like '" + org_code + "%'");
            final BhzCementStatistics one = bhzCementStatisticsService.getOne(queryWrapper);
            list.add(one);
        }


        return Result.OK(list);
    }

    /**
     * 根据组织机构查询
     *
     * @param
     * @return
     */
    @AutoLog(value = "拌合机当前月超标排名")
    @ApiOperation(value = "拌合机当前月超标排名", notes = "拌合机当前月超标排名")
    @GetMapping(value = "/countLists")
    public Result<?> countLists(String orgCategory, Integer date) {
        String[] split = orgCategory.split(",");
        String orgCategorys = "'" + StringUtils.join(split, "','") + "'";//数据格式   'A','B','C'
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        List<Map<String, Object>> bhzCementStatistics = bhzCementStatisticsService.countLists(orgCategorys, loginUser.getOrgCode() + "%", date);
        return Result.OK(bhzCementStatistics);
    }

    /**
     * 拌合站统计详情数据
     *
     * @param csid 拌合站id
     * @return
     */
    @AutoLog(value = "拌合机统计详情")
    @ApiOperation(value = "拌合机统计详情", notes = "拌合机统计详情")
    @GetMapping(value = "/getListById")
    public Result<?> getListById(Integer csid) {
        QueryWrapper<BhzCementDetailStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cs_id", csid);
        queryWrapper.orderByAsc("materiale_type");
        List<BhzCementDetailStatistics> list = bhzCementDetailStatisticsService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 拌合站初、中、高级超标统计
     *
     * @param bhzCementStatistics
     * @return
     */
    @AutoLog(value = "拌合站初中高级超标统计")
    @ApiOperation(value = "拌合站初中高级超标统计", notes = "拌合站初中高级超标统计")
    @GetMapping(value = "/censusList")
    public Result<?> censusList(BhzCementStatistics bhzCementStatistics) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzCementStatistics.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                bhzCementStatistics.setShebeiNo(shebei);
            } else {
                bhzCementStatistics.setShebeiNo("空");
            }
        }
        if ("空".equals(bhzCementStatistics.getShebeiNo())) {
            return Result.error("未查询到超标数据!");
        }
        String[] split = shebei.split(",");
        List<BhzCementStatistics> BCSList = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();
        for (int i = 0; i < split.length; i++) {
            QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shebei_no", split[i]);
            List<BhzCementStatistics> list = bhzCementStatisticsService.list(queryWrapper);
            BCSList.addAll(list);
        }
        Integer cj = 0;
        Integer zj = 0;
        Integer gj = 0;
        for (BhzCementStatistics one : BCSList) {
            cj += one.getPrimaryDish();
            zj += one.getMiddleDish();
            gj += one.getAdvancedDish();
        }
        map.put("cjcb", cj);
        map.put("zjcb", zj);
        map.put("gjcb", gj);
        return Result.OK(map);
    }


    /**
     * 拌合站强度等级统计饼图接口
     *
     * @param bhzCementStatistics
     * @param time
     * @return
     */
    @AutoLog(value = "bhz_cement_base-强度等级查询")
    @ApiOperation(value = "bhz_cement_base-强度等级查询", notes = "bhz_cement_base-强度等级查询")
    @GetMapping(value = "/bhzInfoList")
    public Result<?> bhzInfoList(BhzCementStatistics bhzCementStatistics, Integer time) {
        String shebei = null;
        if (ObjectUtil.isEmpty(bhzCementStatistics.getShebeiNo())) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        } else {
            shebei = bhzCementStatistics.getShebeiNo();
        }
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        Map map = bhzCementStatisticsService.getTongJiList(shebeilist, time);
        return Result.OK(map);
    }

    /**
     * 拌合站强度等级统计列表接口
     *
     * @param bhzCementStatistics
     * @param time
     * @return
     */
    @AutoLog(value = "bhz_cement_base-强度等级查询")
    @ApiOperation(value = "bhz_cement_base-强度等级查询", notes = "bhz_cement_base-强度等级查询")
    @GetMapping(value = "/bhzTongJiLists")
    public Result<?> bhzTongJiLists(BhzCementStatistics bhzCementStatistics, Integer time,String sys_depart_orgcode) {
        List<BhzCementStatistics> list = bhzCementStatisticsService.getTongJiLists(sys_depart_orgcode, time);
        return Result.OK(list);
    }

}
