package com.trtm.iot.soslist.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.JsonNode;
import com.trtm.iot.soslist.entity.SosList;
import com.trtm.iot.soslist.mapper.SosListMapper;
import com.trtm.iot.soslist.service.ISosListService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

/**
 * @Description: sos_list
 * @Author: jeecg-boot
 * @Date: 2024-12-17
 * @Version: V1.0
 */
@Service
public class SosListServiceImpl extends ServiceImpl<SosListMapper, SosList> implements ISosListService {

    @Override
    @Transactional
    public void saveOrUpdateBySid(JsonNode dataObj) {
        if (ObjectUtil.isNotEmpty(dataObj)) {
            for (JsonNode jsonNode : dataObj) {
                String cTime = jsonNode.get("c_time").toString();
                String ctime = cTime.substring(1, cTime.length() - 1);
                Date date = new Date(Long.parseLong(ctime) * 1000);
                if (isToday(date)) {
                    String sid = jsonNode.get("s_id").toString();
                    SosList sosInfo = this.getOne(Wrappers.lambdaQuery(new SosList()).eq(SosList::getSId, sid));
                    if (ObjectUtil.isEmpty(sosInfo)) {
                        SosList sosList = new SosList();
                        if (jsonNode.has("s_id") && !jsonNode.get("s_id").isNull()) {
                            String sId = jsonNode.get("s_id").toString();
                            sosList.setSId(sId.substring(1, sId.length() - 1));
                        }
                        if (jsonNode.has("user_id") && !jsonNode.get("user_id").isNull()) {
                            String userId = jsonNode.get("user_id").toString();
                            sosList.setUserId(userId.substring(1, userId.length() - 1));
                        }
                        if (jsonNode.has("user_name") && !jsonNode.get("user_name").isNull()) {
                            String userName = jsonNode.get("user_name").toString();
                            sosList.setUserName(userName.substring(1, userName.length() - 1));
                        }
                        if (jsonNode.has("device_id") && !jsonNode.get("device_id").isNull()) {
                            String deviceId = jsonNode.get("device_id").toString();
                            sosList.setDeviceId(deviceId.substring(1, deviceId.length() - 1));
                        }
                        if (jsonNode.has("location") && !jsonNode.get("location").isNull()) {
                            String location = jsonNode.get("location").toString();
                            sosList.setLocation(location.substring(1, location.length() - 1));
                        }
                        if (jsonNode.has("x_point") && !jsonNode.get("x_point").isNull()) {
                            String xPoint = jsonNode.get("x_point").toString();
                            sosList.setXPoint(xPoint.substring(1, xPoint.length() - 1));
                        }
                        if (jsonNode.has("y_point") && !jsonNode.get("y_point").isNull()) {
                            String yPoint = jsonNode.get("y_point").toString();
                            sosList.setYPoint(yPoint.substring(1, yPoint.length() - 1));
                        }
                        if (jsonNode.has("c_time") && !jsonNode.get("c_time").isNull()) {
                            String cTime1 = jsonNode.get("c_time").toString();
                            sosList.setCTime(cTime.substring(1, cTime1.length() - 1));
                        }
                        if (jsonNode.has("sos_type") && !jsonNode.get("sos_type").isNull()) {
                            String sosType = jsonNode.get("sos_type").toString();
                            sosList.setSosType(sosType.substring(1, sosType.length() - 1));
                        }
                        if (jsonNode.has("status") && !jsonNode.get("status").isNull()) {
                            String status = jsonNode.get("status").toString();
                            sosList.setStatus(status.substring(1, status.length() - 1));
                        }
                        if (jsonNode.has("admin_id") && !jsonNode.get("admin_id").isNull()) {
                            String adminId = jsonNode.get("admin_id").toString();
                            sosList.setAdminId(adminId.substring(1, adminId.length() - 1));
                        }
                        if (jsonNode.has("msg") && !jsonNode.get("msg").isNull()) {
                            String msg = jsonNode.get("msg").toString();
                            sosList.setMsg(msg.substring(1, msg.length() - 1));
                        }
                        if (jsonNode.has("floor") && !jsonNode.get("floor").isNull()) {
                            String floor = jsonNode.get("floor").toString();
                            sosList.setFloor(floor.substring(1, floor.length() - 1));
                        }
                        if (jsonNode.has("imgurl") && !jsonNode.get("imgurl").isNull()) {
                            String imgurl = jsonNode.get("imgurl").toString();
                            sosList.setImgurl(imgurl.substring(1, imgurl.length() - 1));
                        }
                        if (jsonNode.has("mobile") && !jsonNode.get("mobile").isNull()) {
                            String mobile = jsonNode.get("mobile").toString();
                            sosList.setMobile(mobile.substring(1, mobile.length() - 1));
                        }
                        if (jsonNode.has("is_real") && !jsonNode.get("is_real").isNull()) {
                            String isReal = jsonNode.get("is_real").toString();
                            sosList.setIsReal(isReal.substring(1, isReal.length() - 1));
                        }
                        if (jsonNode.has("r_time") && !jsonNode.get("r_time").isNull()) {
                            String rTime = jsonNode.get("r_time").toString();
                            sosList.setRTime(rTime.substring(1, rTime.length() - 1));
                        }
                        this.save(sosList);
                    }
                }
            }
        }
    }

    public static boolean isToday(Date date) {
        // 获取当前时间
        Calendar now = Calendar.getInstance();
        // 获取传入日期的年月日
        Calendar targetDate = Calendar.getInstance();
        targetDate.setTime(date);

        // 比较年、月、日部分
        return now.get(Calendar.YEAR) == targetDate.get(Calendar.YEAR) &&
                now.get(Calendar.MONTH) == targetDate.get(Calendar.MONTH) &&
                now.get(Calendar.DAY_OF_MONTH) == targetDate.get(Calendar.DAY_OF_MONTH);
    }
}
