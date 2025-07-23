package com.trtm.iot.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.renwudan.entity.RenwudanSchedule;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.entity.StatisticsAndPageVo;
import com.trtm.iot.system.entity.TaskVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 任务单浇筑令
 * @Author:
 * @Date: 2021-05-20
 * @Version: V1.0
 */
public interface IBhzrenwudanService extends IService<Bhzrenwudan> {

    /**
     * 根据任务单编号查询
     *
     * @param code
     * @return
     */
    Bhzrenwudan queryselectone(String code);

    List<Bhzrenwudan> queryselectlist(String code, Integer station, String sysOrgCode);
    List<Bhzrenwudan> queryselectlist1(String code, Integer station, String sysOrgCode);

    Bhzrenwudan selectstation(String code);

    Bhzrenwudan selectstation1(String code,String orgCode);

    Bhzrenwudan selectorderno(String code);

    List<Bhzrenwudan> selectLists(Integer curid);

    List<Shigongphb> getChargerSheetList(Bhzrenwudan task);

    List<RenwudanSchedule>   getTaskSchedule(Bhzrenwudan task);

//    Page<Bhzrenwudan> getSybase(Page<Bhzrenwudan> page, QueryWrapper<Bhzrenwudan> queryWrapper);

    StatisticsAndPageVo getTaskSta(QueryWrapper<Bhzrenwudan> queryWrapper);

    StatisticsAndPageVo getTaskSta(String sysOrgCode);

    List<Bhzrenwudan> getList(QueryWrapper<Bhzrenwudan> queryWrapper);

    List<TaskVo> getTaskToPM(String code);

    StatisticsAndPageVo getTaskSta2(String orgCode, String conspos, String dattim_begin, String dattim_end);

    List<Map> getToDayrwdInfo(String orgCode ,String shebeino );

    List<Bhzrenwudan> queryRWD(String code, Integer station);

    List<Bhzrenwudan> selectrenwudanone(String code, Integer station);

    List<Bhzrenwudan> selectRWDList(String sysOrgCode, Integer curid);
}
