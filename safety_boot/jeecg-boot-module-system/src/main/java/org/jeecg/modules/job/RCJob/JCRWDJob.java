package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.hnthtconsign.entity.HnthtConsign;
import com.trtm.iot.hnthtconsign.service.IHnthtConsignService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName JCRWDJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/3/11 13:25
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JCRWDJob implements Job {

    @Autowired
    private IHnthtConsignService hnthtConsignService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_JCRWD);//瑞仓钢检测任务单
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓钢检测任务单定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓钢检测任务单的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1 = Arrays.asList(split);
        List<HnthtConsign> selectlist = hnthtConsignService.selectlist(strsToList1, curid);
        if (null == selectlist || selectlist.size() == 0) {
            log.info(String.format("暂无瑞仓钢检测任务单未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (HnthtConsign hnthtConsign : selectlist) {
            JSONObject sendObject = JSONUtil.createObj();
            id = hnthtConsign.getId();
            sendObject.set("uuid",hnthtConsign.getUuid());
            sendObject.set("projectname",hnthtConsign.getProjectname());
            sendObject.set("component",hnthtConsign.getComponent());
            sendObject.set("sgbwguid",hnthtConsign.getSgbwguid());
            sendObject.set("sgbwname",hnthtConsign.getSgbwname());
            sendObject.set("status",hnthtConsign.getStatus());
            sendObject.set("zldate",hnthtConsign.getZldate());
            sendObject.set("departid",hnthtConsign.getDepartid());
            sendObject.set("sytypeid",hnthtConsign.getSytypeid());
            sendObject.set("departname",hnthtConsign.getDepartname());
            sendObject.set("createBy",hnthtConsign.getCreateBy());
            sendObject.set("createTime",hnthtConsign.getCreateTime());
            sendObject.set("updateTime",hnthtConsign.getUpdateTime());
            sendObject.set("sysOrgCode",hnthtConsign.getSysOrgCode());
            sendObject.set("issend",hnthtConsign.getIssend());
            sendObject.set("shebeichangjia",hnthtConsign.getShebeichangjia());
            sendObject.set("shebeibianhao",hnthtConsign.getShebeibianhao());
            sendObject.set("isdel",hnthtConsign.getIsdel());

            String url = "https://zgj20.cncico.com/wlwpt/hnthtconsign/hnthtConsign/addOpen";
            String body = HttpRequest.post(url)
                    .body(String.valueOf(sendObject))
                    .timeout(20000)
                    .execute().body();
            System.out.println(body);
            JSONObject jsonObject1 = new JSONObject(body);
            Integer codes = Integer.parseInt(jsonObject1.get("code").toString());
            if(codes==200){
                log.info(String.format("瑞仓钢检测任务单推送成功!" + id+"Json数据"+sendObject));
            }else{
                log.info(String.format("瑞仓钢检测任务单推送失败!" + id+"Json数据"+sendObject));
            }
            sysConfigService.updateSysConfig(JobUtil.RC_JCRWD, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
