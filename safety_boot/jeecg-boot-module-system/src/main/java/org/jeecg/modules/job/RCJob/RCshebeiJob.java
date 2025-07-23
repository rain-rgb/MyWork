package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCshebeiJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCSBTS);//瑞仓设备数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓设备数据定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        String curid = selectsysconfigone.getCurid().toString();
        String token = selectsysconfigone.getToken();
        String orgcode = "A05A01A05A01%";
        List<ShebeiInfo> list = shebeiInfoService.selectLists(orgcode,curid);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无瑞仓设备未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (ShebeiInfo shebeiInfo : list){
            JSONObject jsonObject = new JSONObject();
            id = Integer.parseInt(shebeiInfo.getId());
            jsonObject.set("create_by",shebeiInfo.getCreateBy());
            jsonObject.set("create_time",shebeiInfo.getCreateTime());
            jsonObject.set("update_by",shebeiInfo.getUpdateBy());
            jsonObject.set("update_time",shebeiInfo.getUpdateTime());
            jsonObject.set("sys_org_code",shebeiInfo.getSysOrgCode());
            jsonObject.set("sbtype",shebeiInfo.getSbtype());
            jsonObject.set("sbjno",shebeiInfo.getSbjno());
            jsonObject.set("sbname",shebeiInfo.getSbname());
            jsonObject.set("sbjsimplename",shebeiInfo.getSbjsimplename());
            jsonObject.set("shebei_status",shebeiInfo.getShebeiStatus());
            jsonObject.set("review_by",shebeiInfo.getReviewBy());
            jsonObject.set("review_time",shebeiInfo.getReviewTime());
            jsonObject.set("turndown_by",shebeiInfo.getTurndownBy());
            jsonObject.set("turndown_time",shebeiInfo.getTurndownTime());
            jsonObject.set("testid",shebeiInfo.getTestid());
            jsonObject.set("interfacetype",shebeiInfo.getInterfacetype());
            jsonObject.set("status",shebeiInfo.getStatus());
            jsonObject.set("longitude",shebeiInfo.getLongitude());
            jsonObject.set("latitude",shebeiInfo.getLatitude());
            jsonObject.set("projectname",shebeiInfo.getProjectname());
            jsonObject.set("procode",shebeiInfo.getProcode());
            jsonObject.set("status1",shebeiInfo.getStatus1());
            jsonObject.set("videolive",shebeiInfo.getVideolive());
            jsonObject.set("dtubaud",shebeiInfo.getDtubaud());
            jsonObject.set("projectid",shebeiInfo.getProjectid());
            jsonObject.set("sectionid",shebeiInfo.getSectionid());
            System.out.println(jsonObject);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/system/infos/sheBeiDataUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓设备数据推送推送成功!" +id));
                sysConfigService.updateSysConfig(JobUtil.RCSBTS, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞仓设备数据推送推送失败!" + id));
            }
        }
    }
}
