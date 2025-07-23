package com.trtm.iot.jinanpenlingjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.zhiliangtaizuocfg.entity.ZhiliangTaizuoCfg;
import com.trtm.iot.zhiliangtaizuocfg.service.IZhiliangTaizuoCfgService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jiaozhu/jinancontroll")
@Slf4j
public class JinanController {
//    @Autowired
//    jinanUtil jinanUtil;
      @Autowired
      IZhiliangTaizuoCfgService zhiliangTaizuoCfgService;
    /**
     * 分页列表查询
     *
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList() {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.set("password","369369");
        jsonObject1.set("username","15291561224");
        String token = JinanUtil.getCode(jsonObject1, "https://spray.jstajian.top/prod-api/login");
        JSONArray getdate = JinanUtil.getdate(token);

        List<Jinanentity> list = new ArrayList<>();
        if(getdate != null){
            for (Object o :getdate){
                Jinanentity jinanentity = new Jinanentity();
                JSONObject object = new JSONObject(o);
                Object deviceId = object.get("deviceId");//设备ID
                Object deviceNum = object.get("deviceNum");//设备编号吧
                Object deviceName = object.get("deviceName");//设备名称
                Object deviceStatus = object.get("deviceStatus");//设备状态：1在线，2离线
                Object forbidden = object.get("forbidden");//设备禁用启用
                Object remark = object.get("remark");//备注
                Object tem = object.get("tem");//温度
                Object hum = object.get("hum");//湿度
                Object createTime = object.get("createTime");//创建时间 yyyy-MM-dd HH:mm:ss
                Object imei = object.get("imei");//imei
                Object iccid = object.get("iccid");//iccid
                Object sysPhone = object.get("sysPhone");//用户手机号
                jinanentity.setDeviceId((Integer) deviceId);
                jinanentity.setDeviceNum((String) deviceNum);
                jinanentity.setDeviceName((String) deviceName);
                jinanentity.setDeviceStatus((Integer) deviceStatus);
                jinanentity.setForbidden((Boolean) forbidden);
                jinanentity.setRemark((String) remark);
                jinanentity.setTem((Double) tem);
                jinanentity.setHum((Double) hum);
                jinanentity.setCreateTime((String) createTime);
                jinanentity.setImei((String) imei);
                jinanentity.setIccid((String) iccid);
                jinanentity.setSysPhone((String) sysPhone);
                list.add(jinanentity);
            }
        }
        return Result.OK(list);
    }


    /**
     * 分页列表查询
     *根据设备ID查询设备下的路线状态
     * @return
     */
    @GetMapping(value = "/xqlist")
    public Result<?> queryPagexqList(Integer deviceId) {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.set("password","369369");
        jsonObject1.set("username","15291561224");
        String token = JinanUtil.getCode(jsonObject1, "https://spray.jstajian.top/prod-api/login");
        JSONArray getdate = JinanUtil.getdatexq(token,deviceId);

        List<Jinanentity> list = new ArrayList<>();
        for (Object o :getdate){
            Jinanentity jinanentity = new Jinanentity();
            JSONObject object = new JSONObject(o);
            System.out.println(object);
            Object jobType = object.get("jobType");//是否存在任务计划 0无，1定时，2循环
            Object status = object.get("status");//即时开启的状态 1开启，2关闭
            Object pathName = object.get("pathName");//台座名称
            Object device = object.get("deviceId");//台座名称
            Object path = object.get("path");//路线名称
            if (pathName == "null"){
                continue;
            }
            String s = pathName.toString();
            QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("taizuoname",s);
            ZhiliangTaizuoCfg one = zhiliangTaizuoCfgService.getOne(queryWrapper);
            if (one != null){
                one.setLzplzt((Integer) status);
                zhiliangTaizuoCfgService.updateById(one);
            }
            jinanentity.setDeviceId((Integer) jobType);
            jinanentity.setDeviceStatus((Integer) status);
            jinanentity.setDeviceName(pathName.toString());
            jinanentity.setIccid(device.toString());
            jinanentity.setImei(path.toString());
            list.add(jinanentity);
        }
        return Result.OK(list);
    }

    /**
     * 分页列表查询
     *根据设备ID查询设备下的路线状态
     * @return
     */
    @GetMapping(value = "/allist")
    public Result<?> queryPagealList(Integer deviceId,Integer path,Integer status) {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.set("password","369369");
        jsonObject1.set("username","15291561224");
        String token = JinanUtil.getCode(jsonObject1, "https://spray.jstajian.top/prod-api/login");
        Integer getdate = JinanUtil.getdateal(token,deviceId,path,status);
        return Result.OK(getdate);
    }

    /**
     * 济新高速梁厂环境监测
     *
     * @return
     */
    @GetMapping(value = "/listjxlc")
    public Result<?> queryPageListjxlc() {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.set("password","jxeb");
        jsonObject1.set("username","c230227");
        String url = "http://dust.0531yun.cn/api/getTokenByAcc?loginName=c230227jxeb&password=c230227jxeb";
        String body = HttpRequest.get(url)
                .execute()
                .body();

        JSONObject jsonObject = new JSONObject(body);
        Integer codes = (Integer) jsonObject.get("code");
        String token = null;
        if(codes == 1000){
            String data = jsonObject.getStr("data");
            JSONObject jsonObject3 = new JSONObject(data);
            token = jsonObject3.getStr("token");
        }

        // 获取设备id
        String url1 = "http://dust.0531yun.cn/api/device/getDevices";
        String body1 = HttpRequest.get(url1)
                .header("token",token)
                .execute()
                .body();
        JSONObject jsonObject2 = new JSONObject(body1);
        Integer code = (Integer) jsonObject.get("code");
        JSONArray data1 = null;
        String deviceId = null;
        if(code == 1000){
            data1 = jsonObject2.getJSONArray("data");
            if (data1 != null){
                for (Object o :data1){
                    JSONObject object = new JSONObject(o);
                    deviceId = object.getStr("deviceId");
                }
            }
        }

        // 查询数据
        String url2 = "http://dust.0531yun.cn/api/data/getRealtimeData?deviceIds="+deviceId;
        String body2 = HttpRequest.get(url2)
                .header("token",token)
                .execute()
                .body();

        ArrayList<Map> list = new ArrayList<>();
        JSONObject jsonObject3 = new JSONObject(body2);
        Integer code2 = (Integer) jsonObject.get("code");
        if(code2 == 1000){
            JSONArray data4 = jsonObject3.getJSONArray("data");
            if (data4 != null){
                for (Object o :data4){
                    Map<String, Float> map = new HashMap<>();
                    JSONObject object = new JSONObject(o);
                    String pm25 = object.getStr("pm25");
                    String pm10 = object.getStr("pm10");
                    String tem = object.getStr("tem");
                    String hum = object.getStr("hum");
                    String noise = object.getStr("noise");
                    map.put("pm25", Float.valueOf(pm25));
                    map.put("pm10", Float.valueOf(pm10));
                    map.put("tem", Float.valueOf(tem));
                    map.put("hum", Float.valueOf(hum));
                    map.put("noise", Float.valueOf(noise));
                    list.add(map);
                }
            }
        }
        return Result.OK(list);
    }
}
