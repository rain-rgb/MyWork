package org.jeecg.modules.job.sljbz;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.api.utils.crypt.MD5;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.devicemixpilerealdata.entity.DeviceMixpileRealdata;
import com.trtm.iot.devicemixpilerealdata.service.IDeviceMixpileRealdataService;
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
public class sgjlssjljob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDeviceMixpileRealdataService deviceMixpileRealdataService;

    private static final String code = "gaoxun";
    private static final String secretkey ="9f164bcc3976a8348b584c02fb1046cb";

    private static final String url = "http://112.90.176.194:60000/";//ip

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SLJBZ_SSJL);//水泥搅拌桩大屏-接口文档v1.3 施工记录-实时记录
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到送货短信提醒定时任务的配置信息" + DateUtils.now()));
            return;
        }

        String orgcode = "A05A16A01A02";//民古路交建指挥部
        SysDepart departname = sysDepartService.queryone(orgcode);
        String urling = "smwdata/processLive?";

        List<ShebeiInfo> shebeiInfos = shebeiInfoService.shebeilists(16,orgcode+"%");
        if (shebeiInfos.size() > 0){
            for (ShebeiInfo shebeiInfo :shebeiInfos){
                QueryWrapper<DeviceMixpileRealdata> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("shebeino",shebeiInfo.getSbjno());
                List<DeviceMixpileRealdata> list = deviceMixpileRealdataService.list(queryWrapper);
                if (list.size() > 0){
                    for (DeviceMixpileRealdata l :list){
                        Date date = new Date();
                        long time = date.getTime();
                        JSONObject data = JSONUtil.createObj();
                        data.set("projectId",orgcode);//项目编码
                        data.set("projectName",departname.getDepartNameAbbr());//项目名
                        data.set("equipmentName", shebeiInfo.getSbname());//设备名称
                        data.set("equipmentCode", shebeiInfo.getSbjno());//设备编码
                        data.set("constructionStakeNum", l.getPileno());//桩号
                        data.set("constructionStakeName", l.getMileage());//桩机
                        data.set("realTime", l.getDatatime());//客户端当前记录产生时间（实施采集时间
                        data.set("startTime", l.getDatatime());//开始时间（该根桩的开始时间）
                        data.set("processType", "");//工艺类型（1：两喷两搅，2：两喷四搅，3：四喷四搅）；
                        data.set("recordStatus", "");//记录状态：1-开始，2-暂停，3-结束,4-报警；
                        data.set("pipeStatus", "");//钻杆状态：1-下钻，2-提钻，3-复下，4-复提；
                        data.set("nozzleStatus", "");//喷浆状态：1-喷浆，0-止喷；
                        data.set("waterAshRatio", l.getRatio());//水灰比
                        data.set("segmentPulp", "");//段浆量(L)
                        data.set("segmentAsh", "");//段灰量(kg)
                        data.set("totalPulp", l.getGrouting());//累计浆量（L）
                        data.set("totalAsh", "");//累计灰量（KG）
                        data.set("electricity", l.getElec());//电流(A)
                        data.set("density", "");//密度(g/cm3)
                        data.set("pressure", "");//喷压(MPa)
                        data.set("speed", l.getSpeed());//下钻/提钻速度(cm/min)
                        data.set("drillingRate", "");//桩杆转速(r/min)
                        data.set("currentDepth", l.getCurdep());//深度(m)
                        data.set("maxDepth", l.getCurdep());//最大深度(m)
                        data.set("flow", l.getFlowlst());//流量
                        data.set("xAngle", l.getX());//前后倾角
                        data.set("yAngle", l.getY());//左右倾角
                        data.set("longitudu", l.getLtdfloat());//经度
                        data.set("latitude", l.getLgdfloat());//纬度
                        data.set("maxGradient", l.getX());//最大倾斜度
                        String singes = code + secretkey + data + time;
                        String singe = MD5.toMD5(singes);
                        boolean code = getCode(urling, time, singe, data);
                        if (code){
                            log.info("推送成功！！！");
                        }else {
                            log.info("推送失败！！！");
                        }
                    }
                }else {
                    log.info("该设备没有实时数据。");
                }
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
