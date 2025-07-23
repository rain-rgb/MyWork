package org.jeecg.modules.job.hangyongjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName banHeZhanJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/12/13 16:11
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class banHeZhanJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    private static String projectId = "a17973d8-38e4-4481-90b9-7b3c0b5dfff5";
    private static String companyId = "6844d1c6-a4fe-48c8-beaa-cd92a6e3e7af";
    private static String appkey = "materialMixingReal";
    private static String appSecret = "9006afd1-b7b0-4136-8cb6-26abf95e5eff";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HY_BHZ);//杭甬二标
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到杭甬二标拌合站的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输杭甬二标拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBases = bhzCementBaseService.selectLists2(shebeilist, curid);
        if (null == bhzCementBases || bhzCementBases.size() == 0) {
            log.info(String.format("暂无杭甬二标拌合站未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (BhzCementBase bhzCementBase : bhzCementBases) {
            id = bhzCementBase.getId();
            JSONObject sendDate = new JSONObject();
            sendDate.set("projectId", projectId);
            sendDate.set("companyId", companyId);
            sendDate.set("productStartDate", sdf.format(bhzCementBase.getProductDatetime()));
            sendDate.set("projectName", bhzCementBase.getProjectName());

            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
            shebeiInfoQueryWrapper.eq("sbjno",bhzCementBase.getShebeiNo());
            ShebeiInfo one = shebeiInfoService.getOne(shebeiInfoQueryWrapper);

            sendDate.set("constructionUnit", one.getSbname());
            sendDate.set("constructionPosition", bhzCementBase.getPoureLocation());
            sendDate.set("projectAdd", bhzCementBase.getJobLocation());
            String formulaNo = bhzCementBase.getFormulaNo();
            if (null == formulaNo){
                sendDate.set("mixProportion", "/");
            }else {
                sendDate.set("mixProportion", formulaNo);
            }
            sendDate.set("markNum", bhzCementBase.getStrengthRank());
            sendDate.set("plateVolume", bhzCementBase.getEstimateNumber());

            QueryWrapper<BhzCementDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("batch_no",bhzCementBase.getBatchNo());
            List<BhzCementDetail> bhzCementDetails = bhzCementDetailService.list(queryWrapper);
            List detailList = new ArrayList();
            for (BhzCementDetail bhzCementDetail : bhzCementDetails){
                JSONObject DetailDate = new JSONObject();
                DetailDate.set("materialName",bhzCementDetail.getMaterialeName());
                DetailDate.set("theoryValue",bhzCementDetail.getTheoryNumber());
                DetailDate.set("relValue",bhzCementDetail.getRealityNumber());
                DetailDate.set("exceedanceValue",bhzCementDetail.getOverValue());
                DetailDate.set("exceedanceLevel",bhzCementDetail.getMaterialeOverLevel());
                detailList.add(DetailDate);
            }
            sendDate.set("detailList", detailList);

            long nonce = System.currentTimeMillis();
            //MD5(appKey:appSecret:nonce)
            String sign = MD5Util.MD5Encode(appkey + ":" + appSecret + ":" + nonce, "utf-8");

            String url = "http://bim.bnerc.com/material/material/materialMixingReal/syncData";
            String back = HttpRequest.post(url)
                    .header("sign", sign)
                    .header("appkey", appkey)
                    .header("nonce", String.valueOf(nonce))
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(back);
            String codes = jsonObject1.get("msg").toString();
            if (codes.equals("操作成功")) {
                log.info(String.format("推送成功!" + id + "Json数据" + back));
            } else {
                log.info(String.format("推送失败!" + id + "Json数据" + back));
            }
            sysConfigService.updateSysConfig(JobUtil.HY_BHZ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
