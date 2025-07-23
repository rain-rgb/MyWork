package org.jeecg.modules.job.kezhujob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName huangjingJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/11/4 14:34
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class huangjingJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IDevicehistoryService devicehistoryService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.KZ_HJJC);//柯诸环境检测
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到柯诸环境检测的配置信息" + DateUtils.now()));
            return;
        }

        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输柯诸环境检测的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        //环境检测主表
        List<Devicehistory> selectlistdata = devicehistoryService.selectlistdatatop1(shebeilist,curid);
        if (null == selectlistdata || selectlistdata.size() == 0) {
            log.info(String.format("暂无柯诸环境检测未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Devicehistory devicehistory : selectlistdata) {
            id = devicehistory.getId();
            JSONObject sendDate = new JSONObject();
//            sendDate.set("equCode",devicehistory.getDevaddr());

            QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
            sysDictQueryWrapper.eq("dict_code", "kzhjjcsb");
            SysDict one = sysDictService.getOne(sysDictQueryWrapper);
            String id1 = one.getId();

            QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
            sysDictItemQueryWrapper.eq("dict_id", id1);
            sysDictItemQueryWrapper.eq("item_text", devicehistory.getDevaddr());
            SysDictItem one1 = sysDictItemService.getOne(sysDictItemQueryWrapper);

            sendDate.set("equCode", one1.getItemValue());
            sendDate.set("equStatus", 0);
            sendDate.set("collectionDt", sdf.format(devicehistory.getTimevalue()));
            sendDate.set("collectionDuration", "300");
            sendDate.set("pMTwoValue", devicehistory.getPm25());
            sendDate.set("pMTenValue", devicehistory.getPm10());
            sendDate.set("temperature", devicehistory.getTemperature());
            sendDate.set("humidity", devicehistory.getHumidity());
            sendDate.set("windSpeed", devicehistory.getWindspeed());
            sendDate.set("windDirection", devicehistory.getWinddirection());
            sendDate.set("noise", devicehistory.getNoise());
            sendDate.set("mainId", devicehistory.getId());
            sendDate.set("outterId", devicehistory.getId());
            sendDate.set("proName", "/");
            sendDate.set("oper", "/");
            List sendList = new ArrayList();
            sendList.add(sendDate);
            String body = HttpRequest.post("https://kzgs.bim001.cn/bims/rest/environmentInfo/saveEnvironmentInfo.jo")
                    .body(String.valueOf(sendList))
                    .execute()
                    .body();

            JSONObject jsonObject1 = new JSONObject(body);
            String data = jsonObject1.get("data").toString();
            if ("1".equals(data)) {
                log.info(String.format("柯诸环境检测推送成功!" + id + "Json数据" + sendDate));
            } else {
                log.info(String.format("柯诸环境检测推送失败!" + id + "Json数据" + sendDate));
            }
            sysConfigService.updateSysConfig(JobUtil.KZ_HJJC, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
