package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicepipepilehistorydataone.controller.SignUtil;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import com.trtm.iot.devicepipepilehistorydatapart.entity.DevicePipepileHistorydataPart;
import com.trtm.iot.devicepipepilehistorydatapart.service.IDevicePipepileHistorydataPartService;
import com.trtm.iot.devicepipepilereport.entity.DevicePipepileReport;
import com.trtm.iot.devicepipepilereport.service.IDevicePipepileReportService;
import com.trtm.iot.jtwbs.entity.Jtwbs;
import com.trtm.iot.jtwbs.service.IJtwbsService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class GzbzlJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IJtwbsService jtwbsService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDevicePipepileHistorydataOneService devicePipepileHistorydataOneService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.GZ_ZBZL_SJTS);//管桩质保资料数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到管桩质保资料数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();

        QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("org_category","7");
        List<SysDepart> list1 = sysDepartService.list(queryWrapper);
        if (list1.size() > 0){
            for (SysDepart l :list1){
                if (l.getOrgCode() != null){
                    Jtwbs jtwbs = jtwbsService.selectbycogcode(l.getOrgCode());
                    List<String> shebeiList = shebeiInfoService.selectShebeiList(l.getOrgCode(), 61);
                    if (jtwbs != null && shebeiList.size() > 0){
                        QueryWrapper<DevicePipepileHistorydataOne> queryWrapper1 = new QueryWrapper<>();
                        queryWrapper1.gt("id",curid);
                        queryWrapper1.in("shebeino",shebeiList);
                        queryWrapper1.last("limit 100");
                        List<DevicePipepileHistorydataOne> list = devicePipepileHistorydataOneService.list(queryWrapper1);

                        if (list.size() > 0){
                            for (DevicePipepileHistorydataOne l1 :list){
                                JSONObject params = new JSONObject();
                                params.set("old_id",jtwbs.getSectionid());
                                params.set("gcbw",l1.getPileMileage());
                                params.set("zh",l1.getPileNo());
                                params.set("wcsj",l1.getPileTime());
                                params.set("sjzc",l1.getPileDesigndep());
                                params.set("sgcd",l1.getPileRealdep());
                                List lists = new ArrayList();
                                lists.add(params);
                                int i = queryPagegxsListed(lists);
                                if (i == 200){
                                    l1.setZbzl(1);
                                    devicePipepileHistorydataOneService.updateById(l1);
                                    log.info("管桩质保资料数据推送 成功！！！");
                                }else {
                                    l1.setZbzl(40);
                                    devicePipepileHistorydataOneService.updateById(l1);
                                    log.info("管桩质保资料数据推送 失败！！！");
                                }
                                sysConfigService.updateSysConfig(JobUtil.GZ_ZBZL_SJTS, l1.getId());//根据传过来的唯一值来修改当前判断到的数据id
                            }
                        }
                    }
                }
            }
        }
    }

    public static int queryPagegxsListed(List list) {//A05A01A05A01A01A01A03
        long l = System.currentTimeMillis();
        SecureRandom secureRandom = new SecureRandom();
        int i = secureRandom.nextInt();
        String x_rio_seq = String.valueOf(i);
        String timestamp = String.valueOf(l);
        String signature = SignUtil.signature("e6540129236a44639778678879ce47bb", x_rio_seq, timestamp);
        int code = getCode(signature, x_rio_seq, timestamp,list);
        return code;
    }

    public static int getCode(String signature,String x_rio_seq,String timestamp,List list) {
        String encode = "";
        try {
            encode = URLEncoder.encode(list.toString(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String body = HttpRequest.post("http://fjhmtd.com:32221/glaf/website/ws/execute/api/crud?appId=b598a75acb65427c979d0bdd317f9846&useId=b5b75b4b70064f1398d506d62884776f&type=cu&params="+encode)
                .header("signature", signature)
                .header("x-rio-seq", x_rio_seq)
                .header("timestamp", timestamp)
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        return (Integer) jsonObject1.get("statusCode");
    }
}
