package org.jeecg.modules.job.yongjinqushang;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.syj.entity.FWangnj;
import com.trtm.iot.syj.entity.FYaliji;
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
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName syjJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/11/9 8:54
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class syjJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITSyjzbService syjzbService;
    @Autowired
    private IFYalijiService yalijiService;
    @Autowired
    private IFWangnjService wangnjService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String ylurl = "http://115.236.10.10:8081/zjjg-iot/test_detection/saveSuperDeviceData";
    private String wnurl = "http://115.236.10.10:8081/zjjg-iot/test_detection/saveMasterDeviceData";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJQS_SYJ);//甬金衢上试验机
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到甬金衢上试验机定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输甬金衢上试验机的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TSyjzb> tSyjzbs = syjzbService.selectYJQSlist(shebeilist, curid);
        if (null == tSyjzbs || tSyjzbs.size() == 0) {
            log.info(String.format("暂无甬金衢上试验机的数据" + DateUtils.now()));
            return;
        } else {
            log.info(String.format("甬金衢上试验机数据推送开始！" + DateUtils.now()));
        }
        for (TSyjzb tSyjzb : tSyjzbs) {
            String sbbh = tSyjzb.getSbbh();
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(sbbh);
            Integer sbtype = sbjwd.getSbtype();
            if (sbtype == 3) {
                wnjPost(tSyjzb, sbjwd);
            }
            if (sbtype == 4) {
                yljPost(tSyjzb, sbjwd);
            }

        }
    }

    @SneakyThrows
    private void yljPost(TSyjzb tSyjzb, ShebeiInfo sbjwd) {

        String syjid = tSyjzb.getSyjid();
        List<FYaliji> fYalijis = yalijiService.selectFsYalijiData(syjid);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject sendObject = new JSONObject();
        sendObject.set("_Key_", tSyjzb.getWtbh()); //样品编号
        sendObject.set("orgCode", sbjwd.getTunnelId());//机构编码
        sendObject.set("_LingQi_", tSyjzb.getLq());
        sendObject.set("_DeviceID_", tSyjzb.getSbbh());
        sendObject.set("deviceName", sbjwd.getSbname());
        sendObject.set("constructionSite", tSyjzb.getCjmc());
        sendObject.set("_TestID_", tSyjzb.getSylx());
        sendObject.set("_Tester_", tSyjzb.getCzry());
        sendObject.set("Shape", "Rectangle");
        String sjcc = tSyjzb.getSjcc();
        String[] split = sjcc.split("\\*");
        if (split.length > 0) {
            sendObject.set("Length", split[0]);
            sendObject.set("Width", split[1]);
            sendObject.set("Height", split[2]);
        }
//        sendObject.set("Diameter", "直径");
        sendObject.set("Size", tSyjzb.getSjcc());
        sendObject.set("Span", "");
        sendObject.set("Sample", "");
        sendObject.set("QDDJ", tSyjzb.getSjqd());
        sendObject.set("PingZhong", "");//水泥胶砂
        sendObject.set("XS", tSyjzb.getZsxs());
        sendObject.set("Load", "破坏载荷");
        sendObject.set("Stress", "抗压抗折强度");
        sendObject.set("YXQD", tSyjzb.getQddbz());//有效强度
        sendObject.set("PDJG", tSyjzb.getPdjg());//评定结果
        sendObject.set("MS", "");//马歇尔模数

        List DetailList = new ArrayList();
        for (FYaliji fYaliji : fYalijis) {
            sendObject.set("YXLZ", fYaliji.getKylz());
            sendObject.set("YXQD", fYaliji.getKyqd());
            JSONObject yalijiObject = new JSONObject();
            yalijiObject.set("_Key_", tSyjzb.getWtbh());//	样品编号
            yalijiObject.set("_Time_", sdf.format(sdf.parse(fYaliji.getSysj())));//	试验检测时间
            yalijiObject.set("Shape", "Rectangle");//	形状类型 Round:圆形 Rectangle:矩形
            if (split.length > 0) {
                yalijiObject.set("Length", split[0]);
                yalijiObject.set("Width", split[1]);
                yalijiObject.set("Height", split[2]);
            }
//            yalijiObject.set("Diameter", fYaliji.get);//	直径
            yalijiObject.set("Size", tSyjzb.getSjcc());//	规格尺寸
//            yalijiObject.set("Span", fYaliji.get);//	跨距 部分抗折类试验可能会有
//            yalijiObject.set("Sample", fYaliji.get);//	强度等级 混凝土、水泥胶砂、建筑砂浆
            yalijiObject.set("QDDJ", tSyjzb.getSjqd());//	强度等级 由于历史原因，在上传的时候强度等级用的这个字段
//            yalijiObject.set("PingZhong", fYaliji.get);//	水泥胶砂
            yalijiObject.set("XS", tSyjzb.getZsxs());//	强度修正系数
            yalijiObject.set("Load", fYaliji.getKylz());//	破坏载荷
            yalijiObject.set("Stress", fYaliji.getKyqd());//	抗压抗折强度
            yalijiObject.set("YXLZ", fYaliji.getKylz());//	有效力值
            yalijiObject.set("YXQD", fYaliji.getKyqd());//	有效强度
            yalijiObject.set("PDJG", tSyjzb.getPdjg());//	评定结果
//            yalijiObject.set("WDD", fYaliji.get);//	稳定度
//            yalijiObject.set("LZ", fYaliji.get);//	流值
//            yalijiObject.set("ZRD", fYaliji.get);//	针入度值
//            yalijiObject.set("Temp", fYaliji.get);//	当时的温度 一般没用
//            yalijiObject.set("YTLX", fYaliji.get);//	液体类型 0:水 1:甘油
//            yalijiObject.set("Temp1", fYaliji.get);//	第一个样品掉落时的温度(摄氏度)
//            yalijiObject.set("Temp2", fYaliji.get);//	第二个样品掉落时的温度(摄氏度)

            String yskylz = fYaliji.getYskylz();
            String sjgc = fYaliji.getSjgc();

            String[] yskylzList = yskylz.split(", ");
            String[] sjgcList = sjgc.split(", ");

            JSONObject gcObject = new JSONObject();
            gcObject.set("_Time_", sjgcList);
            gcObject.set("_Load_", yskylzList);

            yalijiObject.set("_Datas_", gcObject);

            DetailList.add(yalijiObject);
        }

        sendObject.set("Detail", DetailList);

        JSONObject sendJson = new JSONObject();
        sendJson.set("Group", sendObject);

        String result = HttpRequest.post(ylurl)
                .header("client-code", "gaoxun")
                .body(String.valueOf(sendJson))
                .timeout(25000)
                .execute()
                .body();

        pushandreturnService.saveData(tSyjzb.getId(), String.valueOf(sendJson), "甬金衢上三标压力机", result);
        if (result.contains("200")) {
            log.info("甬金衢上高速3标压力机推送成功！");
            tSyjzb.setIssend(1);
        } else {
            log.info("甬金衢上高速3标压力机推送失败！");
            tSyjzb.setIssend(2);
        }
        syjzbService.saveOrUpdate(tSyjzb);
        sysConfigService.updateSysConfig(JobUtil.YJQS_SYJ, tSyjzb.getId());
    }

    @SneakyThrows
    private void wnjPost(TSyjzb tSyjzb, ShebeiInfo sbjwd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String syjid = tSyjzb.getSyjid();

        JSONObject sendObject = new JSONObject();
        sendObject.set("_Key_", tSyjzb.getWtbh());//	样品编号
        sendObject.set("orgCode", sbjwd.getTunnelId());//	机构编码
        sendObject.set("_LingQi_", tSyjzb.getLq());//	龄期，无龄期的项目不提供
        sendObject.set("_DeviceID_", tSyjzb.getSbbh());//	设备ID
        sendObject.set("deviceName", sbjwd.getSbname());//	设备名称
        sendObject.set("_TestID_", tSyjzb.getSylx());//	试样项目代号
        sendObject.set("_Tester_", tSyjzb.getCzry());//	试验人员
        sendObject.set("Shape", "Round");//	形状类型 Round:圆形 Rectangle:矩形
//        sendObject.set("Length", tSyjzb.get());//	长度
//        sendObject.set("Width", tSyjzb.get());//	宽度
//        sendObject.set("Height", tSyjzb.get());//	高度
        sendObject.set("do", tSyjzb.getGczj());//	直径 TestMaster6支持使用该字段向试验软件提供直径
        sendObject.set("ZJ", tSyjzb.getGczj());//	直径
//        sendObject.set("Lo", tSyjzb.get());//	原始标距
//        sendObject.set("Lu", tSyjzb.get());//	断后标距
//        sendObject.set("A", tSyjzb.get());//	断后伸长率
//        sendObject.set("Lc", tSyjzb.get());//	平行长度
//        sendObject.set("StdRe", tSyjzb.get());//	标准屈服
//        sendObject.set("Area", tSyjzb.get());//	原始截面积
//        sendObject.set("Su", tSyjzb.get());//	断后截面积
//        sendObject.set("Z", tSyjzb.get());//	断面收缩率
//        sendObject.set("Fm", tSyjzb.get());//	最大力
//        sendObject.set("Rm", tSyjzb.get());//	抗拉强度
//        sendObject.set("FeH", tSyjzb.get());//	上屈服力
//        sendObject.set("ReH", tSyjzb.get());//	上屈服强度
//        sendObject.set("FeL", tSyjzb.get());//	下屈服力
//        sendObject.set("ReL", tSyjzb.get());//	下屈服强度
//        sendObject.set("Fp", tSyjzb.get());//	规定塑性延伸力
//        sendObject.set("Rp", tSyjzb.get());//	规定塑性延伸强度
//        sendObject.set("Ft", tSyjzb.get());//	规定总延伸力
//        sendObject.set("Rt", tSyjzb.get());//	规定总延伸强度
//        sendObject.set("E", tSyjzb.get());//	弹性模量
//        sendObject.set("DLm", tSyjzb.get());//	最大力总延伸
//        sendObject.set("Agt", tSyjzb.get());//	最大力总延伸率
//        sendObject.set("Lm", tSyjzb.get());//	最大力塑性延伸
//        sendObject.set("Ag", tSyjzb.get());//	最大力塑性延伸率
//        sendObject.set("DLTZ", tSyjzb.get());//	断裂特征、破坏形态
//        sendObject.set("DLXZ", tSyjzb.get());//	断裂性质、断口特征
//        sendObject.set("DLWZ", tSyjzb.get());//	断裂位置
//        sendObject.set("MS", tSyjzb.get());//	马歇尔模数
//
        List<FWangnj> wangnjs = wangnjService.selectFswannjData(syjid);
        List DetailList = new ArrayList();
        for (FWangnj wangnj : wangnjs) {
            JSONObject wannengjiObject = new JSONObject();
            wannengjiObject.set("_Key_", tSyjzb.getWtbh());//	样品编号
            String sysj = wangnj.getSysj();
            String wcsj = wangnj.getWcsj();
            Date date;
            try {
                if (StringUtil.isNotEmpty(sysj)) {
                    // 尝试将 sysj 转换为日期对象
                    date = sdf.parse(sysj);
                    String formattedDate = sdf.format(date);
                    sysj = formattedDate;
                }else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(wcsj, formatter);

                    LocalDateTime newDateTime = dateTime.minusMinutes(3);  // 将时间减去三分钟

                    sysj = newDateTime.format(formatter);  // 将减去三分钟后的时间转换为字符串
                }
            } catch (Exception e) {
            }

            wannengjiObject.set("_Time_", sysj);//	试验检测时间
            wannengjiObject.set("Shape", "Round");//	形状类型 Round:圆形 Rectangle:矩形
//            wannengjiObject.set("Length", wangnj.get);//	长度
//            wannengjiObject.set("Width", wangnj.get);//	宽度
//            wannengjiObject.set("Height", wangnj.get);//	高度
            wannengjiObject.set("do", tSyjzb.getGczj());//	直径 TestMaster6支持使用该字段向试验软件提供直径
            wannengjiObject.set("ZJ", tSyjzb.getGczj());//	直径
            wannengjiObject.set("Lo", wangnj.getWsbz());//	原始标距
            wannengjiObject.set("Lu", wangnj.getDhbz());//	断后标距
//            wannengjiObject.set("A", wangnj.get);//	断后伸长率
//            wannengjiObject.set("Lc", wangnj.get);//	平行长度
//            wannengjiObject.set("StdRe", wangnj.get);//	标准屈服
            wannengjiObject.set("Area", tSyjzb.getArea());//	原始截面积
//            wannengjiObject.set("Su", wangnj.get);//	断后截面积
//            wannengjiObject.set("Z", wangnj.get);//	断面收缩率
            wannengjiObject.set("Fm", wangnj.getLz());//	最大力
            wannengjiObject.set("Rm", wangnj.getLzqd());//	抗拉强度
            wannengjiObject.set("FeH", wangnj.getQflz());//	上屈服力
            wannengjiObject.set("ReH", wangnj.getQfqd());//	上屈服强度
            wannengjiObject.set("FeL", wangnj.getSqflz());//	下屈服力
            wannengjiObject.set("ReL", wangnj.getSqfqd());//	下屈服强度
//            wannengjiObject.set("Fp", wangnj.get);//	规定塑性延伸力
//            wannengjiObject.set("Rp", wangnj.get);//	规定塑性延伸强度
//            wannengjiObject.set("Ft", wangnj.get);//	规定总延伸力
//            wannengjiObject.set("Rt", wangnj.get);//	规定总延伸强度
            wannengjiObject.set("E", wangnj.getModuluselasticity());//	弹性模量
//            wannengjiObject.set("DLm", );//	最大力总延伸
//            wannengjiObject.set("Agt", wangnj.get);//	最大力总延伸率
//            wannengjiObject.set("Lm", wangnj.get);//	最大力塑性延伸
//            wannengjiObject.set("Ag", wangnj.get);//	最大力塑性延伸率
//            wannengjiObject.set("DLTZ", wangnj.get);//	断裂特征、破坏形态
            wannengjiObject.set("DLXZ", wangnj.getLdcms());//	断裂性质、断口特征
            wannengjiObject.set("DLWZ", wangnj.getDkwz());//	断裂位置
//            wannengjiObject.set("MS", wangnj.get);//	马歇尔模数
//            wannengjiObject.set("WDD", wangnj.get);//	稳定度
//            wannengjiObject.set("LZ", wangnj.get);//	流值
//            wannengjiObject.set("ZRD", wangnj.get);//	针入度值
//            wannengjiObject.set("Temp", wangnj.get);//	当时的温度 一般没用
//            wannengjiObject.set("YTLX", wangnj.get);//	液体类型 0:水 1:甘油
//            wannengjiObject.set("Temp1", wangnj.get);//	第一个样品掉落时的温度(摄氏度)
//            wannengjiObject.set("Temp2", wangnj.get);//	第二个样品掉落时的温度(摄氏度)

            String wy = wangnj.getWy();
            String sjgc = wangnj.getSjgc();
            String lzgc = wangnj.getLzgc();

            String[] wyList = wy.split(", ");
            String[] sjgcList = sjgc.split(", ");
            String[] lzgcList = lzgc.split(", ");

            JSONObject gcObject = new JSONObject();
            gcObject.set("_Time_", sjgcList);
            gcObject.set("_Load_", lzgcList);
            gcObject.set("_Posi_", wyList);

            wannengjiObject.set("_Datas_", gcObject);

            DetailList.add(wannengjiObject);
        }
        sendObject.set("Detail", DetailList);//	每个试样的详细信息

        JSONObject sendJson = new JSONObject();
        sendJson.set("Group", sendObject);

        String result = HttpRequest.post(wnurl)
                .header("client-code", "gaoxun")
                .body(String.valueOf(sendJson))
                .timeout(25000)
                .execute()
                .body();

        pushandreturnService.saveData(tSyjzb.getId(), String.valueOf(sendJson), "甬金衢上三标万能机", result);
        if (result.contains("200")) {
            log.info("甬金衢上高速3标万能机推送成功！");
            tSyjzb.setIssend(1);
        } else {
            log.info("甬金衢上高速3标万能机推送失败！");
            tSyjzb.setIssend(2);
        }
        System.out.println(sendJson);
        syjzbService.saveOrUpdate(tSyjzb);
        sysConfigService.updateSysConfig(JobUtil.YJQS_SYJ, tSyjzb.getId());

    }
}
