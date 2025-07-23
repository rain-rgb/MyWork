package com.trtm.iot.clgl.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.trtm.iot.clgl.entity.FrontDeviceHistorydata;
import com.trtm.iot.clgl.mapper.FrontDeviceHistorydataMapper;
import com.trtm.iot.clgl.service.IFrontDeviceHistorydataService;
import org.jeecg.common.util.DateUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

/**
 * @Description: 车辆信息历史数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-13
 * @Version: V1.0
 */
@Service
public class FrontDeviceHistorydataServiceImpl extends ServiceImpl<FrontDeviceHistorydataMapper, FrontDeviceHistorydata> implements IFrontDeviceHistorydataService {

    @Override
    public void insertGpsdata(JSONObject json) {
        FrontDeviceHistorydata one = new FrontDeviceHistorydata();
        one.setDeviceType("C");
        one.setLatitude(Convert.toBigDecimal(json.getStr("lat")));
        one.setLongitude(Convert.toBigDecimal(json.getStr("lng")));
        one.setShebeiNo(json.getStr("imei"));
        one.setIfid(json.getStr("status"));
        one.setUploadtime(json.getStr("statusTimeDesc"));
        this.save(one);
    }

    /**
     * 获取token
     * @return
     */
    @Override
    public Map selectToken(String shebeiNo,String datatime_end,String datatime_begin) {

       Map map=new HashMap();
        JSONObject jsonObject=new JSONObject();
        jsonObject.set("appid","17398007785");
        jsonObject.set("time",1635386457);
        jsonObject.set("signature","193386bbc84e8a45d7599706e865bc31");
        String post = HttpRequest.post("http://open.figps.com/api/auth")
                .header("Content-Type", "application/json")
                .body(String.valueOf(jsonObject))
                .execute()
                .body();
        JSONObject jsonObjects = new JSONObject(post);
        String accessToken = jsonObjects.getStr("accessToken");

        String timestart=null;
        String timeend=null;
        try {
            timeend= DateUtils.dateToStamps(datatime_end);
            timestart= DateUtils.dateToStamps(datatime_begin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String posts = HttpRequest.get("http://open.figps.com/api/device/track/history?accessToken="+accessToken+"&imei="+shebeiNo+"&startTime="+timestart+"&endTime="+timeend+"&coordType=gcj02&filterNoise=1&rectifyTrack=1")
                .header("Content-Type", "application/json")
                .execute()
                .body();
        JSONObject jsonObject3 = new JSONObject(posts);
        System.out.println(jsonObject3);
        JSONArray data = (JSONArray) jsonObject3.get("data");
        map.put("data",data);
        return map;

    }

    @Override
    public FrontDeviceHistorydata selectnewData(String shebeino, double lng1, double lat1) {
        try {
            QueryWrapper<FrontDeviceHistorydata> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shebei_no",shebeino);
            queryWrapper.eq("longitude",lng1);
            queryWrapper.eq("latitude",lat1);
            queryWrapper.orderByDesc("id");
            queryWrapper.last("limit 1");
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map> selectgetHistoryMByMUtc(String shebeiNo, String projectId, String uploadtime) {
        String l = null;
        l = "?LoginName="+"jtcs"+"&LoginPassword="+"123456"+"&LoginType=ENTERPRISE"+"&language=cn"+"&timeZone=+08"+"&apply=APP";
        String url = "http://api.588gps.net/GetDateServices.asmx/loginSystem"+l;
        System.out.println(url);
        String body = HttpRequest.get(url)
                .execute()
                .body();

        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        Integer codes = (Integer) jsonObject1.get("errorCode");
        String token = null;
        if (codes==200){
            token = (String) jsonObject1.get("mds");
        }
        String timestart=null;
        String timeend=null;
        try {
            timeend= DateUtils.dateToStamps(projectId);
            timestart= DateUtils.dateToStamps(uploadtime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String l1 = "?method=getHistoryMByMUtc"+"&mds="+token+"&playLBS=true&mapType=BAIDU"+"&macid="+shebeiNo+"&from="+timestart+"&to="+timeend;
        String url1 = "http://api.588gps.net//GetDateServices.asmx/GetDate"+l1;
        System.out.println(url1);
        String body1 = HttpRequest.get(url1)
                .execute()
                .body();

        if(!body1.contains("设备号不存在")){
            JSONObject jsonObject3 = new JSONObject(body1);
            JSONArray data1 = (JSONArray) jsonObject3.get("data");
            Object o1 = data1.get(0);
            JSONObject jsonObject2 = new JSONObject(o1);
            String point = (String)jsonObject2.get("Point");
            System.out.println(point);

            List<Map> list = new ArrayList<>();
            String[] split = point.split(";");
            for (int i = 0; i < split.length; i++) {
                Map<String, String> map3 = new HashMap<>();
                String s = split[i];
                String[] split1 = s.split(",");
                map3.put("gpsTime",split1[2]);
                map3.put("lng",split1[0]);
                map3.put("lat",split1[1]);
                list.add(map3);
            }
            return list;
        }else {
            JSONObject jsonObject=new JSONObject();
            jsonObject.set("appid","17398007785");
            jsonObject.set("time",1635386457);
            jsonObject.set("signature","193386bbc84e8a45d7599706e865bc31");
            String post = HttpRequest.post("http://open.figps.com/api/auth")
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(jsonObject))
                    .execute()
                    .body();
            JSONObject jsonObjects = new JSONObject(post);
            String accessToken = jsonObjects.getStr("accessToken");
            String posts = HttpRequest.get("http://open.figps.com/api/device/track/history?accessToken="+accessToken+"&imei="+shebeiNo+"&startTime="+timestart+"&endTime="+timeend+"&coordType=gcj02&filterNoise=1&rectifyTrack=1")
                    .header("Content-Type", "application/json")
                    .execute()
                    .body();
            JSONObject jsonObject3 = new JSONObject(posts);
            System.out.println(jsonObject3);
            JSONArray data = (JSONArray) jsonObject3.get("data");

            List<Map> list = new ArrayList<>();
            for (Object o : data) {
                Map<String, String> map3 = new HashMap<>();
                JSONObject object = new JSONObject(o);
                Object gpsTime = object.get("gpsTime");
                Object lng = object.get("lng");
                Object lat = object.get("lat");
                map3.put("gpsTime",gpsTime.toString());
                map3.put("lng",lng.toString());
                map3.put("lat",lat.toString());
                list.add(map3);
            }
            return list;
        }
    }
}
