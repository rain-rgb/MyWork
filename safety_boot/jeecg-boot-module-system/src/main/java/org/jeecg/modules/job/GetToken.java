package org.jeecg.modules.job;


import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.system.service.IShigongphbService;
import com.trtm.iot.tokenrecode.entity.Tokenrecode;
import com.trtm.iot.tokenrecode.service.ITokenrecodeService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName StationJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/5/27 11:16
 * @Version 1.0
 **/
@Slf4j
public class GetToken implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITokenrecodeService tokenrecodeService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_TOKEN);
        //如果他等于空

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = "";
        try {
            format = sdf.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject JsonObj = JSONUtil.createObj();
        List<Tokenrecode> list = tokenrecodeService.cxqb();
        for(Tokenrecode one :list){
            JsonObj.put("username", one.getUsername());
            String url = selectsysconfigone.getExtra();// 获取请求地址
            String body = HttpRequest.post(url)
                    .body(String.valueOf(JsonObj))
                    .timeout(20000)
                    .execute().body();
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(body);
            cn.hutool.json.JSONObject jsarray = jsonObject.getJSONObject("result");
            String token = String.valueOf(jsarray.get("token"));
            one.setRedisvalue(token);
            tokenrecodeService.updateById(one);
        }

        sysConfigService.updateSysConfigToken(999,format);


    }
}
