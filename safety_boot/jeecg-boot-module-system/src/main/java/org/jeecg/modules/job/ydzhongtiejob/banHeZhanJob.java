package org.jeecg.modules.job.ydzhongtiejob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.ydzhongtiejob.Util.CryptoUtils;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName banHeZhanJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/9 13:57
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
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private static final String encryptKey = "wz2cFJhkM1qrIdkMqNY8cw==";
    private static final String iv = "wza41thrBplFGTLPE15G6g==";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YB_GTBHZ);//开投
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到开投的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输开投的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBases = bhzCementBaseService.selectListsyb(shebeilist, curid);
        if (null == bhzCementBases || bhzCementBases.size() == 0) {
            log.info(String.format("暂无开投未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (BhzCementBase bhzCementBase : bhzCementBases) {
            id = bhzCementBase.getId();
            JSONObject sendDate = new JSONObject();
            sendDate.set("admixtureType", bhzCementBase.getAdditiveVariety());
            sendDate.set("cementType", bhzCementBase.getCementVariety());
            sendDate.set("collectionTime", sdf.format(bhzCementBase.getCollectDatetime()));
            sendDate.set("dischargeTime", sdf.format(bhzCementBase.getProductDatetime()));
            sendDate.set("executor", bhzCementBase.getHandlers());
            sendDate.set("location", bhzCementBase.getJobLocation());
            sendDate.set("mixRatioCode", bhzCementBase.getFormulaNo());
            sendDate.set("mixTime", bhzCementBase.getStirDatetime());
            sendDate.set("orderNum", bhzCementBase.getWorkNo());
            sendDate.set("plateNum", "");
            sendDate.set("pourPart", bhzCementBase.getPoureLocation());
            sendDate.set("quantity", "");
            sendDate.set("squareAmount", String.valueOf(bhzCementBase.getEstimateNumber()));
            String strengthRank = bhzCementBase.getStrengthRank();
            if (strengthRank == null) {
                strengthRank = bhzCementBase.getFormulaNo();
            }
            sendDate.set("strength", strengthRank);

            String shebeiNo = bhzCementBase.getShebeiNo();

            if (shebeiNo.equals("ybgssfb2_01")) {
                shebeiNo = "YBGS001";
            }
            if (shebeiNo.equals("ybgssfb2_02")) {
                shebeiNo = "YBGS002";
            }
            if (shebeiNo.equals("ybgs1fb1_01")) {
                shebeiNo = "YBGS003";
            }
            if (shebeiNo.equals("ybgs1fb1_02")) {
                shebeiNo = "YBGS004";
            }
            if (shebeiNo.equals("ycxgstj0101")) {
                shebeiNo = "ZTBJYCXGS1B1Z1J";
            }
            if (shebeiNo.equals("ycx2bbh01")) {
                shebeiNo = "YCXGS2B1J";
            }
            if (shebeiNo.equals("ycx2bbh02")) {
                shebeiNo = "YCXGS2B2J";
            }
            QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
            sysDictQueryWrapper.eq("dict_code", "KTSB");
            SysDict one = sysDictService.getOne(sysDictQueryWrapper);
            String id1 = one.getId();

            QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
            sysDictItemQueryWrapper.eq("dict_id", id1);
            sysDictItemQueryWrapper.eq("item_text", shebeiNo);
            SysDictItem one1 = sysDictItemService.getOne(sysDictItemQueryWrapper);

            sendDate.set("checkCode", one1.getItemValue());//设备校验码
            sendDate.set("mixDeviceInfoCode", one1.getItemText());

            List cailiao = new ArrayList();
            QueryWrapper<BhzCementDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("batch_no", bhzCementBase.getBatchNo());
            List<BhzCementDetail> bhzCementDetails = bhzCementDetailService.list(queryWrapper);
            for (BhzCementDetail bhzCementDetail : bhzCementDetails) {
                Integer materialeType = bhzCementDetail.getMaterialeType();
                JSONObject sendDateson = new JSONObject();
                sendDateson.set("actualValue", bhzCementDetail.getRealityNumber());
                sendDateson.set("theoryValue", bhzCementDetail.getTheoryNumber());
                if (materialeType == null) {
                    String materialeId = bhzCementDetail.getMaterialeId();
                    if (materialeId.equals("10001")) {
                        sendDateson.set("code", "Fines1");
                    }
                    if (materialeId.equals("10002")) {
                        sendDateson.set("code", "Coarse1");
                    }
                    if (materialeId.equals("10003")) {
                        sendDateson.set("code", "Coarse2");
                    }
                    if (materialeId.equals("10004")) {
                        sendDateson.set("code", "Coarse3");
                    }
                    if (materialeId.equals("10005")) {
                        sendDateson.set("code", "Cement1");
                    }
                    if (materialeId.equals("10007")) {
                        sendDateson.set("code", "Coal");
                    }
                    if (materialeId.equals("10008")) {
                        sendDateson.set("code", "Additive1");
                    }
                    if (materialeId.equals("10009")) {
                        sendDateson.set("code", "Water");
                    }
                } else {
                    if (materialeType == 5) {
                        sendDateson.set("code", "Water");
                    }
                    if (materialeType == 6) {
                        sendDateson.set("code", "Cement1");
                    }
                    if (materialeType == 1) {
                        sendDateson.set("code", "Fines1");
                    }
                    if (materialeType == 8) {
                        sendDateson.set("code", "Coal");
                    }
                    if (materialeType == 9) {
                        sendDateson.set("code", "Additive1");
                    }
                    if (materialeType == 3) {
                        sendDateson.set("code", "Coarse1");
                    }
                    if (materialeType == 4) {
                        sendDateson.set("code", "Coarse2");
                    }
                    if (materialeType == 2) {
                        sendDateson.set("code", "Coarse3");
                    }
                    if (materialeType == 7) {
                        sendDateson.set("code", "Mineral");
                    }
                }
                cailiao.add(sendDateson);
            }
            sendDate.set("mixMaterialInfoList", cailiao);

            String s = "";
            try {
                s = CryptoUtils.encryptSymmetrically(encryptKey, iv, String.valueOf(sendDate), CryptoUtils.Algorithm.AES_CBC_PKCS5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String timeStr1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String s1 = MD5Util.MD5Encode(encryptKey + iv + timeStr1, "utf-8");

            JSONObject sendDates = new JSONObject();
            sendDates.set("text", s);
            sendDates.set("encryptKey", encryptKey);
            sendDates.set("date", timeStr1);
            sendDates.set("sign", s1);

            String back = HttpRequest.post("https://cdentity.tgmis.cn:7001/mixstation/mix_station_data/add")
                    .body(String.valueOf(sendDates))
                    .execute()
                    .body();
            pushandreturnService.saveData(id, String.valueOf(sendDates), selectsysconfigone.getRemark(), back);

            JSONObject jsonObject1 = new JSONObject(back);
            String codes = jsonObject1.get("code").toString();
            BhzCementBase bhzCementBase1 = new BhzCementBase();
            bhzCementBase1.setId(id);
            if ("200".equals(codes)) {
                log.info(String.format("开投推送成功!" + id + "Json数据" + sendDate));
                bhzCementBase1.setIssend(1);
            } else {
                bhzCementBase1.setIssend(2);
            }
            bhzCementBaseService.updateById(bhzCementBase1);
            sysConfigService.updateSysConfig(JobUtil.YB_GTBHZ, id);//根据传过来的唯一值来修改当前判断到的数据id

        }
    }
}
