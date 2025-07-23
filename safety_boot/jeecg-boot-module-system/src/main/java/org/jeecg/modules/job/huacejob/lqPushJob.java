package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @ClassName lqPushJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/6/13 16:20
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class lqPushJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/mixingstation/materialupload.shtml";
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzLqBasesService lqBasesService;
    @Autowired
    private IBhzLqCailiaoService lqCailiaoService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HC_LQ);//沥青拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东沥青拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东沥青拌合站的设备" + DateUtils.now()));
            return;
        }
        int id = 0;
        String token = getToken();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 时间格式化器
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzLqBases> bhzLqBases = lqBasesService.selectListtoHC(shebeilist, curid);
        for (BhzLqBases bhzLqBase : bhzLqBases) {

            id = bhzLqBase.getId();
            JSONObject sendDate = new JSONObject();
            sendDate.set("batchId","");// 批次id string 否
            sendDate.set("materialType",2);// 用料类型，1为水稳，2为沥青 number 是
            sendDate.set("mixtureStationId","288428544");// 拌和机器id，联系接口方获取 number 否

            sendDate.set("partnerStationId","");// 调用方拌和机器id，接口方做id映射，mixtureStationId和partnerStationId至少有一个不为空 string 否

            String formulaNo = bhzLqBase.getFormulaNo();
            QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
            sysDictQueryWrapper.eq("dict_code", "hhllx");
            SysDict one = sysDictService.getOne(sysDictQueryWrapper);
            String SysDictId = one.getId();
            QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
            sysDictItemQueryWrapper.eq("dict_id",SysDictId);
            sysDictItemQueryWrapper.eq("item_value",formulaNo);
            SysDictItem one1 = sysDictItemService.getOne(sysDictItemQueryWrapper);
            String itemText = one1.getItemText();

            sendDate.set("formula",itemText);// 配方 string 否
            sendDate.set("productionTime",bhzLqBase.getChuliaoshijian());// 生产时间，yyyy-MM-dd HH:mm:ss string 是
            sendDate.set("mixingTime",bhzLqBase.getBanheshijian());// 搅拌时间，单位为秒 number 是

            sendDate.set("asphaltAggregateRatio",bhzLqBase.getYoushibi());// 油石比 number 否
            sendDate.set("asphaltTemp",bhzLqBase.getLiqingwd());// 沥青温度/出料温度，单位℃ number 否
            sendDate.set("aggregateTemp",bhzLqBase.getGuliaowd());// 骨料温度/集料加热温度，单位℃ number 否
            sendDate.set("mixingTemp",bhzLqBase.getChuliaowd());// 搅拌温度/沥青加热温度，单位℃ number 否
            sendDate.set("totalWeight",bhzLqBase.getZongchanliang());// 单锅总重量，单位kg number 是

            Integer chaobiaodengji = bhzLqBase.getChaobiaodengji();
            Integer qualityStatus = 2;
            if (chaobiaodengji==0){
                qualityStatus = 1;
            }
            sendDate.set("qualityStatus",qualityStatus);// 质量，1：合格，2：不合格，3：无法计算 number 否
            sendDate.set("warningLevel",chaobiaodengji);// 告警等级，0为未达到告警值，1为初级，2为中级，3为高级 number 是
            sendDate.set("warningInfo",bhzLqBase.getOverReason());// 告警信息 string 否
            sendDate.set("asphaltAggregateTheoryRatio",bhzLqBase.getLlysb());// 油石比理论值，百分比

            //材料表
            QueryWrapper<BhzLqCailiao> lqCailiaoQueryWrapper = new QueryWrapper<>();
            lqCailiaoQueryWrapper.eq("base_guid", bhzLqBase.getGuid());
            List<BhzLqCailiao> lqCailiaoList = lqCailiaoService.list(lqCailiaoQueryWrapper);
            for (BhzLqCailiao bhzLqCailiao : lqCailiaoList) {
                String cailiaoming = bhzLqCailiao.getCailiaoming();
                if ("石料1".equals(cailiaoming)) {
                    sendDate.set("aggregate1",bhzLqCailiao.getShijiyongliang());// 骨料1重量，单位kg number 是
                    sendDate.set("aggregate1TheoryMixRatio",bhzLqCailiao.getLilunpb());// 骨料1理论配合比，百分比 number 否
                }
                if ("石料2".equals(cailiaoming)) {
                    sendDate.set("aggregate2",bhzLqCailiao.getShijiyongliang());// number 否
                    sendDate.set("aggregate2TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("石料3".equals(cailiaoming)) {
                    sendDate.set("aggregate3",bhzLqCailiao.getShijiyongliang());// number 否
                    sendDate.set("aggregate3TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("石料4".equals(cailiaoming)) {
                    sendDate.set("aggregate4",bhzLqCailiao.getShijiyongliang());// number 否
                    sendDate.set("aggregate4TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("石料5".equals(cailiaoming)) {
                    sendDate.set("aggregate5",bhzLqCailiao.getShijiyongliang());// number 否
                    sendDate.set("aggregate5TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("石料6".equals(cailiaoming)) {
                    sendDate.set("aggregate6",bhzLqCailiao.getShijiyongliang());// number 否
                    sendDate.set("aggregate6TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("石料7".equals(cailiaoming)) {
                    sendDate.set("aggregate7",bhzLqCailiao.getShijiyongliang());// number 否
                    sendDate.set("aggregate7TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("粉料1".equals(cailiaoming)) {
                    sendDate.set("concrete1",bhzLqCailiao.getShijiyongliang());// number 否
                    sendDate.set("concrete1TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("粉料2".equals(cailiaoming)) {
                    sendDate.set("powder1",bhzLqCailiao.getShijiyongliang());// number 否
                    sendDate.set("powder1TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("沥青".equals(cailiaoming)) {
                    sendDate.set("asphalt",bhzLqCailiao.getShijiyongliang());// number 否
                    sendDate.set("asphaltTheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
            }
            System.out.println(sendDate);
            String body = HttpRequest.post(url)
                    .header("token", token)
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();

            if (body.contains("请求成功")){
                log.info("义东沥青推送华测成功！");
            }else {
                log.info("义东沥青推送华测异常！");
            }
            sysConfigService.updateSysConfig(JobUtil.HC_LQ, id);//根据传过来的唯一值来修改当前判断到的数据id
            pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), body);
        }
    }

    public String getToken(){
        JSONObject sendDate = new JSONObject();
        sendDate.set("appId","YD02");
        sendDate.set("pw","123456789");

        String result = HttpRequest.post("https://dp.huace.cn/digitalPlatform/open/api/token/get.shtml")
                .body(String.valueOf(sendDate))
                .execute()
                .body();

        JSONObject jsonObject = new JSONObject(result);

        JSONObject dataObj = jsonObject.getJSONObject("data");
        String token = dataObj.getStr("token");
        return token;
    }
}
