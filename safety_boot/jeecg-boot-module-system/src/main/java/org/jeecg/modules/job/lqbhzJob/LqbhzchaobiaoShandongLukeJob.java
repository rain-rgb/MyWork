package org.jeecg.modules.job.lqbhzJob;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
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
 * \* Date: 2021/6/7
 * \* Description:山东陆科推送沥青拌合站超标数据(实时数据包含所有的合格不合格数据 注：推送的数据已经判断过超不超标)
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LqbhzchaobiaoShandongLukeJob implements Job {
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LQBHZCHAOBIAO_TUISONG_SHANDONGLIKE);//沥青拌合站数据推送山东陆科
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
        List<BhzLqBases> selectlqbhzchaobiaoones = bhzLqBasesService.selectlqbhzchaobiaoList(curid, 1,sbjnolist);
        if (null == selectlqbhzchaobiaoones || selectlqbhzchaobiaoones.size() == 0) {
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
        JSONArray publicPitchMoreList=new JSONArray();//传的参数的格式为[{},{}]
        for (BhzLqBases selectlqbhchaobiaozone : selectlqbhzchaobiaoones) {
            String sbjno = selectlqbhchaobiaozone.getShebeibianhao();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(sbjno);//设备信息
            String guid = selectlqbhchaobiaozone.getGuid();
            List<BhzLqCailiao> selectcailiaoones = bhzLqCailiaoService.selectcailiaolist(guid);
            id=selectlqbhchaobiaozone.getId();
            JSONObject publicPitchMoreList1 =new JSONObject();
            publicPitchMoreList1.putOpt("id",selectlqbhchaobiaozone.getId());//id
            publicPitchMoreList1.putOpt("mixerId",selectlqbhchaobiaozone.getId());//沥青实时数据编号id
            publicPitchMoreList1.putOpt("standardType",selectlqbhchaobiaozone.getChaobiaodengji());//超标类型
            publicPitchMoreList1.putOpt("deviceNumber",selectlqbhchaobiaozone.getShebeibianhao());//设备编号
            publicPitchMoreList1.putOpt("proCode",selectshebeione.getProcode());//项目编码
//            publicPitchMoreList1.putOpt("resultDate",)//结果时间
//            publicPitchMoreList1.putOpt("oilRockbiRatio",)//油石比
            if(selectlqbhchaobiaozone.getBanheshijian() != null){
                publicPitchMoreList1.putOpt("mixingMinute",selectlqbhchaobiaozone.getBanheshijian());//搅拌时长
            }else {
                publicPitchMoreList1.putOpt("mixingMinute","无");//搅拌时长
            }
            if(selectlqbhchaobiaozone.getLiqingwd() != null){
                publicPitchMoreList1.putOpt("pitchTemperature",selectlqbhchaobiaozone.getLiqingwd());//沥青温度
            }else {
                publicPitchMoreList1.putOpt("pitchTemperature","无");//沥青温度
            }
            if(selectlqbhchaobiaozone.getGuliaowd() != null){
                publicPitchMoreList1.putOpt("aggregateTemperature",selectlqbhchaobiaozone.getGuliaowd());//骨料温度
            }else {
                publicPitchMoreList1.putOpt("aggregateTemperature","无");//骨料温度
            }
            if(selectcailiaoones.size() > 0) {
                int[] detailstate = {0,0,0,0,0,0,0,0,0,0};
                for (BhzLqCailiao selectlqcailiao : selectcailiaoones) {
                    if (selectlqcailiao.getCailiaoleixing() == 2) {
                        if(detailstate[1] == 0){//骨料1
                            if (selectlqcailiao.getChaobiaodengji() != null && selectlqcailiao.getChaobiaodengji() > 0) {
                                publicPitchMoreList1.putOpt("aggregateOne", (selectlqcailiao.getChaobiaodengji()).toString());
                            }
                            detailstate[1] = 1;
                        }else if(detailstate[1]==1){//骨料4
                            if (selectlqcailiao.getChaobiaodengji() != null && selectlqcailiao.getChaobiaodengji() > 0) {
                                publicPitchMoreList1.putOpt("aggregateFour", (selectlqcailiao.getChaobiaodengji()).toString());
                            }
                            detailstate[1] = 2;
                        }else {//骨料7
                            if (selectlqcailiao.getChaobiaodengji() != null && selectlqcailiao.getChaobiaodengji() > 0) {
                                publicPitchMoreList1.putOpt("aggregateSeven", (selectlqcailiao.getChaobiaodengji()).toString());
                            }
                            detailstate[1] = 3;
                        }
                    } else if (selectlqcailiao.getCailiaoleixing() == 3) {
                        if(detailstate[2] == 0){//骨料2
                            if (selectlqcailiao.getChaobiaodengji() != null && selectlqcailiao.getChaobiaodengji() > 0) {
                                publicPitchMoreList1.putOpt("aggregateTwo", (selectlqcailiao.getChaobiaodengji()).toString());
                            }
                            detailstate[2] = 1;
                        }else {//骨料5
                            if (selectlqcailiao.getChaobiaodengji() != null && selectlqcailiao.getChaobiaodengji() > 0) {
                                publicPitchMoreList1.putOpt("aggregateFive", (selectlqcailiao.getChaobiaodengji()).toString());
                            }
                            detailstate[2] = 2;
                        }
                    } else if (selectlqcailiao.getCailiaoleixing() == 4) {
                        if(selectlqcailiao.getChaobiaodengji() != null && selectlqcailiao.getChaobiaodengji() > 0){//骨料3
                            if (selectlqcailiao.getShijiyongliang() != null) {
                                publicPitchMoreList1.putOpt("aggregateThree", (selectlqcailiao.getChaobiaodengji()).toString());
                            }
                            detailstate[3] = 1;
                        }else {//骨料6
                            if (selectlqcailiao.getChaobiaodengji() != null && selectlqcailiao.getChaobiaodengji() > 0) {
                                publicPitchMoreList1.putOpt("aggregateSix", (selectlqcailiao.getChaobiaodengji()).toString());
                            }
                            detailstate[3] = 2;
                        }
                    } else if (selectlqcailiao.getCailiaoleixing() == 7 || selectlqcailiao.getCailiaoleixing() == 8) {
                        if(detailstate[6] == 0 || detailstate[7] == 0){//粉料1
                            if (selectlqcailiao.getChaobiaodengji() != null && selectlqcailiao.getChaobiaodengji() > 0) {
                                publicPitchMoreList1.putOpt("powderOne", (selectlqcailiao.getChaobiaodengji()).toString());
                            }
                            detailstate[6] = 1;
                            detailstate[7] = 1;
                        }
                        else {//粉料2
                            if (selectlqcailiao.getChaobiaodengji() != null && selectlqcailiao.getChaobiaodengji() > 0) {
                                publicPitchMoreList1.putOpt("powderTwo", (selectlqcailiao.getChaobiaodengji()).toString());
                            }
                            detailstate[6] = 2;
                            detailstate[7] = 2;
                        }
                    } else if (selectlqcailiao.getCailiaoleixing() == 9) {
                        if(detailstate[8] == 0){//添加剂
                            if (selectlqcailiao.getChaobiaodengji() != null && selectlqcailiao.getChaobiaodengji() > 0) {
                                publicPitchMoreList1.putOpt("additive", (selectlqcailiao.getChaobiaodengji()).toString());
                            }
                            detailstate[8] = 1;
                        }
                    } else if (selectlqcailiao.getCailiaoleixing() == 6) {
                        if(detailstate[5] == 0){//沥青
                            if (selectlqcailiao.getChaobiaodengji() != null && selectlqcailiao.getChaobiaodengji() > 0) {
                                publicPitchMoreList1.putOpt("pitch", (selectlqcailiao.getChaobiaodengji()).toString());
                            }
                            detailstate[5] = 1;
                        }
                    }
                }
            }
            publicPitchMoreList.add(publicPitchMoreList1);
        }
        if(publicPitchMoreList.size()>0){
            String s1 = shandongUtil.TuisongList(ShandongUtil.TuiSong, token, ShandongUtil.publicPitchMoreList, publicPitchMoreList.toString());
            if(s1.equals("200")){
                //此处修改sys_config 中记录推送到那个id
                log.info(String.format("推送沥青拌合站超标数据成功" + id + DateUtils.now()));
            }else {
                log.info(String.format("推送沥青拌合站超标数据失败" + id + DateUtils.now()));
            }
        }
    }
}
