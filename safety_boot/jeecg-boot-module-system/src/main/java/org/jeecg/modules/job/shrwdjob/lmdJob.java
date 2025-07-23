package org.jeecg.modules.job.shrwdjob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.api.utils.crypt.MD5;
import com.trtm.iot.devicecranerealdata.entity.DeviceCraneRealdata;
import com.trtm.iot.devicecranerealdata.service.IDeviceCraneRealdataService;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.sy.syshrwd.entity.Syshrwd;
import com.trtm.sy.syshrwd.service.ISyshrwdService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class lmdJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDeviceCraneRealdataService deviceCraneRealdataService;
    @Autowired
    private IDeviceCraneHistorydataService deviceCraneHistorydataService;

    private static final String comid = "2";
    private static final String compvtkey ="589625dce895454dbff9782c299db156";
    private static final String password="21218cca77804d2ba1922c33e0151105";
    private static final String key="f1cd9351930d4e589922edbcf3b09a7c";

    private static final String url = "http://api.v-box.net/box-data/api/we-data/login?alias=";//登陆
    private static final String urlboxs = "http://api.v-box.net/box-data/api/we-data/boxs";//盒子列表（旧，单级分组）
    private static final String urlboxlist = "http://api.v-box.net/box-data/api/we-data/boxlist";//盒子列表（新，多级分组）
    private static final String urlrealgroups = "http://api.v-box.net/box-data/api/we-data/realgroups";//实时监控点分组列表
    private static final String urlrealcfgs = "http://api.v-box.net/box-data/api/we-data/realcfgs";//实时监控点配置列表
    private static final String urlrealdata = "http://api.v-box.net/box-data/api/we-data/realdata";//实时监控点数据列表
    private static final String urlupdrealdata = "http://api.v-box.net/box-data/api/we-data/updrealdata";//修改实时监控点值
    private static final String urlmonitors = "http://api.v-box.net/box-data/api/we-data/monitors";//历史监控点列表

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LIDONG_JLSRW);//查询送货短信提醒状态
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到送货短信提醒定时任务的配置信息" + DateUtils.now()));
            return;
        }

        for (int i = 0; i < 2; i++) {
            DeviceCraneRealdata one = new DeviceCraneRealdata();
            String alias = "";
            if (i == 0){
                alias = "ZD2023201";
                QueryWrapper<DeviceCraneRealdata> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("device_code",2023062701);
                one = deviceCraneRealdataService.getOne(queryWrapper);
            }else {
                alias = "ZD2023202";
                QueryWrapper<DeviceCraneRealdata> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("device_code",2023062702);
                one = deviceCraneRealdataService.getOne(queryWrapper);
            }
            JSONObject queryurlrealdata = queryurlrealdata(alias);
            JSONObject result = (JSONObject) queryurlrealdata.get("result");
            JSONArray list = (JSONArray) result.get("list");

            one.setCranedate(new Date());
            DeviceCraneHistorydata deviceCraneHistorydata = new DeviceCraneHistorydata();
            for (Object record : list) {
                JSONObject object = new JSONObject(record);

                if (object.get("monitorId").toString().equals("20001")){
                    one.setWindSpeed(Double.valueOf(object.get("value").toString()));
                }else if (object.get("monitorId").toString().equals("20002")){
                    one.setReservedAnalogquantityone(Double.valueOf(object.get("value").toString()));
                }else if (object.get("monitorId").toString().equals("20003")){
                    one.setBigCarroute(Double.valueOf(object.get("value").toString()));
                }else if (object.get("monitorId").toString().equals("20004")){
                    one.setSmallCarroute(Double.valueOf(object.get("value").toString()));
                }else if (object.get("monitorId").toString().equals("20005")){
                    one.setBigCarleftlimit(Integer.valueOf(object.get("value").toString()));
                }else if (object.get("monitorId").toString().equals("20006")){
                    one.setBigCarrightlimit(Integer.valueOf(object.get("value").toString()));
                }else if (object.get("monitorId").toString().equals("20007")){
                    one.setMainHookload(Double.valueOf(object.get("value").toString()));
                }else if (object.get("monitorId").toString().equals("20008")){
                    one.setReservedVicehookload(Double.valueOf(object.get("value").toString()));
                }else if (object.get("monitorId").toString().equals("20009")){
                    one.setMainHookheight(Double.valueOf(object.get("value").toString()));
                }else if (object.get("monitorId").toString().equals("20010")){
                    one.setReservedVicehookheight(Double.valueOf(object.get("value").toString()));
                }else if (object.get("monitorId").toString().equals("20011")){
                    one.setSmallCar1brake1(Integer.valueOf(object.get("value").toString()));
                }
            }
            BeanUtils.copyProperties(one,deviceCraneHistorydata );
            deviceCraneHistorydataService.save(deviceCraneHistorydata);
            deviceCraneRealdataService.updateById(one);
        }
    }

    public JSONObject queryurlrealgroups(String sid,long time,Integer boxId) {
//        Date date = new Date();
//        long time = date.getTime();
//        String sid = getCode(time);//登陆拿到sid
//        JSONObject queryurlboxlist = queryurlboxlist(time,sid);
//        JSONObject result = (JSONObject) queryurlboxlist.get("result");
//        JSONArray list = (JSONArray) result.get("list");
//        JSONObject o = (JSONObject) list.get(0);
//        JSONArray list1 = (JSONArray) o.get("boxList");
//        JSONObject o1 = (JSONObject) list1.get(0);
//        Integer boxId = (Integer) o1.get("boxId");

        String signing = "boxId="+boxId+"&comid="+comid+"&compvtkey="+compvtkey+"&sid="+sid+"&ts="+time+"&key="+key;
        String sign = MD5.toMD5(signing);
        JSONObject sendObject = JSONUtil.createObj();
        sendObject.set("comid", comid);
        sendObject.set("compvtkey", compvtkey);
        sendObject.set("sign", sign);
        sendObject.set("ts", time);
        sendObject.set("sid", sid);
        String urlrealgroup = urlrealgroups + "?boxId="+boxId;
        JSONObject geturlboxs = geturlboxs(sendObject, urlrealgroup);
        return geturlboxs;
    }

    public JSONObject queryurlrealdata(String alias) {
        Date date = new Date();
        long time = date.getTime();
        String sid = getCode(time,alias);//登陆拿到sid
        JSONObject queryurlboxlist = queryurlboxlist(time,sid);
        JSONObject result = (JSONObject) queryurlboxlist.get("result");
        JSONArray list = (JSONArray) result.get("list");
        JSONObject o = (JSONObject) list.get(0);
        JSONArray list1 = (JSONArray) o.get("boxList");
        JSONObject o1 = (JSONObject) list1.get(0);
        Integer boxId = (Integer) o1.get("boxId");

        JSONObject queryurlrealgroups = queryurlrealgroups(sid, time, boxId);
        System.out.println(queryurlrealgroups);

        String signing = "boxId="+boxId+"&comid="+comid+"&compvtkey="+compvtkey+"&devType="+1+"&groupId="+2+"&sid="+sid+"&ts="+time+"&key="+key;
        String sign = MD5.toMD5(signing);
        JSONObject sendObject = JSONUtil.createObj();
        sendObject.set("comid", comid);
        sendObject.set("compvtkey", compvtkey);
        sendObject.set("sign", sign);
        sendObject.set("ts", time);
        sendObject.set("sid", sid);
        String urlrealgroup = urlrealdata + "?boxId="+boxId+"&groupId="+2+"&devType="+1;
        JSONObject geturlboxs = geturlboxs(sendObject, urlrealgroup);
        return geturlboxs;
    }
    /**
     * 龙门吊测试,盒子列表（旧，单级分组）
     */
    @GetMapping(value = "/urlboxs")
    public JSONObject queryurlboxs(long time, String sid) {
        //String sid = getCode(time);//登陆拿到sid
        String signing = "comid="+comid+"&compvtkey="+compvtkey+"&sid="+sid+"&ts="+time+"&key="+key;
        String sign = MD5.toMD5(signing);
        JSONObject sendObject = JSONUtil.createObj();
        sendObject.set("comid", comid);
        sendObject.set("compvtkey", compvtkey);
        sendObject.set("sign", sign);
        sendObject.set("ts", time);
        sendObject.set("sid", sid);
        JSONObject geturlboxs = geturlboxs(sendObject, urlboxs);
        return geturlboxs;
    }

    /**
     * 盒子列表（新，多级分组）
     */
    @GetMapping(value = "/urlboxlist")
    public JSONObject queryurlboxlist(long time,String sid) {
//        String sid = getCode(time);//登陆拿到sid
        String signing = "comid="+comid+"&compvtkey="+compvtkey+"&sid="+sid+"&ts="+time+"&key="+key;
        String sign = MD5.toMD5(signing);
        JSONObject sendObject = JSONUtil.createObj();
        sendObject.set("comid", comid);
        sendObject.set("compvtkey", compvtkey);
        sendObject.set("sign", sign);
        sendObject.set("ts", time);
        sendObject.set("sid", sid);
        JSONObject geturlboxs = geturlboxs(sendObject, urlboxlist);
        return geturlboxs;
    }
    public static JSONObject geturlboxs(JSONObject sendObject,String url) {
        String body = HttpRequest.post(url)
                .header("common", String.valueOf(sendObject))
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        return jsonObject1;
    }
    // 登陆
    public static String getCode(long time,String alias) {
        String signing = "alias="+alias+"&comid="+comid+"&compvtkey="+compvtkey+"&password="+password+"&ts="+time+"&key="+key;
        String sign = MD5.toMD5(signing);
        System.out.println(sign);

        JSONObject sendObject = JSONUtil.createObj();
        sendObject.set("comid", comid);
        sendObject.set("compvtkey", compvtkey);
        sendObject.set("sign", sign);
        sendObject.set("ts", time);

        String body = HttpRequest.post(url+alias+"&password="+password)
                .header("common", String.valueOf(sendObject))
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        Integer integer = (Integer) jsonObject1.get("code");
        if (integer == 200){
            JSONObject result = (JSONObject) jsonObject1.get("result");
            return (String) result.get("sid");
        }else {
            return "登录失败！！！";
        }
    }
}
