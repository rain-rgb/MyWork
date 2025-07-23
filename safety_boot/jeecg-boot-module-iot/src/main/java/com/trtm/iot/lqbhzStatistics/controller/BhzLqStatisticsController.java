package com.trtm.iot.lqbhzStatistics.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhzcailiaoStatistics.entity.BhzLqCailiaoStatistics;
import com.trtm.iot.lqbhzcailiaoStatistics.service.IBhzLqCailiaoStatisticsService;
import com.trtm.iot.system.entity.ShebeiInfo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.lqbhzStatistics.entity.BhzLqStatistics;
import com.trtm.iot.lqbhzStatistics.service.IBhzLqStatisticsService;

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
 * @Description: bhz_lq_statistics
 * @Author: jeecg-boot
 * @Date: 2021-03-30
 * @Version: V1.0
 */
@Api(tags = "bhz_lq_statistics")
@RestController
@RequestMapping("/lqbhzStatistics/bhzLqStatistics")
@Slf4j
public class BhzLqStatisticsController extends JeecgController<BhzLqStatistics, IBhzLqStatisticsService> {
    @Autowired
    private IBhzLqStatisticsService bhzLqStatisticsService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBhzLqCailiaoStatisticsService bhzLqCailiaoStatisticsService;

    /**
     * 分页列表查询
     *
     * @param bhzLqStatistics
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bhz_lq_statistics-分页列表查询")
    @ApiOperation(value = "bhz_lq_statistics-分页列表查询", notes = "bhz_lq_statistics-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BhzLqStatistics bhzLqStatistics,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        Page<BhzLqStatistics> page = new Page<BhzLqStatistics>(pageNo, pageSize);
        IPage<BhzLqStatistics> pageList = bhzLqStatisticsService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param bhzLqStatistics
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bhz_lq_statistics-分页列表查询")
    @ApiOperation(value = "bhz_lq_statistics-分页列表查询", notes = "bhz_lq_statistics-分页列表查询")
    @GetMapping(value = "/ycllist")
    public Result<?> queryPageyclList(BhzLqStatistics bhzLqStatistics,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzLqStatistics.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqStatistics.setShebeibianhao(shebei);
            } else {
                bhzLqStatistics.setShebeibianhao("空");
            }
        }
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        List<BhzLqStatistics> pageList = bhzLqStatisticsService.list(queryWrapper);
        List<Integer> list = new ArrayList<>();
        for (BhzLqStatistics bhzLqStatistics1 : pageList) {
            list.add(bhzLqStatistics1.getId());
        }
        QueryWrapper<BhzLqCailiaoStatistics> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("materiale_name,sum(reality_number) reality_number");
        queryWrapper1.in("cs_id", list);
        queryWrapper1.groupBy("materiale_type");
        List<Map<String, Object>> list1 = bhzLqCailiaoStatisticsService.listMaps(queryWrapper1);
        return Result.OK(list1);
    }


    /**
     * 沥青拌合站统计
     *
     * @param bhzLqStatistics
     * @param
     * @param
     * @param req
     * @return
     */
    @AutoLog(value = "沥青拌合站统计")
    @ApiOperation(value = "沥青拌合站统计-总产能/本年度/当前季度/当前月/当前周/当天", notes = "沥青拌合站统计-超标盘数/方量")
    @GetMapping(value = "/tbhcnlist")
    public Result<?> querytbhcnPageList(BhzLqStatistics bhzLqStatistics, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (!"null".equals(shebei)) {
            bhzLqStatistics.setShebeibianhao(shebei);
        } else {
            bhzLqStatistics.setShebeibianhao("空");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        String format3 = format.format(new Date());
        String format4 = ft.format(new Date());
        double estimateNumberAll = 0.0;
        double estimateNumberY = 0.0;
        double estimateNumberQ = 0.0;
        double estimateNumberM = 0.0;
        double estimateNumberW = 0.0;
        double estimateNumberD = 0.0;
        Map<String, Double> map = new HashMap<>();
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper.select("sum(estimate_number) as estimate_number");
        BhzLqStatistics one = bhzLqStatisticsService.getOne(queryWrapper);
        if (one != null) {
            estimateNumberAll = one.getEstimateNumber();//总产能
        }
        QueryWrapper<BhzLqStatistics> queryWrapper1 = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper1.select("sum(estimate_number) as estimate_number");
        queryWrapper1.last(" and statistics_time like '" + format2 + "%'");
        BhzLqStatistics one1 = bhzLqStatisticsService.getOne(queryWrapper1);
        if (one1 != null) {
            estimateNumberY = one1.getEstimateNumber();//当前年
        }
        QueryWrapper<BhzLqStatistics> queryWrapper2 = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper2.select("sum(estimate_number) as estimate_number");
        queryWrapper2.last(" and statistics_time like '" + format2 + "%' and QUARTER(statistics_time) = QUARTER(now())");
        BhzLqStatistics one2 = bhzLqStatisticsService.getOne(queryWrapper2);
        if (one2 != null) {
            estimateNumberQ = one2.getEstimateNumber();//当前季
        }
        QueryWrapper<BhzLqStatistics> queryWrapper3 = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper3.select("sum(estimate_number) as estimate_number");
        queryWrapper3.last(" and statistics_time like '" + format3 + "%'");
        BhzLqStatistics one3 = bhzLqStatisticsService.getOne(queryWrapper3);
        if (one3 != null) {
            estimateNumberM = one3.getEstimateNumber();//当前月
        }
        QueryWrapper<BhzLqStatistics> queryWrapper4 = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper4.select("sum(estimate_number) as estimate_number");
        // queryWrapper4.last(" and statistics_time like '" + format2 + "%' and YEARWEEK(date_format(statistics_time,'%Y-%m-%d'),1) = YEARWEEK(now())");
        queryWrapper4.last("and year(statistics_time) = year(now()) and week(statistics_time,1) = week(now(),1)");
        BhzLqStatistics one4 = bhzLqStatisticsService.getOne(queryWrapper4);
        if (one4 != null) {
            estimateNumberW = one4.getEstimateNumber();//当前周
        }
        QueryWrapper<BhzLqStatistics> queryWrapper5 = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper5.select("sum(estimate_number) as estimate_number");
        queryWrapper5.last(" and statistics_time like '" + format4 + "%'");
        BhzLqStatistics one5 = bhzLqStatisticsService.getOne(queryWrapper5);
        if (one5 != null) {
            estimateNumberD = one5.getEstimateNumber();//当天
        }
        map.put("estimateNumberAll", estimateNumberAll);
        map.put("estimateNumberY", estimateNumberY);
        map.put("estimateNumberQ", estimateNumberQ);
        map.put("estimateNumberM", estimateNumberM);
        map.put("estimateNumberW", estimateNumberW);
        map.put("estimateNumberD", estimateNumberD);
        return Result.OK(map);
    }

    @AutoLog(value = "拌合站统计表-产能情况表")
    @ApiOperation(value = "拌合站统计表-产能情况表", notes = "拌合站统计表-产能情况表")
    @GetMapping(value = "/cnlist1")
    public Result<?> cnPageList1(BhzLqStatistics bhzLqStatistics,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzLqStatistics.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqStatistics.setShebeibianhao(shebei);
            } else {
                bhzLqStatistics.setShebeibianhao("空");
            }
        }
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper.select("sum(estimate_number) as estimateNumber,statistics_time,shebeibianhao");
        queryWrapper.orderByDesc("statistics_time");
        queryWrapper.groupBy("statistics_time", "shebeibianhao");
        Page<BhzLqStatistics> page = new Page<BhzLqStatistics>(pageNo, pageSize);
        IPage<BhzLqStatistics> pageList = bhzLqStatisticsService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "拌合站统计表-根据混合料类统计查询")
    @ApiOperation(value = "拌合站统计表-根据混合料类统计查询", notes = "拌合站统计表-根据混合料类统计查询")
    @GetMapping(value = "/hhlstalist")
    public Result<?> PagehnlstaList1(BhzLqStatistics bhzLqStatistics,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzLqStatistics.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqStatistics.setShebeibianhao(shebei);
            } else {
                bhzLqStatistics.setShebeibianhao("空");
            }
        }
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper.select("sum(estimate_number) as estimate_number,strength_rank");
        queryWrapper.isNotNull("strength_rank");
        queryWrapper.groupBy("strength_rank");
        Page<BhzLqStatistics> page = new Page<BhzLqStatistics>(pageNo, pageSize);
        IPage<BhzLqStatistics> pageList = bhzLqStatisticsService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 根据统计时间查询产能超标数
     *
     * @param bhzLqStatistics
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bhz_lq_statistics-根据统计时间查询产能超标数")
    @ApiOperation(value = "bhz_lq_statistics-根据统计时间查询产能超标数", notes = "bhz_lq_statistics-根据统计时间查询产能超标数")
    @GetMapping(value = "/liststaData")
    public Result<?> queryPagestaDataList(BhzLqStatistics bhzLqStatistics,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req, String statisticsTime_begin, String statisticsTime_end) throws ParseException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzLqStatistics.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqStatistics.setShebeibianhao(shebei);
            } else {
                bhzLqStatistics.setShebeibianhao("空");
            }
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqStatistics, req.getParameterMap());
        queryWrapper.select("sum(estimate_number) as estimate_number,sum(primary_dish) primary_dish,sum(middle_dish) middle_dish," +
                "sum(advanced_dish) advanced_dish,sum(all_dish) all_dish,sum(all_overproof_dish) all_overproof_dish");
        if (null == statisticsTime_begin && null == statisticsTime_end) {
            queryWrapper.eq("statistics_time", ft.format(new Date()));
        }
        Map<String, Object> pageList = bhzLqStatisticsService.getMap(queryWrapper);
        if (null == pageList) {
            Map<String, Object> map = new HashMap<>();
            map.put("estimate_number", 0.0);
            map.put("primary_dish", 0);
            map.put("middle_dish", 0);
            map.put("advanced_dish", 0);
            map.put("all_dish", 0);
            map.put("all_overproof_dish", 0);
            return Result.OK(map);
        } else {
            return Result.OK(pageList);
        }

    }

    /**
     * 添加
     *
     * @param bhzLqStatistics
     * @return
     */
    @AutoLog(value = "bhz_lq_statistics-添加")
    @ApiOperation(value = "bhz_lq_statistics-添加", notes = "bhz_lq_statistics-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BhzLqStatistics bhzLqStatistics) {
        bhzLqStatisticsService.save(bhzLqStatistics);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bhzLqStatistics
     * @return
     */
    @AutoLog(value = "bhz_lq_statistics-编辑")
    @ApiOperation(value = "bhz_lq_statistics-编辑", notes = "bhz_lq_statistics-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BhzLqStatistics bhzLqStatistics) {
        bhzLqStatisticsService.updateById(bhzLqStatistics);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_lq_statistics-通过id删除")
    @ApiOperation(value = "bhz_lq_statistics-通过id删除", notes = "bhz_lq_statistics-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzLqStatisticsService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "bhz_lq_statistics-批量删除")
    @ApiOperation(value = "bhz_lq_statistics-批量删除", notes = "bhz_lq_statistics-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzLqStatisticsService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_lq_statistics-通过id查询")
    @ApiOperation(value = "bhz_lq_statistics-通过id查询", notes = "bhz_lq_statistics-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BhzLqStatistics bhzLqStatistics = bhzLqStatisticsService.getById(id);
        if (bhzLqStatistics == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzLqStatistics);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzLqStatistics
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzLqStatistics bhzLqStatistics) {
        return super.exportXls(request, bhzLqStatistics, BhzLqStatistics.class, "bhz_lq_statistics");
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
        return super.importExcel(request, response, BhzLqStatistics.class);
    }

    /**
     * 金华大屏材料消耗每日
     *
     * @param pitchSta
     * @return
     */
    @AutoLog(value = "金华大屏材料消耗每日")
    @ApiOperation(value = "金华大屏材料消耗每日数据和总数据", notes = "金华大屏材料消耗每日数据和总数据")
    @GetMapping(value = "/materialsExpendAllAndDay")
    public Result<?> materialsExpendAllAndDay(BhzLqStatistics pitchSta, HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String device = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (!"null".equals(device)) {
            pitchSta.setShebeibianhao(device);
        } else {
            pitchSta.setShebeibianhao("空");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String day = format.format(new Date());
        String parameter = day + "%";
        QueryWrapper<BhzLqStatistics> queryWrapper = QueryGenerator.initQueryWrapper(pitchSta, req.getParameterMap());
        queryWrapper.select("id");
        queryWrapper.eq("statistics_time", parameter);
        List<BhzLqStatistics> result = bhzLqStatisticsService.list(queryWrapper);
        List<Integer> list = new ArrayList<>();
        for (BhzLqStatistics item : result) {
            list.add(item.getId());
        }
        QueryWrapper<BhzLqCailiaoStatistics> qw = new QueryWrapper<>();
        qw.select("sum(reality_number) as reality_number,materiale_name");
        qw.in("cs_id", list);
        qw.groupBy("materiale_name");
        List<BhzLqCailiaoStatistics> dosageResult = bhzLqCailiaoStatisticsService.list(qw);
//        if (dosageResult != null & dosageResult.size() > 0) {
//            DecimalFormat df = new DecimalFormat("#########.00");
//            for (BhzLqCailiaoStatistics item : dosageResult) {
//                item.setRealityNumber(Double.valueOf(df.format(item.getRealityNumber())));
//            }
//        }
        return Result.OK(dosageResult);
    }

}
