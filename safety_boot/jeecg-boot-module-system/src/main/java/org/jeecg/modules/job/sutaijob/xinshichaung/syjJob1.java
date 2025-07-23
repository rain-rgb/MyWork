package org.jeecg.modules.job.sutaijob.xinshichaung;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.syj.entity.FWangnj;
import com.trtm.iot.syj.entity.FsYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.IFWangnjService;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.xkcoding.http.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName syjJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/10/20 14:37
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class syjJob1 implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService itSyjzbService;
    @Autowired
    private IFYalijiService yalijiService;
    @Autowired
    private IFWangnjService wangnjService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    private String tokenurl = "http://115.29.173.88/SysConfigWebApi/api/v1/Visitor/GetToken";
    private  String url = "http://115.29.173.88/IoTWebApi/api/v1/Visitor/UploadForeignTrialData";

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_SYJ1);//苏台三标张拉
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台三标试验机的配置信息" + DateUtils.now()));
            return;
        }

        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject1 = JSONUtil.parseObj(extra);
        if (null == jsonObject1 || jsonObject1.isEmpty()) {
            log.info(String.format("没有配置要传输苏台三标试验机的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject1.getStr("shebeilist");
        //试验机主表
        List<TSyjzb> tSyjzbs = itSyjzbService.selectStsqList1(shebeilist, curid);
        if (null == tSyjzbs || tSyjzbs.size() == 0) {
            log.info(String.format("暂无苏台三标试验机未推送数据" + DateUtils.now()));
            return;
        } else {
            log.info(String.format("苏台三标试验机推送开始" + DateUtils.now()));
        }
        int id = 0;

        for (TSyjzb tSyjzb : tSyjzbs) {
            id = tSyjzb.getId();
            String sylx = tSyjzb.getSylx();
            String sbbh = tSyjzb.getSbbh();
            String getsbid = getsbid(sbbh);
            int sjsl = Integer.parseInt(tSyjzb.getSjsl());
            String body = "";
            if (StringUtil.isNotEmpty(sylx)) {
                if ("100014".equals(sylx)) {
                    QueryWrapper<FsYaliji> yalijiQueryWrapper = new QueryWrapper<>();
                    yalijiQueryWrapper.eq("syjid", tSyjzb.getSyjid());
                    List<FsYaliji> yalijiList = yalijiService.list(yalijiQueryWrapper);

                    body = postyl(tSyjzb, yalijiList, getsbid);

                }
                if ("100133".equals(sylx) || "100131".equals(sylx)) {
                    QueryWrapper<FsYaliji> yalijiQueryWrapper = new QueryWrapper<>();
                    yalijiQueryWrapper.eq("syjid", tSyjzb.getSyjid());
                    List<FsYaliji> yalijiList = yalijiService.list(yalijiQueryWrapper);
                    body = postyl1(tSyjzb, yalijiList, getsbid);
                }
                if ("100047".equals(sylx) || "100049".equals(sylx)) {
                    QueryWrapper<FWangnj> wangnjQueryWrapper = new QueryWrapper<>();
                    wangnjQueryWrapper.eq("syjid", tSyjzb.getSyjid());
                    List<FWangnj> wangnjList = wangnjService.list(wangnjQueryWrapper);
                    body = postwn(tSyjzb, wangnjList, sylx, getsbid);
                }
                if ("100048".equals(sylx)) {
                    QueryWrapper<FWangnj> wangnjQueryWrapper = new QueryWrapper<>();
                    wangnjQueryWrapper.eq("syjid", tSyjzb.getSyjid());
                    List<FWangnj> wangnjList = wangnjService.list(wangnjQueryWrapper);
                    body = postwn1(tSyjzb, wangnjList, getsbid);
                }
                if ("100135".equals(sylx)) {
                    QueryWrapper<FsYaliji> yalijiQueryWrapper = new QueryWrapper<>();
                    yalijiQueryWrapper.eq("syjid", tSyjzb.getSyjid());
                    List<FsYaliji> yalijiList = yalijiService.list(yalijiQueryWrapper);
                    body = postsn1(tSyjzb, yalijiList, getsbid);
                }
                if ("100136".equals(sylx)) {
                    QueryWrapper<FsYaliji> yalijiQueryWrapper = new QueryWrapper<>();
                    yalijiQueryWrapper.eq("syjid", tSyjzb.getSyjid());
                    List<FsYaliji> yalijiList = yalijiService.list(yalijiQueryWrapper);
                    body = postsn2(tSyjzb, yalijiList, getsbid);
                }
            } else {
                tSyjzb.setIssend(4);
                log.info("实验类型为空！");
            }
            if (body.contains("success")){
                tSyjzb.setIssend(1);
                log.info("苏台推送新视窗成功！");
            }else {
                tSyjzb.setIssend(2);
                log.info("苏台推送新视窗失败！");
            }
            itSyjzbService.saveOrUpdate(tSyjzb);
            sysConfigService.updateSysConfig(JobUtil.ST_SYJ1, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
    public String postsn2(TSyjzb tSyjzb, List<FsYaliji> yalijiList, String sbbh) {
        String token = token(tSyjzb.getSbbh());
        JSONObject sendJson = new JSONObject();
        List list = new ArrayList();
        for (FsYaliji yaliji : yalijiList) {
            JSONObject jsonObject = JSONUtil.createObj();
            jsonObject.set("设备编号", tSyjzb.getSbbh());
            jsonObject.set("试验编号", tSyjzb.getSjbh());
            jsonObject.set("强度等级", tSyjzb.getSjqd());
            jsonObject.set("制件日期", tSyjzb.getZzrq());
            jsonObject.set("试验日期及时间", tSyjzb.getSyrq());
            jsonObject.set("龄期", tSyjzb.getLq());
            jsonObject.set("抗压试件编号", yaliji.getSjxh());
            jsonObject.set("抗压试件数量", tSyjzb.getSjsl());
            String wtbh = String.valueOf(tSyjzb.getWtbh());
            String sjbh = String.valueOf(tSyjzb.getSjbh());
            if (wtbh.contains("YP")) {
                jsonObject.set("试验编号", wtbh);
            } else {
                if (sjbh.contains("YP")) {
                    jsonObject.set("试验编号", sjbh);
                } else {
                    jsonObject.set("试验编号", wtbh);
                }

            }
            jsonObject.set("抗压试件编号", "");
            jsonObject.set("抗压试件数量", "");
            jsonObject.set("抗折试件编号", yaliji.getSjxh());
            jsonObject.set("抗折试件数量", tSyjzb.getSjsl());
            jsonObject.set("抗压力值单位", "");
            jsonObject.set("抗压破坏荷载", "");
            jsonObject.set("抗折破坏荷载", yaliji.getKylz());
            double kylz = Double.valueOf(yaliji.getKylz().toString());
            if (kylz > 1000) {
                jsonObject.set("力值单位", "N");
            } else {
                jsonObject.set("力值单位", "kN");
            }
            jsonObject.set("抗压结果值", "");
            jsonObject.set("抗折结果值", yaliji.getKyqd());

//            String yskylz = yaliji.getYskylz();
//            String sjgc = yaliji.getSjgc();
//            String[] yskylzArray = yskylz.split(",");
//            String[] sjgcArray = sjgc.split(",");
//
//            StringBuilder result = new StringBuilder();
//
//            int length = Math.min(yskylzArray.length, sjgcArray.length);
//            for (int i = 0; i < length; i++) {
//                result.append(yskylzArray[i]).append(",").append(sjgcArray[i]);
//                if (i < length - 1) {
//                    result.append(";");
//                }
//            }
//
//            jsonObject.set("曲线", result);
            list.add(jsonObject);
        }
        sendJson.set("data", list);
        sendJson.set("facilityCode", sbbh);
        sendJson.set("dataKey", tSyjzb.getSyjid());
        sendJson.set("trialTypeCode", "JGLQ04006");
        sendJson.set("trialTypeName", "水泥胶砂强度");
        sendJson.set("handType", "add");
        sendJson.set("reportNo", tSyjzb.getSjbh());
        String body = HttpRequest.post(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("token", token)
                .body(String.valueOf(sendJson))
                .timeout(20000)
                .execute().body();
        pushandreturnService.saveData(tSyjzb.getId(), String.valueOf(sendJson),"苏台五标新视窗",body);

        return body;
    }

    public String postsn1(TSyjzb tSyjzb, List<FsYaliji> yalijiList, String sbbh) {
        String token = token(tSyjzb.getSbbh());
        JSONObject sendJson = new JSONObject();
        List list = new ArrayList();
        for (FsYaliji yaliji : yalijiList) {
            JSONObject jsonObject = JSONUtil.createObj();
            jsonObject.set("设备编号", tSyjzb.getSbbh());
            jsonObject.set("试验编号", tSyjzb.getSjbh());
            jsonObject.set("强度等级", tSyjzb.getSjqd());
            jsonObject.set("制件日期", tSyjzb.getZzrq());
            jsonObject.set("试验日期及时间", tSyjzb.getSyrq());
            jsonObject.set("龄期", tSyjzb.getLq());
            jsonObject.set("抗压试件编号", yaliji.getSjxh());
            jsonObject.set("抗压试件数量", tSyjzb.getSjsl());
            String wtbh = String.valueOf(tSyjzb.getWtbh());
            String sjbh = String.valueOf(tSyjzb.getSjbh());
            if (wtbh.contains("YP")){
                jsonObject.set("试验编号", wtbh);
            }else {
                if (sjbh.contains("YP")){
                    jsonObject.set("试验编号", sjbh);
                }else {
                    jsonObject.set("试验编号", wtbh);
                }

            }
            jsonObject.set("抗折试件编号", "");
            jsonObject.set("抗折试件数量", "");
            jsonObject.set("抗折力值单位", "");
            jsonObject.set("抗压破坏荷载", yaliji.getKylz());
            double kylz = Double.valueOf(yaliji.getKylz().toString());
            if (kylz > 1000) {
                jsonObject.set("力值单位", "N");
            } else {
                jsonObject.set("力值单位", "kN");
            }
            jsonObject.set("抗折破坏荷载", "");
            jsonObject.set("抗压结果值", yaliji.getKyqd());
            jsonObject.set("抗折结果值", "");
//            String yskylz = yaliji.getYskylz();
//            String sjgc = yaliji.getSjgc();
//            String[] yskylzArray = yskylz.split(",");
//            String[] sjgcArray = sjgc.split(",");
//
//            StringBuilder result = new StringBuilder();
//
//            int length = Math.min(yskylzArray.length, sjgcArray.length);
//            for (int i = 0; i < length; i++) {
//                result.append(yskylzArray[i]).append(",").append(sjgcArray[i]);
//                if (i < length - 1) {
//                    result.append(";");
//                }
//            }
//
//            jsonObject.set("曲线", result);
            list.add(jsonObject);
        }
        sendJson.set("data", list);
        sendJson.set("facilityCode", sbbh);
        sendJson.set("dataKey", tSyjzb.getSyjid());
        sendJson.set("trialTypeCode", "JGLQ04006");
        sendJson.set("trialTypeName", "水泥胶砂强度");
        sendJson.set("handType", "add");
        sendJson.set("reportNo", tSyjzb.getSjbh());
        String body = HttpRequest.post(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("token", token)
                .body(String.valueOf(sendJson))
                .timeout(20000)
                .execute().body();
        pushandreturnService.saveData(tSyjzb.getId(), String.valueOf(sendJson),"苏台五标新视窗",body);
        return body;
    }

    public String postwn(TSyjzb tSyjzb, List<FWangnj> wangnjList, String sylx, String sbbh) {
        String token = token(tSyjzb.getSbbh());
        JSONObject sendJson = new JSONObject();
        List list = new ArrayList();
        for (FWangnj wangnj : wangnjList) {
            JSONObject jsonObject = JSONUtil.createObj();
            jsonObject.set("设备编号", tSyjzb.getSbbh());
            jsonObject.set("试验编号", tSyjzb.getSjbh());
            jsonObject.set("公称直径", tSyjzb.getGczj());
            jsonObject.set("规格牌号", tSyjzb.getPzbm());
            jsonObject.set("试验日期", tSyjzb.getSyrq());
            jsonObject.set("试件序号", wangnj.getSjxh());
            jsonObject.set("试件数量", tSyjzb.getSjsl());
            String wtbh = String.valueOf(tSyjzb.getWtbh());
            String sjbh = String.valueOf(tSyjzb.getSjbh());
            if (wtbh.contains("YP")) {
                jsonObject.set("试验编号", wtbh);
            } else {
                if (sjbh.contains("YP")) {
                    jsonObject.set("试验编号", sjbh);
                } else {
                    jsonObject.set("试验编号", wtbh);
                }
            }
            jsonObject.set("设备量程", "");
            jsonObject.set("最大力值", wangnj.getLz());
            jsonObject.set("屈服点力值", wangnj.getQflz());
            double kylz = Double.valueOf(wangnj.getLz().toString());
            if (kylz > 1000) {
                jsonObject.set("力值单位", "N");
            } else {
                jsonObject.set("力值单位", "kN");
            }
//            String yskylz = wangnj.getLzgc();
//            String sjgc = wangnj.getSjgc();
//            String[] yskylzArray = yskylz.split(",");
//            String[] sjgcArray = sjgc.split(",");
//
//            StringBuilder result = new StringBuilder();
//
//            int length = Math.min(yskylzArray.length, sjgcArray.length);
//            for (int i = 0; i < length; i++) {
//                result.append(yskylzArray[i]).append(",").append(sjgcArray[i]);
//                if (i < length - 1) {
//                    result.append(";");
//                }
//            }
//
//            jsonObject.set("曲线", result);
            list.add(jsonObject);
        }
        sendJson.set("data", list);
        sendJson.set("facilityCode", sbbh);
        sendJson.set("dataKey", tSyjzb.getSyjid());
        if ("100047".equals(sylx)) {
            sendJson.set("trialTypeCode", "JGLQ15003");
            sendJson.set("trialTypeName", "钢筋拉伸");
        } else {
            sendJson.set("trialTypeCode", "JGLQ150101a");
            sendJson.set("trialTypeName", "钢材与连接接头单向拉伸");
        }
        sendJson.set("handType", "add");
        sendJson.set("reportNo", tSyjzb.getSjbh());
        String body = HttpRequest.post(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("token", token)
                .body(String.valueOf(sendJson))
                .timeout(20000)
                .execute().body();
        pushandreturnService.saveData(tSyjzb.getId(), String.valueOf(sendJson),"苏台五标新视窗",body);
        return body;

    }

    public String postwn1(TSyjzb tSyjzb, List<FWangnj> wangnjList, String sbbh) {
        String token = token(tSyjzb.getSbbh());
        JSONObject sendJson = new JSONObject();
        List list = new ArrayList();
        for (FWangnj wangnj : wangnjList) {
            JSONObject jsonObject = JSONUtil.createObj();
            jsonObject.set("设备编号", tSyjzb.getSbbh());
            jsonObject.set("公称直径", tSyjzb.getGczj());
            jsonObject.set("试验日期", tSyjzb.getSyrq());
            jsonObject.set("试件序号", wangnj.getSjxh());
            jsonObject.set("试件数量", tSyjzb.getSjsl());
            jsonObject.set("极限荷载", wangnj.getLz());
//            String yskylz = wangnj.getLzgc();
//            String sjgc = wangnj.getSjgc();
//            String[] yskylzArray = yskylz.split(",");
//            String[] sjgcArray = sjgc.split(",");
//
//            StringBuilder result = new StringBuilder();
//
//            int length = Math.min(yskylzArray.length, sjgcArray.length);
//            for (int i = 0; i < length; i++) {
//                result.append(yskylzArray[i]).append(",").append(sjgcArray[i]);
//                if (i < length - 1) {
//                    result.append(";");
//                }
//            }
//
//            jsonObject.set("曲线", result);
            String wtbh = String.valueOf(tSyjzb.getWtbh());
            String sjbh = String.valueOf(tSyjzb.getSjbh());
            if (wtbh.contains("YP")) {
                jsonObject.set("试验编号", wtbh);
            } else {
                if (sjbh.contains("YP")) {
                    jsonObject.set("试验编号", sjbh);
                } else {
                    jsonObject.set("试验编号", wtbh);
                }

            }
            list.add(jsonObject);
        }
        sendJson.set("data", list);
        sendJson.set("facilityCode", sbbh);
        sendJson.set("dataKey", tSyjzb.getSyjid());
        sendJson.set("trialTypeCode", "N060020");
        sendJson.set("trialTypeName", "钢筋焊接拉伸");
        sendJson.set("handType", "add");
        sendJson.set("reportNo", tSyjzb.getSjbh());
        String body = HttpRequest.post(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("token", token)
                .body(String.valueOf(sendJson))
                .timeout(20000)
                .execute().body();
        pushandreturnService.saveData(tSyjzb.getId(), String.valueOf(sendJson),"苏台五标新视窗",body);
        return body;
    }

    @SneakyThrows
    public String postyl(TSyjzb tSyjzb, List<FsYaliji> yalijiList, String sbbh) {
        String token = token(tSyjzb.getSbbh());
        JSONObject sendJson = new JSONObject();
        List list = new ArrayList();
        for (FsYaliji yaliji : yalijiList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("设备编号", tSyjzb.getSbbh());
            jsonObject.set("工程部位", tSyjzb.getSgbw());
            jsonObject.set("试件尺寸", tSyjzb.getSjcc());
            jsonObject.set("强度等级", tSyjzb.getSjqd());
            jsonObject.set("浇筑日期", tSyjzb.getZzrq());
            jsonObject.set("取样或成型日期", tSyjzb.getZzrq());
            jsonObject.set("试验日期", tSyjzb.getSyrq());
            String wtbh = String.valueOf(tSyjzb.getWtbh());
            String sjbh = String.valueOf(tSyjzb.getSjbh());
            if (wtbh.contains("YP")) {
                jsonObject.set("试验编号", wtbh);
            } else {
                if (sjbh.contains("YP")) {
                    jsonObject.set("试验编号", sjbh);
                } else {
                    jsonObject.set("试验编号", wtbh);
                }
            }
            jsonObject.set("龄期", tSyjzb.getLq());
            jsonObject.set("试件序号", yaliji.getSjxh());
            jsonObject.set("试件数量", tSyjzb.getSjsl());
            jsonObject.set("试验状态", "已完成");
            jsonObject.set("试验开始时间", yaliji.getSysj());
            jsonObject.set("试验结束时间", yaliji.getWcsj());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Object sysj1 = yaliji.getSysj();
            int cxsj = 0;
            if (sysj1 == null) {
                cxsj = 60;
            } else {
                Date sysj = dateFormat.parse(yaliji.getSysj());
                Date wcsj = dateFormat.parse(yaliji.getWcsj());
                int time = (int) sysj.getTime();
                int time1 = (int) wcsj.getTime();
                cxsj = (time1 - time) / 1000;
            }
            jsonObject.set("试验持续时间", cxsj);
            jsonObject.set("试验最大速率", "");
            jsonObject.set("设备量程", "");
            jsonObject.set("破坏荷载", yaliji.getKylz());
            double kylz = Double.valueOf(yaliji.getKylz().toString());
            if (kylz > 1000) {
                jsonObject.set("力值单位", "N");
            } else {
                jsonObject.set("力值单位", "kN");
            }
            jsonObject.set("结果值", yaliji.getKyqd());
//            String yskylz = yaliji.getYskylz();
//            String sjgc = yaliji.getSjgc();
//            String[] yskylzArray = yskylz.split(",");
//            String[] sjgcArray = sjgc.split(",");
//
//            StringBuilder result = new StringBuilder();
//
//            int length = Math.min(yskylzArray.length, sjgcArray.length);
//            for (int i = 0; i < length; i++) {
//                result.append(yskylzArray[i]).append(",").append(sjgcArray[i]);
//                if (i < length - 1) {
//                    result.append(";");
//                }
//            }
//
//            jsonObject.set("曲线", result);
            list.add(jsonObject);
        }
        sendJson.set("data", list);
        sendJson.set("facilityCode", sbbh);
        sendJson.set("dataKey", tSyjzb.getSyjid());
        sendJson.set("trialTypeCode", "JGLQ05005");
        sendJson.set("trialTypeName", "水泥混凝土抗压");
        sendJson.set("handType", "add");
        sendJson.set("reportNo", tSyjzb.getSjbh());
        System.out.println(sendJson);
        String body = HttpRequest.post(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("token", token)
                .body(String.valueOf(sendJson))
                .timeout(20000)
                .execute().body();
        pushandreturnService.saveData(tSyjzb.getId(), String.valueOf(sendJson),"苏台五标新视窗",body);
        return body;
    }

    @SneakyThrows
    public String postyl1(TSyjzb tSyjzb, List<FsYaliji> yalijiList, String sbbh) {
        String token = token(tSyjzb.getSbbh());
        JSONObject sendJson = new JSONObject();
        List list = new ArrayList();
        for (FsYaliji syjson : yalijiList) {
            JSONObject jsonObject = JSONUtil.createObj();
            jsonObject.set("设备编号", tSyjzb.getSbbh());
            jsonObject.set("工程部位", tSyjzb.getSgbw());
            jsonObject.set("试件尺寸", tSyjzb.getSjcc());
            jsonObject.set("强度等级", tSyjzb.getSjqd());
            jsonObject.set("成型日期", tSyjzb.getZzrq());
            jsonObject.set("试验日期", tSyjzb.getSyrq());
            String wtbh = String.valueOf(tSyjzb.getWtbh());
            String sjbh = String.valueOf(tSyjzb.getSjbh());
            if (wtbh.contains("YP")) {
                jsonObject.set("试验编号", wtbh);
            } else {
                if (sjbh.contains("YP")) {
                    jsonObject.set("试验编号", sjbh);
                } else {
                    jsonObject.set("试验编号", wtbh);
                }

            }
            jsonObject.set("龄期", tSyjzb.getLq());
            jsonObject.set("试件序号", syjson.getSjxh());
            jsonObject.set("试件数量", tSyjzb.getSjsl());
            jsonObject.set("试验状态", "已完成");
            jsonObject.set("试验开始时间", syjson.getSysj());
            jsonObject.set("试验结束时间", syjson.getWcsj());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Object sysj1 = syjson.getSysj();
            int cxsj = 0;
            if (sysj1 == null) {
                cxsj = 60;
            } else {
                Date sysj = dateFormat.parse(syjson.getSysj());
                Date wcsj = dateFormat.parse(syjson.getWcsj());
                int time = (int) sysj.getTime();
                int time1 = (int) wcsj.getTime();
                cxsj = (time1 - time) / 1000;
            }
            jsonObject.set("试验持续时间", cxsj);
            jsonObject.set("试验最大速率", "");
            jsonObject.set("设备量程", "");
            jsonObject.set("破坏荷载", syjson.getKylz());
            double kylz = Double.valueOf(syjson.getKylz().toString());
            if (kylz > 1000) {
                jsonObject.set("力值单位", "N");
            } else {
                jsonObject.set("力值单位", "kN");
            }
            jsonObject.set("结果值", syjson.getKyqd());
//            String yskylz = syjson.getYskylz();
//            String sjgc = syjson.getSjgc();
//            String[] yskylzArray = yskylz.split(",");
//            String[] sjgcArray = sjgc.split(",");
//
//            StringBuilder result = new StringBuilder();
//
//            int length = Math.min(yskylzArray.length, sjgcArray.length);
//            for (int i = 0; i < length; i++) {
//                result.append(yskylzArray[i]).append(",").append(sjgcArray[i]);
//                if (i < length - 1) {
//                    result.append(";");
//                }
//            }
//
//            jsonObject.set("曲线", result);
            list.add(jsonObject);
        }
        sendJson.set("data", list);
        sendJson.set("facilityCode", sbbh);
        sendJson.set("dataKey", tSyjzb.getSyjid());
        sendJson.set("trialTypeCode", "JGLQ050142");
        sendJson.set("trialTypeName", "砂浆抗压");
        sendJson.set("handType", "add");
        sendJson.set("reportNo", tSyjzb.getSjbh());
        String body = HttpRequest.post(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("token", token)
                .body(String.valueOf(sendJson))
                .timeout(20000)
                .execute().body();
        pushandreturnService.saveData(tSyjzb.getId(), String.valueOf(sendJson),"苏台五标新视窗",body);
        return body;
    }

    public String token(String sbbh) {//获取token
        Object token = null;
        QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
        shebeiInfoQueryWrapper.eq("sbjno", sbbh);
        ShebeiInfo one = shebeiInfoService.getOne(shebeiInfoQueryWrapper);
        String sysOrgCode = one.getSysOrgCode();
        if (sysOrgCode.contains("A05A01A14A05A01A05")) {
            tokenurl = tokenurl + "?key=e9f86ab7c85d90ed72adba1c862f3d72";
        } else if (sysOrgCode.contains("A05A01A14A05A01A01")) {
            tokenurl = tokenurl + "?key=79e3de91450ecb4faa66c5988f640f0d";
        } else {
            tokenurl = tokenurl + "?key=c019dc573a9e4f93125ed4339ee26aa1";
        }


        String post = HttpRequest.post(tokenurl)
                .header("Content-Type", "application/json")
                .execute()
                .body();
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(post);
        Object result = jsonObject.get("result");
        net.sf.json.JSONObject resultjson = net.sf.json.JSONObject.fromObject(result);
        token = resultjson.get("token");
        return (String) token;
    }

    private String getsbid(String sbbh) {

        QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
        sysDictQueryWrapper.eq("dict_code", "stsbid");
        SysDict one = sysDictService.getOne(sysDictQueryWrapper);
        String id1 = one.getId();

        QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
        sysDictItemQueryWrapper.eq("dict_id", id1);
        sysDictItemQueryWrapper.eq("item_text", sbbh);
        SysDictItem one1 = sysDictItemService.getOne(sysDictItemQueryWrapper);
        return one1.getItemValue();
    }
}
