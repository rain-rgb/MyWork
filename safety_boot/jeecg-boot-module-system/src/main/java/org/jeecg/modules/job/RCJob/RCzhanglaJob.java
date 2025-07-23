package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCzhanglaJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;
    @Autowired
    private ITrZhanglaSService zhanglaService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCZHANGLA);//杭绍甬张拉推送阳光工程
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓张拉定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓张拉的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrZhanglaXxb> zhanglaXxbs = zhanglaXxbService.selectzlList(shebeilist, curid);
        if (null == zhanglaXxbs || zhanglaXxbs.size() == 0) {
            log.info(String.format("暂无瑞仓张拉未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrZhanglaXxb zhanglaXxb : zhanglaXxbs) {
            id = zhanglaXxb.getId();
            JSONObject sendObject = JSONUtil.createObj();
            sendObject.set("syjid",zhanglaXxb.getSyjid());
            sendObject.set("gcmc",zhanglaXxb.getGcmc());
            sendObject.set("yzlc",zhanglaXxb.getYzlc());
            sendObject.set("gjbh",zhanglaXxb.getGjbh());
            sendObject.set("shebeibianhao",zhanglaXxb.getShebeibianhao());
            sendObject.set("gjmc",zhanglaXxb.getGjmc());
            sendObject.set("snsjqd",zhanglaXxb.getSnsjqd());
            sendObject.set("sgsj",zhanglaXxb.getSgsj());
            sendObject.set("snskqd",zhanglaXxb.getSnskqd());
            sendObject.set("zly1",zhanglaXxb.getZly1());
            sendObject.set("ybbh1",zhanglaXxb.getYbbh1());
            sendObject.set("bdrq1",zhanglaXxb.getBdrq1());
            sendObject.set("zly2",zhanglaXxb.getZly2());
            sendObject.set("ybbh2",zhanglaXxb.getYbbh2());
            sendObject.set("bdrq2",zhanglaXxb.getBdrq2());
            sendObject.set("cbunit",zhanglaXxb.getCbunit());
            sendObject.set("jlunit",zhanglaXxb.getJlunit());
            sendObject.set("htbh",zhanglaXxb.getHtbh());
            sendObject.set("zlcsu",zhanglaXxb.getZlcsu());
            sendObject.set("zlcsk",zhanglaXxb.getZlcsk());
            sendObject.set("zlcsep",zhanglaXxb.getZlcsep());
            sendObject.set("zlbwpic",zhanglaXxb.getZlbwpic());
            sendObject.set("kualiang",zhanglaXxb.getKualiang());
            sendObject.set("scljss",zhanglaXxb.getScljss());
            sendObject.set("fmqkms",zhanglaXxb.getFmqkms());
            sendObject.set("memo",zhanglaXxb.getMemo());
            sendObject.set("status",zhanglaXxb.getStatus());
            sendObject.set("uuid",zhanglaXxb.getUuid());
            sendObject.set("issend",zhanglaXxb.getIssend());
            sendObject.set("statistics",zhanglaXxb.getStatistics());
            sendObject.set("isruicang",zhanglaXxb.getIsruicang());
            List zlmList = new ArrayList();
            QueryWrapper<TrZhanglaM> zhanglaMQueryWrapper = new QueryWrapper<>();
            zhanglaMQueryWrapper.eq("syjid", zhanglaXxb.getSyjid());
            List<TrZhanglaM> zhanglaMList = zhanglaMService.list(zhanglaMQueryWrapper);
            for (TrZhanglaM trZhanglaM : zhanglaMList) {
                JSONObject zhanglaMObject = JSONUtil.createObj();
                zhanglaMObject.set("fguid",trZhanglaM.getFguid());
                zhanglaMObject.set("syjid",trZhanglaM.getSyjid());
                zhanglaMObject.set("shebeibianhao",trZhanglaM.getShebeibianhao());
                zhanglaMObject.set("zlsj",trZhanglaM.getZlsj());
                zhanglaMObject.set("gsbh",trZhanglaM.getGsbh());
                zhanglaMObject.set("holeid",trZhanglaM.getHoleid());
                zhanglaMObject.set("gsgs",trZhanglaM.getGsgs());
                zhanglaMObject.set("txml",trZhanglaM.getTxml());
                zhanglaMObject.set("sjzll",trZhanglaM.getSjzll());
                zhanglaMObject.set("htl",trZhanglaM.getHtl());
                zhanglaMObject.set("zscl",trZhanglaM.getZscl());
                zhanglaMObject.set("llscl",trZhanglaM.getLlscl());
                zhanglaMObject.set("yxpc",trZhanglaM.getYxpc());
                zhanglaMObject.set("sclper",trZhanglaM.getSclper());
                zhanglaMObject.set("wt",trZhanglaM.getWt());
                zhanglaMObject.set("clqk",trZhanglaM.getClqk());
                zhanglaMObject.set("memo",trZhanglaM.getMemo());
                zhanglaMObject.set("status",trZhanglaM.getStatus());
                zhanglaMObject.set("hege",trZhanglaM.getHege());
                zhanglaMObject.set("yzlb",trZhanglaM.getYzlb());
                zhanglaMObject.set("czlb",trZhanglaM.getCzlb());
                zhanglaMObject.set("zzlb",trZhanglaM.getZzlb());
                zhanglaMObject.set("jlsj",trZhanglaM.getJlsj());
                zhanglaMObject.set("uuid",trZhanglaM.getUuid());
                zhanglaMObject.set("issend",trZhanglaM.getIssend());

                List zlsList = new ArrayList();
                QueryWrapper<TrZhanglaS> zhanglaSQueryWrapper = new QueryWrapper<>();
                zhanglaSQueryWrapper.eq("fguid", trZhanglaM.getFguid());
                List<TrZhanglaS> zhanglaSList = zhanglaService.list(zhanglaSQueryWrapper);
                for (TrZhanglaS trZhanglaS : zhanglaSList) {
                    JSONObject zhanglaSObject = JSONUtil.createObj();
                    zhanglaSObject.set("sid",trZhanglaS.getSid());
                    zhanglaSObject.set("fguid",trZhanglaS.getFguid());
                    zhanglaSObject.set("syjid",trZhanglaS.getSyjid());
                    zhanglaSObject.set("gsbh",trZhanglaS.getGsbh());
                    zhanglaSObject.set("jdbfb",trZhanglaS.getJdbfb());
                    zhanglaSObject.set("ybds",trZhanglaS.getYbds());
                    zhanglaSObject.set("jdzll",trZhanglaS.getJdzll());
                    zhanglaSObject.set("jdscl",trZhanglaS.getJdscl());
                    zhanglaSObject.set("dh",trZhanglaS.getDh());
                    zhanglaSObject.set("ljhsl",trZhanglaS.getLjhsl());
                    zhanglaSObject.set("dcscl",trZhanglaS.getDcscl());
                    zhanglaSObject.set("status",trZhanglaS.getStatus());
                    zhanglaSObject.set("chsj",trZhanglaS.getChsj());
                    zlsList.add(zhanglaSObject);
                }
                zhanglaMObject.set("trZhanglaSList", zlsList);
                zlmList.add(zhanglaMObject);
            }
            sendObject.set("trZhanglaMList", zlmList);
//            System.out.println(sendObject);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/zlyj/zhanglaXxbs/tensionDataUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓张拉数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCZHANGLA, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else if(code == 402){
                log.info(String.format("瑞仓张拉数据推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCZHANGLA, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞仓张拉数据推送失败!" + id));
            }

        }
    }
}
