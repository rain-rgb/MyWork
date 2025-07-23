package com.trtm.iot.tpny.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.trtm.iot.soslist.entity.SosList;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.trtm.iot.tpny.mapper.FrontDeviceRealdataMapper;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 摊铺碾压数据监测主表
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Service
public class FrontDeviceRealdataServiceImpl extends ServiceImpl<FrontDeviceRealdataMapper, FrontDeviceRealdata> implements IFrontDeviceRealdataService {

    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Override
    public boolean updateFrontdeviceData(JSONObject json) {
        boolean isdo = false;
        if (StrUtil.isBlank(json.getStr("status")) || json.getStr("status").equalsIgnoreCase("未启用")) {
            return isdo;
        }
        String imei = json.getStr("imei");
        QueryWrapper<FrontDeviceRealdata> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("shebei_no",imei);
        FrontDeviceRealdata onedata =  this.getOne(queryWrapper);
        CopyOptions copyOptions = new CopyOptions().setIgnoreProperties("status").setIgnoreCase(true).setIgnoreNullValue(true);
        if (null == onedata) {
            onedata = new FrontDeviceRealdata();
            BeanUtil.copyProperties(json,onedata,copyOptions);
            onedata.setShebeiNo(imei);
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(imei);
            String deviceType = "C";
            if (sbjwd != null){
                String sbname = sbjwd.getSbname();
                if (sbname.contains("推土机")) deviceType="G";
                if (sbname.contains("挖掘机")) deviceType="H";
            }
            onedata.setDeviceType(deviceType);
            onedata.setLatitude(Convert.toDouble(json.getStr("lat")));
            onedata.setLongitude(Convert.toDouble(json.getStr("lng")));
            this.save(onedata);
            isdo = true;
        } else {
            boolean doupdate = false;
            if (null == onedata.getGpstime()) {
                if (null != onedata.getSignaltime() && !onedata.getSignaltime().equals(json.getInt("signalTime"))) {
                    doupdate = true;
                }
            } else if (!onedata.getGpstime().equals(json.getInt("gpsTime"))) {
                doupdate = true;
            }
            if (doupdate) {
                BeanUtil.copyProperties(json,onedata,copyOptions);
                onedata.setIfid(json.getStr("status"));
                onedata.setLatitude(Convert.toDouble(json.getStr("lat")));
                onedata.setLongitude(Convert.toDouble(json.getStr("lng")));
                onedata.setDatatime(DateTime.now());
                this.saveOrUpdate(onedata);
                isdo = true;
            } else {
                if (null == onedata.getStatustimedesc() || !onedata.getStatustimedesc().equalsIgnoreCase(json.getStr("statusTimeDesc"))) {
                    onedata.setStatustimedesc(json.getStr("statusTimeDesc"));
                    onedata.setDatatime(DateTime.now());
                    onedata.setIfid(json.getStr("status"));
                    this.saveOrUpdate(onedata);
                }
            }
        }
//        String signalTime = json.getStr("signalTime");
//        String gpsTime = json.getStr("gpsTime");
//        DateTime dateTime = new DateTime(Integer.parseInt(signalTime));
//        DateTime dateTime1 = new DateTime(Integer.parseInt(gpsTime));
//        System.out.println(dateTime);
//        System.out.println(dateTime1);
        return isdo;
    }

    @Override
    public FrontDeviceRealdata queryone(String shebeino) {
        try{
            QueryWrapper<FrontDeviceRealdata> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shebei_no",shebeino);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveOrUpdateByProjectId(FrontDeviceRealdata frontDeviceRealdata) {
        FrontDeviceRealdata one = this.getOne(Wrappers.lambdaQuery(new FrontDeviceRealdata()).eq(FrontDeviceRealdata::getShebeiNo, frontDeviceRealdata.getShebeiNo()));
        if (ObjectUtil.isNotEmpty(one)){
            frontDeviceRealdata.setId(one.getId());
            this.updateById(frontDeviceRealdata);
        }else {
            this.save(frontDeviceRealdata);
        }
    }
}
