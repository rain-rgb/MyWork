package org.jeecg.modules.job.fkclJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.carmiles.entity.Carmiles;
import com.trtm.iot.carmiles.service.ICarmilesService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;
import com.trtm.iot.wbshebeireal.entity.WbshebeiReal;
import com.trtm.iot.wbshebeireal.service.IWbshebeiRealService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 设备实施数据更新
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class shebeilbJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IFrontDeviceRealdataService frontDeviceRealdataService;
    @Autowired
    private ICarmilesService carmilesService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IWbshebeiRealService wbshebeiRealService;

    public static String URL = "http://www.iotlock.vip/mt-api/device/repertory/apiFindByPage";
    public static String USERNAME = "hzgx";
    public static String PASSWORD = "dc483e80a7a0bd9ef71d8cf973673924";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJQS_SBLB);//金途车联,车辆实时信息更新
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到设备列表接口,车辆实时信息更新定时任务的配置信息" + DateUtils.now());
            return;
        }
        JSONObject sendObject = JSONUtil.createObj();
        sendObject.set("username", USERNAME);
        sendObject.set("password", PASSWORD);
        sendObject.set("pageNum", 1);
        sendObject.set("pageSize", 10000);
        JSONObject code = getCode(sendObject, URL);
        Object data = code.get("data");
        JSONObject jsonObject = new JSONObject(data);
        Object list = jsonObject.get("list");
        JSONArray jsonObject1 = new JSONArray(list);
        System.out.println(jsonObject1);
        for (Object object :jsonObject1){
            WbshebeiReal wbshebeiReal = new WbshebeiReal();
            JSONObject o = new JSONObject(object);
            String come_time = o.get("come_time").toString();//时间
            String substring = come_time.substring(1, come_time.length() - 1);
            String[] split = substring.split(",");
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                if (Integer.parseInt(s) < 10){
                    s = "0" + s;
                }
                split[i] = s;
            }
            String time  = null;
            if (split.length < 6){
                time  = split[0] +"-"+ split[1] +"-"+ split[2] +" "+split[3]+":"+split[4]+":00";
            }else {
                time  = split[0] +"-"+ split[1] +"-"+ split[2] +" "+split[3]+":"+split[4]+":"+split[5];
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date parse = simpleDateFormat.parse(time);
                wbshebeiReal.setTime(parse);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String device_name = (String) o.get("device_name");//设备名称
            String sn = (String) o.get("sn");//设备编号
            String latitude = (String) o.get("latitude");
            String longitude = (String) o.get("longitude");
            wbshebeiReal.setTeid(sn);
            wbshebeiReal.setLat(latitude);
            wbshebeiReal.setLng(longitude);
            QueryWrapper<WbshebeiReal> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("teid",sn);
            WbshebeiReal one = wbshebeiRealService.getOne(queryWrapper);
            if (one != null){
                wbshebeiReal.setId(one.getId());
                wbshebeiRealService.updateById(wbshebeiReal);
            }else {
                wbshebeiRealService.save(wbshebeiReal);
            }
        }
    }

    public static JSONObject getCode(JSONObject sendObject, String url) {
        String body = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(String.valueOf(sendObject))
                .execute()
                .body();
        System.out.println(body);
        System.out.println(new JSONObject(body));
        return new JSONObject(body);
    }
}