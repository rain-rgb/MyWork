package org.jeecg.modules.job.yggcJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.chaoshengbo.entity.*;
import com.trtm.iot.chaoshengbo.service.*;
import com.trtm.iot.hnthtconsign.entity.HnthtConsign;
import com.trtm.iot.hnthtconsign.service.IHnthtConsignService;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName chaoshengboJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/9/5 13:44
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class chaoshengboJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IChaoshengboSybltsjService chaoshengboSybltsjService;
    @Autowired
    private IChaoshengboSybsjService chaoshengboSybsjService;
    @Autowired
    private IChaoshengboSydsjService chaoshengboSydsjService;
    @Autowired
    private IChaoshengboSyjbsjService chaoshengboSyjbsjService;
    @Autowired
    private IChaoshengboSyjsbService chaoshengboSyjsbService;
    @Autowired
    private IChaoshengboSyljzsService chaoshengboSyljzsService;
    @Autowired
    private IChaoshengboSyqxsjService chaoshengboSyqxsjService;
    @Autowired
    private IHnthtConsignService hnthtConsignService;
    @Autowired
    private ISysDepartService sysDepartService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YGGC_CSB);//杭绍甬试验机推送阳光工程
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到超声波的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输超声波的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<ChaoshengboSyjbsj> chaoshengboSyjbsjs = chaoshengboSyjbsjService.selectLists(shebeilist, curid);
        if (null == chaoshengboSyjbsjs || chaoshengboSyjbsjs.size() == 0) {
            log.info(String.format("暂无超声波未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ChaoshengboSyjbsj chaoshengboSyjbsj : chaoshengboSyjbsjs) {
            id = chaoshengboSyjbsj.getId();
            JSONObject sendJson = JSONUtil.createObj();
            sendJson.set("adzhuanhuan", chaoshengboSyjbsj.getAdzhuanhuan());
            sendJson.set("baoxuhao", chaoshengboSyjbsj.getBaoxuhao());
            sendJson.set("boxingfuzhi", chaoshengboSyjbsj.getBoxingfuzhi());
            sendJson.set("boxingjixian", chaoshengboSyjbsj.getBoxingjixian());
            sendJson.set("caiyanglength", chaoshengboSyjbsj.getCaiyanglength());
            sendJson.set("caiyangpinlv", chaoshengboSyjbsj.getCaiyangpinlv());
            sendJson.set("ceshiguifan", chaoshengboSyjbsj.getCeshiguifan());
            sendJson.set("ceshitime", sdf.format(chaoshengboSyjbsj.getCeshitime()));
            sendJson.set("ceshiyino", chaoshengboSyjbsj.getCeshiyino());
            sendJson.set("chuanshuleirong", chaoshengboSyjbsj.getChuanshuleirong());
            sendJson.set("chuzhiid", chaoshengboSyjbsj.getChuzhiid());
            sendJson.set("dangqiantime", sdf.format(chaoshengboSyjbsj.getDangqiantime()));
            sendJson.set("fangweijiao", chaoshengboSyjbsj.getFangweijiao());
            sendJson.set("guanshu", chaoshengboSyjbsj.getGuanshu());
            sendJson.set("hege", chaoshengboSyjbsj.getHege());
            sendJson.set("id", chaoshengboSyjbsj.getId());
            sendJson.set("isbzb", "");
            sendJson.set("jiaozhutime", sdf.format(chaoshengboSyjbsj.getJiaozhutime()));
            sendJson.set("jingdu", chaoshengboSyjbsj.getJingdu());
            sendJson.set("lunjing", chaoshengboSyjbsj.getLunjing());
            String liushuihao = chaoshengboSyjbsj.getLiushuihao();
            QueryWrapper<HnthtConsign> hnthtConsignQueryWrapper = new QueryWrapper<>();
            hnthtConsignQueryWrapper.eq("uuid", liushuihao);
            HnthtConsign one = hnthtConsignService.getOne(hnthtConsignQueryWrapper);
            String sysOrgCode = one.getSysOrgCode();
            QueryWrapper<SysDepart> sysDepartQueryWrapper = new QueryWrapper<>();
            sysDepartQueryWrapper.eq("org_code", sysOrgCode);
            SysDepart one1 = sysDepartService.getOne(sysDepartQueryWrapper);

            String departName = one1.getDepartName();
            if (departName.contains("监")) {
                sendJson.set("orgType", "1");
            } else if (departName.contains("交竣工")) {
                sendJson.set("orgType", "2");
            } else {
                sendJson.set("orgType", "0");
            }
            sendJson.set("engineeringName", one.getProjectname());
            sendJson.set("sectionId", one1.getOrgCode());
            sendJson.set("sectionName", departName);
            sendJson.set("liushuihao", liushuihao);
            sendJson.set("poumian1", chaoshengboSyjbsj.getPoumian1());
            sendJson.set("poumian10", chaoshengboSyjbsj.getPoumian10());
            sendJson.set("poumian2", chaoshengboSyjbsj.getPoumian2());
            sendJson.set("poumian3", chaoshengboSyjbsj.getPoumian3());
            sendJson.set("poumian4", chaoshengboSyjbsj.getPoumian4());
            sendJson.set("poumian5", chaoshengboSyjbsj.getPoumian5());
            sendJson.set("poumian6", chaoshengboSyjbsj.getPoumian6());
            sendJson.set("poumian7", chaoshengboSyjbsj.getPoumian7());
            sendJson.set("poumian8", chaoshengboSyjbsj.getPoumian8());
            sendJson.set("poumian9", chaoshengboSyjbsj.getPoumian9());
            sendJson.set("poumianlist", chaoshengboSyjbsj.getPoumianlist());
            sendJson.set("poumianshu", chaoshengboSyjbsj.getPoumianshu());
            sendJson.set("projectName", chaoshengboSyjbsj.getProjectName());
            sendJson.set("ruanjianlvbo", chaoshengboSyjbsj.getRuanjianlvbo());
            sendJson.set("rwdno", chaoshengboSyjbsj.getLiushuihao());
            sendJson.set("shangchuantime", chaoshengboSyjbsj.getShangchuantime());
            sendJson.set("shebeino", chaoshengboSyjbsj.getShebeino());
            sendJson.set("shengceguan", chaoshengboSyjbsj.getShengceguan());
            sendJson.set("shengceguanneijing", chaoshengboSyjbsj.getShengceguanneijing());
            sendJson.set("shengceguanwaijing", chaoshengboSyjbsj.getShengceguanwaijing());
            sendJson.set("shizhuangleixing", chaoshengboSyjbsj.getShizhuangleixing());
            sendJson.set("shizhuangno", chaoshengboSyjbsj.getShizhuangno());
            sendJson.set("shoubozengqiang", chaoshengboSyjbsj.getShoubozengqiang());
            sendJson.set("syff", chaoshengboSyjbsj.getSyff());
            sendJson.set("tantouwaijing", chaoshengboSyjbsj.getTantouwaijing());
            sendJson.set("weidu", chaoshengboSyjbsj.getWeidu());
            sendJson.set("wenjianming", chaoshengboSyjbsj.getWenjianming());
            sendJson.set("xianjing", chaoshengboSyjbsj.getXianjing());
            sendJson.set("yiju", chaoshengboSyjbsj.getYiju());
            sendJson.set("zhuangchang", chaoshengboSyjbsj.getZhuangchang());
            sendJson.set("zhuangjing", chaoshengboSyjbsj.getZhuangjing());

            QueryWrapper<ChaoshengboSybltsj> chaoshengboSybltsjQueryWrapper = new QueryWrapper<>();
            chaoshengboSybltsjQueryWrapper.eq("liushuihao", liushuihao);
            List<ChaoshengboSybltsj> chaoshengboSybltsjs = chaoshengboSybltsjService.list(chaoshengboSybltsjQueryWrapper);
            List chaoshengboSybltsjList = new ArrayList();
            for (ChaoshengboSybltsj chaoshengboSybltsj : chaoshengboSybltsjs) {
                JSONObject sendchaoshengboSybltsj = JSONUtil.createObj();
                sendchaoshengboSybltsj.set("baoxuhao", chaoshengboSybltsj.getBaoxuhao());
                sendchaoshengboSybltsj.set("bolietu", chaoshengboSybltsj.getBolietu());
                sendchaoshengboSybltsj.set("ceshiyino", chaoshengboSybltsj.getCeshiyino());
                sendchaoshengboSybltsj.set("chaungshuleirong", chaoshengboSybltsj.getChaungshuleirong());
                sendchaoshengboSybltsj.set("id", chaoshengboSybltsj.getId());
//                sendchaoshengboSybltsj.set("imagedata", chaoshengboSybltsj.getImagedata());
                sendchaoshengboSybltsj.set("liushuihao", chaoshengboSybltsj.getLiushuihao());
                sendchaoshengboSybltsj.set("poumianhao", chaoshengboSybltsj.getPoumianhao());
                sendchaoshengboSybltsj.set("shebeino", chaoshengboSybltsj.getShebeino());
                sendchaoshengboSybltsj.set("shizhuangno", chaoshengboSybltsj.getShizhuangno());
                chaoshengboSybltsjList.add(sendchaoshengboSybltsj);
            }
            sendJson.set("csbSybltsjPOS", chaoshengboSybltsjList);

            QueryWrapper<ChaoshengboSybsj> chaoshengboSybsjQueryWrapper = new QueryWrapper<>();
            chaoshengboSybsjQueryWrapper.eq("liushuihao", liushuihao);
            List<ChaoshengboSybsj> chaoshengboSybsjs = chaoshengboSybsjService.list(chaoshengboSybsjQueryWrapper);
            List chaoshengboSybsjList = new ArrayList();
            for (ChaoshengboSybsj chaoshengboSybsj : chaoshengboSybsjs) {
                JSONObject sendchaoshengboSybltsj = JSONUtil.createObj();
                sendchaoshengboSybltsj.set("baoxuhao", chaoshengboSybsj.getBaoxuhao());
                sendchaoshengboSybltsj.set("ceshiyino", chaoshengboSybsj.getCeshiyino());
                sendchaoshengboSybltsj.set("chaungshuleirong", chaoshengboSybsj.getChaungshuleirong());
                sendchaoshengboSybltsj.set("id", chaoshengboSybsj.getId());
                sendchaoshengboSybltsj.set("liushuihao", chaoshengboSybsj.getLiushuihao());
                sendchaoshengboSybltsj.set("poumianhao1", chaoshengboSybsj.getPoumianhao1());
                sendchaoshengboSybltsj.set("poumianhao10", chaoshengboSybsj.getPoumianhao10());
                sendchaoshengboSybltsj.set("poumianhao2", chaoshengboSybsj.getPoumianhao2());
                sendchaoshengboSybltsj.set("poumianhao3", chaoshengboSybsj.getPoumianhao3());
                sendchaoshengboSybltsj.set("poumianhao4", chaoshengboSybsj.getPoumianhao4());
                sendchaoshengboSybltsj.set("poumianhao5", chaoshengboSybsj.getPoumianhao5());
                sendchaoshengboSybltsj.set("poumianhao6", chaoshengboSybsj.getPoumianhao6());
                sendchaoshengboSybltsj.set("poumianhao7", chaoshengboSybsj.getPoumianhao7());
                sendchaoshengboSybltsj.set("poumianhao8", chaoshengboSybsj.getPoumianhao8());
                sendchaoshengboSybltsj.set("poumianhao9", chaoshengboSybsj.getPoumianhao9());
                sendchaoshengboSybltsj.set("poumianlist", chaoshengboSybsj.getPoumianlist());
                sendchaoshengboSybltsj.set("shebeino", chaoshengboSybsj.getShebeino());
                sendchaoshengboSybltsj.set("shendu", chaoshengboSybsj.getShendu());
                sendchaoshengboSybltsj.set("shizhuangno", chaoshengboSybsj.getShizhuangno());
                chaoshengboSybsjList.add(sendchaoshengboSybltsj);
            }
            sendJson.set("csbSybsjPOS", chaoshengboSybsjList);

            QueryWrapper<ChaoshengboSydsj> chaoshengboSydsjQueryWrapper = new QueryWrapper<>();
            chaoshengboSydsjQueryWrapper.eq("liushuihao", liushuihao);
            List<ChaoshengboSydsj> chaoshengboSydsjs = chaoshengboSydsjService.list(chaoshengboSydsjQueryWrapper);
            List chaoshengboSydsjList = new ArrayList();
            for (ChaoshengboSydsj chaoshengboSydsj : chaoshengboSydsjs) {
                JSONObject sendchaoshengboSydsj = JSONUtil.createObj();
                sendchaoshengboSydsj.set("baoxuhao", chaoshengboSydsj.getBaoxuhao());
                sendchaoshengboSydsj.set("ceshiyino", chaoshengboSydsj.getCeshiyino());
                sendchaoshengboSydsj.set("chaungshuleirong", chaoshengboSydsj.getChaungshuleirong());
                sendchaoshengboSydsj.set("daxuhao", chaoshengboSydsj.getDaxuhao());
                sendchaoshengboSydsj.set("id", chaoshengboSydsj.getId());
                sendchaoshengboSydsj.set("jieshoulength", chaoshengboSydsj.getJieshoulength());
                sendchaoshengboSydsj.set("kuaju", chaoshengboSydsj.getKuaju());
                sendchaoshengboSydsj.set("liushuihao", chaoshengboSydsj.getLiushuihao());
                sendchaoshengboSydsj.set("poumianhao", chaoshengboSydsj.getPoumianhao());
                sendchaoshengboSydsj.set("poumianlist", chaoshengboSydsj.getPoumianlist());
                sendchaoshengboSydsj.set("shebeino", chaoshengboSydsj.getShebeino());
                sendchaoshengboSydsj.set("shendu", chaoshengboSydsj.getShendu());
                sendchaoshengboSydsj.set("shengshiindex", chaoshengboSydsj.getShengshiindex());
                sendchaoshengboSydsj.set("shizhuangno", chaoshengboSydsj.getShizhuangno());
                sendchaoshengboSydsj.set("shoubofengindex", chaoshengboSydsj.getShoubofengindex());
                sendchaoshengboSydsj.set("shuju", chaoshengboSydsj.getShuju());
                sendchaoshengboSydsj.set("yanshi", chaoshengboSydsj.getYanshi());
                sendchaoshengboSydsj.set("zengyi", chaoshengboSydsj.getZengyi());
                chaoshengboSydsjList.add(sendchaoshengboSydsj);
            }
            sendJson.set("csbSydsjPOS", chaoshengboSydsjList);

            QueryWrapper<ChaoshengboSyjsb> chaoshengboSyjsbQueryWrapper = new QueryWrapper<>();
            chaoshengboSyjsbQueryWrapper.eq("liushuihao", liushuihao);
            List<ChaoshengboSyjsb> chaoshengboSyjsbs = chaoshengboSyjsbService.list(chaoshengboSyjsbQueryWrapper);
            List chaoshengboSyjsbList = new ArrayList();
            for (ChaoshengboSyjsb chaoshengboSyjsb : chaoshengboSyjsbs) {
                JSONObject sendchaoshengboSyjsb = JSONUtil.createObj();
                sendchaoshengboSyjsb.set("baoxuhao", chaoshengboSyjsb.getBaoxuhao());
                sendchaoshengboSyjsb.set("ceshiyino", chaoshengboSyjsb.getCeshiyino());
                sendchaoshengboSyjsb.set("chaungshuleirong", chaoshengboSyjsb.getChaungshuleirong());
                sendchaoshengboSyjsb.set("daxuhao", chaoshengboSyjsb.getDaxuhao());
                sendchaoshengboSyjsb.set("id", chaoshengboSyjsb.getId());
                sendchaoshengboSyjsb.set("liushuihao", chaoshengboSyjsb.getLiushuihao());
                sendchaoshengboSyjsb.set("poumianlist", chaoshengboSyjsb.getPoumianlist());
                sendchaoshengboSyjsb.set("shebeino", chaoshengboSyjsb.getShebeino());
                sendchaoshengboSyjsb.set("shendu", chaoshengboSyjsb.getShendu());
                sendchaoshengboSyjsb.set("shizhuangno", chaoshengboSyjsb.getShizhuangno());
                sendchaoshengboSyjsb.set("zhuangtai", chaoshengboSyjsb.getZhuangtai());
                chaoshengboSyjsbList.add(sendchaoshengboSyjsb);
            }
            sendJson.set("csbSyjsbPOS", chaoshengboSyjsbList);

            QueryWrapper<ChaoshengboSyljzs> chaoshengboSyljzsQueryWrapper = new QueryWrapper<>();
            chaoshengboSyljzsQueryWrapper.eq("liushuihao", liushuihao);
            List<ChaoshengboSyljzs> chaoshengboSyljzss = chaoshengboSyljzsService.list(chaoshengboSyljzsQueryWrapper);
            List chaoshengboSyljzsList = new ArrayList();
            for (ChaoshengboSyljzs chaoshengboSyljzs : chaoshengboSyljzss) {
                JSONObject sendchaoshengboSyljzs = JSONUtil.createObj();
                sendchaoshengboSyljzs.set("baoxuhao", chaoshengboSyljzs.getBaoxuhao());
                sendchaoshengboSyljzs.set("ceshiyino", chaoshengboSyljzs.getCeshiyino());
                sendchaoshengboSyljzs.set("chuanshuleirong", chaoshengboSyljzs.getChuanshuleirong());
                sendchaoshengboSyljzs.set("id", chaoshengboSyljzs.getId());
                sendchaoshengboSyljzs.set("liushuihao", chaoshengboSyljzs.getLiushuihao());
                sendchaoshengboSyljzs.set("poumian1", chaoshengboSyljzs.getPoumian1());
                sendchaoshengboSyljzs.set("poumian2", chaoshengboSyljzs.getPoumian2());
                sendchaoshengboSyljzs.set("poumian3", chaoshengboSyljzs.getPoumian3());
                sendchaoshengboSyljzs.set("poumian4", chaoshengboSyljzs.getPoumian4());
                sendchaoshengboSyljzs.set("poumian5", chaoshengboSyljzs.getPoumian5());
                sendchaoshengboSyljzs.set("poumianlist", chaoshengboSyljzs.getPoumianlist());
                sendchaoshengboSyljzs.set("shebeino", chaoshengboSyljzs.getShebeino());
                sendchaoshengboSyljzs.set("shizhuangno", chaoshengboSyljzs.getShizhuangno());
                chaoshengboSyljzsList.add(sendchaoshengboSyljzs);
            }
            sendJson.set("csbSyljzsPOS", chaoshengboSyljzsList);

            QueryWrapper<ChaoshengboSyqxsj> chaoshengboSyqxsjQueryWrapper = new QueryWrapper<>();
            chaoshengboSyqxsjQueryWrapper.eq("liushuihao", liushuihao);
            List<ChaoshengboSyqxsj> chaoshengboSyqxsjs = chaoshengboSyqxsjService.list(chaoshengboSyqxsjQueryWrapper);
            List chaoshengboSyqxsjList = new ArrayList();
            for (ChaoshengboSyqxsj chaoshengboSyqxsj : chaoshengboSyqxsjs) {
                JSONObject sendchaoshengboSyqxsj = JSONUtil.createObj();
                sendchaoshengboSyqxsj.set("baoxuhao", chaoshengboSyqxsj.getBaoxuhao());
                sendchaoshengboSyqxsj.set("ceshiyino", chaoshengboSyqxsj.getCeshiyino());
                sendchaoshengboSyqxsj.set("chaungshuleirong", chaoshengboSyqxsj.getChaungshuleirong());
                sendchaoshengboSyqxsj.set("id", chaoshengboSyqxsj.getId());
                sendchaoshengboSyqxsj.set("liushuihao", chaoshengboSyqxsj.getLiushuihao());
                sendchaoshengboSyqxsj.set("poumianlist", chaoshengboSyqxsj.getPoumianlist());
                sendchaoshengboSyqxsj.set("poumianquexian1", chaoshengboSyqxsj.getPoumianquexian1());
                sendchaoshengboSyqxsj.set("poumianquexian2", chaoshengboSyqxsj.getPoumianquexian2());
                sendchaoshengboSyqxsj.set("poumianquexian3", chaoshengboSyqxsj.getPoumianquexian3());
                sendchaoshengboSyqxsj.set("poumianquexian4", chaoshengboSyqxsj.getPoumianquexian4());
                sendchaoshengboSyqxsj.set("poumianquexian5", chaoshengboSyqxsj.getPoumianquexian5());
                sendchaoshengboSyqxsj.set("shebeino", chaoshengboSyqxsj.getShebeino());
                sendchaoshengboSyqxsj.set("shizhuangno", chaoshengboSyqxsj.getShizhuangno());
                chaoshengboSyqxsjList.add(sendchaoshengboSyqxsj);
            }
            sendJson.set("csbSyqxsjPOS", chaoshengboSyqxsjList);

            System.out.println(sendJson);
            String back = HttpRequest.post("http://39.174.68.27:18086/zeiet/csb/save")
                    .body(String.valueOf(sendJson))
                    .timeout(60000)
                    .execute()
                    .body();

            JSONObject jsonObject1 = new JSONObject(back);
            String message = jsonObject1.get("message").toString();
            if ("操作成功".equals(message)) {
                log.info(String.format("超声波推送阳光工程成功!" + id));
            } else {
                log.info(String.format("超声波推送阳光工程失败!" + id));
            }
            sysConfigService.updateSysConfig(JobUtil.YGGC_CSB, id);//根据传过来的唯一值来修改当前判断到的数据id

            for (ChaoshengboSybltsj chaoshengboSybltsj : chaoshengboSybltsjs) {
                JSONObject jsonObject2 = JSONUtil.createObj();
                jsonObject2.set("id", chaoshengboSybltsj.getId());
                byte[] imagedata = chaoshengboSybltsj.getImagedata();
                String s = bytesToString(imagedata);
                jsonObject2.set("imagedata", s);
                String back1 = HttpRequest.post("http://39.174.68.27:18086/zeiet/csb/saveImageData")
                        .body(String.valueOf(jsonObject2))
                        .timeout(60000)
                        .execute()
                        .body();
                System.out.println(back1);
            }
        }
    }

    public static String bytesToString(byte[] src) {
        List list = new ArrayList();
        for (int i = 0; i < src.length; i++) {
            byte b = src[i];
            list.add(b);
        }
        return list.toString();
    }
}
