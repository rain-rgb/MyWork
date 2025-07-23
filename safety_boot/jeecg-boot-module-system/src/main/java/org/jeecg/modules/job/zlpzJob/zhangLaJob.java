package org.jeecg.modules.job.zlpzJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zhangla.service.ITrZhanglaService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglam.service.IZhanglaYajiangOverHandlerService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import com.trtm.iot.zlpz.entity.Zlpz;
import com.trtm.iot.zlpz.service.IZlpzService;
import com.xkcoding.http.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName zhangLaJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/12/8 11:03
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zhangLaJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;
    @Autowired
    private ITrZhanglaSService zhanglaSService;
    @Autowired
    private IZlpzService zlpzService;
    @Autowired
    private IZhanglaYajiangOverHandlerService overHandlerService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZLPZ_ZL);//浙路品质张拉推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到张拉定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输张拉的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrZhanglaXxb> zhanglaXxbs = zhanglaXxbService.selectListzlpz(shebeilist);
        if (null == zhanglaXxbs || zhanglaXxbs.size() == 0) {
            log.info(String.format("暂无张拉未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (TrZhanglaXxb trZhanglaXxb : zhanglaXxbs) {
            List sendList = new ArrayList();
            JSONObject sendDate = new JSONObject();
            id = trZhanglaXxb.getId();
            QueryWrapper<Zlpz> queryWrapperZlpz = new QueryWrapper<>();
            queryWrapperZlpz.eq("shebeino", trZhanglaXxb.getShebeibianhao());
            Zlpz one = zlpzService.getOne(queryWrapperZlpz);
            String shebeiid = one.getShebeiid();
            String project = one.getProject();

            sendDate.set("id", project + "_" + id);
            sendDate.set("equipId", shebeiid);
            sendDate.set("part", StringUtil.isNotEmpty(trZhanglaXxb.getGjbh()) ? trZhanglaXxb.getGjbh() : trZhanglaXxb.getGjmc());


            List<TrZhanglaM> list = zhanglaMService.lambdaQuery()
                    .eq(TrZhanglaM::getSyjid, trZhanglaXxb.getSyjid())
                    .and(i -> i.eq(TrZhanglaM::getHege, "0").or().eq(TrZhanglaM::getHege, "不合格"))
                    .list();


            sendDate.set("holeid", list.get(0).getGsbh());
            sendDate.set("zlsj", list.get(0).getZlsj());
            String sjzll = list.get(0).getSjzll();
            sendDate.set("sjzll", sjzll);

            QueryWrapper<TrZhanglaS> zhanglaSQueryWrapper = new QueryWrapper<>();
            zhanglaSQueryWrapper.select("MAX(cast(jdzll as DECIMAL(10,2))) AS jdzll");
            zhanglaSQueryWrapper.eq("syjid",trZhanglaXxb.getSyjid());
            TrZhanglaS one1 = zhanglaSService.getOne(zhanglaSQueryWrapper);
            String jdzll = "0";
            if (null != one1){
                jdzll = one1.getJdzll();
            }
            sendDate.set("jdzll", jdzll);
            sendDate.set("zscl", list.get(0).getZscl());
            sendDate.set("llscl", list.get(0).getLlscl());
            sendDate.set("sclper", list.get(0).getSclper());

            QueryWrapper<ZhanglaYajiangOverHandler> overHandlerQueryWrapper = new QueryWrapper<>();
            overHandlerQueryWrapper.eq("baseId",trZhanglaXxb.getSyjid());
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

            double sjzll_double = Double.parseDouble(sjzll);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            sendDate.set("allowMinTension", decimalFormat.format(sjzll_double * 0.98));
            sendDate.set("allowMaxTension", decimalFormat.format(sjzll_double * 1.02));
            sendDate.set("allowMinElongation", 6.0);
            sendDate.set("allowMaxElongation", -6.0);


            sendList.add(sendDate);

            JSONObject sendJsonObject = new JSONObject();
            sendJsonObject.set("serviceName","ZLPZ_ZX_ZL");
            sendJsonObject.set("token","93cd2c6567594107a16b51a65bcd5a37");
            sendJsonObject.set("updateNull","true");
            sendJsonObject.set("param",sendList);

            String url = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/push";
            System.out.println(sendJsonObject);
            String back = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(sendJsonObject.toString())
                    .timeout(20000)
                    .execute().body();


            com.alibaba.fastjson.JSONObject backJson = JSON.parseObject(back);
            int result = backJson.getIntValue("result");
            if (result == 0) {
                log.info("张拉推送浙路品质成功!" + id);
                trZhanglaXxb.setIszlpz(alarmStatus == 0 ? 3 : 1);
            } else {
                log.info("张拉推送浙路品质失败!" + id);
                trZhanglaXxb.setIszlpz(2);
            }
            trZhanglaXxb.setId(id);
            zhanglaXxbService.updateById(trZhanglaXxb);
            pushandreturnService.saveData(id,String.valueOf(sendDate),selectsysconfigone.getRemark(),back);
        }
    }
}
