package org.jeecg.modules.job.jgdatapush;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jgdatapush.vo.BhzCementBasePageJG;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JGbhzPushJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZJJG_BHZ_PUSH);//瑞仓拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓混凝土拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();

        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("未配置交工混凝土拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBasess = bhzCementBaseService.selectListToSHYJ( shebeilist,curid);
        if (null == bhzCementBasess || bhzCementBasess.size() == 0) {
            log.info(String.format("暂无混凝土拌合站的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (BhzCementBase bhzCementBases : bhzCementBasess) {
            BhzCementBasePageJG bhzPush = new BhzCementBasePageJG();

            id= bhzCementBases.getId();
            try {
                BeanUtils.copyProperties(bhzPush, bhzCementBases );
                if(StringUtils.isNotBlank(bhzCementBases.getShebeiNo())){
                    ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(bhzCementBases.getShebeiNo());
                    if(selectshebeione != null){
                        bhzPush.setDeviceName(selectshebeione.getSbname());
                    }
                }

                List<BhzCementDetail> selectcementlist = bhzCementDetailService.selectcementlist(bhzPush.getBatchNo());
                if (selectcementlist.size() > 0) {
                    bhzPush.setBhzCementDetailList(selectcementlist);
                }

                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(bhzCementBases.getShebeiNo());
                bhzPush.setOrgCode(selectshebeione.getTunnelId());
                bhzPush.setOrgName(selectshebeione.getTunnelName());
                JSON parse = JSONUtil.parse(bhzPush);
//                String back = HttpRequest.post("http://115.236.10.10:8081/zjjg-iot-test/mixing_station/saveMixingStationData") // 测试地址
                String back = HttpRequest.post("http://115.236.10.10:8081/zjjg-iot/mixing_station/saveMixingStationData") // 正式地址
//                        .header("client-code", "gaoxun_test")
                        .header("client-code", "gaoxun")
                        .body(String.valueOf(parse))
                        .execute()
                        .body();
                JSONObject jsonObject2 = new JSONObject(back);
                Integer code = (Integer) jsonObject2.get("code");
                if (code == 200) {
                    log.info(String.format("混凝土拌合站数据推送成功!" + id));
                    sysConfigService.updateSysConfig(JobUtil.ZJJG_BHZ_PUSH, id);//根据传过来的唯一值来修改当前判断到的数据id
                } else {
//                    log.info(String.format("混凝土拌合站数据推送失败!"+ parse));
                    log.info(String.format("混凝土拌合站数据推送失败!"+jsonObject2 ));
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        }


    }

}
