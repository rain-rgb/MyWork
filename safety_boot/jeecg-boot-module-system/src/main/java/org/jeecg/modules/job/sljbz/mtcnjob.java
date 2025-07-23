package org.jeecg.modules.job.sljbz;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.api.utils.crypt.MD5;
import com.trtm.iot.devicemixpilerealdata.entity.DeviceMixpileRealdata;
import com.trtm.iot.devicemixpilerealdata.service.IDeviceMixpileRealdataService;
import com.trtm.iot.devicemixpilestatic.entity.DeviceMixpileStatic;
import com.trtm.iot.devicemixpilestatic.service.IDeviceMixpileStaticService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class mtcnjob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDeviceMixpileStaticService deviceMixpileStaticService;

    private static final String code = "gaoxun";
    private static final String secretkey ="9f164bcc3976a8348b584c02fb1046cb";

    private static final String url = "http://112.90.176.194:60000/";//ip

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SLJBZ_MRCN);//水泥搅拌桩大屏-接口文档v1.3 每天产能
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到送货短信提醒定时任务的配置信息" + DateUtils.now()));
            return;
        }
        String orgcode = "A05A16A01A02";//民古路交建指挥部
        SysDepart departname = sysDepartService.queryone(orgcode);
        String urling = "smwdata/dailyCapacity?";

        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");

        List<ShebeiInfo> shebeiInfos = shebeiInfoService.shebeilists(16,orgcode+"%");
        if (shebeiInfos.size() > 0){
            for (ShebeiInfo shebeiInfo :shebeiInfos){
                List<DeviceMixpileStatic> selectbyshebeilist = deviceMixpileStaticService.selectbyshebeilist(shebeiInfo.getSbjno());
                if (selectbyshebeilist.size() > 0){
                    for (DeviceMixpileStatic queryone :selectbyshebeilist){
                        if (queryone != null){
                            JSONObject data = JSONUtil.createObj();
                            int pilecount = Math.round(Float.parseFloat(queryone.getPilecount()));
                            data.set("capacityMi",queryone.getWorklength());//产能（米）
                            data.set("capacityNum", String.valueOf(pilecount));//产能（根）
                            try {
                                data.set("ctime",dateFormat.parse(queryone.getStadate()));//时间
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            data.set("projectId",orgcode);//项目编码
                            data.set("projectName",departname.getDepartNameAbbr());//项目名
                            data.set("smwEquipmentCode",shebeiInfo.getSbjno());//设备编码
                            data.set("smwEquipmentName",shebeiInfo.getSbname());//设备名称
                            Date date = new Date();
                            long time = date.getTime();
                            String singes = code + secretkey + data + time;
                            String singe = MD5.toMD5(singes);
                            boolean code = getCode(urling, time, singe, data);
                            if (code){
                                log.info("推送成功！！！");
                            }else {
                                log.info("推送失败！！！");
                            }
                        }else {
                            log.info("该设备没有实时数据。");
                        }
                    }
                }
            }
        }else {
            log.info("该项目下没有水泥搅拌桩设备。");
        }

    }
    // url
    public static Boolean getCode(String urling,long time,String singe,JSONObject data) {
//        System.out.println(url+urling+"code="+code+"&singe="+singe+"&time="+time);
//        System.out.println(data);
        String body = HttpRequest.post(url+urling+"code="+code+"&singe="+singe+"&time="+time)
                .header("Content-Type", "application/json")
                .body(String.valueOf(data))
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        return (boolean) jsonObject1.get("success");
    }
}
