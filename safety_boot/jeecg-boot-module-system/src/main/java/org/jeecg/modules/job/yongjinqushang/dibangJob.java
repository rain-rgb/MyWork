package org.jeecg.modules.job.yongjinqushang;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import com.xkcoding.http.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName dibangJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/11/22 10:29
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
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    private String url = "http://115.236.10.10:8081/zjjg-iot/weighbridge/saveInPoundListInfo";
//    private String url = "http://121.41.26.120:8081/zjjg-iot-test/weighbridge/saveInPoundListInfo";

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YJQS_DB);//甬金衢上地磅
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到甬金衢上砼地磅定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输甬金衢上砼地磅的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Wzycljinchanggb> wzycljinchanggbList = wzycljinchanggbService.selectListsyjqs(shebeilist, curid);
        if (null == wzycljinchanggbList || wzycljinchanggbList.size() == 0) {
            log.info(String.format("暂无甬金衢上砼地磅的数据" + DateUtils.now()));
            return;
        } else {
            log.info(String.format("甬金衢上砼地磅数据推送开始！" + DateUtils.now()));
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Wzycljinchanggb wzycljinchanggb : wzycljinchanggbList) {
            id = wzycljinchanggb.getId();
            JSONObject sendObject = new JSONObject();
            sendObject.set("id", wzycljinchanggb.getId());
            sendObject.set("ListCode", wzycljinchanggb.getJinchuliaodanno());//	磅单编号
//            sendObject.set("ListType",wzycljinchanggb.get);//	磅单类型名称
//            sendObject.set("ListTypeFlag",wzycljinchanggb.get);//	磅单类型编码
//            sendObject.set("ListSpeci",wzycljinchanggb.get);//	磅单种类
//            sendObject.set("ListSpeciFlag",wzycljinchanggb.get);//	磅单种类编码
            sendObject.set("WeighUnit", "kg");//	过磅单位

            String gongyingshangdanweibianma = wzycljinchanggb.getGongyingshangdanweibianma();
            Wzgongyingshang selectnameone = wzgongyingshangService.selectnameone(gongyingshangdanweibianma);
            sendObject.set("SupplierId", selectnameone.getId());//	供应商Id
            sendObject.set("SupplierCode", gongyingshangdanweibianma);//	供应商编码
            sendObject.set("Supplier", selectnameone.getGongyingshangname());//	供应商
            sendObject.set("CarNumber", wzycljinchanggb.getQianchepai());//	车牌号
//            sendObject.set("DischargeDate",wzycljinchanggb.get);//	进料时间
//            sendObject.set("RedFlushState",wzycljinchanggb.get);//	冲红状态
            sendObject.set("ApproachTime", sdf.format(sdf.parse(wzycljinchanggb.getJinchangshijian())));//	进场时间
            sendObject.set("AppearanceTime", sdf.format(sdf.parse(wzycljinchanggb.getChuchangshijian())));//	出场时间

            Wzliaocang queryselectone = wzliaocangService.queryselectone(wzycljinchanggb.getLcbianhao());
            if (queryselectone != null) {
                sendObject.set("Bunker", queryselectone.getCailiaoname());//	料仓
            }
//            sendObject.set("JobNumber",wzycljinchanggb.get);//	工号
            sendObject.set("GrossWeight", wzycljinchanggb.getMaozhong());//	毛重
            sendObject.set("TareWeight", wzycljinchanggb.getPizhong());//	皮重
            sendObject.set("MainNetWeight",wzycljinchanggb.getJingzhong());//	净重（主）
//            sendObject.set("AuxiliaryNetWeight",wzycljinchanggb.get);//	净重（辅）
            sendObject.set("DeductionRate", wzycljinchanggb.getKoulv());//	扣率
            sendObject.set("DeductionTonnage", wzycljinchanggb.getKouzhong());//	扣吨
//            sendObject.set("AccountingNetWeight",wzycljinchanggb.get);//	核算净重
//            sendObject.set("OriginalNetWeight",wzycljinchanggb.get);//	原始净重
//            sendObject.set("WaybillWeight",wzycljinchanggb.get);//	运单重量
//            sendObject.set("IsAutoSkinBack",wzycljinchanggb.get);//	是否自动回皮
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(wzycljinchanggb.getShebeibianhao());
            sendObject.set("ProjectCode", selectshebeione.getTunnelId());//	项目编码 与组织接口中获取orgCode对应
            sendObject.set("ProjectName", selectshebeione.getTunnelName());//	项目名称
//            sendObject.set("ProjectId",wzycljinchanggb.get);//	项目id
            sendObject.set("Creator", wzycljinchanggb.getSibangyuan());//	制单人
//            sendObject.set("CreateTime",wzycljinchanggb.get);//	制单时间
//            sendObject.set("Remark",wzycljinchanggb.get);//	备注


            List PhotoList = new ArrayList();

            if (StringUtil.isNotEmpty(wzycljinchanggb.getJcgkpic())) {
                Map jcgkmap = new HashMap<>();
                jcgkmap.put("PhotoType", "入场");//	照片类型
                jcgkmap.put("PhotoPosition", "高空");//	照片位置
                jcgkmap.put("PhotoPositionFlag", "高空");//	照片位置Flag
                jcgkmap.put("PhotoOriginalUrl", "http://web.traiot.cn/docs/wz/" + wzycljinchanggb.getJcgkpic());//	图片原始Url
                PhotoList.add(jcgkmap);
            }

            if (StringUtil.isNotEmpty(wzycljinchanggb.getJccppic())) {
                Map jcqmap = new HashMap<>();
                jcqmap.put("PhotoType", "入场");//	照片类型
                jcqmap.put("PhotoPosition", "车前");//	照片位置
                jcqmap.put("PhotoPositionFlag", "车前");//	照片位置Flag
                jcqmap.put("PhotoOriginalUrl", "http://web.traiot.cn/docs/wz/" + wzycljinchanggb.getJccppic());//	图片原始Url
                PhotoList.add(jcqmap);
            }

            if (StringUtil.isNotEmpty(wzycljinchanggb.getJchcppic())) {
                Map jchmap = new HashMap<>();
                jchmap.put("PhotoType", "入场");//	照片类型
                jchmap.put("PhotoPosition", "车后");//	照片位置
                jchmap.put("PhotoPositionFlag", "车后");//	照片位置Flag
                jchmap.put("PhotoOriginalUrl", "http://web.traiot.cn/docs/wz/" + wzycljinchanggb.getJchcppic());//	图片原始Url
                PhotoList.add(jchmap);
            }

            if (StringUtil.isNotEmpty(wzycljinchanggb.getJcbfpic())) {
                Map jcbfmap = new HashMap<>();
                jcbfmap.put("PhotoType", "入场");//	照片类型
                jcbfmap.put("PhotoPosition", "磅房");//	照片位置
                jcbfmap.put("PhotoPositionFlag", "磅房");//	照片位置Flag
                jcbfmap.put("PhotoOriginalUrl", "http://web.traiot.cn/docs/wz/" + wzycljinchanggb.getJcbfpic());//	图片原始Url
                PhotoList.add(jcbfmap);
            }

            if (StringUtil.isNotEmpty(wzycljinchanggb.getCcgkpic())) {
                Map ccgkmap = new HashMap<>();
                ccgkmap.put("PhotoType", "出场");//	照片类型
                ccgkmap.put("PhotoPosition", "高空");//	照片位置
                ccgkmap.put("PhotoPositionFlag", "高空");//	照片位置Flag
                ccgkmap.put("PhotoOriginalUrl", "http://web.traiot.cn/docs/wz/" + wzycljinchanggb.getCcgkpic());//	图片原始Url
                PhotoList.add(ccgkmap);
            }

            if (StringUtil.isNotEmpty(wzycljinchanggb.getCccppic())) {
                Map ccqmap = new HashMap<>();
                ccqmap.put("PhotoType", "出场");//	照片类型
                ccqmap.put("PhotoPosition", "车前");//	照片位置
                ccqmap.put("PhotoPositionFlag", "车前");//	照片位置Flag
                ccqmap.put("PhotoOriginalUrl", "http://web.traiot.cn/docs/wz/" + wzycljinchanggb.getCccppic());//	图片原始Url
                PhotoList.add(ccqmap);
            }

            if (StringUtil.isNotEmpty(wzycljinchanggb.getCchcppic())) {
                Map cchmap = new HashMap<>();
                cchmap.put("PhotoType", "出场");//	照片类型
                cchmap.put("PhotoPosition", "车后");//	照片位置
                cchmap.put("PhotoPositionFlag", "车后");//	照片位置Flag
                cchmap.put("PhotoOriginalUrl", "http://web.traiot.cn/docs/wz/" + wzycljinchanggb.getCchcppic());//	图片原始Url
                PhotoList.add(cchmap);
            }

            if (StringUtil.isNotEmpty(wzycljinchanggb.getCcbfpic())) {
                Map ccbfmap = new HashMap<>();
                ccbfmap.put("PhotoType", "入场");//	照片类型
                ccbfmap.put("PhotoPosition", "磅房");//	照片位置
                ccbfmap.put("PhotoPositionFlag", "磅房");//	照片位置Flag
                ccbfmap.put("PhotoOriginalUrl", "http://web.traiot.cn/docs/wz/" + wzycljinchanggb.getCcbfpic());//	图片原始Url
                PhotoList.add(ccbfmap);
            }

            sendObject.set("Photo", PhotoList);//	照片

            List cailiaoList = new ArrayList();

            Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzycljinchanggb.getCailiaono());
            Map cailiao = new HashMap<>();
//            cailiao.put("MaterialCode",wzcailiaonamedict.get);// 材料编码
            cailiao.put("MaterialName",wzcailiaonamedict.getCailiaoname());//	材料名称
            cailiao.put("MaterialId",wzcailiaonamedict.getCailiaono());//	材料Id
            cailiao.put("Specification",wzcailiaonamedict.getGuigexinghao());//	规格型号
//            cailiao.put("ConversionRate",wzcailiaonamedict.get);//	转化率
            cailiao.put("MainUnit","kg");//	主单位
//            cailiao.put("AuxiliaryUnit",wzcailiaonamedict.get);//	辅单位

            cailiaoList.add(cailiao);
            sendObject.set("MaterialInfo", cailiaoList);//	材料信息

            System.out.println(sendObject);
            String result = HttpRequest.post(url)
                    .header("client-code","gaoxun")
                    .body(String.valueOf(sendObject))
                    .timeout(20000)
                    .execute()
                    .body();

            pushandreturnService.saveData(id, String.valueOf(sendObject),selectsysconfigone.getRemark(),result);
            if (result.contains("200")){
                log.info("甬金衢上高速3标地磅推送成功！");
            }else{
                log.info("甬金衢上高速3标地磅推送失败！");
            }
            sysConfigService.updateSysConfig(JobUtil.YJQS_DB,id);
        }
    }
}
