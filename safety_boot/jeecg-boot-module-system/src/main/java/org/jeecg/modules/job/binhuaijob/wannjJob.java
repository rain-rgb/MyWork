package org.jeecg.modules.job.binhuaijob;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.syj.entity.FWangnj;
import com.trtm.iot.syj.entity.FYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.IFWangnjService;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.util.SecurityUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * \* @author: zml
 * \* Date: 2022-08-17
 * \* Time: 13:08
 * \* Description: 滨淮万能机数据推送
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class wannjJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService tSyjzbService;
    //    @Autowired
//    private IFYalijiService ifYalijiService;
    @Autowired
    private IFWangnjService ifWangnjService;

    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BH_WANNJ);//万能机数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到滨淮万能机数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
//        String extra = selectsysconfigone.getExtra();
//        JSONObject jsonObject = JSONUtil.parseObj(extra);
//        if (null == jsonObject || jsonObject.isEmpty()) {
//            log.info("没有配置要传输滨淮万能机的设备" + DateUtils.now());
//            return;
//        }
        List<ShebeiInfo> shebeilists = shebeiInfoService.shebeilists(3, "A05A07A01A02A01%");
        String shebeilist = null;
        if (shebeilists.size() > 0) {
            for (ShebeiInfo shebeiInfo : shebeilists) {
                if (null == shebeilist) {
                    shebeilist = "'" + shebeiInfo.getSbjno() + "'";
                } else {

                    shebeilist = shebeilist + ",'" + shebeiInfo.getSbjno() + "'";
                }
            }
        }
//        String shebeilist = jsonObject.getStr("shebeilist");
        List<TSyjzb> tSyjzbList = tSyjzbService.selectListone(shebeilist);
        int id = 0;
        for (TSyjzb tSyjzb : tSyjzbList) {
            if(tSyjzb.getSylx() == null){
                tSyjzb.setIsbhkykz(50);
                tSyjzbService.updateById(tSyjzb);
                log.info("实验类型为null");
                continue;
            }
            id = tSyjzb.getId();
            String shebeino = tSyjzb.getSbbh();
//            if ("A05A07A01A02A01A06_WN_1360".equals(tSyjzb.getSbbh())) {
//                shebeino = "SN-004";
//            }
            String callbackParam = null;
            int detectionSampleId = 0;
            int taskId = 0;
            String sampleNumber = null;
            JSONArray jsonObject1 = binhuaiUtil.getdate(shebeino);
            Map<String, Object> map3 = new HashMap<>();
            System.out.println(jsonObject1);
            if (null != jsonObject1) {
                for (Object o : jsonObject1) {
                    JSONObject object = new JSONObject(o);
                    if (object.get("sampleNumber").equals(tSyjzb.getSjbh())) {
                        callbackParam = (String) object.get("callbackParam");
                        detectionSampleId = (int) object.get("detectionSampleId");
                        taskId = (int) object.get("taskId");
                        sampleNumber = (String) object.get("sampleNumber");
                        map3.put("callbackParam", callbackParam);
                        map3.put("taskId", taskId);
                        map3.put("detectionSampleId", detectionSampleId);
                        map3.put("sampleNumber", sampleNumber);
                    }
                    if (object.get("sampleNumber").equals(tSyjzb.getWtbh())) {
                        callbackParam = (String) object.get("callbackParam");
                        detectionSampleId = (int) object.get("detectionSampleId");
                        taskId = (int) object.get("taskId");
                        sampleNumber = (String) object.get("sampleNumber");
                        map3.put("callbackParam", callbackParam);
                        map3.put("taskId", taskId);
                        map3.put("detectionSampleId", detectionSampleId);
                        map3.put("sampleNumber", sampleNumber);
                    }
                }
                if (map3.size() > 0) {
                    JSONObject sendObject = JSONUtil.createObj();
                    sendObject.set("deviceNumber", shebeino);
                    sendObject.set("sampleNumber", sampleNumber);
                    sendObject.set("detectionSampleId", detectionSampleId);
                    sendObject.set("taskId", taskId);
                    if (tSyjzb.getSylx().equals("100048")) {
                        sendObject.set("type", "GJHJT");
                    } else if (tSyjzb.getSylx().equals("100049")) {
                        sendObject.set("type", "GJJXJT");
                    } else if (tSyjzb.getSylx().equals("100137")) {
                        sendObject.set("type", "WQXN");
                    } else {
                        sendObject.set("type", "GJYC");
                    }
                    sendObject.set("callbackParam", callbackParam);

                    List<Map<String, Object>> list = new ArrayList<>();

                    List<FWangnj> fYalijiList = ifWangnjService.selectFswannjData(tSyjzb.getSyjid());
                    String sjsl = tSyjzb.getSjsl();
                    if (StrUtil.isNotBlank(sjsl)) {
                        if (fYalijiList.size() < Integer.parseInt(sjsl)) {
                            log.info("滨淮万能机试验数据不全" + id + "Json数据" + sendObject + DateUtils.now());
                            continue;
                        }
                    } else {
                        if (fYalijiList.size() < 3) {
                            log.info("滨淮万能机试验数据不全" + id + "Json数据" + sendObject + DateUtils.now());
                            continue;
                        }
                    }
                    for (FWangnj fYaliji : fYalijiList) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("sybh", fYaliji.getSjxh());
                        map.put("zdl", fYaliji.getLz());
                        map.put("klqd", fYaliji.getLzqd());
                        map.put("klhz", fYaliji.getQflz());
                        map.put("qfqd", fYaliji.getQfqd());
                        map.put("dhbj", fYaliji.getDhbz());
                        map.put("qufz", fYaliji.getLz());
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("forceValue", fYaliji.getLzgc());
                        map1.put("processTime", fYaliji.getSjgc());
                        map.put("processData", map1);
                        list.add(map);
                    }
                    sendObject.set("data", list);
                    String url = "https://wlwjc.e-jt.cn/zhjc_pc/bios/iot/data/entry";
                    System.out.println(sendObject);
                    int code = binhuaiUtil.getCode(sendObject, url);
                    if (code == 0) {
                        tSyjzb.setIsbhkykz(1);
                        tSyjzbService.updateById(tSyjzb);
                        log.info("滨淮万能机数据推送成功!" + id + "Json数据" + sendObject + DateUtils.now());
                    } else {
                        tSyjzb.setIsbhkykz(40);
                        tSyjzbService.updateById(tSyjzb);
                        log.info("滨淮万能机数据推送失败!" + id + "Json数据" + sendObject + DateUtils.now());
                    }
                } else {
                    tSyjzb.setIsbhkykz(10);
                    tSyjzbService.updateById(tSyjzb);
                    log.info(shebeino + "该设备没有配置试验任务单");
                }
            } else {
                tSyjzb.setIsbhkykz(20);
                tSyjzbService.updateById(tSyjzb);
                log.info(shebeino + "该设备没有回调参数");
            }
            sysConfigService.updateSysConfig(JobUtil.BH_WANNJ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
