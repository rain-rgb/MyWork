package org.jeecg.modules.job.gumingjob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhilianggongxu.service.IZhiliangGongxuService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import com.trtm.iot.zhiliangtaizuocfg.service.IZhiliangTaizuoCfgService;
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

import java.text.SimpleDateFormat;
import java.util.*;

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
public class componentManufactureJob extends BaseHttpRequest implements Job {

    // itype
    private final static String I_TYPE = "componentManufacture";

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IZhiliangGongxuService zhiliangGongxuService;
    @Autowired
    private IZhiliangrenwudanService zhiliangrenwudanService;
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.PM_CSHZL);//制梁
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到品茗数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }

        QueryWrapper<SysDepart> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("org_type",3);
        queryWrapper1.likeRight("org_code","A05A10A01");
        SysDepart one = sysDepartService.getOne(queryWrapper1);

        QueryWrapper<SysDepartproject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("org_type",3);
        queryWrapper.like("depart_name","桥");
        queryWrapper.likeRight("org_code","A13A02");
        List<SysDepartproject> list1 = sysDepartprojectService.list(queryWrapper);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        int id = 0;
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
                                // 施工部位
                                QueryWrapper<SysDepartproject> queryWrapper4 = new QueryWrapper<>();
                                queryWrapper4.eq("parent_id",l2.getId());
                                List<SysDepartproject> list4 = sysDepartprojectService.list(queryWrapper4);
                                if(list4.size() > 0){
                                    for(SysDepartproject l3 :list4){
                                        a = l.getDepartName() + " | " + l1.getDepartName() + " | " + l2.getDepartName() + " | " + l3.getDepartName();
                                        Zhiliangrenwudan one1 = null;
                                        try {
                                            QueryWrapper<Zhiliangrenwudan> queryWrapper5 = new QueryWrapper<>();
                                            queryWrapper5.likeLeft("ConsPos",a);
                                            one1 = zhiliangrenwudanService.getOne(queryWrapper5);
                                        } catch (Exception e) {
                                            log.info(a + "该施工部位重复！！！");
                                            e.printStackTrace();
                                        }
                                        if (one1 == null){
                                            continue;
                                        } else {
                                            List<ZhiliangGongxu> selectgongxu = zhiliangGongxuService.selectgongxuPinmin(one1.getUuid());
                                            if (selectgongxu.size() > 0){
                                                for (ZhiliangGongxu z :selectgongxu){
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("plantId", one.getId());
                                                    map.put("componentId", one1.getCode());
                                                    if (one1.getTaizuono() != null){
                                                        map.put("manufactureBaseId", one1.getTaizuono());//制梁台座id，制梁时必传
                                                    }

                                                    if (z.getStatus() != 0){
                                                        map.put("layer", z.getLiangceng());//存量在台座上的位置,状态是存量时必传
                                                        map.put("storageBaseId", z.getTaizuoname());//存梁台座id，存梁时必传
                                                        if(z.getFinishtime() == null){
                                                            map.put("time", simpleDateFormat.format(z.getCreateTime()));
                                                        }else {
                                                            map.put("time",  simpleDateFormat.format(z.getFinishtime()));//时间
                                                        }
//                                                        System.out.println(simpleDateFormat.format(z.getFinishtime()));
                                                        if (z.getResponsible() != null){
                                                            map.put("operationName", z.getResponsible());//操作⼈
                                                        }else {
                                                            map.put("operationName", z.getCreateBy());//操作⼈
                                                        }
                                                        if (z.getXuhao() == 6){
                                                            map.put("status", 7);//状态 1钢筋绑扎 2混凝⼟浇筑 3收⾯静置 4养⽣ 5张拉 6压浆 7封端 8存梁 9取梁 10提梁
                                                        } else if (z.getXuhao() == 10){
                                                            map.put("status", 6);//状态 1钢筋绑扎 2混凝⼟浇筑 3收⾯静置 4养⽣ 5张拉 6压浆 7封端 8存梁 9取梁 10提梁
                                                        } else if (z.getXuhao() == 8){
                                                            map.put("status", 10);
                                                        } else if (z.getXuhao() == 7){
                                                            if (z.getStatus() == 1){
                                                                map.put("status", 8);
                                                                z.setPinmin(1);
                                                                zhiliangGongxuService.updateById(z);
                                                            } else {
                                                                if (z.getPinmin() == 0){
                                                                    map.put("status", 8);
                                                                    List<Map> list = new ArrayList<Map>();
                                                                    list.add(map);
                                                                    System.out.println(list);
                                                                    String result = requestDataProcessing(list, I_TYPE, 1);
                                                                    System.out.println("结果：" + result);
                                                                }
                                                                map.put("status", 9);
                                                            }
                                                        }else {
                                                            map.put("status", z.getXuhao());
                                                        }

                                                    } else {
                                                        continue;
                                                    }
                                                    List<Map> list = new ArrayList<Map>();
                                                    if (map.size() == 8){
                                                        list.add(map);
                                                        System.out.println(list);
                                                        String result = requestDataProcessing(list, I_TYPE, 1);
                                                        if (result.contains("true")){
                                                            z.setPinmin(2);
                                                            zhiliangGongxuService.updateById(z);
                                                        }else {
                                                            z.setPinmin(40);
                                                            zhiliangGongxuService.updateById(z);
                                                        }
                                                        System.out.println("结果：" + result);
                                                    }else {
                                                        log.info("数据不全！！！");
                                                    }
                                                }
                                            }
                                        }
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
        sysConfigService.updateSysConfig(JobUtil.PM_CSHZL, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}