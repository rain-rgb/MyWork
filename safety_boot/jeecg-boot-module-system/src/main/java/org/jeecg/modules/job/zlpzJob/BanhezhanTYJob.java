package org.jeecg.modules.job.zlpzJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zlpz.entity.Zlpz;
import com.trtm.iot.zlpz.service.IZlpzService;
import com.xkcoding.http.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName ZhanglaJob：
 * @Description 浙路品质拌合站通用接口
 * @Author 55314
 * @Date 2022/6/29 8:43
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BanhezhanTYJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;
    @Autowired
    private IZlpzService zlpzService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZLPZ_BHZ);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到疏港砼拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输疏港砼拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBases = bhzCementBaseService.selectListsbhgty(shebeilist, curid);
        if (null == bhzCementBases || bhzCementBases.size() == 0) {
            log.info(String.format("暂无疏港拌合站未推送数据" + DateUtils.now()));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int id = 0;
        //循环
        for (BhzCementBase bhzCementBase : bhzCementBases) {
            if (bhzCementBase.getShebeiNo().contains("rcgs") || bhzCementBase.getShebeiNo().contains("yjqs")){
                String poureLocation = bhzCementBase.getPoureLocation();
                String workNo = bhzCementBase.getWorkNo();
                if (poureLocation.contains("临建")||poureLocation.contains("硬化")||poureLocation.contains("砂浆")||workNo.contains("临建")){
                    bhzCementBase.setIssend(2);
                    bhzCementBaseService.updateById(bhzCementBase);
                    continue;
                }
            }
            QueryWrapper<Zlpz> queryWrapperZlpz = new QueryWrapper<>();
            queryWrapperZlpz.eq("shebeino", bhzCementBase.getShebeiNo());
            Zlpz one = zlpzService.getOne(queryWrapperZlpz);
            String shebeiid = one.getShebeiid();
            String project = one.getProject();
            if ("2017-330703-48-01-083974-000".equals(project)&&bhzCementBase.getProjectName().contains("235")) {
                bhzCementBase.setIssend(4);
                bhzCementBaseService.updateById(bhzCementBase);
            }
            id = bhzCementBase.getId();
            JSONObject sendDate = new JSONObject();
            List sendList = new ArrayList();
            sendDate.set("id", project + "_" + id);
            sendDate.set("equipId", shebeiid);
            sendDate.set("handler", bhzCementBase.getHandlers());
            sendDate.set("part", bhzCementBase.getPoureLocation());
            String strengthRank = bhzCementBase.getStrengthRank();
            if (StringUtil.isNotEmpty(strengthRank)){
                strengthRank = "空";
            }
            sendDate.set("strengthGrade", strengthRank);
            sendDate.set("stirTime", bhzCementBase.getStirDatetime());
            sendDate.set("estimateNumber", bhzCementBase.getEstimateNumber());
            sendDate.set("prodTime", sdf.format(bhzCementBase.getProductDatetime()));

            QueryWrapper<BhzCementOverHandler> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("baseId", bhzCementBase.getBatchNo());
            BhzCementOverHandler one1 = bhzCementOverHandlerService.getOne(queryWrapper);
            int alarmStatus = 0;
            if (null != one1) {
                alarmStatus = 1;
                sendDate.set("opinion", one1.getHandleWay());
                sendDate.set("closeTime", sdf.format(one1.getHandleTime()));
                sendDate.set("closer", one1.getHandlePerson());
                sendDate.set("attachment", one1.getFilePath2());//附件
            }
            sendDate.set("alarmStatus", alarmStatus);

            QueryWrapper<BhzCementDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("batch_no", bhzCementBase.getBatchNo());
            List<BhzCementDetail> list1 = bhzCementDetailService.list(queryWrapper1);

            List list2 = new ArrayList();
            for (BhzCementDetail bhzCementDetail : list1) {
                Map cailiaoMap = new HashMap();
                Integer materialeType = bhzCementDetail.getMaterialeType();
                if (materialeType == null){
                    bhzCementBase.setIssend(10);
                    bhzCementBaseService.updateById(bhzCementBase);
                    continue;
                }
                cailiaoMap.put("materialType", materialeType);
                cailiaoMap.put("materialName", bhzCementDetail.getMaterialeName());
                Double realityNumber = bhzCementDetail.getRealityNumber();
                cailiaoMap.put("realityNumber", realityNumber);
                Double theoryNumber = bhzCementDetail.getTheoryNumber();
                cailiaoMap.put("theoryNumber", theoryNumber);
                double mix, min;
                if (materialeType == 3 || materialeType == 4) {
                    mix = theoryNumber * 1.02;
                    min = theoryNumber * 0.98;
                } else {
                    mix = theoryNumber * 1.01;
                    min = theoryNumber * 0.99;
                }
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                cailiaoMap.put("allowMin", Double.parseDouble(decimalFormat.format(min)));
                cailiaoMap.put("allowMax", Double.parseDouble(decimalFormat.format(mix)));

                list2.add(cailiaoMap);
            }
            sendDate.set("dosageList", list2);
            sendList.add(sendDate);

            JSONObject sendJsonObject = new JSONObject();
            sendJsonObject.set("serviceName", "ZLPZ_ZX_SNHNTBH");
            sendJsonObject.set("token", "93cd2c6567594107a16b51a65bcd5a37");
            sendJsonObject.set("updateNull", "true");
            sendJsonObject.set("param", sendList);
            System.out.println(sendJsonObject);
            String url = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/push";
            String back = HttpRequest.post(url)
                    .body(sendJsonObject.toString())
                    .timeout(20000)
                    .execute().body();

            com.alibaba.fastjson.JSONObject backJson = JSON.parseObject(back);
            int result = backJson.getIntValue("result");
            if (result == 0) {
                bhzCementBase.setIssend(alarmStatus == 0 ? 3 : 1);
                log.info(String.format("浙路品质砼拌合站推送成功！%s", id));
            } else {
                bhzCementBase.setIssend(2);
                log.info(String.format("浙路品质砼拌合站推送失败！%s", id));
            }
            bhzCementBaseService.updateById(bhzCementBase);
            pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), back);
        }
    }
}
