package org.jeecg.modules.job.gumingjob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.cunliangprocedureconfig.entity.CunliangProcedureConfig;
import com.trtm.iot.cunliangprocedureconfig.service.ICunliangProcedureConfigService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zhiliangtaizuocfg.entity.ZhiliangTaizuoCfg;
import com.trtm.iot.zhiliangtaizuocfg.service.IZhiliangTaizuoCfgService;
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
 * \* Description: 初始化制梁台座信息
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class initManufacturePedestalJob extends BaseHttpRequest implements Job {

    // itype
    private final static String I_TYPE = "initManufacturePedestal";

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IZhiliangTaizuoCfgService zhiliangTaizuoCfgService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.PM_CSHZLTZ);//初始化制梁台座信息
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到品茗数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }

        QueryWrapper<SysDepart> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("org_type",3);
        queryWrapper1.likeRight("org_code","A05A10A01");
        SysDepart one = sysDepartService.getOne(queryWrapper1);

        QueryWrapper<ZhiliangTaizuoCfg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("statisc",0);
        queryWrapper.gt("id",selectsysconfigone.getCurid());
        queryWrapper.likeRight("sys_org_code","A05A10");
        List<ZhiliangTaizuoCfg> list1 = zhiliangTaizuoCfgService.list(queryWrapper);
        int id = 0;
        if (list1.size() > 0){
            for (ZhiliangTaizuoCfg l :list1){
                List<Map> list = new ArrayList<Map>();
                Map<String, Object> map = new HashMap<>();
                map.put("plantId", one.getId());
                map.put("pedestalId", l.getId());
                map.put("pedestalNo", l.getTaizuono());
                map.put("pedestalName", l.getTaizuoname());
                map.put("row", 0);
                map.put("column", 0);
                list.add(map);
                System.out.println(list);
                String result = requestDataProcessing(list, I_TYPE, 1);
                System.out.println("结果：" + result);
                if (result.contains("true")) {
                    l.setStatisc(1);
                    zhiliangTaizuoCfgService.updateById(l);
                    log.info("品茗初始化存梁台座信息推送成功");
                } else {
                    l.setStatisc(2);
                    zhiliangTaizuoCfgService.updateById(l);
                    log.info("品茗初始化存梁台座信息推送失败 原因："+ result);
                }
                id = l.getId();
            }
        }else {
            log.info("暂无存梁台座");
        }
        sysConfigService.updateSysConfig(JobUtil.PM_CSHZLTZ, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}