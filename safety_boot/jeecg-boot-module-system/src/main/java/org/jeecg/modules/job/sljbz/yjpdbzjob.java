package org.jeecg.modules.job.sljbz;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.api.utils.crypt.MD5;
import com.trtm.iot.devicemixpileonecfg.entity.DeviceMixpileOneCfg;
import com.trtm.iot.devicemixpileonecfg.service.IDeviceMixpileOneCfgService;
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

import java.util.Date;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class yjpdbzjob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDeviceMixpileOneCfgService deviceMixpileOneCfgService;

    private static final String code = "gaoxun";
    private static final String secretkey ="9f164bcc3976a8348b584c02fb1046cb";

    private static final String url = "http://112.90.176.194:60000/";//ip

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SLJBZ_ZLYJ);//水泥搅拌桩大屏-接口文档v1.3 质量预警判断标准
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到送货短信提醒定时任务的配置信息" + DateUtils.now()));
            return;
        }

        String orgcode = "A05A16A01A02";//民古路交建指挥部
        SysDepart departname = sysDepartService.queryone(orgcode);
        String urling = "smwdata/qualityStandard?";

        List<ShebeiInfo> shebeiInfos = shebeiInfoService.shebeilists(16,orgcode+"%");
        if (shebeiInfos.size() > 0){
            for (ShebeiInfo shebeiInfo :shebeiInfos){
                QueryWrapper<DeviceMixpileOneCfg> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("shebeino",shebeiInfo.getSbjno());
                DeviceMixpileOneCfg one = deviceMixpileOneCfgService.getOne(queryWrapper);
                if (one != null){
                    Date date = new Date();
                    long time = date.getTime();
                    JSONObject data = JSONUtil.createObj();
                    data.set("projectId",orgcode);//项目编码
                    data.set("projectName",departname.getDepartNameAbbr());//项目名
                    data.set("equipmentName", shebeiInfo.getSbname());//设备名称
                    data.set("equipmentCode", shebeiInfo.getSbjno());//设备编码
                    data.set("minimumPilingTime", "");//最小下钻时间
                    data.set("designTotalPileLength", one.getDesigndep());//设计总桩长(m)
                    data.set("designPulp", "");//设计浆量
                    data.set("designAsh", "");//设计灰量
                    data.set("cementContent", "");//水泥掺量(Kg/m)
                    data.set("pileSpacing", "");//桩间距(m)
                    data.set("designLiftingSpeed", "");//设计提钻速度(cm/min)
                    data.set("designRunningSpeed", "");//设计下钻速度(cm/min)
                    data.set("waterAshRatio", "");//水灰比
                    data.set("density", "");//密度
                    data.set("gradient", "");//倾斜度
                    data.set("minGradient", one.getXx());//最小报警垂直度
                    data.set("maxGradient", one.getXs());//最大报警垂直度
                    data.set("maxElectricity", "");//最大电流
                    data.set("minDesignLiftingSpeed", one.getUspeedx());//最小报警提钻速度(cm/min)
                    data.set("maxDesignLiftingSpeed", one.getUspeeds());//最大报警提钻速度(cm/min)
                    data.set("minDesignRunningSpeed", one.getDspeedx());//最小报警下钻速度(cm/min)
                    data.set("maxDesignRunningSpeed", one.getDspeeds());//最大报警下钻速度(cm/min)
                    data.set("minWaterAshRatio", "");//最小水灰比
                    data.set("maxWaterAshRatio", "");//最大水灰比
                    data.set("minDensity", "");//最小密度
                    data.set("maxDensity", "");//最大密度
                    data.set("minDesignPulp", "");//最小设计浆量
                    data.set("maxDesignPulp", "");//最大设计浆量
                    data.set("minDesignAsh", "");//最小设计灰量
                    data.set("maxDesignAsh", "");//最大设计灰量
                    data.set("minCementContent", "");//最小水泥掺量(Kg/m)
                    data.set("maxCementContent", "");//最大水泥掺量(Kg/m)
                    data.set("minDesignTotalPileLength", "");//最小设计总桩长(m)
                    data.set("maxDesignTotalPileLength", "");//最大设计总桩长(m)
                    data.set("designPileDiameter", "");//设计桩径(mm)
                    data.set("cementGrade", "");//水泥标号
                    data.set("designCementRate", "");//设计水泥掺量(%)
                    data.set("designCementVolume", "");//应灌水泥浆量(L)
                    data.set("designRoadHeight", "");//地面标高(m)
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
