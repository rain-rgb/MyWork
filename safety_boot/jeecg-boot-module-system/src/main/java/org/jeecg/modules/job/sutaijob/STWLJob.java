package org.jeecg.modules.job.sutaijob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.JSONObject;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class STWLJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IWzliaocangService wzliaocangService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.STTWO_ONESL);//苏台二期一标原材料收料
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台原材料收料定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台原材料收料的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
//        String[] split = shebeilist.split(",");
//        List<String> strsToList = Arrays.asList(split);
        List<Wzycljinchanggb> wzycljinchanggbs = wzycljinchanggbService.selectyclList(curid,shebeilist);
        if (null == wzycljinchanggbs || wzycljinchanggbs.size() == 0) {
            log.info(String.format("暂无苏台原材料收料的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        JSONArray jsonArray = new JSONArray();
        for (Wzycljinchanggb wzycljinchanggb : wzycljinchanggbs) {
            id = wzycljinchanggb.getId();
            JSONObject saveDTOList = new JSONObject();
            saveDTOList.set("approachTime", wzycljinchanggb.getJinchangshijian());//进场时间
            saveDTOList.set("batch", wzycljinchanggb.getPici());//批次
            saveDTOList.set("buttonAgain", wzycljinchanggb.getKouzhong());//扣重(单位:kg)
            saveDTOList.set("departureTime", wzycljinchanggb.getChuchangshijian());//离场时间
            saveDTOList.set("discount", wzycljinchanggb.getKoulv());//扣率
            saveDTOList.set("equipmentName", "苏台高速二期2标地磅");//设备编号
            saveDTOList.set("hullNo", wzycljinchanggb.getCheliangbianhao());//材料名称
            saveDTOList.set("orderName", null);//材料名称
            if (wzycljinchanggb.getCailiaono() != null) {
                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzycljinchanggb.getCailiaono());//材料表
                if (wzcailiaonamedict != null) {
                    saveDTOList.set("orderName", wzcailiaonamedict.getCailiaoname());//材料名称
                }
            }

            saveDTOList.set("orderNo", wzycljinchanggb.getJinchuliaodanno());//进出材料单单号
            saveDTOList.set("pass",1);
            saveDTOList.set("plateNo", wzycljinchanggb.getQianchepai());//前车牌
            saveDTOList.set("roundWeight", wzycljinchanggb.getMaozhong());//毛重(单位:kg)
            saveDTOList.set("sectionType", 2);//1 一标 2 二标 3 三标 4 四标 5 五标

            saveDTOList.set("supplierOrg", null);//供应商单位
            if (wzycljinchanggb.getGongyingshangdanweibianma() != null) {
                Wzgongyingshang wzgongyingshang = wzgongyingshangService.selectnameone(wzycljinchanggb.getGongyingshangdanweibianma());
                if (wzgongyingshang != null) {
                    saveDTOList.set("supplierOrg", wzgongyingshang.getGongyingshangname());//供应商单位
                }
            }

            saveDTOList.set("suttle", wzycljinchanggb.getJingzhong());//	净重(单位:kg)
            saveDTOList.set("trae", wzycljinchanggb.getPizhong());//	皮重(单位:kg)
            saveDTOList.set("transportationOrg", wzycljinchanggb.getYunshudanwei());//运输单位

            saveDTOList.set("warehouseName", null);//料仓名称
            if (wzycljinchanggb.getLcbianhao() != null) {
                Wzliaocang queryselectone = wzliaocangService.queryselectone(wzycljinchanggb.getLcbianhao());
                if (queryselectone != null) {
                    saveDTOList.set("warehouseName", queryselectone.getCailiaoname());//料仓名称
                }
            }
            saveDTOList.set("weighman", wzycljinchanggb.getSibangyuan());//司磅员
            jsonArray.add(saveDTOList);
        }

        String post = HttpRequest.post("http://47.98.163.134:9170/solutionsMaterialReceipt/insertBacth")
                .header("Content-Type", "application/json")
                .body(String.valueOf(jsonArray))
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(post);
        Integer code = (Integer) jsonObject1.get("code");
        if (code == 200 || code == 0) {
            log.info(String.format("苏台原材料收料数据推送成功!" + id));
            sysConfigService.updateSysConfig(JobUtil.STTWO_ONESL, id);//根据传过来的唯一值来修改当前判断到的数据id
        } else {
            log.info(String.format("苏台原材料收料数据推送失败!" + id));
        }

    }
}
