package org.jeecg.modules.job.sljbz;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.api.utils.crypt.MD5;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
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
public class sgjllsjob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDeviceMixpileHistorydataOneService deviceMixpileHistorydataOneService;

    private static final String code = "gaoxun";
    private static final String secretkey ="9f164bcc3976a8348b584c02fb1046cb";

    private static final String url = "http://112.90.176.194:60000/";//ip

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SLJBZ_lsjl);//水泥搅拌桩大屏-接口文档v1.3 施工记录-历史记录
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到送货短信提醒定时任务的配置信息" + DateUtils.now()));
            return;
        }
        Integer curid = selectsysconfigone.getCurid();

        String orgcode = "A05A16A01A02";//民古路交建指挥部
        SysDepart departname = sysDepartService.queryone(orgcode);
        String urling = "smwdata/processHistory?";

        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        int id = 0;
        List<ShebeiInfo> shebeiInfos = shebeiInfoService.shebeilists(16,orgcode+"%");
        List<String> list = new ArrayList<>();
        if (shebeiInfos.size() > 0){
            for (ShebeiInfo shebeiInfo :shebeiInfos){
                list.add(shebeiInfo.getSbjno());
            }
            List<DeviceMixpileHistorydataOne>  deviceMixpileHistorydataOnes = deviceMixpileHistorydataOneService.selebyshebeicuid(list,curid);
            if (deviceMixpileHistorydataOnes.size() > 0){
                for (DeviceMixpileHistorydataOne deviceMixpileHistorydataOne :deviceMixpileHistorydataOnes){
                    Date date = new Date();
                    long time = date.getTime();
                    id = deviceMixpileHistorydataOne.getId();
                    JSONObject data = JSONUtil.createObj();
                    ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(deviceMixpileHistorydataOne.getShebeino());
                    data.set("smwEquipmentName", selectshebeione.getSbname());//设备名称
                    data.set("smwEquipmentCode", deviceMixpileHistorydataOne.getShebeino());//设备编码
                    data.set("constructionStakeNum", deviceMixpileHistorydataOne.getPileNo());//桩号
                    data.set("constructionStakeName", deviceMixpileHistorydataOne.getPileMileage());//桩机
                    if (deviceMixpileHistorydataOne.getPileStarttime() != null && deviceMixpileHistorydataOne.getPileTime() != null){
                        try {
                            data.set("startTime", dateFormat.parse(deviceMixpileHistorydataOne.getPileStarttime()));//开始时间
                            data.set("endTime", dateFormat.parse(deviceMixpileHistorydataOne.getPileTime()));//结束时间
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    data.set("processType", 1);//工艺类型（1：两喷两搅，2：两喷四搅，3：四喷四搅）；
                    data.set("constructionStakeLong", deviceMixpileHistorydataOne.getPileRealdep());//施工桩长
                    data.set("waterAshRatio", deviceMixpileHistorydataOne.getPileRatio());//水灰比
                    double v = Double.parseDouble(deviceMixpileHistorydataOne.getPileDownbeton()) + Double.parseDouble(deviceMixpileHistorydataOne.getPileUobeton());
                    data.set("totalPulp", v);//累计浆量（L）
                    data.set("avgPulp", v/2);//平均浆量（L）
                    data.set("totalAsh", "");//累计灰量（KG）
                    data.set("avgAsh", "");//平均灰量（KG/M）
                    data.set("airPulp", "");//空搅浆量（L）
                    data.set("airAsh", "");//空搅灰量（KG）
                    data.set("density", "");//密度
                    data.set("avgDownSpeed", deviceMixpileHistorydataOne.getPileDspeed());//平均下钻速度（cm/min）
                    double v1 = Double.parseDouble(deviceMixpileHistorydataOne.getPileRealdep())*100;//实际桩长
                    double v2 = Double.parseDouble(deviceMixpileHistorydataOne.getPileWorktime())/60;//成桩时长
                    if (v2 != 0.0){
                        data.set("maxDownSpeed", v1/v2);//最大下钻速度（cm/min）
                    }else {
                        data.set("maxDownSpeed", v1);//最大下钻速度（cm/min）
                    }
                    data.set("avgUpSpeed", deviceMixpileHistorydataOne.getPileUspeed());//平均提钻速度（cm/min）
                    data.set("maxUpSpeed", "");//最大提钻速度（cm/min）
                    data.set("avgDrillingRate", "");//平均钻杆速度（r/min）
                    data.set("maxDrillingRate", "");//最大钻杆速度（r/min）
                    if (deviceMixpileHistorydataOne.getGzcount() == 1){
                        data.set("initialMixDepth",deviceMixpileHistorydataOne.getPileRealdep());//初搅深度(m)
                        data.set("reMixDepth", "");//复搅深度(m)
                    }else {
                        data.set("initialMixDepth", "");//初搅深度(m)
                        data.set("reMixDepth",deviceMixpileHistorydataOne.getPileRealdep());//复搅深度(m)
                    }
                    data.set("airMixDepth", "");//空搅深度(m)
                    data.set("pileTipElectricity", deviceMixpileHistorydataOne.getPileUelectr());//桩端电流（A）
                    data.set("avgElectricity", deviceMixpileHistorydataOne.getPileDelectr());//平均电流（A）
                    data.set("maxElectricity", deviceMixpileHistorydataOne.getPileMaxelectr());//最大电流（A）
                    data.set("maxGradient", deviceMixpileHistorydataOne.getPileX());//最大倾斜度
                    data.set("equipmentLongitude", deviceMixpileHistorydataOne.getPileLgd());//设备经度pile_lgd
                    data.set("equipmentLatitude", deviceMixpileHistorydataOne.getPileLtd());//设备纬度pile_ltd
                    data.set("holeLongitude", deviceMixpileHistorydataOne.getPileLgd());//桩孔经度
                    data.set("holeLatitude", deviceMixpileHistorydataOne.getPileLtd());//桩孔纬度
                    data.set("pilingTime", v2);//成桩时间(min)
                    data.set("status", deviceMixpileHistorydataOne.getChaobiaodengji());//是否合格

                    data.set("projectId",orgcode);//项目编码
                    data.set("projectName",departname.getDepartNameAbbr());//项目名
                    String singes = code + secretkey + data + time;
                    String singe = MD5.toMD5(singes);
                    boolean code = getCode(urling, time, singe, data);
                    if (code){
                        log.info("推送成功！！！");
                        sysConfigService.updateSysConfig(JobUtil.SLJBZ_lsjl, id);//根据传过来的唯一值来修改当前判断到的数据id
                    }else {
                        log.info("推送失败！！！");
                    }
                }
            }else {
                log.info("该设备没有实时数据。");
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
