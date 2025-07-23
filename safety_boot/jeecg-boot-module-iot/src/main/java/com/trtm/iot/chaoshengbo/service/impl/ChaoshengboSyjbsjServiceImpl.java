package com.trtm.iot.chaoshengbo.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.chaoshengbo.entity.*;
import com.trtm.iot.chaoshengbo.mapper.*;
import com.trtm.iot.chaoshengbo.service.IChaoshengboSyjbsjService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.kongjingbasicinfo.entity.KongjingBasicinfo;
import com.trtm.iot.syj.entity.FsYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: chaoshengbo_syjbsj
 * @Author: jeecg-boot
 * @Date: 2021-04-08
 * @Version: V1.0
 */
@Service
public class ChaoshengboSyjbsjServiceImpl extends ServiceImpl<ChaoshengboSyjbsjMapper, ChaoshengboSyjbsj> implements IChaoshengboSyjbsjService {

    @Autowired
    private IChaoshengboSyjbsjService iChaoshengboSyjbsjService;
    @Resource
    private ChaoshengboSyjbsjMapper chaoshengboSyjbsjMapper;
    @Resource
    private ChaoshengboSybltsjMapper chaoshengboSybltsjMapper;
    @Resource
    private ChaoshengboSybsjMapper chaoshengboSybsjMapper;
    @Resource
    private ChaoshengboSydsjMapper chaoshengboSydsjMapper;
    @Resource
    private ChaoshengboSyjsbMapper chaoshengboSyjsbMapper;
    @Resource
    private ChaoshengboSyljzsMapper chaoshengboSyljzsMapper;
    @Resource
    private ChaoshengboSyqxsjMapper chaoshengboSyqxsjMapper;

    @Override
    @Transactional
    public int saveMain(ChaoshengboSyjbsjs chaoshengboSyjbsj, List<ChaoshengboSybltsj> chaoshengboSybltsjList, List<ChaoshengboSybsj> chaoshengboSybsjList, List<ChaoshengboSydsj> chaoshengboSydsjList, List<ChaoshengboSyjsb> chaoshengboSyjsbList, List<ChaoshengboSyljzs> chaoshengboSyljzsList, List<ChaoshengboSyqxsj> chaoshengboSyqxsjList) {
        LambdaQueryWrapper<ChaoshengboSyjbsj> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChaoshengboSyjbsj::getShebeino, chaoshengboSyjbsj.getShebeino());
        queryWrapper.eq(ChaoshengboSyjbsj::getLiushuihao, chaoshengboSyjbsj.getLiushuihao());
        queryWrapper.eq(ChaoshengboSyjbsj::getShizhuangno, chaoshengboSyjbsj.getShizhuangno());
        ChaoshengboSyjbsj one = this.getOne(queryWrapper);
        if (one == null) {
            chaoshengboSyjbsjMapper.insertjbsj(chaoshengboSyjbsj);
            if (chaoshengboSybltsjList != null && chaoshengboSybltsjList.size() > 0) {
                for (ChaoshengboSybltsj entity : chaoshengboSybltsjList) {
                    //外键设置
                    entity.setShebeino(chaoshengboSyjbsj.getShebeino());
                    entity.setLiushuihao(chaoshengboSyjbsj.getLiushuihao());
                    entity.setShizhuangno(chaoshengboSyjbsj.getShizhuangno());
                    chaoshengboSybltsjMapper.insert(entity);
                }
            }
            if (chaoshengboSybsjList != null && chaoshengboSybsjList.size() > 0) {
                for (ChaoshengboSybsj entity : chaoshengboSybsjList) {
                    //外键设置
                    entity.setShebeino(chaoshengboSyjbsj.getShebeino());
                    entity.setLiushuihao(chaoshengboSyjbsj.getLiushuihao());
                    entity.setShizhuangno(chaoshengboSyjbsj.getShizhuangno());
                    chaoshengboSybsjMapper.insert(entity);
                }
            }
            if (chaoshengboSydsjList != null && chaoshengboSydsjList.size() > 0) {
                for (ChaoshengboSydsj entity : chaoshengboSydsjList) {
                    //外键设置
                    entity.setShebeino(chaoshengboSyjbsj.getShebeino());
                    entity.setLiushuihao(chaoshengboSyjbsj.getLiushuihao());
                    entity.setShizhuangno(chaoshengboSyjbsj.getShizhuangno());
                    chaoshengboSydsjMapper.insert(entity);
                }
            }
            if (chaoshengboSyjsbList != null && chaoshengboSyjsbList.size() > 0) {
                for (ChaoshengboSyjsb entity : chaoshengboSyjsbList) {
                    //外键设置
                    entity.setShebeino(chaoshengboSyjbsj.getShebeino());
                    entity.setLiushuihao(chaoshengboSyjbsj.getLiushuihao());
                    entity.setShizhuangno(chaoshengboSyjbsj.getShizhuangno());
                    chaoshengboSyjsbMapper.insert(entity);
                }
            }
            if (chaoshengboSyljzsList != null && chaoshengboSyljzsList.size() > 0) {
                for (ChaoshengboSyljzs entity : chaoshengboSyljzsList) {
                    //外键设置
                    entity.setShebeino(chaoshengboSyjbsj.getShebeino());
                    entity.setLiushuihao(chaoshengboSyjbsj.getLiushuihao());
                    entity.setShizhuangno(chaoshengboSyjbsj.getShizhuangno());
                    chaoshengboSyljzsMapper.insert(entity);
                }
            }
            if (chaoshengboSyqxsjList != null && chaoshengboSyqxsjList.size() > 0) {
                for (ChaoshengboSyqxsj entity : chaoshengboSyqxsjList) {
                    //外键设置
                    entity.setShebeino(chaoshengboSyjbsj.getShebeino());
                    entity.setLiushuihao(chaoshengboSyjbsj.getLiushuihao());
                    entity.setShizhuangno(chaoshengboSyjbsj.getShizhuangno());
                    chaoshengboSyqxsjMapper.insert(entity);
                }
            }
            return 200;
        }
        // 分包用
//        else if (one.getIfinish() == 0) {
//            if (chaoshengboSybltsjList != null && chaoshengboSybltsjList.size() > 0) {
//                for (ChaoshengboSybltsj entity : chaoshengboSybltsjList) {
//                    //外键设置
//                    entity.setShebeino(chaoshengboSyjbsj.getShebeino());
//                    entity.setLiushuihao(chaoshengboSyjbsj.getLiushuihao());
//                    entity.setShizhuangno(chaoshengboSyjbsj.getShizhuangno());
//                    chaoshengboSybltsjMapper.insert(entity);
//                }
//            }
//            if (chaoshengboSybsjList != null && chaoshengboSybsjList.size() > 0) {
//                for (ChaoshengboSybsj entity : chaoshengboSybsjList) {
//                    //外键设置
//                    entity.setShebeino(chaoshengboSyjbsj.getShebeino());
//                    entity.setLiushuihao(chaoshengboSyjbsj.getLiushuihao());
//                    entity.setShizhuangno(chaoshengboSyjbsj.getShizhuangno());
//                    chaoshengboSybsjMapper.insert(entity);
//                }
//            }
//            if (chaoshengboSydsjList != null && chaoshengboSydsjList.size() > 0) {
//                for (ChaoshengboSydsj entity : chaoshengboSydsjList) {
//                    //外键设置
//                    entity.setShebeino(chaoshengboSyjbsj.getShebeino());
//                    entity.setLiushuihao(chaoshengboSyjbsj.getLiushuihao());
//                    entity.setShizhuangno(chaoshengboSyjbsj.getShizhuangno());
//                    chaoshengboSydsjMapper.insert(entity);
//                }
//            }
//            if (chaoshengboSyjsbList != null && chaoshengboSyjsbList.size() > 0) {
//                for (ChaoshengboSyjsb entity : chaoshengboSyjsbList) {
//                    //外键设置
//                    entity.setShebeino(chaoshengboSyjbsj.getShebeino());
//                    entity.setLiushuihao(chaoshengboSyjbsj.getLiushuihao());
//                    entity.setShizhuangno(chaoshengboSyjbsj.getShizhuangno());
//                    chaoshengboSyjsbMapper.insert(entity);
//                }
//            }
//            if (chaoshengboSyljzsList != null && chaoshengboSyljzsList.size() > 0) {
//                for (ChaoshengboSyljzs entity : chaoshengboSyljzsList) {
//                    //外键设置
//                    entity.setShebeino(chaoshengboSyjbsj.getShebeino());
//                    entity.setLiushuihao(chaoshengboSyjbsj.getLiushuihao());
//                    entity.setShizhuangno(chaoshengboSyjbsj.getShizhuangno());
//                    chaoshengboSyljzsMapper.insert(entity);
//                }
//            }
//            if (chaoshengboSyqxsjList != null && chaoshengboSyqxsjList.size() > 0) {
//                for (ChaoshengboSyqxsj entity : chaoshengboSyqxsjList) {
//                    //外键设置
//                    entity.setShebeino(chaoshengboSyjbsj.getShebeino());
//                    entity.setLiushuihao(chaoshengboSyjbsj.getLiushuihao());
//                    entity.setShizhuangno(chaoshengboSyjbsj.getShizhuangno());
//                    chaoshengboSyqxsjMapper.insert(entity);
//                }
//            }
//            return 200;
//        }
        else {
            return 500;
        }
    }

    @Override
    public List<ChaoshengboSyjbsj> selectList(String liushuihao) {
        try {
            QueryWrapper<ChaoshengboSyjbsj> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("liushuihao", liushuihao);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ChaoshengboSyjbsj> selectLists(String shebeino, Integer id) {
        return chaoshengboSyjbsjMapper.selectLists(shebeino, id);
    }

    @Override
    public void updateByIdToYb(Integer level, String file, String fileInfo, Integer id) {
        chaoshengboSyjbsjMapper.updateByIdToYb(level, file, fileInfo, id);
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

        QueryWrapper<ChaoshengboSyjbsj> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("shebeino", shebeilist);
        queryWrapper1.eq("level", 1);
        QueryWrapper<ChaoshengboSyjbsj> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.in("shebeino", shebeilist);
        queryWrapper2.eq("level", 2);
        QueryWrapper<ChaoshengboSyjbsj> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.in("shebeino", shebeilist);
        queryWrapper3.eq("level", 3);
        if (date == 0 || ObjectUtil.isEmpty(date)) {//年
            queryWrapper1.eq("YEAR(ceshitime)", year);
            queryWrapper2.eq("YEAR(ceshitime)", year);
            queryWrapper3.eq("YEAR(ceshitime)", year);
        } else if (date == 1) {//季
            queryWrapper1.last("and QUARTER(ceshitime)=QUARTER(now())");
            queryWrapper2.last("and QUARTER(ceshitime)=QUARTER(now())");
            queryWrapper3.last("and QUARTER(ceshitime)=QUARTER(now())");
        } else if (date == 2) {//月
            queryWrapper1.likeRight("ceshitime", yearMonth);
            queryWrapper2.likeRight("ceshitime", yearMonth);
            queryWrapper3.likeRight("ceshitime", yearMonth);
        } else if (date == 3) {//周
            queryWrapper1.eq("MONTH(ceshitime)", month);
            queryWrapper1.last("AND WEEK(ceshitime) = WEEK(CURDATE())");
            queryWrapper2.eq("MONTH(ceshitime)", month);
            queryWrapper2.last("AND WEEK(ceshitime) = WEEK(CURDATE())");
            queryWrapper3.eq("MONTH(ceshitime)", month);
            queryWrapper3.last("AND WEEK(ceshitime) = WEEK(CURDATE())");
        } else if (date == 4) {//上一年
            queryWrapper1.eq("YEAR(ceshitime)", String.valueOf(Integer.parseInt(year) - 1));
            queryWrapper2.eq("YEAR(ceshitime)", String.valueOf(Integer.parseInt(year) - 1));
            queryWrapper3.eq("YEAR(ceshitime)", String.valueOf(Integer.parseInt(year) - 1));
        }

        List<ChaoshengboSyjbsj> level1List = this.list(queryWrapper1);
        List<ChaoshengboSyjbsj> level2List = this.list(queryWrapper2);
        List<ChaoshengboSyjbsj> level3List = this.list(queryWrapper3);

        Map map = new HashMap<>();
        map.put("level1", level1List.size());
        map.put("level2", level2List.size());
        map.put("level3", level3List.size());
        return map;
    }

    @Override
    public List tongjiZJLevel(List<String> shebeilist, Integer date) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        String year = sdf1.format(new Date());

        QueryWrapper<ChaoshengboSyjbsj> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("shebeino", shebeilist);
        if (date == 0 || ObjectUtil.isEmpty(date)) {//年
            queryWrapper.select("COUNT( CASE WHEN level = 1 THEN 1 ELSE NULL END ) AS level,\n" +
                    "\tCOUNT( CASE WHEN level = 2 THEN 1 ELSE NULL END ) AS file,\n" +
                    "\tCOUNT( CASE WHEN level = 3 THEN 1 ELSE NULL END ) AS file_info", "ceshitime");
            queryWrapper.last("GROUP BY (SELECT DATE_FORMAT(ceshitime,'%Y'))");
        } else if (date == 1) {//季
            queryWrapper.select("COUNT( CASE WHEN level = 1 THEN 1 ELSE NULL END ) AS level,\n" +
                    "\tCOUNT( CASE WHEN level = 2 THEN 1 ELSE NULL END ) AS file,\n" +
                    "\tCOUNT( CASE WHEN level = 3 THEN 1 ELSE NULL END ) AS file_info", "FLOOR((DATE_FORMAT(ceshitime,'%m')-1)/3)+1 as code");
            queryWrapper.last("and ceshitime like '" + year + "%'  GROUP BY (SELECT FLOOR((DATE_FORMAT(ceshitime,'%m')-1)/3)+1)");
        } else if (date == 2) {//月
            queryWrapper.select("COUNT( CASE WHEN level = 1 THEN 1 ELSE NULL END ) AS level,\n" +
                    "\tCOUNT( CASE WHEN level = 2 THEN 1 ELSE NULL END ) AS file,\n" +
                    "\tCOUNT( CASE WHEN level = 3 THEN 1 ELSE NULL END ) AS file_info", "ceshitime");
            queryWrapper.last("and ceshitime like '" + year + "%'  GROUP BY (SELECT DATE_FORMAT(ceshitime,'%Y-%m'))");
        } else if (date == 3) {//周
            queryWrapper.select("COUNT( CASE WHEN level = 1 THEN 1 ELSE NULL END ) AS level,\n" +
                    "\tCOUNT( CASE WHEN level = 2 THEN 1 ELSE NULL END ) AS file,\n" +
                    "\tCOUNT( CASE WHEN level = 3 THEN 1 ELSE NULL END ) AS file_info", "DATE_FORMAT(ceshitime,'第%u周') as code");
            queryWrapper.last("and ceshitime like '" + year + "%'  GROUP BY (SELECT DATE_FORMAT(ceshitime,'%Y%u'))");
        } else if (date == 4) {//上一年
            queryWrapper.select("COUNT( CASE WHEN level = 1 THEN 1 ELSE NULL END ) AS level,\n" +
                    "\tCOUNT( CASE WHEN level = 2 THEN 1 ELSE NULL END ) AS file,\n" +
                    "\tCOUNT( CASE WHEN level = 3 THEN 1 ELSE NULL END ) AS file_info", "ceshitime");
            queryWrapper.likeRight("ceshitime",String.valueOf(Integer.parseInt(year) - 1));
        }
        List<ChaoshengboSyjbsj> zjList = this.list(queryWrapper);
        List list = new ArrayList<>();
        for (ChaoshengboSyjbsj zj : zjList) {
            Map map = new HashMap();
            String format1 = "";
            Integer level1 = zj.getLevel();
            Integer level2 = Integer.parseInt(zj.getFile());
            Integer level3 = Integer.parseInt(zj.getFileInfo());
            if (date == 0 || ObjectUtil.isEmpty(date)) {
                Date ceshitime = zj.getCeshitime();
                format1 = sdf1.format(ceshitime);
            } else if (date == 1) {
                format1 = String.valueOf(zj.getCode());
            } else if (date == 2) {
                Date ceshitime = zj.getCeshitime();
                format1 = sdf2.format(ceshitime);
            } else if (date == 3) {
                format1 = zj.getCode();
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
            map.put("ceshitime", format1);
            list.add(map);
        }
        return list;
    }
}
