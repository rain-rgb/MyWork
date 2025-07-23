package org.jeecg.modules.job.gumingjob;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.cunliangprocedureconfig.service.ICunliangProcedureConfigService;
import com.trtm.iot.outsource.entity.FTrial;
import com.trtm.iot.syj.entity.FYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.poi.hpsf.Array;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.binhuaijob.binhuaiUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.lab.push.job.BaseHttpRequest;
import org.jeecg.modules.job.lab.push.utils.PMUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * \* @author: zml
 * \* Date: 2022-08-17
 * \* Time: 13:08
 * \* Description: 初始化梁场信息
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class bridgePlantDtoJob extends BaseHttpRequest implements Job {
    // itype
    private final static String I_TYPE = "initBridgePlant";

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ICunliangProcedureConfigService cunliangProcedureConfigService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.PM_CSHLC);//初始化梁场信息
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到品茗数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }

//        Integer cuid = selectsysconfigone.getId();
        QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("org_type",3);
        queryWrapper.likeRight("org_code","A05A10A01");
        SysDepart one = sysDepartService.getOne(queryWrapper);
//        JSONObject bridgePlantDto = JSONUtil.createObj();
//        bridgePlantDto.set("companyId", 11353);
//        bridgePlantDto.set("projectId", 2354);
//        bridgePlantDto.set("plantId", one.getOrgCode());
//        bridgePlantDto.set("plantName", one.getDepartName());
//        bridgePlantDto.set("description", "滨淮高速智慧梁场");
        List<Map> list = new ArrayList<Map>();
        Map<String, Object> map = new HashMap<>();
        map.put("companyId", 23800);
        map.put("projectId", 16675);
        map.put("plantId", one.getId());
        map.put("plantName", one.getDepartName());
        map.put("description", "金华高速智慧梁场");
        list.add(map);
//        bridgePlantDto.set("data",map);

        System.out.println(list);
        String result = requestDataProcessing(list, I_TYPE, 1);
        System.out.println("结果：" + result);
       //sysConfigService.updateSysConfig(JobUtil.SYS_WYSY, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}