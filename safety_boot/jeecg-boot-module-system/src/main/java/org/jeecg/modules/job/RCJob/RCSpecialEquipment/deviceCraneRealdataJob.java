package org.jeecg.modules.job.RCJob.RCSpecialEquipment;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trtm.iot.devicecranerealdata.entity.DeviceCraneRealdata;
import com.trtm.iot.devicecranerealdata.service.IDeviceCraneRealdataService;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.RCJob.RSAUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class deviceCraneRealdataJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IDeviceCraneRealdataService deviceCraneRealdataService;
    @Autowired
    private ISysDepartService sysDepartService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.DEVICE_CRANE_REAL);//集团标准版龙门吊实时数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到集团标准版龙门吊实时数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输集团标准版龙门吊实时数据推送的组织机构" + DateUtils.now()));
            return;
        }
        String orgCodeList = jsonObject.getStr("orgCodeList");
        String[] split = orgCodeList.split(",");
        List<String> strsToList1 = Arrays.asList(split);
        List<String> shebeiList = new ArrayList<>();
        for (String s : strsToList1) {
            QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.likeRight("sys_org_code", s);
            queryWrapper.eq("sbtype", 21);
            List<ShebeiInfo> shebei = shebeiInfoService.list(queryWrapper);
            if (shebei != null || shebeiList.size() != 0) {
                for (ShebeiInfo shebeiInfo : shebei) {
                    shebeiList.add(shebeiInfo.getSbjno());
                }
            }
        }
        if (shebeiList.size() == 0) {
            log.info(String.format("没有配置要传输集团标准版龙门吊实时数据推送的设备" + DateUtils.now()));
            return;
        }
        QueryWrapper<DeviceCraneRealdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("id",curid);
        queryWrapper.in("device_code", shebeiList);
        queryWrapper.last("limit 100");
        List<DeviceCraneRealdata> realdataList = deviceCraneRealdataService.list(queryWrapper);
        if (null == realdataList || realdataList.size() == 0) {
            log.info(String.format("暂无集团标准版龙门吊实时数据推送的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (DeviceCraneRealdata data : realdataList) {
            id = data.getId();
            // 使用 Jackson 库将实体类转换为 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(data);
            JSONObject sendObject = objectMapper.readValue(json, JSONObject.class);
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(data.getDeviceCode());
            if (selectshebeione != null) {
                sendObject.set("deviceCode",selectshebeione.getSbname());
                SysDepart queryone = sysDepartService.queryone(selectshebeione.getSysOrgCode());
                if (queryone != null) {
                    sendObject.set("sectionId",queryone.getSectionid());
                    sendObject.set("projectId",queryone.getProjectid());
                }
            }
            sendObject.set("isDeleted",0);
            String token = null;
            String clientSecret = RSAUtil.queryById22("KJXOVlay6Bw0SDgp");
            String back1 = HttpRequest.post("https://yggc.cncico.com:1080/api/login/appToken")
                    .header("Content-Type", "application/json")
                    .header("clientid", "app-ext-iot")
                    .header("clientSecret", clientSecret)
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(back1);
            String code1 = (String) jsonObject1.get("code");
            if (code1.equals("00000")) {
                JSONObject model = (JSONObject) jsonObject1.get("model");
                token = (String) model.get("access_token");
                String post = HttpRequest.post("https://yggc.cncico.com:1080/api/iot/dxsb/craneRealdatas")
                        .header("Content-Type", "application/json")
                        .header("Authorization", String.format("%s %s", "Bearer", token))
                        .body(String.valueOf(sendObject))
                        .execute()
                        .body();
                JSONObject jsonObject2 = new JSONObject(post);
                String code2 = (String) jsonObject2.get("code");
                if(code2.equals("00000")){
                    log.info(String.format("集团标准版龙门吊实时数据推送成功!" + sendObject));
                    sysConfigService.updateSysConfig(JobUtil.DEVICE_CRANE_REAL, id);
                } else {
                    log.info(String.format("集团标准版龙门吊实时数据推送失败!" + sendObject));
                }
            }

        }
    }
}
