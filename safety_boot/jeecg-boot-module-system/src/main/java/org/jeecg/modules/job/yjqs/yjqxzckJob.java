package org.jeecg.modules.job.yjqs;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.wzyclpidaichen.entity.Wzyclpidaichen;
import com.trtm.iot.wzyclpidaichen.service.IWzyclpidaichenService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName shiYanJiJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/16 15:13
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class yjqxzckJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWzyclpidaichenService wzyclpidaichenService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.CKJL_JOB);//新增出库记录
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到新增编辑母材收料的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输新增编辑母材收料的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Wzyclpidaichen> wzycljinchanggbs = wzyclpidaichenService.slistrqid(shebeilist, curid);
        int id = 0;
        if (wzycljinchanggbs.size() > 0){
            String gettoken = gettoken();
//            String getmcxh = getmcxh(gettoken);
            for (Wzyclpidaichen wzycljinchanggb :wzycljinchanggbs){
//                if (!wzycljinchanggb.getCailiaoguige().equals("10-25mm")){
//                    continue;
//                }
                JSONObject sendObject = JSONUtil.createObj();
                id = wzycljinchanggb.getId();
                sendObject.set("lineNo", "10366");//线路编号
                sendObject.set("bidNo", "108561");//标段编号
                sendObject.set("sandFactoryNo", "050");//砂场编号
                sendObject.set("chukuren", "自动采集");//出库人
                sendObject.set("jieshouBid", "108561");//接收标段
                sendObject.set("yewuRiqi", wzycljinchanggb.getJinchangshijian());
                sendObject.set("jieshouSHD", "688a934f5a6e4d7c8550672d64771996");//接收拌合站

                if (wzycljinchanggb.getCailiaoname().contains("砂")){
                    sendObject.set("yuancailiaoleixing", "004");//原材料类型
                }else if (wzycljinchanggb.getCailiaoguige().equals("5-10mm")){
                    sendObject.set("yuancailiaoleixing", "003");//原材料类型
                }else if (wzycljinchanggb.getCailiaoguige().equals("10-25mm")){
                    sendObject.set("yuancailiaoleixing", "011");//原材料类型
                }else if (wzycljinchanggb.getCailiaoguige().equals("25-31.5mm")){
                    sendObject.set("yuancailiaoleixing", "009");//原材料类型
                }else if (wzycljinchanggb.getCailiaoguige().equals("20-40mm")){
                    sendObject.set("yuancailiaoleixing", "008");//原材料类型
                }else if (wzycljinchanggb.getCailiaoguige().equals("16-31.5mm")){
                    sendObject.set("yuancailiaoleixing", "001");//原材料类型
                }else if (wzycljinchanggb.getCailiaoguige().equals("16-25mm")){
                    sendObject.set("yuancailiaoleixing", "007");//原材料类型
                }else if (wzycljinchanggb.getCailiaoguige().equals("10-20mm")){
                    sendObject.set("yuancailiaoleixing", "002");//原材料类型
                }else if (wzycljinchanggb.getCailiaoguige().equals("10-16mm")){
                    sendObject.set("yuancailiaoleixing", "006");//原材料类型
                }else if (wzycljinchanggb.getCailiaoguige().equals("5-20mm")){
                    sendObject.set("yuancailiaoleixing", "010");//原材料类型
                }else if (wzycljinchanggb.getCailiaoguige().equals("5-16mm")){
                    sendObject.set("yuancailiaoleixing", "005");//原材料类型
                }

                sendObject.set("shuLiang", wzycljinchanggb.getJingzhong());//出库数量（吨）

                if (wzycljinchanggb.getCailiaoguige().equals("0-4.75mm")){
                    sendObject.set("liaocangNo", "212");//料仓编号
                }else if (wzycljinchanggb.getCailiaoguige().equals("5-10mm")){
                    sendObject.set("liaocangNo", "213");//料仓编号
                }else if (wzycljinchanggb.getCailiaoguige().equals("10-25mm")){
                    sendObject.set("liaocangNo", "215");//料仓编号
                }

                System.out.println(sendObject);
                List<JSONObject> list = new ArrayList<>();
                list.add(sendObject);
                String back = HttpRequest.post("http://jzs.sgiot.xyz/api/production-management/out-history-data")
                        .header("Content-Type", "application/json")
                        .header("token",gettoken)
                        .body(String.valueOf(list))
                        .execute()
                        .body();
                JSONObject jsonObject1 = new JSONObject(back);
                System.out.println(jsonObject1);
                JSONObject data = (JSONObject) jsonObject1.get("data");
                int code = (int) data.get("code");
                String msg = (String) data.get("msg");
                if (msg.equals("出库失败,库存不足！")){
                    return;
                }
                if (code == -1) {
                    wzycljinchanggb.setStatuscode(-1);
                    log.info(String.format("新增编辑母材收料推送失败!" + id));
                } else {
                    wzycljinchanggb.setStatuscode(1);
                    log.info(String.format("新增编辑母材收料推送成功!" + id));
                }
                wzyclpidaichenService.updateById(wzycljinchanggb);
                sysConfigService.updateSysConfig(JobUtil.CKJL_JOB, id);//根据传过来的唯一值来修改当前判断到的数据id
            }
        }
    }
    //母材消耗批次号
    private static String getmcxh(String gettoken){
        String back = HttpRequest.get("http://jzs.sgiot.xyz/api/dictionary-management/sand-type-tag-list?type=OutPutMaterial&pageno=1&pagesize=10")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("token",gettoken)
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(back);
        JSONObject jsonObject2 =(JSONObject) jsonObject1.get("data");
        String accessToken = jsonObject2.get("code").toString();
        return accessToken;
    }
    //登录
    private static String gettoken(){
        JSONObject sendObject = JSONUtil.createObj();
        sendObject.set("loginName", "wangyafei");
        sendObject.set("password", "F2963AFA2DA2596A669EA57EFF2BC9A6");//密码需通过MD5 32位大写进行加密
        sendObject.set("isVerify", false);
        System.out.println(sendObject);
        String back = HttpRequest.post("http://jzs.sgiot.xyz/api/login/third-party-logins")
                .header("Content-Type", "application/json")
                .body(String.valueOf(sendObject))
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(back);
        JSONObject jsonObject2 =(JSONObject) jsonObject1.get("data");
        String accessToken = jsonObject2.get("accessToken").toString();
        return accessToken;
    }

}

