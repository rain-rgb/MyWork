package org.jeecg.modules.job.yggcJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName shiYanJiJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/16 15:13
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class shiYanJiJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService itSyjzbService;
    @Autowired
    private IFYalijiService yalijiService;
    @Autowired
    private IFWangnjService wangnjService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysDepartService sysDepartService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HSY_SYJ);//杭绍甬试验机推送阳光工程
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到杭绍甬试验室的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输杭绍甬试验室的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TSyjzb> tSyjzbs = itSyjzbService.selectLists(shebeilist, curid);
        if (null == tSyjzbs || tSyjzbs.size() == 0) {
            log.info(String.format("暂无杭绍甬试验室未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TSyjzb tSyjzb : tSyjzbs) {
            id = tSyjzb.getId();
            JSONObject sendDate = new JSONObject();
            sendDate.set("area", tSyjzb.getArea());
            sendDate.set("beizhu", tSyjzb.getBeizhu());
            sendDate.set("cjmc", tSyjzb.getCjmc());
            sendDate.set("czry", tSyjzb.getCzry());
            sendDate.set("gczj", tSyjzb.getGczj());
            sendDate.set("iswjj", tSyjzb.getIswjj());
            sendDate.set("judgestate", tSyjzb.getJudgestate());
            sendDate.set("lq", tSyjzb.getLq());
            String sbbh = tSyjzb.getSbbh();
            sendDate.set("sbbh", sbbh);
            QueryWrapper<ShebeiInfo> shebeiInfo = new QueryWrapper();
            shebeiInfo.eq("sbjno", sbbh);
            ShebeiInfo one = shebeiInfoService.getOne(shebeiInfo);
            QueryWrapper<SysDepart> sysDepartQueryWrapper = new QueryWrapper();
            sysDepartQueryWrapper.eq("org_code", one.getSysOrgCode());
            SysDepart one1 = sysDepartService.getOne(sysDepartQueryWrapper);

            sendDate.set("organizeId", one1.getOrgCode());
            sendDate.set("organizeName", one1.getDepartName());
            sendDate.set("overproofStatus", tSyjzb.getOverproofStatus());
            sendDate.set("pdjg", tSyjzb.getPdjg());
            sendDate.set("pzbm", tSyjzb.getPzbm());
            sendDate.set("qddbz", tSyjzb.getQddbz());
            sendDate.set("recGuid", tSyjzb.getRecGuid());
            sendDate.set("rtcode", tSyjzb.getRtcode());
            sendDate.set("sectionId", one1.getOrgCode());
            sendDate.set("sectionName", one1.getDepartName());
            sendDate.set("sjbh", tSyjzb.getSjbh());
            sendDate.set("sjcc", tSyjzb.getSjcc());
            sendDate.set("sjmj", tSyjzb.getSjmj());
            sendDate.set("sjqd", tSyjzb.getSjqd());
            sendDate.set("sjscsj", tSyjzb.getSjscsj());
            sendDate.set("sjsl", tSyjzb.getSjscsj());
            sendDate.set("status", tSyjzb.getStatus());
            sendDate.set("syjid", tSyjzb.getSyjid());
            sendDate.set("sylx", tSyjzb.getSylx());
            sendDate.set("syrq", tSyjzb.getSyrq());
            sendDate.set("sywcsj", tSyjzb.getSywcsj());
            sendDate.set("szfw", tSyjzb.getSzfw());
            sendDate.set("tjstate", 0);
            sendDate.set("version", 0);
            sendDate.set("wtbh", tSyjzb.getWtbh());
            sendDate.set("wtid", tSyjzb.getWtid());
            sendDate.set("wtzs", tSyjzb.getWtzs());
            sendDate.set("zsxs", tSyjzb.getZsxs());
            sendDate.set("zzrq", tSyjzb.getZzrq());

            if (one.getSbtype() == 3) {
                sendDate.set("dataType", 0);
                QueryWrapper<FWangnj> queryWrapper = new QueryWrapper();
                queryWrapper.eq("syjid", tSyjzb.getSyjid());
                List<FWangnj> wangnjs = wangnjService.list(queryWrapper);
                List yalijisson = new ArrayList();
                for (FWangnj wangnj : wangnjs) {
                    JSONObject sendDatesson = new JSONObject();
                    sendDatesson.set("dhbz", wangnj.getDhbz());
                    sendDatesson.set("dkwz", wangnj.getDkwz());
                    String fGuid = wangnj.getFGuid();
                    if (fGuid==null){
                        sendDatesson.set("fid", wangnj.getSyjid()+wangnj.getId());
                    }else {
                        sendDatesson.set("fid", wangnj.getFGuid());
                    }
                    sendDatesson.set("ldcms", wangnj.getLdcms());
                    sendDatesson.set("lz", wangnj.getLz());
                    sendDatesson.set("lzgc", wangnj.getLzgc());
                    sendDatesson.set("lzqd", wangnj.getLzqd());
                    sendDatesson.set("qflz", wangnj.getQflz());
                    sendDatesson.set("qfqd", wangnj.getQfqd());
                    sendDatesson.set("scl", wangnj.getScl());
                    sendDatesson.set("sjgc", wangnj.getSjgc());
                    sendDatesson.set("sjxh", wangnj.getSjxh());
                    sendDatesson.set("status", wangnj.getStatus());
                    sendDatesson.set("syjid", wangnj.getSyjid());
                    sendDatesson.set("sysj", wangnj.getSysj());
                    sendDatesson.set("version", wangnj.getVersion());
                    sendDatesson.set("wcsj", wangnj.getWcsj());
                    sendDatesson.set("wsbz", wangnj.getWsbz());
                    sendDatesson.set("wy", wangnj.getWy());
                    sendDatesson.set("zdlzscl", wangnj.getZdlzscl());
                    yalijisson.add(sendDatesson);
                }
                sendDate.set("fsWangnjList", yalijisson);
                String back = HttpRequest.post("http://39.174.68.27:18086/zeiet/wnjData/save")
                        .body(String.valueOf(sendDate))
                        .execute()
                        .body();

                JSONObject jsonObject1 = new JSONObject(back);
                String codes = jsonObject1.get("code").toString();
                if ("200".equals(codes)) {
                    log.info(String.format("杭绍甬万能机推送阳光工程成功!" + id + "Json数据" + sendDate));
                } else {
                    log.info(String.format("杭绍甬万能机推送阳光工程失败!" + id + "Json数据" + sendDate));
                }
            } else {
                sendDate.set("dataType", 1);
                QueryWrapper<FsYaliji> queryWrapper = new QueryWrapper();
                queryWrapper.eq("syjid", tSyjzb.getSyjid());
                List<FsYaliji> yalijis = yalijiService.list(queryWrapper);
                List yalijisson = new ArrayList();
                for (FsYaliji yaliji : yalijis) {
                    JSONObject sendDatesson = new JSONObject();
                    String fGuid = yaliji.getFGuid();
                    if (fGuid==null){
                        sendDatesson.set("fid", yaliji.getSyjid()+yaliji.getId());
                    }else {
                        sendDatesson.set("fid", yaliji.getFGuid());
                    }
                    sendDatesson.set("kylz", yaliji.getKylz());
                    sendDatesson.set("kyqd", yaliji.getKyqd());
                    sendDatesson.set("qrcode", yaliji.getQrcode());
                    sendDatesson.set("sjgc", yaliji.getSjgc());
                    sendDatesson.set("sjxh", yaliji.getSjxh());
                    sendDatesson.set("status", yaliji.getStatus());
                    sendDatesson.set("syjid", yaliji.getSyjid());
                    sendDatesson.set("sysj", yaliji.getSysj());
                    sendDatesson.set("version", yaliji.getVersion());
                    sendDatesson.set("wcsj", yaliji.getWcsj());
                    sendDatesson.set("yskylz", yaliji.getYskylz());
                    sendDatesson.set("zzjj", yaliji.getZzjj());
                    yalijisson.add(sendDatesson);
                }
                sendDate.set("fsYalijiList", yalijisson);
                String back = HttpRequest.post("http://39.174.68.27:18086/zeiet/yljData/save")
                        .body(String.valueOf(sendDate))
                        .execute()
                        .body();

                JSONObject jsonObject1 = new JSONObject(back);
                String codes = jsonObject1.get("code").toString();
                if ("200".equals(codes)) {
                    log.info(String.format("杭绍甬压力机推送阳光工程成功!" + id) );
                } else {
                    log.info(String.format("杭绍甬压力机推送阳光工程失败!" + id) );
                }
                sysConfigService.updateSysConfig(JobUtil.HSY_SYJ, id);//根据传过来的唯一值来修改当前判断到的数据id
            }
        }
    }
}

