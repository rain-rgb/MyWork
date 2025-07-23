package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCyajiangJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrYajiangSService yajiangSService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCYAJIANG);//压浆推送阳光工程
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓压浆定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓压浆的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrYajiangM> trYajiangMS = yajiangMService.selectyjList(shebeilist, curid);
        if (null == trYajiangMS || trYajiangMS.size() == 0) {
            log.info(String.format("暂无瑞仓压浆未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrYajiangM yajiangM : trYajiangMS) {
            id = yajiangM.getId();
            JSONObject sendObject = JSONUtil.createObj();
            sendObject.set("syjid",yajiangM.getSyjid());
            sendObject.set("sgdw",yajiangM.getSgdw());
            sendObject.set("jldw",yajiangM.getJldw());
            sendObject.set("htbh",yajiangM.getHtbh());
            sendObject.set("gcmc",yajiangM.getGcmc());
            sendObject.set("yjsj",yajiangM.getYjsj());
            sendObject.set("zhbw",yajiangM.getZhbw());
            sendObject.set("sgbw",yajiangM.getSgbw());
            sendObject.set("gjjg",yajiangM.getGjjg());
            sendObject.set("gjbh",yajiangM.getGjbh());
            sendObject.set("qw",yajiangM.getQw());
            sendObject.set("cjsjl",yajiangM.getCjsjl());
            sendObject.set("cpzjl",yajiangM.getCpzjl());
            sendObject.set("sw",yajiangM.getSw());
            sendObject.set("shuijiaobi",yajiangM.getShuijiaobi());
            sendObject.set("snyl",yajiangM.getSnyl());
            sendObject.set("yjwd",yajiangM.getYjwd());
            sendObject.set("msl",yajiangM.getMsl());
            sendObject.set("beiyong",yajiangM.getBeiyong());
            sendObject.set("yjsbbh",yajiangM.getYjsbbh());
            sendObject.set("lblx",yajiangM.getLblx());
            sendObject.set("lianghao",yajiangM.getLianghao());
            sendObject.set("zlsj",yajiangM.getZlsj());
            sendObject.set("yajiangji",yajiangM.getYajiangji());
            sendObject.set("snmc",yajiangM.getSnmc());
            sendObject.set("kongdaoshu",yajiangM.getKongdaoshu());
            sendObject.set("yajiangfang",yajiangM.getYajiangfang());
            sendObject.set("yajiangbuzh",yajiangM.getYajiangbuzh());
            sendObject.set("yajiangguoc",yajiangM.getYajiangguoc());
            sendObject.set("chushisudu",yajiangM.getChushisudu());
            sendObject.set("memo",yajiangM.getMemo());
            sendObject.set("liudongdu",yajiangM.getLiudongdu());
            sendObject.set("status",yajiangM.getStatus());
            sendObject.set("guid",yajiangM.getGuid());
            sendObject.set("uuid",yajiangM.getUuid());
            sendObject.set("issend",yajiangM.getIssend());
            sendObject.set("statistics",yajiangM.getStatistics());
            sendObject.set("isruicang",yajiangM.getIsruicang());
            List listsendson = new ArrayList();
            QueryWrapper<TrYajiangS> yajiangSQueryWrapper = new QueryWrapper<>();
            yajiangSQueryWrapper.eq("syjid", yajiangM.getSyjid());
            List<TrYajiangS> trYajiangSs = yajiangSService.list(yajiangSQueryWrapper);
            for (TrYajiangS yajiangS : trYajiangSs) {
                JSONObject yajiangSObject = JSONUtil.createObj();
                yajiangSObject.set("fguid",yajiangS.getFguid());
                yajiangSObject.set("syjid",yajiangS.getSyjid());
                yajiangSObject.set("yajiangsj",yajiangS.getYajiangsj());
                yajiangSObject.set("status",yajiangS.getStatus());
                yajiangSObject.set("kongdao",yajiangS.getKongdao());
                yajiangSObject.set("holeid",yajiangS.getHoleid());
                yajiangSObject.set("yajiangmosh",yajiangS.getYajiangmosh());
                yajiangSObject.set("peihebi",yajiangS.getPeihebi());
                yajiangSObject.set("shuijiaobi",yajiangS.getShuijiaobi());
                yajiangSObject.set("jiaobansj",yajiangS.getJiaobansj());
                yajiangSObject.set("starttime",yajiangS.getStarttime());
                yajiangSObject.set("endtime",yajiangS.getEndtime());
                yajiangSObject.set("jinjiangyal",yajiangS.getJinjiangyal());
                yajiangSObject.set("fanjiangyal",yajiangS.getFanjiangyal());
                yajiangSObject.set("chixushijia",yajiangS.getChixushijia());
                yajiangSObject.set("jinjiangshu",yajiangS.getJinjiangshu());
                yajiangSObject.set("fanjianglia",yajiangS.getFanjianglia());
                yajiangSObject.set("zkyl",yajiangS.getZkyl());
                yajiangSObject.set("hege",yajiangS.getHege());
                yajiangSObject.set("tongguo",yajiangS.getTongguo());
                yajiangSObject.set("yjcs",yajiangS.getYjcs());
                yajiangSObject.set("mjqk",yajiangS.getMjqk());
                yajiangSObject.set("overproof_status",yajiangS.getOverproofStatus());
                listsendson.add(yajiangSObject);
            }
            sendObject.set("yajiangSList", listsendson);
//            System.out.println(sendObject);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/zlyj/yajiangMs/yJDataUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓压浆数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCYAJIANG, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else if(code == 402){
                log.info(String.format("瑞仓压浆数据推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCYAJIANG, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞仓压浆数据推送失败!" + id));
            }
        }
    }
}
