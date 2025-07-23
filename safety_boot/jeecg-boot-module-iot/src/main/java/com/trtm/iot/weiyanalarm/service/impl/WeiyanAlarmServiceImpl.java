package com.trtm.iot.weiyanalarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.internal.$Gson$Preconditions;
import com.trtm.iot.weiyanalarm.entity.WeiyanAlarm;
import com.trtm.iot.weiyanalarm.mapper.WeiyanAlarmMapper;
import com.trtm.iot.weiyanalarm.service.IWeiyanAlarmService;
import com.trtm.iot.weiyanalarmhandler.entity.WeiyanAlarmHandler;
import com.trtm.iot.weiyanalarmhandler.mapper.WeiyanAlarmHandlerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 监控量测报警数据表
 * @Author: jeecg-boot
 * @Date:   2022-07-06
 * @Version: V1.0
 */
@Service
public class WeiyanAlarmServiceImpl extends ServiceImpl<WeiyanAlarmMapper, WeiyanAlarm> implements IWeiyanAlarmService {

    @Autowired WeiyanAlarmMapper weiyanAlarmMapper;
    @Autowired
    WeiyanAlarmHandlerMapper weiyanAlarmHandlerMapper;
    @Autowired
    private IWeiyanAlarmService weiyanAlarmService;
    @Override
    public Integer saveMain(WeiyanAlarm weiyanAlarm, WeiyanAlarmHandler weiyanAlarmHandler) {
        try {
            QueryWrapper<WeiyanAlarm> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("batch_no",weiyanAlarm.getBatchNo());
            WeiyanAlarm weiyanAlarm1 = weiyanAlarmService.getOne(queryWrapper);
            if (weiyanAlarm1==null) {
                weiyanAlarmMapper.insert(weiyanAlarm);
                if (weiyanAlarmHandler != null) {
                    //外键设置
                    weiyanAlarmHandler.setBatchNo(weiyanAlarm.getBatchNo());
                    weiyanAlarmHandlerMapper.insert(weiyanAlarmHandler);
                }
                return 200;
            }else {
                return 500;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
    }
}
