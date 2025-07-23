package org.jeecg.modules.job.yongjinqushang;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.jtwbs.entity.Jtwbs;
import com.trtm.iot.jtwbs.mapper.JtwbsMapper;
import com.trtm.iot.jtwbs.service.IJtwbsService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class yjsxJZLJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private SysDepartMapper sysDepartMapper;
    @Autowired
    private JtwbsMapper jtwbsMapper;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private String url = "http://1.14.103.201:8401//IOTDataAcquisition/dataAcquisitionController/receivePouringOrder";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJGS_JZLTS);//甬金高速（绍兴段）浇筑令数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到甬金绍兴段浇筑令数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要推送甬金绍兴段浇筑令数据的组织机构" + DateUtils.now()));
            return;
        }
        String sysOrgCode = jsonObject.getStr("sysOrgCode");
        List<Bhzrenwudan> bhzRWDList = bhzrenwudanService.selectRWDList(sysOrgCode, curid);
        if (ObjectUtil.isEmpty(bhzRWDList)) {
            log.info(String.format("暂无甬金高速绍兴段浇筑令未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Bhzrenwudan bhzrenwudan : bhzRWDList) {
            id = bhzrenwudan.getId();
            JSONObject sendObject = new JSONObject();
            sendObject.set("fId", id);//数据唯⼀标识符(必填)
            sendObject.set("fProjectid", "49b3e853-cfbf-4445-8466-c5e79c87064d");//项⽬id(必填)
            SysDepart depart = sysDepartMapper.getSysDepart(bhzrenwudan.getSysOrgCode());
            sendObject.set("fProjectname", depart.getDepartName());//项⽬名称(必填)
            Jtwbs jtwbs = jtwbsMapper.getJtwbs(bhzrenwudan.getSysOrgCode());
            sendObject.set("fCompanyid", getIdByOrgCode(jtwbs.getSysOrgCode()));//标段id(必填)
            sendObject.set("fCompanyname", jtwbs.getName());//标段名称(必填)
            sendObject.set("fLine", bhzrenwudan.getStation());//⽣产线标识(必填)
            sendObject.set("fTaskno", bhzrenwudan.getCode());//任务编号(必填)
            sendObject.set("fCreatedates", sdf.format(bhzrenwudan.getDattim()));//创建时间(必填)
            sendObject.set("fProjecttitle", bhzrenwudan.getProjname());//⼯程名称
            sendObject.set("fBuildpartid", bhzrenwudan.getTreeid());//施⼯部位id
            sendObject.set("fBuildpart", bhzrenwudan.getConspos());//施⼯部位
            sendObject.set("fPourmethod", bhzrenwudan.getPour());//浇筑⽅式
            sendObject.set("fType", bhzrenwudan.getVariety());//产品种类
            sendObject.set("fStrengthgrade", bhzrenwudan.getBetlev());//强度等级
            sendObject.set("fSlump", bhzrenwudan.getLands());//塌落度
            sendObject.set("fTaskvolume", bhzrenwudan.getMete());//任务⽅量
            Date begtim = bhzrenwudan.getBegtim();
            if (begtim != null) {
                sendObject.set("fPouringdates", sdf.format(bhzrenwudan.getBegtim()));//浇筑⽇期(必填)
            }else {
                sendObject.set("fPouringdates", sdf.format(bhzrenwudan.getDattim()));//浇筑⽇期(必填)
            }
            sendObject.set("fNote", bhzrenwudan.getNote());//备注

            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(body);
            String errcode = String.valueOf(jsonObject1.get("errcode"));
            pushandreturnService.saveData(id,String.valueOf(sendObject),"浇筑令推送云检",body);
            if ("0".equals(errcode)) {
                log.info(String.format("甬金绍兴段浇筑令数据推送成功！" + DateUtils.now()));
                sysConfigService.updateSysConfig(JobUtil.YJGS_JZLTS, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("甬金绍兴段浇筑令数据推送失败！" + DateUtils.now()));
            }
        }
    }

    public String getIdByOrgCode(String sysOrgCode){
        switch (sysOrgCode){
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
