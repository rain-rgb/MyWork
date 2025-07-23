package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wzyclpidaichen.entity.Wzyclpidaichen;
import com.trtm.iot.wzyclpidaichen.service.IWzyclpidaichenService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class WzpidaichenPushData implements Job {
    @Autowired
    private IWzyclpidaichenService wzyclpidaichenService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private ISysConfigService sysConfigService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJQS_LC);//
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到甬金瞿上推送皮带秤数据任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);

        String sysOrgCode = jsonObject.getStr("sysOrgCode");
        String[] codes = sysOrgCode.split(",");
        String[] orgCodes = jsonObject.getStr("orgCode").split(",");
        String[] orgNames = jsonObject.getStr("orgName").split(",");
        for( int i =0 ;i<codes.length;i++){

            List<String> shebeis = shebeiInfoService.selectSbjnoListLikeOrgcode(sysOrgCode, 80);
            QueryWrapper<Wzyclpidaichen> queryWrapper = new QueryWrapper<>();
           // queryWrapper.likeRight("sys_org_code",codes[i]);
            queryWrapper.gt("id",curid);
             queryWrapper.in("shebeibianhao", shebeis);
            Page<Wzyclpidaichen> page = new Page<Wzyclpidaichen>(1, 100);
            IPage<Wzyclpidaichen> pageList = wzyclpidaichenService.page(page, queryWrapper);
            ArrayList sendDataList = new ArrayList<>();
            for(Wzyclpidaichen wz :pageList.getRecords()){
              //  ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(wz.getShebeibianhao());
                JSONObject sendObject = JSONUtil.createObj();
                sendObject.set("orgCode", orgCodes[i]);
                sendObject.set("orgName", orgNames[i]);
                sendObject.set("warehouseCode", wz.getLcbianhao());
                sendObject.set("warehouseName", wz.getLiaocangname());
                sendObject.set("materialName", wz.getCailiaoname());
                sendObject.set("materialStatus", wz.getCailiaoguige());
                sendObject.set("unit", "吨");
                sendObject.set("netWeight", wz.getJingzhong());
                sendObject.set("equipmentCode", wz.getShebeibianhao());
                sendObject.set("equipmentName", "甬金衢上三标梁场皮带秤");
                sendObject.set("uuid", wz.getGuid());
                sendObject.set("startTime", wz.getJinchangshijian());
                sendObject.set("saveTime", wz.getChuchangshijian());
                sendDataList.add(sendObject);
                curid = wz.getId();
            }

            // 测试地址
//            String urlcs = "http://121.41.26.120:8081/zjjg-iot-test/belt-scale/saveBelt";
//            String bodycs = HttpRequest.post(urlcs)
//                    .header("Content-Type", "application/json")
//                    .header("client-code","zjjg_test")
//                    .body(String.valueOf(sendDataList))
//                    .execute()
//                    .body();
//            System.out.println(bodycs);
//            JSONObject jsonObject1cs = new JSONObject(bodycs);
//            Integer returncodecs = (Integer) jsonObject1cs.get("code");
//            if (returncodecs == 200) {
//                log.info(String.format("甬金瞿上三标料仓数据推送成功!" + sendDataList ));
//            } else {
//                log.info(String.format("甬金瞿上三标料仓推送失败!" + "erro" + jsonObject1cs.get("msg")  ));
//            }

            // 正式地址
            String url = "http://121.41.26.120:8081/zjjg-iot/belt-scale/saveBelt";
            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .header("client-code","gaoxun")
                    .body(String.valueOf(sendDataList))
                    .execute()
                    .body();
            System.out.println(body);
            JSONObject jsonObject1 = new JSONObject(body);
            Integer returncode = (Integer) jsonObject1.get("code");
            if (returncode == 200) {
                log.info(String.format("甬金瞿上皮带秤数据推送成功!" + sendDataList ));
            } else {
                log.info(String.format("甬金瞿上皮带秤数据推送失败!" + "erro" + jsonObject1.get("msg")  ));
            }

            sysConfigService.updateSysConfig(JobUtil.YJQS_LC,curid);
            pushandreturnService.saveData(1,String.valueOf(sendDataList),"甬金瞿上皮带秤数据推送",body);
        }


    }
}
