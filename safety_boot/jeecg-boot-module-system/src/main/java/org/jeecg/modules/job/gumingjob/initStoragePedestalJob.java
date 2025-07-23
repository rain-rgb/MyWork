package org.jeecg.modules.job.gumingjob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.cunliangprocedureconfig.entity.CunliangProcedureConfig;
import com.trtm.iot.cunliangprocedureconfig.service.ICunliangProcedureConfigService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.lab.push.job.BaseHttpRequest;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * \* @author: zml
 * \* Date: 2022-08-17
 * \* Time: 13:08
 * \* Description: 初始化存梁台座信息
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class initStoragePedestalJob extends BaseHttpRequest implements Job {

    // itype
    private final static String I_TYPE = "initStoragePedestal";

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ICunliangProcedureConfigService cunliangProcedureConfigService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.PM_CSHCLTZ);//初始化存梁台座信息
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到品茗数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }

        QueryWrapper<SysDepart> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("org_type",3);
        queryWrapper1.likeRight("org_code","A05A10A01");
        SysDepart one = sysDepartService.getOne(queryWrapper1);

        QueryWrapper<CunliangProcedureConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("statisc",0);
        queryWrapper.gt("id",selectsysconfigone.getCurid());
        queryWrapper.eq("create_by","徐智玲");
        queryWrapper.groupBy("liangzuoname");
        List<CunliangProcedureConfig> list1 = cunliangProcedureConfigService.list(queryWrapper);
        int id = 0;
        if (list1.size() > 0){
            for (CunliangProcedureConfig l :list1){
                List<Map> list = new ArrayList<Map>();
                Map<String, Object> map = new HashMap<>();
                map.put("plantId", one.getId());
                map.put("pedestalId", l.getId());
                map.put("pedestalNo", l.getLiangzuoname());
                map.put("pedestalName", l.getLiangzuoname());
                map.put("layers", Integer.parseInt(l.getCengshu()));
                map.put("row", Integer.parseInt(l.getLianghang()));
                map.put("column", Integer.parseInt(l.getLianglie()));
                list.add(map);
                System.out.println(list);
                String result = requestDataProcessing(list, I_TYPE, 1);
                System.out.println("结果：" + result);
                if (result.contains("true")) {
                    QueryWrapper<CunliangProcedureConfig> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("liangzuoname",l.getLiangzuoname());
                    List<CunliangProcedureConfig> list2 = cunliangProcedureConfigService.list(queryWrapper2);
                    for (CunliangProcedureConfig l1 :list2){
                        l1.setStatisc(1);
                        cunliangProcedureConfigService.updateById(l1);
                    }
                    log.info("品茗初始化存梁台座信息推送成功");
                } else {
                    QueryWrapper<CunliangProcedureConfig> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("liangzuoname",l.getLiangzuoname());
                    List<CunliangProcedureConfig> list2 = cunliangProcedureConfigService.list(queryWrapper2);
                    for (CunliangProcedureConfig l1 :list2){
                        l1.setStatisc(1);
                        cunliangProcedureConfigService.updateById(l1);
                    }
                    log.info("品茗初始化存梁台座信息推送失败 原因："+ result);
                }
                id = l.getId();
            }
        }else {
            log.info("暂无存梁台座");
        }
        sysConfigService.updateSysConfig(JobUtil.PM_CSHCLTZ, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}