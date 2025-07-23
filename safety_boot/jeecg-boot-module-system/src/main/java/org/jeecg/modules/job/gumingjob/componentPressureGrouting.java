package org.jeecg.modules.job.gumingjob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhilianggongxu.service.IZhiliangGongxuService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * \* @author: zml
 * \* Date: 2022-08-17
 * \* Time: 13:08
 * \* Description: 压浆
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class componentPressureGrouting extends BaseHttpRequest implements Job {

    // itype
    private final static String I_TYPE = "componentPressureGrouting";

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrYajiangRenwudanService yajiangRenwudanService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private ITrYajiangSService yajiangSService;
    @Autowired
    private IZhiliangrenwudanService zhiliangrenwudanService;
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.PMSJ_YJ);//压浆
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
                                // 桥名称
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
                                            TrYajiangRenwudan one = null;
                                            try {
                                                QueryWrapper<TrYajiangRenwudan> queryWrapper1 = new QueryWrapper<>();
                                                queryWrapper1.eq("Code",one1.getCode());
                                                one = yajiangRenwudanService.getOne(queryWrapper1);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if (one != null){
                                                TrYajiangM one2 = null;
                                                try {
                                                    QueryWrapper<TrYajiangM> queryWrapper6 = new QueryWrapper<>();
                                                    queryWrapper6.eq("uuid",one.getUuid());
                                                    one2 = yajiangMService.getOne(queryWrapper6);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                if (one2 != null){
                                                    Map<String, Object> map1 = new HashMap<>();
                                                    List<Map> list = new ArrayList<Map>();
                                                    List<Map> listTj = new ArrayList<Map>();
                                                    map1.put("taskId",one.getUuid());//唯⼀任务id
                                                    map1.put("bridgeId",l.getId());//⼤桥id
                                                    map1.put("componentId",one1.getCode());//梁id
                                                    map1.put("pressureGroutingTime",one2.getYjsj());//压浆时间
                                                    map1.put("holeNumber",one2.getKongdaoshu());//孔数量
                                                    QueryWrapper<TrYajiangS> queryWrapper7 = new QueryWrapper<>();
                                                    queryWrapper7.eq("syjid",one2.getSyjid());
                                                    List<TrYajiangS> list5 = yajiangSService.list(queryWrapper7);
                                                    if (list5.size() > 0){
                                                        for (TrYajiangS l4 :list5){
                                                            Map<String, Object> map = new HashMap<>();

                                                            map.put("holeId",l4.getHoleid());//孔id
                                                            map.put("holeNo",l4.getKongdao());//孔号
                                                            map.put("mixProportion",l4.getPeihebi());//配合⽐
                                                            map.put("startTime",l4.getStarttime());//开始时间
                                                            map.put("endTime",l4.getEndtime());//结束时间
                                                            map.put("inflowPulpPressure",l4.getJinjiangyal());//进浆压⼒MPa
                                                            map.put("outflowPulpPressure",l4.getFanjiangyal());//返浆压⼒MPa
                                                            map.put("duration",l4.getChixushijia());//持续时间
                                                            map.put("inflowPulpAmount",l4.getJinjiangshu());//进浆量L
                                                            map.put("outflowPulpAmount",l4.getFanjianglia());//返浆量L
                                                            map.put("vacuumPumpPressure",l4.getZkyl());//真空泵压⼒MPa
                                                            map.put("amount",l4.getYjcs());//压浆次数
                                                            map.put("isQualified",l4.getHege());//是否合格
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
        sysConfigService.updateSysConfig(JobUtil.PMSJ_YJ, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}