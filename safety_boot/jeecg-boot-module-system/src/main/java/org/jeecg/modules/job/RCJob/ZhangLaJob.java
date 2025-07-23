package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglass.entity.TrZhanglaSS;
import com.trtm.iot.zhanglass.service.ITrZhanglaSSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ZhangLaJob：
 * @Description TODO
 * @Author zml
 * @Date 2022/05/09 10:52
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ZhangLaJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrZhanglaXxbService trZhanglaXxbService;
    @Autowired
    private ITrZhanglaRenwudanService trZhanglaRenwudanService;
    @Autowired
    private ITrZhanglaMService trZhanglaMService;
    @Autowired
    private ITrZhanglaSService trZhanglaSService;
    @Autowired
    private ITrZhanglaSSService trZhanglaSSService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_ZHANGLA);//瑞仓内网张拉数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓内网张拉数据推送定时任务的配置信息" + DateUtils.now()));
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
//        String[] split = shebeilist.split(",");
//        List<String> strsToList1 = Arrays.asList(split);
        List<TrZhanglaRenwudan> selecthntbhzones = trZhanglaRenwudanService.selectLists(shebeilist, 0);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info("暂无瑞仓内网张拉未推送数据" + DateUtils.now());
            return;
        }
        int id = 0;
        for (TrZhanglaRenwudan trZhanglaRenwudan : selecthntbhzones) {
            JSONObject sendObject = JSONUtil.createObj();
            JSONObject sendObject1 = JSONUtil.createObj();
            JSONObject sendObject2 = JSONUtil.createObj();
            JSONObject sendObject3 = JSONUtil.createObj();
            String urls = null;
            Integer code1 = 0;
            Integer code3 = 0;
            Integer code4 = 0;
            id = trZhanglaRenwudan.getId();
            List<TrZhanglaXxb> trZhanglaXxbList = trZhanglaXxbService.selectLists(trZhanglaRenwudan.getUuid());
            if (trZhanglaXxbList.size()==0){
                trZhanglaXxbList = trZhanglaXxbService.selectList(trZhanglaRenwudan.getShebeibianh());
            }
            if (trZhanglaXxbList.size()>0) {
                for (TrZhanglaXxb trZhanglaXxb : trZhanglaXxbList) {
                    List<TrZhanglaM> zhanglaMList = trZhanglaMService.selectmList(trZhanglaXxb.getSyjid());
                    for (TrZhanglaM trZhanglaM : zhanglaMList) {
                        List<TrZhanglaS> trZhanglaSList = trZhanglaSService.selelctList(trZhanglaM.getSyjid(), trZhanglaM.getFguid());
                        if (trZhanglaSList.size() > 0) {
                            sendObject2.set("trZhanglaS", trZhanglaSList);
                            urls = "https://zgj20.cncico.com/wlwpt/zhanglas/trZhanglaS/addList";
                            code3 = getcodes(sendObject2, urls);
                            if (code3 == 200) {
                                log.info("瑞仓内网张拉子表实时数据推送成功!" + "Json数据" + sendObject2);
                            } else {
                                log.info("瑞仓内网张拉子表实时数据推送失败!" + "Json数据" + sendObject2);
                            }
                        } else {
                            log.info("张拉设备" + trZhanglaXxb.getShebeibianhao() + "没有子表实时数据" + DateUtils.now());
                        }
                        List<TrZhanglaSS> zhanglaSSList = trZhanglaSSService.getselectList(trZhanglaM.getHoleid());
                        if (zhanglaSSList.size() > 0) {
                            sendObject3.set("trZhanglaSS", zhanglaSSList);
                            urls = "https://zgj20.cncico.com/wlwpt/zhanglass/trZhanglaSS/addList";
                            code4 = getcodes(sendObject3, urls);
                            if (code4 == 200) {
                                log.info("瑞仓内网张拉子表过程值数据推送成功!" + "Json数据" + sendObject3);
                            } else {
                                log.info("瑞仓内网张拉子表过程值数据推送失败!" + "Json数据" + sendObject3);
                            }
                        } else {
                            log.info("张拉设备" + trZhanglaRenwudan.getShebeibianh() + "没有子表过程值数据" + DateUtils.now());
                        }
                    }
                    sendObject.set("shebeibianhao", trZhanglaXxb.getShebeibianhao());
                    sendObject.set("syjid", trZhanglaXxb.getSyjid());
                    sendObject.set("gjbh", trZhanglaXxb.getGjbh());
                    sendObject.set("gcmc", trZhanglaXxb.getGcmc());
                    sendObject.set("yzlc", trZhanglaXxb.getYzlc());
                    sendObject.set("gjmc", trZhanglaXxb.getGjmc());
                    sendObject.set("sgsj", trZhanglaXxb.getSgsj());
                    sendObject.set("snsjqd", trZhanglaXxb.getSnsjqd());
                    sendObject.set("snskqd", trZhanglaXxb.getSnskqd());
                    sendObject.set("zly1", trZhanglaXxb.getZly1());
                    sendObject.set("ybbh1", trZhanglaXxb.getYbbh1());
                    sendObject.set("bdrq1", trZhanglaXxb.getBdrq1());
                    sendObject.set("zly2", trZhanglaXxb.getZly2());
                    sendObject.set("ybbh2", trZhanglaXxb.getYbbh2());
                    sendObject.set("bdrq2", trZhanglaXxb.getBdrq2());
                    sendObject.set("cbunit", trZhanglaXxb.getCbunit());
                    sendObject.set("jlunit", trZhanglaXxb.getJlunit());
                    sendObject.set("htbh", trZhanglaXxb.getHtbh());
                    sendObject.set("zlcsu", trZhanglaXxb.getZlcsu());
                    sendObject.set("zlcsk", trZhanglaXxb.getZlcsk());
                    sendObject.set("zlcsep", trZhanglaXxb.getZlcsep());
                    sendObject.set("zlbwpic", trZhanglaXxb.getZlbwpic());
                    sendObject.set("kualiang", trZhanglaXxb.getKualiang());
                    sendObject.set("scljss", trZhanglaXxb.getScljss());
                    sendObject.set("fmqkms", trZhanglaXxb.getFmqkms());
                    sendObject.set("memo", trZhanglaXxb.getMemo());
                    sendObject.set("status", trZhanglaXxb.getStatus());
                    sendObject.set("uuid", trZhanglaXxb.getUuid());
                    sendObject.set("issend", trZhanglaXxb.getIssend());
//                    sendObject.set("trZhanglaXxb",trZhanglaXxb);
                    if (zhanglaMList.size() > 0) {
                        sendObject.set("zhanglam", zhanglaMList);
                    } else {
                        log.info("张拉设备" + trZhanglaRenwudan.getShebeibianh() + "没有主表信息" + DateUtils.now());
                    }
                    urls = "https://zgj20.cncico.com/wlwpt/zhanglaxxb/trZhanglaXxb/addZhangla";
                    Integer code2 = getcodes(sendObject, urls);
                    if (code2 == 200) {
                        TrZhanglaXxb trZhanglaXxb1 = new TrZhanglaXxb();
                        trZhanglaXxb1.setId(trZhanglaXxb.getId());
                        trZhanglaXxb1.setIsruicang(1);
                        trZhanglaXxbService.updateById(trZhanglaXxb1);
                        log.info("瑞仓内网张拉信息/主表数据推送成功!" + id + "Json数据" + sendObject);
                    } else {
                        log.info("瑞仓内网张拉信息/主表数据推送失败!" + id + "Json数据" + sendObject);
                    }
                }
            }else {
                log.info("张拉设备" + trZhanglaRenwudan.getShebeibianh() + "没有主表信息数据" + DateUtils.now());
            }
            sendObject1.set("uuid", trZhanglaRenwudan.getUuid());
            sendObject1.set("projectname", trZhanglaRenwudan.getProjectname());
            sendObject1.set("girderplace", trZhanglaRenwudan.getGirderplace());
            sendObject1.set("component", trZhanglaRenwudan.getComponent());
            sendObject1.set("sgbwuuid", trZhanglaRenwudan.getSgbwuuid());
            sendObject1.set("sgbwname", trZhanglaRenwudan.getSgbwname());
            sendObject1.set("pedestal", trZhanglaRenwudan.getPedestal());
            sendObject1.set("status", trZhanglaRenwudan.getStatus());
            sendObject1.set("zldate", trZhanglaRenwudan.getZldate());
            sendObject1.set("createTime", trZhanglaRenwudan.getCreateTime());
            sendObject1.set("updateTime", trZhanglaRenwudan.getUpdateTime());
            sendObject1.set("departid", trZhanglaRenwudan.getDepartid());
            sendObject1.set("sjzll", trZhanglaRenwudan.getSjzll());
            sendObject1.set("createBy", trZhanglaRenwudan.getCreateBy());
            sendObject1.set("sysOrgCode", trZhanglaRenwudan.getSysOrgCode());
            sendObject1.set("zldate2", trZhanglaRenwudan.getZldate2());
            sendObject1.set("sjzll2", trZhanglaRenwudan.getSjzll2());
            sendObject1.set("status2", trZhanglaRenwudan.getStatus2());
            sendObject1.set("departname", trZhanglaRenwudan.getDepartname());
            sendObject1.set("shebeibianh", trZhanglaRenwudan.getShebeibianh());
            sendObject1.set("yylgczlgg", trZhanglaRenwudan.getYylgczlgg());
            sendObject1.set("yylgccdpp", trZhanglaRenwudan.getYylgccdpp());
            sendObject1.set("yylgcbh", trZhanglaRenwudan.getYylgcbh());
            sendObject1.set("mjjzlgg", trZhanglaRenwudan.getMjjzlgg());
            sendObject1.set("mjjcdpp", trZhanglaRenwudan.getMjjcdpp());
            sendObject1.set("mjjbh", trZhanglaRenwudan.getMjjbh());
            sendObject1.set("mdbzlgg", trZhanglaRenwudan.getMdbzlgg());
            sendObject1.set("mdbcdpp", trZhanglaRenwudan.getMjjcdpp());
            sendObject1.set("mdbbh", trZhanglaRenwudan.getMdbbh());
            sendObject1.set("lbgd", trZhanglaRenwudan.getLbgd());
            sendObject1.set("hntsjqd", trZhanglaRenwudan.getHntsjqd());
            sendObject1.set("zllwc", trZhanglaRenwudan.getZllwc());
            sendObject1.set("wcstatus", trZhanglaRenwudan.getWcstatus());
            sendObject1.set("treeid", trZhanglaRenwudan.getTreeid());
//            sendObject1.set("trZhanglaRenwudan",trZhanglaRenwudan);
            urls = "https://zgj20.cncico.com/wlwpt/trzhanglarenwudan/trZhanglaRenwudan/addOpen";
            code1 = getcodes(sendObject1, urls);
            if (code1 == 200) {
                TrZhanglaRenwudan trZhanglaRenwudan1 = new TrZhanglaRenwudan();
                trZhanglaRenwudan1.setId(trZhanglaRenwudan.getId());
                trZhanglaRenwudan1.setIsruicang(1);
                trZhanglaRenwudanService.updateById(trZhanglaRenwudan1);
//                sysConfigService.updateSysConfig(JobUtil.RC_ZHANGLA, id);//根据传过来的唯一值来修改当前判断到的数据id
                log.info("瑞仓内网张拉任务单数据推送成功!" + "Json数据" + sendObject1);
            } else {
                log.info("瑞仓内网张拉任务单数据推送失败!" + "Json数据" + sendObject1);
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
