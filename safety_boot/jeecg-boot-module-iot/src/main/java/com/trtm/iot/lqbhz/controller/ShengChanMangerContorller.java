package com.trtm.iot.lqbhz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.trtm.iot.lqbhzStatistics.entity.BhzLqStatistics;
import com.trtm.iot.lqbhzStatistics.service.IBhzLqStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年11月16日 9:17
 */
@Api(tags="沥青主表")
@RestController
@RequestMapping("/lqbhz/bhzLqBases")
@Slf4j
public class ShengChanMangerContorller {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBhzLqStatisticsService iBhzLqStatisticsService;


    @AutoLog(value = "沥青拌合站统计分析")
    @ApiOperation(value = "沥青拌合站统计-超标盘数/方量", notes = "沥青土拌合站统计-超标盘数/方量")
    @GetMapping(value = "/list10")
    public Result<?> queryPageList7(BhzLqStatistics bhzLqStatistics, HttpServletRequest req, Integer date) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        SimpleDateFormat format=new SimpleDateFormat("MM");
        SimpleDateFormat format1=new SimpleDateFormat("yyyy");
        SimpleDateFormat ft=new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format1.format(new Date());
        QueryWrapper<BhzLqStatistics> queryWrapper = new QueryWrapper<>();
        if (bhzLqStatistics.getShebeibianhao() !=null){
            queryWrapper.in("shebeibianhao",bhzLqStatistics.getShebeibianhao());
        }else {
            queryWrapper.in("shebeibianhao",shebeilist);
        }
        if (date!=null){
            if (date == 0){
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number","statistics_time");
                queryWrapper.last(" GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y'))");
            } else if (date == 1){
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number","FLOOR((DATE_FORMAT(statistics_time,'%m')-1)/3)+1 as all_handle_dish");
                queryWrapper.last("and statistics_time like '"+format2+"%'  GROUP BY (SELECT FLOOR((DATE_FORMAT(statistics_time,'%m')-1)/3)+1)");
            } else if (date == 2){
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number","statistics_time");
                queryWrapper.last("and statistics_time like '"+format2+"%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y-%m'))");
            } else if (date == 3) {
                queryWrapper.select("sum(all_dish) as all_dish,sum(primary_dish) as primary_dish,sum(all_overproof_dish) as all_overproof_dish," +
                        "sum(middle_dish) as middle_dish,sum(advanced_dish) as advanced_dish,sum(estimate_number) as estimate_number","DATE_FORMAT(statistics_time,'第%u周') as project_name");
                queryWrapper.last("and statistics_time like '" + format2 + "%'  GROUP BY (SELECT DATE_FORMAT(statistics_time,'%Y%u'))");
            }
        }else {
            queryWrapper.orderByDesc("statistics_time");
            queryWrapper.last("limit 10");
            queryWrapper.groupBy("statistics_time", "statistics_time");
        }
        List<BhzLqStatistics> bhzlqStatisticsList=iBhzLqStatisticsService.list(queryWrapper);
        List list =new ArrayList();
        for (BhzLqStatistics statistics : bhzlqStatisticsList) {
            Map map=new HashMap();
            Date statisticsTime = statistics.getStatisticsTime();
            String format3 = "";
            Integer allDish = statistics.getAllDish();
            Integer allDallOverproofDishish = statistics.getAllOverproofDish();
            Double estimateNumber = statistics.getEstimateNumber();//方量
            if (date!=null){
                if (date == 0){
                    format3 = format1.format(statisticsTime);
                }else if (date == 1){
                    format3 = String.valueOf(statistics.getAllHandleDish());
                }else if (date == 3){
                    format3 = statistics.getProjectName();
                }else {
                    format3 = format.format(statisticsTime);
                }
            }else {
                format3 = ft.format(statisticsTime);
            }
            map.put("statisticsTime",format3);
            map.put("estimateNumber",estimateNumber);
            map.put("date",date);
            list.add(map);
        }
        return Result.OK(list);
    }
}
