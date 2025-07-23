package org.jeecg.modules.job.zlpzJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.syj.entity.FWangnj;
import com.trtm.iot.syj.entity.FYaliji;
import com.trtm.iot.syj.entity.FsYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.IFWangnjService;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.syjoverhandler.entity.SyjOverHandler;
import com.trtm.iot.syjoverhandler.service.ISyjOverHandlerService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.zlpz.entity.Zlpz;
import com.trtm.iot.zlpz.service.IZlpzService;
import com.xkcoding.http.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName syjJob：
 * @Description 浙路品质试验机推送
 * @Author 55314
 * @Date 2022/12/9 10:35
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class syjJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService syjzbService;
    @Autowired
    private IZlpzService zlpzService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;
    @Autowired
    private ISyjOverHandlerService overHandlerService;
    @Autowired
    private IFWangnjService wangnjService;
    @Autowired
    private IFYalijiService yalijiService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZLPZ_SYJ);//浙路品质试验机推送
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
            log.info(String.format("没有配置要传输试验机的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TSyjzb> tSyjzbs = syjzbService.selectSyjListzlpz(shebeilist);
        if (null == tSyjzbs || tSyjzbs.size() == 0) {
            log.info(String.format("暂无试验机未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (TSyjzb syjzb : tSyjzbs) {
            id = syjzb.getId();
            List sendList = new ArrayList();
            JSONObject sendDate = new JSONObject();
            QueryWrapper<Zlpz> queryWrapperZlpz = new QueryWrapper<>();
            queryWrapperZlpz.eq("shebeino", syjzb.getSbbh());
            Zlpz one = zlpzService.getOne(queryWrapperZlpz);
            String shebeiid = one.getShebeiid();
            String project = one.getProject();

            sendDate.set("id", project + "_" + id);
            sendDate.set("equipId", shebeiid);

            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
            shebeiInfoQueryWrapper.eq("sbjno",syjzb.getSbbh());
            ShebeiInfo one1 = shebeiInfoService.getOne(shebeiInfoQueryWrapper);
            Integer sbtype = one1.getSbtype();

            String code = syjzb.getSjbh() != null ? syjzb.getSjbh() : syjzb.getWtbh();
            sendDate.set("code", code);

            QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
            sysDictQueryWrapper.eq("dict_code", "SYLX");
            SysDict one2 = sysDictService.getOne(sysDictQueryWrapper);
            String id1 = one2.getId();

            QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
            sysDictItemQueryWrapper.eq("dict_id", id1);
            sysDictItemQueryWrapper.eq("item_value", syjzb.getSylx());
            SysDictItem one3 = sysDictItemService.getOne(sysDictItemQueryWrapper);

            sendDate.set("testname",one3.getItemText());
            sendDate.set("betlev",syjzb.getSjqd());
            String qddbz = syjzb.getQddbz();
            if (!StringUtil.isNotEmpty(qddbz)){
                qddbz = "0";
            }
            sendDate.set("strength",qddbz);
            sendDate.set("samplename","");
            sendDate.set("samplenum",syjzb.getSjsl());
            sendDate.set("qrcode","");
            sendDate.set("testdate",syjzb.getSyrq());
            sendDate.set("operator",syjzb.getCzry());
            sendDate.set("result",0);

            QueryWrapper<SyjOverHandler> overHandlerQueryWrapper = new QueryWrapper<>();
            overHandlerQueryWrapper.eq("baseid",syjzb.getSyjid());
            SyjOverHandler one4 = overHandlerService.getOne(overHandlerQueryWrapper);

            int alarmStatus = 0;
            if (one4 != null){
                alarmStatus = 1;
                sendDate.set("opinion", one4.getHandleWay());
                sendDate.set("closeTime", sdf.format(one4.getHandleTime()));
                sendDate.set("closer", one4.getHandlePerson());
                sendDate.set("attachment", one4.getFilePath2());
            }
            sendDate.set("alarmStatus", alarmStatus);

            List detailList = new ArrayList();
            JSONObject sendJsonObject = new JSONObject();

            if (3==sbtype){
                sendJsonObject.set("serviceName","ZLPZ_ZX_WNSYJ");

                sendDate.set("conspos",syjzb.getCjmc());
                sendDate.set("samplebatch","");
                sendDate.set("betlev",syjzb.getPzbm());

                QueryWrapper<FWangnj> wangnjQueryWrapper = new QueryWrapper<>();
                wangnjQueryWrapper.eq("syjid",syjzb.getSyjid());
                List<FWangnj> list = wangnjService.list(wangnjQueryWrapper);
                for (FWangnj wangnj : list) {
                    JSONObject sonObject = new JSONObject();
                    sonObject.set("code", code);
                    sonObject.set("sjxh", wangnj.getSjxh());
                    sonObject.set("wsbz", wangnj.getWsbz());
                    sonObject.set("dhbz", wangnj.getDhbz());
                    sonObject.set("lz", wangnj.getLz());
                    sonObject.set("lzqd", wangnj.getLzqd());
                    sonObject.set("qflz", wangnj.getQflz());
                    sonObject.set("qfqd", wangnj.getQfqd());
                    sonObject.set("scl", wangnj.getScl());
                    sonObject.set("zdlzscl", wangnj.getZdlzscl());
                    sonObject.set("sysj", wangnj.getSysj());
                    sonObject.set("wcsj", wangnj.getWcsj());
                    sonObject.set("alarmRule", "");

                    detailList.add(sonObject);
                }
            }
            if (4==sbtype){
                sendJsonObject.set("serviceName","ZLPZ_ZX_YLJ");

                sendDate.set("age",syjzb.getLq());
                sendDate.set("part", StringUtil.isNotEmpty(syjzb.getCjmc()) ? syjzb.getCjmc() : "/");

                QueryWrapper<FsYaliji> yalijiQueryWrapper = new QueryWrapper<>();
                yalijiQueryWrapper.eq("syjid",syjzb.getSyjid());
                List<FsYaliji> list = yalijiService.list(yalijiQueryWrapper);
                for (FsYaliji yaliji : list) {
                    JSONObject sonObject = new JSONObject();
                    sonObject.set("code", code);
                    sonObject.set("sjxh", yaliji.getSjxh());
                    sonObject.set("kylz", yaliji.getKylz());
                    sonObject.set("kyqd", yaliji.getKyqd());
                    sonObject.set("sysj", yaliji.getSysj());
                    sonObject.set("wcsj", yaliji.getWcsj());
                    sonObject.set("alarmRule", "");

                    detailList.add(sonObject);
                }

            }
            if (12==sbtype){
                sendJsonObject.set("serviceName","ZLPZ_ZX_KYKZSYJ");

                sendDate.set("age",syjzb.getLq());
                sendDate.set("part", StringUtil.isNotEmpty(syjzb.getCjmc()) ? syjzb.getCjmc() : "/");

                QueryWrapper<FsYaliji> yalijiQueryWrapper = new QueryWrapper<>();
                yalijiQueryWrapper.eq("syjid",syjzb.getSyjid());
                List<FsYaliji> list = yalijiService.list(yalijiQueryWrapper);
                for (FsYaliji yaliji : list) {
                    JSONObject sonObject = new JSONObject();
                    sonObject.set("code", code);
                    sonObject.set("sjxh", yaliji.getSjxh());
                    sonObject.set("kylz", yaliji.getKylz());
                    sonObject.set("kyqd", yaliji.getKyqd());
                    sonObject.set("sysj", yaliji.getSysj());
                    sonObject.set("wcsj", yaliji.getWcsj());
                    sonObject.set("alarmRule", "");

                    detailList.add(sonObject);
                }

            }

            sendDate.set("detailList", detailList);
            sendList.add(sendDate);

            sendJsonObject.set("token","93cd2c6567594107a16b51a65bcd5a37");
            sendJsonObject.set("updateNull","true");
            sendJsonObject.set("param",sendList);
            System.out.println(sendJsonObject);
            String url = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/push";
            String back = HttpRequest.post(url)
                    .body(sendJsonObject.toString())
                    .timeout(20000)
                    .execute().body();

            com.alibaba.fastjson.JSONObject backJson = JSON.parseObject(back);
            int result = backJson.getIntValue("result");
            if (result == 0) {
                syjzb.setIszlpz(alarmStatus == 0 ? 3 : 1);
                log.info(String.format("试验机浙路品质推送成功!" + id));
            } else {
                syjzb.setIszlpz(2);
                log.info(String.format("试验机浙路品质推送失败!" + id));
            }
            syjzbService.updateById(syjzb);
            pushandreturnService.saveData(id,String.valueOf(sendDate),selectsysconfigone.getRemark(),back);
        }
    }
}
