package org.jeecg.modules.job.hntbhzjob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * \* @author: zml
 * \* Date: 2022/6/21
 * \* Time: 14:00
 * \* Description: 砼拌合站数据过滤
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TbhzDatafilterJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private JobUtil jobUtil;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.TBHZ_FILTER); //砼拌合站数据过滤
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到砼拌合站数据过滤定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要过滤砼拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> selecthntbhzones = bhzCementBaseService.selecthntbhzlists(curid, 0,shebeilist);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info("砼拌合站暂无未过滤的数据" + DateUtils.now());
            return;
        }
        int id = 0;
        for (BhzCementBase bhzCementBase:selecthntbhzones){
            id = bhzCementBase.getId();
            try {
                if ("A05A07A02A01A01A01A01A01_BHZ_1154".equals(bhzCementBase.getShebeiNo()) && !bhzCementBase.getProjectName().contains("256省道")){
                    BhzCementBase bhzCementBase1 = new BhzCementBase();
                    bhzCementBase1.setId(id);
                    bhzCementBase1.setIsfilter(1);
                    bhzCementBase1.setShebeiNo(bhzCementBase.getShebeiNo()+"_FEIQI");
                    bhzCementBaseService.updateById(bhzCementBase1);
                }
                if ("YFBHJ_JG01".equals(bhzCementBase.getShebeiNo()) && !bhzCementBase.getProjectName().contains("204国道阜宁花园")){
                    BhzCementBase bhzCementBase1 = new BhzCementBase();
                    bhzCementBase1.setId(id);
                    bhzCementBase1.setIsfilter(1);
                    bhzCementBase1.setShebeiNo(bhzCementBase.getShebeiNo()+"_FEIQI");
                    bhzCementBaseService.updateById(bhzCementBase1);
                }
                if ("A05A07A01A01A02A02_BHZ_1173".equals(bhzCementBase.getShebeiNo()) && !bhzCementBase.getProjectName().contains("G204国道阜宁至亭湖段")){
                    BhzCementBase bhzCementBase1 = new BhzCementBase();
                    bhzCementBase1.setId(id);
                    bhzCementBase1.setIsfilter(1);
                    bhzCementBase1.setShebeiNo(bhzCementBase.getShebeiNo()+"_FEIQI");
                    bhzCementBaseService.updateById(bhzCementBase1);
                }
                if ("A05A07A01A01A02A01_BHZ_1223".equals(bhzCementBase.getShebeiNo()) && !bhzCementBase.getProjectName().contains("盐城204国道亭湖段")){
                    BhzCementBase bhzCementBase1 = new BhzCementBase();
                    bhzCementBase1.setId(id);
                    bhzCementBase1.setIsfilter(1);
                    bhzCementBase1.setShebeiNo(bhzCementBase.getShebeiNo()+"_FEIQI");
                    bhzCementBaseService.updateById(bhzCementBase1);
                }
                if ("A05A07A01A01A03A01_BHZ_1222".equals(bhzCementBase.getShebeiNo()) && !bhzCementBase.getProjectName().contains("G204阜宁至亭湖段")){
                    BhzCementBase bhzCementBase1 = new BhzCementBase();
                    bhzCementBase1.setId(id);
                    bhzCementBase1.setIsfilter(1);
                    bhzCementBase1.setShebeiNo(bhzCementBase.getShebeiNo()+"_FEIQI");
                    bhzCementBaseService.updateById(bhzCementBase1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("砼拌合站数据过滤!   时间:" + DateUtils.now());
        }
        sysConfigService.updateSysConfig(JobUtil.TBHZ_FILTER, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
