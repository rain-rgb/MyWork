package org.jeecg.modules.job.ydJob.jiaotoujituan;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.xkcoding.http.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName lqcbJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/5/17 16:47
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class lqcbJob implements Job {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String SECRET_KEY = "4028e5cc811396dd01811396dd790000";
    private static final String ACCESS_KEY = "10002";
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;
    @Autowired
    private IBhzLqCailiaoService bhzLqCailiaoService;
    @Autowired
    private IBhzCementOverHandlerService overHandlerService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    /**
     * 使用 HMAC-SHA1 签名方法对data进行签名
     *
     * @param data 被签名的字符串
     * @param key  密钥
     * @return 加密后的字符串
     */
    public static String getSign(String data, String key) {
        byte[] result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64.encodeBase64(rawHmac);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println(e.getMessage());
        }
        if (null != result) {
            return new String(result);
        } else {
            return null;
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JTJT_LQCBLSSJ);//沥青历史数据推送交投集团
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东沥青的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东沥青的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzLqBases> bhzLqBases = bhzLqBasesService.selectcbList(shebeilist,curid);
        if (null == bhzLqBases || bhzLqBases.size() == 0) {
            log.info(String.format("暂无义东沥青未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        DecimalFormat df = new DecimalFormat("#.00");
        for (BhzLqBases bhzLqBase : bhzLqBases) {
            JSONObject sendJSONObject = JSONUtil.createObj();
            // 生成随机的 UUID
            UUID uuid = UUID.randomUUID();
            id = bhzLqBase.getId();
            // 将 UUID 转换为 32 位的字符串形式
            String uuidStr = uuid.toString().replace("-", "");
            sendJSONObject.set("id", uuidStr);
            String shebeibianhao = bhzLqBase.getShebeibianhao();

            if (shebeibianhao.equals("hyfxs5lq01")) {
                sendJSONObject.set("deviceNum", "HYFX_S5_" + shebeibianhao);
            } else {
                sendJSONObject.set("deviceNum", "YD_LM01_" + shebeibianhao);
            }
            sendJSONObject.set("alarmLevel", bhzLqBase.getChaobiaodengji()); // 预警等级
            sendJSONObject.set("alarmTime", bhzLqBase.getChuliaoshijian()); // 预警时间
            sendJSONObject.set("osbMisPstSj", bhzLqBase.getYoushibi()); // 实际油石比
            sendJSONObject.set("osbMisPstLl", bhzLqBase.getLlysb()); // 理论油石比
            String liqingwd = bhzLqBase.getLiqingwd();
            int asphaltTemp = Double.valueOf(liqingwd).intValue();
            sendJSONObject.set("asphaltTemp", asphaltTemp);
            String chuliaowd = bhzLqBase.getChuliaowd();
            int mixTemp = Double.valueOf(chuliaowd).intValue();
            sendJSONObject.set("mixTemp", mixTemp);
            sendJSONObject.set("weight", bhzLqBase.getZongchanliang()); // 本锅重量
            Integer overproofStatus = bhzLqBase.getOverproofStatus();
            int status; // 新状态变量
            if (overproofStatus == 0) {
                status = 0; // 待处置
            } else if (overproofStatus == 10) {
                status = 1; // 审核中
            } else if (overproofStatus == 20) {
                status = 2; // 已闭合
            } else {
                status = -1; // 其他情况
            }
            sendJSONObject.set("dealStatus", status); // 闭合状态
            QueryWrapper<BhzCementOverHandler> bhzCementOverHandlerQueryWrapper = new QueryWrapper<>();
            bhzCementOverHandlerQueryWrapper.eq("baseId", bhzLqBase.getGuid());
            BhzCementOverHandler one = overHandlerService.getOne(bhzCementOverHandlerQueryWrapper);
            if (null != one) {
                sendJSONObject.set("problemReasons", one.getProblemReasons()); // 问题原因
                sendJSONObject.set("handleWay", one.getHandleWay()); // 处理方式
//                sendJSONObject.set("fileUrl", ); // 处置前情况附件

                List auditProcessList = new ArrayList();
                JSONObject jsonObjectcz = new JSONObject();
                jsonObjectcz.set("process", "处置"); // 环节名称
                jsonObjectcz.set("examineComment", one.getHandleResult()); // 审核结果
                jsonObjectcz.set("examineTime", one.getHandleTime()); // 审批时间（格式：yyyy-MM-dd HH:mm:ss）
                jsonObjectcz.set("examineer", one.getHandlePerson()); // 审批人
                auditProcessList.add(jsonObjectcz);

                String approvalPerson = one.getApprovalPerson();
                if (!StringUtil.isEmpty(approvalPerson)) {
                    JSONObject jsonObjectsh = new JSONObject();
                    jsonObjectsh.set("process", "审核"); // 环节名称
                    jsonObjectsh.set("examineComment", one.getSupervisorResult()); // 审核结果
                    jsonObjectsh.set("examineTime", one.getSupervisorHandleTime()); // 审批时间（格式：yyyy-MM-dd HH:mm:ss）
                    jsonObjectsh.set("examineer", approvalPerson); // 审批人
                    auditProcessList.add(jsonObjectsh);
                }
                sendJSONObject.set("auditProcessList", auditProcessList); // 审核环节详情
            }

            List materialList = new ArrayList();
            QueryWrapper<BhzLqCailiao> bhzLqCailiaoQueryWrapper = new QueryWrapper<>();
            bhzLqCailiaoQueryWrapper.eq("base_guid", bhzLqBase.getGuid());
            List<BhzLqCailiao> list = bhzLqCailiaoService.list(bhzLqCailiaoQueryWrapper);
            for (BhzLqCailiao bhzLqCailiao : list) {
                JSONObject jsonObjectcl = new JSONObject();

                jsonObjectcl.set("material", bhzLqCailiao.getCailiaoming()); // 材料名称
                jsonObjectcl.set("sj", bhzLqCailiao.getShijiyongliang()); // 每盘实际用量（单位：kg）
                jsonObjectcl.set("th", bhzLqCailiao.getTheoryNumber()); // 每盘理论用量（单位：kg）
                jsonObjectcl.set("sjPb", bhzLqCailiao.getShijipb()); // 每盘实际配比（单位：%）
                jsonObjectcl.set("thPb", bhzLqCailiao.getLilunpb()); // 每盘理论配比（单位：%）
                jsonObjectcl.set("misPst", bhzLqCailiao.getWucha()); // 配比误差（单位：%）
                jsonObjectcl.set("result", bhzLqCailiao.getChaobiaodengji()); // 判断结果（0：合格，1：初级超标，2：中级超标，3：高级超标）
                materialList.add(jsonObjectcl);
            }

            sendJSONObject.set("materialList", materialList); // 材料详情

            String md5 = DigestUtils.md5DigestAsHex(String.valueOf(sendJSONObject).getBytes(StandardCharsets.UTF_8));
            long reqTimes = System.currentTimeMillis();
            //对data进行签名
            String sign = getSign(String.join(":", String.valueOf(reqTimes), md5), SECRET_KEY);
            String s = ACCESS_KEY + ":" + sign;

            String back = HttpRequest.post("http://47.110.39.210:6023/api/alarm/saveAlarmLqProductData")
                    .header("request-time", String.valueOf(reqTimes))
                    .header("request-auth", s)
                    .body(String.valueOf(sendJSONObject))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(back);
            String data = jsonObject1.get("code").toString();
            if ("200".equals(data)) {
                log.info(String.format("义东沥青站预警推送成功!" + id + "Json数据" + sendJSONObject));
                bhzLqBase.setJtjtuuid(uuidStr+"-"+bhzLqBase.getOverproofStatus());
            } else {
                log.info(String.format("义东沥青站预警推送失败!" + id + "Json数据" + sendJSONObject));
            }
            bhzLqBasesService.saveOrUpdate(bhzLqBase);
            sysConfigService.updateSysConfig(JobUtil.JTJT_LQCBLSSJ, id);//根据传过来的唯一值来修改当前判断到的数据id
            pushandreturnService.saveData(id,String.valueOf(sendJSONObject),selectsysconfigone.getRemark(),back);
        }
    }
}
