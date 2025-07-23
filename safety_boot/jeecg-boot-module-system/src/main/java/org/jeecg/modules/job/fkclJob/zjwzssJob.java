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

/**
 * 设备实施数据更新
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zjwzssJob implements Job {
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZJWZ_TOKEN);//浙江网泽车辆位置第三方WEB调用接口
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到金途车联,车辆实时信息更新定时任务的配置信息" + DateUtils.now());
            return;
        }
        // 获取token
        String token = zjwzUtil.gettoken();
        JSONArray getdate = zjwzUtil.getdate(token);
        //String转Date
        Date dateEnd = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = simpleDateFormat.format(dateEnd);


        if (getdate != null){
            for (Object o :getdate){
                JSONObject object = new JSONObject(o);
                String buslicplate = (String) object.get("buslicplate");
                Double dimx = (Double) object.get("dimx");
                Double dimy = (Double) object.get("dimy");
                String time = (String) object.get("time");
                Double speed = (Double) object.get("speed");
                Date date = null;
                try {
                    date = simpleDateFormat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //里程
                JSONArray getshebeimil = zjwzUtil.getshebeimil(token, buslicplate, endTime);
                if (getshebeimil != null){
                    JSONObject object1 = new JSONObject(getshebeimil);
                    String mileage =(String) object1.get("mileage");
                    Carmiles carmiles = new Carmiles();
                    QueryWrapper<Carmiles> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("shebei_no",buslicplate);
                    Carmiles one1 = carmilesService.getOne(queryWrapper1);
                    if (one1 != null){
                        one1.setMiles(Float.parseFloat(mileage));
                        carmilesService.updateById(one1);
                    }else {
                        carmiles.setShebeiNo(buslicplate);
                        carmiles.setMiles(Float.parseFloat(mileage));
                        carmilesService.save(carmiles);
                    }
                }

                FrontDeviceRealdata queryone = frontDeviceRealdataService.queryone(buslicplate);
                if (queryone != null){
                    queryone.setLongitude(dimx);
                    queryone.setLatitude(dimy);
                    queryone.setDatatime(date);
                    queryone.setSpeed(speed);
                    boolean b = frontDeviceRealdataService.updateById(queryone);
                    if (b){
                        log.info("修改成功");
                    }else {
                        log.info("修改失败");
                    }
                }else {
                    FrontDeviceRealdata frontDeviceRealdata = new FrontDeviceRealdata();
                    frontDeviceRealdata.setDeviceType("C");
                    frontDeviceRealdata.setShebeiNo(buslicplate);
                    frontDeviceRealdata.setLongitude(dimx);
                    frontDeviceRealdata.setLatitude(dimy);
                    frontDeviceRealdata.setDatatime(date);
                    frontDeviceRealdata.setSpeed(speed);
                    frontDeviceRealdataService.save(frontDeviceRealdata);
                    log.info("浙江网泽车辆位置第三方WEB调用接口,添加成功！");
                }
            }
        }
    }
}