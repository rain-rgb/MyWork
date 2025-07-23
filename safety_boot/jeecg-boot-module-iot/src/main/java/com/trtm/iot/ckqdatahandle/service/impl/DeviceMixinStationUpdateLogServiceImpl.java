/**
 * @ClassName DeviceMixinStationUpdateLogServiceImpl.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年08月02日 15:09:00
 */
package com.trtm.iot.ckqdatahandle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPumpDay;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStation;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStationUpdateLog;
import com.trtm.iot.ckqdatahandle.mapper.DeviceMixinStationMapper;
import com.trtm.iot.ckqdatahandle.mapper.DeviceMixinStationUpdateLogMapper;
import com.trtm.iot.ckqdatahandle.service.IDeviceMixinStationService;
import com.trtm.iot.ckqdatahandle.service.IDeviceMixinStationUpdateLogService;
import com.trtm.iot.ckqdatahandle.vo.SelectDeviceMixinStationUpdateLogVo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class DeviceMixinStationUpdateLogServiceImpl extends ServiceImpl<DeviceMixinStationUpdateLogMapper, DeviceMixinStationUpdateLog> implements IDeviceMixinStationUpdateLogService {

    @Autowired
    private IDeviceMixinStationService deviceMixinStationService;

    @Autowired
    private DeviceMixinStationMapper deviceMixinStationMapper;

    @Autowired
    private IDeviceMixinStationUpdateLogService deviceMixinStationUpdateLogService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public IPage<DeviceMixinStationUpdateLog> selectMixinStationUpdateLogPage(SelectDeviceMixinStationUpdateLogVo selectDeviceMixinStationUpdateLogVo) {
        IPage<DeviceMixinStationUpdateLog> page = new Page<>(selectDeviceMixinStationUpdateLogVo.getPageNo(), selectDeviceMixinStationUpdateLogVo.getPageSize());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        QueryWrapper<DeviceMixinStationUpdateLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", "0");
        queryWrapper.in("sid",shebeilist);//用户权限下设备编号
        queryWrapper.lambda().orderByDesc(DeviceMixinStationUpdateLog::getUpdateTime);
        //标段编号
        String bidCode = selectDeviceMixinStationUpdateLogVo.getBidCode();
        if (StringUtils.isNotEmpty(bidCode)) {
            queryWrapper.eq("bid_code", bidCode);
        }
        //搅拌站号
        String mixinStationCode = selectDeviceMixinStationUpdateLogVo.getMixinStationCode();
        if (StringUtils.isNotEmpty(mixinStationCode)) {
            queryWrapper.eq("mixin_station_code", mixinStationCode);
        }
        String startTime = selectDeviceMixinStationUpdateLogVo.getStartTime();
        String endTime = selectDeviceMixinStationUpdateLogVo.getEndTime();
        queryWrapper.apply(StringUtils.isNotBlank(startTime),
                "date_format (update_time,'%Y-%m-%d %H:%i:%s') >= date_format('" + startTime + "','%Y-%m-%d %H:%i:%s')")
                .apply(StringUtils.isNotBlank(endTime),
                        "date_format (update_time,'%Y-%m-%d %H:%i:%s') <= date_format('" + endTime + "','%Y-%m-%d %H:%i:%s')");
        IPage<DeviceMixinStationUpdateLog> result = this.page(page, queryWrapper);
        return result;
    }

    /*
     * @Author zgq
     * @Description 拌合站配料信息修正
     * @Date 2021/8/1 22:07
     * @Param
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> saveMixinStationInfoUpdateLog(Integer id,String batchingProductionUpdate,String groutingTotalUpdate) {
        Result<Boolean> result = new Result<>();
        result.setResult(true);
        result.setMessage("修正成功");
        try {
            DeviceMixinStation deviceMixinStation = deviceMixinStationService.getById(id);
            DeviceMixinStationUpdateLog mixinStationInfoUpdateLog = new DeviceMixinStationUpdateLog();
            mixinStationInfoUpdateLog.setBidCode(deviceMixinStation.getBidCode());
            mixinStationInfoUpdateLog.setMixinStationCode(deviceMixinStation.getMixinStationCode());
            mixinStationInfoUpdateLog.setSid(deviceMixinStation.getSid());
            mixinStationInfoUpdateLog.setUpdateTime(new Date());
            if(StringUtils.isNotEmpty(batchingProductionUpdate)){
                mixinStationInfoUpdateLog.setBatchingProductionUpdate(Double.parseDouble(batchingProductionUpdate));
                deviceMixinStation.setBatchingProductionTotal(deviceMixinStation.getBatchingProductionTotal()+Double.parseDouble(batchingProductionUpdate));
                deviceMixinStationMapper.update(deviceMixinStation,new QueryWrapper<DeviceMixinStation>().eq("id",deviceMixinStation.getId()));
            }
            if(StringUtils.isNotEmpty(groutingTotalUpdate)){
                mixinStationInfoUpdateLog.setGroutingTotalUpdate(Double.parseDouble(groutingTotalUpdate));
            }
            boolean save = deviceMixinStationUpdateLogService.save(mixinStationInfoUpdateLog);
            if(!save){
                result.setResult(false);
                result.setMessage("修正失败!");
            }
        } catch (Exception e) {
            result.setResult(false);
            throw new JeecgBootException("修正失败！");
        }
        return result;
    }
}


