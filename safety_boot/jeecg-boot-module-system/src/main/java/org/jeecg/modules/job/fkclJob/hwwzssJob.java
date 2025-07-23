package org.jeecg.modules.job.fkclJob;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.carmiles.entity.Carmiles;
import com.trtm.iot.carmiles.service.ICarmilesService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.jeecg.qywx.api.base.JwParamesAPI.token;

/**
 * 设备实施数据更新
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class hwwzssJob implements Job {
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZTYJ_HWCL);//合武车辆调度
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到合武车辆调度车辆实时信息更新定时任务的配置信息" + DateUtils.now());
            return;
        }
        JSONArray getshebeilist = null;
        try {
            // 获取设备列表
            getshebeilist = zjwzUtil.getshebeilist();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String projectid = null;
        for (Object shebei :getshebeilist){
            JSONObject object = new JSONObject(shebei);
            Integer deviceid = (Integer) object.get("device_id");//设备ID
            projectid = (String) object.get("project_id");//项目ID
            String licenseplate = (String) object.get("license_plate");//车牌号
            ShebeiInfo shebeiInfo = new ShebeiInfo();
            shebeiInfo.setSbjno(String.valueOf(deviceid));
            shebeiInfo.setSbname(licenseplate);
            shebeiInfo.setSbjsimplename(licenseplate);
            shebeiInfo.setSysOrgCode("A05A20A01A01A01A01A01");
            shebeiInfo.setSbtype(35);

            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(String.valueOf(deviceid));
            if (selectshebeione == null){
                shebeiInfoService.save(shebeiInfo);
            }else {
                shebeiInfo.setId(selectshebeione.getId());
                shebeiInfoService.updateById(shebeiInfo);
            }
        }
        // 获取设备实时位置
        JSONArray getshebeilistwz = null;
        try {
            getshebeilistwz = zjwzUtil.getshebeilistwz(projectid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Object shebeiwz :getshebeilistwz){
            JSONObject object = new JSONObject(shebeiwz);
            String latitude = (String) object.get("latitude");//精度
            String longitude = (String) object.get("longitude");//维度
            Integer deviceid = (Integer) object.get("device_id");//设备ID

            FrontDeviceRealdata frontDeviceRealdata = new FrontDeviceRealdata();
            frontDeviceRealdata.setDeviceType("C");
            frontDeviceRealdata.setShebeiNo(String.valueOf(deviceid));
            if (longitude != null){
                frontDeviceRealdata.setLongitude(Double.valueOf(longitude));
            }
            if (latitude != null){
                frontDeviceRealdata.setLatitude(Double.valueOf(latitude));
            }
            FrontDeviceRealdata queryone = frontDeviceRealdataService.queryone(String.valueOf(deviceid));
            if (queryone == null){
                frontDeviceRealdataService.save(frontDeviceRealdata);
            }else {
                frontDeviceRealdata.setId(queryone.getId());
                frontDeviceRealdataService.updateById(frontDeviceRealdata);
            }
        }
    }
}