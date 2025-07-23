package org.jeecg.modules.job.binhuaijob;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.syj.entity.FYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.expr.Component;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
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
 * \* Description: 滨淮抗折抗压一体机数据推送
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class kzkyJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService tSyjzbService;
    @Autowired
    private IFYalijiService ifYalijiService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BH_KZKY);//抗折抗压机数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到滨淮抗折抗压一体机数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
//        String extra = selectsysconfigone.getExtra();
//        JSONObject jsonObject = JSONUtil.parseObj(extra);
//        if (null == jsonObject || jsonObject.isEmpty()) {
//            log.info("没有配置要传输滨淮抗折抗压一体机的设备" + DateUtils.now());
//            return;
//        }
//        String shebeilist = jsonObject.getStr("shebeilist");
        List<ShebeiInfo> shebeilists = shebeiInfoService.shebeilists(12, "A05A07A01A02A01%");
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
        List<TSyjzb> tSyjzbList = tSyjzbService.selectListData(shebeilist);
        int id = 0;
        for (TSyjzb tSyjzb : tSyjzbList) {
            id = tSyjzb.getId();
            String shebeino = tSyjzb.getSbbh();
            String callbackParam = null;
            int detectionSampleId = 0;
            int taskId = 0;
            String sampleNumber = null;
            JSONArray jsonObject1 = binhuaiUtil.getdate(shebeino);
            Map<String, Object> map3 = new HashMap<>();
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
                    sendObject.set("taskId", taskId);
                    sendObject.set("detectionSampleId", detectionSampleId);
                    sendObject.set("sampleNumber", sampleNumber);
                    sendObject.set("type", "KZKY");
                    sendObject.set("callbackParam", callbackParam);
                    sendObject.set("lq", tSyjzb.getLq());
                    List<Object> list = new ArrayList<>();
                    List<Map<String, Object>> list2 = new ArrayList<>();
                    Map<String, Object> map = new HashMap<>();
                    List<FYaliji> fYalijiList = ifYalijiService.selectFsYalijiData(tSyjzb.getSyjid());
                    List<Map<String, Object>> list1 = new ArrayList<>();
                    for (FYaliji fYaliji : fYalijiList) {
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("sjhb", fYaliji.getSjxh());
                        map1.put("phhz", fYaliji.getKylz());
                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("forceValue", fYaliji.getYskylz());
                        map2.put("processTime", fYaliji.getSjgc());
                        map1.put("processData", map2);
                        if (Double.parseDouble(fYaliji.getKyqd()) > 10) {
                            map1.put("kyqdcdz", fYaliji.getKyqd());
                            list1.add(map1);
                        } else {
                            map1.put("kzqdcz", fYaliji.getKyqd());
                            list2.add(map1);
                        }
                    }
                    TSyjzb tSyjzb1 = null;
                    try {
                        tSyjzb1 = tSyjzbService.getsjbhData(tSyjzb.getSjbh(), tSyjzb.getSyjid(), tSyjzb.getSbbh());
                    } catch (Exception e) {
                        tSyjzb.setIsbhkykz(50);
                        tSyjzbService.updateById(tSyjzb);
                        log.info(shebeino + "该设备推送数据重复");
                        e.printStackTrace();
                    }
                    if (null != tSyjzb1) {
                        List<FYaliji> fYalijiLists = ifYalijiService.selectFsYalijiData(tSyjzb1.getSyjid());
                        for (FYaliji fYaliji : fYalijiLists) {
                            Map<String, Object> map1 = new HashMap<>();
                            map1.put("sjhb", fYaliji.getSjxh());
                            map1.put("phhz", fYaliji.getKylz());
                            Map<String, Object> map2 = new HashMap<>();
                            map2.put("forceValue", fYaliji.getYskylz());
                            map2.put("processTime", fYaliji.getSjgc());
                            map1.put("processData", map2);
                            if (Double.parseDouble(fYaliji.getKyqd()) > 10) {
                                map1.put("kyqdcdz", fYaliji.getKyqd());
                                list1.add(map1);
                            } else {
                                map1.put("kzqdcz", fYaliji.getKyqd());
                                list2.add(map1);
                            }
                        }
                    }
                    if (list1.size() >= 6 && list2.size() >= 3) {
                        map.put("ky", list1);
                        map.put("kz", list2);
                    } else {
                        log.info("滨淮抗折抗压一体机试验数据不全" + id + "Json数据" + sendObject + DateUtils.now());
                        continue;
                    }
                    list.add(map);
                    sendObject.set("data", list);
                  //  System.out.println(sendObject);
                    String url = "https://wlwjc.e-jt.cn/zhjc_pc/bios/iot/data/entry";
                    int code = binhuaiUtil.getCode(sendObject, url);
                    if (code == 0) {
                        tSyjzb.setIsbhkykz(1);
                        tSyjzbService.updateById(tSyjzb);
                        if (null != tSyjzb1) {
                            tSyjzb1.setIsbhkykz(1);
                            tSyjzbService.updateById(tSyjzb1);
                        }
                        log.info("滨淮抗折抗压一体机数据推送成功!" + id + "Json数据" + sendObject + DateUtils.now());
                        sysConfigService.updateSysConfig(JobUtil.BH_KZKY, id);//根据传过来的唯一值来修改当前判断到的数据id
                    } else {
                        tSyjzb.setIsbhkykz(40);
                        tSyjzbService.updateById(tSyjzb);
                        if (null != tSyjzb1) {
                            tSyjzb1.setIsbhkykz(40);
                            tSyjzbService.updateById(tSyjzb1);
                        }
                        log.info("滨淮抗折抗压一体机数据推送失败!" + id + "Json数据" + sendObject + DateUtils.now());
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
            sysConfigService.updateSysConfig(JobUtil.BH_KZKY, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
