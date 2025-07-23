package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.yajiangss.entity.TrYajiangSS;
import com.trtm.iot.yajiangss.service.ITrYajiangSSService;
import com.trtm.iot.yj.entity.TrYajiang;
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

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName YaJiangJob：
 * @Description TODO
 * @Author zml
 * @Date 2022/05/10 10:42
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class YaJiangJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrYajiangMService trYajiangMService;
    @Autowired
    private ITrYajiangRenwudanService trYajiangRenwudanService;
    @Autowired
    private ITrYajiangSService trYajiangSService;
    @Autowired
    private ITrYajiangSSService trYajiangSSService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_YAJIANG);//瑞仓内网压浆数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到瑞仓内网压浆数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info("没有配置要传输瑞仓压浆设备" + DateUtils.now());
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
//        String[] split = shebeilist.split(",");
//        List<String> strsToList1 = Arrays.asList(split);
        List<TrYajiangRenwudan> selecthntbhzones = trYajiangRenwudanService.selectLists(shebeilist, 0);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info("暂无瑞仓内网压浆未推送数据" + DateUtils.now());
            return;
        }
        int id = 0;
        for (TrYajiangRenwudan trYajiangRenwudan : selecthntbhzones) {
            JSONObject sendObject = JSONUtil.createObj();
            JSONObject sendObject1 = JSONUtil.createObj();
            JSONObject sendObject2 = JSONUtil.createObj();
            String urls = null;
            Integer code1 = 0;
            Integer code3 = 0;
            id = trYajiangRenwudan.getId();
            List<TrYajiangM> trYajiangMList = trYajiangMService.selectLists(trYajiangRenwudan.getUuid());
            if (trYajiangMList.size() == 0) {
                trYajiangMList = trYajiangMService.selectList(trYajiangRenwudan.getShebeibianhao());
            }
            if (trYajiangMList.size() > 0) {
                for (TrYajiangM trYajiangM : trYajiangMList) {
                    List<TrYajiangS> trYajiangSList = trYajiangSService.selectListTrYajiangS(trYajiangM.getSyjid());
                    for (TrYajiangS trYajiangS : trYajiangSList) {
                        List<TrYajiangSS> trYajiangSSList = trYajiangSSService.selectList(trYajiangS.getHoleid());
                        if (trYajiangSSList.size() > 0) {
                            sendObject2.set("trYajiangSS", trYajiangSSList);
                            urls = "https://zgj20.cncico.com/wlwpt/yajiangs/trYajiangSS/addList";
                            code3 = getcodes(sendObject2, urls);
                            if (code3 == 200) {
                                log.info("瑞仓内网压浆子表过程值数据推送成功!" + "Json数据" + sendObject2);
                            } else {
                                log.info("瑞仓内网压浆子表过程值数据推送失败/数据已存在!" + "Json数据" + sendObject2);
                            }
                        } else {
                            log.info("压浆设备" + trYajiangRenwudan.getShebeibianhao() + "没有子表过程值数据" + DateUtils.now());
                        }
                    }
                    sendObject.set("yjsbbh", trYajiangM.getYjsbbh());
                    sendObject.set("syjid", trYajiangM.getSyjid());
                    sendObject.set("sgdw", trYajiangM.getSgdw());
                    sendObject.set("jldw", trYajiangM.getJldw());
                    sendObject.set("gjbh", trYajiangM.getGjbh());
                    sendObject.set("gcmc", trYajiangM.getGcmc());
                    sendObject.set("yjsj", trYajiangM.getYjsj());
                    sendObject.set("zhbw", trYajiangM.getZhbw());
                    sendObject.set("sgbw", trYajiangM.getSgbw());
                    sendObject.set("gjjg", trYajiangM.getGjjg());
                    sendObject.set("qw", trYajiangM.getQw());
                    sendObject.set("cjsjl", trYajiangM.getCjsjl());
                    sendObject.set("cpzjl", trYajiangM.getCpzjl());
                    sendObject.set("sw", trYajiangM.getSw());
                    sendObject.set("shuijiaobi", trYajiangM.getShuijiaobi());
                    sendObject.set("snyl", trYajiangM.getSnyl());
                    sendObject.set("yjwd", trYajiangM.getYjwd());
                    sendObject.set("msl", trYajiangM.getMsl());
                    sendObject.set("beiyong", trYajiangM.getBeiyong());
                    sendObject.set("htbh", trYajiangM.getHtbh());
                    sendObject.set("lblx", trYajiangM.getLblx());
                    sendObject.set("lianghao", trYajiangM.getLianghao());
                    sendObject.set("zlsj", trYajiangM.getZlsj());
                    sendObject.set("yajiangji", trYajiangM.getYajiangji());
                    sendObject.set("snmc", trYajiangM.getSnmc());
                    sendObject.set("kongdaoshu", trYajiangM.getKongdaoshu());
                    sendObject.set("yajiangfang", trYajiangM.getYajiangfang());
                    sendObject.set("yajiangbuzh", trYajiangM.getYajiangbuzh());
                    sendObject.set("yajiangguoc", trYajiangM.getYajiangguoc());
                    sendObject.set("chushisudu", trYajiangM.getChushisudu());
                    sendObject.set("memo", trYajiangM.getMemo());
                    sendObject.set("liudongdu", trYajiangM.getLiudongdu());
                    sendObject.set("status", trYajiangM.getStatus());
                    sendObject.set("guid", trYajiangM.getGuid());
                    sendObject.set("uuid", trYajiangM.getUuid());
                    sendObject.set("issend", trYajiangM.getIssend());
//                    sendObject.set("trYajiangM", trYajiangM);
                    if (trYajiangSList.size() > 0) {
                        sendObject.set("yajiangs", trYajiangSList);
                    } else {
                        log.info("压浆设备" + trYajiangRenwudan.getShebeibianhao() + "没有zi表实时数据" + DateUtils.now());
                    }
                    urls = "https://zgj20.cncico.com/wlwpt/yajiangm/trYajiangM/addYaJiang";
                    Integer code2 = getcodes(sendObject, urls);
                    if (code2 == 200) {
                        TrYajiangM trYajiangM1 = new TrYajiangM();
                        trYajiangM1.setId(trYajiangM.getId());
                        trYajiangM1.setIsruicang(1);
                        trYajiangMService.updateById(trYajiangM1);
                        log.info("瑞仓内网压浆主表/子表实时数据推送成功!" + id + "Json数据" + sendObject);
                    } else {
                        log.info("瑞仓内网压浆主表/子表实时数据推送失败!" + id + "Json数据" + sendObject);
                    }
                }
            } else {
                log.info("压浆设备" + trYajiangRenwudan.getShebeibianhao() + "没有主表信息" + DateUtils.now());
            }
            sendObject1.set("uuid", trYajiangRenwudan.getUuid());
            sendObject1.set("projectname", trYajiangRenwudan.getProjectname());
            sendObject1.set("girderplace", trYajiangRenwudan.getGirderplace());
            sendObject1.set("component", trYajiangRenwudan.getComponent());
            sendObject1.set("sgbwuuid", trYajiangRenwudan.getSgbwuuid());
            sendObject1.set("sgbwname", trYajiangRenwudan.getSgbwname());
            sendObject1.set("pedestal", trYajiangRenwudan.getPedestal());
            sendObject1.set("status", trYajiangRenwudan.getStatus());
            sendObject1.set("yjdate", trYajiangRenwudan.getYjdate());
            sendObject1.set("createTime", trYajiangRenwudan.getCreateTime());
            sendObject1.set("updateTime", trYajiangRenwudan.getUpdateTime());
            sendObject1.set("departid", trYajiangRenwudan.getDepartid());
            sendObject1.set("orgcode", trYajiangRenwudan.getOrgcode());
            sendObject1.set("createBy", trYajiangRenwudan.getCreateBy());
            sendObject1.set("sysOrgCode", trYajiangRenwudan.getSysOrgCode());
            sendObject1.set("departname", trYajiangRenwudan.getDepartname());
            sendObject1.set("shebeibianhao", trYajiangRenwudan.getShebeibianhao());
            sendObject1.set("treeid", trYajiangRenwudan.getTreeid());
//            sendObject1.set("trYajiangRenwudan", trYajiangRenwudan);
            urls = "https://zgj20.cncico.com/wlwpt/yajiangrenwudan/trYajiangRenwudan/addOpen";
            code1 = getcodes(sendObject1, urls);
            if (code1 == 200) {
                TrYajiangRenwudan trYajiangRenwudan1 = new TrYajiangRenwudan();
                trYajiangRenwudan1.setId(trYajiangRenwudan.getId());
                trYajiangRenwudan1.setIsruicang(1);
                trYajiangRenwudanService.updateById(trYajiangRenwudan1);
//                sysConfigService.updateSysConfig(JobUtil.RC_YAJIANG, id);//根据传过来的唯一值来修改当前判断到的数据id
                log.info("瑞仓内网压浆任务单数据推送成功!" + "Json数据" + sendObject1);
            } else {
                log.info("瑞仓内网压浆任务单数据推送失败!" + "Json数据" + sendObject1);
            }
        }
    }

    private Integer getcodes(JSONObject sendObject, String urls) {
        String body = HttpRequest.post(urls)
                .header("Content-Type", "application/json")
                .body(String.valueOf(sendObject))
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject1 = new JSONObject(body);
        return (Integer) jsonObject1.get("code");
    }
}
