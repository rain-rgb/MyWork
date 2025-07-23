package org.jeecg.modules.job.ydJob.jiaotoujituan;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.syj.entity.FYaliji;
import com.trtm.iot.syj.entity.FsYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hamcrest.core.Is;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName kzkyJob
 * @Author
 * @Date 2024/10/9 16:54
 * @Version
 * @Description 抗压抗折推送交投集团
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Service
public class kzkyJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService syjzbService;
    @Autowired
    private IFYalijiService yalijiService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String SECRET_KEY = "4028e5cc811396dd01811396dd790000";
    private static final String ACCESS_KEY = "10002";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JTJT_KZKY);//抗折抗压数据推送交投集团
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到数据的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输稳定度数据的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TSyjzb> tSyjzbs = syjzbService.selectListToJTJT(shebeilist, curid);
        if (null == tSyjzbs || tSyjzbs.isEmpty()) {
            log.info(String.format("没有要传输的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 时间格式化器
        for (TSyjzb tSyjzb : tSyjzbs) {

            String wtbh = tSyjzb.getWtbh();
            Integer lq = tSyjzb.getLq();
            LambdaQueryWrapper<TSyjzb> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TSyjzb::getWtbh, wtbh)
                    .eq(TSyjzb::getLq, lq)
                    .eq(TSyjzb::getSylx, "100136")
                    .last("limit 1");
            TSyjzb one = syjzbService.getOne(queryWrapper);

            id = tSyjzb.getId();

            JSONObject sendJSONObject = JSONUtil.createObj();

            String sjqd = tSyjzb.getSjqd();

            sendJSONObject.put("deviceNum", "RC_TJ04_SN_KZKY01");
            int kzresult = 0;
            int kyresult = 0;
            if (one.getPdjg().equals("不合格") || one.getPdjg().equals("无效")) {
                kzresult = 1;
            }
            if (tSyjzb.getPdjg().equals("不合格") || tSyjzb.getPdjg().equals("无效")) {
                kyresult = 1;
            }
            int result = Math.max(kzresult, kyresult);
            sendJSONObject.put("result", result);
            sendJSONObject.put("trialDate", sdf.format(tSyjzb.getSjscsj()));
            sendJSONObject.put("precativeNum", wtbh);
            sendJSONObject.put("compressionResult", kyresult);
            sendJSONObject.put("compressionValue", tSyjzb.getQddbz());
            sendJSONObject.put("compressionDemand", tSyjzb.getSksjqd());
            sendJSONObject.put("bendingDemand", one.getSksjqd());
            sendJSONObject.put("bendingResult", kzresult);
            sendJSONObject.put("bendingValue", one.getQddbz());

            List snKykzCompositionList = new ArrayList();
            LambdaQueryWrapper<FsYaliji> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(FsYaliji::getSyjid, tSyjzb.getSyjid());
            List<FsYaliji> yalijis = yalijiService.list(queryWrapper1);

            LambdaQueryWrapper<FsYaliji> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(FsYaliji::getSyjid, one.getSyjid());
            List<FsYaliji> yalijis2 = yalijiService.list(queryWrapper2);
            JSONObject jsonObject1 = new JSONObject();

            String yskylzky1 = yalijis.get(0).getYskylz();
            String sjgcky1 = yalijis.get(0).getSjgc();
            String yskylzky2 = yalijis.get(1).getYskylz();
            String sjgcky2 = yalijis.get(1).getSjgc();

            String yskylzkz1 = yalijis2.get(0).getYskylz();
            String sjgckz1 = yalijis2.get(0).getSjgc();
            // 调用方法进行合并
            String[] cruveky1 = mergeStrings(yskylzky1, sjgcky1);
            String[] cruveky2 = mergeStrings(yskylzky2, sjgcky2);

            String[] cruvekz = mergeStrings(yskylzkz1, sjgckz1);

            jsonObject1.put("cruveky1", new JSONArray(cruveky1).toString() );
            jsonObject1.put("cruveky2", new JSONArray(cruveky2).toString() );
            jsonObject1.put("cruvekz", new JSONArray(cruvekz).toString() );
            jsonObject1.put("kyhz1", yalijis.get(0).getKylz());
            jsonObject1.put("kyhz2", yalijis.get(1).getKylz());
            jsonObject1.put("kyqd1", yalijis.get(0).getKyqd());
            jsonObject1.put("kyqd2", yalijis.get(1).getKyqd());
            jsonObject1.put("kzhz", yalijis2.get(0).getKylz());
            jsonObject1.put("kzqd", yalijis2.get(0).getKyqd());
            jsonObject1.put("sampleNum", wtbh);

            snKykzCompositionList.add(jsonObject1);

            JSONObject jsonObject2 = new JSONObject();

            String yskylzky3 = yalijis.get(2).getYskylz();
            String sjgcky3 = yalijis.get(2).getSjgc();
            String yskylzky4 = yalijis.get(3).getYskylz();
            String sjgcky4 = yalijis.get(3).getSjgc();

            String yskylzkz2 = yalijis2.get(1).getYskylz();
            String sjgckz2 = yalijis2.get(1).getSjgc();
            // 调用方法进行合并
            String[] cruveky3 = mergeStrings(yskylzky3, sjgcky3);
            String[] cruveky4 = mergeStrings(yskylzky4, sjgcky4);

            String[] cruvekz2 = mergeStrings(yskylzkz2, sjgckz2);

            jsonObject2.put("cruveky1", new JSONArray(cruveky3).toString() );
            jsonObject2.put("cruveky2", new JSONArray(cruveky4).toString() );
            jsonObject2.put("cruvekz", new JSONArray(cruvekz2).toString() );
            jsonObject2.put("kyhz1", yalijis.get(2).getKylz());
            jsonObject2.put("kyhz2", yalijis.get(3).getKylz());
            jsonObject2.put("kyqd1", yalijis.get(2).getKyqd());
            jsonObject2.put("kyqd2", yalijis.get(3).getKyqd());
            jsonObject2.put("kzhz", yalijis2.get(1).getKylz());
            jsonObject2.put("kzqd", yalijis2.get(1).getKyqd());
            jsonObject2.put("sampleNum", wtbh);

            snKykzCompositionList.add(jsonObject2);

            JSONObject jsonObject3 = new JSONObject();

            String yskylzky5 = yalijis.get(4).getYskylz();
            String sjgcky5 = yalijis.get(4).getSjgc();
            String yskylzky6 = yalijis.get(5).getYskylz();
            String sjgcky6 = yalijis.get(5).getSjgc();

            String yskylzkz3 = yalijis2.get(2).getYskylz();
            String sjgckz3 = yalijis2.get(2).getSjgc();
            // 调用方法进行合并
            String[] cruveky5 = mergeStrings(yskylzky5, sjgcky5);
            String[] cruveky6 = mergeStrings(yskylzky6, sjgcky6);

            String[] cruvekz3 = mergeStrings(yskylzkz3, sjgckz3);

            jsonObject3.put("cruveky1", new JSONArray(cruveky5).toString() );
            jsonObject3.put("cruveky2", new JSONArray(cruveky6).toString() );
            jsonObject3.put("cruvekz", new JSONArray(cruvekz3).toString() );
            jsonObject3.put("kyhz1", yalijis.get(4).getKylz());
            jsonObject3.put("kyhz2", yalijis.get(5).getKylz());
            jsonObject3.put("kyqd1", yalijis.get(4).getKyqd());
            jsonObject3.put("kyqd2", yalijis.get(5).getKyqd());
            jsonObject3.put("kzhz", yalijis2.get(2).getKylz());
            jsonObject3.put("kzqd", yalijis2.get(2).getKyqd());
            jsonObject3.put("sampleNum", wtbh);

            snKykzCompositionList.add(jsonObject3);


            sendJSONObject.put("snKykzCompositionList", snKykzCompositionList);


            String md5 = DigestUtils.md5DigestAsHex(String.valueOf(sendJSONObject).getBytes(StandardCharsets.UTF_8));
            long reqTimes = System.currentTimeMillis();
            //对data进行签名
            String sign = getSign(String.join(":", String.valueOf(reqTimes), md5), SECRET_KEY);
            String s = ACCESS_KEY + ":" + sign;

            String body = HttpRequest.post("http://47.110.39.210:6023/api/swProduct/saveSnkykzData")
                    .header("request-time", String.valueOf(reqTimes))
                    .header("request-auth", s)
                    .body(String.valueOf(sendJSONObject))
                    .execute()
                    .body();
            if (body.contains("成功")) {
                log.info(String.format("推送成功!" + id + "Json数据" + sendJSONObject));
                tSyjzb.setIssend(1);
            } else {
                log.info(String.format("推送失败!" + id + "Json数据" + sendJSONObject));
                tSyjzb.setIssend(2);
            }
            syjzbService.updateById(tSyjzb);
            sysConfigService.updateSysConfig(JobUtil.JTJT_KZKY, id);//根据传过来的唯一值来修改当前判断到的数据id
            pushandreturnService.saveData(id, String.valueOf(sendJSONObject), selectsysconfigone.getRemark(), body);
        }
    }

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

    public static String[] mergeStrings(String yskylz, String sjgc) {
        // 将字符串按逗号分割成数组
        String[] yskylzArray = yskylz.split(",");
        String[] sjgcArray = sjgc.split(",");

        // 检查两个数组长度是否一致
        if (yskylzArray.length != sjgcArray.length) {
            throw new IllegalArgumentException("两个字符串的元素数量必须相同");
        }

        // 创建一个新数组来存放合并后的结果
        List<String> mergedList = new ArrayList<>();

        // 使用循环将两个数组的对应元素合并到新列表中
        for (int i = 0; i < yskylzArray.length; i++) {
            mergedList.add(yskylzArray[i] + "," + sjgcArray[i]);
        }

        // 将列表转换为数组
        return mergedList.toArray(new String[0]);
    }
}