package org.jeecg.modules.job.sutaijob.zjzl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.yajiangss.service.ITrYajiangSSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName YajaingJob：
 * @Description TODO
 * @Author 55314
 * @Date 2024/3/27 16:30
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class YajaingJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private ITrYajiangSService yajiangSService;
    @Autowired
    private ITrYajiangSSService yajiangSSService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "https://zjiot.21huayan.com/api/dataupLoad/PostGroutData";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_YJ);//苏台压浆数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台压浆的配置信息" + DateUtils.now()));
            return;
        }
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台压浆的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrYajiangM> trYajiangMS = yajiangMService.selectListst(shebeilist, curid);
        if (null == trYajiangMS || trYajiangMS.size() == 0) {
            log.info(String.format("暂无苏台压浆未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        //压浆信息表
        for (TrYajiangM trYajiangM : trYajiangMS) {
            id = trYajiangM.getId();

            JSONObject sendJson = new JSONObject();

            sendJson.set("ProjectId","STGS");// 100 项目 Id,我方提供(详见数据接口密钥文档)，下同
            sendJson.set("SectionCode","e22f0573-0604-448d-9617-dcfb298f2685");// 100 合同段编号，我方提供(详见数据接口密钥文 档)，GUID
            sendJson.set("FactoryCode","STTJ01-1");// 100 预制场编号，我方提供(详见数据接口密钥文 档)，下同
            String bw = trYajiangM.getLianghao();
            String keyword = "桥";
            String Bridge = "";
            String BeamCode = "";

            int index = bw.indexOf(keyword);
            // 再次根据空格进行分割
            if (index!=-1 && bw.length() >= index) {
                Bridge = bw.substring(0,index);
                BeamCode = bw.substring(index+1);
            } else {
                Bridge = bw;
                BeamCode = bw;
            }
            sendJson.set("Bridge",Bridge);// 20 桥梁名称
            sendJson.set("BeamCode",BeamCode);// string 100 梁体编号
            sendJson.set("LTType",trYajiangM.getLblx());// string 100 梁体类型
            sendJson.set("ProjectPart",trYajiangM.getLianghao());// string 100 工程部位
            sendJson.set("EquipmentMaker","");// string 100 设备厂家
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(trYajiangM.getYjsbbh());
            sendJson.set("EquipmentType",sbjwd.getSbname());// string 100 设备名称、型号
            sendJson.set("EquipmentCode","STTJ01-HTGS-GROUT-01");// string 100 设备编号
            sendJson.set("ZKBYL","");// float 8 真空泵压力
            sendJson.set("TensionTime",trYajiangM.getZlsj());// datetime 8 压浆时间
            sendJson.set("StartTime","");// datetime 8 开始时间
            sendJson.set("EndTime","");// datetime 8 结束时间
            sendJson.set("SGUnit",trYajiangM.getSgdw());// string 50 施工单位
            sendJson.set("JLUnit",trYajiangM.getJldw());// string 50 监理单位
            sendJson.set("Sort",trYajiangM.getYajiangbuzh());// String 50 压浆顺序
            sendJson.set("Count","");// int 4 压浆次数
            sendJson.set("Mode","");// string 50 压浆模式
            sendJson.set("Status",2);// int 4 状态(1：开始 2：结束)
            sendJson.set("Direction",trYajiangM.getYajiangfang());// string 50 压浆方向
            sendJson.set("Step","");// string 50 压浆步骤
            sendJson.set("EnvironmentTemp","");// string 50 环境温度
            sendJson.set("GroutTemp",trYajiangM.getSw());// string 50 浆液温度
            sendJson.set("SGWorker","");// string 50 施工员
            sendJson.set("QAWorker","");// string 50 质量检测人员
            sendJson.set("JLWorker","");// string 50 监理人员
            sendJson.set("Principal","");// string 50 检测负责人
            sendJson.set("Recorder","");// string 50 记录人
            sendJson.set("IsWarning","");// int 4 控制情况(1:正常 0:异常) 9
            sendJson.set("YJLOK",1);// int 1 压浆量是否正常(0-否,1-是)
            sendJson.set("SJBOK",1);// int 1 水胶比是否正常(0-否,1-是)
            sendJson.set("YJWYOK",1);// int 1 压浆稳压是否正常(0-否,1-是)

            List GroutInfoDataList = new ArrayList();

            JSONObject snObject = new JSONObject();

            snObject.set("SNPZ",trYajiangM.getSnmc());// string 50 水泥(名称)品种
            snObject.set("SNBH","");// string 50 水泥标号
            snObject.set("SNCJ","");// string 50 水泥厂家
            snObject.set("JBTime","");// int 4 搅拌时间(s)
            snObject.set("SJB",trYajiangM.getShuijiaobi());// float 8 水胶比
            snObject.set("SNYL",trYajiangM.getSnyl());// float 8 水泥用量(kg)
            snObject.set("SYL","");// float 8 水用量(kg)
            snObject.set("WJJName",trYajiangM.getCpzjl());// string 50 外加剂名称
            snObject.set("WJJYL",trYajiangM.getCjsjl());// float 8 外加剂用量(kg)
            snObject.set("SNJQD_Design","");// float 8 水泥浆强度(设计)
            snObject.set("SNJQD_Actual","");// float 8 水泥浆强度(实测)
            snObject.set("SNJCD_Design","");// float 8 水泥浆稠度(设计)
            snObject.set("SNJCD_Actual","");// float 8 水泥浆稠度(实测)
            snObject.set("SNJLDD_Design","");// float 8 水泥浆流动度(设计)
            snObject.set("SNJLDD_Actual","");// float 8 水泥浆流动度(实测)
            snObject.set("ZDQSL_Design","");// float 8 最大泌水率(设计)
            snObject.set("ZDQSL_Actual",trYajiangM.getMsl());// float 8 最大泌水率(实测)
            snObject.set("SNBH_3H_Design","");// float 8 水泥拌和后 3h 泌水率(设计)
            snObject.set("SNBH_3H_Actual","");// float 8 水泥拌和后 3h 泌水率(实测)
            snObject.set("SNBH_24H_Design","");// float 8 水泥拌和后 24h 泌水率(设计)
            snObject.set("SNBH_24H_Actual","");// float 8 水泥拌和后 24h 泌水率(实测)
            GroutInfoDataList.add(snObject);
            sendJson.set("GroutInfoDataList",GroutInfoDataList);// List<HTGS_GroutInfo> 梁体水泥浆信息

            LambdaQueryWrapper<TrYajiangS> yajiangSLambdaQueryWrapper = new LambdaQueryWrapper<>();
            yajiangSLambdaQueryWrapper.eq(TrYajiangS::getSyjid,trYajiangM.getSyjid());
            List<TrYajiangS> list = yajiangSService.list(yajiangSLambdaQueryWrapper);

            List GroutResultDataList = new ArrayList();
            for (TrYajiangS trYajiangS : list) {
                JSONObject sonObject = new JSONObject();

                sonObject.set("KDCode",trYajiangS.getKongdao());// string 50 孔道编号
                sonObject.set("DataID","");// int 4 梁体压浆编号
                sonObject.set("StartTime",trYajiangS.getStarttime());// datetime 8 开始时间
                sonObject.set("EndTime",trYajiangS.getEndtime());// datetime 8 结束时间
                sonObject.set("JJYL",trYajiangS.getJinjiangyal());// float 8 进浆压力(MPa)
                sonObject.set("CJYL",trYajiangS.getFanjiangyal());// float 8 出浆压力(MPa)
                sonObject.set("WYYL",trYajiangS.getZkyl());// float 8 稳压压力(MPa)
                sonObject.set("ZYJL",trYajiangS.getJinjiangshu());// float 8 总压浆量(L)
                sonObject.set("BCSJ",trYajiangS.getChixushijia());// int 4 保(持)压时间(s)
                sonObject.set("SJB",trYajiangS.getShuijiaobi());// float 8 水胶比
                sonObject.set("LLJL","");// float 8 理论浆量
                sonObject.set("SCJL","");// float 8 实测总压浆浆量
                sonObject.set("YJLVol","");// float 8 压浆料体积
                sonObject.set("WaterVol","");// float 8 水体积
                sonObject.set("YJCS",trYajiangS.getYjcs());// int 4 压浆次数
                sonObject.set("YJLOK","1");// int 1 压浆量是否正常(0-否,1-是)
                sonObject.set("SJBOK","1");// int 1 水胶比是否正常(0-否,1-是)
                sonObject.set("YJWYOK","1");// int 1 压浆稳压是否正常(0-否,1-是)
                sonObject.set("IsOK","1");// int 4 是否正常（0-否,1-是）
                sonObject.set("Memo","");// string 500 备注
                GroutResultDataList.add(sonObject);
            }
            sendJson.set("GroutResultDataList",GroutResultDataList);// List<HTGS_GroutResult> 单孔道压浆结果数据

            String body = HttpRequest.post(url)
                    .header("content-type","application/json")
                    .body(String.valueOf(sendJson))
                    .execute()
                    .body();
            System.out.println(body);
            System.out.println(sendJson);
            pushandreturnService.saveData(id,String.valueOf(sendJson),selectsysconfigone.getRemark(),body);
            sysConfigService.updateSysConfig(JobUtil.ST_YJ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
