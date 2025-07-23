package org.jeecg.modules.job.yongjinqushang;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.jtwbs.entity.Jtwbs;
import com.trtm.iot.jtwbs.mapper.JtwbsMapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.mapper.WztaizhangMapper;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.xkcoding.http.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @ClassName yjsxJZLJob
 * @Author alalei
 * @Date 2024/12/9 14:06
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class yjsxJYPJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private WztaizhangMapper wztaizhangMapper;
    @Autowired
    private JtwbsMapper jtwbsMapper;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "http://1.14.103.201:8401/IOTDataAcquisition/pmtapi/Wz_Interface/saveEntryledger.do";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJGS_JYPTS);//甬金高速（绍兴段）检验批数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到甬金绍兴段检验批数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要推送甬金绍兴段检验批数据的组织机构" + DateUtils.now()));
            return;
        }
        String sysOrgCode = jsonObject.getStr("sysOrgCode");
        List<Wztaizhang> wztaizhangList = wztaizhangService.selectRWDList(sysOrgCode, curid);
        if (ObjectUtil.isEmpty(wztaizhangList)) {
            log.info(String.format("暂无甬金高速绍兴段检验批未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (Wztaizhang wztaizhang : wztaizhangList) {
            id = wztaizhang.getId();
            JSONObject sendObject = new JSONObject();
            sendObject.set("fId", wztaizhang.getId());//数据唯⼀标识符(必填)
            Jtwbs jtwbs = jtwbsMapper.getJtwbs(wztaizhang.getSysOrgCode());
            sendObject.set("fCompanyid", getIdByOrgCode(jtwbs.getSysOrgCode()));//标段id(必填)
            sendObject.set("fCompanyname", jtwbs.getName());//标段名称(必填)
            sendObject.set("fMaterialname", wztaizhangMapper.getCaiLiaoName(wztaizhang.getCailiaono()));//材料名称
            sendObject.set("fSpecification", wztaizhang.getGuigexinghao());//规格型号
            sendObject.set("fUnit", wztaizhang.getDanwei());//单位
            sendObject.set("fSuppliername", wztaizhangMapper.getGongYingShangName(wztaizhang.getGongyingshangdanweibianma()));//供应商名称
            sendObject.set("fBatchnumber", wztaizhang.getPici());//批号
            sendObject.set("fEntrynum", wztaizhang.getJingzhongt() + wztaizhang.getDanwei());//进场数量
            sendObject.set("fEntrydates", (wztaizhang.getJinchangshijian().split("\\s+"))[0]);//进场日期
            sendObject.set("fLocationname", wztaizhang.getStoragePlace());//存放位置名称集合 多位置用,隔开
            sendObject.set("fIntendeduse", wztaizhang.getUsePart());//拟用部位
            if (StringUtil.isNotEmpty(wztaizhang.getNodetype())) {
                sendObject.set("fMaterialtypeid", wztaizhang.getNodetype());//材料类型编号
                String dictId = wztaizhangMapper.getDictId();
                sendObject.set("fMaterialtypename", wztaizhangMapper.getNodetypeName(dictId, wztaizhang.getNodetype()));//材料类型名称
            }else {
                sendObject.set("fMaterialtypeid", "");//材料类型编号
                sendObject.set("fMaterialtypename","");//材料类型名称
            }

            sendObject.set("fMaterialid", "");//材料编号
            sendObject.set("fDetecttype", "");//材料检测类型
            sendObject.set("fInsfrequency", "");//检测频率
            sendObject.set("fBasedstandard", "");//依据标准
            sendObject.set("fSupplierid", "");//供应商id
            sendObject.set("fManufacturername", "");//生产厂家
            sendObject.set("fManufacturerid", "");//生产厂家id
            sendObject.set("fInspectionnum", "");//应检次数
            sendObject.set("fLocationid", "");//存放位置id集合 多位置用,隔开
            sendObject.set("fLocationnum", "");//存放数量集合 多位置用,隔开
            sendObject.set("fReportname", "");//操作人姓名
            sendObject.set("fFileurl", "");//资料
            sendObject.set("fNote", "");//备注
            sendObject.set("fLjusername", "");//LJ单位抄送人姓名 多人用逗号隔开
            sendObject.set("fJlusername", "");//JL单位抄送人姓名 多人用逗号隔开
            sendObject.set("fYzusername", "");//业主单位抄送人姓名 多人用逗号隔开

            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(body);
            int status = Integer.parseInt(String.valueOf(jsonObject1.get("status")));
            pushandreturnService.saveData(id, String.valueOf(sendObject), "检验批推送云检", body);
            if (status == 200) {
                log.info(String.format("甬金绍兴段检验批数据推送成功！" + DateUtils.now()));
                sysConfigService.updateSysConfig(JobUtil.YJGS_JYPTS, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("甬金绍兴段检验批数据推送失败！" + DateUtils.now()));
            }
        }
    }

    public String getIdByOrgCode(String sysOrgCode) {
        switch (sysOrgCode) {
            case "A05A01A02A08A01A01A01":
                return "f52315a8-2e09-4488-b490-641b4e793b4f";
            case "A05A01A02A08A01A01A02":
                return "20148ca6-e612-4835-b6a9-4acd7a82a5cb";
            case "A05A01A02A08A01A02A01":
                return "11e3fc11-22cb-46ee-829a-e33950e3bb69";
            case "A05A01A02A08A01A02A02":
                return "882daf4e-063e-4de6-93b2-c1c627ef6119";
            default:
                return null;
        }
    }

}
