package org.jeecg.modules.job.sutaijob;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.yajiangss.entity.TrYajiangSS;
import com.trtm.iot.yajiangss.service.ITrYajiangSSService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @ClassName SHYJyjJob：
 * @Description 上海有间压浆数据推送
 * @Author 55314
 * @Date 2023/2/24 9:04
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SHYJyjJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private ITrYajiangSService yajiangSService;
    @Autowired
    private ITrYajiangSSService yajiangSSService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "https://api.ilabx.cn/iot-service/interface/tongWang/DataInfa/SWZLYJ/SaveSWZNYJDataInfa";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SHYJ_YJ);//上海有间张拉数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到上海有间压浆的配置信息" + DateUtils.now()));
            return;
        }
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输上海有间压浆的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrYajiangM> trYajiangMS = yajiangMService.selectListToSHYJ(shebeilist, curid);
        if (null == trYajiangMS || trYajiangMS.size() == 0) {
            log.info(String.format("暂无上海有间压浆未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        //压浆主表
        for (TrYajiangM yajiangM : trYajiangMS){
            id = yajiangM.getId();
            JSONObject sendJson = new JSONObject();
            JSONObject yajiangMJson = new JSONObject();
            List list = new ArrayList();

            yajiangMJson.set("syjid",yajiangM.getSyjid());
            yajiangMJson.set("shebeibianhao",yajiangM.getYjsbbh());
            yajiangMJson.set("sgdw",yajiangM.getSgdw());
            yajiangMJson.set("jldw",yajiangM.getJldw());
            yajiangMJson.set("htbh",yajiangM.getHtbh());
            yajiangMJson.set("gcmc",yajiangM.getGcmc());
            yajiangMJson.set("yjsj",yajiangM.getYjsj());
            yajiangMJson.set("zhbw",yajiangM.getZhbw());
            yajiangMJson.set("sgbw",yajiangM.getSgbw());
            yajiangMJson.set("gjjg",yajiangM.getGjjg());
            yajiangMJson.set("gjbh",yajiangM.getGjbh());
            yajiangMJson.set("qw",yajiangM.getQw());
            yajiangMJson.set("cjsjl",yajiangM.getCjsjl());
            yajiangMJson.set("cpzjl",yajiangM.getCpzjl());
            yajiangMJson.set("sw",yajiangM.getSw());
            yajiangMJson.set("shuijiaobi",yajiangM.getShuijiaobi());
            yajiangMJson.set("snyl",yajiangM.getSnyl());
            yajiangMJson.set("yjwd",yajiangM.getYjwd());
            yajiangMJson.set("msl",yajiangM.getMsl());
            yajiangMJson.set("beiyong",yajiangM.getBeiyong());
            yajiangMJson.set("yjsbbh",yajiangM.getYjsbbh());
            yajiangMJson.set("lblx",yajiangM.getLblx());
            yajiangMJson.set("lianghao",yajiangM.getLianghao());
            yajiangMJson.set("zlsj",yajiangM.getZlsj());
            yajiangMJson.set("yajiangji",yajiangM.getYajiangji());
            yajiangMJson.set("snmc",yajiangM.getSnmc());
            yajiangMJson.set("kongdaoshu",yajiangM.getKongdaoshu());
            yajiangMJson.set("yajiangfangxiang",yajiangM.getYajiangfang());
            yajiangMJson.set("yajiangbuzhou",yajiangM.getYajiangbuzh());
            yajiangMJson.set("yajiangguocheng",yajiangM.getYajiangguoc());
            yajiangMJson.set("chushisudu",yajiangM.getChushisudu());
            yajiangMJson.set("liudongdu",yajiangM.getLiudongdu());
            yajiangMJson.set("memo",yajiangM.getMemo());
            yajiangMJson.set("status",yajiangM.getStatus());

            list.add(yajiangMJson);
            sendJson.set("Method","SWYJSY_M");
            sendJson.set("data",list);
            String encode = base64(sendJson.toString());
            //推送
            String result = HttpRequest.post(url)
                    .header("Content-Type", "text/html")
                    .body(encode)
                    .execute()
                    .body();
            pushandreturnService.saveData(id,String.valueOf(sendJson),selectsysconfigone.getRemark(),result);

            sysConfigService.updateSysConfig(JobUtil.SHYJ_YJ, id);//根据传过来的唯一值来修改当前判断到的数据id
            //压浆子表
            QueryWrapper<TrYajiangS> yajiangSQueryWrapper = new QueryWrapper<>();
            yajiangSQueryWrapper.eq("syjid",yajiangM.getSyjid());
            List<TrYajiangS> TrYajiangSs = yajiangSService.list(yajiangSQueryWrapper);

            List Slist = new ArrayList();
            JSONObject sendSJson = new JSONObject();
            for (TrYajiangS trYajiangS : TrYajiangSs){

                JSONObject trYajiangSJson = new JSONObject();

                trYajiangSJson.set("f_guid",trYajiangS.getFguid());
                trYajiangSJson.set("syjid",trYajiangS.getSyjid());
                trYajiangSJson.set("yajiangsj",trYajiangS.getYajiangsj());
                trYajiangSJson.set("kongdao",trYajiangS.getKongdao());
                trYajiangSJson.set("yajiangmoshi",trYajiangS.getYajiangmosh());
                trYajiangSJson.set("peihebi",trYajiangS.getPeihebi());
                trYajiangSJson.set("shuijiaobi",trYajiangS.getShuijiaobi());
                trYajiangSJson.set("jiaobansj",trYajiangS.getJiaobansj());
                trYajiangSJson.set("starttime",trYajiangS.getStarttime());
                trYajiangSJson.set("endtime",trYajiangS.getEndtime());
                trYajiangSJson.set("jinjiangyali",trYajiangS.getJinjiangyal());
                trYajiangSJson.set("fanjiangyali",trYajiangS.getFanjiangyal());
                trYajiangSJson.set("chixushijian",trYajiangS.getChixushijia());
                trYajiangSJson.set("jinjiangshuliang",trYajiangS.getJinjiangshu());
                trYajiangSJson.set("fanjiangliang",trYajiangS.getFanjiangyal());
                trYajiangSJson.set("zkyl",trYajiangS.getZkyl());
                trYajiangSJson.set("tongguo",trYajiangS.getTongguo());
                trYajiangSJson.set("yjcs",trYajiangS.getYjcs());
                trYajiangSJson.set("hege",trYajiangS.getHege());
                trYajiangSJson.set("holeid",trYajiangS.getHoleid());
                trYajiangSJson.set("status",trYajiangS.getStatus());
                trYajiangSJson.set("yjcs",trYajiangS.getYjcs());
                trYajiangSJson.set("mjqk",trYajiangS.getMjqk());
                trYajiangSJson.set("cbzt",trYajiangS.getOverproofStatus());

                Slist.add(trYajiangSJson);

                //压浆过程表
                QueryWrapper<TrYajiangSS> yajiangSSQueryWrapper = new QueryWrapper<>();
                yajiangSSQueryWrapper.eq("holeid",trYajiangS.getHoleid());
                List<TrYajiangSS> trYajiangSSS = yajiangSSService.list(yajiangSSQueryWrapper);

                List SSlist = new ArrayList();
                JSONObject sendSSJson = new JSONObject();
                for (TrYajiangSS yajiangSS : trYajiangSSS) {

                    JSONObject trYajiangSSJson = new JSONObject();

                    trYajiangSSJson.set("sid",yajiangSS.getSid());
                    trYajiangSSJson.set("shebeibianhao",yajiangSS.getShebeibianh());
                    trYajiangSSJson.set("holeid",yajiangSS.getHoleid());
                    trYajiangSSJson.set("jlsj",yajiangSS.getJlsj());
                    trYajiangSSJson.set("cysj",yajiangSS.getCysj());
                    trYajiangSSJson.set("zt",yajiangSS.getZt());
                    trYajiangSSJson.set("jjyl",yajiangSS.getJjyl());
                    trYajiangSSJson.set("fjyl",yajiangSS.getFjyl());
                    trYajiangSSJson.set("jjl",yajiangSS.getJjl());
                    trYajiangSSJson.set("fjl",yajiangSS.getFjl());

                    SSlist.add(trYajiangSSJson);
                }
                sendSSJson.set("Method","SWYJSY_S_S");
                sendSSJson.set("data",SSlist);
                String Sencode = base64(sendSSJson.toString());
                //推送
                String Sresult = HttpRequest.post(url)
                        .header("Content-Type", "text/html")
                        .body(Sencode)
                        .execute()
                        .body();
                pushandreturnService.saveData(id,String.valueOf(sendJson),selectsysconfigone.getRemark(),Sresult);
            }
            sendSJson.set("Method","SWYJSY_S");
            sendSJson.set("data",Slist);
            String Mencode = base64(sendSJson.toString());
            //推送
            String Mresult = HttpRequest.post(url)
                    .header("Content-Type", "text/html")
                    .body(Mencode)
                    .execute()
                    .body();
            pushandreturnService.saveData(id,String.valueOf(sendJson),selectsysconfigone.getRemark(),Mresult);
        }
    }

    public static String base64(String str) {
        byte[] bytes = str.getBytes();

        //Base64 加密
        String encoded = Base64.getEncoder().encodeToString(bytes);
        return encoded;
//        System.out.println("Base 64 加密后：" + encoded);
//
//        //Base64 解密
//        byte[] decoded = Base64.getDecoder().decode(encoded);
//
//        String decodeStr = new String(decoded);
//        System.out.println("Base 64 解密后：" + decodeStr);
//
//        System.out.println();


    }

}
