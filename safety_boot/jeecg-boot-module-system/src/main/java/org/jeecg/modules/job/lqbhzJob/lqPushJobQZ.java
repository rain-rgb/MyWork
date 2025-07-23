package org.jeecg.modules.job.lqbhzJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_token.service.IHcTokenService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.RCJob.RSAUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Arrays;
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
public class lqPushJobQZ implements Job {

    // 衢州质监站沥青数据推送
    // http://pps.public-soft.com:17100/asphaltApi/receiveData
    private static String url = "http://qz.sxght.net/prod-api/municipal/asphaltApi/receiveData";
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZJZ_KHCM_LQ);//沥青拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到开化成明沥青数据推送衢州质监站" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);

        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("未获取到开化成明沥青数据推送衢州质监站的设备" + DateUtils.now()));
            return;
        }

        String clientSecret = RSAUtil.queryByIdkhlq();
      //  String clientSecret = "EF5hFVPwMTxAnJz9pOC7PBvoYw3l9fs8aTa14X5j1IUaOf/e8yfwIhMZPvVYFcT4DxXYpYoJW2yRySOwwv9pbMi6NvUAgla08fmsxXn/RWuqz0kRv+T+gB1bMd60MyASGe56zcAbDvBCZ2J2tJe+hKlL6ySO0/GzdtUZhbcq5EY=";
        String token ="";
        String back1 = HttpRequest.post("http://qz.sxght.net/prod-api/municipal/getAccToken")
                .form("data",clientSecret)
//                    .header("clientSecret", clientSecret)
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(back1);
        Integer code1 = (Integer) jsonObject1.get("code");
        if (code1.equals(200)) {
           token = (String) jsonObject1.get("data");
        }


        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 时间格式化器
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1= Arrays.asList(split);
        List<BhzLqBases> bhzLqBases = lqBasesService.selectlqbhzBysb(curid,strsToList1);
        for (BhzLqBases bhzLqBase : bhzLqBases) {

            id = bhzLqBase.getId();
            JSONObject sendDate = new JSONObject();
            sendDate.set("mixingMachineId","30");// 拌合站id


//            String formulaNo = bhzLqBase.getFormulaNo();
//            QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
//            sysDictQueryWrapper.eq("dict_code", "hhllx");
//            SysDict one = sysDictService.getOne(sysDictQueryWrapper);
//            String SysDictId = one.getId();
//            QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
//            sysDictItemQueryWrapper.eq("dict_id",SysDictId);
//            sysDictItemQueryWrapper.eq("item_value",formulaNo);
//            SysDictItem one1 = sysDictItemService.getOne(sysDictItemQueryWrapper);
//            String itemText = one1.getItemText();

            sendDate.set("formulaCode",bhzLqBase.getFormulaNo());// 配方 string 否
            sendDate.set("TIME",bhzLqBase.getChuliaoshijian());
            sendDate.set("startTime",bhzLqBase.getChuliaoshijian());//拌合开始时间（yyyy-MM-dd HH:mm:ss）
            sendDate.set("endTime",bhzLqBase.getChuliaoshijian());// 出料时间，yyyy-MM-dd HH:mm:ss string 是
            sendDate.set("mixingTime",bhzLqBase.getBanheshijian());// 搅拌时间，单位为秒 number 是

            sendDate.set("bhTemp",bhzLqBase.getChuliaowd());// 拌合温度
           // sendDate.set("jlTemp",bhzLqBase.getGuliaowd());// 集料温度
            sendDate.set("jlTemp","--");// 集料温度
            sendDate.set("asphaltTemperature",bhzLqBase.getLiqingwd());// 沥青温度/出料温度，单位℃ number 否
            sendDate.set("aggregateTemperature","--");// 骨料温度/集料加热温度，单位℃ number 否
            sendDate.set("temperature",bhzLqBase.getChuliaowd());// 搅拌温度/沥青加热温度，单位℃ number 否
            sendDate.set("weight",bhzLqBase.getZongchanliang());// 单锅总重量，单位kg number 是


            //材料表
            QueryWrapper<BhzLqCailiao> lqCailiaoQueryWrapper = new QueryWrapper<>();
            lqCailiaoQueryWrapper.eq("base_guid", bhzLqBase.getGuid());
            List<BhzLqCailiao> lqCailiaoList = lqCailiaoService.list(lqCailiaoQueryWrapper);
            sendDate.set("g1",0);sendDate.set("g2",0);sendDate.set("g3",0);sendDate.set("g4",0);sendDate.set("g5",0);sendDate.set("g6",0);sendDate.set("g7",0);sendDate.set("g8",0);sendDate.set("g9",0);sendDate.set("g10",0);
            sendDate.set("f1",0);sendDate.set("f2",0);sendDate.set("f3",0);sendDate.set("f4",0);sendDate.set("f5",0);
            for (BhzLqCailiao bhzLqCailiao : lqCailiaoList) {
                String cailiaoming = bhzLqCailiao.getCailiaoming();

                if ("石料1".equals(cailiaoming)) {
                    sendDate.set("g1",bhzLqCailiao.getShijiyongliang());// 骨料1重量，单位kg number 是
                   // sendDate.set("aggregate1TheoryMixRatio",bhzLqCailiao.getLilunpb());// 骨料1理论配合比，百分比 number 否
                }
                if ("石料2".equals(cailiaoming)) {
                    sendDate.set("g2",bhzLqCailiao.getShijiyongliang());// number 否
                  //  sendDate.set("aggregate2TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("石料3".equals(cailiaoming)) {
                    sendDate.set("g3",bhzLqCailiao.getShijiyongliang());// number 否
                   // sendDate.set("aggregate3TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("石料4".equals(cailiaoming)) {
                    sendDate.set("g4",bhzLqCailiao.getShijiyongliang());// number 否
                 //   sendDate.set("aggregate4TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("石料5".equals(cailiaoming)) {
                    sendDate.set("g5",bhzLqCailiao.getShijiyongliang());// number 否
                 //   sendDate.set("aggregate5TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("石料6".equals(cailiaoming)) {
                    sendDate.set("g6",bhzLqCailiao.getShijiyongliang());// number 否
                   // sendDate.set("aggregate6TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("石料7".equals(cailiaoming)) {
                    sendDate.set("g7",bhzLqCailiao.getShijiyongliang());// number 否
                  //  sendDate.set("aggregate7TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("矿粉".equals(cailiaoming)) {
                    sendDate.set("f1",bhzLqCailiao.getShijiyongliang());// number 否
                   // sendDate.set("concrete1TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("回粉".equals(cailiaoming)) {
                    sendDate.set("f2",bhzLqCailiao.getShijiyongliang());// number 否
                    //  sendDate.set("powder1TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("粉料1".equals(cailiaoming)) {
                    sendDate.set("f1",bhzLqCailiao.getShijiyongliang());// number 否
                    // sendDate.set("concrete1TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("粉料2".equals(cailiaoming)) {
                    sendDate.set("f2",bhzLqCailiao.getShijiyongliang());// number 否
                    // sendDate.set("concrete1TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
                if ("粉料3".equals(cailiaoming)) {
                    sendDate.set("f3",bhzLqCailiao.getShijiyongliang());// number 否
                    // sendDate.set("concrete1TheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }

                if ("沥青".equals(cailiaoming)) {
                    sendDate.set("asphalt",bhzLqCailiao.getShijiyongliang());// number 否
                   // sendDate.set("asphaltTheoryMixRatio",bhzLqCailiao.getLilunpb());// number 否
                }
            }
            String body = HttpRequest.post(url)
                     .header("Authorization", token)
                    .form("jsonData",sendDate)
                   // .body(String.valueOf(sendDate))
                    .execute()
                    .body();

            if (body.contains("请求成功")){
                log.info("开化沥青推送衢州市质监站！");
            }else {
                log.info("开化沥青推送衢州市质监站！");
            }
            sysConfigService.updateSysConfig(JobUtil.ZJZ_KHCM_LQ, id);//根据传过来的唯一值来修改当前判断到的数据id
            pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), body);
        }
    }

//    public String getToken(){
//        JSONObject sendDate = new JSONObject();
//        sendDate.set("appId","YD02");
//        sendDate.set("pw","123456789");
//
//        String result = HttpRequest.post("https://dp.huace.cn/digitalPlatform/open/api/token/get.shtml")
//                .body(String.valueOf(sendDate))
//                .execute()
//                .body();
//
//        JSONObject jsonObject = new JSONObject(result);
//
//        JSONObject dataObj = jsonObject.getJSONObject("data");
//        String token = dataObj.getStr("token");
//        return token;
//    }
}
