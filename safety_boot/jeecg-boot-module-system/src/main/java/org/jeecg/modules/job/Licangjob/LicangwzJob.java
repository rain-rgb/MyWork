package org.jeecg.modules.job.Licangjob;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.mapper.WzliaocangMapper;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.mapper.WzycljinchanggbMapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jeecg.common.util.DateUtils.getTimestampten;

/**
 * 获取料仓仓位定时任务(中铁三局西康)
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LicangwzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private WzycljinchanggbMapper wzycljinchanggbMapper;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.WZLC_WLRW);//物资料仓
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到料仓统计信息" + DateUtils.now()));
            return;
        }
        QueryWrapper<Wzliaocang> wrapper = new QueryWrapper<>();
        wrapper.eq("cailiaono",1).or().eq("cailiaono",2).or().eq("cailiaono",3).or().eq("cailiaono",4);

        List<Wzliaocang> list = wzliaocangService.list(wrapper);
        if (list.size() > 0){
            for (Wzliaocang wzliaocang :list){
                if (wzliaocang.getInfraredFence() != null) {//合格，并关联栅栏
                    JSONObject sendObject = new JSONObject();
                    String post = null;
                    if ("2".equals(wzliaocang.getLiaocangStatus()) || "4".equals(wzliaocang.getLiaocangStatus())) {
                        sendObject.set("deviceId", wzliaocang.getInfraredFence());
                        sendObject.set("command", "on");
                        post = HttpRequest.post("http://101.37.166.105:8001/appInfraredVirtualGateway/sendDeviceCommand")
                                .header("Content-Type", "application/json")
                                .body(String.valueOf(sendObject))
                                .execute()
                                .body();
                        JSONObject postObject = new JSONObject(post);
                        Object code = postObject.get("code");
                        if ("0000".equals(String.valueOf(code))) {
                            log.info(String.format("发送设备指令成功" + DateUtils.now()));
                        } else {
                            log.info(String.format("发送设备指令失败" + DateUtils.now()));
                        }
                    }
                }
                Map selectqueryone = wzliaocangService.selectqueryone(wzliaocang.getSysOrgCode());
                if (selectqueryone != null) {
                    wzliaocang.setDepartid(String.valueOf(selectqueryone.get("id")));
                }
                Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
                wzliaocang.setTs(ts);
                wzliaocang.setIfStatus(("2".equals(wzliaocang.getLiaocangStatus()) || "4".equals(wzliaocang.getLiaocangStatus())) ? 1 : 0);
                if (wzliaocang.getJinchangTime() == null || "".equals(wzliaocang.getJinchangTime())) {
                    wzliaocang.setJianyanTime("/");
                }
                wzliaocangService.updateById(wzliaocang);
            }
        }
    }
}
