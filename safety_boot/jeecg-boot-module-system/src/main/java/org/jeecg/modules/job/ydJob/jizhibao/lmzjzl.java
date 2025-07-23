package org.jeecg.modules.job.ydJob.jizhibao;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_constructionresults.entity.HcConstructionresultsTPNY;
import com.trtm.iot.lmzjzl.entity.Lmzjzl;
import com.trtm.iot.lmzjzl.service.ILmzjzlService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName lmzjzl：
 * @Description TODO
 * @Author 55314
 * @Date 2023/8/23 11:30
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class lmzjzl implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ILmzjzlService lmzjzlService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        String back = HttpRequest.post("http://z.traiot.cn/jeecg-boot/hc_pavement_stationdata/hcPavementStationdata/getDay")
                .execute()
                .body();

        com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(back);
        JSONArray resultArray = json.getJSONArray("result");
        for (int i = 0; i < resultArray.size(); i++) {
            com.alibaba.fastjson.JSONObject resultObject = resultArray.getJSONObject(i);
            String date = resultObject.getString("date");
            add(date);
        }
    }

    public void add(String date) {

        QueryWrapper<Lmzjzl> lmzjzlQueryWrapper = new QueryWrapper<>();
        lmzjzlQueryWrapper.eq("date", date);
        List<Lmzjzl> list = lmzjzlService.list(lmzjzlQueryWrapper);
        if (list.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date currentDate = new Date();
                String format = sdf.format(currentDate);
                Date parse = sdf.parse(format);
                Date inputDate = sdf.parse(date);

                if (inputDate.before(parse)) {
                    String back = HttpRequest.post("http://z.traiot.cn//jeecg-boot/hc_pavement_stationdata/hcPavementStationdata/getData1?date=" + date)
                            .execute()
                            .body();
                    com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(back);
                    JSONArray resultArray = json.getJSONArray("result");
                    for (int i = 0; i < resultArray.size(); i++) {
                        com.alibaba.fastjson.JSONObject object = resultArray.getJSONObject(i);
                        Lmzjzl lmzjzl = com.alibaba.fastjson.JSONObject.toJavaObject(object, Lmzjzl.class);
                        lmzjzl.setDate(inputDate);
                        lmzjzlService.save(lmzjzl);
                        log.info("路面质检资料保存成功！date：" + date);
                    }
                }
            } catch (Exception e) {
                // 无法解析日期格式时的异常处理
                e.printStackTrace();
            }
        }
    }
}
