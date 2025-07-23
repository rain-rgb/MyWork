package org.jeecg.modules.job.huaifushuniu;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardRecord;
import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardType;
import com.trtm.iot.entranceGuardRecord.service.IEntranceGuardRecordService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.staffbase.entity.StaffBaseInfo;
import com.trtm.iot.staffbase.entity.StaffBaseWork;
import com.trtm.iot.staffbase.entity.StaffWorkDetail;
import com.trtm.iot.staffbase.service.IStaffBaseInfoService;
import com.trtm.iot.staffbase.service.IStaffBaseWorkService;
import com.trtm.iot.staffbase.service.IStaffWorkDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import freemarker.ext.servlet.IncludePage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.DingTalkUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
 * \* Description:  淮阜枢纽(门禁考勤数据接入) 获取用户打卡信息
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class HFMengJingKaoQingJob implements Job {
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

    private static String code="202410291526";//
    private static String sessionToken="GrsIHagK8muLfUGnFlit5FQ9Y7s5ULE6jSkPBDfgtmxRJebppwHh2InfW1pTwxc7";

    private static final HashMap<Long, String> departs = new HashMap<Long, String>() {{
        put(973764057L, "A05A01A02A09A01A01A01A03");//土建施工及部分设备安装
        put(983012050L, "A05A01A02A09A01A01A01A03");//土建施工及部分设备安装（劳务人员）
        put(973784034L, "A05A01A02A09A01A01A01");//建成监理
        put(978128095L, "A05A01A02A09A01A01A01A04");//安全咨询
         }};

    @Autowired
    private IEntranceGuardRecordService entranceGuardRecordService;

    private static final  JSONObject loginobj = new JSONObject(){{
        put("username", "2024102915261");
        put("password", "123456");
    } };

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HFSN_MJKQ_DEVICE);


        // 1 获取token信息
        String post = HttpRequest.post("http://114.55.134.184:3001/apis/user/login")
                .header("Content-Type", "application/json")
                .body(String.valueOf(loginobj))
                .execute()
                .body();
        JSONObject postObject = new JSONObject(post);
        String token = ((JSONObject) postObject.get("d")).get("token").toString();

        String strDateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date());
        if(StringUtils.isNotBlank(selectsysconfigone.getCurdate())){
            format = selectsysconfigone.getCurdate();
        }

//        // 定义起始日期和结束日期
//        LocalDate startDate = LocalDate.of(2025, 2, 13);
//        LocalDate endDate = LocalDate.of(2025, 2, 24);
//        // 循环打印日期
//        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
//            format = String.valueOf(date);


        // 2 获取考勤信息并保存
        String url = "http://114.55.134.184:3001/openApis/projectServices/getAtds?code=202410291526&beginDay="+format+"&endDay="+format+"&sessionToken="+token;
        String body = HttpRequest.get(url)
                .header("Content-Type", "application/json")
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        Integer codes = (Integer) jsonObject1.get("code");
        if (codes==200){
            JSONArray list = jsonObject1.getJSONArray("d");
            if (list.size()>0) {
                for(Object one : list ){
                    // 获取考勤数据
                   String idNumber = ((JSONObject) one).get("idNumber").toString();
                    String firstEnterTime = ((JSONObject) one).get("firstEnterTime").toString();
                    String lastOutTime = ((JSONObject) one).get("lastOutTime").toString();
                    String day = ((JSONObject) one).get("day").toString();

                    // 查询对应用户
                    QueryWrapper<StaffBaseInfo> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("shenfenz", idNumber);
                    StaffBaseInfo staffone = staffBaseInfoService.getOne(queryWrapper);
                    //是否有考勤记录，有则更新，无则新增
                    QueryWrapper<StaffBaseWork> queryWrapperwork = new QueryWrapper<>();
                    queryWrapperwork.eq("shenfenz", idNumber);
                    queryWrapperwork.eq("kaoqriq", day);
                    StaffBaseWork work1 = staffBaseWorkService.getOne(queryWrapperwork);
                    StaffBaseWork work = new StaffBaseWork();
                    // 检测是否存在考勤信息，存在则更新，不存在则新增
                    if (!ObjectUtils.isEmpty(work1)) {
                        work.setId(work1.getId());
                    } else {
                        if(! ObjectUtils.isEmpty(staffone)){
                            BeanUtils.copyProperties(staffone, work);
                            work.setId(null);
                        }else{

                            pushandreturnService.saveData(356,day,"淮阜门禁考勤人员未登记",((JSONObject) one).toString());
                        }

                    }
                    if(StringUtils.isNotBlank(firstEnterTime)){ work.setShangbantime(sdf2.parse(firstEnterTime)); }
                    if(StringUtils.isNotBlank(lastOutTime)){ work.setXiabantime(sdf2.parse(lastOutTime)); }
                    work.setKaoqriq(sdf.parse(day));

                    // 更新保存打卡记录
                    staffBaseWorkService.saveOrUpdate(work);

                }

            }else {
                pushandreturnService.saveData(356,format,"淮阜门禁考勤人员获取失败!!!",jsonObject1.toString());
            }
        }

        // 3 获取门禁信息并保存

        String url2 = "http://114.55.134.184:3001/openApis/projectServices/getProjectRecord?code=202410291526&day="+format+"&sessionToken="+token;
        String body2 = HttpRequest.get(url2)
                .header("Content-Type", "application/json")
                .execute()
                .body();
        System.out.println(body);
        cn.hutool.json.JSONObject jsonObject2 = new cn.hutool.json.JSONObject(body2);
        Integer codes2 = (Integer) jsonObject1.get("code");
        if (codes2==200){
            JSONArray list = jsonObject2.getJSONArray("d");
            for(Object one : list ){
                // 获取考勤数据
                String idNumber = ((JSONObject) one).get("idNumber").toString();
                String name = ((JSONObject) one).get("name").toString();
                String time = ((JSONObject) one).get("time").toString();
                String photo = ((JSONObject) one).get("photo").toString();
                String status = ((JSONObject) one).get("status").toString();// 1 进⻔ 2出⻔
                String deviceUuid = ((JSONObject) one).get("deviceUuid").toString();

                QueryWrapper<StaffBaseInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("shenfenz", idNumber);
                StaffBaseInfo staffone = staffBaseInfoService.getOne(queryWrapper);


                EntranceGuardRecord kaoqing =  new EntranceGuardRecord();
                kaoqing.setOpentime(sdf2.parse(time));
                kaoqing.setShebeino(deviceUuid);
                kaoqing.setNames(name);
                kaoqing.setPic(photo);
                kaoqing.setStatus(Integer.valueOf(status));
                kaoqing.setCid(idNumber);
                kaoqing.setInfo("土建施工及部分设备安装");
                if(!ObjectUtils.isEmpty(staffone)){
                    kaoqing.setDepartname(staffone.getBanzu());
                }
                QueryWrapper<EntranceGuardRecord> queryWrapper1 = new QueryWrapper<>(kaoqing);
                EntranceGuardRecord one1 = entranceGuardRecordService.getOne(queryWrapper1);
                if(ObjectUtils.isEmpty(one1)){
                    entranceGuardRecordService.save(kaoqing);
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
