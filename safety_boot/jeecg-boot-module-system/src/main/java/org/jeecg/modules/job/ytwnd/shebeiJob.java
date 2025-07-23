package org.jeecg.modules.job.ytwnd;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName shebeiJob：
 * @Description TODO
 * @Author 55314
 * @Date 2024/3/26 16:02
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class shebeiJob implements Job {

    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "http://183.247.216.148:7030/tz_server/wlw_equipment/pushWlwEquipment";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LambdaQueryWrapper<ShebeiInfo> shebeiInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shebeiInfoLambdaQueryWrapper.likeRight(ShebeiInfo::getSysOrgCode, "A05A01A12A12A01")
                .eq(ShebeiInfo::getStatus1, 0);
        List<ShebeiInfo> list = shebeiInfoService.list(shebeiInfoLambdaQueryWrapper);
        for (ShebeiInfo shebeiInfo : list) {

            Integer sbtype = shebeiInfo.getSbtype();
            String equipType = "";
            if (sbtype==3){
                equipType = "WNSYJ";
            }else if (sbtype==4){
                equipType = "YLJ";
            } else if(sbtype==11){ // 标养室
                equipType = "YHSWSD";
            }
            else if (sbtype==12){
                equipType = "KZKYSYJ";
            }else {
                shebeiInfoService.updatestatus1(shebeiInfo.getSbjno(),3);
                pushandreturnService.saveData(1,"暂不推送","甬台温北段设备推送","1");
                continue;
            }
            JSONObject sendJson = new JSONObject();
            sendJson.set("project_code","TZ001");
            sendJson.set("equipType",equipType);
            sendJson.set("makerName",shebeiInfo.getMillname());
            sendJson.set("equipmentName",shebeiInfo.getSbname());
            sendJson.set("equipmentModel","");
            sendJson.set("equipmentNumber",shebeiInfo.getSbjno());
            sendJson.set("labId","");

            String body = HttpRequest.post(url)
                    .body(String.valueOf(sendJson))
                    .execute()
                    .body();

            if (body.contains("成功")){
                shebeiInfoService.updatestatus1(shebeiInfo.getSbjno(),1);
            }else {
                shebeiInfoService.updatestatus1(shebeiInfo.getSbjno(),2);
            }
            pushandreturnService.saveData(1,String.valueOf(sendJson),"甬台温北段设备推送",body);
        }

    }
}
