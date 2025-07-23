package org.jeecg.modules.job.lqbhzJob;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
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

import java.sql.Timestamp;
import java.util.List;

/**
 * \* @author:
 * \* Date: 2021/6/5
 * \* Time: 11:23
 * \* Description:山东陆科推送沥青拌合站实时数据(实时数据包含所有的合格不合格数据 注：推送的数据已经判断过超不超标)
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LqbhzShandongLukeJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private IBhzChaobiaoCfgService bhzChaobiaoCfgService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzLqCailiaoService bhzLqCailiaoService;
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LQBHZ_TUISONG_SHANDONGLIKE);//沥青拌合站数据推送山东陆科
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到沥青拌合站推送山东陆科定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject =JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输沥青拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        String sbjnolist = null;
        for (int i=0;i<split.length;i++) {
            String shebeiNo = split[i];
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);//设备信息
//            SysDepart queryone = sysDepartService.queryone(selectshebeione.getSysOrgCode());//拌合站信息
            Integer status1 = selectshebeione.getStatus1(); //状态 是否推送 0 未推送 1项目信息已经推送 2 拌合站信息已经推送 3 设备信息已经推送  10 推送项目信息异常 20推送拌合站信息异常 30推送设备信息异常
            if(status1==3){
                if(sbjnolist == null){
                    sbjnolist = selectshebeione.getSbjno();
                }else {
                    sbjnolist = shebeilist + "," + selectshebeione.getSbjno();
                }
            }
        }
        if (StrUtil.isBlank(sbjnolist)) {
            log.info(String.format("没有配置要传输沥青拌合站的设备" + DateUtils.now()));
            return;
        }
        List<BhzLqBases> selectlqbhzones = bhzLqBasesService.selectlqbhzList(curid, 1,sbjnolist);
        if (null == selectlqbhzones || selectlqbhzones.size() == 0) {
            log.info(String.format("暂无沥青拌合站未判断的数据" + DateUtils.now()));
            return;
        }
        int id=0;
        String s = shandongUtil.GetTokenpost(ShandongUtil.GetToken, ShandongUtil.MORENToken);
        String token = null;
        if(s.equals("200")){
            //重新刷新获取的token
            token = (String) redisUtil.get("ShanDongLuKeToken");
        }else{
            //未刷新之前的token
            token = ShandongUtil.MORENToken;
        }
        JSONArray publicPitchList=new JSONArray();//传的参数的格式为[{},{}]
        for (BhzLqBases selectlqbhzone : selectlqbhzones) {
            String guid = selectlqbhzone.getGuid();
            String sbjno = selectlqbhzone.getShebeibianhao();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(sbjno);//设备信息
            SysDepart queryone = sysDepartService.queryone(selectshebeione.getSysOrgCode());//拌合站信息
            List<BhzLqCailiao> selectcailiaoones = bhzLqCailiaoService.selectcailiaolist(guid);
            int collettimes = (int) ((selectlqbhzone.getCaijishijian().getTime())/1000);
            int chualiaotimes = (int) ((Timestamp.valueOf(selectlqbhzone.getChuliaoshijian()).getTime())/1000);
            int savetimes = (int) ((Timestamp.valueOf(selectlqbhzone.getBaocunshijian()).getTime())/1000);
            id=selectlqbhzone.getId();
            JSONObject publicPitchList1 =new JSONObject();
            publicPitchList1.putOpt("id",selectlqbhzone.getId());//id
            publicPitchList1.putOpt("deviceNumber",selectlqbhzone.getShebeibianhao());//设备编号
            publicPitchList1.putOpt("proCode",selectshebeione.getProcode());//项目编码
            if(selectlqbhzone.getZongchanliang() != null){
                publicPitchList1.putOpt("realityCount",selectlqbhzone.getZongchanliang());//产量
            }else {
                publicPitchList1.putOpt("realityCount","无");//产量
            }
            publicPitchList1.putOpt("outgoingDate",chualiaotimes);//出料时间
            if(selectlqbhzone.getBanheshijian() != null){
                publicPitchList1.putOpt("mixingMinute",selectlqbhzone.getBanheshijian());//搅拌时长
            }else {
                publicPitchList1.putOpt("mixingMinute","无");//搅拌时长
            }
            if(selectlqbhzone.getFormulaNo() != null){
                publicPitchList1.putOpt("formulaNumber",selectlqbhzone.getFormulaNo());//配方号
            }else {
                publicPitchList1.putOpt("formulaNumber","无");//配方号
            }
            if(selectlqbhzone.getLiqingwd() != null){
                publicPitchList1.putOpt("pitchTemperature",selectlqbhzone.getLiqingwd());//沥青温度
            }else {
                publicPitchList1.putOpt("pitchTemperature","无");//沥青温度
            }
            if(selectlqbhzone.getGuliaowd() != null){
                publicPitchList1.putOpt("aggregateTemperature",selectlqbhzone.getGuliaowd());//骨料温度
            }else {
                publicPitchList1.putOpt("aggregateTemperature","无");//骨料温度
            }
            if(selectlqbhzone.getChuliaoshijian() != null){
                publicPitchList1.putOpt("outgoingTemperature",selectlqbhzone.getChuliaowd());//出料温度
            }else {
                publicPitchList1.putOpt("outgoingTemperature","无");//出料温度
            }
            if(selectlqbhzone.getPoureLocation() != null){
                publicPitchList1.putOpt("constructionSite",selectlqbhzone.getPoureLocation());//施工部位
            }else {
                publicPitchList1.putOpt("constructionSite","无");//施工部位
            }
            if(selectlqbhzone.getProjectName() != null){
                publicPitchList1.putOpt("proName",selectlqbhzone.getProjectName());//工程名称
            }else {
                publicPitchList1.putOpt("proName","无");//工程名称
            }
            publicPitchList1.putOpt("acquisitionDate",savetimes);//保存时间
            if(selectcailiaoones.size() > 0) {
                int[] detailstate = {0,0,0,0,0,0,0,0,0,0};
                for (BhzLqCailiao selectlqcailiao : selectcailiaoones) {
                    if (selectlqcailiao.getCailiaoleixing() == 2) {
                        if(detailstate[1] == 0){//骨料1
                            if (selectlqcailiao.getShijiyongliang() != null) {
                                publicPitchList1.putOpt("factAggregateOne", selectlqcailiao.getShijiyongliang());
                            } else {
                                publicPitchList1.putOpt("factAggregateOne", "无");
                            }
                            if (selectlqcailiao.getTheoryNumber() != null) {
                                publicPitchList1.putOpt("theoryAggregateOne", selectlqcailiao.getTheoryNumber());
                            } else {
                                publicPitchList1.putOpt("theoryAggregateOne", "无");
                            }
                            detailstate[1] = 1;
                        }else if(detailstate[1]==1){//骨料4
                            if (selectlqcailiao.getShijiyongliang() != null) {
                                publicPitchList1.putOpt("factAggregateFour", selectlqcailiao.getShijiyongliang());
                            } else {
                                publicPitchList1.putOpt("factAggregateFour", "无");
                            }
                            if (selectlqcailiao.getTheoryNumber() != null) {
                                publicPitchList1.putOpt("theoryAggregateFour", selectlqcailiao.getTheoryNumber());
                            } else {
                                publicPitchList1.putOpt("theoryAggregateFour", "无");
                            }
                            detailstate[1] = 2;
                        }else {//骨料7
                            if (selectlqcailiao.getShijiyongliang() != null) {
                                publicPitchList1.putOpt("factAggregateSeven", selectlqcailiao.getShijiyongliang());
                            } else {
                                publicPitchList1.putOpt("factAggregateSeven", "无");
                            }
                            if (selectlqcailiao.getTheoryNumber() != null) {
                                publicPitchList1.putOpt("theoryAggregateSeven", selectlqcailiao.getTheoryNumber());
                            } else {
                                publicPitchList1.putOpt("theoryAggregateSeven", "无");
                            }
                            detailstate[1] = 3;
                        }
                    } else if (selectlqcailiao.getCailiaoleixing() == 3) {
                        if(detailstate[2] == 0){//骨料2
                            if (selectlqcailiao.getShijiyongliang() != null) {
                                publicPitchList1.putOpt("factAggregateTwo", selectlqcailiao.getShijiyongliang());
                            } else {
                                publicPitchList1.putOpt("factAggregateTwo", "无");
                            }
                            if (selectlqcailiao.getTheoryNumber() != null) {
                                publicPitchList1.putOpt("theoryAggregateTwo", selectlqcailiao.getTheoryNumber());
                            } else {
                                publicPitchList1.putOpt("theoryAggregateTwo", "无");
                            }
                            detailstate[2] = 1;
                        }else {//骨料5
                            if (selectlqcailiao.getShijiyongliang() != null) {
                                publicPitchList1.putOpt("factAggregateFive", selectlqcailiao.getShijiyongliang());
                            } else {
                                publicPitchList1.putOpt("factAggregateFive", "无");
                            }
                            if (selectlqcailiao.getTheoryNumber() != null) {
                                publicPitchList1.putOpt("theoryAggregateFive", selectlqcailiao.getTheoryNumber());
                            } else {
                                publicPitchList1.putOpt("theoryAggregateFive", "无");
                            }
                            detailstate[2] = 2;
                        }

                    } else if (selectlqcailiao.getCailiaoleixing() == 4) {
                        if(detailstate[3] == 0){//骨料3
                            if (selectlqcailiao.getShijiyongliang() != null) {
                                publicPitchList1.putOpt("factAggregateThree", selectlqcailiao.getShijiyongliang());
                            } else {
                                publicPitchList1.putOpt("factAggregateThree", "无");
                            }
                            if (selectlqcailiao.getTheoryNumber() != null) {
                                publicPitchList1.putOpt("theoryAggregateThree", selectlqcailiao.getTheoryNumber());
                            } else {
                                publicPitchList1.putOpt("theoryAggregateThree", "无");
                            }
                            detailstate[3] = 1;
                        }else {//骨料6
                            if (selectlqcailiao.getShijiyongliang() != null) {
                                publicPitchList1.putOpt("factAggregateSix", selectlqcailiao.getShijiyongliang());
                            } else {
                                publicPitchList1.putOpt("factAggregateSix", "无");
                            }
                            if (selectlqcailiao.getTheoryNumber() != null) {
                                publicPitchList1.putOpt("theoryAggregateSix", selectlqcailiao.getTheoryNumber());
                            } else {
                                publicPitchList1.putOpt("theoryAggregateSix", "无");
                            }
                            detailstate[3] = 2;
                        }
                    } else if (selectlqcailiao.getCailiaoleixing() == 7 || selectlqcailiao.getCailiaoleixing() == 8) {
                        if(detailstate[6] == 0 || detailstate[7] == 0){//粉料1
                            if (selectlqcailiao.getShijiyongliang() != null) {
                                publicPitchList1.putOpt("factPowderOne", selectlqcailiao.getShijiyongliang());
                            } else {
                                publicPitchList1.putOpt("factPowderOne", "无");
                            }
                            if (selectlqcailiao.getTheoryNumber() != null) {
                                publicPitchList1.putOpt("theoryPowderOne", selectlqcailiao.getTheoryNumber());
                            } else {
                                publicPitchList1.putOpt("theoryPowderOne", "无");
                            }
                            detailstate[6] = 1;
                            detailstate[7] = 1;
                        }
                        else {//粉料2
                            if (selectlqcailiao.getShijiyongliang() != null) {
                                publicPitchList1.putOpt("factPowderTwo", selectlqcailiao.getShijiyongliang());
                            } else {
                                publicPitchList1.putOpt("factPowderTwo", "无");
                            }
                            if (selectlqcailiao.getTheoryNumber() != null) {
                                publicPitchList1.putOpt("theoryPowderTwo", selectlqcailiao.getTheoryNumber());
                            } else {
                                publicPitchList1.putOpt("theoryPowderTwo", "无");
                            }
                            detailstate[6] = 2;
                            detailstate[7] = 2;
                        }
                    } else if (selectlqcailiao.getCailiaoleixing() == 9) {
                        if(detailstate[8] == 0){//添加剂
                            if (selectlqcailiao.getShijiyongliang() != null) {
                                publicPitchList1.putOpt("factAdditive", selectlqcailiao.getShijiyongliang());
                            } else {
                                publicPitchList1.putOpt("factAdditive", "无");
                            }
                            if (selectlqcailiao.getTheoryNumber() != null) {
                                publicPitchList1.putOpt("theoryAdditive", selectlqcailiao.getTheoryNumber());
                            } else {
                                publicPitchList1.putOpt("theoryAdditive", "无");
                            }
                            detailstate[8] = 1;
                        }
                    } else if (selectlqcailiao.getCailiaoleixing() == 6) {
                        if(detailstate[5] == 0){//沥青
                            if (selectlqcailiao.getShijiyongliang() != null) {
                                publicPitchList1.putOpt("factPitch", selectlqcailiao.getShijiyongliang());
                            } else {
                                publicPitchList1.putOpt("factPitch", "无");
                            }
                            if (selectlqcailiao.getTheoryNumber() != null) {
                                publicPitchList1.putOpt("theoryPitch", selectlqcailiao.getTheoryNumber());
                            } else {
                                publicPitchList1.putOpt("theoryPitch", "无");
                            }
                            detailstate[5] = 1;
                        }
                    }
                }
            }
            if(selectlqbhzone.getYoushibi() != null){
                publicPitchList1.putOpt("factOilRockRatio",selectlqbhzone.getYoushibi());//实际油石比
            }else {
                publicPitchList1.putOpt("factOilRockRatio","无");//实际油石比
            }if(selectlqbhzone.getLlysb() != null){
                publicPitchList1.putOpt("theoryOilRock",selectlqbhzone.getLlysb());//理论油石比
            }else {
                publicPitchList1.putOpt("theoryOilRock","无");//理论油石比
            }
            publicPitchList1.putOpt("mixingName",selectshebeione.getSbname());//拌合机名称
            publicPitchList1.putOpt("mixingStationName",queryone.getDepartName());//拌合站名称
            publicPitchList1.putOpt("mixingStationId",queryone.getId());//拌合站ID
            publicPitchList1.putOpt("type",1);//拌合站类型
            publicPitchList1.putOpt("collectDate",collettimes);//采集时间
            if(selectlqbhzone.getChaobiaodengji()>0) {
                publicPitchList1.putOpt("isStandard", 1);//是否超标
            }else {
                publicPitchList1.putOpt("isStandard", 0);
            }
            publicPitchList1.putOpt("standardType",selectlqbhzone.getChaobiaodengji());//超标级别
            if(selectlqbhzone.getBeiy1() != null){
                publicPitchList1.putOpt("reserveOne",selectlqbhzone.getBeiy1());//备用1
            }else {
                publicPitchList1.putOpt("reserveOne", selectlqbhzone.getBeiy1());//备用1
            }
            if(selectlqbhzone.getBeiy2() != null){
                publicPitchList1.putOpt("reserveTwo",selectlqbhzone.getBeiy2());//备用2
            }else {
                publicPitchList1.putOpt("reserveTwo","无");//备用2
            }
            if(selectlqbhzone.getBeiy3() != null){
                publicPitchList1.putOpt("reserveThree",selectlqbhzone.getBeiy3());//备用3
            }else {
                publicPitchList1.putOpt("reserveThree","无");//备用3
            }
            publicPitchList.add(publicPitchList1);
        }
        if(publicPitchList.size()>0){
            String s1 = shandongUtil.TuisongList(ShandongUtil.TuiSong, token, ShandongUtil.publicPitchList, publicPitchList.toString());
            if(s1.equals("200")){
                //此处修改sys_config 中记录推送到那个id
                log.info(String.format("推送沥青拌合站数据成功" + id + DateUtils.now()));
            }else {
                log.info(String.format("推送沥青拌合站数据失败" + id + DateUtils.now()));
            }
        }
    }
}
