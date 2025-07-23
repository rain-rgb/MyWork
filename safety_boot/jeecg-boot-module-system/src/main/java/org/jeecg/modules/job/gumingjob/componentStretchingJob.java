package org.jeecg.modules.job.gumingjob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglass.service.ITrZhanglaSSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * \* Description: 张拉
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class componentStretchingJob extends BaseHttpRequest implements Job {

    // itype
    private final static String I_TYPE = "componentStretching";

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrZhanglaRenwudanService zhanglaRenwudanService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;
    @Autowired
    private ITrZhanglaSService zhanglaSService;
    @Autowired
    private IZhiliangrenwudanService zhiliangrenwudanService;
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.PMSJ_ZL);//张拉
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
                                // 桥名
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
                                            QueryWrapper<TrZhanglaRenwudan> queryWrapper1 = new QueryWrapper<>();
                                            queryWrapper1.eq("Code",one1.getCode());
                                            TrZhanglaRenwudan one = zhanglaRenwudanService.getOne(queryWrapper1);
                                            if (one != null){
                                                TrZhanglaXxb one2 = null;
                                                try {
                                                    QueryWrapper<TrZhanglaXxb> queryWrapper6 = new QueryWrapper<>();
                                                    queryWrapper6.eq("uuid",one.getUuid());
                                                    one2 = zhanglaXxbService.getOne(queryWrapper6);
                                                } catch (Exception e) {
                                                    log.info(one.getUuid()+"张拉任务单号重复！！！");
                                                    e.printStackTrace();
                                                }
                                                if (one2 != null){
                                                    Map<String, Object> map1 = new HashMap<>();
                                                    List<Map> list = new ArrayList<Map>();
                                                    List<Map> lists = new ArrayList<Map>();
                                                    List<Map> listTj = new ArrayList<Map>();
                                                    map1.put("taskId",one.getUuid());//唯⼀任务id
                                                    map1.put("bridgeId",l.getId());//⼤桥id
                                                    map1.put("componentId",one1.getCode());//梁id
                                                    map1.put("stretchTime",one2.getSgsj());//张拉时间

                                                    QueryWrapper<TrZhanglaM> queryWrapper7 = new QueryWrapper<>();
                                                    queryWrapper7.eq("syjid",one2.getSyjid());
                                                    List<TrZhanglaM> list5 = zhanglaMService.list(queryWrapper7);
                                                    map1.put("holeNumber",list5.size());//孔数量
                                                    if (list5.size() > 0){
                                                        for (TrZhanglaM l4 :list5){
                                                            QueryWrapper<TrZhanglaS> queryWrapper8 = new QueryWrapper<>();
                                                            queryWrapper8.eq("fguid",l4.getFguid());
                                                            List<TrZhanglaS> list6 = zhanglaSService.list(queryWrapper8);
                                                            if (list6.size() > 0){
                                                                for (TrZhanglaS l5 :list6){
                                                                    Map<String, Object> map2 = new HashMap<>();
                                                                    try {
                                                                        map2.put("fractureSurface",Integer.parseInt(l5.getDh()));//张拉断⾯
                                                                        map2.put("stageLength",Double.parseDouble(l5.getJdbfb()));//阶段⾏程(%)
                                                                        map2.put("elongation",Double.parseDouble(l5.getJdscl()));//伸⻓量(mm)
                                                                        map2.put("tensionForce",Double.parseDouble(l5.getJdzll()));//张拉⼒(KN)
                                                                    } catch (NumberFormatException e) {
                                                                        map2.put("fractureSurface","");//张拉断⾯
                                                                        map2.put("stageLength","");//阶段⾏程(%)
                                                                        map2.put("elongation","");//伸⻓量(mm)
                                                                        map2.put("tensionForce","");//张拉⼒(KN)
                                                                        e.printStackTrace();
                                                                    }
                                                                    lists.add(map2);
                                                                }
                                                            }
                                                            Map<String, Object> map = new HashMap<>();

                                                            map.put("holeId",l4.getHoleid());//孔id
                                                            map.put("holeNo",l4.getGsbh());//孔号
                                                            map.put("deviceName",l4.getShebeibianhao());//设备名称
                                                            map.put("startTime",l4.getZlsj());//开始时间
                                                            if (StringUtils.isEmpty(l4.getSjzll())){
                                                                map.put("designTension","");//设计张⼒(KN)
                                                            }else {
                                                                map.put("designTension",Double.parseDouble(l4.getSjzll()));//设计张⼒(KN)
                                                            }
                                                            if (StringUtils.isEmpty(l4.getZscl())){
                                                                map.put("totalElongation","");//总伸⻓量(mm)
                                                            }else {
                                                                map.put("totalElongation",Double.parseDouble(l4.getZscl()));//总伸⻓量(mm)
                                                            }
                                                            if (StringUtils.isEmpty(l4.getLlscl())){
                                                                map.put("designElongation","");//设计伸⻓量(mm)
                                                            }else {
                                                                map.put("designElongation",Double.parseDouble(l4.getLlscl()));//设计伸⻓量(mm)
                                                            }
                                                            if (StringUtils.isEmpty(l4.getSclper())){
                                                                map.put("elongationError","");//延⻓量误差(%)
                                                            }else {
                                                                map.put("elongationError",Double.parseDouble(l4.getSclper()));//延⻓量误差(%)
                                                            }
                                                            if (StringUtils.isEmpty(l4.getZzlb())){
                                                                map.put("overLengthPercentage","");//超⻓百分⽐(%)
                                                            }else {
                                                                map.put("overLengthPercentage",Double.parseDouble(l4.getZzlb()));//超⻓百分⽐(%)
                                                            }
                                                            map.put("fractureSurfaceDtoList",lists);//张拉断⾯列表
                                                            list.add(map);
                                                        }
                                                        map1.put("holeList",list);//孔列表
                                                        listTj.add(map1);
                                                        System.out.println(listTj);
                                                        String result = requestDataProcessing(listTj, I_TYPE, 1);
                                                        System.out.println("结果：" + result);
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
        sysConfigService.updateSysConfig(JobUtil.PMSJ_ZL, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
