package org.jeecg.modules.job.sutaijob.zjzl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
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
 * @ClassName ZhanglaJob：
 * @Description TODO
 * @Author 55314
 * @Date 2024/3/25 10:09
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ZhanglaJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;
    @Autowired
    private ITrZhanglaSService zhanglaSService;
    @Autowired
    private ITrZhanglaSSService zhanglaSSService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "https://zjiot.21huayan.com/api/dataupLoad/PostTensionInfo";
    private String urlss = "https://zjiot.21huayan.com/api/dataupLoad/PostMultiTensionProcess";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_ZL);//苏台张拉数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台张拉的配置信息" + DateUtils.now()));
            return;
        }
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台张拉的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrZhanglaXxb> zhanglaXxbs = zhanglaXxbService.selectListzjzl(shebeilist, curid);
        if (null == zhanglaXxbs || zhanglaXxbs.size() == 0) {
            log.info(String.format("暂无苏台张拉未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        //张拉信息表
        for (TrZhanglaXxb zhanglaXxb : zhanglaXxbs) {
            id = zhanglaXxb.getId();

            JSONObject sendJson = new JSONObject();
            sendJson.set("ProjectId","STGS");// 100 项目 Id,我方提供(详见数据接口密钥文档)，下同
            sendJson.set("SectionCode","e22f0573-0604-448d-9617-dcfb298f2685");// 100 合同段编号，我方提供(详见数据接口密钥文 档)，GUID
            sendJson.set("FactoryCode","STTJ01-1");// 100 预制场编号，我方提供(详见数据接口密钥文 档)，下同
            sendJson.set("Bridge",zhanglaXxb.getGjmc());// 100 桥梁名称
            sendJson.set("BeamCode",zhanglaXxb.getGjbh());// 100 梁体编号
            sendJson.set("BeamType",zhanglaXxb.getKualiang());// 100 梁形
            sendJson.set("ProjectPart",zhanglaXxb.getGjmc());// 200 工程部位
            sendJson.set("EquipmentMaker","");// 100 设备厂家
            sendJson.set("EquipmentType","苏台一标梁场一号张拉机");// 100 设备名称、型号
            sendJson.set("EquipmentCode","STTJ01-HTGS-TENSION-01");// 100 设备编号，我方提供
            sendJson.set("Subentry","");// 100 分项工程
            sendJson.set("BeamLength","");// 100 梁体长度
            sendJson.set("PouringDate",zhanglaXxb.getSgsj());// 8 浇筑日期
            sendJson.set("SGUnit","");// 100 施工单位
            sendJson.set("JLUnit","");// 100 监理单位
            sendJson.set("JCUnit","");// 100 检测单位
            sendJson.set("Concrete",zhanglaXxb.getSnsjqd());// 100 张拉时砼强度
            sendJson.set("JackVersion",zhanglaXxb.getZly1());// 100 千斤顶规格及型号
            sendJson.set("PressureVersion",zhanglaXxb.getYbbh1());// 100 油压表规格及型号
            sendJson.set("StrandVersion","");// 100 钢纹线型号规格
            sendJson.set("Construct","");// 100 构件名称
            sendJson.set("ConstructCode","");// 100 构件编号 3 字段名 类型 字段长度 字段说明
            sendJson.set("CalibrationDate",zhanglaXxb.getBdrq1());//",);// 8 设备标定日期
            sendJson.set("InsideAvg","");// 8 内缩量均值
            sendJson.set("AnchorInside","");// 8 锚具内缩量
            sendJson.set("Rub","");// 8 摩擦系数
            sendJson.set("TensionOrder","");// 100 张拉顺序
            sendJson.set("SGUser","");// 20 施工员
            sendJson.set("JCUser","");// 20 质量检测人
            sendJson.set("JLUser","");// 20 监理人员
            sendJson.set("JCLeader","");// 20 检测负责人
            sendJson.set("RecordUser","");// 20 记录人
            sendJson.set("EndTime","");// 8 梁体张拉结束时间
            sendJson.set("Stage","");// 4 张拉阶段，整形： 1：预制梁不进行分级张拉 2：预制梁分级张拉第一级 3：预制梁分级张拉第二级 4：预制梁先张法张拉 5：现浇梁 6：连续钢构
            sendJson.set("Mode","");// 4 张拉方式，整形： 四顶张拉 1：(1#~3#,2#~4#) 双顶张拉 2：(1#~3#);3：(2#~4#);4: (1#,2#);5: (3#,4#) 单顶张拉 6: (1#);7: (2#);8: (3#);9: (4#) 双顶张拉：10: (1#~2#)
            sendJson.set("Upwarp","");// 8 终张拉弹性上拱(mm)
            sendJson.set("Warning","");// 400 预警原因
            sendJson.set("IsDispose","");// Bool 1 预警是否处理
            List ResultDataList = new ArrayList();
            //张拉主表
            QueryWrapper<TrZhanglaM> zhanglaMQueryWrapper = new QueryWrapper<>();
            zhanglaMQueryWrapper.eq("syjid",zhanglaXxb.getSyjid());

            List<TrZhanglaM> TrZhanglaMList = zhanglaMService.list(zhanglaMQueryWrapper);
            for (TrZhanglaM zhanglaM : TrZhanglaMList) {
                sendJson.set("StartTime",zhanglaM.getZlsj());// 8 梁体张拉开始时间
                JSONObject sendMJson = new JSONObject();
                sendMJson.set("HoleCode",zhanglaM.getGsbh());// 100 孔道号
                sendMJson.set("DesignTension",zhanglaM.getSjzll());// 8 设计张拉力值(kN)
                String llscl = zhanglaM.getLlscl();
                String zscl = zhanglaM.getZscl();
                // 将字符串转换为double类型
                double llsclDouble = Double.parseDouble(llscl);
                double zsclDouble = Double.parseDouble(zscl);

                // 计算差值
                double difference = zsclDouble - llsclDouble;

                sendMJson.set("DesignElongation",llscl);// 8 理论伸长量(mm)
                sendMJson.set("Elongation",zscl);// 8 张拉实际总伸长量
                sendMJson.set("ElongationDeviation",difference);// 8 伸长量偏差
                sendMJson.set("ElongationDeviationRate",zhanglaM.getSclper());// 8 伸长量偏差率(%)
                sendMJson.set("StartTime","");// 8 张拉开始时间
                sendMJson.set("EndTime",zhanglaM.getJlsj());// 8 张拉结束时间

                //张拉子表
                QueryWrapper<TrZhanglaS> zhanglaSQueryWrapper = new QueryWrapper<>();
                zhanglaSQueryWrapper.eq("fguid",zhanglaM.getFguid());

                List<TrZhanglaS> TrZhanglaSList = zhanglaSService.list(zhanglaSQueryWrapper);
                for (TrZhanglaS zhanglaS : TrZhanglaSList) {
                    String dh = zhanglaS.getDh();
                    String jdbfb = zhanglaS.getJdbfb();
                    String jdscl = zhanglaS.getJdscl();
                    String jdzll = zhanglaS.getJdzll();
                    String ybds = zhanglaS.getYbds();

                    if ("1".equals(dh)){
                        if ("10".equals(jdbfb)){
                            sendMJson.set("LeTension",jdzll);// 8 1 号顶第 1 行程张拉力
                            sendMJson.set("LeElongation",jdscl);// 8 1 号顶第 1 行程伸长量
                            sendMJson.set("LePressure",ybds);// 8 1 号油压表第 1 行程(MPa)
                            sendMJson.set("LeAnchorLength","");// 8 1 号锚固段长度(mm)

                        }
                        if ("20".equals(jdbfb)){
                            sendMJson.set("LeTension1",jdzll);// 8 1 号顶第 2 行程张拉力
                            sendMJson.set("LeElongation1",jdscl);// 8 1 号顶第 2 行程伸长量
                            sendMJson.set("LePressure1",ybds);// 8 1 号油压表第 2 行程(MPa)
                            sendMJson.set("LeAnchorInside","");// 8 1 号锚固内缩量(mm)

                        }
                        if ("50".equals(jdbfb)){
                            sendMJson.set("LeTension2",jdzll);// 8 1 号顶第 3 行程张拉力
                            sendMJson.set("LeElongation2",jdscl);// 8 1 号顶第 3 行程伸长量
                            sendMJson.set("LePressure2",ybds);// 8 1 号油压表第 3 行程(MPa)
                            sendMJson.set("LeAnchorExtend","");// 8 1 号锚固段伸长量(mm)

                        }
                        if ("100".equals(jdbfb)){
                            sendMJson.set("LeTension3",jdzll);// 8 1 号顶第 4 行程张拉力
                            sendMJson.set("LeElongation3",jdscl);// 8 1 号顶第 4 行程伸长量
                            sendMJson.set("LePressure3",ybds);// 8 1 号油压表第 4 行程(MPa)
                            sendMJson.set("LeAnchorInside2","");// 8 1 号工具锚内缩量(mm)
                        }
                    }else {
                        if ("10".equals(jdbfb)){
                            sendMJson.set("RiTension",jdzll);// 8 1 号顶第 1 行程张拉力
                            sendMJson.set("RiElongation",jdscl);// 8 1 号顶第 1 行程伸长量
                            sendMJson.set("RiPressure",ybds);// 8 1 号油压表第 1 行程(MPa)
                            sendMJson.set("RiAnchorLength","");// 8 1 号锚固段长度(mm)

                        }
                        if ("20".equals(jdbfb)){
                            sendMJson.set("RiTension1",jdzll);// 8 1 号顶第 2 行程张拉力
                            sendMJson.set("RiElongation1",jdscl);// 8 1 号顶第 2 行程伸长量
                            sendMJson.set("RiPressure1",ybds);// 8 1 号油压表第 2 行程(MPa)
                            sendMJson.set("RiAnchorInside","");// 8 1 号锚固内缩量(mm)

                        }
                        if ("50".equals(jdbfb)){
                            sendMJson.set("RiTension2",jdzll);// 8 1 号顶第 3 行程张拉力
                            sendMJson.set("RiElongation2",jdscl);// 8 1 号顶第 3 行程伸长量
                            sendMJson.set("RiPressure2",ybds);// 8 1 号油压表第 3 行程(MPa)
                            sendMJson.set("RiAnchorExtend","");// 8 1 号锚固段伸长量(mm)

                        }
                        if ("100".equals(jdbfb)){
                            sendMJson.set("RiTension3",jdzll);// 8 1 号顶第 4 行程张拉力
                            sendMJson.set("RiElongation3",jdscl);// 8 1 号顶第 4 行程伸长量
                            sendMJson.set("RiPressure3",ybds);// 8 1 号油压表第 4 行程(MPa)
                            sendMJson.set("RiAnchorInside2","");// 8 1 号工具锚内缩量(mm)
                        }

                    }
                }

                sendMJson.set("Ratio","");// 8 初应力-张拉比例
                sendMJson.set("Ratio1","");// 8 2 倍初张力-张拉比例
                sendMJson.set("Ratio2","");// 8 50%张拉力-张拉比例
                sendMJson.set("Ratio3","");// 8 100%张拉力-张拉比例
                sendMJson.set("Ratio4","");// 8 100%回油到初应力-张拉比例(超张百分比)
                sendMJson.set("LeJackCode",zhanglaXxb.getZly1());// 40 1 号顶编号
                sendMJson.set("LePressureCode",zhanglaXxb.getYbbh1());// 40 1 号油压表编号
                sendMJson.set("LePumpCode","");// 40 1 号油压泵编号
                sendMJson.set("RiJackCode",zhanglaXxb.getZly2());// 40 2 号顶编号
                sendMJson.set("RiPressureCode",zhanglaXxb.getYbbh2());// 8 2 号油压表编号
                sendMJson.set("RiPumpCode","");// 40 2 号油压泵编号
                sendMJson.set("Note","");// 100 备注
                sendMJson.set("Craft","");// 50 张拉工艺
                sendMJson.set("Producer","");// 100 设备厂家
                ResultDataList.add(sendMJson);

                JSONObject sendSsJson = new JSONObject();
                sendSsJson.set("ProjectId","STGS");// string 100 工程 Id
                sendSsJson.set("SectionCode","e22f0573-0604-448d-9617-dcfb298f2685");// string 40 合同段编号
                sendSsJson.set("FactoryCode","STTJ01-1");// string 40 梁场编号
                sendSsJson.set("Bridge",zhanglaXxb.getGjmc());// string 40 桥梁名称
                sendSsJson.set("BeamCode",zhanglaXxb.getGjbh());// string 40 梁体编号
                sendSsJson.set("HoleCode",zhanglaM.getGsbh());// string 40 孔道号
                String holeid = zhanglaM.getHoleid();
                QueryWrapper<TrZhanglaSS> zhanglaSSQueryWrapper = new QueryWrapper<>();
                zhanglaSSQueryWrapper.eq("holeid",holeid);

                List ProcessList = new ArrayList();
                List<TrZhanglaSS> TrZhanglaSSList = zhanglaSSService.list(zhanglaSSQueryWrapper);
                if (TrZhanglaSSList.size()>0){
                    for (TrZhanglaSS trZhanglaSS : TrZhanglaSSList) {
                        JSONObject sendSJson = new JSONObject();
                        sendSJson.set("LeJackCode",zhanglaXxb.getZly1());// string 40 1 号顶编号
                        sendSJson.set("LeTension",trZhanglaSS.getZll1());// float 8 1 号顶张拉力
                        sendSJson.set("LePressure",trZhanglaSS.getYy1());// float 8 1 号油压表(MPa)
                        sendSJson.set("LeElongation",trZhanglaSS.getScl1());// float 8 1 号伸长量
                        sendSJson.set("RiJackCode",zhanglaXxb.getZly2());// string 40 2 号顶编号
                        sendSJson.set("RiTension",trZhanglaSS.getZll2());// float 8 2 号顶张拉力
                        sendSJson.set("RiPressure",trZhanglaSS.getYy2());// float 8 2 号油压表(MPa)
                        sendSJson.set("RiElongation",trZhanglaSS.getScl2());// float 8 2 号伸长量
                        sendSJson.set("OperateTime",trZhanglaSS.getJlsj());// datetime 8 采集时间
                        ProcessList.add(sendSJson);
                    }
                }
                sendSsJson.set("ProcessList",ProcessList);
                String body = HttpRequest.post(urlss)
                        .header("content-type","application/json")
                        .body(String.valueOf(sendSsJson))
                        .execute()
                        .body();
                System.out.println(body);
                pushandreturnService.saveData(id,String.valueOf(sendSsJson),selectsysconfigone.getRemark(),body);
            }
            sendJson.set("ResultDataList",ResultDataList);

            String body = HttpRequest.post(url)
                    .header("content-type","application/json")
                    .body(String.valueOf(sendJson))
                    .execute()
                    .body();
            System.out.println(body);
            pushandreturnService.saveData(id,String.valueOf(sendJson),selectsysconfigone.getRemark(),body);
            sysConfigService.updateSysConfig(JobUtil.ST_ZL, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
