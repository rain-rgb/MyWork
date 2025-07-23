package org.jeecg.modules.job.JiaQiaoJijob;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.JsonArray;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.trtm.iot.lmd.service.IDeviceCraneHistorydataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/11/22
 * \* Time: 13:41
 * \* Description:宁波市特检察院(通用门式起重机)550T 1100T   2021111202    2021111201
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JiaQiaoJiJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDeviceCraneHistorydataService deviceCraneHistorydataService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.NB_JIAQIAOJI);//通用门式起重机30
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到通用门式起重机定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输通用门式起重机的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1= Arrays.asList(split);
        List<DeviceCraneHistorydata> selecthntbhzones = deviceCraneHistorydataService.selectLists(strsToList1,curid);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无通用门式起重机未推送数据" + DateUtils.now()));
            return;
        }
        int id=0;
        String unifiedCode=null;
        JSONArray jsonArray=new JSONArray();
        for (DeviceCraneHistorydata selecthntbhzone : selecthntbhzones) {
            id=selecthntbhzone.getId();
            String deviceCode = selecthntbhzone.getDeviceCode();
            JSONObject publicPitchList1 = new JSONObject();
            publicPitchList1.set("faultRecord",0);//当前故障状态，0 未故障/1 故障中
            Double mainHookload = selecthntbhzone.getMainHookload();//主钩吊重
            Double reservedVicehookload = selecthntbhzone.getReservedVicehookload();//副钩吊重
            publicPitchList1.set("liftCapacity",mainHookload+reservedVicehookload);//当前载荷，主钩起重量，如有多主钩，则为 所有主钩起重量之和，单位 t
            Double mainHookheight = selecthntbhzone.getMainHookheight();
            publicPitchList1.set("liftingPosition",mainHookheight);//起升位置，单位 m
            Double smallCarroute = selecthntbhzone.getSmallCarroute();
            publicPitchList1.set("carPosition",smallCarroute);//小车位置，单位 m
            Double bigCarroute = selecthntbhzone.getBigCarroute();
            publicPitchList1.set("cartPosition",bigCarroute);//大车位置，单位 m
            if(mainHookload>0||reservedVicehookload>0){
                publicPitchList1.set("equState",1);//起重机运行状态，0 未运行/1 运行中
            }else{
                publicPitchList1.set("equState",0);//起重机运行状态，0 未运行/1 运行中
            }
            Double windSpeed = selecthntbhzone.getWindSpeed();
            publicPitchList1.set("windSpeed",windSpeed);//当前风速，单位米/秒，起升高度大于 50 米 以上必传
            publicPitchList1.set("waaDevice",1);//防风抗滑装置状态，0 未运行/1 运行中
            Date cranedate = selecthntbhzone.getCranedate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(cranedate);
            publicPitchList1.set("recordingTime",format);//记录时间，格式：yyyy-MM-dd HH:mm:ss
            jsonArray.add(publicPitchList1);
            if(deviceCode.equals("2021111201")){
                unifiedCode="360806";
            }
        }
        String  nBaccessToken = jobUtil.GETNingBoQzj();
        if(null!=nBaccessToken){
            JSONObject sendData = JSONUtil.createObj();
            sendData.set("identifier","craneRecord");
            sendData.set("accessToken",nBaccessToken);
            sendData.set("unifiedCode",unifiedCode);
            sendData.set("deviceType","3");
            sendData.set("data",jsonArray);
            Integer integer = jobUtil.GETNingBoQzjTuiSong(sendData);
            if(integer==200){
                log.info(String.format("宁波市特检察院(通用门式起重机+360806)数据推送成功!" + id+"Json数据"+sendData));
            }else{
                log.info(String.format("宁波市特检察院(通用门式起重机+360806)数据推送失败!" + id+"Json数据"+sendData));
            }
        }else{
            log.info(String.format("宁波市特检察院(通用门式起重机+360806)获取token失败!"));
        }

        sysConfigService.updateSysConfig(JobUtil.NB_JIAQIAOJI, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
