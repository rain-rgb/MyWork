package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCbysJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBysRealService bysRealService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCBYS);//瑞仓标养室数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓标养室数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓标养室数据的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BysReal> list = bysRealService.selectBysList(curid,shebeilist);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无瑞仓标养室未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (BysReal bysReal : list){
            id = bysReal.getId();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("gatherDate",bysReal.getGatherdate());
            jsonObject1.set("shebeiNo",bysReal.getShebeino());
            jsonObject1.set("temperature",bysReal.getTemperature());
            jsonObject1.set("humidity",bysReal.getHumidity());
            jsonObject1.set("guid",bysReal.getGuid());
            jsonObject1.set("forwardstatus",bysReal.getForwardstatus());
            jsonObject1.set("temstatus",bysReal.getTemstatus());
            jsonObject1.set("humstatus",bysReal.getHumstatus());
            jsonObject1.set("alertstate",bysReal.getAlertstate());
            jsonObject1.set("status",bysReal.getStatus());
            jsonObject1.set("dataTime",bysReal.getDatatime());
            System.out.println(jsonObject1);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/syj/real/standardCuringRoomUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓标养室数据推送成功!" ));
                sysConfigService.updateSysConfig(JobUtil.RCBYS, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else if(code == 402){
                log.info(String.format("瑞仓标养室数据推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCBYS, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else {
                log.info(String.format("瑞仓标养室数据推送失败!" ));
            }
        }

    }
}
