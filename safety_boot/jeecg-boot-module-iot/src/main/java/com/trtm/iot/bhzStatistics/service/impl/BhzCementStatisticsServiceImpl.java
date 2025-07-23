package com.trtm.iot.bhzStatistics.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.TbhzcailiaoStatistics.mapper.BhzCementDetailStatisticsMapper;
import com.trtm.iot.bhzStatistics.controller.BhzCementStatisticsController;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.mapper.BhzCementStatisticsMapper;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.kongjingbasicinfo.entity.KongjingBasicinfo;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 拌合站统计表
 * @Author: jeecg-boot
 * @Date: 2021-03-22
 * @Version: V1.0
 */
@Service
public class BhzCementStatisticsServiceImpl extends ServiceImpl<BhzCementStatisticsMapper, BhzCementStatistics> implements IBhzCementStatisticsService {

    @Autowired
    BhzCementStatisticsMapper bhzCementStatisticsMapper;
    @Autowired
    BhzCementDetailStatisticsMapper bhzCementDetailStatisticsMapper;


    @Override
    public BhzCementStatistics selectlimit(Date datanyr1, String projectName, String poureLocation, String jobLocation, String formulaNo, String strengthRank, String shebeiNo) {
//        try {
//            QueryWrapper<BhzCementStatistics> queryWrapper=new QueryWrapper<>();
//            queryWrapper.eq("statistics_time",date);
//            if(projectName!=null){
//                queryWrapper.eq("project_name",projectName);
//            }
//            if(poureLocation!=null){
//                queryWrapper.eq("poure_location",poureLocation);
//            }
//            if(jobLocation!=null){
//                queryWrapper.eq("job_location",jobLocation);
//            }
//            if(formulaNo!=null){
//                queryWrapper.eq("formula_no",formulaNo);
//            }
//            if(strengthRank!=null){
//                queryWrapper.eq("strength_rank",strengthRank);
//            }
//            return this.getOne(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return bhzCementStatisticsMapper.selectlimit(datanyr1, projectName, poureLocation, jobLocation, formulaNo, strengthRank, shebeiNo);
    }

    @Override
    public List<BhzCementStatistics> selectbhzstaList(Integer curid, Integer istongji, Date datanyr1) {
        try {
            QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", curid);
            queryWrapper.eq("istongji", istongji);
            queryWrapper.lt("statistics_time", datanyr1);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateistongji(int id, Integer istongji) {
        return bhzCementStatisticsMapper.updateistongji(id, istongji);
    }

    @Override
    public boolean updatestatisticsone(Integer id, Integer allsum, Double sumfl, Integer cjsum, Integer allsums, Integer zjsum, Integer gjsum) {
        return bhzCementStatisticsMapper.updatestatisticsone(id, allsum, sumfl, cjsum, allsums, zjsum, gjsum);
    }

    @Override
    public List<Map<String, Object>> countList() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        return bhzCementStatisticsMapper.countList(loginUser.getOrgCode() + "%");
    }

    @Override
    public void saveMain(BhzCementStatistics bhzCementStatistics, List<BhzCementDetailStatistics> bhzCementDetailStatisticsList) {
        bhzCementStatisticsMapper.insert(bhzCementStatistics);
        if (bhzCementDetailStatisticsList != null && bhzCementDetailStatisticsList.size() > 0) {
            for (BhzCementDetailStatistics bhzCementDetailStatistics : bhzCementDetailStatisticsList) {
                bhzCementDetailStatisticsMapper.insert(bhzCementDetailStatistics);
            }
        }
    }

    @Override
    public List<BhzCementStatistics> selectBhzStatisticsList(Integer curid, List<String> shebeiNo, Date datanyr1) {
        try {
            QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", curid);
            queryWrapper.lt("statistics_time", datanyr1);
            queryWrapper.in("shebei_no", shebeiNo);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BhzCementStatistics> selectBhzStatisticsLists(Integer curid, List<String> shebeiNo) {
        try {
            QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", curid);
            queryWrapper.in("shebei_no", shebeiNo);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<Object, String>> selectOrgCode(String orgCode) {
        return bhzCementStatisticsMapper.selectOrgCode(orgCode);
    }

    @Override
    public List<Map<String, Object>> countLists(String orgCategorys, String orgCode, Integer date) {
        return bhzCementStatisticsMapper.countLists(orgCategorys, orgCode, date);
    }

    @Override
    public List<BhzCementStatistics> selectjbzzones(String shebeino, Integer curid) {
        return bhzCementStatisticsMapper.selectjbzzones(shebeino, curid);
    }

    @Override
    public List<BhzCementStatistics> selectbydays(String shebeiNo) {
        return bhzCementStatisticsMapper.selectbydays(shebeiNo);
    }

    @Override
    public BhzCementStatistics findRenwudanzs(List<String> sbs) {
        return bhzCementStatisticsMapper.findRenwudanzs(sbs);
    }

    @Override
    public Map getTongJiList(List<String> shebeilist, Integer time) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
        String year = sdf1.format(new Date());
        String yearMonth = sdf2.format(new Date());
        List<String> quarters = bhzCementStatisticsMapper.getQuarters();
        String quarter1 = quarters.get(0);
        String quarter2 = quarters.get(1);
        String quarter3 = quarters.get(2);
        QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("strength_rank,SUM(estimate_number) as estimate_number");
        queryWrapper.in("shebei_no", shebeilist);
        //time   =1:本年  =2:本季  =3:本月  =4:本周  =5:当天
        if (ObjectUtil.isEmpty(time) || time == 1) {//本年
            queryWrapper.likeRight("statistics_time", year);
            queryWrapper.groupBy("strength_rank");
        } else if (time == 2) {//本季
            queryWrapper.likeRight("statistics_time", quarter1)
                    .or().likeRight("statistics_time", quarter2)
                    .or().likeRight("statistics_time", quarter3);
            queryWrapper.groupBy("strength_rank");
        } else if (time == 3) {//本月
            queryWrapper.likeRight("statistics_time", yearMonth);
            queryWrapper.groupBy("strength_rank");
        } else if (time == 4) {//本周
            String week1 = getFromToDate(new Date(), 0, 0);//本周周一
            String week7 = getFromToDate(new Date(), 1, 0);//本周周日
            queryWrapper.between("statistics_time", week1, week7);
            queryWrapper.last("group by strength_rank");
        } else if (time == 5) { //当天
            queryWrapper.last("and (TO_DAYS(statistics_time) = TO_DAYS(NOW())) group by strength_rank");
        }
        List<BhzCementStatistics> list = this.list(queryWrapper);
        Map map = new HashMap<>();
        for (BhzCementStatistics bhzCementStatistics : list) {
            String strengthRank = bhzCementStatistics.getStrengthRank();//强度等级
            Double estimateNumber = bhzCementStatistics.getEstimateNumber();//方量
            map.put(strengthRank, estimateNumber);
        }
        return map;
    }

    @Override
    public List<BhzCementStatistics> getTongJiLists(String sysDepartOrgcode, Integer time) {
        List<BhzCementStatistics> list = new ArrayList<>();
        List<BhzCementStatistics> listBY = new ArrayList<>();
        List<String> sbjAllList = new ArrayList<>();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
        String year = sdf1.format(new Date());
        String yearMonth = sdf2.format(new Date());
        List<String> quarters = bhzCementStatisticsMapper.getQuarters();
        String quarter1 = quarters.get(0);
        String quarter2 = quarters.get(1);
        String quarter3 = quarters.get(2);
        List<SysDepart> departList = bhzCementStatisticsMapper.getDepartList(sysDepartOrgcode);//org_category = 7标段列表
        if (ObjectUtil.isNotEmpty(departList)) {
            Integer id = 3;
            for (SysDepart sysDepart : departList) {
                List<String> sbjList = bhzCementStatisticsMapper.getSbjList(sysDepart.getOrgCode());
                if (ObjectUtil.isNotEmpty(sbjList)) {
                    for (String shebei : sbjList) {
                        sbjAllList.add(shebei);
                    }
                    QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
                    queryWrapper.select("strength_rank,SUM(estimate_number) as estimate_number");
                    queryWrapper.in(!(sbjList.size() == 0), "shebei_no", sbjList);
                    //time   =1:本年  =2:本季  =3:本月  =4:本周  =5:当天
                    if (ObjectUtil.isEmpty(time) || time == 1) {//本年
                        queryWrapper.likeRight("statistics_time", year);
                        queryWrapper.groupBy("strength_rank");
                    } else if (time == 2) {//本季
                        queryWrapper.likeRight("statistics_time", quarter1)
                                .or().likeRight("statistics_time", quarter2)
                                .or().likeRight("statistics_time", quarter3);
                        queryWrapper.groupBy("strength_rank");
                    } else if (time == 3) {//本月
                        queryWrapper.likeRight("statistics_time", yearMonth);
                        queryWrapper.groupBy("strength_rank");
                    } else if (time == 4) {//本周
                        String week1 = getFromToDate(new Date(), 0, 0);//本周周一
                        String week7 = getFromToDate(new Date(), 1, 0);//本周周日
                        queryWrapper.between("statistics_time", week1, week7);
                        queryWrapper.last("group by strength_rank");
                    } else if (time == 5) { //当天
                        queryWrapper.last("and (TO_DAYS(statistics_time) = TO_DAYS(NOW())) group by strength_rank");
                    }
                    List<BhzCementStatistics> listBD = this.list(queryWrapper);
                    if (ObjectUtil.isNotEmpty(listBD)) {
                        for (BhzCementStatistics bhzCementStatistics : listBD) {
                            bhzCementStatistics.setId(id);
                            bhzCementStatistics.setProjectName(sysDepart.getDepartName());
                            listBY.add(bhzCementStatistics);
                            id++;
                        }
                    }
                }
            }
            QueryWrapper<BhzCementStatistics> query = new QueryWrapper<>();
            query.select("SUM(estimate_number) as estimate_number");
            query.in("shebei_no", sbjAllList);
            BhzCementStatistics one1 = this.getOne(query);
            one1.setProjectName("开工累计");
            one1.setStrengthRank("/");
            one1.setId(1);
            list.add(one1);
            QueryWrapper<BhzCementStatistics> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("SUM(estimate_number) as estimate_number");
            queryWrapper1.in("shebei_no", sbjAllList);
            BhzCementStatistics bhzCSOne = null;
            //time   =1:本年  =2:本季  =3:本月  =4:本周  =5:当天
            if (ObjectUtil.isEmpty(time) || time == 1) {//本年
                queryWrapper1.likeRight("statistics_time", year);
                bhzCSOne = this.getOne(queryWrapper1);
                bhzCSOne.setProjectName("总方量(本年)");
            } else if (time == 2) {//本季
                queryWrapper1.likeRight("statistics_time", quarter1)
                        .or().likeRight("statistics_time", quarter2)
                        .or().likeRight("statistics_time", quarter3);
                bhzCSOne = this.getOne(queryWrapper1);
                bhzCSOne.setProjectName("总方量(本季)");
            } else if (time == 3) {//本月
                queryWrapper1.likeRight("statistics_time", yearMonth);
                bhzCSOne = this.getOne(queryWrapper1);
                bhzCSOne.setProjectName("总方量(本月)");
            } else if (time == 4) {//本周
                String week1 = getFromToDate(new Date(), 0, 0);//本周周一
                String week7 = getFromToDate(new Date(), 1, 0);//本周周日
                queryWrapper1.between("statistics_time", week1, week7);
                bhzCSOne = this.getOne(queryWrapper1);
                bhzCSOne.setProjectName("总方量(本周)");
            } else if (time == 5) { //当天
                queryWrapper1.last("and (TO_DAYS(statistics_time) = TO_DAYS(NOW()))");
                bhzCSOne = this.getOne(queryWrapper1);
                bhzCSOne.setProjectName("总方量(今天)");
            }
            bhzCSOne.setStrengthRank("/");
            bhzCSOne.setId(2);
            list.add(bhzCSOne);
            for (BhzCementStatistics one : listBY) {
                list.add(one);
            }
        }
        return list;
    }

    /**
     * 根据data时间获取data所在周的周一和周日
     *
     * @param date
     * @param option
     * @param k
     * @return
     */
    private static String getFromToDate(Date date, int option, int k) {
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        //判断是否为星期天,外国定义星期天是他们的第一天
        if (dayOfWeek <= 0) {
            dayOfWeek = 7;
        }
        int offset = 0 == option ? 1 - dayOfWeek : 7 - dayOfWeek;
        int amount = 0 == option ? offset - k * 7 : offset - k * 7;
        calendar.add(Calendar.DATE, amount);
        String format = null;
        try {
            format = SDF.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return format;
    }


}
