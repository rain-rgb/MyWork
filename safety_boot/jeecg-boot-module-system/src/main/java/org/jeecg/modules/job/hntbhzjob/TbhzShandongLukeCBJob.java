package org.jeecg.modules.job.hntbhzjob;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.ShandongUtil;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/6/2
 * \* Time: 11:23
 * \* Description:山东陆科推送砼拌合站超标数据(超标数据包含所有的不合格数据 注：推送的数据已经判断过超不超标)
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TbhzShandongLukeCBJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private IBhzChaobiaoCfgService bhzChaobiaoCfgService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private ShandongUtil shandongUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysDepartService sysDepartService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HNTBHZ_TUISONG_SHANDONGLUKECB);//砼拌合站数据推送山东陆科
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到砼拌合站推送山东陆科超标任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject =JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输砼拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        String sbjnolist=null;
        for (int i=0;i<split.length;i++) {
            String shebeiNo = split[i];
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);//设备信息
            Integer status1 = selectshebeione.getStatus1();
            if(status1==3){//判断当前设备是否发送了项目信息 拌合站信息 以及设备信息
                if(sbjnolist==null){
                    sbjnolist=selectshebeione.getSbjno();
                }else{
                    sbjnolist=sbjnolist+","+selectshebeione.getSbjno();
                }
            }
        }
        if (StrUtil.isBlank(sbjnolist)) {
            log.info(String.format("没有配置要传输砼拌合站的设备" + DateUtils.now()));
            return;
        }
        List<BhzCementBase> selecthntbhzones = bhzCementBaseService.selecthntbhzcbList(curid, 1,sbjnolist,0);//所有的超标数据
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无砼拌合站未判断的数据" + DateUtils.now()));
            return;
        }
        int id=0;
        String s = shandongUtil.GetTokenpost(ShandongUtil.GetToken, ShandongUtil.MORENToken);
        String token=null;
        if(s.equals("200")){
            //重新刷新获取的token
            token = (String) redisUtil.get("ShanDongLuKeToken");
        }else{
            //未刷新之前的token
            token=ShandongUtil.MORENToken;
        }
        JSONArray publicMixerMoreList=new JSONArray();//传的参数的格式为[{},{}]
        for (BhzCementBase selecthntbhzone : selecthntbhzones) {
            id=selecthntbhzone.getId();
            String shebeiNo = selecthntbhzone.getShebeiNo();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            //SysDepart queryone = sysDepartService.queryone(selectshebeione.getSysOrgCode());//拌合站信息
            String procode = selectshebeione.getProcode();// 项目编码
            JSONObject publicMixingStationList1 =new JSONObject();
            //int chuliaotimes = (int) ((selecthntbhzone.getProductDatetime().getTime())/1000);
            //int savetimes = (int) ((selecthntbhzone.getSaveDatetime().getTime())/1000);
            //int caijitimes = (int) ((selecthntbhzone.getCollectDatetime().getTime())/1000);
            publicMixingStationList1.putOpt("id",selecthntbhzone.getId());
            publicMixingStationList1.putOpt("mixerId",selecthntbhzone.getId());//实时数据id
            publicMixingStationList1.putOpt("deviceNumber",shebeiNo);//设备编号
            publicMixingStationList1.putOpt("proCode",procode);//    项目编码
            publicMixingStationList1.putOpt("mixerStandardId",selecthntbhzone.getBatchNo());//    编号
           // publicMixingStationList1.putOpt("realityCount",selecthntbhzone.getEstimateNumber());//	实际方量
            //publicMixingStationList1.putOpt("jobNumber",selecthntbhzone.getWorkNo());//	工单号
            //publicMixingStationList1.putOpt("handlers",selecthntbhzone.getHandlers());//	操作者
//            if(selecthntbhzone.getProjectName()!=null){
//                publicMixingStationList1.putOpt("proName",selecthntbhzone.getProjectName());//	工程名称
//            }else{
//                publicMixingStationList1.putOpt("proName","无");//	工程名称
//            }
//            if(selecthntbhzone.getJobLocation()!=null){
//                publicMixingStationList1.putOpt("jobLocation",selecthntbhzone.getJobLocation());//    施工地点
//            }else{
//                publicMixingStationList1.putOpt("jobLocation","无");//    施工地点
//            }
//            if(selecthntbhzone.getPoureLocation()!=null){
//                publicMixingStationList1.putOpt("castingParts",selecthntbhzone.getPoureLocation());//	浇筑部位
//            }else{
//                publicMixingStationList1.putOpt("castingParts","无");//	浇筑部位
//            }
//            if(selecthntbhzone.getCementVariety()!=null){
//                publicMixingStationList1.putOpt("cementType",selecthntbhzone.getCementVariety());//	水泥品种
//            }else{
//                publicMixingStationList1.putOpt("cementType","无");//	水泥品种
//            }
//            if(selecthntbhzone.getAdditiveVariety()!=null){
//                publicMixingStationList1.putOpt("otherAddType",selecthntbhzone.getAdditiveVariety());//    外加剂品种
//            }else{
//                publicMixingStationList1.putOpt("otherAddType","无");//    外加剂品种
//            }
//            if(selecthntbhzone.getFormulaNo()!=null){
//                publicMixingStationList1.putOpt("formulaNumber",selecthntbhzone.getFormulaNo());//    配方号
//            }else{
//                publicMixingStationList1.putOpt("formulaNumber","无");//    配方号
//            }
            //publicMixingStationList1.putOpt("strengthGrade",selecthntbhzone.getStrengthRank());//	强度等级
            //publicMixingStationList1.putOpt("mixingMinute",selecthntbhzone.getStirDatetime());//	搅拌时长
            //publicMixingStationList1.putOpt("outgoingDate",chuliaotimes);//    出料时间
            //publicMixingStationList1.putOpt("acquisitionDate",savetimes);//    保存时间
            //publicMixingStationList1.putOpt("mixingName",selectshebeione.getSbname());//    拌合机名称
            //publicMixingStationList1.putOpt("mixingStationName",queryone.getDepartName());//    拌合站名称
            //publicMixingStationList1.putOpt("mixingStationId",queryone.getId());//    拌合站ID
            //publicMixingStationList1.putOpt("type",0);//    拌合站类型
            //publicMixingStationList1.putOpt("collectDate",caijitimes);//    采集时间
//            if(selecthntbhzone.getOverLevel()>0){
                //publicMixingStationList1.putOpt("isStandard",1);//    是否超标
//            }else{
//                publicMixingStationList1.putOpt("isStandard",0);//    是否超标
//            }
            publicMixingStationList1.putOpt("standardType",selecthntbhzone.getOverLevel());//    超标级别
            String batchNo = selecthntbhzone.getBatchNo();
            List<BhzCementDetail> bhzCementDetails = bhzCementDetailService.selectByMainId(batchNo);
            if(bhzCementDetails.size()>0){
                int [] datelist={0,0,0,0,0,0,0,0,0,0};
                for (BhzCementDetail bhzCementDetail : bhzCementDetails) {
                    if(bhzCementDetail.getMaterialeType()==1 && bhzCementDetail.getMaterialeOverLevel()>0){//细骨料
                        if(datelist[1]==0){
                            publicMixingStationList1.putOpt("thinOne",bhzCementDetail.getMaterialeOverLevel());//细骨料1
                            //publicMixingStationList1.putOpt("thinOneKg",bhzCementDetail.getTheoryNumber());//细骨料1配比
                            datelist[1]=1;
                        }else if(datelist[1]==1){
                            publicMixingStationList1.putOpt("thinTwo",bhzCementDetail.getMaterialeOverLevel());//    细骨料2
                            //publicMixingStationList1.putOpt("thinTwoKg",bhzCementDetail.getTheoryNumber());//细骨料2配比
                            datelist[1]=2;
                        }
                    }
                    if(bhzCementDetail.getMaterialeType()==3 && bhzCementDetail.getMaterialeOverLevel()>0){//粗骨料
                        if(datelist[2]==0){
                            publicMixingStationList1.putOpt("thickOne",bhzCementDetail.getMaterialeOverLevel());//粗骨料1
                            //publicMixingStationList1.putOpt("thickOneKg",bhzCementDetail.getTheoryNumber());//粗骨料1配比
                            datelist[2]=1;
                        }else if(datelist[2]==1){
                            publicMixingStationList1.putOpt("thickTwo",bhzCementDetail.getMaterialeOverLevel());//    粗骨料2
                            //publicMixingStationList1.putOpt("thickTwoKg",bhzCementDetail.getTheoryNumber());//粗骨料1配比
                            datelist[2]=2;
                        }else if(datelist[2]==2){
                            publicMixingStationList1.putOpt("thickThree",bhzCementDetail.getMaterialeOverLevel());//    粗骨料3
                            //publicMixingStationList1.putOpt("thickThreeKg",bhzCementDetail.getTheoryNumber());//粗骨料1配比
                            datelist[2]=3;
                        }
                    }
                    if(bhzCementDetail.getMaterialeType()==5 && bhzCementDetail.getMaterialeOverLevel()>0){//水
                        if(datelist[4]==0){
                            publicMixingStationList1.putOpt("waterOne",bhzCementDetail.getMaterialeOverLevel());//水1
                            //publicMixingStationList1.putOpt("waterOneKg",bhzCementDetail.getTheoryNumber());//水1配比
                            datelist[4]=1;
                        }else if(datelist[4]==1){
                            publicMixingStationList1.putOpt("waterTwo",bhzCementDetail.getMaterialeOverLevel());//    水2
                            //publicMixingStationList1.putOpt("waterTwoKg",bhzCementDetail.getTheoryNumber());//    水2配比
                            datelist[4]=2;
                        }
                    }
                    if(bhzCementDetail.getMaterialeType()==6 && bhzCementDetail.getMaterialeOverLevel()>0){//水泥
                        if(datelist[5]==0){
                            publicMixingStationList1.putOpt("cementOne",bhzCementDetail.getMaterialeOverLevel());//水泥1
                            //publicMixingStationList1.putOpt("cementOneKg",bhzCementDetail.getTheoryNumber());//水泥1配比
                            datelist[5]=1;
                        }else if(datelist[5]==1){
                            publicMixingStationList1.putOpt("cementTwo",bhzCementDetail.getMaterialeOverLevel());//水泥2
                            //publicMixingStationList1.putOpt("cementTwoKg",bhzCementDetail.getTheoryNumber());//水泥2配比
                            datelist[5]=2;
                        }
                    }
                    if(bhzCementDetail.getMaterialeType()==7 && bhzCementDetail.getMaterialeOverLevel()>0){//矿粉&&粉料
                        if(datelist[6]==0){
                            publicMixingStationList1.putOpt("mineralPowder",bhzCementDetail.getMaterialeOverLevel());//矿粉
                            //publicMixingStationList1.putOpt("mineralPowderKg",bhzCementDetail.getTheoryNumber());//矿粉配比
                            datelist[6]=1;
                        }else if(datelist[6]==1){
                            publicMixingStationList1.putOpt("powderFive",bhzCementDetail.getMaterialeOverLevel());//粉料5
                            //publicMixingStationList1.putOpt("powderFiveKg",bhzCementDetail.getTheoryNumber());//粉料5配比
                            datelist[6]=2;
                        }else if(datelist[6]==2){
                            publicMixingStationList1.putOpt("powderSix",bhzCementDetail.getMaterialeOverLevel());//粉料6
                            //publicMixingStationList1.putOpt("powderSixKg",bhzCementDetail.getTheoryNumber());//粉料6配比
                        }
                    }
                    if(bhzCementDetail.getMaterialeType()==8 && bhzCementDetail.getMaterialeOverLevel()>0){//粉煤灰&&粉料
                        if(datelist[7]==0){
                            publicMixingStationList1.putOpt("mineralPowder",bhzCementDetail.getMaterialeOverLevel());//矿粉
                            //publicMixingStationList1.putOpt("mineralPowderKg",bhzCementDetail.getTheoryNumber());//矿粉配比
                            datelist[7]=1;
                        }else if(datelist[7]==1){
                            publicMixingStationList1.putOpt("powderFive",bhzCementDetail.getMaterialeOverLevel());//粉料5
                            //publicMixingStationList1.putOpt("powderFiveKg",bhzCementDetail.getTheoryNumber());//粉料5配比
                            datelist[7]=2;
                        }else if(datelist[7]==2){
                            publicMixingStationList1.putOpt("powderSix",bhzCementDetail.getMaterialeOverLevel());//粉料6
                            //publicMixingStationList1.putOpt("powderSixKg",bhzCementDetail.getTheoryNumber());//粉料6配比
                        }
                    }
                    if(bhzCementDetail.getMaterialeType()==9 && bhzCementDetail.getMaterialeOverLevel()>0){//外加剂
                        if(datelist[8]==0){
                            publicMixingStationList1.putOpt("otherAddOne",bhzCementDetail.getMaterialeOverLevel());//外加剂1
                            //publicMixingStationList1.putOpt("otherAddOneKg",bhzCementDetail.getTheoryNumber());//外加剂1配比
                            datelist[8]=1;
                        }else if(datelist[8]==1){
                            publicMixingStationList1.putOpt("otherAddTwo",bhzCementDetail.getMaterialeOverLevel());//外加剂2
                            //publicMixingStationList1.putOpt("otherAddTwoKg",bhzCementDetail.getTheoryNumber());//外加剂2配比
                            datelist[8]=2;
                        }else if(datelist[8]==2){
                            publicMixingStationList1.putOpt("otherAddThree",bhzCementDetail.getMaterialeOverLevel());//外加剂3
                            //publicMixingStationList1.putOpt("otherAddThreeKg",bhzCementDetail.getTheoryNumber());//外加剂3配比
                            datelist[8]=3;
                        }else if(datelist[8]==3){
                            publicMixingStationList1.putOpt("otherAddFour",bhzCementDetail.getMaterialeOverLevel());//外加剂4
                            //publicMixingStationList1.putOpt("otherAddFourKg",bhzCementDetail.getTheoryNumber());//外加剂4配比
                            datelist[8]=4;
                        }
                    }
                }
            }
            publicMixerMoreList.add(publicMixingStationList1);
        }
        if(publicMixerMoreList.size()>0){
            String s1 = shandongUtil.TuisongList(ShandongUtil.TuiSong, token, ShandongUtil.publicMixerMoreList, publicMixerMoreList.toString());
            if(s1.equals("200")){
                sysConfigService.updateSysConfig(JobUtil.HNTBHZ_TUISONG_SHANDONGLUKECB, id);//根据传过来的唯一值来修改当前判断到的数据id
                //此处修改sys_config 中记录推送到那个id
                log.info(String.format("推送砼拌合站超标数据成功" + id+DateUtils.now()));
            }else{
                log.info(String.format("推送砼拌合站超标数据失败" + id+DateUtils.now()));
            }
        }

    }
}
