package org.jeecg.modules.job.ydzhihuiyongdianJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhydreal.entity.DeviceElectricRealdata;
import com.trtm.iot.zhydreal.service.IDeviceElectricRealdataService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.util.SignUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ydzhihuiyongdianJob：
 * @Description TODO
 * @Author 55314
 * @Date 2021/12/1 13:17
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ydzhihuiyongdianJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDeviceElectricRealdataService deviceElectricRealdataService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YD_ZHIHUIYONGDIAN);//义东智慧用电
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东智慧用电定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东智慧用电的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1= Arrays.asList(split);
        List<DeviceElectricRealdata> selecthntbhzones = deviceElectricRealdataService.selectLists(strsToList1,curid);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无义东智慧用电未推送数据" + DateUtils.now()));
            return;
        }
        int id=0;
        List list = new ArrayList();
        JSONObject mapError = JSONUtil.createObj();
        mapError.set("useId", "1a2af5a06c0049bd8b3bc262c0cc113d");
        mapError.set("appId", "8d00e55194e74b4396fd560296ed7364");
        mapError.set("type", "cu");
        for (DeviceElectricRealdata selecthntbhzone : selecthntbhzones) {
            id=selecthntbhzone.getId();
            JSONObject publicPitchList1 = new JSONObject();

            publicPitchList1.set("sb_id",selecthntbhzone.getImei());
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(selecthntbhzone.getImei());
            publicPitchList1.set("ypbh",sbjwd.getSbname());
            publicPitchList1.set("yj_time",selecthntbhzone.getElecdate());
            publicPitchList1.set("energya",selecthntbhzone.getEnergya());
            publicPitchList1.set("energyb",selecthntbhzone.getEnergyb());
            publicPitchList1.set("energyc",selecthntbhzone.getEnergyc());
            publicPitchList1.set("tempa",selecthntbhzone.getTempa());
            publicPitchList1.set("tempb",selecthntbhzone.getTempb());
            publicPitchList1.set("tempc",selecthntbhzone.getTempc());
            publicPitchList1.set("tempn",selecthntbhzone.getTempn());
            publicPitchList1.set("voltagea",selecthntbhzone.getVoltagea());
            publicPitchList1.set("voltageb",selecthntbhzone.getVoltageb());
            publicPitchList1.set("voltagec",selecthntbhzone.getVoltagec());
            publicPitchList1.set("electricitya",selecthntbhzone.getElectricitya());
            publicPitchList1.set("electricityb",selecthntbhzone.getElectricityb());
            publicPitchList1.set("electricityc",selecthntbhzone.getElectricityc());
            publicPitchList1.set("seepelectricity",selecthntbhzone.getSeepelectricity());
            list.add(publicPitchList1);
        }
        SecureRandom random = new SecureRandom();
        String s = String.valueOf(random.nextInt());
        String time = String.valueOf(System.currentTimeMillis());
        String signature = SignUtil.signature("ef1264412c9b491da5df7690eb1da844", s, time);
        String encode = URLEncoder.encode(list.toString());
        String url = "http://fjhmtd.com:15131/glaf/website/ws/execute/api/crud?appId=8d00e55194e74b4396fd560296ed7364&useId=1a2af5a06c0049bd8b3bc262c0cc113d&type=cu&params="+encode;
        String back = HttpRequest.post(url)
                .header("x-rio-seq", s)
                .header("signature", signature)
                .header("timestamp", time)
                .timeout(20000)
                .execute().body();
        JSONObject jsonObject1 = new JSONObject(back);
        Integer codes = Integer.parseInt(jsonObject1.get("statusCode").toString());
        if(codes==200){
            log.info(String.format("义东智慧用电数据推送成功!" + id+"Json数据"+mapError));
        }else{
            log.info(String.format("义东智慧用电数据推送失败!" + id+"Json数据"+mapError));
        }
        sysConfigService.updateSysConfig(JobUtil.YD_ZHIHUIYONGDIAN, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
