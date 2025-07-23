package org.jeecg.modules.ruicangjob.shiyan;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.hnthtconsign.entity.HnthtConsign;
import com.trtm.iot.hnthtconsign.service.IHnthtConsignService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import com.trtm.iot.trhnthtm.service.ITrHnthtMService;
import com.trtm.iot.trhnthts.entity.TrHnthtS;
import com.trtm.iot.trhnthts.service.ITrHnthtSService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.ruicangjob.RuiCangUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * \* @author: Xx
 * \* Date: 2022/1/5
 * \* Time: 13:39
 * \* Description: 瑞苍--混凝土回弹数据推送
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class HntHtJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ITrHnthtMService trHnthtMService;
    @Autowired
    private ITrHnthtSService trHnthtSService;
    @Autowired
    private IHnthtConsignService iHnthtConsignService;
    @Autowired
    private RuiCangUtil ruiCangUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RUICANG_HNTHT);//混凝土回弹数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞苍--混凝土回弹数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输混凝土回弹的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrHnthtM> trHnthtMS = trHnthtMService.selectHntHtsyList(shebeilist, curid);
        if (null == trHnthtMS || trHnthtMS.size() == 0) {
            log.info(String.format("暂无瑞苍--混凝土回弹未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrHnthtM trHnthtM : trHnthtMS) {
            JSONObject jsonObject1 = new JSONObject();//主表数据
            SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            id = trHnthtM.getId();
            String testid = trHnthtM.getTestid();
            String code = trHnthtM.getCode();
            HnthtConsign queryone = iHnthtConsignService.queryone(code);
            if (null == queryone) {
                log.info(String.format("暂无瑞苍--混凝土回弹任务单数据" + DateUtils.now()));
                continue;
            }
            jsonObject1.set("wtid", code);//委托id
            jsonObject1.set("sampleNo", code);//委托编号
            //jsonObject1.set("sampleDate","");//取样日期
            jsonObject1.set("projectName", trHnthtM.getSgbw());//施工部位
            jsonObject1.set("syrq", dateFm.format(trHnthtM.getChecktime()));//试验日期
            jsonObject1.set("createBy", queryone.getCreateBy());//创建人
            jsonObject1.set("createTime", dateFm.format(queryone.getCreateTime()));//创建时间
            jsonObject1.set("ampleShengChanPiHao", trHnthtM.getPoint());//生产批号
            jsonObject1.set("SBBH", trHnthtM.getShebeiNo());//设备编号
            List<TrHnthtS> trHnthtS = trHnthtSService.selectHntHtList(testid);//混凝土回弹子表测点数据
            if (null == trHnthtS || trHnthtS.size() == 0) {
                log.info(String.format("暂无瑞苍--混凝土回弹子表测点数据" + DateUtils.now()));
                continue;
            }
            Map map = new HashMap();//子表数据
            map.put("ampleQiangDuDengJi", trHnthtM.getStrength());//设计强度等级 (MPa)
            map.put("bengshnt", trHnthtM.getIspumping());//是否为泵送混凝土例:是或否
            map.put("cemzt", trHnthtM.getDetectionsurface());//测面状态 例：底 面
            if ("0".equals(trHnthtM.getDetectionangle()) || "水平".equals(trHnthtM.getDetectionangle())) {
                map.put("cesjd", "水平");// 测试角度（例：水平
            }
            //map.put("huityxh","");//回弹仪率定
            map.put("shiybz", "国标");//试验标准（例：国 标）
            for (int i = 0; i < trHnthtS.size(); i++) {
                if (i == 0) {

                    map.put("ceqh01", trHnthtM.getDetectionsurface());//测点测区1
                    map.put("schtz01", trHnthtS.get(i).getNumber());//实测回弹值1（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16）注意用英文逗号隔开
                    map.put("tanhsd01", String.valueOf(trHnthtS.get(i).getCarbonization()));//碳化深度 (mm)1
                    map.put("yuanshitanhuazhi011",String.valueOf(trHnthtS.get(i).getCarbonizetwo()));
                    map.put("yuanshitanhuazhi012",String.valueOf(trHnthtS.get(i).getCarbonizethree()));
                    map.put("yuanshitanhuazhi013",String.valueOf(trHnthtS.get(i).getFlagcarbonization()));
                } else if (i == 1) {

                    map.put("ceqh02", trHnthtM.getDetectionsurface());//测点测区1
                    map.put("schtz02", trHnthtS.get(i).getNumber());//实测回弹值1（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16）注意用英文逗号隔开
                    map.put("tanhsd02", String.valueOf(trHnthtS.get(i).getCarbonization()));//碳化深度 (mm)1
                    map.put("yuanshitanhuazhi021",String.valueOf(trHnthtS.get(i).getCarbonizetwo()));
                    map.put("yuanshitanhuazhi022",String.valueOf(trHnthtS.get(i).getCarbonizethree()));
                    map.put("yuanshitanhuazhi023",String.valueOf(trHnthtS.get(i).getFlagcarbonization()));
                } else if (i == 2) {

                    map.put("ceqh03", trHnthtM.getDetectionsurface());//测点测区1
                    map.put("schtz03", trHnthtS.get(i).getNumber());//实测回弹值1（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16）注意用英文逗号隔开
                    map.put("tanhsd03", String.valueOf(trHnthtS.get(i).getCarbonization()));//碳化深度 (mm)1
                    map.put("yuanshitanhuazhi031",String.valueOf(trHnthtS.get(i).getCarbonizetwo()));
                    map.put("yuanshitanhuazhi032",String.valueOf(trHnthtS.get(i).getCarbonizethree()));
                    map.put("yuanshitanhuazhi033",String.valueOf(trHnthtS.get(i).getFlagcarbonization()));
                } else if (i == 3) {
                    map.put("ceqh04", trHnthtM.getDetectionsurface());//测点测区1
                    map.put("schtz04", trHnthtS.get(i).getNumber());//实测回弹值1（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16）注意用英文逗号隔开
                    map.put("tanhsd04", String.valueOf(trHnthtS.get(i).getCarbonization()));//碳化深度 (mm)1
                    map.put("yuanshitanhuazhi041",String.valueOf(trHnthtS.get(i).getCarbonizetwo()));
                    map.put("yuanshitanhuazhi042",String.valueOf(trHnthtS.get(i).getCarbonizethree()));
                    map.put("yuanshitanhuazhi043",String.valueOf(trHnthtS.get(i).getFlagcarbonization()));
                } else if (i == 4) {

                    map.put("ceqh05", trHnthtM.getDetectionsurface());//测点测区1
                    map.put("schtz05", trHnthtS.get(i).getNumber());//实测回弹值1（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16）注意用英文逗号隔开
                    map.put("tanhsd05", String.valueOf(trHnthtS.get(i).getCarbonization()));//碳化深度 (mm)1
                    map.put("yuanshitanhuazhi051",String.valueOf(trHnthtS.get(i).getCarbonizetwo()));
                    map.put("yuanshitanhuazhi052",String.valueOf(trHnthtS.get(i).getCarbonizethree()));
                    map.put("yuanshitanhuazhi053",String.valueOf(trHnthtS.get(i).getFlagcarbonization()));
                } else if (i == 5) {

                    map.put("ceqh06", trHnthtM.getDetectionsurface());//测点测区1
                    map.put("schtz06", trHnthtS.get(i).getNumber());//实测回弹值1（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16）注意用英文逗号隔开
                    map.put("tanhsd06", String.valueOf(trHnthtS.get(i).getCarbonization()));//碳化深度 (mm)1
                    map.put("yuanshitanhuazhi061",String.valueOf(trHnthtS.get(i).getCarbonizetwo()));
                    map.put("yuanshitanhuazhi062",String.valueOf(trHnthtS.get(i).getCarbonizethree()));
                    map.put("yuanshitanhuazhi063",String.valueOf(trHnthtS.get(i).getFlagcarbonization()));
                } else if (i == 6) {
                    map.put("ceqh07", trHnthtM.getDetectionsurface());//测点测区1
                    map.put("schtz07", trHnthtS.get(i).getNumber());//实测回弹值1（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16）注意用英文逗号隔开
                    map.put("tanhsd07", String.valueOf(trHnthtS.get(i).getCarbonization()));//碳化深度 (mm)1
                    map.put("yuanshitanhuazhi071",String.valueOf(trHnthtS.get(i).getCarbonizetwo()));
                    map.put("yuanshitanhuazhi072",String.valueOf(trHnthtS.get(i).getCarbonizethree()));
                    map.put("yuanshitanhuazhi073",String.valueOf(trHnthtS.get(i).getFlagcarbonization()));
                } else if (i == 7) {

                    map.put("ceqh08", trHnthtM.getDetectionsurface());//测点测区1
                    map.put("schtz08", trHnthtS.get(i).getNumber());//实测回弹值1（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16）注意用英文逗号隔开
                    map.put("tanhsd08", String.valueOf(trHnthtS.get(i).getCarbonization()));//碳化深度 (mm)1
                    map.put("yuanshitanhuazhi081",String.valueOf(trHnthtS.get(i).getCarbonizetwo()));
                    map.put("yuanshitanhuazhi082",String.valueOf(trHnthtS.get(i).getCarbonizethree()));
                    map.put("yuanshitanhuazhi083",String.valueOf(trHnthtS.get(i).getFlagcarbonization()));
                } else if (i == 8) {

                    map.put("ceqh09", trHnthtM.getDetectionsurface());//测点测区1
                    map.put("schtz09", trHnthtS.get(i).getNumber());//实测回弹值1（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16）注意用英文逗号隔开
                    map.put("tanhsd09", String.valueOf(trHnthtS.get(i).getCarbonization()));//碳化深度 (mm)1
                    map.put("yuanshitanhuazhi091",String.valueOf(trHnthtS.get(i).getCarbonizetwo()));
                    map.put("yuanshitanhuazhi092",String.valueOf(trHnthtS.get(i).getCarbonizethree()));
                    map.put("yuanshitanhuazhi093",String.valueOf(trHnthtS.get(i).getFlagcarbonization()));
                } else if (i == 9) {

                    map.put("ceqh10", trHnthtM.getDetectionsurface());//测点测区1
                    map.put("schtz10", trHnthtS.get(i).getNumber());//实测回弹值1（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16）注意用英文逗号隔开
                    map.put("tanhsd10", String.valueOf(trHnthtS.get(i).getCarbonization()));//碳化深度 (mm)1
                    map.put("yuanshitanhuazhi101",String.valueOf(trHnthtS.get(i).getCarbonizetwo()));
                    map.put("yuanshitanhuazhi102",String.valueOf(trHnthtS.get(i).getCarbonizethree()));
                    map.put("yuanshitanhuazhi103",String.valueOf(trHnthtS.get(i).getFlagcarbonization()));
                }

            }

            jsonObject1.set("data", map);
            System.out.println(jsonObject1.toString());
            String s = ruiCangUtil.GETToken();
            if (s == null) {
                log.info(String.format("瑞苍--混凝土回弹获取token失败" + id + DateUtils.now()));
            }
            Integer integer = ruiCangUtil.HntHtTuiSong(jsonObject1, s);
            if (integer == 200) {
                TrHnthtM trHnthtM1 = new TrHnthtM();
                trHnthtM1.setId(id);
                trHnthtM1.setIstuisong(1);
                trHnthtMService.updateById(trHnthtM1);
                log.info(String.format("瑞苍--混凝土回弹数据推送成功" + id + DateUtils.now()));
                sysConfigService.updateSysConfig(JobUtil.RUICANG_HNTHT, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞苍--混凝土回弹数据推送失败" + id + DateUtils.now()));
            }
        }

    }
}
