package org.jeecg.modules.job.weilanJob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.carmiles.entity.Carmiles;
import com.trtm.iot.carmiles.service.ICarmilesService;
import com.trtm.iot.clgl.entity.ClxxRealdata;
import com.trtm.iot.clgl.service.IClxxRealdataService;
import com.trtm.iot.frontDeviceWeilan.entity.FrontDeviceWeilan;
import com.trtm.iot.frontDeviceWeilan.service.IFrontDeviceWeilanService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.WeiLanUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.trtm.iot.util.GPSUtil.gps84_To_Gcj02;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ElectricFencemileJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IFrontDeviceWeilanService iFrontDeviceWeilanService;
    @Autowired
    private WeiLanUtil weiLanUtil;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private ICarmilesService carmilesService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private IClxxRealdataService iClxxRealdataService;
    //风控在线GPS定时任务(运料车)
    @Autowired
    private IFrontDeviceRealdataService realService;
    private static final String dataurl = "http://open.figps.com/api/";
    private static final String authstr="{" +
            "\"appid\":\"17398007785\"," +
            "\"time\":1635386457," +
            "\"signature\":\"193386bbc84e8a45d7599706e865bc31\"" +
            "}";

    private static final String statusurl = "device/status?accessToken=%s&account=17398007785";

    private String accessToken = "";

    private void doAuth() {
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(HttpUtil.post(dataurl+"auth", authstr));
        if (jsonObject.getInt("code") == 0) {
            accessToken = jsonObject.getStr("accessToken");
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //风控在线GPS定时任务(运料车)
        if (StrUtil.isBlank(accessToken)) {
            doAuth();
        }
        if (StrUtil.isNotBlank(accessToken)) {
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(HttpUtil.get(dataurl+String.format(statusurl,accessToken)));
            if (jsonObject != null && jsonObject.getInt("code") == 0) {
                JSONArray jsarray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsarray.size(); i++) {
                    cn.hutool.json.JSONObject json = jsarray.getJSONObject(i);
                    try {
                        lichengtj(accessToken,json);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                accessToken = "";
            }
        }
    }

    // 里程统计
    private void lichengtj(String accessToken, cn.hutool.json.JSONObject json) throws ParseException {
        long time = System.currentTimeMillis()/1000;
        String imei = json.getStr("imei");
        long startTime = time - 2592000;
        String url = "http://open.figps.com/api/device/miles?accessToken="+accessToken+"&startTime="+startTime+"&endTime="+time+"&imei="+imei;
        String body = HttpRequest.get(url)
                .execute()
                .body();

        cn.hutool.json.JSONObject jsonObject1 = new cn.hutool.json.JSONObject(body);
        String miles = jsonObject1.getStr("miles");
        String runTime = jsonObject1.getStr("runTime");
        QueryWrapper<Carmiles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shebei_no",imei);
        Carmiles one = carmilesService.getOne(queryWrapper);
        if (one != null){
            one.setMiles(Float.valueOf(miles));
            one.setJobruntime(new Date());
            one.setRuntime(Integer.valueOf(runTime));
            carmilesService.updateById(one);
        }else {
            Carmiles carmiles = new Carmiles();
            carmiles.setShebeiNo(imei);
            carmiles.setMiles(Float.valueOf(miles));
            carmiles.setJobruntime(new Date());
            carmiles.setRuntime(Integer.valueOf(runTime));
            carmilesService.save(carmiles);
        }
    }
}