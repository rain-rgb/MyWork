package org.jeecg.modules.job.ztswjjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
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
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName LoadometerJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/5/26 17:13
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LoadometerJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.DB_SFBDB);//中铁十五局三分部地磅
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到中铁十五局三分部地磅的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输中铁十五局三分部地磅的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Wzycljinchanggb> wzycljinchanggbs = wzycljinchanggbService.selectLists1(shebeilist, curid);
        if (null == wzycljinchanggbs || wzycljinchanggbs.size() == 0) {
            log.info(String.format("暂无中铁十五局三分部地磅未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (Wzycljinchanggb wzycljinchanggb : wzycljinchanggbs) {
            id = wzycljinchanggb.getId();
            JSONObject sendDate = new JSONObject();
            sendDate.set("number", wzycljinchanggb.getJinchuliaodanno());
            sendDate.set("car", wzycljinchanggb.getQianchepai());
            sendDate.set("gross", wzycljinchanggb.getMaozhong());
            sendDate.set("tare", wzycljinchanggb.getPizhong());
            sendDate.set("net", wzycljinchanggb.getJingzhong());
            sendDate.set("weight", wzycljinchanggb.getJingzhong());
            sendDate.set("volume", "");
            sendDate.set("imei", wzycljinchanggb.getShebeibianhao());
            sendDate.set("sort", wzycljinchanggb.getGuobangleibie());

            String gongyingshangdanweibianma = wzycljinchanggb.getGongyingshangdanweibianma();
            QueryWrapper<Wzgongyingshang> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("gongyingshangName");
            queryWrapper.eq("guid", gongyingshangdanweibianma);
            Wzgongyingshang gongyingshangName = wzgongyingshangService.getOne(queryWrapper);

            sendDate.set("supply", gongyingshangName.getGongyingshangname());

            String shebeibianhao = wzycljinchanggb.getShebeibianhao();
            QueryWrapper<ShebeiInfo> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("sys_org_code");
            queryWrapper1.eq("sbjno", shebeibianhao);
            ShebeiInfo sys_org_code = shebeiInfoService.getOne(queryWrapper1);
            QueryWrapper<SysDepart> sysDepartQueryWrapper = new QueryWrapper<>();
            sysDepartQueryWrapper.select("depart_name");
            sysDepartQueryWrapper.eq("org_code",sys_org_code.getSysOrgCode());
            SysDepart depart_name = sysDepartService.getOne(sysDepartQueryWrapper);

            sendDate.set("receive", depart_name.getDepartName());

            String cailiaono = wzycljinchanggb.getCailiaono();
            QueryWrapper<Wzcailiaonamedict> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.select("cailiaoName,guigexinghao");
            queryWrapper2.eq("cailiaoNo", cailiaono);
            Wzcailiaonamedict cailiaoName = wzcailiaonamedictService.getOne(queryWrapper2);

            sendDate.set("name", cailiaoName.getCailiaoname());
            sendDate.set("spec", cailiaoName.getGuigexinghao());

            sendDate.set("rate", wzycljinchanggb.getChengzhongpiancha());
            sendDate.set("delivery_net", wzycljinchanggb.getJingzhong());
            sendDate.set("buckle", wzycljinchanggb.getKouzhong());
            sendDate.set("price", "");
            sendDate.set("money", "");
            sendDate.set("unit", "kg");
            sendDate.set("fee", "");
            sendDate.set("gross_clerk", wzycljinchanggb.getSibangyuan());
            sendDate.set("tare_clerk", wzycljinchanggb.getSibangyuan());
            sendDate.set("gross_number", "");
            sendDate.set("tare_number", "");
            sendDate.set("gross_time", wzycljinchanggb.getJinchangshijian());
            sendDate.set("tare_time", wzycljinchanggb.getChuchangshijian());
            sendDate.set("first_time", wzycljinchanggb.getJinchangshijian());
            sendDate.set("second_time", wzycljinchanggb.getChuchangshijian());
            System.out.println(sendDate);
            String back = HttpRequest.post("https://build.cninct.com/CR15G3GS?op=UploadLoadometerWeight")
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();

            pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), back);
            JSONObject jsonObject1 = new JSONObject(back);
            String codes = jsonObject1.get("message").toString();
            if (codes.contains("成功")) {
                log.info(String.format("中铁十五局三分部地磅推送成功!" + id + "Json数据" + sendDate));
            } else {
                log.info(String.format("中铁十五局三分部地磅推送失败!" + id + "Json数据" + sendDate));
            }
            sysConfigService.updateSysConfig(JobUtil.DB_SFBDB, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
