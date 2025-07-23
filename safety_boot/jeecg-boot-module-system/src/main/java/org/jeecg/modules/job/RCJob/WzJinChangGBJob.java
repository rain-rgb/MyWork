package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName WzJinChangGBJob：
 * @Description TODO
 * @Author zml
 * @Date 2022/05/11 10:27
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class WzJinChangGBJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_WZJINCHANGGB);//瑞仓内网地磅数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到瑞仓地磅数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info("没有配置要传输瑞仓地磅的设备" + DateUtils.now());
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1 = Arrays.asList(split);
        List<Wzycljinchanggb> selecthntbhzones = wzycljinchanggbService.selectLists(strsToList1, curid);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info("暂无瑞仓地磅未推送数据" + DateUtils.now());
            return;
        }
        int id = 0;
        for (Wzycljinchanggb wzycljinchanggb : selecthntbhzones) {
            JSONObject sendObject = JSONUtil.createObj();
            id = wzycljinchanggb.getId();
            sendObject.set("wzycljinchanggb",wzycljinchanggb);
            Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzycljinchanggb.getCailiaono());
            if (wzcailiaonamedict!=null){
                sendObject.set("wzcailiaonamedict",wzcailiaonamedict);
            }
            Wzgongyingshang selectnameone = wzgongyingshangService.selectnameone(wzycljinchanggb.getGongyingshangdanweibianma());
            if (selectnameone!=null){
                sendObject.set("wzgongyingshang",selectnameone);
            }
            Wzliaocang wzliaocang = wzliaocangService.queryselectone(wzycljinchanggb.getLcbianhao());
            if (wzliaocang!=null){
                sendObject.set("wzliaocang",wzliaocang);
            }
            String url = "https://zgj20.cncico.com/wlwpt/yclsl/wzycljinchanggb/addOpens";
            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            System.out.println(body);
            JSONObject jsonObject1 = new JSONObject(body);
            Integer codes = (Integer) jsonObject1.get("code");
            if (codes == 200) {
                sysConfigService.updateSysConfig(JobUtil.RC_WZJINCHANGGB, id);//根据传过来的唯一值来修改当前判断到的数据id
                log.info(String.format("瑞仓内网地磅数据推送成功!" + id + "Json数据" + sendObject));
            } else {
                log.info(String.format("瑞仓内网地磅数据推送失败!" + id + "Json数据" + sendObject));
            }
        }
    }
}
