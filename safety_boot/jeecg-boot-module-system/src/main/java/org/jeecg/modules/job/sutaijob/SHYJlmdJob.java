package org.jeecg.modules.job.sutaijob;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName SHYJlmdJob：
 * @Description 上海有间龙门吊数据推送
 * @Author 55314
 * @Date 2023/2/24 10:17
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SHYJlmdJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDeviceCraneHistorydataService deviceCraneHistorydataService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "https://api.ilabx.cn/iot-service/interface/dltengyi/gantryCraneTj05";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SHYJ_LMD);//上海有间环境检测数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到上海有间龙门吊的配置信息" + DateUtils.now()));
            return;
        }
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输上海有间龙门吊的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<DeviceCraneHistorydata> deviceCraneHistorydata = deviceCraneHistorydataService.selectListToSHYJ(shebeilist, curid);
        if (null == deviceCraneHistorydata || deviceCraneHistorydata.size() == 0) {
            log.info(String.format("暂无上海有间龙门吊的数据" + DateUtils.now()));
            return;
        }
        int id=0;

        for (DeviceCraneHistorydata deviceCraneHistorydatum : deviceCraneHistorydata) {
            id= deviceCraneHistorydatum.getId();

            JSONObject sendJson = new JSONObject();

            sendJson.set("deviceId",deviceCraneHistorydatum.getDeviceCode());
            sendJson.set("wagonTravel",deviceCraneHistorydatum.getBigCarroute());
            sendJson.set("creatTime",deviceCraneHistorydatum.getCranedate());
            sendJson.set("zggd",deviceCraneHistorydatum.getMainHookheight());
            sendJson.set("ylfggd",deviceCraneHistorydatum.getReservedVicehookheight());
            sendJson.set("zgdz",deviceCraneHistorydatum.getMainHookload());
            sendJson.set("ylfgdz",deviceCraneHistorydatum.getReservedVicehookload());
            sendJson.set("xcxc",deviceCraneHistorydatum.getSmallCarroute());
            sendJson.set("hxcxc2",deviceCraneHistorydatum.getReservedSmallcarroute());
            sendJson.set("windSpeed",deviceCraneHistorydatum.getWindSpeed());
            sendJson.set("ylmnl",deviceCraneHistorydatum.getReservedAnalogquantityone());
            sendJson.set("zgzhzz",deviceCraneHistorydatum.getMainHookstatus());
            sendJson.set("zgfgzhzz",deviceCraneHistorydatum.getReservedVicehookstatus());
            sendJson.set("fsbj",deviceCraneHistorydatum.getWindSpeedalarm());
            sendJson.set("dczxw",deviceCraneHistorydatum.getBigCarleftlimit());
            sendJson.set("xdyxw",deviceCraneHistorydatum.getBigCarrightlimit());
            sendJson.set("xcqxw",deviceCraneHistorydatum.getSmallCarfrontlimit());
            sendJson.set("xchxw",deviceCraneHistorydatum.getSmallCarbacklimit());
            sendJson.set("ylxcqxw2",deviceCraneHistorydatum.getReservedSmallcarfrontlimit());
            sendJson.set("elxchxw2",deviceCraneHistorydatum.getReservedSmallcarbacklimit());
            sendJson.set("dgsxw",deviceCraneHistorydatum.getHookUplimit());
            sendJson.set("ylfdgsxw", deviceCraneHistorydatum.getReservedVicehookuplimit());
            sendJson.set("mxw",deviceCraneHistorydatum.getDoorLimit());
            sendJson.set("kffhzt",deviceCraneHistorydatum.getWindAntiskidstatus());
            sendJson.set("dljtzz",deviceCraneHistorydatum.getWireDrumstatus());
            sendJson.set("xc1zdq1",deviceCraneHistorydatum.getSmallCar1brake1());
            sendJson.set("xc1zdq2",deviceCraneHistorydatum.getSmallCar1brake2());
            sendJson.set("xc2zdq1",deviceCraneHistorydatum.getSmallCar2brake1());
            sendJson.set("xc2zdq2",deviceCraneHistorydatum.getSmallCar2brake2());
            sendJson.set("dcztpc",deviceCraneHistorydatum.getBigCarlegdeviation());
            sendJson.set("cumulativeTime",deviceCraneHistorydatum.getAllTime());
            sendJson.set("xhsycs",deviceCraneHistorydatum.getAllTimes());
            sendJson.set("ztpcbj",deviceCraneHistorydatum.getBigcarAlm());
            sendJson.set("zgez",deviceCraneHistorydatum.getReservedOne());
            sendJson.set("fgez",deviceCraneHistorydatum.getReservedTwo());
            sendJson.set("dcspjdx",deviceCraneHistorydatum.getBigx());
            sendJson.set("dcspjdy",deviceCraneHistorydatum.getBigy());
            sendJson.set("czjdx",deviceCraneHistorydatum.getVerticalx());
            sendJson.set("czjdy",deviceCraneHistorydatum.getVerticaly());
            sendJson.set("lbjd",deviceCraneHistorydatum.getArmangle());
            sendJson.set("lbcd",deviceCraneHistorydatum.getArmlength());
            sendJson.set("zglj",deviceCraneHistorydatum.getMainmoment());
            sendJson.set("fglj",deviceCraneHistorydatum.getVicemoment());
            sendJson.set("jgcj1",deviceCraneHistorydatum.getSlr1());
            sendJson.set("jgcj2",deviceCraneHistorydatum.getSlr2());
            sendJson.set("state",deviceCraneHistorydatum.getStatus());

            //推送
            String result = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(sendJson))
                    .execute()
                    .body();

            sysConfigService.updateSysConfig(JobUtil.SHYJ_LMD, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
