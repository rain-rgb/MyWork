package com.trtm.iot.entranceguardrecordreal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardRecord;
import com.trtm.iot.entranceGuardRecord.mapper.EntranceGuardRecordMapper;
import com.trtm.iot.entranceguardrecordreal.entity.EntranceGuardRecordReal;
import com.trtm.iot.entranceguardrecordreal.mapper.EntranceGuardRecordRealMapper;
import com.trtm.iot.entranceguardrecordreal.service.IEntranceGuardRecordRealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description: 门禁考勤实时表
 * @Author: jeecg-boot
 * @Date: 2021-09-27
 * @Version: V1.0
 */
@Service
public class EntranceGuardRecordRealServiceImpl extends ServiceImpl<EntranceGuardRecordRealMapper, EntranceGuardRecordReal> implements IEntranceGuardRecordRealService {

    @Autowired
    private EntranceGuardRecordRealMapper entranceGuardRecordRealMapper;

    @Autowired
    private EntranceGuardRecordMapper entranceGuardRecordMapper;

    @Override
    public EntranceGuardRecordReal listMaxOpentime(String shebeis) {
        return entranceGuardRecordRealMapper.listMaxOpentime(shebeis);
    }

    @Override
    public List<EntranceGuardRecordReal> list1(String doorid1) {
        return entranceGuardRecordRealMapper.list1(doorid1);
    }

    @Override
    public List<EntranceGuardRecordReal> getBanZu(String shebeino,Date parse) {
        return entranceGuardRecordRealMapper.getBanZu(shebeino,parse);
    }

    @Override
    public List<EntranceGuardRecordReal> getEntranceGuardRecordRealList(String shebeino, Date parse) {
        return entranceGuardRecordRealMapper.getEntranceGuardRecordRealList(shebeino,parse);
    }

    @Override
    public List<EntranceGuardRecordReal> queryJiXinList(String shebeino) {
        return entranceGuardRecordRealMapper.queryJiXinList(shebeino);
    }

    @Override
    public List<EntranceGuardRecordReal> queryJiXinBanZuList(String shebeino) {
        return entranceGuardRecordRealMapper.queryJiXinBanZuList(shebeino);
    }

    @Override
    public List<EntranceGuardRecordReal> querylistAttendance(String shebeino) {
        return entranceGuardRecordRealMapper.querylistAttendance(shebeino);
    }

    @Override
    public EntranceGuardRecordReal getByshebi(String shebei) {
        return entranceGuardRecordRealMapper.getByshebi(shebei);
    }
}
