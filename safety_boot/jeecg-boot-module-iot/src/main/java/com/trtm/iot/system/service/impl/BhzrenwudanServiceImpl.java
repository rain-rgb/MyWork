package com.trtm.iot.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.renwudan.entity.RenwudanSchedule;
import com.trtm.iot.renwudan.mapper.RenwudanScheduleMapper;
import com.trtm.iot.system.entity.*;
import com.trtm.iot.system.mapper.BhzrenwudanMapper;
import com.trtm.iot.system.mapper.ShigongphbMapper;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 任务单浇筑令
 * @Author: jeecg-boot
 * @Date: 2021-05-20
 * @Version: V1.0
 */
@Service
public class BhzrenwudanServiceImpl extends ServiceImpl<BhzrenwudanMapper, Bhzrenwudan> implements IBhzrenwudanService {

    @Resource
    private BhzrenwudanMapper bhzrenwudanMapper;

    @Resource
    private ShigongphbMapper chargerSheetMapper;

    @Resource
    private RenwudanScheduleMapper taskScheduleMapper;
    @Resource
    private IShebeiInfoService shebeiInfoService;

    @Override
    public Bhzrenwudan queryselectone(String code) {
        try {
            QueryWrapper<Bhzrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("code", code);
            queryWrapper.eq("isdel", 0);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Bhzrenwudan> queryselectlist(String code, Integer station, String sysOrgCode) {
//        try {
//            QueryWrapper<Bhzrenwudan> queryWrapper = new QueryWrapper<>();
//            if(station<3){
//                queryWrapper.eq("station",station);
//            }
//            queryWrapper.eq("Code", code);
//            queryWrapper.eq("isdel", 0);
//            return this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return bhzrenwudanMapper.queryselectlist(code, station, sysOrgCode);
    }

    @Override
    public List<Bhzrenwudan> queryselectlist1(String code, Integer station, String sysOrgCode) {
        return bhzrenwudanMapper.queryselectlist1(code, station, sysOrgCode);
    }

    @Override
    public Bhzrenwudan selectstation(String code) {
        try {
            QueryWrapper<Bhzrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("Code", code);
            queryWrapper.eq("isdel", 0);
            queryWrapper.last("limit 1");
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Bhzrenwudan selectstation1(String code, String orgCode) {
        try {
            QueryWrapper<Bhzrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("Code", code);
            queryWrapper.likeRight("sys_org_code", orgCode);
            queryWrapper.eq("isdel", 0);
            queryWrapper.last("limit 1");
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Bhzrenwudan selectorderno(String code) {
        try {
            QueryWrapper<Bhzrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("orderno", code);
            queryWrapper.eq("isdel", 0);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Bhzrenwudan> selectLists(Integer curid) {
        return bhzrenwudanMapper.selectLists(curid);
    }

    @Override
    public List<Shigongphb> getChargerSheetList(Bhzrenwudan task) {
        QueryWrapper<Shigongphb> cSQw = new QueryWrapper<>();
        cSQw.eq("renwudan", task.getCode());
        cSQw.like(task.getSysOrgCode() != null && !"".equals(task.getSysOrgCode()), "sys_org_code", task.getSysOrgCode());
        List<Shigongphb> cSList = chargerSheetMapper.selectList(cSQw);
        return cSList;
    }

    @Override
    public List<RenwudanSchedule> getTaskSchedule(Bhzrenwudan task) {
        QueryWrapper<RenwudanSchedule> tSQw = new QueryWrapper<>();
        tSQw.eq("Code", task.getCode());
        tSQw.like(task.getSysOrgCode() != null && !"".equals(task.getSysOrgCode()), "sys_org_code", task.getSysOrgCode());
        List<RenwudanSchedule> taskSchedule = taskScheduleMapper.selectList(tSQw);
        for (RenwudanSchedule renwudanSchedule : taskSchedule) {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(renwudanSchedule.getShebeiNo());
            renwudanSchedule.setShebeiNo(selectshebeione.getSbname());
        }
        return taskSchedule;
    }

//    @Override
//    public Page<Bhzrenwudan> getSybase(Page<Bhzrenwudan> page, QueryWrapper<Bhzrenwudan> queryWrapper) {
//        Page<Bhzrenwudan> result = bhzrenwudanMapper.selectPage(page, queryWrapper);
//        List<Bhzrenwudan> records = result.getRecords();
//        if (records != null && records.size() > 0) {
//            for (Bhzrenwudan item : records) {
//                try {
//                    RenwudanSchedule tS = getTaskSchedule(item);
//                    item.setTS(tS);
//                    Integer status = item.getStatus();
//                    if (status == 1) {
//                        List<Shigongphb> csList = getChargerSheetList(item);
//                        if (csList != null && csList.size() > 0) {
//                            double finish = 100.00;
//                            boolean isAfter = item.getEndtim().after(new Date());
//                            if (tS != null) {
//                                if (tS.getBfb() == finish) {
//                                    item.setStatus(5);//已完成
//                                } else if (isAfter) {
//                                    item.setStatus(4);//生产中
//                                } else if (!isAfter && tS.getBfb() < finish) {
//                                    item.setStatus(6); //已滞后
//                                }
//                            } else if (tS == null) {
//                                item.setStatus(3); //配料未生产
//                            }
//                        } else {
//                            item.setStatus(2); //审核未配料
//                        }
//                    }
//                    if (tS != null) {
//                        if (tS.getMetes() != null && item.getMete() != null) {
//                            item.setDValue(tS.getMetes() - item.getMete());
//                        } else {
//                            item.setDValue(0.00);
//                        }
//                    } else {
//                        item.setDValue(0.00);
//                    }
//                } catch (Exception exception) {
//                    log.error("任务单编号为：" + item.getCode() + "，浇筑令分页查询异常：" + exception.getMessage());
//                    continue;
//                }
//            }
//        }
//        result.setRecords(records);
//        return result;
//    }

    /**
     * 浇筑令统计（暂时弃用）
     *
     * @param queryWrapper
     * @return
     */
    @Override
    public StatisticsAndPageVo getTaskSta(QueryWrapper<Bhzrenwudan> queryWrapper) {
        int done = 0;
        int inProduction = 0;
        int lag = 0;
        int ingredientsNotProduced = 0;
        int reviewNotIngredients = 0;
        int unreviewed = 0;
        StatisticsAndPageVo vo = new StatisticsAndPageVo();
        List<Bhzrenwudan> list = bhzrenwudanMapper.selectList(queryWrapper);
        if (list != null && list.size() > 0) {
            for (Bhzrenwudan item : list) {
                QueryWrapper<RenwudanSchedule> tSQw = new QueryWrapper<>();
                tSQw.eq("Code", item.getCode());
                RenwudanSchedule tS = taskScheduleMapper.selectOne(tSQw);
                Integer status = item.getStatus();
                if (status == 1) {
                    QueryWrapper<Shigongphb> cSQw = new QueryWrapper<>();
                    cSQw.eq("renwudan", item.getCode());
                    List<Shigongphb> csList = chargerSheetMapper.selectList(cSQw);
                    if (csList != null && csList.size() > 0) {
                        boolean isAfter = item.getEndtim().after(new Date());
                        if (tS != null && isAfter) {
                            double finish = 100.00;
                            if (tS.getBfb() == finish) {
                                item.setStatus(5);//已完成
                                done++;
                            } else {
                                item.setStatus(4);//生产中
                                inProduction++;
                            }
                        } else if (tS != null && !isAfter) {
                            item.setStatus(6); //已滞后
                            lag++;
                        } else {
                            item.setStatus(3); //配料未生产
                            ingredientsNotProduced++;
                        }
                    } else {
                        item.setStatus(2); //审核未配料
                        reviewNotIngredients++;
                    }
                } else {
                    unreviewed++; //未审核
                }
            }
        }
        vo.setDone(done);
        vo.setInProduction(inProduction);
        vo.setLag(lag);
        vo.setUnreviewed(unreviewed);
        vo.setReviewNotIngredients(reviewNotIngredients);
        vo.setIngredientsNotProduced(ingredientsNotProduced);
        return vo;
    }

    /**
     * 浇筑令统计
     *
     * @param sysOrgCode
     * @return
     */
    @Override
    public StatisticsAndPageVo getTaskSta(String sysOrgCode) {
        return baseMapper.getTaskStaBySysOrgCode(sysOrgCode);
    }

    @Override
    public List<Bhzrenwudan> getList(QueryWrapper<Bhzrenwudan> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<TaskVo> getTaskToPM(String code) {
        return baseMapper.getTaskToPM(code);
    }

    @Override
    public StatisticsAndPageVo getTaskSta2(String orgCode, String conspos, String dattim_begin, String dattim_end) {
        return baseMapper.getTaskSta2(orgCode, conspos, dattim_begin, dattim_end);
    }

    @Override
    public List<Map> getToDayrwdInfo(String orgCode, String shebeino) {
        return baseMapper.getToDayrwdInfo(orgCode, shebeino);
    }

    @Override
    public List<Bhzrenwudan> queryRWD(String code, Integer station) {
        try {
            QueryWrapper<Bhzrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("code", code);
            queryWrapper.eq("station", station);
            queryWrapper.eq("isdel", 0);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Bhzrenwudan> selectrenwudanone(String code, Integer station) {
        try {
            QueryWrapper<Bhzrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("Code", code);
            queryWrapper.ne("Customer","");
            queryWrapper.isNotNull("Customer");
            queryWrapper.eq("isdel", 0);
            queryWrapper.groupBy("Code");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Bhzrenwudan> selectRWDList(String sysOrgCode, Integer curid) {
        try {
            QueryWrapper<Bhzrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.likeRight("sys_org_code",sysOrgCode);
            queryWrapper.gt("id", curid);
            queryWrapper.eq("isdel", 0);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

