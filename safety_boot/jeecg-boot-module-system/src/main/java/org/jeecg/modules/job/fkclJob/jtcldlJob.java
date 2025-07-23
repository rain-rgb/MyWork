package org.jeecg.modules.job.fkclJob;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.carmiles.entity.Carmiles;
import com.trtm.iot.carmiles.service.ICarmilesService;
import com.trtm.iot.syj.entity.FYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.binhuaijob.binhuaiUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.junit.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 设备实施数据更新
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class jtcldlJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IFrontDeviceRealdataService frontDeviceRealdataService;

    @Autowired
    private ICarmilesService carmilesService;

    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.FKYSC_TOKEN);//金途车联,车辆实时信息更新
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到金途车联,车辆实时信息更新定时任务的配置信息" + DateUtils.now());
            return;
        }
        for (int j = 1; j < 5; j++) {
            String LoginName = null;
            String LoginPassword = "123456";
            if (j == 1){
                LoginName = "成达万1号站";
            }else if (j == 2){
                LoginName = "成达万2号站";
            }else if (j == 3){
                LoginName = "成达万3号站";
            }else {
                LoginName = "西渝4号站";
            }
            // 获取token
            JSONObject getdate = fkjspUtil.gettoken(LoginName,LoginPassword);
            String token = (String) getdate.get("mds");
            JSONArray mds = fkjspUtil.getshebeiid(token);
            //   JSONArray getshebeimil = fkjspUtil.getshebeimil(token);
            if (mds != null){
                for (Object o :mds){
                    JSONObject object = new JSONObject(o);
                    String objectid = (String) object.get("objectid");
                    JSONArray getdate1 = fkjspUtil.getdate(token, objectid);
                    JSONArray getdate2 = (JSONArray) getdate1.get(0);

                    if (getdate2 != null){
                        //设备定位时间
                        Object o1 = getdate2.get(0);
                        String s = o1.toString();
                        Date date = new Date(Long.parseLong(s));

                        //速度
                        Object o3 = getdate2.get(8);
                        String s1 = o3.toString();
                        //经纬度
                        Object o4 = getdate2.get(2);
                        String s2 = o4.toString();
                        Object o5 = getdate2.get(3);
                        String s3 = o5.toString();
                        //离线在线判断
                        Object o2 = getdate2.get(7);
                        String s5 = o2.toString();
                        Object o6 = getdate2.get(15);
                        String s6 = o6.toString();
                        String zxlx = null;
                        int i = 0;
                        long so = Long.parseLong(s6) - Long.parseLong(s5);
                        if (so > 25*60*1000){
                            zxlx = "离线";
                        }else {
                            zxlx = "在线";
                            i = 1;
                        }
                        //设备编号
                        String s4 = getdate2.get(11).toString();
                        ShebeiInfo sbjwd = shebeiInfoService.SBJWD(s4);
                        if (sbjwd == null){
                            continue;
                        }else {
                            sbjwd.setStatus(i);
                            shebeiInfoService.updateById(sbjwd);
                        }
                        //里程
                        String s7 = getdate2.get(19).toString();
                        String[] split = s7.split(",");
                        String s8 = split[0];
                        Carmiles carmiles = new Carmiles();
                        carmiles.setRundate(date);
                        QueryWrapper<Carmiles> queryWrapper1 = new QueryWrapper<>();
                        queryWrapper1.eq("shebei_no",s4);
                        System.out.println(s4);
                        Carmiles one1 = carmilesService.getOne(queryWrapper1);
                        if (one1 != null){
                            one1.setRundate(date);
                            one1.setMiles(Float.parseFloat(s8));
                            carmilesService.updateById(one1);
                        }else {
                            carmiles.setShebeiNo(s4);
                            carmiles.setMiles(Float.parseFloat(s8));
                            carmilesService.save(carmiles);
                        }

                        QueryWrapper<FrontDeviceRealdata> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("shebei_no",s4);
                        FrontDeviceRealdata one = frontDeviceRealdataService.getOne(queryWrapper);
                        FrontDeviceRealdata frontDeviceRealdata = new FrontDeviceRealdata();
                        if (one != null){
                            one.setSpeed(Double.valueOf(s1));
                            one.setLongitude(Double.valueOf(s2));
                            one.setLatitude(Double.valueOf(s3));
                            one.setDatatime(date);
                            one.setIfid(zxlx);
                            one.setShebeistatus(i);
                            frontDeviceRealdataService.updateById(one);
                        }else {
                            frontDeviceRealdata.setDeviceType("C");
                            frontDeviceRealdata.setShebeiNo(s4);
                            frontDeviceRealdata.setSpeed(Double.valueOf(s1));
                            frontDeviceRealdata.setLongitude(Double.valueOf(s2));
                            frontDeviceRealdata.setLatitude(Double.valueOf(s3));
                            frontDeviceRealdata.setDatatime(date);
                            frontDeviceRealdata.setIfid(zxlx);
                            frontDeviceRealdata.setShebeistatus(i);
                            frontDeviceRealdataService.save(frontDeviceRealdata);
                        }
                    }
                }
            }
        }
    }
}