package org.jeecg.modules.job.memberDataJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.trtm.iot.memberList.entity.MemberList;
import com.trtm.iot.memberList.service.IMemberListService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName memberDataJob：
 * @Description TODO
 * @Author zml
 * @Date 2022/09/07 14:03
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class memberDataJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IMemberListService memberListService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.MEMLIST);//获取义东在场人员名单数据
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到获取在场人员名单数据定时任务的配置信息" + DateUtils.now()));
            return;
        }
        String body = HttpRequest.get("https://fjhmtd.com:15131/glaf/website/ws/dataset/api/json?id=ebbf2bcbd00c43a586662562a5318f7a")
                .header("Content-Type", "application/json")
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject = new JSONObject(body);
        if ((int)jsonObject.get("total")>0) {
            JSONArray jsonArray = (JSONArray) jsonObject.get("rows");
            for(Object o :jsonArray) {
                JSONObject jsonObject1 = new JSONObject(o);
                MemberList memberList = new MemberList();
                String personid = (String) jsonObject1.get("person_id");
                MemberList memberList1 = memberListService.getperson(personid);
                String sysorgcode = (String) jsonObject1.get("所在单位（标段）");
                String orgcode = null;
                if ("土建第01标".equals(sysorgcode)){
                    orgcode = "A05A01A03A01A01A02A01";
                }else if ("土建第02标".equals(sysorgcode)){
                    orgcode = "A05A01A03A01A01A07A01";
                }else if ("土建第03标".equals(sysorgcode)){
                    orgcode = "A05A01A03A01A01A07A02";
                }
                memberList.setName((String) jsonObject1.get("人员名称"));
                memberList.setPersonId((String) jsonObject1.get("person_id"));
                memberList.setUuid((String) jsonObject1.get("身份证号"));
                memberList.setSex((String) jsonObject1.get("性别"));
                memberList.setSysOrgCode(orgcode);
                memberList.setTeam((String) jsonObject1.get("施工班组名称"));
                if (null==memberList1){
                    boolean save = memberListService.save(memberList);
                    if (save){
                        log.info("义东在场人员名单添加成功!" + "Json数据" + memberList);
                    }else {
                        log.info("义东在场人员名单添加失败!" + "Json数据" + memberList);
                    }
                }else {
                    memberList.setId(memberList1.getId());
                    boolean b = memberListService.updateById(memberList);
                    if (b){
                        log.info("义东在场人员名单修改成功!" + "Json数据" + memberList);
                    }else {
                        log.info("义东在场人员名单修改失败!" + "Json数据" + memberList);
                    }
                }
            }
        }
    }
}
