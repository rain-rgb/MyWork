package org.jeecg.modules.job.sgsanbiaojob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglass.entity.TrZhanglaSS;
import com.trtm.iot.zhanglass.service.ITrZhanglaSSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName zhanhLaJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/12/13 14:13
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zhanhLaJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrZhanglaXxbService trZhanglaXxbService;
    @Autowired
    private ITrZhanglaMService trZhanglaMService;
    @Autowired
    private ITrZhanglaSService trZhanglaSService;
    @Autowired
    private ITrZhanglaSSService zhanglaSSService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LH_ZL);//六横（疏港三标）张拉推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到张拉数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓张拉设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrZhanglaXxb> zhanglaXxbs = trZhanglaXxbService.selectListsss(shebeilist, curid);
        if (null == zhanglaXxbs || zhanglaXxbs.size() == 0) {
            log.info("暂无瑞仓内网张拉未推送数据" + DateUtils.now());
            return;
        }
        int id = 0;
        for (TrZhanglaXxb trZhanglaXxb : zhanglaXxbs) {

            String syjid = trZhanglaXxb.getSyjid();
            QueryWrapper<TrZhanglaM> zhanglaMQueryWrapper = new QueryWrapper<>();
            zhanglaMQueryWrapper.eq("syjid", syjid);
            List<TrZhanglaM> trZhanglaMlist = trZhanglaMService.list(zhanglaMQueryWrapper);

            JSONObject sendObject = JSONUtil.createObj();
            sendObject.set("taskno", trZhanglaXxb.getUuid());    //	任务单号
//            sendObject.set("platformdeviceid", trZhanglaXxb.get);    //	平台设备识别码（提供）
//            sendObject.set("vendorno", trZhanglaXxb.get);    //	厂家编码
//            sendObject.set("deviceno", trZhanglaXxb.get);    //	厂家设备编号
            sendObject.set("subprojectpath", trZhanglaXxb.getGjbh());    //	分部分项（路径），例如：A匝道1号桥/AK0+130/上部构造预制和安装/第1孔/预应力砼T梁/1-1#预应力砼T梁/
//            sendObject.set("componentid", trZhanglaXxb.get);    //	梁识别码
            sendObject.set("componentparts", trZhanglaXxb.getGjbh());    //	梁（构件）名称或编号
            sendObject.set("engineeringname", trZhanglaXxb.getGcmc());    //	工程名称
            sendObject.set("engineeringsite", trZhanglaXxb.getGjbh());    //	工程部位，桥名
            sendObject.set("beamtype", trZhanglaXxb.getKualiang());    //	梁型
            sendObject.set("concretestrength", trZhanglaXxb.getSnskqd());    //	砼设计强度
            sendObject.set("userid", trZhanglaXxb.getMemo());    //	操作员
            sendObject.set("alarm_info", trZhanglaXxb.getOverReason());    //	预警信息

            for (TrZhanglaM zhanglaM : trZhanglaMlist) {

                sendObject.set("id", zhanglaM.getFguid());    //	唯一识别码UUID

                sendObject.set("modulusofelasticity", zhanglaM.getTxml());    //	弹性模量
                sendObject.set("steelbeamno", zhanglaM.getGsbh());    //	钢束编号
                sendObject.set("steelstrand", zhanglaM.getGsgs());    //	钢绞线根数
                sendObject.set("tensioningdate", zhanglaM.getJlsj());    //	张拉时间（yyyy-MM-dd HH:mm:ss）
                sendObject.set("tensioncontrol", zhanglaM.getSjzll());    //	设计张拉控制力，单位Kn
                sendObject.set("totalelongation", zhanglaM.getZscl());    //	总伸长量，单位mm
                sendObject.set("heoreticalelongation", zhanglaM.getLlscl());    //	理论伸长量，单位mm
                sendObject.set("extenderror", zhanglaM.getSclper());    //	延伸误差量，单位%
                sendObject.set("otherinformation", zhanglaM.getWt());    //	其他信息，滑丝断丝等情况说明
                sendObject.set("retraction", zhanglaM.getHtl());    //	回缩量 ，单位mm(毫米)
                sendObject.set("birthtime", zhanglaM.getJlsj());    //	数据采集时间

                String hege = zhanglaM.getHege();
                int resultValue = hege.equals("0") ? 1 : 0;
                sendObject.set("result", resultValue);    //	0代表合格,1代表不合格
                sendObject.set("isAlarm", resultValue);    //	是否预警（0：否，1：是）

                String holeid = zhanglaM.getHoleid();
                QueryWrapper<TrZhanglaSS> zhanglaSSQueryWrapper = new QueryWrapper<>();
                zhanglaSSQueryWrapper.eq("holeid",holeid);
                List<TrZhanglaSS> ssList = zhanglaSSService.list(zhanglaSSQueryWrapper);
                StringBuilder sbZll1 = new StringBuilder();
                StringBuilder sbScl1 = new StringBuilder();
                StringBuilder sbYy1 = new StringBuilder();

                for (TrZhanglaSS trZhanglaSS : ssList) {
                    String zll1 = trZhanglaSS.getZll1();
                    String scl1 = trZhanglaSS.getScl1();
                    String yy1 = trZhanglaSS.getYy1();

                    if (sbZll1.length() > 0) {
                        sbZll1.append(",");
                        sbScl1.append(",");
                        sbYy1.append(",");
                    }

                    sbZll1.append(zll1);
                    sbScl1.append(scl1);
                    sbYy1.append(yy1);
                }

                String joinedZll1 = sbZll1.toString();
                String joinedScl1 = sbScl1.toString();
                String joinedYy1 = sbYy1.toString();
                sendObject.set("forcecurve", joinedZll1);    //	压力值曲线数据，每个点之间用逗号分隔
                sendObject.set("elongationcurve", joinedScl1);    //	伸长量曲线数据，每个点之间用逗号分隔
                sendObject.set("oilpressurecurve", joinedYy1);    //	油压曲线数据，每个点之间用逗号分隔

                QueryWrapper<TrZhanglaS> qhdQueryWrapper = new QueryWrapper<>();
                qhdQueryWrapper.select("dh");
                qhdQueryWrapper.eq("f_guid", zhanglaM.getFguid());
                qhdQueryWrapper.groupBy("dh");
                List<TrZhanglaS> list = trZhanglaSService.list(qhdQueryWrapper);
                if (list.size() > 0) {
                    String qd = list.get(0).getDh();
                    String hd = list.get(1).getDh();
                    List dmList = new ArrayList();
                    dmList.add(qd);
                    dmList.add(hd);

                    for (int i = 0; i < dmList.size(); i++) {
                        Object o = dmList.get(i);
                        QueryWrapper<TrZhanglaS> zhanglaSQueryWrapper = new QueryWrapper<>();
                        zhanglaSQueryWrapper.eq("f_guid", zhanglaM.getFguid());
                        zhanglaSQueryWrapper.eq("dh", o);
                        List<TrZhanglaS> zhanglaSList = trZhanglaSService.list(zhanglaSQueryWrapper);

                        sendObject.set("tensioningsection", o);    //	张拉断面
                        for (TrZhanglaS zhanglaS : zhanglaSList) {

                            int jd50 = 0;
                            Double zll = 0.0;
                            String jdbfb = zhanglaS.getJdbfb();
                            if ("10".equals(jdbfb)) {
                                sendObject.set("tensioningforce0", zhanglaS.getJdzll());    //	张拉力0，10%（或其他)行程张拉力，单位KN
                                sendObject.set("elongation0", zhanglaS.getJdscl());    //	10%（或其他)行程伸长量，单位mm
                                sendObject.set("oilpressure0", zhanglaS.getYbds());    //	10%（或其他)行程油压，单位MPa
                            }
                            if ("20".equals(jdbfb)) {
                                sendObject.set("tensioningforce1", zhanglaS.getJdzll());    //	张拉力1，20%（或其他)行程张拉力，单位KN
                                sendObject.set("elongation1", zhanglaS.getJdscl());    //	20%（或其他)行程伸长量，单位mm
                                sendObject.set("oilpressure1", zhanglaS.getYbds());    //	20%（或其他)行程油压，单位MPa
                            }
                            if ("100".equals(jdbfb)) {
                                sendObject.set("tensioningforce4", zhanglaS.getJdzll());    //	张拉力4，100%行程张拉力，单位KN
                                sendObject.set("elongation4", zhanglaS.getJdscl());    //	100%行程伸长量，单位mm
                                sendObject.set("oilpressure4", zhanglaS.getYbds());    //	100%行程油压，单位MPa
                                zll = Double.parseDouble(zhanglaS.getJdzll());
                                sendObject.set("holdingtime", zhanglaS.getChsj());    //	持荷时间，单位s(秒)
                            }
                            if ("50".equals(jdbfb) && jd50 == 0) {
                                sendObject.set("tensioningforce2", zhanglaS.getJdzll());    //	张拉力2，50%（或其他)行程张拉力，单位KN
                                sendObject.set("elongation2", zhanglaS.getJdscl());    //	50%（或其他)行程伸长量，单位mm
                                sendObject.set("oilpressure2", zhanglaS.getYbds());    //	50%（或其他)行程油压，单位MPa
                                jd50 = 1;
                            }
                            if ("50".equals(jdbfb) && jd50 == 1) {
                                sendObject.set("tensioningforce3", zhanglaS.getJdzll());    //	张拉力3，50%（或其他)行程张拉力，单位KN
                                sendObject.set("elongation3", zhanglaS.getJdscl());    //	50%（或其他)行程伸长量，单位mm
                                sendObject.set("oilpressure3", zhanglaS.getYbds());    //	50%（或其他)行程油压，单位MPa
                            }

                            double zll1 = Double.parseDouble(zhanglaM.getSjzll());
                            zll1 = (double)Math.round(zll1*100)/100;
                            sendObject.set("zllwc", zll - zll1);    //	张拉力误差
//                            sendObject.set("anchorwidth", trZhanglaXxb.get);    //	锚片厚度，单位mm(毫米)
//                            sendObject.set("jackno", trZhanglaXxb.get);    //	千斤顶编号
//                            sendObject.set("sectionerror", trZhanglaXxb.get);    //	两端千斤顶的张拉力误差%
//                            sendObject.set("tensionmode", trZhanglaXxb.get);    //	张拉方式，1代表单端张拉、2代表双端同步张拉
                        }

                    }
                } else {
                    trZhanglaXxb.setIslh(2);
                    trZhanglaXxbService.saveOrUpdate(trZhanglaXxb);
                }
            }

        }
    }
}
