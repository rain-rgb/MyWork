package org.jeecg.modules.job.yjqs;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzyclpidaichen.entity.Wzyclpidaichen;
import com.trtm.iot.wzyclpidaichen.service.IWzyclpidaichenService;
import com.trtm.iot.yclcl.entity.Wzyclchuchanggb;
import com.trtm.iot.yclcl.service.IWzyclchuchanggbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName shiYanJiJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/16 15:13
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class yjqxzgbJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWzyclchuchanggbService wzyclchuchanggbService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.XZGB_JOB);//新增过磅信息
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到新增过磅信息的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输新增编辑母材收料的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Wzyclchuchanggb> wzycljinchanggbs = wzyclchuchanggbService.slistrqid(shebeilist, curid);
        int id = 0;
        if (wzycljinchanggbs.size() > 0){
            String gettoken = gettoken();
            JSONObject sendObject = JSONUtil.createObj();
            sendObject.set("wagonNo", "bfr3nnx2nj297xkaffp7xlaq15lp30u1");//地磅编号
            sendObject.set("dataType", "his");//数据类型
            for (Wzyclchuchanggb wzycljinchanggb :wzycljinchanggbs){
                id = wzycljinchanggb.getId();

                JSONObject sendObject1 = JSONUtil.createObj();
                sendObject1.set("carNo", wzycljinchanggb.getQianchepai());//车牌号
                sendObject1.set("clientId", wzycljinchanggb.getGuid());//现场ID（唯一标识）
                sendObject1.set("clientNo", wzycljinchanggb.getJinchuliaodanno());//现场流水号
                if (wzycljinchanggb.getGongyingshangdanweibianma() != null){
                    Wzgongyingshang selectnameone = wzgongyingshangService.selectnameone(wzycljinchanggb.getGongyingshangdanweibianma());
                    if (selectnameone != null){
                        sendObject1.set("supplier", selectnameone.getGongyingshangname());//供应商名称（发货方）
                    }else {
                        sendObject1.set("supplier", wzycljinchanggb.getGongyingshangdanweibianma());//供应商名称（发货方）
                    }
                }else {
                    sendObject1.set("supplier", wzycljinchanggb.getGongyingshangdanweibianma());//供应商名称（发货方）
                }
                sendObject1.set("customer", wzycljinchanggb.getLiaocangid());//客户名称（收货方，例如XX拌合站）

                Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.queryselectone1(wzycljinchanggb.getCailiaono());
                if (wzcailiaonamedict.getCailiaoname().contains("砂")){
                    sendObject1.set("materialName", "004");//送料名称(根据6.1接口type=OutPutMaterial获取code,如001)
                }else if (wzcailiaonamedict.getGuigexinghao().equals("5-10mm")){
                    sendObject1.set("materialName", "003");//送料名称(根据6.1接口type=OutPutMaterial获取code,如001)
                }else if (wzcailiaonamedict.getGuigexinghao().equals("10-25mm")){
                    sendObject1.set("materialName", "011");//送料名称(根据6.1接口type=OutPutMaterial获取code,如001)
                }else if (wzcailiaonamedict.getGuigexinghao().equals("25-31.5mm")){
                    sendObject1.set("materialName", "009");//送料名称(根据6.1接口type=OutPutMaterial获取code,如001)
                }else if (wzcailiaonamedict.getGuigexinghao().equals("20-40mm")){
                    sendObject1.set("materialName", "008");//送料名称(根据6.1接口type=OutPutMaterial获取code,如001)
                }else if (wzcailiaonamedict.getGuigexinghao().equals("16-31.5mm")){
                    sendObject1.set("materialName", "001");//送料名称(根据6.1接口type=OutPutMaterial获取code,如001)
                }else if (wzcailiaonamedict.getGuigexinghao().equals("16-25mm")){
                    sendObject1.set("materialName", "007");//送料名称(根据6.1接口type=OutPutMaterial获取code,如001)
                }else if (wzcailiaonamedict.getGuigexinghao().equals("10-20mm")){
                    sendObject1.set("materialName", "002");//送料名称(根据6.1接口type=OutPutMaterial获取code,如001)
                }else if (wzcailiaonamedict.getGuigexinghao().equals("10-16mm")){
                    sendObject1.set("materialName", "006");//送料名称(根据6.1接口type=OutPutMaterial获取code,如001)
                }else if (wzcailiaonamedict.getGuigexinghao().equals("5-20mm")){
                    sendObject1.set("materialName", "010");//送料名称(根据6.1接口type=OutPutMaterial获取code,如001)
                }else if (wzcailiaonamedict.getGuigexinghao().equals("5-16mm")){
                    sendObject1.set("materialName", "005");//送料名称(根据6.1接口type=OutPutMaterial获取code,如001)
                }

                sendObject1.set("weightRough", wzycljinchanggb.getMaozhongt());//毛重
                sendObject1.set("weightTare", wzycljinchanggb.getPizhongt());//皮重
                sendObject1.set("weightSuttle", wzycljinchanggb.getJingzhongt());//净重
                sendObject1.set("deduction", 0.0);//减扣
                sendObject1.set("roughTime", wzycljinchanggb.getChuchangshijian());//毛重称重时间（yyyy-MM-dd
                sendObject1.set("tareTime", wzycljinchanggb.getJinchangshijian());//重称重时间（yyyy-MM-dd hh:mm:ss）
                sendObject1.set("completeTime", wzycljinchanggb.getJinchangshijian());//完成时间（yyyy-MM-dd hh:mm:ss）
                sendObject1.set("weightman", wzycljinchanggb.getSibangyuan());//司磅员
                sendObject1.set("weightFinal", wzycljinchanggb.getJingzhongt());//实重

                List<JSONObject> list = new ArrayList<>();
                list.add(sendObject1);
                sendObject.set("data", list);//添加出库记录详情数据
                System.out.println(sendObject);
                String back = HttpRequest.post("http://jzs.sgiot.xyz/api/weighbridge/weigh-receive-port")
                        .header("Content-Type", "application/json")
                        .header("token",gettoken)
                        .body(String.valueOf(sendObject))
                        .execute()
                        .body();
                JSONObject jsonObject1 = new JSONObject(back);
                System.out.println(jsonObject1);
                JSONObject data = (JSONObject) jsonObject1.get("data");
                if (data != null){
                    int code = (int) data.get("code");
                    String msg = (String) data.get("msg");
                    if (msg.equals("出库失败,库存不足！")){
                        return;
                    }
                    if (code == -1) {
                        wzycljinchanggb.setStatuscode(-1);
                        log.info(String.format("新增编辑母材收料推送失败!" + id));
                    } else {
                        wzycljinchanggb.setStatuscode(1);
                        log.info(String.format("新增编辑母材收料推送成功!" + id));
                    }
                    wzyclchuchanggbService.updateById(wzycljinchanggb);
                    sysConfigService.updateSysConfig(JobUtil.XZGB_JOB, id);//根据传过来的唯一值来修改当前判断到的数据id
                }else {
                    return;
                }
            }
        }
    }
    //母材消耗批次号
    private static String getmcxh(String gettoken){
        String back = HttpRequest.get("http://jzs.sgiot.xyz/api/dictionary-management/sand-type-tag-list?type=OutPutMaterial&pageno=1&pagesize=10")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("token",gettoken)
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(back);
        JSONObject jsonObject2 =(JSONObject) jsonObject1.get("data");
        String accessToken = jsonObject2.get("code").toString();
        return accessToken;
    }
    //登录
    private static String gettoken(){
        JSONObject sendObject = JSONUtil.createObj();
        sendObject.set("loginName", "wangyafei");
        sendObject.set("password", "F2963AFA2DA2596A669EA57EFF2BC9A6");//密码需通过MD5 32位大写进行加密
        sendObject.set("isVerify", false);
        System.out.println(sendObject);
        String back = HttpRequest.post("http://jzs.sgiot.xyz/api/login/third-party-logins")
                .header("Content-Type", "application/json")
                .body(String.valueOf(sendObject))
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(back);
        JSONObject jsonObject2 =(JSONObject) jsonObject1.get("data");
        String accessToken = jsonObject2.get("accessToken").toString();
        return accessToken;
    }

}

