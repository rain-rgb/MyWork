package org.jeecg.modules.job.STZJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
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
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hpsf.Decimal;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class stzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private ISchedulingCarService schedulingCarService;
    @Autowired
    private ISysDepartService sysDepartService;

    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.STZ_TS);//商砼站数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到商砼站数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输商砼站数据推送的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        Integer cheflag = 1;
        Integer alertstate = 1;
        List<BhzCementBase> bhzCementBaseList = bhzCementBaseService.selectByCheFlag(shebeilist, cheflag, alertstate, curid);
        if (null == bhzCementBaseList || bhzCementBaseList.size() == 0) {
            log.info(String.format("暂无该车拌合站的数据" + DateUtils.now()));
            return;
        }

        for (BhzCementBase base : bhzCementBaseList) {
            Integer id = base.getId();
            String projectName = base.getProjectName();
            String jobLocation = base.getJobLocation();
            String poureLocation = base.getPoureLocation();
            String strengthRank = base.getStrengthRank();
            String slump = base.getSlump();
            String workNo = base.getWorkNo();
            String sys_org_code = null;
            Double estimateNumber = 0.0D;
            Integer overLevel = 0;
            String carno = base.getCarno();
            JSONObject object = new JSONObject();
            JSONArray batchDetail = new JSONArray();
            String clientNo = base.getClientNo();
            List<BhzCementBase> bhzCementBases = bhzCementBaseService.selectByClientNo(clientNo);
            if (null == bhzCementBaseList || bhzCementBaseList.size() == 0) {
                log.info(String.format("暂无该车拌合站的数据" + DateUtils.now()));
                return;
            }
            List<BhzCementDetail> zeroDetail = new ArrayList<>();
            int status = 0;
            for (BhzCementBase bhzCementBase : bhzCementBases) {
                estimateNumber = estimateNumber + bhzCementBase.getEstimateNumber();
                if (overLevel < bhzCementBase.getOverLevel()) {
                    overLevel = bhzCementBase.getOverLevel();
                }
                JSONObject baseObject = new JSONObject();
                baseObject.set("bachNo", bhzCementBase.getPanhao());//盘号
                ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(bhzCementBase.getShebeiNo());
                sys_org_code = shebeiInfo.getSysOrgCode();
                baseObject.set("deviceName", shebeiInfo.getSbname());//生产设备名称
                baseObject.set("deviceNo", bhzCementBase.getShebeiNo());//生产设备编号
                baseObject.set("batchAmount", bhzCementBase.getEstimateNumber());//盘方量
                baseObject.set("stirringTimes", bhzCementBase.getStirDatetime().toString());//搅拌时长

                JSONArray BhzDetail = new JSONArray();
                List<BhzCementDetail> bhzCementDetailList = bhzCementDetailService.selectByBatchNo(bhzCementBase.getBatchNo());
                for (BhzCementDetail bhzCementDetail : bhzCementDetailList) {
                    if (status == 0) {
                        zeroDetail.add(bhzCementDetail);
                    } else {
                        zeroDetail = matchAndSum(zeroDetail, bhzCementDetail);
                    }
                    JSONObject detailObject = new JSONObject();
                    detailObject.set("materialeName", bhzCementDetail.getMaterialeName());//材料名
                    detailObject.set("realityAmount", bhzCementDetail.getRealityNumber());//实际用量
                    detailObject.set("theoryAmount", bhzCementDetail.getTheoryNumber());//理论用量

                    detailObject.set("overValue", bhzCementDetail.getOverValue());//误差率
                    detailObject.set("waterRate", bhzCementDetail.getWaterRate());//含水率
                    detailObject.set("materialeOverLevel", bhzCementDetail.getMaterialeOverLevel());//超标等级
                    BhzDetail.add(detailObject);
                }
                status = 1;
                baseObject.set("cementDetail", BhzDetail);//配料明细
                batchDetail.add(baseObject);
            }
            QueryWrapper<SysDepart> sysDepartQueryWrapper = new QueryWrapper<>();
            sysDepartQueryWrapper.eq("org_code", sys_org_code);
            SysDepart sysDepart = sysDepartService.getOne(sysDepartQueryWrapper);


            JSONObject zero = createZeroJSON(estimateNumber, zeroDetail);
            batchDetail.add(zero);

            JSONObject JSONData = new JSONObject();
            JSONData.set("mixingPlantCode", sys_org_code);//商砼站编码
            JSONData.set("mixingPlantName", sysDepart.getDepartName());//商砼站名称
            JSONData.set("tranCode", clientNo);//运输单号
            JSONData.set("clientName", sysDepart.getDepartName());//施工单位
            JSONData.set("workNo", workNo);//任务单号
            JSONData.set("projecName", projectName);//工程名称
            JSONData.set("lineName", null);//线路名称
            JSONData.set("sectionName", sysDepart.getDepartName());//标段名称
            JSONData.set("fragmentName", null);//工区名称
            JSONData.set("siteName", null);//工点名称
            JSONData.set("jobLocation", poureLocation);//施工地点
            JSONData.set("poureLocation", poureLocation);//浇筑部位
            JSONData.set("carNo", carno);//车牌号
            JSONData.set("estimateAmount", estimateNumber);//本车方量
            JSONData.set("strengthRank", strengthRank);//强度等级
            JSONData.set("slump", slump);//坍落度
            JSONData.set("overLevel", overLevel);//超标等级
            Date productDatetime = bhzCementBaseList.get(0).getProductDatetime();
            Date transportDatetime = bhzCementBaseList.get(bhzCementBaseList.size() - 1).getProductDatetime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONData.set("stirDatetime", sdf.format(productDatetime));//搅拌时间

            JSONData.set("transportDatetime", sdf.format(transportDatetime));//运输时间
            JSONData.set("batchDetail", batchDetail);//盘列表

            object.set("deviceSN", sys_org_code);
            object.set("data", JSONData);//
            System.out.println(object);

            String post = HttpRequest.post("http://219.146.82.130:11011/system/commercialConcreteStation")
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(object))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(post);
            Integer code = (Integer) jsonObject1.get("code");
            if (code == 200) {
                log.info(String.format("商砼站数据推送成功!" + id+carno));
                BhzCementBase bhzCementBase = new BhzCementBase();
                bhzCementBase.setId(base.getId());
                if(base.getIsstz() == null){
                    bhzCementBase.setIsstz(1);
                }else{
                    bhzCementBase.setIsstz(base.getIsstz()+1);
                }
                bhzCementBaseService.updateById(bhzCementBase);
               sysConfigService.updateSysConfig(JobUtil.STZ_TS, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("商砼站数据推送失败!" + id+carno));
            }

            pushandreturnService.saveData(id, String.valueOf(object), selectsysconfigone.getRemark(), String.valueOf(jsonObject1));

        }
    }

    private JSONObject createZeroJSON(Double estimateNumber, List<BhzCementDetail> zeroDetail) {
        JSONObject zero = new JSONObject();
        zero.set("bachNo",0);
        zero.set("deviceName", "");//生产设备名称
        zero.set("deviceNo", "");//生产设备编号
        zero.set("batchAmount", estimateNumber);//盘方量
        zero.set("stirringTimes", "");//搅拌时长
        JSONArray BhzDetail = new JSONArray();
        for (BhzCementDetail detail : zeroDetail) {
            JSONObject jsonObject = new JSONObject();
            double overnum = 0;
            Double realnum = detail.getRealityNumber();
            Double theorynum = detail.getTheoryNumber();
            String numStr = String.valueOf(theorynum);
            if (numStr.matches("\\d+\\.\\d{3,}")) {
                BigDecimal b2 = new BigDecimal(theorynum);
                theorynum = b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            String numStr2 = String.valueOf(realnum);
            if (numStr2.matches("\\d+\\.\\d{3,}")) {
                BigDecimal b2 = new BigDecimal(realnum);
                realnum = b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            double errornum = realnum - theorynum;
            if (theorynum != 0) {
                overnum = errornum / theorynum * 100;
            }
            BigDecimal b = new BigDecimal(overnum);
            overnum = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (Double.isNaN(overnum)) {
                overnum = 0;
            }
            if (overnum < 0) {// 如果小于0就把值变为正数
                overnum = -overnum;
            }

            jsonObject.set("materialeName", detail.getMaterialeName());//材料名
            jsonObject.set("realityAmount", realnum);//实际用量
            jsonObject.set("theoryAmount", theorynum);//理论用量
            jsonObject.set("overValue", overnum);//误差率
            jsonObject.set("waterRate", "");//含水率
            jsonObject.set("materialeOverLevel", "");//超标等级
            BhzDetail.add(jsonObject);
        }
        zero.set("cementDetail", BhzDetail);//配料明细
        return zero;
    }

    private List<BhzCementDetail> matchAndSum(List<BhzCementDetail> zeroDetail, BhzCementDetail bhzCementDetail) {
        OptionalInt index = IntStream.range(0, zeroDetail.size())
                .filter(i -> zeroDetail.get(i).getMaterialeName().equals(bhzCementDetail.getMaterialeName()))
                .findFirst();
        if(!index.isPresent()){
            zeroDetail.add(bhzCementDetail);
            return zeroDetail;
        }
        int asInt = index.getAsInt();
        if (asInt == -1) {
            throw new JeecgBootException("0盘数据统计错误");
        }
        BhzCementDetail detail = zeroDetail.get(asInt);
        detail.setRealityNumber(bhzCementDetail.getRealityNumber() + detail.getRealityNumber());
        detail.setTheoryNumber(bhzCementDetail.getTheoryNumber() + detail.getTheoryNumber());
        zeroDetail.set(asInt, detail);
        return zeroDetail;
    }
}
