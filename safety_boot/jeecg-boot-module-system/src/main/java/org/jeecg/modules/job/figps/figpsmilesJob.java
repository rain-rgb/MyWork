package org.jeecg.modules.job.figps;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.carmiles.entity.Carmiles;
import com.trtm.iot.carmiles.service.ICarmilesService;
import com.trtm.iot.clgl.service.IFrontDeviceHistorydataService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.september.entity.September;
import com.trtm.iot.september.service.ISeptemberService;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.query.QueryGenerator;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 风控在线GPS定时任务(运料车)--统计每天的里程和运行时长
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class figpsmilesJob implements Job {

    @Autowired
    private IFrontDeviceHistorydataService frontService;

    @Autowired
    private ICarmilesService carmilesService;

    @Autowired
    private ISeptemberService septemberService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Autowired
    private IFrontDeviceRealdataService realService;
    private static final String dataurl = "http://open.figps.com/api/";
    private static final String authstr="{" +
            "\"appid\":\"17398007785\"," +
            "\"time\":1635386457," +
            "\"signature\":\"193386bbc84e8a45d7599706e865bc31\"" +
            "}";

    private static final String statusurl = "device/miles?accessToken=%s&startTime=%d&endTime=%d&imei=%s";
    private static final String historyurl = "device/track/history?accessToken=%s&startTime=%s&endTime=%s&imei=%s&coordType=gcj02&filterNoise=1&rectifyTrack=1";

    private String accessToken = "";

    private void doAuth() {
        JSONObject jsonObject = JSONUtil.parseObj(HttpUtil.post(dataurl+"auth", authstr));
        if (jsonObject.getInt("code") == 0) {
            accessToken = jsonObject.getStr("accessToken");
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (StrUtil.isBlank(accessToken)) {
            doAuth();
        }
        if (StrUtil.isNotBlank(accessToken)) {
            QueryWrapper<FrontDeviceRealdata> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("device_type","C").eq("isWireless",1).or().eq("isWireless",0);
            //queryWrapper.inSql("shebei_no","select shebei_no from front_device_realdata where device_type='C' and (isWireless=1 or isWireless=0)");
            List<FrontDeviceRealdata> realList = realService.list( queryWrapper);
            DateTime dt = DateUtil.beginOfDay(DateUtil.yesterday());
            //里程
            for (FrontDeviceRealdata real:realList) {
                JSONObject jsonObject = JSONUtil.parseObj(HttpUtil.get(dataurl+String.format(statusurl,accessToken,
                        DateUtil.beginOfDay(DateUtil.yesterday()).getTime()/1000,
                        DateUtil.endOfDay(DateUtil.yesterday()).getTime()/1000,real.getShebeiNo())));
                if (jsonObject != null && jsonObject.getInt("code") == 0) {
                    if (jsonObject.getInt("runTime")>0) {
                        QueryWrapper<Carmiles> carmilesQueryWrapper = new QueryWrapper<>();
                        carmilesQueryWrapper.eq("shebei_no", real.getShebeiNo()).eq("rundate",dt);
                        Carmiles carmiles = carmilesService.getOne(carmilesQueryWrapper);
                        if (null == carmiles) {
                            carmiles = new Carmiles();
                            carmiles.setShebeiNo(real.getShebeiNo());
                            carmiles.setRundate(dt);
                        }
                        carmiles.setMiles(jsonObject.getFloat("miles"));
                        carmiles.setRuntime(jsonObject.getInt("runTime"));
                        carmiles.setJobruntime(DateUtil.date());
                        carmilesService.saveOrUpdate(carmiles);
                          }
                    pushandreturnService.saveData(real.getId(), String.valueOf(jsonObject),"获取里程",dataurl+String.format(statusurl,accessToken, DateUtil.beginOfDay(DateUtil.yesterday()).getTime()/1000, DateUtil.endOfDay(DateUtil.yesterday()).getTime()/1000,real.getShebeiNo()));

                } else {
                    pushandreturnService.saveData(real.getId(), String.valueOf(jsonObject),"获取里程",dataurl+String.format(statusurl,accessToken, DateUtil.beginOfDay(DateUtil.yesterday()).getTime()/1000, DateUtil.endOfDay(DateUtil.yesterday()).getTime()/1000,real.getShebeiNo()));
                    continue;
                }
            }
            //历史轨迹
            for (FrontDeviceRealdata real:realList) {
                JSONObject jsonObject = JSONUtil.parseObj(HttpUtil.get(dataurl+String.format(historyurl,accessToken,
                        DateUtil.beginOfDay(DateUtil.yesterday()).getTime()/1000,
                        DateUtil.endOfDay(DateUtil.yesterday()).getTime()/1000,real.getShebeiNo())));
                if (jsonObject != null && jsonObject.getInt("code") == 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    List<September> septemberList = new ArrayList<September>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jso = jsonArray.getJSONObject(i);
                        September september = new September();
                        september.setImei(real.getShebeiNo());
                        september.setLat(jso.getStr("lat"));
                        september.setLng(jso.getStr("lng"));
                        september.setSpeed(jso.getInt("speed"));
                        september.setCourse(jso.getInt("course"));
                        september.setGpstime(jso.getInt("gpsTime"));
                        september.setPositiontype(jso.getStr("positionType"));
                        septemberList.add(september);
                    }
                    if (septemberList.size()>0) {
                        //List<September> septemberList = JSONUtil.toList(jsonArray,September.class);
                        septemberService.saveBatch(septemberList);
                    }

                } else {
                    break;
                }
            }


        }
    }
}
