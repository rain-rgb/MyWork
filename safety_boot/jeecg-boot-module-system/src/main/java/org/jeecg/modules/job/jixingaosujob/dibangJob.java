package org.jeecg.modules.job.jixingaosujob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName dibangJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/11/24 14:16
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class dibangJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JX_DB);//济新高速地磅
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到济新高速地磅定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传济新高速地磅的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Wzycljinchanggb> wzycljinchanggbs = wzycljinchanggbService.selectyclList(curid, shebeilist);
        if (null == wzycljinchanggbs || wzycljinchanggbs.size() == 0) {
            log.info(String.format("暂无中铁十五局三分部地磅未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (Wzycljinchanggb wzycljinchanggb : wzycljinchanggbs) {
            id = wzycljinchanggb.getId();
            JSONObject sendDate = new JSONObject();
//            String shebeibianhao = wzycljinchanggb.getShebeibianhao();
//            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
//            shebeiInfoQueryWrapper.eq("sbjno",shebeibianhao);
//            ShebeiInfo one = shebeiInfoService.getOne(shebeiInfoQueryWrapper);
//            String sbname = one.getSbname();

            sendDate.set("Id","JX"+wzycljinchanggb.getId());
//            sendDate.set("OrganizationId","1567673706677800960");
//            sendDate.set("SectionId","1572515707655032832");
//            sendDate.set("SectionName","济新高速JXSG-2标");
            String MixingStationCode = "";
            if ("A05A05A01A01A01A01_DB_10289".equals(wzycljinchanggb.getShebeibianhao())){
                MixingStationCode = "2-2-3-1";
            }else {
                MixingStationCode = "2-1-1-1";
            }
            sendDate.set("MixingStationCode",MixingStationCode);
//            sendDate.set("MixingStationName","济新高速二标二分部三号拌合站");
//            sendDate.set("EquipmentNumber","JXGSDB0203");

            String gongyingshangdanweibianma = wzycljinchanggb.getGongyingshangdanweibianma();
            QueryWrapper<Wzgongyingshang> wzgongyingshangQueryWrapper = new QueryWrapper<>();
            wzgongyingshangQueryWrapper.eq("guid",gongyingshangdanweibianma);
            Wzgongyingshang one1 = wzgongyingshangService.getOne(wzgongyingshangQueryWrapper);

            sendDate.set("MaterialBrand","");//厂商
            sendDate.set("MaterialVendor",one1.getGongyingshangname());//供货商

            String cailiaono = wzycljinchanggb.getCailiaono();
            QueryWrapper<Wzcailiaonamedict> wzcailiaonamedictQueryWrapper = new QueryWrapper<>();
            wzcailiaonamedictQueryWrapper.eq("cailiaoNo",cailiaono);
            Wzcailiaonamedict one2 = wzcailiaonamedictService.getOne(wzcailiaonamedictQueryWrapper);
            String cailiaoname = one2.getCailiaoname();
            String guige = one2.getGuigexinghao();
            String MaterialType = cailiaoname;
            String MaterialSpec = guige;
            if (cailiaoname.contains("粉煤灰")){
                MaterialType = "粉煤灰";
                MaterialSpec = "Ⅰ级F类";
            }
            if (cailiaoname.contains("机制砂")){
                if (guige.contains("中砂")) {
                    MaterialType = "机制砂";
                    MaterialSpec = "中砂";
                }
                if (guige.contains("0-5mm")) {
                    MaterialType = "机制砂";
                    MaterialSpec = "0-5mm";
                }
            }
            if (cailiaoname.contains("河砂")){
                MaterialType = "河砂";
                MaterialSpec = "中砂";
            }
            if (cailiaoname.contains("水泥")){
                if (guige.contains("PO52.5")){
                    MaterialType = "水泥";
                    MaterialSpec = "P·O52.5散装";
                }
                if (guige.contains("P·O42.5")){
                    MaterialType = "水泥";
                    MaterialSpec = "P·O42.5散装";
                }
            }
            if (cailiaoname.contains("碎石")){
                if (guige.contains("16mm-31.5")){
                    MaterialType = "碎石";
                    MaterialSpec = "16-31.5mm";
                }
                if (guige.contains("10mm-20")){
                    MaterialType = "碎石";
                    MaterialSpec = "10-20mm";
                }
                if (guige.contains("5mm-10")){
                    MaterialType = "碎石";
                    MaterialSpec = "5-10mm";
                }
            }
            sendDate.set("MaterialType",MaterialType);
            sendDate.set("MaterialSpec",MaterialSpec);
            sendDate.set("MaterialBatch",wzycljinchanggb.getJinchuliaodanno());
            sendDate.set("ProductionDate",wzycljinchanggb.getJinchangshijian());
            sendDate.set("SendingPlace",one1.getGongyingshangname());
            sendDate.set("ReceivingPlace","济新高速JXSG-2标");
            sendDate.set("GrossWeight",Double.valueOf(wzycljinchanggb.getMaozhong()));
            sendDate.set("TareWeight",Double.valueOf(wzycljinchanggb.getPizhong()));
            sendDate.set("NetWeight",Double.valueOf(wzycljinchanggb.getJingzhong()));
            sendDate.set("Deduction",Double.valueOf(wzycljinchanggb.getKouzhong()));
//            sendDate.set("CountingWeight",wzycljinchanggb.get);
            sendDate.set("ActualWeight",Double.valueOf(wzycljinchanggb.getJingzhong()));
            sendDate.set("CarNumber",wzycljinchanggb.getQianchepai());
            sendDate.set("EntryTime",wzycljinchanggb.getJinchangshijian());
            sendDate.set("ExitTime",wzycljinchanggb.getChuchangshijian());
            sendDate.set("SerialNumber",wzycljinchanggb.getJinchuliaodanno());
            sendDate.set("Creator",wzycljinchanggb.getSibangyuan());

            System.out.println(sendDate);
            String back = HttpRequest.post("http://59.110.35.155:8122/api/material/loadometer/add")
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();

            pushandreturnService.saveData(id,String.valueOf(sendDate),selectsysconfigone.getRemark(),back);

            JSONObject jsonObject1 = new JSONObject(back);
            String message = jsonObject1.get("Message").toString();
            String Data = jsonObject1.get("Data").toString();
            if ("操作成功".equals(message)&&Data.contains("true")) {
                log.info(String.format("济新高速地磅推送成功!" + back));
            } else {
                log.info(String.format("济新高速地磅推送失败!" + back));
                Wzycljinchanggb wzycljinchanggb1 = new Wzycljinchanggb();
                wzycljinchanggb1.setId(id);
                wzycljinchanggb1.setRemark("1");
                wzycljinchanggbService.updateById(wzycljinchanggb1);
            }
            sysConfigService.updateSysConfig(JobUtil.JX_DB, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
