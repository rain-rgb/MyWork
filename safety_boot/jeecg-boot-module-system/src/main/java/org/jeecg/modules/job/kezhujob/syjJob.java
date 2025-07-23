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
public class syjJob implements Job {
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

        List<FsYaliji> yalijiLisy = yalijiService.selectSyjylList(shebeilist, curid);
        if (null == yalijiLisy || yalijiLisy.size() == 0) {
            log.info(String.format("暂无柯诸试验机未推送数据" + DateUtils.now()));
            return;
        } else {
            log.info(String.format("柯诸试验机推送开始" + DateUtils.now()));
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DecimalFormat df = new DecimalFormat("#.0000");
        for (FsYaliji yaliji : yalijiLisy) {
            LambdaQueryWrapper<TSyjzb> queryWrappersyj = new LambdaQueryWrapper<>();
            queryWrappersyj.eq(TSyjzb::getSyjid, yaliji.getSyjid());

            TSyjzb tSyjzb = itSyjzbService.getOne(queryWrappersyj);
            if (null == tSyjzb) {
                yaliji.setIssend(0);
                yalijiService.updateById(yaliji);
                continue;
            }
            //如果tSyjzb.getSbbh()不包含在shebeilist中
            String sbbh1 = tSyjzb.getSbbh();
            if (sbbh1==null||!shebeilist.contains(sbbh1)) {
                yaliji.setIssend(3);
                yalijiService.updateById(yaliji);
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

                if (sylx.equals("100014")) {


                    JSONObject sendDate = new JSONObject();
                    sendDate.set("CustomerID", CustomerID);
                    sendDate.set("AppID", AppID);
                    sendDate.set("TestDataKid", yaliji.getFGuid());
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
                    sendDate.set("TestIdx", yaliji.getSjxh());
                    String sysj = yaliji.getSysj();
                    String wcsj = yaliji.getWcsj();
                    String substring = "";
                    if (StringUtil.isNotEmpty(sysj)) {
                        substring = sysj.substring(0, sysj.length() - 4);
                    } else {
                        substring = wcsj.substring(0, wcsj.length() - 4);
                    }
                    sendDate.set("TestDate", substring);
                    sendDate.set("ProjectName", tSyjzb.getSgbw());
//                    sendDate.set("FileName", "");
                    sendDate.set("DLineTime", yaliji.getSjgc());
                    sendDate.set("DLineValue", yaliji.getYskylz());
//                    sendDate.set("Ext", "");
//                    sendDate.set("File", "");
                    String ItemName = "非标准龄期混凝土抗压";
                    JSONObject map = new JSONObject();
                    Integer lq = tSyjzb.getLq();
                    if (lq == 28 || lq == 7) {
                        ItemName = "普通混凝土抗压";
                    }
                    map.set("Instar", tSyjzb.getLq());
                    map.set("D02", yaliji.getKylz());
                    map.set("D03", yaliji.getKyqd());

                    sendDate.set("Data", map);
                    sendDate.set("ItemName", ItemName);
                    System.out.println(sendDate);
                    String body = HttpRequest.post(url)
                            .form(sendDate)
                            .execute()
                            .body();

                    pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), body);
                    yaliji.setIssend(1);
                    yalijiService.updateById(yaliji);
                    tSyjzb.setIssend(tSyjzb.getIssend() + 1);
                    itSyjzbService.saveOrUpdate(tSyjzb);
                } else if (sylx.equals("100135") || sylx.equals("100136") || sylx.equals("100134") || sylx.equals("100138")
                        || sylx.equals("100131") || sylx.equals("100133") || sylx.equals("100793") || sylx.equals("100794")) {


                    JSONObject sendDate = new JSONObject();
                    sendDate.set("CustomerID", CustomerID);
                    sendDate.set("AppID", AppID);
                    sendDate.set("TestDataKid", yaliji.getFGuid());
                    String ItemName = "";

                    String kylz = yaliji.getKylz();
                    double D02 = 0.0;
                    if (StringUtil.isNotEmpty(kylz)) {
                        D02 = Double.parseDouble(kylz);
                    }
                    if (sylx.equals("100135")) {
                        ItemName = "水泥胶砂抗压";
                    }
                    if (sylx.equals("100136")) {
                        String sysOrgCode = one.getSysOrgCode();
                        if (sysOrgCode.contains("A05A01A04A10A02A01")) {
                            D02 = D02 / 1000;
                        }
                        ItemName = "水泥胶砂抗折";
                    }
                    if (sylx.equals("100134") || sylx.equals("100793")) {
                        ItemName = "水泥净浆抗压";
                    }
                    if (sylx.equals("100138") || sylx.equals("100794")) {
                        ItemName = "水泥净浆抗折";
                    }
                    if (sylx.equals("100131")) {
                        ItemName = "水泥砂浆抗压";
                    }
                    if (sylx.equals("100133")) {
                        ItemName = "建筑砂浆抗压";
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
                    sendDate.set("TestIdx", yaliji.getSjxh());
                    String sysj = yaliji.getSysj();
                    String substring = "";
                    if (StringUtil.isNotEmpty(sysj)) {
                        substring = sysj.substring(0, sysj.length() - 4);
                    } else {
                        substring = yaliji.getWcsj().substring(0, yaliji.getWcsj().length() - 4);
                    }
                    sendDate.set("TestDate", substring);
                    sendDate.set("ProjectName", tSyjzb.getSgbw());
//                    sendDate.set("FileName", "");
                    sendDate.set("DLineTime", yaliji.getSjgc());
                    sendDate.set("DLineValue", yaliji.getYskylz());
//                    sendDate.set("Ext", "");
//                    sendDate.set("File", "");

                    JSONObject map = new JSONObject();
                    map.set("Instar", tSyjzb.getLq());

                    map.set("D02", df.format(D02));
                    map.set("D03", yaliji.getKyqd());

                    sendDate.set("Data", map.toString());

                    String body = HttpRequest.post(url)
                            .form(sendDate)
                            .execute()
                            .body();

                    pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), body);
                    if(body.contains("操作成功") || body.contains("数据已覆盖")) {
                        yaliji.setIssend(1);
                        yalijiService.updateById(yaliji);
                        tSyjzb.setIssend(tSyjzb.getIssend() + 1);
                        itSyjzbService.saveOrUpdate(tSyjzb);
                    }
                }
            }
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

