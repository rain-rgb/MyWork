package org.jeecg.modules.ruicangjob.shiyan;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.hnthtconsign.entity.HnthtConsign;
import com.trtm.iot.hnthtconsign.service.IHnthtConsignService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.trgangjinbhcm.entity.TrGangjinbhcM;
import com.trtm.iot.trgangjinbhcm.service.ITrGangjinbhcMService;
import com.trtm.iot.trgangjinbhcs.entity.TrGangjinbhcS;
import com.trtm.iot.trgangjinbhcs.service.ITrGangjinbhcSService;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.ruicangjob.RuiCangUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * \* @author: Xx
 * \* Date: 2022/1/7
 * \* Time: 9:30
 * \* Description:瑞苍--钢筋保护层数据推送
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class GangJinBaoHuCengJob implements Job {
    @Autowired
    private IHnthtConsignService iHnthtConsignService;
    @Autowired
    private RuiCangUtil ruiCangUtil;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrGangjinbhcMService trGangjinbhcMService;
    @Autowired
    private ITrGangjinbhcSService trGangjinbhcSService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RUICANG_GANGJINBAOHUCENG);//混凝土回弹数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞苍--钢筋保护层数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输钢筋保护层的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrGangjinbhcM> trGangjinbhcMS = trGangjinbhcMService.selectGangJinList(shebeilist, curid);
        if (null == trGangjinbhcMS || trGangjinbhcMS.size() == 0) {
            log.info(String.format("暂无瑞苍--钢筋保护层未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrGangjinbhcM trGangjinbhcM : trGangjinbhcMS) {
            JSONObject jsonObject1 = new JSONObject();//主表数据
            SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            id = trGangjinbhcM.getId();
            String code = trGangjinbhcM.getCode();
            HnthtConsign queryone = iHnthtConsignService.queryone(code);
            if (null == queryone) {
                log.info(String.format("暂无瑞苍--钢筋保护层任务单数据" + DateUtils.now()));
                continue;
            }
            jsonObject1.set("wtid", code);//委托id
            jsonObject1.set("sampleNo", code);//委托编号
            //jsonObject1.set("sampleDate",);//取样日期
            jsonObject1.set("projectName", trGangjinbhcM.getSgbw());//施工部位
            jsonObject1.set("syrq", dateFm.format(trGangjinbhcM.getChecktime()));//实验日期
            jsonObject1.set("createBy", queryone.getCreateBy());//创建人
            jsonObject1.set("createTime", dateFm.format(queryone.getCreateTime()));//创建时间
            jsonObject1.set("sampleShengChanPiHao", trGangjinbhcM.getPoint());//生产批号
            jsonObject1.set("SBBH", trGangjinbhcM.getShebeiNo());//设备编号

            List<TrGangjinbhcS> trGangjinbhcS = trGangjinbhcSService.selectGangjinSList(trGangjinbhcM.getTestid());
            if (trGangjinbhcS.size() == 0) {
                log.info(String.format("暂无瑞苍--钢筋保护层子表数据数据" + DateUtils.now()));
                continue;
            }
            Map map = new HashMap();
            String bhchdscz1 = "";
            String bhchdscz2 = "";
            String bhchdscz3 = "";
            String bhchdscz4 = "";
            String bhchdscz5 = "";
            String bhchdscz6 = "";
            String bhchdscz7 = "";
            String bhchdscz8 = "";
            String jjscz1 = "";
            String jjscz2 = "";
            String jjscz3 = "";
            String jjscz4 = "";
            String jjscz5 = "";
            String jjscz6 = "";
            String jjscz7 = "";
            String jjscz8 = "";
            for (int i = 0; i < trGangjinbhcS.size(); i++) {
                if (i < 10) {
                    String[] split1 = new String[0];
                    try {
                        split1 = trGangjinbhcS.get(i).getStrthickness().split(",");
                    } catch (Exception e) {
                        trGangjinbhcM.setIstuisong(2);
                        trGangjinbhcMService.updateById(trGangjinbhcM);
                        continue;
                    }
                    if(split1.length<3){
                        bhchdscz1+=(trGangjinbhcS.get(i).getStrthickness());

                        bhchdscz1+=(",");
                    }else{
                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
                        bhchdscz1+=(split1[0]+","+split1[1]);
                        bhchdscz1+=(",");
                    }
                    jjscz1 += trGangjinbhcS.get(i).getThickness()+",";
//                    if (i == 9) {
//                        bhchdscz1+=(trGangjinbhcS.get(i).getThickness());
//                    }else{
//                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
//                        bhchdscz1+=(trGangjinbhcS.get(i).getThickness() + ",");
//                    }
                }
                if (i > 9 && i < 20) {
                    String[] split1 = trGangjinbhcS.get(i).getStrthickness().split(",");
                    if(split1.length<3){
                        bhchdscz2+=(trGangjinbhcS.get(i).getStrthickness());
                        bhchdscz2+=(",");
                    }else{
                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
                        bhchdscz2+=(split1[0]+","+split1[1]);
                        bhchdscz2+=(",");
                    }
                    jjscz2+=trGangjinbhcS.get(i).getThickness()+",";
//                    if (i == 39) {
//                        bhchdscz2+=(trGangjinbhcS.get(i).getThickness());
//                    }else{
//                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
//                        bhchdscz2+=(trGangjinbhcS.get(i).getThickness() + ",");
//                    }
                }
                if (i > 19 && i < 30) {
                    String[] split1 = trGangjinbhcS.get(i).getStrthickness().split(",");
                    if(split1.length<3){
                        bhchdscz3+=(trGangjinbhcS.get(i).getStrthickness());
                        bhchdscz3+=(",");
                    }else{
                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
                        bhchdscz3+=(split1[0]+","+split1[1]);
                        bhchdscz3+=(",");
                    }
                    jjscz3+=trGangjinbhcS.get(i).getThickness()+",";
//                    if (i == 59) {
//                        bhchdscz3+=(trGangjinbhcS.get(i).getThickness());
//                    }else{
//                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
//                        bhchdscz3+=(trGangjinbhcS.get(i).getThickness() + ",");
//                    }
                }
                if (i > 29 && i < 40) {
                    String[] split1 = trGangjinbhcS.get(i).getStrthickness().split(",");
                    if(split1.length<3){
                        bhchdscz4+=(trGangjinbhcS.get(i).getStrthickness());
                        bhchdscz4+=(",");
                    }else{
                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
                        bhchdscz4+=(split1[0]+","+split1[1]);
                        bhchdscz4+=(",");
                    }
                    jjscz4+=trGangjinbhcS.get(i).getThickness()+",";
//                    if (i == 79) {
//                        bhchdscz4+=(trGangjinbhcS.get(i).getThickness());
//                    }else{
//                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
//                        bhchdscz4+=(trGangjinbhcS.get(i).getThickness() + ",");
//                    }
                }
                if (i > 39 && i < 50) {
                    String[] split1 = trGangjinbhcS.get(i).getStrthickness().split(",");
                    if(split1.length<3){
                        bhchdscz5+=(trGangjinbhcS.get(i).getStrthickness());
                        bhchdscz5+=(",");
                    }else{
                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
                        bhchdscz5+=(split1[0]+","+split1[1]);
                        bhchdscz5+=(",");
                    }
                    jjscz5+=trGangjinbhcS.get(i).getThickness()+",";
//                    if (i == 99) {
//                        bhchdscz5+=(trGangjinbhcS.get(i).getThickness());
//                    }else{
//                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
//                        bhchdscz5+=(trGangjinbhcS.get(i).getThickness() + ",");
//                    }
                }
                if (i > 49 && i < 60) {
                    String[] split1 = trGangjinbhcS.get(i).getStrthickness().split(",");
                    if(split1.length<3){
                        bhchdscz6+=(trGangjinbhcS.get(i).getStrthickness());
                        bhchdscz6+=(",");
                    }else{
                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
                        bhchdscz6+=(split1[0]+","+split1[1]);
                        bhchdscz6+=(",");
                    }
                    jjscz6+=trGangjinbhcS.get(i).getThickness()+",";
//                    if (i == 119) {
//                        bhchdscz6+=(trGangjinbhcS.get(i).getThickness());
//                    }else{
//                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
//                        bhchdscz6+=(trGangjinbhcS.get(i).getThickness() + ",");
//                    }
                }
                if (i > 59 && i < 70) {
                    String[] split1 = trGangjinbhcS.get(i).getStrthickness().split(",");
                    if(split1.length<3){
                        bhchdscz7+=(trGangjinbhcS.get(i).getStrthickness());
                        bhchdscz7+=(",");
                    }else{
                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
                        bhchdscz7+=(split1[0]+","+split1[1]);
                        bhchdscz7+=(",");
                    }
                    jjscz7+=trGangjinbhcS.get(i).getThickness()+",";
//                    if (i == 139) {
//                        bhchdscz7+=(trGangjinbhcS.get(i).getThickness());
//                    }else{
//                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
//                        bhchdscz7+=(trGangjinbhcS.get(i).getThickness() + ",");
//                    }
                }
                if (i > 69 && i < 80) {
                    String[] split1 = trGangjinbhcS.get(i).getStrthickness().split(",");
                    if(split1.length<3){
                        bhchdscz8+=(trGangjinbhcS.get(i).getStrthickness());
                        bhchdscz8+=(",");
                    }else{
                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
                        bhchdscz8+=(split1[0]+","+split1[1]);
                        bhchdscz8+=(",");
                    }
                    jjscz8+=trGangjinbhcS.get(i).getThickness()+",";
//                    if (i == 159) {
//                        bhchdscz8+=(trGangjinbhcS.get(i).getThickness());
//                    }else{
//                        //保护层厚度实测值 1(mm)（例:1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15,16,17,18,19,20） 注意用英文逗号隔 开
//                        bhchdscz8+=(trGangjinbhcS.get(i).getThickness() + ",");
//                    }
                }
            }
            if (bhchdscz1.length()>0){
                bhchdscz1 = bhchdscz1.replace(",,", ",");
                bhchdscz1 = bhchdscz1.substring(0,bhchdscz1.length()-1);
                jjscz1 = jjscz1.substring(0,jjscz1.length()-1);
            }
            if (bhchdscz2.length()>0){
                bhchdscz2 = bhchdscz2.replace(",,", ",");
                bhchdscz2 = bhchdscz2.substring(0,bhchdscz2.length()-1);
                jjscz2 = jjscz2.substring(0,jjscz2.length()-1);
            }
            if (bhchdscz3.length()>0){
                bhchdscz3 = bhchdscz3.replace(",,", ",");
                bhchdscz3 = bhchdscz3.substring(0,bhchdscz3.length()-1);
                jjscz3 = jjscz3.substring(0,jjscz3.length()-1);
            }
            if (bhchdscz4.length()>0){
                bhchdscz4 = bhchdscz4.replace(",,", ",");
                bhchdscz4 = bhchdscz4.substring(0,bhchdscz4.length()-1);
                jjscz4 = jjscz4.substring(0,jjscz4.length()-1);
            }
            if (bhchdscz5.length()>0){
                bhchdscz5 = bhchdscz5.replace(",,", ",");
                bhchdscz5 = bhchdscz5.substring(0,bhchdscz5.length()-1);
                jjscz5 = jjscz5.substring(0,jjscz5.length()-1);
            }
            if (bhchdscz6.length()>0){
                bhchdscz6 = bhchdscz6.replace(",,", ",");
                bhchdscz6 = bhchdscz6.substring(0,bhchdscz6.length()-1);
                jjscz6 = jjscz6.substring(0,jjscz6.length()-1);
            }
            if (bhchdscz7.length()>0){
                bhchdscz7 = bhchdscz7.replace(",,", ",");
                bhchdscz7 = bhchdscz7.substring(0,bhchdscz7.length()-1);
                jjscz7 = jjscz7.substring(0,jjscz7.length()-1);
            }
            if (bhchdscz8.length()>0){
                bhchdscz8 = bhchdscz8.replace(",,", ",");
                bhchdscz8 = bhchdscz8.substring(0,bhchdscz8.length()-1);
                jjscz8 = jjscz8.substring(0,jjscz8.length()-1);
            }
            map.put("bhchdscz1", bhchdscz1);
            map.put("bhchdscz2", bhchdscz2);
            map.put("bhchdscz3", bhchdscz3);
            map.put("bhchdscz4", bhchdscz4);
            map.put("bhchdscz5", bhchdscz5);
            map.put("bhchdscz6", bhchdscz6);
            map.put("bhchdscz7", bhchdscz7);
            map.put("bhchdscz8", bhchdscz8);
            map.put("jjscz1", jjscz1);
            map.put("jjscz2", jjscz2);
            map.put("jjscz3", jjscz3);
            map.put("jjscz4", jjscz4);
            map.put("jjscz5", jjscz5);
            map.put("jjscz6", jjscz6);
            map.put("jjscz7", jjscz7);
            map.put("jjscz8", jjscz8);
            map.put("goujianmingcheng", trGangjinbhcM.getPoint());//构件名称
            map.put("gangjinzhijing", trGangjinbhcM.getMasterdiameter());//钢筋直径
            map.put("jianjushejizhi", trGangjinbhcM.getMasterspacing());//钢筋间距设计值 (mm)
            map.put("jianjuyunxupiancha", "0");//钢筋间距允许偏差 (mm
            map.put("baohucengshejizhi", trGangjinbhcM.getDesignthickness());//保护层厚度设计值 (mm
            map.put("baohucengyunxupiancha", "0");//保护层厚度允许偏差(mm)

            jsonObject1.set("data", map);
            System.out.println(jsonObject1.toString());
            log.info(String.format("瑞苍--钢筋保护层数据" + DateUtils.now())+jsonObject1.toString());
            String s = ruiCangUtil.GETToken();
            if(s==null){
                log.info(String.format("瑞苍--钢筋保护层获取token失败"+id + DateUtils.now()));
            }
            Integer integer = ruiCangUtil.GangJingTuiSong(jsonObject1,s);
            if(integer==200){
                TrGangjinbhcM trGangjinbhcM1=new TrGangjinbhcM();
                trGangjinbhcM1.setId(id);
                trGangjinbhcM.setIstuisong(1);
                trGangjinbhcMService.updateById(trGangjinbhcM);
                log.info(String.format("瑞苍--钢筋保护层数据推送成功"+id + DateUtils.now()));
                sysConfigService.updateSysConfig(JobUtil.RUICANG_GANGJINBAOHUCENG, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else{
                log.info(String.format("瑞苍--钢筋保护层数据推送失败"+id + DateUtils.now()));
            }
        }
    }
}
