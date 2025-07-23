package org.jeecg.modules.job.ydJob.jizhibao;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.xkcoding.http.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName lqJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/4/19 13:46
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class lqJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzLqBasesService lqBasesService;
    @Autowired
    private IBhzLqCailiaoService lqCailiaoService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JZB_LQ);//沥青拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东沥青拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东沥青拌合站的设备" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 时间格式化器
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzLqBases> bhzLqBases = lqBasesService.selectList2(shebeilist, curid);
        for (BhzLqBases bhzLqBase : bhzLqBases) {
            id = bhzLqBase.getId();
            JSONObject sendDate = new JSONObject();

            sendDate.set("mixtureTemperature", StringUtil.isNotEmpty(bhzLqBase.getLiqingwd()) ? Double.parseDouble(String.format("%.1f", Double.parseDouble(bhzLqBase.getLiqingwd()))) : 0.0);
            sendDate.set("mixtureType", bhzLqBase.getHunheliaoid());
            sendDate.set("oilStoneRatio", bhzLqBase.getYoushibi());
            sendDate.set("outDeviceId", "70F76138-ADE5-0E39-C35A-615927D866A4");
            sendDate.set("outId", bhzLqBase.getGuid());
            sendDate.set("outTemperature", StringUtil.isNotEmpty(bhzLqBase.getChuliaowd()) ? Double.parseDouble(String.format("%.1f", Double.parseDouble(bhzLqBase.getChuliaowd()))) : 0.0);
            sendDate.set("stoneTemperature", StringUtil.isNotEmpty(bhzLqBase.getGuliaowd()) ? Double.parseDouble(String.format("%.1f", Double.parseDouble(bhzLqBase.getGuliaowd()))) : 0.0);
            sendDate.set("timestamp", bhzLqBase.getChuliaoshijian());
            sendDate.set("yield", String.format("%.2f", bhzLqBase.getZongchanliang()));

            //材料表
            QueryWrapper<BhzLqCailiao> lqCailiaoQueryWrapper = new QueryWrapper<>();
            lqCailiaoQueryWrapper.eq("base_guid",bhzLqBase.getGuid());
            List<BhzLqCailiao> lqCailiaoList = lqCailiaoService.list(lqCailiaoQueryWrapper);
            for (BhzLqCailiao bhzLqCailiao : lqCailiaoList) {
                String cailiaoming = bhzLqCailiao.getCailiaoming();
                if ("石料1".equals(cailiaoming)){
                    sendDate.set("col1", bhzLqCailiao.getShijiyongliang());
                    sendDate.set("col1Theory", bhzLqCailiao.getTheoryNumber());
                }
                if ("石料2".equals(cailiaoming)){
                    sendDate.set("col2", bhzLqCailiao.getShijiyongliang());
                    sendDate.set("col2Theory", bhzLqCailiao.getTheoryNumber());
                }
                if ("石料3".equals(cailiaoming)){
                    sendDate.set("col3", bhzLqCailiao.getShijiyongliang());
                    sendDate.set("col3Theory", bhzLqCailiao.getTheoryNumber());
                }
                if ("石料4".equals(cailiaoming)){
                    sendDate.set("col4", bhzLqCailiao.getShijiyongliang());
                    sendDate.set("col4Theory", bhzLqCailiao.getTheoryNumber());
                }
                if ("石料5".equals(cailiaoming)){
                    sendDate.set("col5", bhzLqCailiao.getShijiyongliang());
                    sendDate.set("col5Theory", bhzLqCailiao.getTheoryNumber());
                }
                if ("石料6".equals(cailiaoming)){
                    sendDate.set("col6", bhzLqCailiao.getShijiyongliang());
                    sendDate.set("col6Theory", bhzLqCailiao.getTheoryNumber());
                }
                if ("粉料1".equals(cailiaoming)){
                    sendDate.set("col7", bhzLqCailiao.getShijiyongliang());
                    sendDate.set("col7Theory", bhzLqCailiao.getTheoryNumber());
                }
                if ("粉料2".equals(cailiaoming)){
                    sendDate.set("col8", bhzLqCailiao.getShijiyongliang());
                    sendDate.set("col8Theory", bhzLqCailiao.getTheoryNumber());
                }
                if ("沥青".equals(cailiaoming)){
                    sendDate.set("col9", bhzLqCailiao.getShijiyongliang());
                    sendDate.set("col9Theory", bhzLqCailiao.getTheoryNumber());
                }
            }
            sendDate.set("engineerPart", bhzLqBase.getPoureLocation());
            sendDate.set("productNo", bhzLqBase.getGuid());

            List<JSONObject> list = new ArrayList();
            list.add(sendDate);

            JSONObject dataJson = new JSONObject();
            dataJson.set("list", list);
            dataJson.set("appId", "c0017538-d079-48c6-a23a-123a12ec9691");
            dataJson.set("appSecret", "44FE35FC-EDE7-42E1-B1E1-72007E2A13C7");

            String url = "http://gateway.jizhibao.com.cn/iot/message/lqbhz/push";
            String back = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(dataJson))
                    .timeout(20000)
                    .execute().body();
            JSONObject response = new JSONObject(back);
            int code = response.getInt("code");
            String msg = response.getStr("msg");
            if (code == 30000) {
                log.info(String.format("义东沥青拌合站推送成功!" + id));
            }else {
                log.info(String.format("义东沥青拌合站推送成功!" + id+msg));
            }
            sysConfigService.updateSysConfig(JobUtil.JZB_LQ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
