package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.trgangjinbhcm.entity.TrGangjinbhcM;
import com.trtm.iot.trgangjinbhcm.service.ITrGangjinbhcMService;
import com.trtm.iot.trgangjinbhcs.entity.TrGangjinbhcS;
import com.trtm.iot.trgangjinbhcs.service.ITrGangjinbhcSService;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import com.trtm.iot.trhnthtm.service.ITrHnthtMService;
import com.trtm.iot.trhnthts.entity.TrHnthtS;
import com.trtm.iot.trhnthts.service.ITrHnthtSService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName HTYJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/3/10 16:59
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class HTYJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrHnthtMService iTrHnthtMService;
    @Autowired
    private ITrHnthtSService iTrHnthtSService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_HNTHT);//瑞仓钢筋保护层
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓混凝土回弹定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓混凝土回弹的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1 = Arrays.asList(split);
        List<TrHnthtM> trHnthtMS = iTrHnthtMService.selectHntHtLists(strsToList1, curid);
        if (null == trHnthtMS || trHnthtMS.size() == 0) {
            log.info(String.format("暂无瑞仓混凝土回弹未推送数据" + DateUtils.now()));
            return;
        }
        int id=0;
        for (TrHnthtM trGangjinbhcM:trHnthtMS){
            List list = new ArrayList();
            JSONObject sendObject = JSONUtil.createObj();
            id = trGangjinbhcM.getId();
            sendObject.set("point",trGangjinbhcM.getPoint());
            sendObject.set("checktime",trGangjinbhcM.getChecktime());
            sendObject.set("checklocation",trGangjinbhcM.getChecklocation());
            sendObject.set("testid",trGangjinbhcM.getTestid());
            sendObject.set("testerpeople",trGangjinbhcM.getTesterpeople());
            sendObject.set("place",trGangjinbhcM.getPlace());
            sendObject.set("strength",trGangjinbhcM.getStrength());
            sendObject.set("testingcount",trGangjinbhcM.getTestingcount());
            sendObject.set("detectionsurface",trGangjinbhcM.getDetectionsurface());
            sendObject.set("pouringsurface",trGangjinbhcM.getPouringsurface());
            sendObject.set("carbonizedepth",trGangjinbhcM.getCarbonizedepth());
            sendObject.set("detectionangle",trGangjinbhcM.getDetectionangle());
            sendObject.set("ispumping",trGangjinbhcM.getIspumping());
            sendObject.set("detectionstandard",trGangjinbhcM.getDetectionstandard());
            sendObject.set("pouringdate",trGangjinbhcM.getPouringdate());
            sendObject.set("testingmin",trGangjinbhcM.getTestingmin());
            sendObject.set("testingaverage",trGangjinbhcM.getTestingaverage());
            sendObject.set("standarddeviation",trGangjinbhcM.getStandarddeviation());
            sendObject.set("estimatedvalue",trGangjinbhcM.getEstimatedvalue());
            sendObject.set("showmin",trGangjinbhcM.getShowmin());
            sendObject.set("showaverage",trGangjinbhcM.getShowaverage());
            sendObject.set("showstandarddev",trGangjinbhcM.getShowstandarddev());
            sendObject.set("showestimatedval",trGangjinbhcM.getShowestimatedval());
            sendObject.set("code",trGangjinbhcM.getCode());
            sendObject.set("shebeiNo",trGangjinbhcM.getShebeiNo());
            sendObject.set("projectname",trGangjinbhcM.getProjectname());
            sendObject.set("sgbw",trGangjinbhcM.getSgbw());
            //获取子表数据
            List<TrHnthtS> trHnthtS = iTrHnthtSService.selectHntHtList(trGangjinbhcM.getTestid());
            for (TrHnthtS trGangjinbhcS:trHnthtS){
                JSONObject sendObjectson = JSONUtil.createObj();
                sendObjectson.set("testid",trGangjinbhcS.getTestid());
                sendObjectson.set("average",trGangjinbhcS.getAverage());
                sendObjectson.set("carbonize",trGangjinbhcS.getCarbonize());
                sendObjectson.set("strcar",trGangjinbhcS.getStrcar());
                sendObjectson.set("calsurface",trGangjinbhcS.getCalsurface());
                sendObjectson.set("calangle",trGangjinbhcS.getCalangle());
                sendObjectson.set("pouringsurface",trGangjinbhcS.getPouringsurface());
                sendObjectson.set("surface",trGangjinbhcS.getSurface());
                sendObjectson.set("carbonization",trGangjinbhcS.getCarbonization());
                sendObjectson.set("ispumping",trGangjinbhcS.getIspumping());
                sendObjectson.set("standardid",trGangjinbhcS.getStandardid());
                sendObjectson.set("flagcarbonization",trGangjinbhcS.getFlagcarbonization());
                sendObjectson.set("number",trGangjinbhcS.getNumber());
                list.add(sendObjectson);
            }
            sendObject.set("trHnthtS",list);

            String url = "https://zgj20.cncico.com/wlwpt/trhnthtm/trHnthtM/add";
            String body = HttpRequest.post(url)
                    .body(String.valueOf(sendObject))
                    .timeout(20000)
                    .execute().body();
            System.out.println(body);
            JSONObject jsonObject1 = new JSONObject(body);
            Integer codes = Integer.parseInt(jsonObject1.get("code").toString());
            if(codes==200){
                log.info(String.format("瑞仓混凝土回弹推送成功!" + id+"Json数据"+sendObject));
            }else{
                log.info(String.format("瑞仓混凝土回弹推送失败!" + id+"Json数据"+sendObject));
            }
            sysConfigService.updateSysConfig(JobUtil.RC_HNTHT, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}

