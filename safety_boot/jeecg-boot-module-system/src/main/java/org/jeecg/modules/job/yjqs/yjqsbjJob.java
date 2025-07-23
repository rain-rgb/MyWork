package org.jeecg.modules.job.yjqs;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.syj.entity.FWangnj;
import com.trtm.iot.syj.entity.FsYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.IFWangnjService;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
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
public class yjqsbjJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    IWzycljinchanggbService wzycljinchanggbService;

    private static String loginName = "wangyafei";
    private static String password = "F2963AFA2DA2596A669EA57EFF2BC9A6";
    private static boolean isVerify = false;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJQS_JOB);//新增编辑母材收料
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
        List<Wzycljinchanggb> wzycljinchanggbs = wzycljinchanggbService.slistrqid(shebeilist, curid);
        int id = 0;
        if (wzycljinchanggbs.size() > 0){
            String gettoken = gettoken();
            for (Wzycljinchanggb wzycljinchanggb :wzycljinchanggbs){
                JSONObject sendObject = JSONUtil.createObj();
                id = wzycljinchanggb.getId();
                sendObject.set("id", 0);
                sendObject.set("sandFactoryNo", "050");
                sendObject.set("yewuRiqi", wzycljinchanggb.getJinchangshijian());
                sendObject.set("pici", "");
                sendObject.set("yuancailiaoLeixing", "002");
                sendObject.set("shangbaoren", "自动采集");//上报人
                sendObject.set("cheShu", 1);//车数
                sendObject.set("shuLiang", wzycljinchanggb.getMaozhongt());//数量
                sendObject.set("shiyanJielun", 1);
                System.out.println(sendObject);
                String back = HttpRequest.post("http://jzs.sgiot.xyz/api/production-prepare/material-in")
                        .header("Content-Type", "application/json")
                        .header("token",gettoken)
                        .body(String.valueOf(sendObject))
                        .execute()
                        .body();
                JSONObject jsonObject1 = new JSONObject(back);
                int message = (int) jsonObject1.get("statusCode");
                if (message == 200) {
                    wzycljinchanggb.setStatuscode(1);
                    log.info(String.format("新增编辑母材收料推送成功!" + id));
                } else {
                    wzycljinchanggb.setStatuscode(-1);
                    log.info(String.format("新增编辑母材收料推送失败!" + id));
                }
                wzycljinchanggbService.updateById(wzycljinchanggb);
                sysConfigService.updateSysConfig(JobUtil.YJQS_JOB, id);//根据传过来的唯一值来修改当前判断到的数据id
            }
        }
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

