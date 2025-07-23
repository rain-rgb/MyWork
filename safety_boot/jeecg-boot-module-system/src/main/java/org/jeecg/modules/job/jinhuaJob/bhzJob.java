package org.jeecg.modules.job.jinhuaJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.hntbhz.vo.BhzCementBasePage;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName bhzJob
 * @Author
 * @Date 2024/12/2 14:00
 * @Version
 * @Description z平台金华基地推送47甬金衢上
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class bhzJob  implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JHJD_YJQSBHZ);//瑞苍拌合站质检资料推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓配合比定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓配合比的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBaseList = bhzCementBaseService.selectListjhjd(shebeilist);
        if (null == bhzCementBaseList || bhzCementBaseList.size() == 0) {
            log.info(String.format("暂无拌合站未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (BhzCementBase bhzCementBaseOne : bhzCementBaseList) {
            id = bhzCementBaseOne.getId();
            BhzCementBasePage bhzCementBasePage = new BhzCementBasePage();
            BeanUtils.copyProperties(bhzCementBaseOne, bhzCementBasePage);
            bhzCementBasePage.setAlertstate(0);
            bhzCementBasePage.setShebeiNo("yjqs3bbh03");
            List<BhzCementDetail> bhzCementDetails = bhzCementDetailService.selectByBatchNo(bhzCementBaseOne.getBatchNo());
            if (bhzCementDetails.size() > 0) {
                for (BhzCementDetail bhzCementDetail : bhzCementDetails) {
                    bhzCementDetail.setShebeino("yjqs3bbh03");
                }
                bhzCementBasePage.setBhzCementDetailList(bhzCementDetails);
            }
            String aa= JSON.toJSONString(bhzCementBasePage, SerializerFeature.WRITE_MAP_NULL_FEATURES);
            com.alibaba.fastjson.JSONObject object= JSON.parseObject(aa);

            String back = HttpRequest.post("http://47.96.161.157/jeecg-boot/hntbhz/bhzCementBase/addOpen")
                    .body(String.valueOf(object))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("47金华基地拌合站数据推送成功" + DateUtils.now()));
                bhzCementBaseOne.setIssend(1);
            } else {
                log.info(String.format("47金华基地拌合站数据推送失败" + DateUtils.now()));
                bhzCementBaseOne.setIssend(2);
            }
            bhzCementBaseService.updateById(bhzCementBaseOne);
            sysConfigService.updateSysConfig(JobUtil.JHJD_YJQSBHZ, id);//根据传过来的唯一值来修改当前判断到的数据id
            pushandreturnService.saveData(id, String.valueOf(object), selectsysconfigone.getRemark(), back);
        }
    }
}