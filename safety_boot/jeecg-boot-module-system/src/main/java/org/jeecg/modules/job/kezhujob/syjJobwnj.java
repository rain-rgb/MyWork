package org.jeecg.modules.job.kezhujob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
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
import com.xkcoding.http.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @ClassName syjJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/11/1 9:57
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class syjJobwnj implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;
    @Autowired
    private ITSyjzbService itSyjzbService;
    @Autowired
    private IFYalijiService yalijiService;
    @Autowired
    private IFWangnjService wangnjService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    private static String url = "http://112.95.76.11:6543/iotws/lab/uploadTestSourceExt";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.KZ_SYJ);//柯诸张拉
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到柯诸试验机的配置信息" + DateUtils.now()));
            return;
        }

        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输柯诸试验机的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");

        //试验机z表
        List<FWangnj> yalijiLiwn = wangnjService.selectSyjwnList(shebeilist, curid);
        if (null == yalijiLiwn || yalijiLiwn.size() == 0) {
            log.info(String.format("暂无柯诸试验机未推送数据" + DateUtils.now()));
            return;
        } else {
            log.info(String.format("柯诸试验机推送开始" + DateUtils.now()));
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DecimalFormat df = new DecimalFormat("#.0000");
        for (FWangnj wangnji : yalijiLiwn) {
            LambdaQueryWrapper<TSyjzb> queryWrappersyj = new LambdaQueryWrapper<>();
            queryWrappersyj.eq(TSyjzb::getSyjid, wangnji.getSyjid());
            TSyjzb tSyjzb = itSyjzbService.getOne(queryWrappersyj);
            if (null == tSyjzb) {
                wangnji.setIssend(0);
                wangnjService.updateById(wangnji);
                continue;
            }
            //如果tSyjzb.getSbbh()不包含在shebeilist中
            String sbbh1 = tSyjzb.getSbbh();
            if (sbbh1==null||!shebeilist.contains(sbbh1)) {
                wangnji.setIssend(3);
                wangnjService.updateById(wangnji);
                continue;
            }
            id = tSyjzb.getId();
            String sylx = tSyjzb.getSylx();
            String sbbh = tSyjzb.getSbbh();
            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
            shebeiInfoQueryWrapper.eq("sbjno", sbbh);
            ShebeiInfo one = shebeiInfoService.getOne(shebeiInfoQueryWrapper);
            String getkhid = getkhid(one.getSysOrgCode());
            String getsbid = getsbid(sbbh);
            if (getkhid != null && getsbid != null) {

                String CustomerID = getkhid;
                String AppID = getsbid;

                JSONObject sendDate = new JSONObject();
                sendDate.set("CustomerID", CustomerID);
                sendDate.set("AppID", AppID);
                sendDate.set("TestDataKid", wangnji.getFGuid());
                String ItemName = "";
                JSONObject map = new JSONObject();
                map.set("Instar", tSyjzb.getLq());
                if (sylx.equals("100049")) {
                    ItemName = "钢筋机械连接拉伸";
                    map.set("D03", wangnji.getLz());
                    map.set("D04", wangnji.getLzqd());
                }
                if (sylx.equals("100048")) {
                    ItemName = "钢筋焊接拉伸";
                    map.set("D03", wangnji.getLz());
                    map.set("D04", wangnji.getLzqd());
                }
                if (sylx.equals("100047")) {
                    ItemName = "金属室温拉伸";
                    map.set("D01", wangnji.getQflz());
                    map.set("D02", wangnji.getQfqd());
                    map.set("D03", wangnji.getLz());
                    map.set("D04", wangnji.getLzqd());
                }
                sendDate.set("ItemName", ItemName);

                String wtbh = tSyjzb.getWtbh();
                if (StringUtil.isNotEmpty(wtbh)) {
                    if (wtbh.contains("-")) {
                        sendDate.set("GSNo", tSyjzb.getWtbh());
                    } else {
                        sendDate.set("GSNo", tSyjzb.getSjbh());
                    }
                } else {
                    sendDate.set("GSNo", tSyjzb.getSjbh());
                }
                sendDate.set("TestIdx", wangnji.getSjxh());
                String sysj = wangnji.getSysj();
                if (!StringUtil.isNotEmpty(sysj)) {
                    sysj = wangnji.getWcsj();
                }
                String substring = sysj.substring(0, sysj.length() - 4);
                sendDate.set("TestDate", substring);
                sendDate.set("ProjectName", tSyjzb.getSgbw());
//                    sendDate.set("FileName", "");
                sendDate.set("DLineTime", wangnji.getSjgc());
                sendDate.set("DLineValue", wangnji.getLzgc());
//                    sendDate.set("Ext", "");
//                    sendDate.set("File", "");
                sendDate.set("Data", map);

                String body = HttpRequest.post(url)
                        .form(sendDate)
                        .execute()
                        .body();

                pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), body);
                if(body.contains("操作成功") || body.contains("数据已覆盖")) {
                    wangnji.setIssend(1);
                    wangnjService.updateById(wangnji);
                }
            }
            tSyjzb.setIssend(tSyjzb.getIssend() + 1);
            itSyjzbService.saveOrUpdate(tSyjzb);
        }
    }

    private String getsbid(String sbbh) {

        QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
        sysDictQueryWrapper.eq("dict_code", "kzsbid");
        SysDict one = sysDictService.getOne(sysDictQueryWrapper);
        String id1 = one.getId();

        QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
        sysDictItemQueryWrapper.eq("dict_id", id1);
        sysDictItemQueryWrapper.eq("item_text", sbbh);
        SysDictItem one1 = sysDictItemService.getOne(sysDictItemQueryWrapper);
        return one1.getItemValue();
    }

    public String getkhid(String orgcode) {

        QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
        sysDictQueryWrapper.eq("dict_code", "kzkhid");
        SysDict one = sysDictService.getOne(sysDictQueryWrapper);
        String id1 = one.getId();

        QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
        sysDictItemQueryWrapper.eq("dict_id", id1);
        sysDictItemQueryWrapper.eq("item_text", orgcode);
        SysDictItem one1 = sysDictItemService.getOne(sysDictItemQueryWrapper);
        return one1.getItemValue();
    }

    public String user() {

        return null;
    }
}

