package org.jeecg.modules.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yj.entity.TrYajiang;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.system.entity.SysLog;
import org.jeecg.modules.system.service.ISysLogService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class yajiangjob implements Job {

    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysLogService sysLogService;
    @Autowired
    private ITrYajiangMService trYajiangMService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<SysLog> sysLogList = sysLogService.getlist();
        for (SysLog sysLog : sysLogList) {
            String requestParam = sysLog.getRequestParam();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(requestParam);
                // 读取第一个对象
                String syjid = jsonNode.get(0).get("syjid").asText();
                Date create_time = sysLog.getCreateTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedTime = sdf.format(create_time);
                LambdaQueryWrapper<TrYajiangM> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TrYajiangM::getSyjid, syjid);
                TrYajiangM one = trYajiangMService.getOne(queryWrapper);
                if (one == null) {
                    continue;
                }
                one.setBaocunshijian(formattedTime);
                trYajiangMService.updateById(one);
                System.out.println("----------------------------------已更新");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
