package org.jeecg.modules.job.syjjob;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.syj.entity.FsYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.entity.fswangnj;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.syj.vo.TSyjzbWnjPage;
import com.trtm.iot.syj.vo.YljPage;
import com.trtm.iot.syjyujingconfig.entity.SyjYujingConfig;
import com.trtm.iot.syjyujingconfig.service.ISyjYujingConfigService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDictItem;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
public class SyjSmsYuJingJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService itSyjzbService;
    @Autowired
    private ISyjYujingConfigService syjYujingConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysMessageCoreService sysMessageCoreService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SYJ_DXYJ);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到试验机定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置需要推送试验机数据的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TSyjzb> syjYJList = itSyjzbService.selectSYJYuJingData(curid, shebeilist);
        if (null == syjYJList || syjYJList.size() == 0) {
            log.info(String.format("暂无试验机预警的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TSyjzb syjOne : syjYJList) {//试验机预警数据
            id = syjOne.getId();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(syjOne.getSbbh());
            //试验机预警配置信息查询
            QueryWrapper<SyjYujingConfig> syjyujingQuery = new QueryWrapper<>();
            syjyujingQuery.eq("shebei_no", selectshebeione.getSbjno());
            syjyujingQuery.eq("is_call", 0);
            SyjYujingConfig syjConfig = syjYujingConfigService.getOne(syjyujingQuery);
            //有预警短信配置
            if (ObjectUtil.isNotEmpty(syjConfig)) {
                String itemText = itSyjzbService.selectSylxName(syjOne.getSylx());
                String info = "";
                if (ObjectUtil.isNotEmpty(syjOne.getSjbh())) {
                    info = itemText + "，" + syjOne.getSjbh() + "，该试验不合格，请相关管理人员处置、闭环！！";
                } else {
                    info = itemText + "，" + syjOne.getWtbh() + "，该试验不合格，请相关管理人员处置、闭环！！";
                }

                Boolean yesOrNot = saveSms(selectshebeione, info, syjConfig);
                if (yesOrNot) {
                    sysConfigService.updateSysConfig(JobUtil.SYJ_DXYJ, id);//根据传过来的唯一值来修改当前判断到的数据id
                    log.info("力学试验预警短信数据添加成功!!");
                } else {
                    log.info("力学试验预警短信数据添加失败..");
                }
            } else {
                log.info("该设备机没有预警短信配置...");
            }
        }
    }

    public Boolean saveSms(ShebeiInfo selectshebeione, String info, SyjYujingConfig syjConfig) {
        com.alibaba.fastjson.JSONObject obj = new com.alibaba.fastjson.JSONObject();
        obj.put("sbname", selectshebeione.getSbname());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        obj.put("time", sdf.format(now));
        obj.put("content", info);
        SysMessageCore sysSms = new SysMessageCore();
        sysSms.setEsTitle("力学试验数据预警");
        sysSms.setEsType("1");
        sysSms.setEsReceiver(syjConfig.getPhones());
        sysSms.setEsContent(obj.toString());
        sysSms.setEsSendStatus("0");
        sysSms.setEsSendNum(0);
        sysSms.setRemark(selectshebeione.getSbjno());
        return sysMessageCoreService.save(sysSms);
    }
}
