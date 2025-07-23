package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.hntbhz.vo.BhzCementBasePage;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import com.trtm.iot.trhnthtm.service.ITrHnthtMService;
import com.trtm.iot.trhnthts.service.ITrHnthtSService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCJhjdBhzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_JHJD_BHZ);//47金华基地拌合站推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到47金华基地拌合站推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
//        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输47金华基地拌合站推送的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> list = bhzCementBaseService.selectRCJHJD(curid, shebeilist);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无47金华基地拌合站未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (BhzCementBase bhzCementBase : list) {
            id = bhzCementBase.getId();
            BhzCementBasePage bhzCementBasePage = new BhzCementBasePage();
            BeanUtils.copyProperties(bhzCementBase, bhzCementBasePage);
            bhzCementBasePage.setAlertstate(0);
            bhzCementBasePage.setRenwudanstatus(0);
            List<BhzCementDetail> bhzCementDetails = bhzCementDetailService.selectByBatchNo(bhzCementBase.getBatchNo());
            if (bhzCementDetails.size() > 0) {
                bhzCementBasePage.setBhzCementDetailList(bhzCementDetails);
            }
            BhzCementOverHandler selectlist = bhzCementOverHandlerService.selectlist(bhzCementBase.getBatchNo());
            if (selectlist != null) {
                bhzCementBasePage.setBhzCementOverHandler(selectlist);
            }
            String aa= JSON.toJSONString(bhzCementBasePage, SerializerFeature.WRITE_MAP_NULL_FEATURES);
            com.alibaba.fastjson.JSONObject object= JSON.parseObject(aa);
            System.out.println(bhzCementBasePage);
            String back = HttpRequest.post("http://47.96.161.157/jeecg-boot/hntbhz/bhzCementBase/addOpen")
                    .body(String.valueOf(object))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("47金华基地拌合站数据推送成功" + DateUtils.now()));
                sysConfigService.updateSysConfig(JobUtil.RC_JHJD_BHZ, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("47金华基地拌合站数据推送失败" + DateUtils.now()));
            }
        }
    }
}
