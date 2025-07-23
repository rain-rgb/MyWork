package org.jeecg.modules.ruicangjob.shiyan;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;
import com.xkcoding.http.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.ruicangjob.RuiCangUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ZHYDJob：
 * @Description TODO
 * @Author zml
 * @Date 2022/05/07 10:52
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class wzycljinchanggbJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private RuiCangUtil ruiCangUtil;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RUICANG_WZJINCHANGGB);//瑞仓高速物资进场过磅数据推送到试验平台
        //如果为空
        if (null == selectsysconfigone) {
            log.info("未获取到瑞仓高速原材料检验批数据推送到实验平台定时任务的配置信息" + DateUtils.now());
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
//        String[] split = shebeilist.split(",");
//        List<String> strsToList1 = Arrays.asList(split);
        List<Wztaizhang> selecthntbhzones = wztaizhangService.selectLists(shebeilist, 0);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info("暂无瑞仓高速原材料检验批未推送数据" + DateUtils.now());
            return;
        }
        int id = 0;
        for (Wztaizhang wztaizhang : selecthntbhzones) {
            JSONObject sendObject = JSONUtil.createObj();
            id = wztaizhang.getId();
            sendObject.set("gbid", id);
            Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wztaizhang.getCailiaono());
            if (wzcailiaonamedict!=null){
                sendObject.set("cailiaono",wzcailiaonamedict.getCailiaoname());
                sendObject.set("guigexinghao",wzcailiaonamedict.getGuigexinghao());
            }
            Map<String,Object> map = shebeiInfoService.selectshebeiones(wztaizhang.getShebeibianhao());
            if (!map.isEmpty()) {
                sendObject.set("orgCode", map.get("departName"));
            }
            sendObject.set("jinchangshijian",wztaizhang.getJinchangshijian());
            sendObject.set("pici",wztaizhang.getPici());
            sendObject.set("shebeibianhao",wztaizhang.getShebeibianhao());
            Wzgongyingshang wzgongyingshang = wzgongyingshangService.selectnameone(wztaizhang.getGongyingshangdanweibianma());
            if (wzgongyingshang!=null){
                sendObject.set("gongyingshangdanweibianma",wzgongyingshang.getGongyingshangname());
            }
            sendObject.set("maozhongt",wztaizhang.getMaozhongt());
            sendObject.set("pizhongt",wztaizhang.getPizhongt());
            Wzliaocang wzliaocang = wzliaocangService.queryselectone(wztaizhang.getLcbianhao());
            if (wzliaocang!=null){
                sendObject.set("lcbianhao",wzliaocang.getName());
            }
            if (Double.parseDouble(wztaizhang.getJingzhongt() ) > 0) {
                sendObject.set("jingzhongt", wztaizhang.getJingzhongt());
            }else {
                sendObject.set("jingzhongt", "0");
            }
            String s = ruiCangUtil.GETToken();
            if (s == null) {
                log.info("瑞苍--物资过磅上传获取token失败" + id + DateUtils.now());
            }
            sendObject.set("jcgkpic", wztaizhang.getBaogaofile());
            String url = "http://121.40.163.88:8084/CATDPS/rest/WzReceivingController/WZGBData";
            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .header("X-AUTH-TOKEN",s)
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            System.out.println(body);
            JSONObject jsonObject1 = new JSONObject(body);
            String codes = (String) jsonObject1.get("success");
            if ("0".equals(codes)) {
                log.info("瑞仓高速物资进场过磅数据推送成功!" + id + "Json数据" + sendObject + DateUtils.now());
//                sysConfigService.updateSysConfig(JobUtil.RUICANG_WZJINCHANGGB, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info("瑞仓高速物资进场过磅数据推送失败!" + id + "Json数据" + sendObject+ DateUtils.now());
            }
        }
    }
}
