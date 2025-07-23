package org.jeecg.modules.job.ydJob.dongjiao;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName lqysczf：
 * @Description TODO
 * @Author 55314
 * @Date 2023/8/15 16:40
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class lqysczf implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.Z_DB);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东过磅数据定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东过磅数据的设备" + DateUtils.now()));
            return;
        }
        int id = 0;
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Wzycljinchanggb> wzycljinchanggbList = wzycljinchanggbService.selectydyclList(curid, shebeilist);
        if (null == wzycljinchanggbList || wzycljinchanggbList.size() == 0) {
            log.info(String.format("暂无义东过磅数据未推送数据" + DateUtils.now()));
            return;
        }
        for (Wzycljinchanggb wzycljinchanggb : wzycljinchanggbList) {

            String s = JSONUtil.toJsonStr(wzycljinchanggb);
            String back = HttpRequest.post("http://z.traiot.cn/jeecg-boot/yclsl/wzycljinchanggb/addOpen")
                    .header("Content-Type", "application/json")
                    .body(s)
                    .execute()
                    .body();

            if (back.contains("添加成功")){
                log.info("推送成功！");
            }else {
                log.info("推送失败！");
            }

            sysConfigService.updateSysConfig(JobUtil.Z_DB, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
