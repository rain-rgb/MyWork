package org.jeecg.modules.job.syjjob;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.syj.entity.FsYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.NameValuePair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jeecg.common.util.ConverUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.util.HttpUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 推送盐阜1标试验压力机数据
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class YbYLJJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService syjzbService;
    @Autowired
    private IFYalijiService yalijiService;

    DecimalFormat df = new DecimalFormat("#.##########");//格式化小数


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YC_ONE_SYYLJ);//物资原材料检验批数据推送义东高速
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到盐城一标压力机配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输盐城一标压力机的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1 = Arrays.asList(split);
        List<TSyjzb> syjzbs = syjzbService.selectListSy(curid, strsToList1);
        if (oConvertUtils.isEmpty(syjzbs) || syjzbs.size() == 0) {
            log.info(String.format("没有需要推送的试验数据" + DateUtils.now()));
            return;
        }
        JSONObject sendObject = JSONUtil.createObj();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (TSyjzb syjzb : syjzbs) {
            List<FsYaliji> yalijis = yalijiService.list(new QueryWrapper<FsYaliji>().lambda().eq(FsYaliji::getSyjid, syjzb.getSyjid()));
            sendObject.set("project_id", 10080845);
            sendObject.set("test_type", syjzb.getSylx());
            sendObject.set("test_num", syjzb.getSjbh());
            sendObject.set("sample_num", Integer.valueOf(syjzb.getSjsl()));
            sendObject.set("age_period", syjzb.getLq());
            sendObject.set("shape", 2);
            sendObject.set("size", syjzb.getSjmj());
            sendObject.set("coefficient", Integer.valueOf(syjzb.getZsxs()));
            sendObject.set("strength_grade", syjzb.getSjqd());

            sendObject.set("valid_force_value", 1);
            sendObject.set("valid_intensity", syjzb.getQddbz());
            if (syjzb.getLq() == 7 || syjzb.getLq() == 3) {
                sendObject.set("assessment_result", "");
            } else {
                sendObject.set("assessment_result", syjzb.getPdjg());
            }
            sendObject.set("test_date", syjzb.getSyrq().substring(0, 10));
            sendObject.set("machine", syjzb.getSbbh());
            sendObject.set("tester", syjzb.getCzry());
            sendObject.set("tag", "1");
            sendObject.set("project_position", syjzb.getCjmc());

            sendObject.set("test_block_type", "标准养护");
            sendObject.set("design_strength_grade", syjzb.getSjqd());

            sendObject.set("construction_time",syjzb.getZzrq());

            List list = new ArrayList();
            for (FsYaliji yaliji : yalijis) {
                JSONObject obj = JSONUtil.createObj();
                obj.set("order_num", yaliji.getSjxh());
                obj.set("breed", "");
                obj.set("transform_coefficient", Integer.valueOf(syjzb.getZsxs()));
                obj.set("load", yaliji.getKylz());
                obj.set("stress", yaliji.getKyqd());
                obj.set("test_begin", yaliji.getSysj().substring(0, 19));
                obj.set("test_end", yaliji.getWcsj().substring(0, 19));
                String[] yskylz = yaliji.getYskylz().split(",");
                String[] sjgc = yaliji.getSjgc().split(",");
                List sjgclist = new ArrayList();
                for (int i = 0; i < sjgc.length; i++) {
                    String s = sjgc[i];
                    BigDecimal decimal = new BigDecimal(s);
                    sjgclist.add(decimal);
                }
                List yskylzlist = new ArrayList();
                for (int i = 0; i < yskylz.length; i++) {
                    String s = yskylz[i];
                    BigDecimal decimal = new BigDecimal(s);
                    yskylzlist.add(decimal);
                }
                JSONObject obj1 = JSONUtil.createObj();

                obj1.set("Time", sjgclist);
                obj1.set("Load", yskylzlist);
                obj1.set("Posi", new Arrays[1]);
                obj1.set("Extn", new Arrays[1]);

                obj.set("process_data", obj1);
                obj.set("picture", "");
                list.add(obj);
            }

            sendObject.set("Details", list);
//            log.info("传送数据为  ===================={}", sendObject);
            SecureRandom random = new SecureRandom();
            String s = String.valueOf(random.nextInt());
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtils.getDate());
//            funl506oq73aw94bsvjcdi8pgexztky1
            String str = "appid=cscec8-gdjt2&data=" + sendObject + "&format=json&method=upload.pressmachine&nonce=" + s + "&timestamp=" + date + "&version=1.0&appsecret=funl506oq73aw94bsvjcdi8pgexztky1";
//            log.info("str=   {}", str);
            byte[] digest = MD5.create().digest(str.toLowerCase());
            String binaryStr = ConverUtils.byteToHex(digest);

            String url = "http://app.cscec8b.com.cn/acquisition/netty/httpDataServer/dataInterface/dataServer.shtml";
            List<NameValuePair> pairList = new ArrayList<>();
            pairList.add(new BasicNameValuePair("appid", "cscec8-gdjt2"));
            pairList.add(new BasicNameValuePair("data", String.valueOf(sendObject)));
            pairList.add(new BasicNameValuePair("format", "json"));
            pairList.add(new BasicNameValuePair("method", "upload.pressMachine"));
            pairList.add(new BasicNameValuePair("nonce", s));
            pairList.add(new BasicNameValuePair("timestamp", date));
            pairList.add(new BasicNameValuePair("version", "1.0"));
            pairList.add(new BasicNameValuePair("sign", binaryStr));
            pairList.add(new BasicNameValuePair("appsecret", "funl506oq73aw94bsvjcdi8pgexztky1"));

            String json = HttpUtils.postWithParamsForString(url, pairList);
//            log.info("digest=  {}", digest);
//            log.info("binaryStr=  {}", binaryStr);
            JSONObject jsonObj = new JSONObject(json);
            String code = jsonObj.get("code").toString();
            if (Integer.valueOf(code) == 0) {
                log.info(String.format("中铁8局试验室，压力机数据推送成功!推送数据id=  " + syjzb.getId() + "\n Json数据=========" + sendObject));
            } else {
                log.info(String.format("中铁8局试验室，压力机数据推送失败!" + syjzb.getId() + "Json数据" + sendObject));
            }
            selectsysconfigone.setCurid(syjzb.getId());
            sysConfigService.updateById(selectsysconfigone);
        }
    }

}
