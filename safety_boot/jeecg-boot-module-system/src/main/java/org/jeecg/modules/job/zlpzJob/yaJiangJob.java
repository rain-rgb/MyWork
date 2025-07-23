package org.jeecg.modules.job.zlpzJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.trtm.iot.zhanglam.service.IZhanglaYajiangOverHandlerService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zlpz.entity.Zlpz;
import com.trtm.iot.zlpz.service.IZlpzService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName yaJiangJob：
 * @Description 浙路品质压浆
 * @Author 55314
 * @Date 2022/12/8 14:13
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
    private ITrYajiangSService yajiangSService;
    @Autowired
    private IZlpzService zlpzService;
    @Autowired
    private IZhanglaYajiangOverHandlerService overHandlerService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZLPZ_YJ);//浙路品质压浆推送
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
        List<TrYajiangM> trYajiangMS = yajiangMService.selectListzlpz(shebeilist);
        if (null == trYajiangMS || trYajiangMS.size() == 0) {
            log.info(String.format("暂无压浆未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (TrYajiangM yajiangM : trYajiangMS){
            TrYajiangM trYajiangM = new TrYajiangM();
            id = yajiangM.getId();
            List sendList = new ArrayList();
            JSONObject sendDate = new JSONObject();
            QueryWrapper<Zlpz> queryWrapperZlpz = new QueryWrapper<>();
            queryWrapperZlpz.eq("shebeino", yajiangM.getYjsbbh());
            Zlpz one = zlpzService.getOne(queryWrapperZlpz);
            String shebeiid = one.getShebeiid();
            String project = one.getProject();

            sendDate.set("id", project + "_" + id);
            sendDate.set("equipId", shebeiid);
            sendDate.set("part", yajiangM.getLianghao());

            List<TrYajiangS> list = yajiangSService.lambdaQuery()
                    .eq(TrYajiangS::getSyjid, yajiangM.getSyjid())
                    .and(i -> i.eq(TrYajiangS::getHege, "0").or().eq(TrYajiangS::getHege, "不合格"))
                    .list();

            sendDate.set("yjMode", list.get(0).getYajiangmosh());
            sendDate.set("holeid", list.get(0).getHoleid());
            sendDate.set("sjb", yajiangM.getShuijiaobi());
            sendDate.set("stirTime", list.get(0).getJiaobansj());
            sendDate.set("starttime", list.get(0).getStarttime());
            sendDate.set("endtime", list.get(0).getEndtime());
            sendDate.set("jjValue", list.get(0).getJinjiangshu());
            sendDate.set("jjPress", list.get(0).getJinjiangyal());
            int byTime = (int) Double.parseDouble(list.get(0).getChixushijia());

            sendDate.set("byTime", byTime);
            QueryWrapper<ZhanglaYajiangOverHandler> overHandlerQueryWrapper = new QueryWrapper<>();
            overHandlerQueryWrapper.eq("baseId",yajiangM.getSyjid());
            ZhanglaYajiangOverHandler one2 = overHandlerService.getOne(overHandlerQueryWrapper);
            int alarmStatus = 0;
            if (one2 != null){
                alarmStatus = 1;
                sendDate.set("opinion", one2.getHandleWay());
                sendDate.set("closeTime", sdf.format(one2.getHandleTime()));
                sendDate.set("closer", one2.getHandlePerson());
                sendDate.set("attachment", one2.getFilePath2());
            }
            sendDate.set("alarmStatus", alarmStatus);
            sendDate.set("allowMinPressure", 0.5);
            sendDate.set("allowMaxPressure", 0.7);
            sendDate.set("allowMinDuration", 180);
            sendDate.set("allowMaxDuration", 300);

            sendList.add(sendDate);

            JSONObject sendJsonObject = new JSONObject();
            sendJsonObject.set("serviceName","ZLPZ_ZX_YJ");
            sendJsonObject.set("token","93cd2c6567594107a16b51a65bcd5a37");
            sendJsonObject.set("updateNull","true");
            sendJsonObject.set("param",sendList);

            String url = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/push";
            String back = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(sendJsonObject.toString())
                    .timeout(20000)
                    .execute().body();

            com.alibaba.fastjson.JSONObject backJson = JSON.parseObject(back);
            int result = backJson.getIntValue("result");
            if (result == 0) {
                yajiangM.setIszlpz(alarmStatus == 0 ? 3 : 1);
                log.info(String.format("浙路品质压浆推送成功!" + id));
            } else {
                yajiangM.setIszlpz(2);
                log.info(String.format("浙路品质压浆推送失败!" + id));
            }
            yajiangMService.updateById(yajiangM);
            pushandreturnService.saveData(id,String.valueOf(sendDate),selectsysconfigone.getRemark(),back);
        }
    }
}
