package com.trtm.iot.renwudan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.renwudan.entity.RenwudanSchedule;
import com.trtm.iot.renwudan.mapper.RenwudanScheduleMapper;
import com.trtm.iot.renwudan.service.IRenwudanScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 拌合站任务单进度
 * @Author: jeecg-boot
 * @Date:   2021-06-16
 * @Version: V1.0
 */
@Service
public class RenwudanScheduleServiceImpl extends ServiceImpl<RenwudanScheduleMapper, RenwudanSchedule> implements IRenwudanScheduleService {
    @Autowired
    private RenwudanScheduleMapper renwudanScheduleMapper;
    @Override
    public RenwudanSchedule queryone(String code) {
        try {
            QueryWrapper<RenwudanSchedule> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("code",code);
            queryWrapper.eq("isdel",0);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RenwudanSchedule queryones(String code, String shebei_no) {
//        try {
//            QueryWrapper<RenwudanSchedule> queryWrapper=new QueryWrapper<>();
//            queryWrapper.eq("code",code);
//            queryWrapper.eq("shebei_no",shebei_no);
//            queryWrapper.eq("isdel",0);
//            return this.getOne(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return renwudanScheduleMapper.queryones(code,shebei_no);
    }

    @Override
    public int updateone(double metes, double bfb, Integer id, Date endtime, Integer dishCount) {
        return renwudanScheduleMapper.updatealertsate(metes,bfb,id,endtime,dishCount);
    }

    @Override
    public int updateonezt(double metes, double bfb, Integer id, Date endtime, Integer dishCount, String conspos) {
        return renwudanScheduleMapper.updateonezt(metes,bfb,id,endtime,dishCount,conspos);
    }

    @Override
    public List<RenwudanSchedule> selectrwdschedule(String code, String shebeiNo) {
        return renwudanScheduleMapper.selectrwdschedule(code,shebeiNo);
    }

    @Override
    public List<RenwudanSchedule> queryoness(String code, Integer station) {
        try {
            QueryWrapper<RenwudanSchedule> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("code",code);
            queryWrapper.eq("station",station);
            queryWrapper.eq("isdel",0);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String,Object> selectmetes(String code, Integer station,String sys_depart_orgcode) {
        return renwudanScheduleMapper.selectmetes(code,station,sys_depart_orgcode);
    }

    @Override
    public List<RenwudanSchedule> selectmetesd(String code,String orgcode) {
        return renwudanScheduleMapper.selectmetesd(code,orgcode);
    }
}
