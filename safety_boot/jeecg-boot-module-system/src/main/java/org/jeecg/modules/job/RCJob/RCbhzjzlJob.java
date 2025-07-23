package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShigongphbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCbhzjzlJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCBHZ_JZL);//瑞仓拌合站浇筑令数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓拌合站浇筑令数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String token = selectsysconfigone.getToken();
        List<Bhzrenwudan> list = bhzrenwudanService.selectLists(curid);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无瑞仓拌合站浇筑令未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (Bhzrenwudan bhzrenwudan : list) {
            id = bhzrenwudan.getId();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("station", bhzrenwudan.getStation());
            jsonObject1.set("Code", bhzrenwudan.getCode());
            jsonObject1.set("DatTim", bhzrenwudan.getDattim());
            jsonObject1.set("Attribute", bhzrenwudan.getAttribute());
            jsonObject1.set("Recipe", bhzrenwudan.getRecipe());
            jsonObject1.set("Recipes", bhzrenwudan.getRecipes());
            jsonObject1.set("Contract", bhzrenwudan.getContract());
            jsonObject1.set("Customer", bhzrenwudan.getCustomer());
            jsonObject1.set("ProjName", bhzrenwudan.getProjname());
            jsonObject1.set("ProjType", bhzrenwudan.getProjtype());
            jsonObject1.set("ProjGrade", bhzrenwudan.getProjgrade());
            jsonObject1.set("ProjArea", bhzrenwudan.getProjarea());
            jsonObject1.set("ProjAdr", bhzrenwudan.getProjadr());
            jsonObject1.set("Distance", bhzrenwudan.getDistance());
            jsonObject1.set("ConsPos", bhzrenwudan.getConspos());
            jsonObject1.set("Pour", bhzrenwudan.getPour());
            jsonObject1.set("Variety", bhzrenwudan.getVariety());
            jsonObject1.set("BetLev", bhzrenwudan.getBetlev());
            jsonObject1.set("Filter", bhzrenwudan.getFilter());
            jsonObject1.set("Freeze", bhzrenwudan.getFreeze());
            jsonObject1.set("Lands", bhzrenwudan.getLands());
            jsonObject1.set("Cement", bhzrenwudan.getCement());
            jsonObject1.set("Stone", bhzrenwudan.getStone());
            jsonObject1.set("BnSize", bhzrenwudan.getBnsize());
            jsonObject1.set("AddLiq", bhzrenwudan.getAddliq());
            jsonObject1.set("Request", bhzrenwudan.getRequest());
            jsonObject1.set("MixLast", bhzrenwudan.getMixlast());
            jsonObject1.set("Mete", bhzrenwudan.getMete());
            jsonObject1.set("BegTim", bhzrenwudan.getBegtim());
            jsonObject1.set("EndTim", bhzrenwudan.getEndtim());
            jsonObject1.set("Attamper", bhzrenwudan.getAttamper());
            jsonObject1.set("Flag", bhzrenwudan.getFlag());
            jsonObject1.set("Note", bhzrenwudan.getNote());
            jsonObject1.set("create_by", bhzrenwudan.getCreateBy());
            jsonObject1.set("sys_org_code", bhzrenwudan.getSysOrgCode());
            jsonObject1.set("isdel", bhzrenwudan.getIsdel());
            jsonObject1.set("status", bhzrenwudan.getStatus());
            jsonObject1.set("treeid", bhzrenwudan.getTreeid());
            jsonObject1.set("isfinish", bhzrenwudan.getIsfinish());
            jsonObject1.set("orderno", bhzrenwudan.getOrderno());
//            System.out.println(jsonObject1);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/hnt/s/IBhzrenwudanUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓拌合站浇筑令数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCBHZ_JZL, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else if(code == 201){
                log.info(String.format("瑞仓拌合站浇筑令数据推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCBHZ_JZL, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞仓拌合站浇筑令数据推送失败!" + id));
            }

        }
    }
}
