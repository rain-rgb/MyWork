package org.jeecg.modules.job.yongjinqushang;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class WzLiaoCangPushData implements Job {
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private ISysConfigService sysConfigService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZJJG_LC);//
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sysOrgCode = jsonObject.getStr("sysOrgCode");
        String[] codes = sysOrgCode.split(",");
        String[] orgCodes = jsonObject.getStr("orgCode").split(",");
        String[] orgNames = jsonObject.getStr("orgName").split(",");
        for( int i =0 ;i<codes.length;i++){
            QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
            queryWrapper.likeRight("sys_org_code",codes[i]);
             queryWrapper.in("cailiaono", Arrays.asList("1,2,3,6,7,8".split(",")));
            queryWrapper.isNotNull( "weight" );
           // queryWrapper.last( " and  ")
            Page<Wzliaocang> page = new Page<Wzliaocang>(1, 100);
            IPage<Wzliaocang> pageList = wzliaocangService.page(page, queryWrapper);
            ArrayList sendDataList = new ArrayList<>();

            for(Wzliaocang wz :pageList.getRecords()){
                JSONObject sendObject = JSONUtil.createObj();
                sendObject.set("orgCode", orgCodes[i]);
                sendObject.set("orgName", orgNames[i]);
                sendObject.set("warehouseName", wz.getName());
                sendObject.set("warehouseStatus", wz.getLiaocangStatus());
                sendObject.set("materialName", wz.getCailiaoname());
                sendObject.set("materialSpec", wz.getGuigexinghao());
                sendObject.set("materialWeight", wz.getWeight());
                sendObject.set("materialNo", wz.getLiaoweino());
                sendObject.set("unit", "吨");
                sendObject.set("inventory", wz.getKucun());
                sendObject.set("batchEntryWeight", wz.getPicizhong());
                sendObject.set("batchNo", wz.getPici());
                sendObject.set("manufacturer", wz.getChangjia());
                sendObject.set("entryTime",format.format(new Date()) );
                sendObject.set("designCapacity", wz.getDesigncapacity());
                sendDataList.add(sendObject);

            }

            // 测试地址
            String urlcs = "http://121.41.26.120:8081/zjjg-iot-test/v1/powder-tank/savePowderTank";
            // 正式地址
            String url = "http://121.41.26.120:8081/zjjg-iot/v1/powder-tank/savePowderTank";
            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .header("client-code","gaoxun")
                    .body(String.valueOf(sendDataList))
                    .execute()
                    .body();
            System.out.println(body);
            JSONObject jsonObject1 = new JSONObject(body);
            Integer returncode = (Integer) jsonObject1.get("code");
            if (returncode == 200) {
                log.info(String.format("数据推送成功!" + sendDataList ));
            } else {
                log.info(String.format("推送失败!" + "erro" + jsonObject1.get("msg")  ));
            }
            pushandreturnService.saveData(1,String.valueOf(sendDataList),"",body);
        }


    }
}
