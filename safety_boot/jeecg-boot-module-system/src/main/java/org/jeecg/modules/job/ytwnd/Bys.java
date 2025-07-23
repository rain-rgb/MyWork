package org.jeecg.modules.job.ytwnd;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.syj.entity.FWangnj;
import com.trtm.iot.syj.entity.FsYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.IFWangnjService;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.syjoverhandler.entity.SyjOverHandler;
import com.trtm.iot.syjoverhandler.service.ISyjOverHandlerService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.xkcoding.http.util.StringUtil;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName syjJob：
 * @Description TODO
 * @Author 55314
 * @Date 2024/3/26 16:44
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class Bys implements Job {


    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Autowired
    private IBysRealService bysRealService;

    private String bysurl = "http://183.247.216.148:7030/tz_server/standardData/pushStandardData";


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YTW_ND_BYS);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到甬台温南段标养室数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }

        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        String shebeilist = jsonObject.getStr("shebeilist");
       List sblist = Arrays.asList(shebeilist.split(","));
        QueryWrapper<BysReal> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("shebeiNo",sblist );
        queryWrapper.gt("id",selectsysconfigone.getCurid());
        queryWrapper.orderByAsc("gatherdate");
        queryWrapper.last(" limit " + selectsysconfigone.getCurdate());
        List<BysReal> list = bysRealService.list(queryWrapper);
         Integer  cid = selectsysconfigone.getCurid();
        SimpleDateFormat aDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject jsonObject2 = new JSONObject();
        String body2 = "";
        for(BysReal bysone : list){
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("device_code",bysone.getShebeino());
            jsonObject1.set("date",aDate.format(bysone.getGatherdate()));
            jsonObject1.set("humidity",bysone.getHumidity());
            jsonObject1.set("temperature",bysone.getTemperature());
            jsonObject1.set("direction",bysone.getShebeino());
            jsonObject1.set("project_code", "TZ001");

            String body = HttpRequest.post(bysurl)
                    .header("Content-Type", "application/json")
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject returnObject = new JSONObject(body);
            Integer codes = (Integer) returnObject.get("status");
            if (codes == 200) {
                log.info(String.format("甬台温南段标养室数据推送成功!"+ bysone.getShebeino()));
            } else {
                log.info(String.format("甬台温南段标养室数据推送失败!"+ bysone.getShebeino()) );
            }

            cid = bysone.getId();
            jsonObject2 = jsonObject1;
            body2 = body;
        }
        pushandreturnService.saveData(cid,String.valueOf(jsonObject2),"甬台温南段标养室",body2);
        sysConfigService.updateSysConfig(JobUtil.YTW_ND_BYS, cid);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
