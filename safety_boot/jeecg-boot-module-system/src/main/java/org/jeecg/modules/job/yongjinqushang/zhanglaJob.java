package org.jeecg.modules.job.yongjinqushang;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.zhangla.service.ITrZhanglaService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglass.entity.TrZhanglaSS;
import com.trtm.iot.zhanglass.service.ITrZhanglaSSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @ClassName zhanglaJob
 * @Author
 * @Date 2024/12/13 16:53
 * @Version
 * @Description 张拉推送交工
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zhanglaJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrZhanglaXxbService TrZhanglaXxbService;
    @Autowired
    private ITrZhanglaMService trZhanglaMService;
    @Autowired
    private ITrZhanglaSService trZhanglaSService;
    @Autowired
    private ITrZhanglaSSService trZhanglaSSService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrZhanglaRenwudanService zhanglaRenwudanService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    public static String zhangla_url = "http://115.236.10.10:8081/zjjg-iot/tensionAndGrouting/saveTensionData";
//    public static String zhangla_url = "http://121.41.26.120:8081/zjjg-iot-test/tensionAndGrouting/saveTensionData";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZJJG_ZL);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到湖州张拉定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输湖州张拉的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrZhanglaXxb> trZhanglaXxxbs = TrZhanglaXxbService.selectListoJG(shebeilist);
        if (null == trZhanglaXxxbs || trZhanglaXxxbs.size() == 0) {
            log.info(String.format("暂无湖州张拉的数据" + DateUtils.now()));
            return;
        } else {
            log.info(String.format("湖州张拉数据推送开始！" + DateUtils.now()));
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //循环信息表
        for (TrZhanglaXxb trZhanglaXxb : trZhanglaXxxbs) {
            //查询子表
            LambdaQueryWrapper<TrZhanglaM> zhanglaMLambdaQueryWrapper = new LambdaQueryWrapper<>();
            zhanglaMLambdaQueryWrapper.eq(TrZhanglaM::getSyjid, trZhanglaXxb.getSyjid());
            List<TrZhanglaM> trZhanglaMs = trZhanglaMService.list(zhanglaMLambdaQueryWrapper);
            for (TrZhanglaM trZhanglaM : trZhanglaMs) {

                JSONObject sendObject = new JSONObject();
                sendObject.set("id", trZhanglaM.getFguid());
                sendObject.set("equipment_code", trZhanglaXxb.getShebeibianhao());
                ShebeiInfo sbjwd = shebeiInfoService.SBJWD(trZhanglaXxb.getShebeibianhao());
                sendObject.set("equipment_name", sbjwd.getSbname());
                sendObject.set("component_id", trZhanglaXxb.getSyjid());
                sendObject.set("engineering_name", trZhanglaXxb.getGcmc());
                sendObject.set("engineering_site", trZhanglaXxb.getGjbh());
                //任务单
                TrZhanglaRenwudan zhanglaRenwudan = zhanglaRenwudanService.selectone(trZhanglaXxb.getUuid());
                if (zhanglaRenwudan != null) {
                    sendObject.set("component_part", zhanglaRenwudan.getSgbwname());
                } else {
                    sendObject.set("component_part", trZhanglaXxb.getGjbh());
                }
                sendObject.set("beam_length", "");
                sendObject.set("beam_type", trZhanglaXxb.getKualiang());
                sendObject.set("concrete_strength", trZhanglaXxb.getSnsjqd());
                sendObject.set("modulus_of_elasticity", trZhanglaM.getTxml());
                sendObject.set("steel_beam_number", trZhanglaM.getGsbh());
                sendObject.set("steel_strand", trZhanglaM.getGsgs());
                sendObject.set("tension_control", trZhanglaM.getSjzll());
                sendObject.set("total_elongation", trZhanglaM.getZscl());
                sendObject.set("theoretical_elongation", trZhanglaM.getLlscl());
                sendObject.set("extend_error", trZhanglaM.getSclper());
                sendObject.set("result", trZhanglaXxb.getHege());
                sendObject.set("userid", "");
                sendObject.set("other_information", trZhanglaXxb.getFmqkms());
                sendObject.set("retraction", trZhanglaM.getHtl());
                sendObject.set("anchor_width", "");
                sendObject.set("collection_time", trZhanglaM.getZlsj());
                sendObject.set("task_id", trZhanglaXxb.getUuid());
                sendObject.set("jack_no", trZhanglaXxb.getZly1());
                sendObject.set("section_error", "");
                sendObject.set("tension_mode", "2");
                sendObject.set("pedestal", "");
                sendObject.set("working_length", "");
                sendObject.set("orgCode", sbjwd.getTunnelId());
                sendObject.set("orgName", sbjwd.getTunnelName());
                sendObject.set("manufacture", sbjwd.getMillname());

                for (int i = 1; i < 3; i++) {

                    //按断面查询进度数据
                    LambdaQueryWrapper<TrZhanglaS> trZhanglaSLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    trZhanglaSLambdaQueryWrapper.eq(TrZhanglaS::getFguid, trZhanglaM.getFguid())
                            .eq(TrZhanglaS::getDh, i)
                            .orderByAsc(TrZhanglaS::getJdscl);
                    List<TrZhanglaS> zhanglaSList = trZhanglaSService.list(trZhanglaSLambdaQueryWrapper);

                    sendObject.set("holding_duration", zhanglaSList.get(0).getChsj());
                    sendObject.set("tensioning_datetime", trZhanglaXxb.getSgsj());
                    int a = 0;
                    for (TrZhanglaS trZhanglaS : zhanglaSList) {
                        String jdbfb = trZhanglaS.getJdbfb();
                        if (jdbfb.equals("10")){
                            sendObject.set("tensioning_force0", trZhanglaS.getJdzll());
                            sendObject.set("elongation0", trZhanglaS.getJdscl());
                            sendObject.set("oilpressure0", trZhanglaS.getYbds());
                        }
                        if (jdbfb.equals("20")){
                            sendObject.set("tensioning_force1", trZhanglaS.getJdzll());
                            sendObject.set("elongation1", trZhanglaS.getJdscl());
                            sendObject.set("oilpressure1", trZhanglaS.getYbds());
                        }
                        if (jdbfb.equals("50") && a==0){
                            sendObject.set("tensioning_force2", trZhanglaS.getJdzll());
                            sendObject.set("elongation2", trZhanglaS.getJdscl());
                            sendObject.set("oilpressure2", trZhanglaS.getYbds());
                            a=1;
                        }
                        if (jdbfb.equals("50") && a==1){
                            sendObject.set("tensioning_force3", trZhanglaS.getJdzll());
                            sendObject.set("elongation3", trZhanglaS.getJdscl());
                            sendObject.set("oilpressure3", trZhanglaS.getYbds());
                        }
                        if (jdbfb.equals("100")){
                            sendObject.set("tensioning_force4", trZhanglaS.getJdzll());
                            sendObject.set("elongation4", trZhanglaS.getJdscl());
                            sendObject.set("oilpressure4", trZhanglaS.getYbds());
                        }
                    }

                    //查询过程数据
                    LambdaQueryWrapper<TrZhanglaSS> trzhanglassLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    trzhanglassLambdaQueryWrapper.eq(TrZhanglaSS::getHoleid, trZhanglaM.getHoleid());
                    List<TrZhanglaSS> zhanglaSSList = trZhanglaSSService.list(trzhanglassLambdaQueryWrapper);
                    // 压力值曲线数据，每个点之间用逗号分隔
                    String zllqx = "";
                    String sclqx = "";
                    String yyqx = "";
                    if (i==1){
                        sendObject.set("tensioning_section", "前端");
                        for (TrZhanglaSS trZhanglaSS : zhanglaSSList) {
                            zllqx += trZhanglaSS.getZll1() + ",";
                            sclqx += trZhanglaSS.getScl1() + ",";
                            yyqx += trZhanglaSS.getYy1() + ",";
                        }
                    }else {
                        sendObject.set("tensioning_section", "后端");
                        for (TrZhanglaSS trZhanglaSS : zhanglaSSList) {
                            zllqx += trZhanglaSS.getZll2() + ",";
                            sclqx += trZhanglaSS.getScl1() + ",";
                            yyqx += trZhanglaSS.getYy2() + ",";
                        }
                    }
                    if (zllqx.length()!=0) {
                        String force_curve = zllqx.substring(0, zllqx.length() - 1);
                        sendObject.set("force_curve", force_curve);
                    }
                    if (sclqx.length()!=0) {
                        String elongation_curve = sclqx.substring(0, sclqx.length() - 1);
                        sendObject.set("elongation_curve", elongation_curve);
                    }
                    if (yyqx.length()!=0) {
                        String oilpressure_curve = yyqx.substring(0, yyqx.length() - 1);
                        sendObject.set("oilpressure_curve", oilpressure_curve);
                    }

                    String jsonString = JSON.toJSONString(sendObject);
                    System.out.println(jsonString);
                    String result = HttpRequest.post(zhangla_url)
                            .header("client-code", "gaoxun")
                            .body(jsonString)
                            .timeout(25000)
                            .execute()
                            .body();
                    if (result.contains("200")){
                        trZhanglaXxb.setIszl("1");
                    }else {
                        trZhanglaXxb.setIszl("2");
                    }
                    TrZhanglaXxbService.updateById(trZhanglaXxb);
                    pushandreturnService.saveData(trZhanglaXxb.getId(), jsonString, selectsysconfigone.getRemark(), result);
                }
            }
            sysConfigService.updateSysConfig(JobUtil.ZJJG_ZL,id);
        }
    }
}