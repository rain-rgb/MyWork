package org.jeecg.modules.job.huaifushuniu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.staffbase.entity.StaffBaseInfo;
import com.trtm.iot.staffbase.entity.StaffBaseWork;
import com.trtm.iot.staffbase.entity.StaffWorkDetail;
import com.trtm.iot.staffbase.service.IStaffBaseInfoService;
import com.trtm.iot.staffbase.service.IStaffBaseWorkService;
import com.trtm.iot.staffbase.service.IStaffWorkDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.DingTalkUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * \* @author: Xx
 * \* Date: 2021/10/19
 * \* Time: 14:44
 * \* Description:  连云港(钉考勤数据接入) 获取用户打卡信息
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LYGDingTalkJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private DingTalkUtil dingTalkUtil;

    @Autowired
    private IStaffBaseInfoService staffBaseInfoService;
    @Autowired
    private IStaffBaseWorkService staffBaseWorkService;
    @Autowired
    private IStaffWorkDetailService staffWorkDetailService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private static String APPKEY="dingj3hxlqj7oaxl0mwg";//
    private static String APPSECRET="ZtGl3me62FuQE5RcFW9ayTGU0yQ4Y7rCw4bKfrswK-wtYeiNNPz0BGDnDeWrgUpY";

    private static final HashMap<Long, String> departs = new HashMap<Long, String>() {{
//        put(973764057L, "A05A01A02A09A01A01A01A03");//土建施工及部分设备安装
//        put(983012050L, "A05A01A02A09A01A01A01A03");//土建施工及部分设备安装（劳务人员）
        put(925850362L, "A05A01A02A07A01A01A01");//总监办
        put(925899275L, "A05A01A02A07A01A01A01A01");//项目部
         }};


    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LYG_MJKQ_PULL);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到钉钉考勤定时任务配置信息" + DateUtils.now()));
            return;
        }
        String token = dingTalkUtil.getTokens(APPKEY,APPSECRET);
        long offset = 0L;

        for(Long key : departs.keySet() ){
            Long departid = key;
        Boolean hasMore = true;
        while (hasMore && StringUtils.isNotBlank(String.valueOf(selectsysconfigone.getToken()))) {
            if (hasMore) {
                //  获取部门用户userid列表
                Map map = dingTalkUtil.GetUserMessageDingTalks(offset, token, departid);
                List<OapiV2UserListResponse.ListUserResponse> userlist = (List<OapiV2UserListResponse.ListUserResponse>) map.get("userlist");
                hasMore = (Boolean) map.get("hasMore");
                if (hasMore) {
                    offset = (long) map.get("next_cursor");
                }
                // 循环用户放入职员信息表
                if (userlist.size() > 0) {
                    for (OapiV2UserListResponse.ListUserResponse userlist1 : userlist) {
                        StaffBaseInfo one = new StaffBaseInfo();
                        String name = userlist1.getName();
                        String userid = userlist1.getUserid();
                        // 根据userid查询相关信息
                        QueryWrapper<StaffBaseInfo> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("userid", userid).or().eq(StringUtils.isNotBlank(userlist1.getMobile()),"dianhua",userlist1.getMobile());
                        List<StaffBaseInfo> list = staffBaseInfoService.list(queryWrapper);
                        if (list.size() == 0) { //不存在就新增
                            one.setUserid(userid);
                            one.setXingming(name);
                            one.setSysOrgCode(departs.get(departid));
                            one.setDianhua(userlist1.getMobile());
                            one.setShenfenz(userid);
                            staffBaseInfoService.save(one);
                        }else{
                            if(StringUtils.isEmpty(list.get(0).getUserid())){
                                one.setUserid(userid);
                                one.setId(list.get(0).getId());
                                staffBaseInfoService.updateById(one);

                            }
                        }
                    }
                }
            } else {
                break;
            }
        }

        // 查询现有用户userid
        List<String> departid1 = staffBaseInfoService.userIdList(departs.get(departid));
        List<List<String>> batchs = batch(departid1, 50);
        // 根据userid查询考勤记录，userids最多不能超过50个[查询当天00:00:00 到 23:59:59] 9:30一次，19:00 一次  23：00 一次
        String strDateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            String format = sdf.format(new Date());
        if(StringUtils.isNotBlank(selectsysconfigone.getCurdate())){
            format = selectsysconfigone.getCurdate();
        }
        String startTime = format + " 00:00:00";
        String endTime = format + " 23:59:59";
        for(int i = 0 ;i<batchs.size();i++){
            List<OapiAttendanceListRecordResponse.Recordresult> recordresult = dingTalkUtil.PostListRecord(batchs.get(i), startTime, endTime, token);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(recordresult);
            pushandreturnService.saveData(355,departid+":"+format,"淮阜考勤获取列表"+i,json);
            if (recordresult != null && recordresult.size() > 0 ) {
                for (OapiAttendanceListRecordResponse.Recordresult item : recordresult) {

                    // 查询对应用户
                    QueryWrapper<StaffBaseInfo> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("userid", item.getUserId());
                    StaffBaseInfo one = staffBaseInfoService.getOne(queryWrapper);
                    //是否有考勤记录，有则更新，无则新增
                    QueryWrapper<StaffBaseWork> queryWrapperwork = new QueryWrapper<>();
                    queryWrapperwork.eq("dianhua", one.getDianhua());
                    queryWrapperwork.eq("kaoqriq", format);
                    StaffBaseWork one1 = staffBaseWorkService.getOne(queryWrapperwork);
                    StaffBaseWork work = new StaffBaseWork();
                    if (!ObjectUtils.isEmpty(one1)) {
                        work = one1;
                    } else {
                        BeanUtils.copyProperties(one, work);
                        work.setId(null);
                    }
                    work.setKaoqriq(sdf.parse(format));

                    StaffWorkDetail detail = new StaffWorkDetail();
                    if (item.getCheckType() == null) {
                        item.setCheckType("--");
                        detail.setType("3");

                    }
                    if (item.getCheckType().equals("OnDuty")) {
                        detail.setType("1");
                        work.setShangbantime(item.getUserCheckTime());
                    } else if (item.getCheckType().equals("OffDuty")) {
                        detail.setType("2");
                        work.setXiabantime(item.getUserCheckTime());
                    }
                    // 更新保存打卡记录
                    staffBaseWorkService.saveOrUpdate(work);

                    // 是否有打卡记录，有则更新，无则新增
                    QueryWrapper<StaffWorkDetail> queryWrapperdetail = new QueryWrapper<>();
                    queryWrapperdetail.eq("shengfenz", one.getShenfenz());
                    queryWrapperdetail.eq("type", detail.getType());
                    queryWrapperdetail.likeRight("dakatime",format);
                    StaffWorkDetail detail1 = staffWorkDetailService.getOne(queryWrapperdetail);
                    if (!ObjectUtils.isEmpty(detail1)) {
                        detail = detail1;
                    } else {
                        detail.setSysOrgCode(departs.get(departid));
                        detail.setWorkname(item.getUserLatitude() + "," + item.getUserLatitude());
                        detail.setShengfenz(one.getShenfenz());
                    }
                    detail.setDakatime(item.getUserCheckTime());

                    staffWorkDetailService.saveOrUpdate(detail);


                }


            }
         }
        }
    }


    public   List<List<String>> batch(List<String> list, int batchSize) {
        return IntStream.range(0, (list.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> list.subList(i * batchSize, Math.min((i + 1) * batchSize, list.size())))
                .collect(Collectors.toList());
    }


}
