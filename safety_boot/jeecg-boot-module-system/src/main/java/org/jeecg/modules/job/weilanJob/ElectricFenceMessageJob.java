package org.jeecg.modules.job.weilanJob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.carmiles.entity.Carmiles;
import com.trtm.iot.carmiles.service.ICarmilesService;
import com.trtm.iot.clgl.entity.ClxxRealdata;
import com.trtm.iot.clgl.service.IClxxRealdataService;
import com.trtm.iot.frontDeviceWeilan.entity.FrontDeviceWeilan;
import com.trtm.iot.frontDeviceWeilan.service.IFrontDeviceWeilanService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.WeiLanUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static com.trtm.iot.util.GPSUtil.formatLnglat;
import static com.trtm.iot.util.GPSUtil.gps84_To_Gcj02;

/**
 * \* @author: Xx
 * \* Date: 2021/6/29
 * \* Time: 18:06
 * \* Description: 检测电子围栏与运输车的关系  （运输车是否超过这个围栏 超过发送信息）
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ElectricFenceMessageJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IFrontDeviceWeilanService iFrontDeviceWeilanService;
    @Autowired
    private WeiLanUtil weiLanUtil;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private ICarmilesService carmilesService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private IClxxRealdataService iClxxRealdataService;
    //风控在线GPS定时任务(运料车)
    @Autowired
    private IFrontDeviceRealdataService realService;
    private static final String dataurl = "http://open.figps.com/api/";
    private static final String authstr="{" +
            "\"appid\":\"17398007785\"," +
            "\"time\":1635386457," +
            "\"signature\":\"193386bbc84e8a45d7599706e865bc31\"" +
            "}";

    private static final String statusurl = "device/status?accessToken=%s&account=17398007785";

    private String accessToken = "";

    private void doAuth() {
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(HttpUtil.post(dataurl+"auth", authstr));
        if (jsonObject.getInt("code") == 0) {
            accessToken = jsonObject.getStr("accessToken");
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //风控在线GPS定时任务(运料车)
        if (StrUtil.isBlank(accessToken)) {
            doAuth();
        }
        if (StrUtil.isNotBlank(accessToken)) {
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(HttpUtil.get(dataurl+String.format(statusurl,accessToken)));
            if (jsonObject != null && jsonObject.getInt("code") == 0) {
                JSONArray jsarray = jsonObject.getJSONArray("data");
                System.out.println(jsarray);
                for (int i = 0; i < jsarray.size(); i++) {
                    cn.hutool.json.JSONObject json = jsarray.getJSONObject(i);
                    realService.updateFrontdeviceData(json);
                    /*if (realService.updateFrontdeviceData(json)) {
                        frontService.insertGpsdata(json);
                    }*/
                }
            } else {
                accessToken = "";
            }
        }

        //电子围栏预警
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.FRONT_DEVICE_WEILAN_YUJING);//创建圆形电子围栏
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到创建电子围栏定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<FrontDeviceWeilan> querylist = iFrontDeviceWeilanService.querylist(3,0,0);//需要检测的围栏数据
        if (null == querylist || querylist.size() == 0) {
            log.info(String.format("暂无需要检测的围栏数据"));
            return;
        }
        for (FrontDeviceWeilan frontDeviceWeilan : querylist) {
//            String sid = frontDeviceWeilan.getSid();
//            Integer tid = frontDeviceWeilan.getTid();
            Integer radius = frontDeviceWeilan.getRadius();Integer radius2 = frontDeviceWeilan.getRadius2();// 二级围栏
//            Integer gfid = frontDeviceWeilan.getGfid();
            String shebeiNo = frontDeviceWeilan.getShebeiNo();
            String center = frontDeviceWeilan.getCenter();
            String[] split = center.split(",");
            Double lng2=null;
            Double lat2=null;
            lng2= Double.valueOf(split[0]);
            lat2= Double.valueOf(split[1]);
            ClxxRealdata queryone = iClxxRealdataService.queryone(shebeiNo);//查询实时定位
            if(queryone!=null){
                Double latitude = queryone.getLatitude();
                Double longitude = queryone.getLongitude();
//                double v = formatLnglat(longitude);
//                double v1 = formatLnglat(latitude);
                double[] doubles = gps84_To_Gcj02(latitude, longitude);
                double latitude1 = doubles[0];
                double longitude1 = doubles[1];
//                String result = String .format("%.6f", latitude);
//                String result1 = String .format("%.6f", longitude);
//                String center=result1+","+result;
                Double distance = weiLanUtil.getDistance(longitude1, latitude1, lng2, lat2);
                // 超出二级围栏
                if(radius2 != null && distance>radius2){

                    ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
                    if (selectshebeione != null){
                        String sysOrgCode = selectshebeione.getSysOrgCode();
                        List<BhzPhone> bhzPhones = bhzPhoneService.selectBhzPhoneList(sysOrgCode, 21);// 21表示二级围栏预计
                        if(bhzPhones.size()>0){
                            for (BhzPhone bhzPhone : bhzPhones) {
                                try {
                                    String phones = bhzPhone.getPhones();
                                    JSONObject obj = new JSONObject();
                                    String product_datetime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                                    obj.put("sbname", selectshebeione.getSbname());
                                    obj.put("time", product_datetime);
                                    obj.put("content", "此车辆已经超出二级围栏范围");
                                    log.info(String.format("设备已经超出围栏范围"));
                                    SysMessage sysMessage = new SysMessage();
                                    sysMessage.setEsTitle("电子围栏监测");
                                    sysMessage.setEsType("1");//短信类型  1短信
                                    sysMessage.setEsReceiver(phones);//手机号
                                    sysMessage.setEsContent(obj.toString());//短信内容
                                    sysMessage.setEsSendStatus("0");//推送状态0未推送
                                    sysMessage.setEsSendNum(0);//推送总次数
                                    sysMessage.setCreateTime(new Date());
                                    sysMessageService.save(sysMessage);
                                } catch (Exception e) {
                                    log.info("报错了，");
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            log.info(String.format("此设备不需要发送短信检测围栏数据"));
                        }
                    }
                    ClxxRealdata clxxRealdata=new ClxxRealdata();
                    clxxRealdata.setShebeiNo(queryone.getShebeiNo());
                    clxxRealdata.setId(queryone.getId());
                    // 修改状态为5：超出二级电子围栏
                    clxxRealdata.setStatus(5);
                    boolean update = iClxxRealdataService.updateById(clxxRealdata);
                    if(update){
                        log.info(String.format("修改设备状态成功！"));
                    }else{
                        log.info(String.format("修改设备状态失败！"));
                    }

                }// 超出拌合站范围
                else if(distance>radius){
                    ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
                    if (selectshebeione != null){
                        String sysOrgCode = selectshebeione.getSysOrgCode();
                        List<BhzPhone> bhzPhones = bhzPhoneService.selectBhzPhoneList(sysOrgCode, 7);
                        if(bhzPhones.size()>0){
                            for (BhzPhone bhzPhone : bhzPhones) {
                                try {
                                    String phones = bhzPhone.getPhones();
                                    JSONObject obj = new JSONObject();
                                    String product_datetime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                                    obj.put("shebeiname", selectshebeione.getSbname());
                                    obj.put("time", product_datetime);
                                    obj.put("strengthrank", "无");
                                    obj.put("pourelocation", "无");
                                    obj.put("finalcontent", "此车辆已经超出围栏范围");
                                    log.info(String.format("设备已经超出围栏范围"));
                                    SysMessage sysMessage = new SysMessage();
                                    sysMessage.setEsTitle("电子围栏监测");
                                    sysMessage.setEsType("1");//短信类型  1短信
                                    sysMessage.setEsReceiver(phones);//手机号
                                    sysMessage.setEsContent(obj.toString());//短信内容
                                    sysMessage.setEsSendStatus("0");//推送状态0未推送
                                    sysMessage.setEsSendNum(0);//推送总次数
                                    sysMessage.setCreateTime(new Date());
                                    sysMessageService.save(sysMessage);
                                } catch (Exception e) {
                                    log.info("报错了，");
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            log.info(String.format("此设备不需要发送短信检测围栏数据"));
                        }
                    }
                    ClxxRealdata clxxRealdata=new ClxxRealdata();
                    clxxRealdata.setShebeiNo(queryone.getShebeiNo());
                    clxxRealdata.setId(queryone.getId());
                    clxxRealdata.setStatus(1);
                    boolean update = iClxxRealdataService.updateById(clxxRealdata);
                    if(update){
                        log.info(String.format("修改设备状态成功！"));
                    }else{
                        log.info(String.format("修改设备状态失败！"));
                    }
                }else{
                    log.info(String.format("设备在围栏范围内!"));
                    ClxxRealdata queryone1 = iClxxRealdataService.queryone(shebeiNo);
                    // 如果状态为接料中则不进行修改车辆状态
                    if(StringUtils.isNotBlank(queryone1.getStatus().toString()) && queryone1.getStatus() == 4){
                         continue;
                    }
                    ClxxRealdata clxxRealdata=new ClxxRealdata();
                    clxxRealdata.setShebeiNo(queryone1.getShebeiNo());
                    clxxRealdata.setId(queryone1.getId());
                    clxxRealdata.setStatus(3);
                    boolean update = iClxxRealdataService.updateById(clxxRealdata);
                    if(update){
                        log.info(String.format("修改设备状态成功！"));
                    }else{
                        log.info(String.format("修改设备状态失败！"));
                    }
                }
            }else {
                log.info(String.format("未查询到此设备！！"));
            }
        }
    }
}
