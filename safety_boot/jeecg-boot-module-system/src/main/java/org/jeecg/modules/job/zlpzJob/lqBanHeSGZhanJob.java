package org.jeecg.modules.job.zlpzJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zlpz.entity.Zlpz;
import com.trtm.iot.zlpz.service.IZlpzService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName lqBanHeZhanJob：
 * @Description 浙路品质疏港(大于初级)
 * @Author 55314
 * @Date 2022/12/5 10:25
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class lqBanHeSGZhanJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;
    @Autowired
    private IBhzLqCailiaoService bhzLqCailiaoService;
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;
    @Autowired
    private IZlpzService zlpzService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZLPZ_LQSG);//沥青拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞苍砼拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzLqBases> bhzLqBasess = bhzLqBasesService.selectListSG(shebeilist, curid);
        if (null == bhzLqBasess || bhzLqBasess.size() == 0) {
            log.info(String.format("暂无拌合站未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (BhzLqBases bhzLqBases : bhzLqBasess) {
            id = bhzLqBases.getId();
            QueryWrapper<Zlpz> queryWrapperZlpz = new QueryWrapper<>();
            queryWrapperZlpz.eq("shebeino",bhzLqBases.getShebeibianhao());
            Zlpz one1 = zlpzService.getOne(queryWrapperZlpz);
            String shebeiid = one1.getShebeiid();
            String project = one1.getProject();

            JSONObject sendDate = new JSONObject();
            List sendList = new ArrayList();

            sendDate.set("id", project+"_"+id);
            sendDate.set("equipId", shebeiid);
            sendDate.set("part", "");
            sendDate.set("colTime", bhzLqBases.getCaijishijian());
            int alarmStatus = 0;
            QueryWrapper<BhzCementOverHandler> bhzCementOverHandlerQueryWrapper = new QueryWrapper<>();
            bhzCementOverHandlerQueryWrapper.eq("baseId",bhzLqBases.getGuid());
            BhzCementOverHandler one = bhzCementOverHandlerService.getOne(bhzCementOverHandlerQueryWrapper);

            if (null != one) {
                alarmStatus = 1;
                sendDate.set("opinion", one.getHandleWay());
                sendDate.set("closeTime", one.getHandleTime());
                sendDate.set("closer", one.getHandlePerson());
                sendDate.set("attachment", one.getFilePath());//附件
            }
            sendDate.set("alarmStatus", alarmStatus);

            QueryWrapper<BhzLqCailiao> bhzLqCailiaoQueryWrapper = new QueryWrapper<>();
            bhzLqCailiaoQueryWrapper.eq("base_guid",bhzLqBases.getGuid());
            List<BhzLqCailiao> list = bhzLqCailiaoService.list(bhzLqCailiaoQueryWrapper);
            List list2 = new ArrayList();
            for (BhzLqCailiao bhzLqCailiao:list){
                JSONObject cailiao = new JSONObject();
                cailiao.set("materialType", bhzLqCailiao.getCailiaoleixing());
                cailiao.set("materialName", bhzLqCailiao.getCailiaoming());
                Double shijiyongliang = bhzLqCailiao.getShijiyongliang();
                Double theoryNumber = bhzLqCailiao.getTheoryNumber();
                cailiao.set("realityVal", shijiyongliang);

                double chujichaobiao = bhzLqCailiao.getChujichaobiao();
                double max = theoryNumber * (1 + chujichaobiao / 100);
                double min = theoryNumber * (1 - chujichaobiao / 100);
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                cailiao.set("allowMin", Double.parseDouble(decimalFormat.format(min)));
                cailiao.set("allowMax", Double.parseDouble(decimalFormat.format(max)));


                list2.add(cailiao);
            }

            sendDate.set("dosageList", list2);

            sendList.add(sendDate);
            JSONObject sendJsonObject = new JSONObject();
            sendJsonObject.set("serviceName","ZLPZ_ZX_LQHNTBH");
            sendJsonObject.set("token","93cd2c6567594107a16b51a65bcd5a37");
            sendJsonObject.set("updateNull","true");
            sendJsonObject.set("param",sendList);

            String url = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/push";
            String back = HttpRequest.post(url)
                    .body(sendJsonObject.toString())
                    .timeout(20000)
                    .execute().body();

            com.alibaba.fastjson.JSONObject backJson = JSON.parseObject(back);
            int result = backJson.getIntValue("result");
            if (result == 0) {
                bhzLqBases.setIszlpz(alarmStatus == 0 ? 3 : 1);
                log.info(String.format("浙路品质疏港沥青拌合站推送成功！%s%s", sendJsonObject, back));
            } else {
                bhzLqBases.setIszlpz(2);
                log.info(String.format("浙路品质疏港沥青拌合站推送失败！%s%s", sendJsonObject, back));
            }

            bhzLqBasesService.updateById(bhzLqBases);
            pushandreturnService.saveData(id,String.valueOf(sendDate),selectsysconfigone.getRemark(),back);
        }
    }
}
