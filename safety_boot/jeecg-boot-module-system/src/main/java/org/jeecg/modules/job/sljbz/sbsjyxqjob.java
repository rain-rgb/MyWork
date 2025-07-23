package org.jeecg.modules.job.sljbz;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.api.utils.crypt.MD5;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class sbsjyxqjob implements Job {
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SLJBZ_SJXQ);//水泥搅拌桩大屏-接口文档v1.3 设备数据与详情
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到送货短信提醒定时任务的配置信息" + DateUtils.now()));
            return;
        }
        String orgcode = "A05A16A01A02";//民古路交建指挥部
        SysDepart departname = sysDepartService.queryone(orgcode);
        String urling = "smwdata/equipmentDetail?";


        List<ShebeiInfo> shebeiInfos = shebeiInfoService.shebeilists(16,orgcode+"%");
        List<ShebeiInfo> selecegcsb = shebeiInfoService.selecegcsb(16, orgcode);

        JSONObject data = JSONUtil.createObj();
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        data.set("onlineEquipment",selecegcsb.size());//在线设备数
        data.set("totalEquipment", shebeiInfos.size());//总设备数
        data.set("projectId",orgcode);//项目编码
        data.set("projectName",departname.getDepartNameAbbr());//项目名
        if (shebeiInfos.size() > 0){
            for (ShebeiInfo shebeiInfo :shebeiInfos){

                DeviceMixpileStatic queryone = deviceMixpileStaticService.selecshebeilist(shebeiInfo.getSbjno());
                if (queryone != null){
                    HashMap<String, Object> map = new HashMap<>();
                    int pilecount = Math.round(Float.parseFloat(queryone.getPilecount()));
                    map.put("ashNum",queryone.getAllash());//灰量（kg）
                    map.put("equipmentCode",shebeiInfo.getSbjno());//设备编码
                    map.put("equipmentName",shebeiInfo.getSbname());//设备名
                    map.put("pileLong",queryone.getWorklength());//施工桩长（米）
                    map.put("pileNum",pilecount);//施工桩数量（根）
                    map.put("pulpNum",queryone.getAllbeton());//浆量（L）
                    map.put("runTime","");//作业时长（m）
                    if (shebeiInfo.getStatus() == 3){
                        map.put("state",1);//是否在线0离线 1在线
                    }else {
                        map.put("state",0);//是否在线0离线 1在线
                    }
                    list.add(map);
                }else {
                    log.info("该设备没有实时数据。");
                }
            }
            Date date = new Date();
            long time = date.getTime();
            data.set("equipmentList",list);
            String singes = code + secretkey + data + time;
            String singe = MD5.toMD5(singes);
            boolean code = getCode(urling, time, singe, data);
            if (code){
                log.info("推送成功！！！");
            }else {
                log.info("推送失败！！！");
            }
        }else {
            log.info("该项目下没有水泥搅拌桩设备。");
        }

    }
    // url
    public static Boolean getCode(String urling,long time,String singe,JSONObject data) {
        System.out.println(url+urling+"code="+code+"&singe="+singe+"&time="+time);
        System.out.println(data);
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
