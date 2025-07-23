package org.jeecg.modules.job.gumingjob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.lab.push.job.BaseHttpRequest;
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
 * \* Description: 初始化梁类型信息（相同的梁型的id需要保持⼀致）
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class initComponentJob extends BaseHttpRequest implements Job {

    // itype
    private final static String I_TYPE = "initComponent";

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IZhiliangrenwudanService zhiliangrenwudanService;
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.PM_CSHL);//初始化梁信息
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
        int sum = 0;
        String a = null;
        if (list1.size() > 0){
            for (SysDepartproject l :list1){
                // 左右幅
                QueryWrapper<SysDepartproject> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("parent_id",l.getId());
                List<SysDepartproject> list2 = sysDepartprojectService.list(queryWrapper2);
                if (list2.size() > 0){
                    for (SysDepartproject l1 :list2) {
                        // 跨
                        QueryWrapper<SysDepartproject> queryWrapper3 = new QueryWrapper<>();
                        queryWrapper3.eq("parent_id",l1.getId());
                        List<SysDepartproject> list3 = sysDepartprojectService.list(queryWrapper3);
                        if (list3.size() > 0){
                            for (SysDepartproject l2 :list3) {
                                int seq = 1;
                                int seq1 = 1;
                                QueryWrapper<SysDepartproject> queryWrapper4 = new QueryWrapper<>();
                                queryWrapper4.eq("parent_id",l2.getId());
                                List<SysDepartproject> list4 = sysDepartprojectService.list(queryWrapper4);
                                if(list4.size() > 0){
                                    for(SysDepartproject l3 :list4){
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("bridgeId", l.getId());
                                        map.put("bridgeCrossId", l2.getId());
                                        if (l1.getDepartName().contains("左")){
                                            map.put("orientation", 1);
                                            map.put("sort", seq);
                                            seq++;
                                        }else {
                                            map.put("orientation", 2);
                                            map.put("sort", seq1);
                                            seq1++;
                                        }
                                        a = l.getDepartName() + " | " + l1.getDepartName() + " | " + l2.getDepartName() + " | " + l3.getDepartName();
                                        Zhiliangrenwudan one = null;
                                        try {
                                            QueryWrapper<Zhiliangrenwudan> queryWrapper1 = new QueryWrapper<>();
                                            queryWrapper1.likeLeft("ConsPos",a);
                                            one = zhiliangrenwudanService.getOne(queryWrapper1);
                                        } catch (Exception e) {
                                            log.info(a + "该施工部位重复！！！");
                                            e.printStackTrace();
                                        }
                                        if (one == null){
                                            continue;
                                        }
                                        map.put("componentId", one.getCode());
                                        map.put("componentClassifyId", "12580");
                                        map.put("planBridgingTime", one.getProductiontime1());
                                        map.put("planManufactureTime", one.getProductiontime());
                                        map.put("componentName", one.getConspos());
                                        map.put("componentNo", one.getId());
                                        List<Map> list = new ArrayList<Map>();
                                        list.add(map);
                                        System.out.println(list);
                                        String result = requestDataProcessing(list, I_TYPE, 1);
                                        System.out.println("结果：" + result);
                                    }
                                }
                            }
                        }
                    }
                }
                // id = Integer.parseInt(l.getId());
            }
        }else {
            log.info("暂无存梁台座");
        }
        sysConfigService.updateSysConfig(JobUtil.PM_CSHL, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}