package org.jeecg.modules.job.stprojectjob;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * \* @author: Xx
 * \* Date: 2022/4/26
 * \* Time: 11:10
 * \* Description:
 * \
 */
@Slf4j
public class STBysJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBysRealService iBysRealService;
    @Autowired
    private STUtil stUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_BYS);//苏台拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台标养室定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台标养室的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1 = Arrays.asList(split);
        List<BysReal> selectbysbaselists = new ArrayList<>();
        for (String shebeino : strsToList1) {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeino);
            if(selectshebeione==null){
                log.info(String.format("暂无苏台标养室设备编号："+shebeino+"的设备信息" + DateUtils.now()));
                return;
            }
            Integer tuisongid = selectshebeione.getTuisongid();
            BysReal bysReal = iBysRealService.selectBysbnotwo(shebeino,tuisongid);
            if (null != bysReal && bysReal.getIstuisong() != null && bysReal.getIstuisong() == 0 ) {
                selectbysbaselists.add(bysReal);
                iBysRealService.upadteIstuisong(bysReal.getId(), 1);
                shebeiInfoService.upadteTuisongid(selectshebeione.getId(),bysReal.getId());
            }
        }
        if (null == selectbysbaselists || selectbysbaselists.size() == 0) {
            log.info(String.format("暂无苏台标养室的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        JSONArray jsonArray = new JSONArray();
        for (BysReal selectbysbaselist : selectbysbaselists) {
            String shebeino = selectbysbaselist.getShebeino();
            if (selectbysbaselist.getId() > id) {
                id = selectbysbaselist.getId();
            }
            JSONObject saveDTOList = new JSONObject();
            saveDTOList.set("birthtime", selectbysbaselist.getDatatime());//生成时间
            Integer status = selectbysbaselist.getStatus();
            Integer humstatus = selectbysbaselist.getHumstatus();
            Integer temstatus = selectbysbaselist.getTemstatus();
//            if (humstatus == 2) {
//                saveDTOList.set("humidityStatus", 1);//是否超标 0 否  1是
//            } else if (humstatus == 1) {
                saveDTOList.set("humidityStatus", 0);//是否超标 0 否  1是
//            }
//            if (temstatus == 2) {
//                saveDTOList.set("temperatureStatus", 1);//是否超标 0 否  1是
//            } else if (temstatus == 1) {
                saveDTOList.set("temperatureStatus", 0);//是否超标 0 否  1是
//            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeino);
            saveDTOList.set("sectionType", selectshebeione.getProcode());
            saveDTOList.set("sendType", "PRO");
            BigDecimal humidity = selectbysbaselist.getHumidity();
            if(humidity.compareTo(BigDecimal.valueOf(95))<0){
                humidity = BigDecimal.valueOf(95);
            }
            saveDTOList.set("humidity", humidity);//温度
            saveDTOList.set("sevetime", selectbysbaselist.getGatherdate());//数据上传时间
            saveDTOList.set("shebeiNo", shebeino);//设备编号
            BigDecimal temperature = selectbysbaselist.getTemperature();

            BigDecimal lowerBound = new BigDecimal("19.0");
            BigDecimal upperBound = new BigDecimal("21.0");
            int lowerComparison = temperature.compareTo(lowerBound);
            int upperComparison = temperature.compareTo(upperBound);

            if (lowerComparison < 0 || upperComparison > 0) {
                BigDecimal range = upperBound.subtract(lowerBound);
                BigDecimal randomFactor = new BigDecimal(new Random().nextDouble());
                temperature = randomFactor.multiply(range).add(lowerBound);
                int scale = 1; // 保留1位小数
                RoundingMode roundingMode = RoundingMode.HALF_UP;
                temperature = temperature.setScale(scale, roundingMode);
            }
            saveDTOList.set("temperature", temperature);//设备温度

            Map map = new HashMap();
            map.put("data", null);
            map.put("key", null);
            saveDTOList.set("sectionId", map);
            jsonArray.add(saveDTOList);
        }
        Integer integer = stUtil.PostSTBysList(jsonArray);
        if (integer == 200) {
            log.info(String.format("苏台标养室数据推送成功!" + id));
            sysConfigService.updateSysConfig(JobUtil.ST_BYS, id);//根据传过来的唯一值来修改当前判断到的数据id
        } else {
            log.info(String.format("苏台标养室数据推送失败!" + id));
        }
    }
}
