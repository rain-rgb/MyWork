package com.trtm.iot.clgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.car.entity.SchedulingCar;
import com.trtm.iot.clgl.entity.ClxxRealdata;
import com.trtm.iot.clgl.entity.FrontDeviceHistorydata;
import com.trtm.iot.clgl.mapper.ClxxRealdataMapper;
import com.trtm.iot.clgl.service.IClxxRealdataService;
import com.trtm.iot.device_mixer_trucks.entity.DeviceMixerTrucks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 车辆信息实时数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-13
 * @Version: V1.0
 */
@Service
public class ClxxRealdataServiceImpl extends ServiceImpl<ClxxRealdataMapper, ClxxRealdata> implements IClxxRealdataService {

    @Autowired
    private ClxxRealdataMapper clxxRealdataMapper;
    @Override
    public ClxxRealdata queryone(String shebeino) {
//        try {
//            QueryWrapper<ClxxRealdata> queryWrapper=new QueryWrapper<>();
//            queryWrapper.eq("shebei_no",shebeino);
//            return this.getOne(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return clxxRealdataMapper.queryone(shebeino);
    }
    @Override
    public List<ClxxRealdata> querylist(Integer status) {
        try {
            QueryWrapper<ClxxRealdata>  queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("status",status);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ClxxRealdata> selectList(Integer curid, List<String> strsToList) {
        try {
            QueryWrapper<ClxxRealdata> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", curid);
            queryWrapper.in("shebei_no", strsToList);
//            queryWrapper.last("limit 30");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public String getDepartName(String sysOrgCode) {
        return clxxRealdataMapper.getDepartName(sysOrgCode);
    }

    @Override
    public void updateStatusById(Integer id) {
        this.baseMapper.updateStatusById(id);
    }

    @Override
    public void saveClxxRealdata(DeviceMixerTrucks deviceMixerTrucks) {
        LambdaQueryWrapper<ClxxRealdata> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ClxxRealdata::getShebeiNo,deviceMixerTrucks.getDeviceid());
        queryWrapper.last("limit 1");
        ClxxRealdata clxxRealdata = this.getOne(queryWrapper);
        if (clxxRealdata==null){
            clxxRealdata=new ClxxRealdata();
        }
        clxxRealdata.setShebeiNo(deviceMixerTrucks.getDeviceid());
        clxxRealdata.setDeviceType("C");
        clxxRealdata.setSpeed(deviceMixerTrucks.getSpeed());
        clxxRealdata.setLatitude(deviceMixerTrucks.getLat());
        clxxRealdata.setLongitude(deviceMixerTrucks.getLon());
        clxxRealdata.setAltitude(deviceMixerTrucks.getAltitude());
        clxxRealdata.setDatatime(deviceMixerTrucks.getUpdatetime());
        clxxRealdata.setGpsnum(deviceMixerTrucks.getGpsvalidnum());
        clxxRealdata.setIfid(deviceMixerTrucks.getStrstatus());
        clxxRealdata.setRotatestatus(deviceMixerTrucks.getRotatestatus());
        this.saveOrUpdate(clxxRealdata);
    }
}
