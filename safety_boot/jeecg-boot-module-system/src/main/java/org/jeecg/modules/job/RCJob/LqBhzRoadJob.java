package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
import com.trtm.iot.lqbhz.vo.BhzLqBasesPage;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LqBhzRoadJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;
    @Autowired
    private IBhzLqCailiaoService bhzLqCailiaoService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LQBHZ_TUISONG_ROAD);//拌合站沥青数据推送甬台平台
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到沥青拌合站推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("47平台没有配置要传输的沥青拌合站推送的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzLqBases> list = bhzLqBasesService.selectLQBHZRoad(curid, shebeilist);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无沥青拌合站未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (BhzLqBases bhzLqBases : list) {
            id = bhzLqBases.getId();
            JSONObject sendObject = JSONUtil.createObj();
            sendObject.set("work_time", bhzLqBases.getChuliaoshijian());
            sendObject.set("formula", bhzLqBases.getFormulaNo());
            sendObject.set("temperature", bhzLqBases.getChuliaowd());
            List<BhzLqCailiao> bhzLqCailiaos = bhzLqCailiaoService.selectByGuid(bhzLqBases.getGuid());
            sendObject.set("silo_1", "");
            sendObject.set("silo_2", "");
            sendObject.set("silo_3", "");
            sendObject.set("silo_4", "");
            sendObject.set("silo_5", "");
            sendObject.set("silo_6", "");
            sendObject.set("asphalt", "");
            sendObject.set("powder_2", "");
            sendObject.set("powder_1", "");
            for (BhzLqCailiao bhzLqCailiao : bhzLqCailiaos) {
                switch (bhzLqCailiao.getCailiaoming()) {
                    case "骨料1"://骨料1
                        sendObject.set("silo_1", bhzLqCailiao.getShijiyongliang());
                        break;
                    case "骨料2"://骨料2
                        sendObject.set("silo_2", bhzLqCailiao.getShijiyongliang());
                        break;
                    case "骨料3"://骨料3
                        sendObject.set("silo_3", bhzLqCailiao.getShijiyongliang());
                        break;
                    case "骨料4"://骨料4
                        sendObject.set("silo_4", bhzLqCailiao.getShijiyongliang());
                        break;
                    case "骨料5"://骨料5
                        sendObject.set("silo_5", bhzLqCailiao.getShijiyongliang());
                        break;
                    case "骨料6"://骨料6
                        sendObject.set("silo_6", bhzLqCailiao.getShijiyongliang());
                        break;
                    case "沥青"://沥青
                        sendObject.set("asphalt", bhzLqCailiao.getShijiyongliang());
                        break;
                    case "粉料1"://粉料1
                        sendObject.set("powder_1", bhzLqCailiao.getShijiyongliang());
                        break;
                    case "粉料2"://粉料2
                        sendObject.set("powder_2", bhzLqCailiao.getShijiyongliang());
                        break;
                    default:
                        break;
                }
            }
            String param = "{\n" +
                    "   \"appid\":\"hygs\",\n" +
                    "   \"secret\":\"a1d2c743a8321a46\"\n" +
                    "}";
            String back = HttpRequest.post("https://road.iroadw.com/oapi/get_token")
                    .body(param)
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            String token = null;
            if ((Integer) jsonObject2.get("code") == 1) {
                token = (String) jsonObject2.get("token");
            }
            String returnInfo = HttpRequest.post("https://road.iroadw.com/oapi/save_station_data?token=" + token)
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            JSONObject jsonObject3 = new JSONObject(returnInfo);
            Integer code = (Integer) jsonObject3.get("code");
            if (code == 0) {
                log.info(String.format("沥青拌合站数据推送成功" + DateUtils.now()));
                sysConfigService.updateSysConfig(JobUtil.LQBHZ_TUISONG_ROAD, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("沥青拌合站数据推送失败" + DateUtils.now()));
            }

        }
    }
}
