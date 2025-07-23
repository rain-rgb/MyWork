package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.JypWztz;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.util.HttpUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCJypWztzJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private IWzliaocangService wzliaocangService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_JypWztz);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到检验批数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        // 仓储类型检验批推送
        List<JypWztz> jypWztzs = wztaizhangService.queryJypList();//查询jystatus=0，isfinish=1的数据进行推送
        if (null == jypWztzs || jypWztzs.size() == 0){
            log.info(String.format("暂无检验批未推送数据" + DateUtils.now()));
            return;
        }
        for (JypWztz jypWztz : jypWztzs) {

            JSONObject jsonObject1 =new JSONObject();
            jsonObject1.set("departId",jypWztz.getSysOrgCode());
            jsonObject1.set("pici",jypWztz.getPici());
            jsonObject1.set("cailiaoName",jypWztz.getCailiaoName());
            jsonObject1.set("guigexinghao",jypWztz.getGuigexinghao());
            jsonObject1.set("SupplierUnit",jypWztz.getGongyingshangName());
            jsonObject1.set("jingzhong",jypWztz.getJingzhongT()+jypWztz.getDanwei());
            jsonObject1.set("jinchangshijian",jypWztz.getJinchangshijian());
            jsonObject1.set("cunfangdidian",jypWztz.getCunfangdidian());
            if(StringUtils.isNotBlank(jypWztz.getLcbh())){
                Wzliaocang queryselectone = wzliaocangService.queryselectone(jypWztz.getLcbh());
                if (queryselectone == null) {
                }else{
                    jsonObject1.set("lcbh",queryselectone.getName());
                }
            }
            jsonObject1.set("sampleGcbw",jypWztz.getSampleGcbw());
            jsonObject1.set("shengchanpihao",jypWztz.getShengchanpihao());
            jsonObject1.set("url",jypWztz.getZhibaodan());
            jsonObject1.set("chuchangriqi", jypWztz.getChuchangshijian());
            log.info("推送消息记录{}       "  +  jsonObject1);
            String back = HttpUtils.httpPost("http://121.40.163.88:8084/CATDPS/WzReceivingController.do?InspectionLotData", String.valueOf(jsonObject1));
            JSONObject jsonObject2 = new JSONObject(back);
            String success =  jsonObject2.get("success").toString();
            if ("0".equals(success)){
                log.info(String.format("瑞仓检验批数据推送成功!" + jypWztz.getId()));
                wztaizhangService.update(new UpdateWrapper<Wztaizhang>().lambda().set(Wztaizhang::getJystatus,1).eq(Wztaizhang::getId,jypWztz.getId()));//推送成功，修改jystatus值为1
                sysConfigService.updateSysConfig(JobUtil.RC_JypWztz,jypWztz.getId());
            } else {
                log.info(String.format("瑞仓检验批数据推送失败!" + jypWztz.getId()));
                wztaizhangService.update(new UpdateWrapper<Wztaizhang>().lambda().set(Wztaizhang::getJystatus,2).eq(Wztaizhang::getId,jypWztz.getId()));//推送成功，修改jystatus值为2
                sysConfigService.updateSysConfig(JobUtil.RC_JypWztz,jypWztz.getId());
            }
        }
    }
}
