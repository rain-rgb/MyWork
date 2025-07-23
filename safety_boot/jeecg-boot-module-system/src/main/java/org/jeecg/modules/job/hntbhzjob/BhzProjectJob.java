package org.jeecg.modules.job.hntbhzjob;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
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

/**
 * \* @author: Xx
 * \* Date: 2021/6/3
 * \* Time: 9:40
 * \* Description:推送拌合站项目以及设备信息山东陆科
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BhzProjectJob implements Job {
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HNTBHZ_TUISONG_PROJECT);//拌合站项目以及设备信息山东陆科
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到拌合站推送山东陆科定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        if (StrUtil.isBlank(shebeilist)) {
            log.info(String.format("没有配置要传输拌合站的设备" + DateUtils.now()));
            return;
        }
        String s = shandongUtil.GetTokenpost(ShandongUtil.GetToken, ShandongUtil.MORENToken);
        String token=null;
        if(s.equals("200")){
            //重新刷新获取的token
            token = (String) redisUtil.get("ShanDongLuKeToken");
        }else{
            //未刷新之前的token
            token=ShandongUtil.MORENToken;
        }
        String[] split = shebeilist.split(",");
        for (int i=0;i<split.length;i++) {
            String shebeiNo=split[i];
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);//设备信息
            if (null == selectshebeione) {
                log.info(String.format("暂无砼拌合站的设备" + DateUtils.now()));
                continue;
            }
            SysDepart queryone = sysDepartService.queryone(selectshebeione.getSysOrgCode());//拌合站信息
            Integer status1 = selectshebeione.getStatus1(); //状态 是否推送 0 未推送 1项目信息已经推送 2 拌合站信息已经推送 3 设备信息已经推送  10 推送项目信息异常 20推送拌合站信息异常 30推送设备信息异常
            if(status1==0){
                //推送项目信息
                JSONArray publicProjectList=new JSONArray();//传的参数的格式为[{},{}]
                JSONObject publicProjectList1 =new JSONObject();
                publicProjectList1.putOpt("id",selectshebeione.getId());
                publicProjectList1.putOpt("proName",selectshebeione.getProjectname());
                publicProjectList1.putOpt("proCode",selectshebeione.getProcode());
                publicProjectList.add(publicProjectList1);
                //推送项目信息
                String s1 = shandongUtil.TuisongList(ShandongUtil.TuiSong, token, ShandongUtil.publicProjectList, publicProjectList.toString());
                if(s1.equals("200")){
                    shebeiInfoService.updatestatus1(shebeiNo,1);//修改设备表中的推送状态为1 推送项目信息成功
                    log.info(String.format("推送项目信息成功" +selectshebeione.getSbname()+DateUtils.now()));
                    //推送拌合站信息
                    JSONArray publicMixingStationList=new JSONArray();//传的参数的格式为[{},{}]
                    JSONObject publicMixingStationList1 =new JSONObject();
                    publicMixingStationList1.putOpt("id",queryone.getId());
                    publicMixingStationList1.putOpt("mixingStationName",queryone.getDepartName());
                    publicMixingStationList1.putOpt("proCode",selectshebeione.getProcode());
                    publicMixingStationList1.putOpt("type",0);
                    publicMixingStationList.add(publicMixingStationList1);
                    String s2 = shandongUtil.TuisongList(ShandongUtil.TuiSong, token, ShandongUtil.publicMixingStationList, publicMixingStationList.toString());
                    if(s2.equals("200")){
                        shebeiInfoService.updatestatus1(shebeiNo,2);//修改设备表中的推送状态为2 推送拌合站信息成功
                        log.info(String.format("推送拌合站信息成功" +DateUtils.now()));
                        JSONArray publicMixingList=new JSONArray();//传的参数的格式为[{},{}]
                        JSONObject publicMixingList1 =new JSONObject();
                        publicMixingList1.putOpt("id",selectshebeione.getId());
                        publicMixingList1.putOpt("mixingName",selectshebeione.getSbname());
                        publicMixingList1.putOpt("mixingStationName",queryone.getDepartName());
                        publicMixingList1.putOpt("mixingShortName",selectshebeione.getSbjsimplename());
                        publicMixingList1.putOpt("unitType","无");
                        publicMixingList1.putOpt("type",0);
                        publicMixingList1.putOpt("proCode",selectshebeione.getProcode());
                        publicMixingList1.putOpt("mixingStationId",queryone.getId());
                        publicMixingList1.putOpt("deviceNumber",selectshebeione.getSbjno());
                        publicMixingList.add(publicMixingList1);
                        String s3 = shandongUtil.TuisongList(ShandongUtil.TuiSong, token, ShandongUtil.publicMixingList, publicMixingList.toString());
                        if(s3.equals("200")){
                            shebeiInfoService.updatestatus1(shebeiNo,3);//修改设备表中的推送状态为3 推送设备信息成功
                            log.info(String.format("推送拌合机信息成功" +DateUtils.now()));
                        }else{
                            shebeiInfoService.updatestatus1(shebeiNo,30);//修改设备表中的推送状态为30 推送设备信息异常
                        }
                    }else{
                        shebeiInfoService.updatestatus1(shebeiNo,20);//修改设备表中的推送状态为20 推送拌合站信息异常
                    }

                }else{
                    shebeiInfoService.updatestatus1(shebeiNo,10);//修改设备表中的推送状态为10 推送项目信息异常
                    log.info(String.format("推送项目信息失败" +selectshebeione.getSbname()+DateUtils.now()));
                }
            }else if(status1==1){//项目信息不用在去推送
                //推送拌合站信息
                JSONArray publicMixingStationList=new JSONArray();//传的参数的格式为[{},{}]
                JSONObject publicMixingStationList1 =new JSONObject();
                publicMixingStationList1.putOpt("id",queryone.getId());
                publicMixingStationList1.putOpt("mixingStationName",queryone.getDepartName());
                publicMixingStationList1.putOpt("proCode",selectshebeione.getProcode());
                publicMixingStationList1.putOpt("type",0);
                publicMixingStationList.add(publicMixingStationList1);
                String s2 = shandongUtil.TuisongList(ShandongUtil.TuiSong, token, ShandongUtil.publicMixingStationList, publicMixingStationList.toString());
                if(s2.equals("200")){
                    shebeiInfoService.updatestatus1(shebeiNo,2);//修改设备表中的推送状态为2 推送拌合站信息成功
                    log.info(String.format("推送拌合站信息成功" +DateUtils.now()));
                    JSONArray publicMixingList=new JSONArray();//传的参数的格式为[{},{}]
                    JSONObject publicMixingList1 =new JSONObject();
                    publicMixingList1.putOpt("id",selectshebeione.getId());
                    publicMixingList1.putOpt("mixingName",selectshebeione.getSbname());
                    publicMixingList1.putOpt("mixingStationName",queryone.getDepartName());
                    publicMixingList1.putOpt("mixingShortName",selectshebeione.getSbjsimplename());
                    publicMixingList1.putOpt("unitType","无");
                    publicMixingList1.putOpt("type",0);
                    publicMixingList1.putOpt("proCode",selectshebeione.getProcode());
                    publicMixingList1.putOpt("mixingStationId",queryone.getId());
                    publicMixingList1.putOpt("deviceNumber",selectshebeione.getSbjno());
                    publicMixingList.add(publicMixingList1);
                    String s3 = shandongUtil.TuisongList(ShandongUtil.TuiSong, token, ShandongUtil.publicMixingList, publicMixingList.toString());
                    if(s3.equals("200")){
                        shebeiInfoService.updatestatus1(shebeiNo,3);//修改设备表中的推送状态为3 推送设备信息成功
                        log.info(String.format("推送拌合机信息成功" +DateUtils.now()));
                    }else{
                        shebeiInfoService.updatestatus1(shebeiNo,30);//修改设备表中的推送状态为30 推送设备信息异常
                    }
                }else{
                    shebeiInfoService.updatestatus1(shebeiNo,20);//修改设备表中的推送状态为20 推送拌合站信息异常
                }
            }else if(status1==2){//不同推送拌合站信息
                JSONArray publicMixingList=new JSONArray();//传的参数的格式为[{},{}]
                JSONObject publicMixingList1 =new JSONObject();
                publicMixingList1.putOpt("id",selectshebeione.getId());
                publicMixingList1.putOpt("mixingName",selectshebeione.getSbname());
                publicMixingList1.putOpt("mixingStationName",queryone.getDepartName());
                publicMixingList1.putOpt("mixingShortName",selectshebeione.getSbjsimplename());
                publicMixingList1.putOpt("unitType","无");
                publicMixingList1.putOpt("type",0);
                publicMixingList1.putOpt("proCode",selectshebeione.getProcode());
                publicMixingList1.putOpt("mixingStationId",queryone.getId());
                publicMixingList1.putOpt("deviceNumber",selectshebeione.getSbjno());
                publicMixingList.add(publicMixingList1);
                String s3 = shandongUtil.TuisongList(ShandongUtil.TuiSong, token, ShandongUtil.publicMixingList, publicMixingList.toString());
                if(s3.equals("200")){
                    shebeiInfoService.updatestatus1(shebeiNo,3);//修改设备表中的推送状态为3 推送设备信息成功
                    log.info(String.format("推送拌合机信息成功" +DateUtils.now()));
                }else{
                    shebeiInfoService.updatestatus1(shebeiNo,30);//修改设备表中的推送状态为30 推送设备信息异常
                }
            }else{
                log.info(String.format("项目信息等已经推送过" +selectshebeione.getSbname()+DateUtils.now()));
            }
        }

    }
}
