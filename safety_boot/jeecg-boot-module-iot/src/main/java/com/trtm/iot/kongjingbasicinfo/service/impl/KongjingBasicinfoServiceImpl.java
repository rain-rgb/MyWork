package com.trtm.iot.kongjingbasicinfo.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.chaoshengbo.entity.*;
import com.trtm.iot.kongjinganalysisdata.entity.KongjingAnalysisdata;
import com.trtm.iot.kongjinganalysisdata.mapper.KongjingAnalysisdataMapper;
import com.trtm.iot.kongjingbasicinfo.entity.KongjingBasicinfo;
import com.trtm.iot.kongjingbasicinfo.mapper.KongjingBasicinfoMapper;
import com.trtm.iot.kongjingbasicinfo.service.IKongjingBasicinfoService;
import com.trtm.iot.kongjingpointsdata.entity.KongjingPointsdata;
import com.trtm.iot.kongjingpointsdata.mapper.KongjingPointsdataMapper;
import com.trtm.iot.kongjingtongdao.entity.KongjingTongdao;
import com.trtm.iot.kongjingtongdao.mapper.KongjingTongdaoMapper;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 孔径基本信息数据
 * @Author: jeecg-boot
 * @Date: 2022-03-01
 * @Version: V1.0
 */
@Service
public class KongjingBasicinfoServiceImpl extends ServiceImpl<KongjingBasicinfoMapper, KongjingBasicinfo> implements IKongjingBasicinfoService {

    @Resource
    KongjingBasicinfoMapper kongjingBasicinfoMapper;
    @Resource
    KongjingTongdaoMapper kongjingTongdaoMapper;
    @Resource
    KongjingAnalysisdataMapper kongjingAnalysisdataMapper;
    @Resource
    KongjingPointsdataMapper kongjingPointsdataMapper;

    @Override
    @Transactional
    public int saveMain(KongjingBasicinfo kongjingBasicinfo, List<KongjingTongdao> kongjingTongdaoList, List<KongjingPointsdata> kongjingPointsdataList, List<KongjingAnalysisdata> kongjingAnalysisdataList) {
        LambdaQueryWrapper<KongjingBasicinfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KongjingBasicinfo::getBasicinfoid, kongjingBasicinfo.getBasicinfoid());
        KongjingBasicinfo one = this.getOne(queryWrapper);
        if (one == null) {
            kongjingBasicinfoMapper.insert(kongjingBasicinfo);
            if (kongjingTongdaoList != null && kongjingTongdaoList.size() > 0) {
                for (KongjingTongdao entity : kongjingTongdaoList) {
                    //外键设置
                    entity.setGuid(kongjingBasicinfo.getGuid());
                    kongjingTongdaoMapper.insert(entity);
                }
            }
            if (kongjingPointsdataList != null && kongjingPointsdataList.size() > 0) {
                for (KongjingPointsdata entity : kongjingPointsdataList) {
                    //外键设置
                    entity.setBasicinfoid(kongjingBasicinfo.getBasicinfoid());
                    kongjingPointsdataMapper.insert(entity);
                }
            }
            if (kongjingAnalysisdataList != null && kongjingAnalysisdataList.size() > 0) {
                for (KongjingAnalysisdata entity : kongjingAnalysisdataList) {
                    //外键设置
                    entity.setBasicinfoid(kongjingBasicinfo.getBasicinfoid());
                    kongjingAnalysisdataMapper.insert(entity);
                }
            }
            return 200;
        } else {
            return 500;
        }
    }

    @Override
    public Map statisticsLevelZB(List<String> shebeilist, Integer date) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        String year = sdf1.format(new Date());
        String yearMonth = sdf2.format(new Date());
        String[] split = yearMonth.split("-");
        String month = split[split.length - 1];
        String day = sdf3.format(new Date());

        QueryWrapper<KongjingBasicinfo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("shebeino", shebeilist);
        queryWrapper1.eq("level", 1);
        QueryWrapper<KongjingBasicinfo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.in("shebeino", shebeilist);
        queryWrapper2.eq("level", 2);
        QueryWrapper<KongjingBasicinfo> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.in("shebeino", shebeilist);
        queryWrapper3.eq("level", 3);
        if (date == 0 || ObjectUtil.isEmpty(date)) {//年
            queryWrapper1.eq("YEAR(testPiledate)", year);
            queryWrapper2.eq("YEAR(testPiledate)", year);
            queryWrapper2.eq("YEAR(testPiledate)", year);
        } else if (date == 1) {//季
            queryWrapper1.last("and QUARTER(testPiledate)=QUARTER(now())");
            queryWrapper2.last("and QUARTER(testPiledate)=QUARTER(now())");
            queryWrapper3.last("and QUARTER(testPiledate)=QUARTER(now())");
        } else if (date == 2) {//月
            queryWrapper1.likeRight("testPiledate", yearMonth);
            queryWrapper2.likeRight("testPiledate", yearMonth);
            queryWrapper3.likeRight("testPiledate", yearMonth);
        } else if (date == 3) {//周
            queryWrapper1.eq("MONTH(testPiledate)", month);
            queryWrapper1.last("AND WEEK(testPiledate) = WEEK(CURDATE())");
            queryWrapper2.eq("MONTH(testPiledate)", month);
            queryWrapper2.last("AND WEEK(testPiledate) = WEEK(CURDATE())");
            queryWrapper3.eq("MONTH(testPiledate)", month);
            queryWrapper3.last("AND WEEK(testPiledate) = WEEK(CURDATE())");
        } else if (date == 4) {
            queryWrapper1.eq("YEAR(testPiledate)", String.valueOf(Integer.parseInt(year) - 1));
            queryWrapper2.eq("YEAR(testPiledate)", String.valueOf(Integer.parseInt(year) - 1));
            queryWrapper2.eq("YEAR(testPiledate)", String.valueOf(Integer.parseInt(year) - 1));
        }

        List<KongjingBasicinfo> level1List = this.list(queryWrapper1);
        List<KongjingBasicinfo> level2List = this.list(queryWrapper2);
        List<KongjingBasicinfo> level3List = this.list(queryWrapper3);

        Map map = new HashMap<>();
        map.put("level1", level1List.size());
        map.put("level2", level2List.size());
        map.put("level3", level3List.size());
        return map;
    }

    @Override
    public List tongjiKJLevel(List<String> shebeilist, Integer date) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf3 = new SimpleDateFormat("MM");
        String year = sdf1.format(new Date());

        QueryWrapper<KongjingBasicinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("shebeino", shebeilist);
        if (date == 0 || ObjectUtil.isEmpty(date)) {//年
            queryWrapper.select("COUNT( CASE WHEN level = 1 THEN 1 ELSE NULL END ) AS level,\n" +
                    "\tCOUNT( CASE WHEN level = 2 THEN 1 ELSE NULL END ) AS file,\n" +
                    "\tCOUNT( CASE WHEN level = 3 THEN 1 ELSE NULL END ) AS file_info", "testPiledate");
            queryWrapper.last("GROUP BY (SELECT DATE_FORMAT(testPiledate,'%Y'))");
        } else if (date == 1) {//季
            queryWrapper.select("COUNT( CASE WHEN level = 1 THEN 1 ELSE NULL END ) AS level,\n" +
                    "\tCOUNT( CASE WHEN level = 2 THEN 1 ELSE NULL END ) AS file,\n" +
                    "\tCOUNT( CASE WHEN level = 3 THEN 1 ELSE NULL END ) AS file_info", "FLOOR((DATE_FORMAT(testPiledate,'%m')-1)/3)+1 as detloc");
            queryWrapper.last("and testPiledate like '" + year + "%'  GROUP BY (SELECT FLOOR((DATE_FORMAT(testPiledate,'%m')-1)/3)+1)");
        } else if (date == 2) {//月
            queryWrapper.select("COUNT( CASE WHEN level = 1 THEN 1 ELSE NULL END ) AS level,\n" +
                    "\tCOUNT( CASE WHEN level = 2 THEN 1 ELSE NULL END ) AS file,\n" +
                    "\tCOUNT( CASE WHEN level = 3 THEN 1 ELSE NULL END ) AS file_info", "testPiledate");
            queryWrapper.last("and testPiledate like '" + year + "%'  GROUP BY (SELECT DATE_FORMAT(testPiledate,'%Y-%m'))");
        } else if (date == 3) {//周
            queryWrapper.select("COUNT( CASE WHEN level = 1 THEN 1 ELSE NULL END ) AS level,\n" +
                    "\tCOUNT( CASE WHEN level = 2 THEN 1 ELSE NULL END ) AS file,\n" +
                    "\tCOUNT( CASE WHEN level = 3 THEN 1 ELSE NULL END ) AS file_info", "DATE_FORMAT(testPiledate,'第%u周') as detloc");
            queryWrapper.last("and testPiledate like '" + year + "%'  GROUP BY (SELECT DATE_FORMAT(testPiledate,'%Y%u'))");
        }else if (date ==4){
            queryWrapper.select("COUNT( CASE WHEN level = 1 THEN 1 ELSE NULL END ) AS level,\n" +
                    "\tCOUNT( CASE WHEN level = 2 THEN 1 ELSE NULL END ) AS file,\n" +
                    "\tCOUNT( CASE WHEN level = 3 THEN 1 ELSE NULL END ) AS file_info", "testPiledate");
            queryWrapper.last("GROUP BY (SELECT DATE_FORMAT(testPiledate,'"+ String.valueOf(Integer.parseInt(year) - 1)+"'))");
        }
        List<KongjingBasicinfo> basicInfos = this.list(queryWrapper);
        List list = new ArrayList<>();
        for (KongjingBasicinfo basicInfo : basicInfos) {
            Map map = new HashMap();
            String format1 = "";
            Integer level1 = basicInfo.getLevel();
            Integer level2 = Integer.parseInt(basicInfo.getFile());
            Integer level3 = Integer.parseInt(basicInfo.getFileInfo());
            if (date == 0 || ObjectUtil.isEmpty(date)) {
                String testpiledate = basicInfo.getTestpiledate();
                Date testPileDate = null;
                try {
                    testPileDate = sdf2.parse(testpiledate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                format1 = sdf1.format(testPileDate);
            } else if (date == 1) {
                format1 = String.valueOf(basicInfo.getDetloc());
            } else if (date == 2) {
                String testpiledate = basicInfo.getTestpiledate();
                Date testPileDate = null;
                try {
                    testPileDate = sdf2.parse(testpiledate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                format1 = sdf3.format(testPileDate);
            } else if (date == 3) {
                format1 = basicInfo.getDetloc();
            }
            if (ObjectUtil.isEmpty(level1)) {
                level1 = 0;
            }
            if (ObjectUtil.isEmpty(level2)) {
                level2 = 0;
            }
            if (ObjectUtil.isEmpty(level3)) {
                level3 = 0;
            }
            map.put("level1", level1);
            map.put("level2", level2);
            map.put("level3", level3);
            map.put("testpiledate", format1);
            list.add(map);
        }
        return list;
    }
}
