package org.jeecg.modules.job.sutaijob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.clgl.entity.ClxxRealdata;
import com.trtm.iot.clgl.entity.FrontDeviceHistorydata;
import com.trtm.iot.clgl.service.IClxxRealdataService;
import com.trtm.iot.clgl.service.IFrontDeviceHistorydataService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class STCLGJJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IClxxRealdataService clxxRealdataService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_CLGJ);//苏台车辆轨迹
        if(selectsysconfigone == null){
            log.info(String.format("未获取到苏台车辆轨迹定时任务的配置信息" + DateUtils.now()));
            return;
        }
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台车辆轨迹的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList = Arrays.asList(split);
        List<ClxxRealdata> clxxRealdataList = clxxRealdataService.selectList(curid, strsToList);
        if (null == clxxRealdataList || clxxRealdataList.size() == 0) {
            log.info(String.format("暂无苏台车辆轨迹的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        JSONArray jsonArray = new JSONArray();
        for (ClxxRealdata clxxRealdata : clxxRealdataList
             ) {
            id=clxxRealdata.getId();
            JSONObject saveDTOList = new JSONObject();
            saveDTOList.set("carNo",clxxRealdata.getShebeiNo());//车辆编号
            saveDTOList.set("constr",clxxRealdata.getSpeed());//速度
            saveDTOList.set("latitude",clxxRealdata.getLatitude());//纬度
            saveDTOList.set("longitude",clxxRealdata.getLongitude());//经度
            saveDTOList.set("projectName","苏台车辆轨迹信息");//项目
//            saveDTOList.set("projectName","测试数据");//项目
            saveDTOList.set("sectionType",1);//1 一标 2 二标 3 三标 4 四标 5 五标
            saveDTOList.set("temperature",clxxRealdata.getTemperature());//温度
            saveDTOList.set("transportType",clxxRealdata.getStatus());//	运输状态 0未判断 1运输中 2已完成
            saveDTOList.set("uploadTime",clxxRealdata.getDatatime());//数据上传时间
            jsonArray.add(saveDTOList);
        }

        String post = HttpRequest.post("http://47.98.163.134:9170/solutionsMovingCar/insertBacth")
                .header("Content-Type", "application/json")
                .body(String.valueOf(jsonArray))
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(post);
        Integer code = (Integer) jsonObject1.get("code");
        if (code == 200 || code == 0) {
            log.info(String.format("苏台车辆轨迹数据推送成功!" + id));
            sysConfigService.updateSysConfig(JobUtil.ST_CLGJ, id);//根据传过来的唯一值来修改当前判断到的数据id
        } else {
            log.info(String.format("苏台车辆轨迹数据推送失败!" + id));
        }
    }
}
