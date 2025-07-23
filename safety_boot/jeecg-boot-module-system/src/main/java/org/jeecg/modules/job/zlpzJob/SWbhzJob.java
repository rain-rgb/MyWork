package org.jeecg.modules.job.zlpzJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.swbhz.service.IBhzSwCailiaoService;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SWbhzJob：
 * @Description 浙路品质水稳
 * @Author 55314
 * @Date 2023/2/24 14:49
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SWbhzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzSwBasesService bhzSwBasesService;
    @Autowired
    private IBhzSwCailiaoService bhzSwCailiaoService;
    @Autowired
    private IZlpzService zlpzService;
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZLPZ_SWBHZ);//浙路品质水稳
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到水稳拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输水稳拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzSwBases> selectswbhzlist = bhzSwBasesService.selectList1(shebeilist, curid);
        if (null == selectswbhzlist || selectswbhzlist.size() == 0) {
            log.info(String.format("暂无拌合站未推送数据" + DateUtils.now()));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int id = 0;
        for (BhzSwBases bhzSwBases : selectswbhzlist) {
            id = bhzSwBases.getId();
            QueryWrapper<Zlpz> queryWrapperZlpz = new QueryWrapper<>();
            queryWrapperZlpz.eq("shebeino", bhzSwBases.getShebeibianhao());
            Zlpz one1 = zlpzService.getOne(queryWrapperZlpz);
            String shebeiid = one1.getShebeiid();
            String project = one1.getProject();

            JSONObject sendDate = new JSONObject();
            List sendList = new ArrayList();

            sendDate.set("id", project + "_" + id);
            sendDate.set("equipId", shebeiid);

            String poureLocation = bhzSwBases.getPoureLocation();
            String part = poureLocation != null ? poureLocation : "/";
            sendDate.set("part", part);

            sendDate.set("prodTime", bhzSwBases.getChuliaoshijian());
//            sendDate.set("handler", "/");
//            sendDate.set("prodVal", bhzSwBases.getZongchanliang());
            sendDate.set("colTime", sdf.format(bhzSwBases.getCaijishijian()));
//            sendDate.set("result", bhzSwBases.getChaobiaodengji());

            int alarmStatus = 0;
            QueryWrapper<BhzCementOverHandler> bhzCementOverHandlerQueryWrapper = new QueryWrapper<>();
            bhzCementOverHandlerQueryWrapper.eq("baseId", bhzSwBases.getGuid());
            BhzCementOverHandler one = bhzCementOverHandlerService.getOne(bhzCementOverHandlerQueryWrapper);
            if (null != one) {
                alarmStatus = 1;
                sendDate.set("opinion", one.getHandleWay());
                sendDate.set("closeTime", sdf.format(one.getHandleTime()));
                sendDate.set("closer", one.getHandlePerson());
                sendDate.set("attachment", one.getFilePath());//附件
            }
            sendDate.set("alarmStatus", alarmStatus);

            QueryWrapper<BhzSwCailiao> bhzSwCailiaoQueryWrapper = new QueryWrapper<>();
            bhzSwCailiaoQueryWrapper.eq("base_guid", bhzSwBases.getGuid());
            List<BhzSwCailiao> bhzSwCailiaoList = bhzSwCailiaoService.list(bhzSwCailiaoQueryWrapper);

            List cailiaoList = new ArrayList();
            for (BhzSwCailiao bhzSwCailiao : bhzSwCailiaoList) {
                JSONObject sendCailiaoDate = new JSONObject();
                sendCailiaoDate.set("materialType", bhzSwCailiao.getCailiaoleixing());
                sendCailiaoDate.set("materialName", bhzSwCailiao.getCailiaoming());
                sendCailiaoDate.set("realityVal", bhzSwCailiao.getShijiyongliang());
                sendCailiaoDate.set("theoryVal", bhzSwCailiao.getTheoryNumber());
                sendCailiaoDate.set("alarmRule", "");
                cailiaoList.add(sendCailiaoDate);
            }
            sendDate.set("dosageList", cailiaoList);
            sendList.add(sendDate);

            JSONObject sendJsonObject = new JSONObject();
            sendJsonObject.set("serviceName","ZLPZ_ZX_SNWTBH");
            sendJsonObject.set("token","93cd2c6567594107a16b51a65bcd5a37");
            sendJsonObject.set("updateNull","true");
            sendJsonObject.set("param",sendList);
            System.out.println(sendJsonObject);
            String url = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/push";
            String back = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(sendJsonObject.toString())
                    .timeout(20000)
                    .execute().body();

            com.alibaba.fastjson.JSONObject backJson = JSON.parseObject(back);
            int result = backJson.getIntValue("result");
            if (result == 0) {
                bhzSwBases.setIszlpz(alarmStatus == 0 ? 3 : 1);
                log.info(String.format("浙路品质水稳拌合站推送成功！%s", id));
            } else {
                bhzSwBases.setIszlpz(2);
                log.info(String.format("浙路品质水稳拌合站推送失败！%s", id));
            }
            bhzSwBasesService.updateByGuid(bhzSwBases);
            pushandreturnService.saveData(id,String.valueOf(sendDate),selectsysconfigone.getRemark(),back);
        }
    }
}
