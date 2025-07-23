package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trgangjinbhcm.entity.TrGangjinbhcM;
import com.trtm.iot.trgangjinbhcm.service.ITrGangjinbhcMService;
import com.trtm.iot.trgangjinbhcs.entity.TrGangjinbhcS;
import com.trtm.iot.trgangjinbhcs.service.ITrGangjinbhcSService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName GJYJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/3/10 14:13
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class GJYJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrGangjinbhcSService iTrGangjinbhcSService;
    @Autowired
    private ITrGangjinbhcMService iTrGangjinbhcMService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_GJBHC);//瑞仓钢筋保护层
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到钢筋保护层定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输钢筋保护层的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1 = Arrays.asList(split);
        List<TrGangjinbhcM> trGangjinbhcMS = iTrGangjinbhcMService.selectGangJinLists(strsToList1,curid);
        if (null == trGangjinbhcMS || trGangjinbhcMS.size() == 0) {
            log.info(String.format("暂无钢筋保护层未推送数据" + DateUtils.now()));
            return;
        }
        int id=0;
        for (TrGangjinbhcM trGangjinbhcM:trGangjinbhcMS){
            List list = new ArrayList();
            JSONObject sendObject = JSONUtil.createObj();
            id = trGangjinbhcM.getId();
            sendObject.set("point",trGangjinbhcM.getPoint());
            sendObject.set("type",trGangjinbhcM.getType());
            sendObject.set("testid",trGangjinbhcM.getTestid());
            sendObject.set("targettype",trGangjinbhcM.getTargettype());
            sendObject.set("zonecount",trGangjinbhcM.getZonecount());
            sendObject.set("passrate",trGangjinbhcM.getPassrate());
            sendObject.set("checklocation",trGangjinbhcM.getChecklocation());
            sendObject.set("testerid",trGangjinbhcM.getTesterid());
            sendObject.set("checktime",trGangjinbhcM.getChecktime());
            sendObject.set("masterdiameter",trGangjinbhcM.getMasterspacing());
            sendObject.set("masterspacing",trGangjinbhcM.getMasterdiameter());
            sendObject.set("designthickness",trGangjinbhcM.getDesignthickness());
            sendObject.set("zdesignthickness",trGangjinbhcM.getZdesignthickness());
            sendObject.set("subdiameter",trGangjinbhcM.getSubdiameter());
            sendObject.set("subspacing",trGangjinbhcM.getSubspacing());
            sendObject.set("curveddiameter",trGangjinbhcM.getCurveddiameter());
            sendObject.set("code",trGangjinbhcM.getCode());
            sendObject.set("shebeiNo",trGangjinbhcM.getShebeiNo());
            sendObject.set("projectname",trGangjinbhcM.getProjectname());
            sendObject.set("sgbw",trGangjinbhcM.getSgbw());
            //获取子表数据
            List<TrGangjinbhcS> trGangjinbhcSs = iTrGangjinbhcSService.selectGangjinSList(trGangjinbhcM.getTestid());
            for (TrGangjinbhcS trGangjinbhcS:trGangjinbhcSs){
                JSONObject sendObjectson = JSONUtil.createObj();
                sendObjectson.set("testid",trGangjinbhcS.getTestid());
                sendObjectson.set("thickness",trGangjinbhcS.getThickness());
                sendObjectson.set("distance",trGangjinbhcS.getDistance());
                sendObjectson.set("strthickness",trGangjinbhcS.getStrthickness());
                sendObjectson.set("strdistance",trGangjinbhcS.getDistance());
                sendObjectson.set("beforedistance",trGangjinbhcS.getBeforedistance());
                sendObjectson.set("flagpassrate",trGangjinbhcS.getFlagpassrate());
                sendObjectson.set("signals",trGangjinbhcS.getSignals());
                sendObjectson.set("designthickness",trGangjinbhcS.getDesignthickness());
                sendObjectson.set("masterspacing",trGangjinbhcS.getMasterspacing());
                sendObjectson.set("direction",trGangjinbhcS.getDirection());
                list.add(sendObjectson);
            }
            sendObject.set("trGangjinbhcS",list);

            String url = "https://zgj20.cncico.com/wlwpt/trgangjinbhcm/trGangjinbhcM/add";
            String body = HttpRequest.post(url)
                    .body(String.valueOf(sendObject))
                    .timeout(20000)
                    .execute().body();
            System.out.println(body);
            JSONObject jsonObject1 = new JSONObject(body);
            Integer codes = Integer.parseInt(jsonObject1.get("code").toString());
            if(codes==200){
                log.info(String.format("瑞仓钢筋保护层推送成功!" + id+"Json数据"+sendObject));
            }else{
                log.info(String.format("瑞仓钢筋保护层推送失败!" + id+"Json数据"+sendObject));
            }
            sysConfigService.updateSysConfig(JobUtil.RC_GJBHC, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
