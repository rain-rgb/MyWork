package org.jeecg.modules.job.gxjob;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.taobao.api.ApiException;
import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardRecord;
import com.trtm.iot.entranceGuardRecord.service.IEntranceGuardRecordService;
import com.trtm.iot.entranceguardrecordreal.entity.EntranceGuardRecordReal;
import com.trtm.iot.entranceguardrecordreal.service.IEntranceGuardRecordRealService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.gxjob.statusEnum.recordStatus;
import org.jeecg.modules.job.jobutil.GXDingTalkUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 高迅钉钉考勤数据获取
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class EntranceGuardRecordJob implements Job {


    @Autowired
    private IEntranceGuardRecordRealService entranceGuardRecordRealService;
    @Autowired
    private IEntranceGuardRecordService entranceGuardRecordService;

    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Autowired
    ISysConfigService sysConfigService;

    private static Integer status;
    // 测试
    private static String apiKey="36098891f1e04fbaabcff811b6d71afc";
    private static String apiSecret="18930cf43f8e410aa95facdcdffea3c4";
    private static String tokenurl = "https://space.uni-ubi.com/saas/open/api/auth/generateApiToken";
    private static String url = "https://space.uni-ubi.com/saas/open/api/transit/getTransitList";

    private static int pagesize = 100;


    public String getAccessToken() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("apiKey",apiKey);
        jsonObject.set("apiSecret",apiSecret);

        String back1 = HttpRequest.post(tokenurl)
                .body(String.valueOf(jsonObject))
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(back1);
        if(jsonObject1.get("success").equals(true)){
         return (String) jsonObject1.get("data");
        }
        return  "获取token失败";
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {


        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.MJKQ_NEW_YF);

        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到门禁考勤的配置信息" + DateUtils.now()));
            return;
        }

        // 获取门禁考勤设备
       // List<ShebeiInfo> selectbhzone = shebeiInfoService.selectbhzone(18);
//        String extra = selectsysconfigone.getExtra();
//        JSONObject jsonObject = JSONUtil.parseObj(extra);
        //当前数据检测到的数据时间
        Long startTime = Long.valueOf(selectsysconfigone.getCurdate());
        Long endtime = (new Date()).getTime();
        Long setendtime = Long.valueOf(selectsysconfigone.getCurdate());

        // 获取token
        String apitoken = getAccessToken();

      //  for(ShebeiInfo item : selectbhzone ){

            // 设置参数
            JSONObject sendDate = new JSONObject();
           // sendDate.set("deviceNo",item.getSbjno());// 设备序列号

           int pageNo = 1;
            sendDate.set("admitType",1);// 员工
            sendDate.set("index",pageNo);// 开始时间
            sendDate.set("length",pagesize);// 查询数据条数
            sendDate.set("startTime",startTime);// 开始时间
            sendDate.set("endTime",endtime);// 结束时间

        Map r = getPages(sendDate,apitoken,setendtime);
        int page = (int) r.get("page");
        setendtime = (Long) r.get("setendtime");
          if(page > 0){
                for(int k = 0 ;k<page;k++){
                    sendDate.set("index",pageNo+k);// 开始时间
                    // 分页获取通行记录
                    JSONArray data = getList(sendDate, apitoken);

                    for(int i = 0;i<data.size();i++ ){
                        EntranceGuardRecordReal entranceGuardRecordReal = new EntranceGuardRecordReal();
                        EntranceGuardRecord entranceGuardRecord = new EntranceGuardRecord();
                        JSONObject one = (JSONObject) data.get(i);
                        try {
                            entranceGuardRecordReal.setOpentime(DateUtils.parseDate((String) one.get("transitTime"), "yyyy-MM-dd HH:mm:ss"));
                            entranceGuardRecordReal.setNames(StringUtils.isNotEmpty((String) one.get("admitName"))?(String) one.get("admitName"):"" );

                            if(!one.get("photoUrl").equals(null)){
                                entranceGuardRecordReal.setPic((String) one.get("photoUrl"));
                            }
                            entranceGuardRecordReal.setShebeino(StringUtils.isNotEmpty((String) one.get("deviceNo"))?(String) one.get("deviceNo"):"" );
                            entranceGuardRecordReal.setDepartname( StringUtils.isNotEmpty((String)  one.get("deviceName"))?(String) one.get("deviceName"):"" );
                            entranceGuardRecordReal.setCid(!ObjectUtil.isEmpty(one.get("admitId"))?(String) one.get("admitId"):"");
                          //  entranceGuardRecordReal.setCid((String) one.get("admitCode"));
                         //   boolean success = entranceGuardRecordRealService.save(entranceGuardRecordReal);
                            EntranceGuardRecordReal deviceNo = entranceGuardRecordRealService.getByshebi((String) one.get("deviceNo"));
                            if(ObjectUtil.isEmpty(deviceNo)){
                                entranceGuardRecordRealService.save(entranceGuardRecordReal);
                            }else {
                                entranceGuardRecordReal.setId(deviceNo.getId());
                                entranceGuardRecordRealService.updateById(entranceGuardRecordReal);
                            }
                            BeanUtils.copyProperties(entranceGuardRecordReal,entranceGuardRecord);
                            entranceGuardRecordService.save(entranceGuardRecord);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }


        sysConfigService.updateSysConfigDate(JobUtil.MJKQ_NEW_YF, String.valueOf((setendtime+10)));//根据传过来的唯一值来修改当前判断到的数据id

    }

    Map getPages(JSONObject sendDate,String apitoken,Long setendtime){
        Map r = new HashMap<>();

        // 请求历史记录
        int page = 0;
        String back = HttpRequest.post(url)
                .header("apiToken", apitoken)
                .header("Content-Type", "application/json")
                .body(String.valueOf(sendDate))
                .execute()
                .body();

        JSONObject jsonObject1 = new JSONObject(back);
        String codes = jsonObject1.get("msg").toString();

        if ("success".equals(codes)) {
            // 解析获取到的门禁数据
            JSONArray data = (JSONArray) jsonObject1.get("data");
            if(ObjectUtil.isEmpty(data)){
                page =  0;
            }else {
                setendtime = (Long) ((JSONObject) data.get(0)).get("showTime");
                JSONObject jsonObject2 = (JSONObject) jsonObject1.get("paginationOutput");
                int total = (int) jsonObject2.get("total");
                page = (int) Math.ceil((double) total / pagesize);
            }
        }

        r.put("page",page);
        r.put("setendtime",setendtime);
        return  r;
    }

    JSONArray getList(JSONObject sendDate,String apitoken){
        JSONArray data = new JSONArray();
        String back = HttpRequest.post(url)
                .header("apiToken", apitoken)
                .body(String.valueOf(sendDate))
                .execute()
                .body();

        JSONObject jsonObject1 = new JSONObject(back);
        String codes = jsonObject1.get("msg").toString();
        if ("success".equals(codes)) {
            // 解析获取到的门禁数据
             data = (JSONArray) jsonObject1.get("data");

        }
        return  data;
    }

}

