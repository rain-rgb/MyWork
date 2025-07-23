package org.jeecg.modules.job.gumingjob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.lab.push.job.BaseHttpRequest;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDepartprojectService;
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
public class initBridgeCrossJob extends BaseHttpRequest implements Job {

    // itype
    private final static String I_TYPE = "initBridgeCross";

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.PM_CSHQK);//初始化桥跨信息
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到品茗数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }

        QueryWrapper<SysDepartproject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("org_type",3);
        queryWrapper.like("depart_name","桥");
        queryWrapper.likeRight("org_code","A13A02");
        List<SysDepartproject> list1 = sysDepartprojectService.list(queryWrapper);
        int id = 0;
        if (list1.size() > 0){
            for (SysDepartproject l :list1){
                int seq = 1;
                // 左右幅
                QueryWrapper<SysDepartproject> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("parent_id",l.getId());
                List<SysDepartproject> list2 = sysDepartprojectService.list(queryWrapper2);

                if (list2.size() > 0){
                    for (SysDepartproject l1 :list2) {
                        // 查左右幅下的跨
                        QueryWrapper<SysDepartproject> queryWrapper3 = new QueryWrapper<>();
                        queryWrapper3.eq("parent_id",l1.getId());
                        List<SysDepartproject> list3 = sysDepartprojectService.list(queryWrapper3);
                        if (list3.size() > 0){
                            for (SysDepartproject l2 :list3) {
                                List<Map> list = new ArrayList<Map>();
                                Map<String, Object> map = new HashMap<>();
                                map.put("bridgeId", l.getId());
                                map.put("acrossId", l2.getId());
                                map.put("acrossName", l2.getDepartName());
                                map.put("seq", seq);
                                seq++;
                                list.add(map);
                                System.out.println(list);
                                String result = requestDataProcessing(list, I_TYPE, 1);
                                System.out.println("结果：" + result);
                            }
                        }
                    }
                }
                // id = Integer.parseInt(l.getId());
            }
        }else {
            log.info("暂无存梁台座");
        }
        sysConfigService.updateSysConfig(JobUtil.PM_CSHQK, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}