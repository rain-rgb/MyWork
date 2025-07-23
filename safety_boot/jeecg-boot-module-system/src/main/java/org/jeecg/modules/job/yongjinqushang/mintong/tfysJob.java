package org.jeecg.modules.job.yongjinqushang.mintong;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.hc_machine.entity.HcMachine;
import com.trtm.iot.hc_machine.service.IHcMachineService;
import com.trtm.iot.hc_machine_onlinerecord.entity.HcMachineOnlinerecord;
import com.trtm.iot.hc_machine_onlinerecord.service.IHcMachineOnlinerecordService;
import com.trtm.iot.hc_tfstationdetail.entity.HcTfstationdetail;
import com.trtm.iot.hc_tfstationdetail.service.IHcTfstationdetailService;
import com.trtm.iot.hc_tfysworkarea.entity.HcTfysworkarea;
import com.trtm.iot.hc_tfysworkarea.service.IHcTfysworkareaService;
import com.trtm.iot.hctfysworkareapeiz.entity.HcTfysworkareapeiz;
import com.trtm.iot.hctfysworkareapeiz.service.IHcTfysworkareapeizService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.util.SignUtil;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.service.ISysDepartprojectService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class tfysJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IHcTfysworkareapeizService hcTfysworkareapeizService;
    @Autowired
    private IHcTfysworkareaService hcTfysworkareaService;
    @Autowired
    private IHcTfstationdetailService hcTfstationdetailService;
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;
    @Autowired
    private IHcMachineOnlinerecordService hcMachineOnlinerecordService;
    @Autowired
    private IHcMachineService hcMachineService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJQS_TPNY);//甬金衢上土方压实
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到甬金衢上土方压实定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输甬金衢上土方压实的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        if (null == shebeilist || shebeilist.isEmpty()) {
            log.info(String.format("没有配置要传输甬金衢上土方压实的设备" + DateUtils.now()));
            return;
        }
        List<HcTfysworkareapeiz> tpysList = hcTfysworkareapeizService.tfys(shebeilist, curid);
        if (null == tpysList || tpysList.isEmpty()) {
            log.info(String.format("没有要传输甬金衢上土方压实的任务" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (HcTfysworkareapeiz hcTfysworkareapeiz : tpysList) {
            id = hcTfysworkareapeiz.getId();
            String startstation = hcTfysworkareapeiz.getStartstation();
            String endstation = hcTfysworkareapeiz.getEndstation();
            String mileageid = hcTfysworkareapeiz.getMileageid();

            LambdaQueryWrapper<SysDepartproject> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(SysDepartproject::getOrgCode, mileageid);
            SysDepartproject sysDepartproject = sysDepartprojectService.getOne(queryWrapper1);
            String departName = sysDepartproject.getDepartName();

            int startstationInt = Integer.parseInt(startstation);
            int endstationInt = Integer.parseInt(endstation);

            LambdaQueryWrapper<HcTfysworkarea> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.ge(HcTfysworkarea::getStartstation, startstationInt);
            queryWrapper.le(HcTfysworkarea::getEndstation, endstationInt);
            List<HcTfysworkarea> hcTfysworkareaList = hcTfysworkareaService.list(queryWrapper);

            if (null == hcTfysworkareaList || hcTfysworkareaList.isEmpty()) {
                log.info(String.format("没有要传输甬金衢上土方压实的数据" + DateUtils.now()));
                continue;
            }
            for (HcTfysworkarea hcTfysworkarea : hcTfysworkareaList) {

                JSONObject sendObject = new JSONObject();
                JSONObject dateObject = new JSONObject();
                sendObject.put("INDEX_ID", sysDepartproject.getId());
                sendObject.put("TREEID", sysDepartproject.getTreeid());
                sendObject.put("CREATEDATE", sdf.format(hcTfysworkarea.getStarttime()));
                sendObject.put("UPDATEDATE", "");
                sendObject.put("OLD_ID", sysDepartproject.getOldid());
                sendObject.put("DWGC", hcTfysworkareapeiz.getXiangmu());
                sendObject.put("FBGC", hcTfysworkareapeiz.getBiaoduan());
                sendObject.put("FXGC", hcTfysworkareapeiz.getDanwei());
                sendObject.put("ZHBW", departName);
                sendObject.put("SGDYZH", getkx(hcTfysworkarea.getStartstation()) + " - " + getkx(hcTfysworkarea.getEndstation()));

                String starttime = sdf.format(hcTfysworkarea.getStarttime());
                String endtime = sdf.format(hcTfysworkarea.getEndtime());
                String sectionid = hcTfysworkarea.getSectionid();
                List<HcMachineOnlinerecord> hcMachineOnlinerecordList = hcMachineOnlinerecordService.findByStarttimeAndEndtime(starttime, endtime, sectionid);
                for (int i = 0; i < hcMachineOnlinerecordList.size(); i++) {
                    String machineid = hcMachineOnlinerecordList.get(i).getMachineid();
                    LambdaQueryWrapper<HcMachine> queryWrapper2 = new LambdaQueryWrapper<>();
                    queryWrapper2.eq(HcMachine::getMachineid, machineid);
                    HcMachine hcMachine = hcMachineService.getOne(queryWrapper2);
                    if (null != hcMachine) {
                        sendObject.put("SGJX_MC" + (i + 1), hcMachine.getMachinename());
                        sendObject.put("SGJX_XH" + (i + 1), hcMachine.getMachinetypename());
                    }
                }

                SecureRandom random = new SecureRandom();
                String s = String.valueOf(random.nextInt());
                String time = String.valueOf(System.currentTimeMillis());
                String signature = SignUtil.signature("e6540129236a44639778678879ce47bb", s, time);

                List list = new ArrayList();
                list.add(sendObject);

                dateObject.put("appId", "b598a75acb65427c979d0bdd317f9846");
                dateObject.put("useId", "89ce0523e50f42798150ba1cb33cfd0f");
                dateObject.put("type", "cu");
                dateObject.put("params", list.toString());
                System.out.println(dateObject);
                String back = HttpRequest.post("https://fjhmtd.com:15269/glaf/website/ws/execute/api/crud")
                        .header("x-rio-seq", s)
                        .header("signature", signature)
                        .header("timestamp", time)
                        .form(dateObject)
                        .timeout(20000)
                        .execute().body();

                pushandreturnService.saveData(id, String.valueOf(sendObject), selectsysconfigone.getRemark(), back);

                String topid = "";
                if (null != back && !back.isEmpty()) {
                    JSONObject jsonObject1 = JSONUtil.parseObj(back);
                    String code = jsonObject1.getStr("status");
                    if (code.equals("200")) {
                        log.info(String.format("甬金衢上土方压实数据传输成功" + DateUtils.now()));
                        hcTfysworkareapeiz.setIsmt(1);
                        hcTfysworkarea.setIsmt(1);
                    } else {
                        log.info(String.format("甬金衢上土方压实数据传输失败" + DateUtils.now()));
                        hcTfysworkareapeiz.setIsmt(3);
                        hcTfysworkarea.setIsmt(3);
                    }
                    hcTfysworkareapeizService.updateById(hcTfysworkareapeiz);
                    hcTfysworkareaService.updateById(hcTfysworkarea);
                    topid = jsonObject1.getJSONArray("data").getJSONObject(0).getStr("id");
                }

                LambdaQueryWrapper<HcTfstationdetail> queryWrapper2 = new LambdaQueryWrapper<>();
                queryWrapper2.eq(HcTfstationdetail::getBlockid, hcTfysworkarea.getBlockid());
                List<HcTfstationdetail> hcTfstationdetailList = hcTfstationdetailService.list(queryWrapper2);
                List list1 = new ArrayList();
                JSONObject senddateObject = new JSONObject();
                int a = 1;
                for (HcTfstationdetail hcTfstationdetail : hcTfstationdetailList) {
                    JSONObject sendzhuangObject = new JSONObject();

                    sendzhuangObject.put("TOPID", topid);
                    sendzhuangObject.put("LISTNO", a);
                    sendzhuangObject.put("YSQC", hcTfstationdetail.getStation());
                    sendzhuangObject.put("TZHD", hcTfstationdetail.getAvgthick());
                    sendzhuangObject.put("YSBS", hcTfstationdetail.getAvgtimes());
//                    sendzhuangObject.put("QDYSD", hcTfstationdetail.getQdyds());
                    a++;
                    list1.add(sendzhuangObject);

                }
                senddateObject.put("appId", "b598a75acb65427c979d0bdd317f9846");
                senddateObject.put("useId", "89ce0523e50f42798150ba1cb33cfd0f");
                senddateObject.put("type", "cu");
                senddateObject.put("params", list1.toString());

                String zhuangback = HttpRequest.post("https://fjhmtd.com:15269/glaf/website/ws/execute/api/crud")
                        .header("x-rio-seq", s)
                        .header("signature", signature)
                        .header("timestamp", time)
                        .form(senddateObject)
                        .timeout(20000)
                        .execute().body();

                pushandreturnService.saveData(id, String.valueOf(senddateObject), selectsysconfigone.getRemark(), zhuangback);
                if (null != zhuangback && !zhuangback.isEmpty()) {
                    JSONObject jsonObject1 = JSONUtil.parseObj(zhuangback);
                    String code = jsonObject1.getStr("status");
                    if (code.equals("200")) {
                        log.info(String.format("甬金衢上土方压实数据传输成功！" + DateUtils.now()));
                        hcTfysworkareapeiz.setIsmt(2);
                        hcTfysworkarea.setIsmt(2);
                    } else {
                        log.info(String.format("甬金衢上土方压实数据传输失败！" + DateUtils.now()));
                        hcTfysworkareapeiz.setIsmt(3);
                        hcTfysworkarea.setIsmt(3);
                    }
                    hcTfysworkareapeizService.updateById(hcTfysworkareapeiz);
                    hcTfysworkareaService.updateById(hcTfysworkarea);
                }
            }
            sysConfigService.updateSysConfig(JobUtil.YJQS_HJJC, id);
        }
    }

    public static String getkx(String station) {
        int stationInt = Integer.parseInt(station);
        String formattedStation = String.format("K%04d", stationInt);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < formattedStation.length(); i++) {
            char c = formattedStation.charAt(i);
            if (i == formattedStation.length()-3) {
                sb.append('+');
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
