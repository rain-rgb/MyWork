package org.jeecg.modules.job.yggcJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName yaJiangJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/22 14:01
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class yaJiangJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrYajiangSService yajiangSService;
    @Autowired
    private ISysDepartService sysDepartService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HSY_YJ);//压浆推送阳光工程
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到压浆定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输压浆的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrYajiangM> trYajiangMS = yajiangMService.selectyjList(shebeilist, curid);
        if (null == trYajiangMS || trYajiangMS.size() == 0) {
            log.info(String.format("暂无压浆未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrYajiangM yajiangM : trYajiangMS) {
            JSONObject sendObject = JSONUtil.createObj();
            id = yajiangM.getId();
            sendObject.set("beiyong", yajiangM.getBeiyong());
            sendObject.set("chushisudu", yajiangM.getChushisudu());
            sendObject.set("cjsjl", yajiangM.getCjsjl());
            sendObject.set("cpzjl", yajiangM.getCpzjl());
            sendObject.set("gcmc", yajiangM.getGcmc());
            sendObject.set("gjbh", yajiangM.getGjbh());
            sendObject.set("gjjg", yajiangM.getGjjg());
            sendObject.set("htbh", yajiangM.getHtbh());
            sendObject.set("jldw", yajiangM.getJldw());
            sendObject.set("kongdaoshu", yajiangM.getKongdaoshu());
            sendObject.set("lblx", yajiangM.getLblx());
            sendObject.set("lianghao", yajiangM.getLianghao());
            sendObject.set("liudongdu", yajiangM.getLiudongdu());
            sendObject.set("memo", yajiangM.getMemo());
            sendObject.set("msl", yajiangM.getMsl());
            sendObject.set("qw", yajiangM.getQw());
            String sbbh = yajiangM.getYjsbbh();
            sendObject.set("shebeibianhao", sbbh);
            QueryWrapper<ShebeiInfo> shebeiInfo = new QueryWrapper();
            shebeiInfo.eq("sbjno", sbbh);
            ShebeiInfo one = shebeiInfoService.getOne(shebeiInfo);
            QueryWrapper<SysDepart> sysDepartQueryWrapper = new QueryWrapper();
            sysDepartQueryWrapper.eq("org_code", one.getSysOrgCode());
            SysDepart one1 = sysDepartService.getOne(sysDepartQueryWrapper);
            sendObject.set("sectionId", one1.getOrgCode());
            sendObject.set("sectionName", one1.getDepartName());
            sendObject.set("sgbw", yajiangM.getSgbw());
            sendObject.set("sgdw", yajiangM.getSgdw());
            sendObject.set("shuijiaobi", yajiangM.getShuijiaobi());
            sendObject.set("snmc", yajiangM.getSnmc());
            sendObject.set("snyl", yajiangM.getSnyl());
            sendObject.set("status", yajiangM.getStatus());
            sendObject.set("sw", yajiangM.getSw());
            sendObject.set("syjid", yajiangM.getSyjid());
            String departName = one1.getDepartName();
            if (departName.contains("监")) {
                sendObject.set("type", "1");
            } else if (departName.contains("交竣工")) {
                sendObject.set("type", "2");
            } else {
                sendObject.set("type", "0");
            }
            sendObject.set("uuid", yajiangM.getUuid());
            sendObject.set("yajiangbuzhou", yajiangM.getYajiangbuzh());
            sendObject.set("yajiangfangxiang", yajiangM.getYajiangfang());
            sendObject.set("yajiangguocheng", yajiangM.getYajiangguoc());
            sendObject.set("yajiangji", yajiangM.getYajiangji());
            sendObject.set("yjsbbh", yajiangM.getYjsbbh());
            sendObject.set("yjsj", yajiangM.getYjsj());
            sendObject.set("yjwd", yajiangM.getYjwd());
            sendObject.set("zhbw", yajiangM.getZhbw());
            sendObject.set("zlsj", yajiangM.getZlsj());

            List listsendson = new ArrayList();
            QueryWrapper<TrYajiangS> yajiangSQueryWrapper = new QueryWrapper<>();
            yajiangSQueryWrapper.eq("syjid", yajiangM.getSyjid());
            List<TrYajiangS> trYajiangSs = yajiangSService.list(yajiangSQueryWrapper);
            for (TrYajiangS yajiangS : trYajiangSs) {
                JSONObject sendObjectson = JSONUtil.createObj();
                sendObjectson.set("chixushijian", yajiangS.getChixushijia());
                sendObjectson.set("endtime", yajiangS.getEndtime());
                sendObjectson.set("fanjiangliang", yajiangS.getFanjianglia());
                sendObjectson.set("fanjiangyali", yajiangS.getFanjiangyal());
                sendObjectson.set("fguid", yajiangS.getFguid());
                sendObjectson.set("hege", yajiangS.getHege());
                sendObjectson.set("holeid", yajiangS.getHoleid());
                sendObjectson.set("jiaobansj", yajiangS.getJiaobansj());
                sendObjectson.set("jinjiangshuliang", yajiangS.getJinjiangshu());
                sendObjectson.set("jinjiangyali", yajiangS.getJinjiangyal());
                sendObjectson.set("kongdao", yajiangS.getKongdao());
                sendObjectson.set("peihebi", yajiangS.getPeihebi());
                sendObjectson.set("shuijiaobi", yajiangS.getShuijiaobi());
                sendObjectson.set("starttime", yajiangS.getStarttime());
                sendObjectson.set("status", yajiangS.getStatus());
                sendObjectson.set("syjid", yajiangS.getSyjid());
                sendObjectson.set("tongguo", yajiangS.getTongguo());
                sendObjectson.set("yajiangmoshi", yajiangS.getYajiangmosh());
                sendObjectson.set("yajiangsj", yajiangS.getYajiangsj());
                sendObjectson.set("yjcs", yajiangS.getYjcs());
                sendObjectson.set("zkyl", yajiangS.getZkyl());
                listsendson.add(sendObjectson);
            }
            sendObject.set("znyjChildList", listsendson);
            System.out.println(sendObject);
            String back = HttpRequest.post("http://39.174.68.27:18086/zeiet/znyjMain/save")
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();

            JSONObject jsonObject1 = new JSONObject(back);
            String message = jsonObject1.get("message").toString();
            if ("操作成功".equals(message)) {
                log.info(String.format("压浆推送阳光工程成功!" + id));
            } else {
                log.info(String.format("压浆推送阳光工程失败!" + id));
            }
            sysConfigService.updateSysConfig(JobUtil.HSY_YJ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
