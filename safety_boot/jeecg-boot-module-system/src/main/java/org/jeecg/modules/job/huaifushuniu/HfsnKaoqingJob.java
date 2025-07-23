package org.jeecg.modules.job.huaifushuniu;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.staffbase.entity.StaffBaseInfo;
import com.trtm.iot.staffbase.entity.StaffWorkDetail;
import com.trtm.iot.staffbase.service.IStaffBaseInfoService;
import com.trtm.iot.staffbase.service.IStaffBaseWorkService;
import com.trtm.iot.staffbase.service.IStaffWorkDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class HfsnKaoqingJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IStaffBaseInfoService staffBaseInfoService;
    @Autowired
    private IStaffBaseWorkService staffBaseWorkService;
    @Autowired
    private IStaffWorkDetailService staffWorkDetailService;
    @Autowired
    private IPushandreturnService pushandreturnService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HFSN_MJKQ_SJG);//淮阜枢纽数据对接省建管
        //如果他等于空

        if (null == selectsysconfigone) {
            log.info(String.format("未获取到淮阜枢纽数据对接省建管定时任务的配置信息" + DateUtils.now()));
            return;
        }

        // ------------------------------- 获取token以及setionid -------------------------------
        String startsKaoqing =selectsysconfigone.getCurdate();
         String endkaoqing = selectsysconfigone.getCurdate();
        JSONArray jsonArray1 = new JSONArray(selectsysconfigone.getExtra());
        for(Object info1 : jsonArray1){

        JSONObject info = new JSONObject(info1);
        String baseurl= info.getStr("baseurl") ;
        String Tenant_id= info.getStr("Tenant_id") ;
        String sysOrgCode = info.getStr("code");
        // 获取token
            Map tokeninfo = getToken(info);
            String token = tokeninfo.get("token").toString();
            String sectionid = tokeninfo.get("setioncId").toString();
            if(StringUtils.isNotBlank(token)){
                pushandreturnService.saveData(354, String.valueOf(info),selectsysconfigone.getRemark()+"获取token",token);
            }else{
                return;
            }


        // ---------------------------------------------------- 人员信息推送 ---------------------------------------------
         // 1. 获取未推送人员信息数据
        List<StaffBaseInfo> guanlis = staffBaseInfoService.getPushList("管理人员",sysOrgCode);
        List<StaffBaseInfo> shishis = staffBaseInfoService.getPushList("实施人员",sysOrgCode);
        // 2. 推送人员信息
        String back = postData1(guanlis,sectionid,baseurl,token,Tenant_id,"管理员");
        JSONObject backinfo = new JSONObject(back);
        if((Boolean)backinfo.get("success")){
            guanlis.stream().forEach(one->{ one.setPushstatus(1); });
        }else{
            guanlis.stream().forEach(one->{ one.setPushstatus(2); });
        }
        // 3.更新推送状态
        staffBaseInfoService.updateBatchById(guanlis);
        String backs = postData1(shishis,sectionid,baseurl,token,Tenant_id,"实施员");
        JSONObject backsinfo = new JSONObject(backs);
        if((Boolean)backsinfo.get("success")){
            shishis.stream().forEach(one->{ one.setPushstatus(1); });
        }else{
            shishis.stream().forEach(one->{ one.setPushstatus(2); });
        }
        staffBaseInfoService.updateBatchById(shishis);
        // ----------------------------------------------------考勤信息 -------------------------------------------------
        // 1.获取考勤信息
        List<StaffWorkDetail> StaffWorkDetail = staffWorkDetailService.getPushList(sysOrgCode);
        // 2. 推送考勤信息
        String backss = postWorkDetail(StaffWorkDetail,sectionid,baseurl,token,Tenant_id);
        log.info("淮阜枢纽考勤"+backss);
        // 3. 考勤信息推送情况回写
        JSONObject backssinfo = new JSONObject(backss);
        if((Boolean)backssinfo.get("success")){
            StaffWorkDetail.stream().forEach(one->{ one.setPushstatus(1); });
        }else{
            StaffWorkDetail.stream().forEach(one->{ one.setPushstatus(2); });
        }
        staffWorkDetailService.updateBatchById(StaffWorkDetail);
        // ---------------------------------------------------- 查询统计信息 ---------------------------------------------
        List<Map> statistics = staffBaseWorkService.getStatistics(sysOrgCode,startsKaoqing);
//            statistics.stream().forEach(one->{
//                if( endkaoqing.compareTo(String.valueOf(one.get("time")))>0){
//                    endkaoqing = String.valueOf(one.get("time"));
//                }
//            });
        // 推送统计信息
        String s = postStatistics(statistics, sectionid, baseurl, token, Tenant_id);
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format4 = ft.format(new Date());
        sysConfigService.updateSysConfigDate(JobUtil.HFSN_MJKQ_SJG,format4);
    }

    Map getToken(JSONObject info ){
        String baseurl= info.getStr("baseurl") ;
        String Tenant_id= info.getStr("Tenant_id") ;
        String appkey= info.getStr("appkey") ;
        String appSecret= info.getStr("appSecret") ;
        String url = baseurl + "/sys/getTokenByAkSk";
        String token = "";
        Map s = new HashMap<>();

        String back = HttpRequest.get(url+"?appKey="+appkey+"&appSecret="+appSecret)
                .header("Tenant_id", Tenant_id)
                .execute()
                .body();
        JSONObject jsonObject2 = new JSONObject(back);
        Boolean success = (Boolean) jsonObject2.get("success");
        if(success){
            JSONObject jsonObject3 = new JSONObject(jsonObject2.get("result"));
            token = (String) jsonObject3.get("token");
            s.put("setioncId",(String) jsonObject3.get("sectionId"));
        }else{
          token = back;
        }
        s.put("token",token);

        return s;
    }

    // 推送人员信息
    String postData1( List<StaffBaseInfo> mans,String section,String baseurl,String token,String tenid,String type){

        JSONObject sendObject = JSONUtil.createObj();
        JSONArray jsonArray1 = new JSONArray();
        String url = "";
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format4 = ft.format(new Date());
        // 管理员
        if(type.equals("管理员")){
            url = baseurl+"/smartcity/comm/v1/managersList";
            for(StaffBaseInfo one : mans){
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.set("workerName",one.getXingming());
                jsonObject1.set("idCardType",one.getZhengjiantype());
                jsonObject1.set("idCardNumber",one.getShenfenz());
                jsonObject1.set("cellPhone",one.getDianhua());
                jsonObject1.set("userUnit",one.getDanwei());
                jsonObject1.set("enteryStatus",one.getJintuichangzhuangtai());
                jsonObject1.set("planEnterTime",ObjectUtils.isEmpty(one.getJihuajingchangtime())?"": ft.format(one.getJihuajingchangtime()) );
                jsonObject1.set("planOutTime",ObjectUtils.isEmpty(one.getJihualichangtime())?"":ft.format(one.getJihualichangtime()));
                jsonObject1.set("realEnterTime",ObjectUtils.isEmpty(one.getShijijinchangtime())?"":ft.format(one.getShijijinchangtime()));
                jsonObject1.set("postType",one.getShifoubixudaogang());
                jsonArray1.add(jsonObject1);
            }
        }else{
            // 施工员
            url = baseurl+"/smartcity/comm/v1/constructionCrewsList";
            for(StaffBaseInfo one : mans){
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.set("workerName",one.getXingming());
                jsonObject1.set("idCardType",one.getZhengjiantype());
                jsonObject1.set("idCardNumber",one.getShenfenz());
                jsonObject1.set("cellPhone",one.getDianhua());
                jsonObject1.set("workType",one.getGongzhong());
                jsonObject1.set("workArea",one.getGongqu());
                jsonArray1.add(jsonObject1);
            }
        }

        sendObject.set("sectionId",section);
        sendObject.set("data",jsonArray1);
        String body = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("X-Access-Token", token)
                .header("Tenant_id", tenid)
                .body(String.valueOf(sendObject))
                .execute()
                .body();
        pushandreturnService.saveData(354, String.valueOf(sendObject),"淮阜枢纽考勤"+type,body);
        return body;

    }

    String postWorkDetail( List<StaffWorkDetail> StaffWorkDetail,String section,String baseurl,String token,String tenid){
        JSONObject sendObject = JSONUtil.createObj();
        JSONArray jsonArray1 = new JSONArray();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         // 人员考核信息
         String   url = baseurl+"/smartcity/comm/v1/attendanceList";
            for(StaffWorkDetail one : StaffWorkDetail){
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.set("idCardNumber",one.getShengfenz());
                jsonObject1.set("swipeType",one.getType());
                jsonObject1.set("cardSwipeTime", ObjectUtils.isEmpty(one.getDakatime())?"": ft.format(one.getDakatime()) );
                jsonArray1.add(jsonObject1);
            }
        sendObject.set("sectionId",section);
        sendObject.set("data",jsonArray1);
        String body = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("X-Access-Token", token)
                .header("Tenant_id", tenid)
                .body(String.valueOf(sendObject))
                .execute()
                .body();
        pushandreturnService.saveData(354, String.valueOf(sendObject),"淮阜枢纽考勤信息",body);
        return body;

    }

    String postStatistics( List<Map> statistics,String section,String baseurl,String token,String tenid){
        JSONObject sendObject = JSONUtil.createObj();
        JSONArray jsonArray1 = new JSONArray();
        // 人员考核信息
        String   url = baseurl+"/smartcity/comm/v1/attendanceStatistics";
        sendObject.set("sectionId",section);
        sendObject.set("data",statistics);
        String body = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("X-Access-Token", token)
                .header("Tenant_id", tenid)
                .body(String.valueOf(sendObject))
                .execute()
                .body();
        pushandreturnService.saveData(354, String.valueOf(sendObject),"淮阜枢纽考勤统计信息",body);

        return body;

    }


}
