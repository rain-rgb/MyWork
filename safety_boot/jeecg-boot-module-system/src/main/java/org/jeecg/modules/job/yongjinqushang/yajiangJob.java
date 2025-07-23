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
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.yajiangss.entity.TrYajiangSS;
import com.trtm.iot.yajiangss.service.ITrYajiangSSService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName yajiangJob
 * @Author
 * @Date 2024/12/23 9:12
 * @Version
 * @Description
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class yajiangJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private ITrYajiangSService yajiangSService;
    @Autowired
    private ITrYajiangSSService yajiangSSService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrYajiangRenwudanService yajiangRenwudanService;

        public static String url = "http://115.236.10.10:8081/zjjg-iot//tensionAndGrouting/saveGroutingData";
//    public static String zhangla_url = "http://121.41.26.120:8081/zjjg-iot-test/tensionAndGrouting/saveTensionData";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZJJG_YJ);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到湖州压浆定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输湖州压浆的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrYajiangM> trYajiangMS = yajiangMService.selectListoJG(shebeilist, curid);
        if (null == trYajiangMS || trYajiangMS.size() == 0) {
            log.info(String.format("暂无湖州压浆未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrYajiangM yajiangM : trYajiangMS) {
            id = yajiangM.getId();
            JSONObject sendObject = JSONUtil.createObj();
            sendObject.put("id", yajiangM.getId());
            String yjsbbh = yajiangM.getYjsbbh();
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(yjsbbh);
            sendObject.set("equipment_code", yjsbbh);
            sendObject.set("equipment_name", sbjwd.getSbname());
            sendObject.set("component_id", yajiangM.getSyjid());
            sendObject.set("engineering_name", yajiangM.getGcmc());
            sendObject.set("engineering_site", yajiangM.getGjbh());
            TrYajiangRenwudan selectone = yajiangRenwudanService.selectone(yajiangM.getUuid());
            if (selectone != null) {
                sendObject.set("component_part", selectone.getSgbwname());
            } else {
                sendObject.set("component_part", yajiangM.getGjbh());
            }
            sendObject.set("beam_length", "");
            sendObject.set("beam_type", yajiangM.getLblx());
            sendObject.set("concrete_strength", "");
            sendObject.set("modulus_of_elasticity", "");

            String kdlx = yajiangM.getKdlx();
            int hole_type = 0;
            if (kdlx!=null){
                if (kdlx.equals("3")) {
                    hole_type = 1;
                }
                if (kdlx.equals("4")) {
                    hole_type = 2;
                }
            }
            sendObject.set("hole_type", hole_type);
            sendObject.set("birth_time", yajiangM.getYjsj());
            sendObject.set("entrust_id", yajiangM.getUuid());
            sendObject.set("pedestal", "");
            sendObject.set("orgCode", sbjwd.getTunnelId());
            sendObject.set("orgName", sbjwd.getTunnelName());
            sendObject.set("manufacture", sbjwd.getMillname());

            LambdaQueryWrapper<TrYajiangS> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TrYajiangS::getSyjid, yajiangM.getSyjid());
            List<TrYajiangS> list = yajiangSService.list(queryWrapper);
            for (TrYajiangS yajiangS : list) {
                sendObject.set("steel_beam_number", yajiangS.getKongdao());
                sendObject.set("steel_strand", "");
                sendObject.set("pulping_orientation", yajiangM.getYajiangfang());
                sendObject.set("pulping_order", yajiangM.getYajiangbuzh());
                sendObject.set("flow_velocity", yajiangM.getChushisudu());
                sendObject.set("fluidity", yajiangM.getLiudongdu());
                sendObject.set("mixproportion", yajiangS.getPeihebi());
                sendObject.set("water_binder_ratio", yajiangS.getShuijiaobi());
                sendObject.set("mixing_duration", yajiangS.getJiaobansj());
                sendObject.set("start_time", yajiangS.getStarttime());
                sendObject.set("end_time", yajiangS.getEndtime());
                sendObject.set("pulping_volume", yajiangS.getJinjiangshu());
                sendObject.set("standard_volume", "");//理论浆量
                sendObject.set("pulping_pressure_in", yajiangS.getJinjiangyal());
                sendObject.set("pulping_pressure_out", yajiangS.getFanjiangyal());
                sendObject.set("holding_duration", yajiangS.getChixushijia());
                sendObject.set("result", yajiangS.getHege());
                sendObject.set("userid", "");
                sendObject.set("other_information", "");
                sendObject.set("vacuum_degree", yajiangS.getZkyl());
                sendObject.set("cycle_time", yajiangS.getChixushijia());
                sendObject.set("stabilizing_pressure", yajiangS.getFanjiangyal());
                String yajiangmosh = yajiangS.getYajiangmosh();
                String gouting_mode = "0";
                if (yajiangmosh.contains("大") || yajiangmosh.contains("双")) {
                    sendObject.set("gouting_mode", "1");
                }
                sendObject.set("gouting_mode", gouting_mode);//压浆模式

                LambdaQueryWrapper<TrYajiangSS> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(TrYajiangSS::getHoleid, yajiangS.getHoleid());
                List<TrYajiangSS> list1 = yajiangSSService.list(queryWrapper1);
                String volume_curve = "";
                String pressure_in_curve = "";
                String pressure_out_curve = "";
                for (TrYajiangSS trYajiangSS : list1) {
                    volume_curve += trYajiangSS.getJjl() + ",";
                    pressure_in_curve += trYajiangSS.getJjyl() + ",";
                    pressure_out_curve += trYajiangSS.getFjyl() + ",";
                }
                if (volume_curve.length() != 0) {
                    volume_curve = volume_curve.substring(0, volume_curve.length() - 1);
                    sendObject.set("volume_curve", volume_curve);
                }
                if (pressure_in_curve.length() != 0) {
                    pressure_in_curve = pressure_in_curve.substring(0, pressure_in_curve.length() - 1);
                    sendObject.set("pressure_in_curve", pressure_in_curve);
                }
                if (pressure_out_curve.length() != 0) {
                    pressure_out_curve = pressure_out_curve.substring(0, pressure_out_curve.length() - 1);
                    sendObject.set("pressure_out_curve", pressure_out_curve);
                }

                String jsonString = JSON.toJSONString(sendObject);
                System.out.println(jsonString);
                String result = HttpRequest.post(url)
                        .header("client-code", "gaoxun")
                        .body(jsonString)
                        .timeout(25000)
                        .execute()
                        .body();
                pushandreturnService.saveData(yajiangM.getId(), jsonString, selectsysconfigone.getRemark(), result);
            }
            sysConfigService.updateSysConfig(JobUtil.ZJJG_YJ, id);
        }
    }
}