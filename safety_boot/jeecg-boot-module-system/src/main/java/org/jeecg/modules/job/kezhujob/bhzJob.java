package org.jeecg.modules.job.kezhujob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.car.entity.SchedulingCar;
import com.trtm.iot.car.service.ISchedulingCarService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName bhzJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/12/28 13:33
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class bhzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private ISchedulingCarService schedulingCarService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.KZ_BHZ);//瑞苍拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞苍砼拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞苍砼拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBases = bhzCementBaseService.selectListskz4(shebeilist, curid);
        if (null == bhzCementBases || bhzCementBases.size() == 0) {
            log.info(String.format("暂无瑞苍拌合站未推送数据" + DateUtils.now()));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        int id = 0;
        //循环
        for (BhzCementBase bhzCementBase : bhzCementBases){
            id = bhzCementBase.getId();

            String shebeiNo = bhzCementBase.getShebeiNo();
            Integer projId = 0;
            Integer mixNo = 0;
            String mixName = "";
            if ("kzgsbh04_01_01".equals(shebeiNo)){
                projId = 81504;
                mixNo = 1;
                mixName = "柯诸项目4标拌合站1号线";
            }
            if ("kzgsbh04_01_02".equals(shebeiNo)){
                projId = 81504;
                mixName = "柯诸项目4标拌合站2号线";
                mixNo = 2;
            }
            if ("kzgsbh04_02_03".equals(shebeiNo)){
                projId = 81504;
                mixName = "柯诸项目4标拌合站3号线";
                mixNo = 3;
            }
            if ("kzgsbh04_02_04".equals(shebeiNo)){
                projId = 81504;
                mixName = "柯诸项目4标拌合站4号线";
                mixNo = 4;
            }
            if ("kzgsbh04_02_01".equals(shebeiNo)){
                projId = 81504;
                mixName = "柯诸项目4标拌合站5号线";
                mixNo = 5;
            }
            if ("kzgsbh04_02_02".equals(shebeiNo)){
                projId = 81504;
                mixName = "柯诸项目4标拌合站6号线";
                mixNo = 6;
            }
            JSONObject sendDate = new JSONObject();
            sendDate.set("dbName","zj_kzPallasIOT");
            sendDate.set("taskId",bhzCementBase.getBatchNo());
            sendDate.set("projId",projId);
            sendDate.set("mixNo",mixNo);
            sendDate.set("useArea",bhzCementBase.getPoureLocation());
            sendDate.set("cube",bhzCementBase.getEstimateNumber());
            sendDate.set("actualCube",bhzCementBase.getEstimateNumber());

            Integer cardIdx = bhzCementBaseService.getCarCount(bhzCementBase);
                sendDate.set("carNumber", bhzCementBase.getCarno());
                sendDate.set("trayIdx", bhzCementBase.getPanhao());
                sendDate.set("cardIdx", cardIdx);//累计车次

            Date productDatetime = bhzCementBase.getProductDatetime();
            int StirDatetime = Integer.parseInt(bhzCementBase.getStirDatetime().toString());
            Date btime = new Date(productDatetime.getTime() - StirDatetime * 1000);
            sendDate.set("btime",sdf.format(btime));
            sendDate.set("etime",sdf.format(bhzCementBase.getProductDatetime()));
            sendDate.set("toneName",bhzCementBase.getStrengthRank());
            sendDate.set("isOver",0);
            sendDate.set("isDel",0);
            sendDate.set("delUserId","");
            sendDate.set("delUserName","");

            String url = "http://112.95.76.11:6543/iotws/mixtask/add";
            String body = HttpRequest.post(url)
                    .form(sendDate)
                    .execute()
                    .body();
            pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), body);
            JSONObject jsonObject1 = new JSONObject(body);
            String data = jsonObject1.get("msg").toString();
            if ("操作成功！".equals(data)) {
                log.info(String.format("柯诸拌合站推送成功!" + id + "Json数据" + sendDate));
                bhzCementBase.setIssend(1);
            } else {
                log.info(String.format("柯诸拌合站推送失败!" + id + "Json数据" + sendDate));
                bhzCementBase.setIssend(2);
            }
            bhzCementBaseService.saveOrUpdate(bhzCementBase);
            pushandreturnService.saveData(id,String.valueOf(sendDate),selectsysconfigone.getRemark(),body);
            JSONObject cardata = new JSONObject();
            cardata.set("dbName","zj_kzPallasIOT");
            cardata.set("kid",bhzCementBase.getBatchNo()+bhzCementBase.getId());
            cardata.set("projId",projId);
            cardata.set("mixNo",mixNo);
//            cardata.set("queueId",bhzCementBase.get);
            cardata.set("taskId",bhzCementBase.getBatchNo());
            cardata.set("carNumber", bhzCementBase.getCarno());
            cardata.set("trayIdx", bhzCementBase.getPanhao());
            cardata.set("cardIdx", cardIdx);//累计车次
            cardata.set("taskName",bhzCementBase.getFormulaNo());
            cardata.set("toneName",bhzCementBase.getStrengthRank());
            cardata.set("useArea",bhzCementBase.getPoureLocation());
            cardata.set("cube",bhzCementBase.getEstimateNumber());
//            cardata.set("actualCube",bhzCementBase.get);
            cardata.set("trayCube",bhzCementBase.getEstimateNumber());
//            cardata.set("trayCount",bhzCementBase.get);
//            cardata.set("isOver",bhzCementBase.get);
//            cardata.set("isDel",bhzCementBase.get);
//            cardata.set("delUserId",bhzCementBase.get);
//            cardata.set("delUserName",bhzCementBase.get);

            String url2 = "http://112.95.76.11:6543/iotws/mixproductionqueue/add";
            String body2 = HttpRequest.post(url2)
                    .form(cardata)
                    .execute()
                    .body();

            pushandreturnService.saveData(id, String.valueOf(cardata), selectsysconfigone.getRemark(), body2);

            QueryWrapper<BhzCementDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("materiale_type, sum(reality_number) as reality_number, sum(theory_number) as theory_number")
            .eq("batch_no",bhzCementBase.getBatchNo())
            .groupBy("materiale_type");
            List<BhzCementDetail> list1 = bhzCementDetailService.list(queryWrapper1);
            for (BhzCementDetail bhzCementDetail : list1) {
                JSONObject Detail = new JSONObject();
                Detail.set("dbName","zj_kzPallasIOT");
                Detail.set("trayId","pan_"+ bhzCementBase.getId());
                Integer materialeType = bhzCementDetail.getMaterialeType();
                if (1==materialeType){
                    Detail.set("material","碎石");
                }
                if (2==materialeType){
                    Detail.set("material","大石");
                }
                if (3==materialeType){
                    Detail.set("material","中石");
                }
                if (4==materialeType){
                    Detail.set("material","小石");
                }
                if (5==materialeType){
                    Detail.set("material","水");
                }
                if (6==materialeType){
                    Detail.set("material","水泥");
                }
                if (7==materialeType){
                    Detail.set("material","矿粉");
                }
                if (8==materialeType){
                    Detail.set("material","粉煤灰");
                }
                if (9==materialeType){
                    Detail.set("material","外加剂1");
                }
                Detail.set("design",bhzCementDetail.getTheoryNumber());
                Detail.set("actual",bhzCementDetail.getRealityNumber());

                String url1 = "http://112.95.76.11:6543/iotws/mixtrayactual/add";
                String body1 = HttpRequest.post(url1)
                        .form(Detail)
                        .execute()
                        .body();

                pushandreturnService.saveData(id, String.valueOf(Detail), selectsysconfigone.getRemark(), body1);
            }

            JSONObject pan = new JSONObject();
            pan.set("dbName","zj_kzPallasIOT");
            pan.set("trayId","pan_"+ bhzCementBase.getId());
            pan.set("taskId",bhzCementBase.getBatchNo());
            pan.set("queueKid",bhzCementBase.getBatchNo()+bhzCementBase.getId());
            pan.set("carNumber", bhzCementBase.getCarno());
            pan.set("trayIdx", bhzCementBase.getPanhao());
            pan.set("cube",bhzCementBase.getEstimateNumber());
            pan.set("cardIdx", cardIdx);
            pan.set("btime",sdf.format(btime));
            pan.set("etime",sdf.format(bhzCementBase.getProductDatetime()));
            pan.set("time",bhzCementBase.getStirDatetime());
            pan.set("trayCube",bhzCementBase.getEstimateNumber());
//            pan.set("temperature",bhzCementBase.get);
//            pan.set("isDel",bhzCementBase.get);
//            pan.set("delUserId",bhzCementBase.get);
//            pan.set("delUserName",bhzCementBase.get);
            String body3 = HttpRequest.post("http://112.95.76.11:6543/iotws/mixtray/add")
                    .form(pan)
                    .execute()
                    .body();

            pushandreturnService.saveData(id, String.valueOf(pan), selectsysconfigone.getRemark(), body3);
            JSONObject xintiao = new JSONObject();
            xintiao.set("dbName","zj_kzPallasIOT");
            xintiao.set("projId",projId);
            xintiao.set("mixNo",mixNo);
            xintiao.set("mixName",mixName);
            String body4 = HttpRequest.post("http://112.95.76.11:6543/iotws/mixstationheartbeat/add")
                    .form(xintiao)
                    .execute()
                    .body();

            pushandreturnService.saveData(id, String.valueOf(xintiao), selectsysconfigone.getRemark(), body4);
            sysConfigService.updateSysConfig(JobUtil.KZ_BHZ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
