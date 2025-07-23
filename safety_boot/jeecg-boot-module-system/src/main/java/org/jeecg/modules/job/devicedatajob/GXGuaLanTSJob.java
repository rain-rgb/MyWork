package org.jeecg.modules.job.devicedatajob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.trtm.iot.gualan.entity.GualanBase;
import com.trtm.iot.gualan.service.IGualanBaseService;
import com.trtm.iot.jiaqiaoji.entity.DeviceBridgeHistorydata;
import com.trtm.iot.jiaqiaoji.service.IDeviceBridgeHistorydataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.RCJob.RSAUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class GXGuaLanTSJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IGualanBaseService gualanBaseService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysDepartService sysDepartService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.GX_GUALAN_TUISONG);//挂篮数据推送高信平台
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到挂篮数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置挂篮数据推送设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<GualanBase> gualanBaseList = gualanBaseService.selectGuaLanList(shebeilist, curid);
        if (null == gualanBaseList || gualanBaseList.size() == 0) {
            log.info(String.format("暂无挂篮数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (GualanBase gualanBase : gualanBaseList) {
            id = gualanBase.getId();
            ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(gualanBase.getShebeibanhao());
            SysDepart sysDepart = sysDepartService.queryone(shebeiInfo.getSysOrgCode());
            JSONObject jsonObj = new JSONObject();
            jsonObj.set("project_id", sysDepart.getProjectid());//项目id
            jsonObj.set("project_name", sysDepart.getDepartNameAbbr());//项目名称
            jsonObj.set("section_id", sysDepart.getSectionid());//标段id
            jsonObj.set("section_name", sysDepart.getDepartName());//标段名称
            jsonObj.set("shebeibanhao", gualanBase.getShebeibanhao());//设备编号
            jsonObj.set("data_type", gualanBase.getDataType());//数据类标志
            jsonObj.set("tension1", gualanBase.getTension1());//1#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)
            jsonObj.set("tension2", gualanBase.getTension2());//2#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)
            jsonObj.set("tension3", gualanBase.getTension3());//3#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)
            jsonObj.set("tension4", gualanBase.getTension4());//4#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)
            jsonObj.set("levelness1", gualanBase.getLevelness1());//下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)
            jsonObj.set("levelness2", gualanBase.getLevelness2());//下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)
            jsonObj.set("overweight1", gualanBase.getOverweight1());//1#螺纹钢是否超重 0:正常 1:超重
            jsonObj.set("overweight2", gualanBase.getOverweight2());//2#螺纹钢是否超重 0:正常 1:超重
            jsonObj.set("overweight3", gualanBase.getOverweight3());//3#螺纹钢是否超重 0:正常 1:超重
            jsonObj.set("overweight4", gualanBase.getOverweight4());//4#螺纹钢是否超重 0:正常 1:超重
            jsonObj.set("basket", gualanBase.getBasket());//挂篮前端下沉量（放大10倍单位mm）
            jsonObj.set("windspeed", gualanBase.getWindspeed());//风速（放大10倍 单位m/s）
            jsonObj.set("windspeedwarn", gualanBase.getWindspeedwarn());//风速报警0---正常  1---报警
            jsonObj.set("reserved_one", gualanBase.getReservedOne());//下托梁有效长度
            jsonObj.set("overval1", gualanBase.getOverval1());//螺纹钢承重预警额定拉力85%预警 0:正常 1:超重
            jsonObj.set("overval2", gualanBase.getOverval2());//超重预警额定重量100%预警 0:正常 1:超重
            jsonObj.set("overval3", gualanBase.getOverval3());//前横梁水平度预警偏差3度预警 0:正常 1:超重
            jsonObj.set("overval4", gualanBase.getOverval4());//横梁左右偏差3度预警倾斜预警偏差3度预警 0:正常 1:超重
            jsonObj.set("overval5", gualanBase.getOverval5());//纵梁变形预警偏差3度预警 0:正常 1:超重
            jsonObj.set("datatime", gualanBase.getDatatime());//数据时间
            jsonObj.set("overproof_status", gualanBase.getOverproofStatus());//审核状态

            String token = null;
            String clientSecret = RSAUtil.queryById22("KJXOVlay6Bw0SDgp");
            String back1 = HttpRequest.post("https://yggc.cncico.com:1080/api/login/appToken")
                    .header("Content-Type", "application/json")
                    .header("clientid", "app-ext-iot")
                    .header("clientSecret", clientSecret)
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(back1);
            String code1 = (String) jsonObject1.get("code");
            if (code1.equals("00000")) {
                JSONObject model = (JSONObject) jsonObject1.get("model");
                token = (String) model.get("access_token");
                String post = HttpRequest.post("https://yggc.cncico.com:1080/api/iot/tunnel/bases")
                        .header("Content-Type", "application/json")
                        .header("Authorization", String.format("%s %s", "Bearer", token))
                        .body(String.valueOf(jsonObj))
                        .execute()
                        .body();
                JSONObject jsonObject2 = new JSONObject(post);
                String code2 = (String) jsonObject2.get("code");
                if (code2.equals("00000")) {
                    log.info(String.format("挂篮数据推送成功!" + id));
                    sysConfigService.updateSysConfig(JobUtil.GX_GUALAN_TUISONG, id);//根据传过来的唯一值来修改当前判断到的数据id
                } else {
                    log.info(String.format("挂篮数据推送失败!" + id));
                }
            }
        }
    }
}
