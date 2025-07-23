package org.jeecg.modules.job.lqbhzJob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @ClassName updatesbbhJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/10/13 14:36
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class updatesbbhJob implements Job {
    @Autowired
    private IBhzLqBasesService lqBasesService;
    @Autowired
    private IBhzLqCailiaoService lqCailiaoService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        List<BhzLqBases> bhzLqBases = lqBasesService.selectUpdateList("jhjdlqbh01_01");
        if (null == bhzLqBases || bhzLqBases.size() == 0) {
            log.info(String.format("暂无金华基地沥青拌合机需修改的数据！" + DateUtils.now()));
            return;
        }
        for (BhzLqBases bhzLqBase : bhzLqBases) {
            bhzLqBase.setShebeibianhao("dy351lq01");
            lqBasesService.saveOrUpdate(bhzLqBase);

            QueryWrapper<BhzLqCailiao> lqCailiaoQueryWrapper = new QueryWrapper<>();
            lqCailiaoQueryWrapper.eq("base_guid",bhzLqBase.getGuid());
            List<BhzLqCailiao> list = lqCailiaoService.list(lqCailiaoQueryWrapper);
            for (BhzLqCailiao bhzLqCailiao : list) {
                bhzLqCailiao.setShebeibianhao("dy351lq01");
                lqCailiaoService.saveOrUpdate(bhzLqCailiao);
            }

            log.info(String.format("金华基地沥青拌合机编号已改变！" + DateUtils.now()));
        }
    }
}
