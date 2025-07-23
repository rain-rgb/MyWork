package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.chaoshengbo.entity.ChaoshengboSyjbsj;
import com.trtm.iot.chaoshengbo.service.IChaoshengboSyjbsjService;
import com.trtm.iot.hnthtconsign.entity.HnthtConsign;
import com.trtm.iot.hnthtconsign.service.IHnthtConsignService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ZJJob implements Job {
    @Autowired
    private IHnthtConsignService hnthtConsignService;

    @Autowired
    private IChaoshengboSyjbsjService chaoshengboSyjbsjService;
    @Autowired
    private ISysConfigService sysConfigService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_ZJTS);//
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓桩基检测任务单定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓桩基检测任务单的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
//        String[] split = shebeilist.split(",");
//        List<String> strsToList1 = Arrays.asList(split);
        List<HnthtConsign> selectlist = hnthtConsignService.selectPushList(shebeilist, 0);
        if (null == selectlist || selectlist.size() == 0) {
            log.info(String.format("暂无瑞仓桩基检测任务单未推送数据" + DateUtils.now()));
            return;
        }

        for (HnthtConsign hnthtConsign : selectlist) {
            hnthtConsignService.updatePushStatus(hnthtConsign);
            JSONObject sendObject = JSONUtil.createObj();
            sendObject.set("uuid", hnthtConsign.getUuid());
            sendObject.set("projectname", hnthtConsign.getProjectname());
            sendObject.set("component", hnthtConsign.getComponent());
            sendObject.set("sgbwguid", hnthtConsign.getSgbwguid());
            sendObject.set("sgbwname", hnthtConsign.getSgbwname());
            sendObject.set("status", hnthtConsign.getStatus());
            sendObject.set("zldate", hnthtConsign.getZldate());
            sendObject.set("departid", hnthtConsign.getDepartid());
            sendObject.set("sytypeid", hnthtConsign.getSytypeid());
            sendObject.set("departname", hnthtConsign.getDepartname());
            sendObject.set("createBy", hnthtConsign.getCreateBy());
            sendObject.set("createTime", hnthtConsign.getCreateTime());
            sendObject.set("updateTime", hnthtConsign.getUpdateTime());
            sendObject.set("sysOrgCode", hnthtConsign.getSysOrgCode());
            sendObject.set("issend", hnthtConsign.getIssend());
            sendObject.set("shebeichangjia", hnthtConsign.getShebeichangjia());
            sendObject.set("shebeibianhao", hnthtConsign.getShebeibianhao());
            sendObject.set("isdel", hnthtConsign.getIsdel());
            sendObject.set("pushStatus",hnthtConsign.getPushStatus());
//            String url = "http://localhost:8333/jeecg-boot/hnthtconsign/hnthtConsign/addOpen";
            String url = "https://zgj20.cncico.com/wlwpt/hnthtconsign/hnthtConsign/addOpen";
            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            System.out.println(body);
            JSONObject jsonObject1 = new JSONObject(body);
            Integer codes = (Integer) jsonObject1.get("code");
            if (codes == 200) {
                log.info(String.format("瑞仓检测任务单数据推送成功!" + "Json数据" + sendObject));
            } else {
                log.info(String.format("瑞仓检测任务单数据推送失败!" + "Json数据" + sendObject));
            }
            List<ChaoshengboSyjbsj> chaoshengboSyjbsjs = chaoshengboSyjbsjService.selectList(hnthtConsign.getUuid());
            for(ChaoshengboSyjbsj chaoshengboSyjbsj : chaoshengboSyjbsjs){
                JSONObject sendObject2 = JSONUtil.createObj();
                sendObject2.set("shebeino",chaoshengboSyjbsj.getShebeino());
                sendObject2.set("syff",chaoshengboSyjbsj.getSyff());
                sendObject2.set("shizhuangleixing",chaoshengboSyjbsj.getShizhuangleixing());
                sendObject2.set("ceshiyino",chaoshengboSyjbsj.getCeshiyino());
                sendObject2.set("liushuihao",chaoshengboSyjbsj.getLiushuihao());
                sendObject2.set("shizhuangno",chaoshengboSyjbsj.getShizhuangno());
                sendObject2.set("poumianlist",chaoshengboSyjbsj.getPoumianlist());
                sendObject2.set("chuanshuleirong",chaoshengboSyjbsj.getChuanshuleirong());
                sendObject2.set("baoxuhao",chaoshengboSyjbsj.getBaoxuhao());
                sendObject2.set("dangqiantime",chaoshengboSyjbsj.getDangqiantime());
                sendObject2.set("ceshitime",chaoshengboSyjbsj.getCeshitime());
                sendObject2.set("jiaozhutime",chaoshengboSyjbsj.getJiaozhutime());
                sendObject2.set("wenjianming",chaoshengboSyjbsj.getWenjianming());
                sendObject2.set("zhuangjing",chaoshengboSyjbsj.getZhuangjing());
                sendObject2.set("zhuangchang",chaoshengboSyjbsj.getZhuangchang());
                sendObject2.set("yiju",chaoshengboSyjbsj.getYiju());
                sendObject2.set("guanshu",chaoshengboSyjbsj.getGuanshu());
                sendObject2.set("poumianshu",chaoshengboSyjbsj.getPoumianshu());
                sendObject2.set("caiyangpinlv",chaoshengboSyjbsj.getCaiyangpinlv());
                sendObject2.set("caiyanglength",chaoshengboSyjbsj.getCaiyanglength());
                sendObject2.set("ADzhuanhuan",chaoshengboSyjbsj.getAdzhuanhuan());
                sendObject2.set("boxingfuzhi",chaoshengboSyjbsj.getBoxingfuzhi());
                sendObject2.set("boxingjixian",chaoshengboSyjbsj.getBoxingjixian());
                sendObject2.set("jingdu",chaoshengboSyjbsj.getJingdu());
                sendObject2.set("weidu",chaoshengboSyjbsj.getWeidu());
                sendObject2.set("shoubozengqiang",chaoshengboSyjbsj.getShoubozengqiang());
                sendObject2.set("ruanjianlvbo",chaoshengboSyjbsj.getRuanjianlvbo());
                sendObject2.set("shengceguan",chaoshengboSyjbsj.getShengceguan());
                sendObject2.set("shengceguanwaijing",chaoshengboSyjbsj.getShengceguanwaijing());
                sendObject2.set("shengceguanneijing",chaoshengboSyjbsj.getShengceguanneijing());
                sendObject2.set("tantouwaijing",chaoshengboSyjbsj.getTantouwaijing());
                sendObject2.set("fangweijiao",chaoshengboSyjbsj.getFangweijiao());
                sendObject2.set("lunjing",chaoshengboSyjbsj.getLunjing());
                sendObject2.set("xianjing",chaoshengboSyjbsj.getXianjing());
                sendObject2.set("poumian1",chaoshengboSyjbsj.getPoumian1());
                sendObject2.set("poumian2",chaoshengboSyjbsj.getPoumian2());
                sendObject2.set("poumian3",chaoshengboSyjbsj.getPoumian3());
                sendObject2.set("poumian4",chaoshengboSyjbsj.getPoumian4());
                sendObject2.set("poumian5",chaoshengboSyjbsj.getPoumian5());
                sendObject2.set("poumian6",chaoshengboSyjbsj.getPoumian6());
                sendObject2.set("poumian7",chaoshengboSyjbsj.getPoumian7());
                sendObject2.set("poumian8",chaoshengboSyjbsj.getPoumian8());
                sendObject2.set("poumian9",chaoshengboSyjbsj.getPoumian9());
                sendObject2.set("poumian10",chaoshengboSyjbsj.getPoumian10());
                sendObject2.set("ceshiguifan",chaoshengboSyjbsj.getCeshiguifan());
                sendObject2.set("hege",chaoshengboSyjbsj.getHege());
                sendObject2.set("chuzhiid",chaoshengboSyjbsj.getChuzhiid());
                sendObject2.set("shangchuantime",chaoshengboSyjbsj.getShangchuantime());
                sendObject2.set("projectName",chaoshengboSyjbsj.getProjectName());
                sendObject2.set("sgbwName",chaoshengboSyjbsj.getSgbw());
                sendObject2.set("Code",chaoshengboSyjbsj.getCode());
//                String url2 = "http://localhost:8333/jeecg-boot/chaoshengbo/chaoshengboSyjbsj/addOpen";
                String url2 = "https://zgj20.cncico.com/wlwpt/chaoshengbo/chaoshengboSyjbsj/addOpen";
                String body2 = HttpRequest.post(url2)
                        .header("Content-Type", "application/json")
                        .body(String.valueOf(sendObject2))
                        .execute()
                        .body();
                System.out.println(body2);
                JSONObject jsonObject2 = new JSONObject(body2);
                Integer codes2 = (Integer) jsonObject2.get("code");
                if (codes2 == 200) {
                    log.info(String.format("瑞仓桩基数据推送成功!" + "Json数据" + sendObject2));
                } else {
                    log.info(String.format("瑞仓桩基数据推送失败!" + "Json数据" + sendObject2));
                }
            }

        }


    }
}
