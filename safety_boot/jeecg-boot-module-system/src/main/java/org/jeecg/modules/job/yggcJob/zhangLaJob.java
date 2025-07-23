package org.jeecg.modules.job.yggcJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
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
 * @ClassName zhangLaJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/19 9:58
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zhangLaJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;
    @Autowired
    private ITrZhanglaSService zhanglaService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysDepartService sysDepartService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HSY_ZL);//杭绍甬张拉推送阳光工程
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到张拉定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输张拉的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrZhanglaXxb> zhanglaXxbs = zhanglaXxbService.selectzlList(shebeilist, curid);
        if (null == zhanglaXxbs || zhanglaXxbs.size() == 0) {
            log.info(String.format("暂无张拉未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrZhanglaXxb zhanglaXxb : zhanglaXxbs) {
            id = zhanglaXxb.getId();
            JSONObject sendObject = JSONUtil.createObj();
            sendObject.set("bdrq1", zhanglaXxb.getBdrq1());
            sendObject.set("bdrq2", zhanglaXxb.getBdrq2());
            sendObject.set("cbunit", zhanglaXxb.getCbunit());
            sendObject.set("fmqkms", zhanglaXxb.getFmqkms());
            sendObject.set("gcmc", zhanglaXxb.getGcmc());
            sendObject.set("gjbh", zhanglaXxb.getGjbh());
            sendObject.set("gjmc", zhanglaXxb.getGjmc());
            sendObject.set("htbh", zhanglaXxb.getHtbh());
            sendObject.set("jlunit", zhanglaXxb.getJlunit());
            sendObject.set("kualiang", zhanglaXxb.getKualiang());
            sendObject.set("memo", zhanglaXxb.getMemo());
            sendObject.set("scljss", zhanglaXxb.getScljss());
            String sbbh = zhanglaXxb.getShebeibianhao();
            sendObject.set("shebeibianhao", sbbh);
            QueryWrapper<ShebeiInfo> shebeiInfo = new QueryWrapper();
            shebeiInfo.eq("sbjno", sbbh);
            ShebeiInfo one = shebeiInfoService.getOne(shebeiInfo);
            QueryWrapper<SysDepart> sysDepartQueryWrapper = new QueryWrapper();
            sysDepartQueryWrapper.eq("org_code", one.getSysOrgCode());
            SysDepart one1 = sysDepartService.getOne(sysDepartQueryWrapper);

            sendObject.set("sectionId", one1.getOrgCode());
            sendObject.set("sectionName", one1.getDepartName());
            sendObject.set("sgsj", zhanglaXxb.getSgsj());
            sendObject.set("snsjqd", zhanglaXxb.getSnsjqd());
            sendObject.set("snskqd", zhanglaXxb.getSnskqd());
            sendObject.set("status", zhanglaXxb.getStatus());
            sendObject.set("syjid", zhanglaXxb.getSyjid());
            String departName = one1.getDepartName();
            if (departName.contains("监")) {
                sendObject.set("type", "1");
            } else if (departName.contains("交竣工")) {
                sendObject.set("type", "2");
            } else {
                sendObject.set("type", "0");
            }
            sendObject.set("ybbh1", zhanglaXxb.getYbbh1());
            sendObject.set("ybbh2", zhanglaXxb.getYbbh2());
            sendObject.set("yzlc", zhanglaXxb.getYzlc());
            sendObject.set("zlbwpic", zhanglaXxb.getZlbwpic());
            sendObject.set("zlcsep", zhanglaXxb.getZlcsep());
            sendObject.set("zlcsk", zhanglaXxb.getZlcsk());
            sendObject.set("zlcsu", zhanglaXxb.getZlcsu());
            sendObject.set("zly1", zhanglaXxb.getZly1());
            sendObject.set("zly2", zhanglaXxb.getZly2());

            List zlmList = new ArrayList();
            QueryWrapper<TrZhanglaM> zhanglaMQueryWrapper = new QueryWrapper<>();
            zhanglaMQueryWrapper.eq("syjid", zhanglaXxb.getSyjid());
            List<TrZhanglaM> zhanglaMList = zhanglaMService.list(zhanglaMQueryWrapper);
            for (TrZhanglaM trZhanglaM : zhanglaMList) {
                JSONObject zhanglaMObject = JSONUtil.createObj();
                zhanglaMObject.set("clqk", trZhanglaM.getClqk());
                zhanglaMObject.set("czlb", trZhanglaM.getCzlb());
                zhanglaMObject.set("fguid", trZhanglaM.getFguid());
                zhanglaMObject.set("gsbh", trZhanglaM.getGsbh());
                zhanglaMObject.set("gsgs", trZhanglaM.getGsgs());
                zhanglaMObject.set("hege", trZhanglaM.getHege());
                zhanglaMObject.set("holeid", trZhanglaM.getHoleid());
                zhanglaMObject.set("htl", trZhanglaM.getHtl());
                zhanglaMObject.set("jlsj", trZhanglaM.getJlsj());
                zhanglaMObject.set("llscl", trZhanglaM.getLlscl());
                zhanglaMObject.set("memo", trZhanglaM.getMemo());
                zhanglaMObject.set("sclper", trZhanglaM.getSclper());
                zhanglaMObject.set("shebeibianhao", trZhanglaM.getShebeibianhao());
                zhanglaMObject.set("sjzll", trZhanglaM.getSjzll());
                zhanglaMObject.set("status", trZhanglaM.getStatus());
                zhanglaMObject.set("syjid", trZhanglaM.getSyjid());
                zhanglaMObject.set("txml", trZhanglaM.getTxml());
                zhanglaMObject.set("uuid", trZhanglaM.getUuid());
                zhanglaMObject.set("wt", trZhanglaM.getWt());
                zhanglaMObject.set("yxpc", trZhanglaM.getYxpc());
                zhanglaMObject.set("yzlb", trZhanglaM.getYzlb());
                zhanglaMObject.set("zlsj", trZhanglaM.getZlsj());
                zhanglaMObject.set("zscl", trZhanglaM.getZscl());
                zhanglaMObject.set("zzlb", trZhanglaM.getZzlb());

                List zlsList = new ArrayList();
                QueryWrapper<TrZhanglaS> zhanglaSQueryWrapper = new QueryWrapper<>();
                zhanglaSQueryWrapper.eq("fguid", trZhanglaM.getFguid());
                List<TrZhanglaS> zhanglaSList = zhanglaService.list(zhanglaSQueryWrapper);
                for (TrZhanglaS trZhanglaS : zhanglaSList) {
                    JSONObject zhanglaSObject = JSONUtil.createObj();
                    zhanglaSObject.set("chsj", trZhanglaS.getChsj());
                    zhanglaSObject.set("dcscl", trZhanglaS.getDcscl());
                    zhanglaSObject.set("dh", trZhanglaS.getDh());
                    zhanglaSObject.set("fguid", trZhanglaS.getFguid());
                    zhanglaSObject.set("gsbh", trZhanglaS.getGsbh());
                    zhanglaSObject.set("jdbfb", trZhanglaS.getJdbfb());
                    zhanglaSObject.set("jdscl", trZhanglaS.getJdscl());
                    zhanglaSObject.set("jdzll", trZhanglaS.getJdzll());
                    zhanglaSObject.set("ljhsl", trZhanglaS.getLjhsl());
                    zhanglaSObject.set("sid", trZhanglaS.getSid());
                    zhanglaSObject.set("status", trZhanglaS.getStatus());
                    zhanglaSObject.set("syjid", trZhanglaS.getSyjid());
                    zhanglaSObject.set("ybds", trZhanglaS.getYbds());
                    zlsList.add(zhanglaSObject);
                }
                zhanglaMObject.set("znzlGrandsonList", zlsList);
                zlmList.add(zhanglaMObject);
            }
            sendObject.set("znzlChildList", zlmList);
            System.out.println(sendObject);
            String back = HttpRequest.post("http://39.174.68.27:18086/zeiet/znzlMain/save")
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();

            JSONObject jsonObject1 = new JSONObject(back);
            String message = jsonObject1.get("message").toString();
            if ("操作成功".equals(message)) {
                log.info(String.format("杭绍甬张拉推送阳光工程成功!" + id));
            } else {
                log.info(String.format("杭绍甬张拉推送阳光工程失败!" + id));
            }
            sysConfigService.updateSysConfig(JobUtil.HSY_ZL, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}


