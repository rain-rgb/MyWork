package org.jeecg.modules.job.huacejob;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hc_machine.entity.HcMachine;
import com.trtm.iot.hc_machine.service.IHcMachineService;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.service.IHcTokenService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName machineJob：
 * @Description 获取华测路面设备
 * @Author 55314
 * @Date 2023/4/23 14:57
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class machineJob implements Job {

    private static String url = "https://dp.huace.cn/digitalPlatform/open/api/base/machine.shtml";
    @Autowired
    private IHcProjectService projectService;
    @Autowired
    private IHcTokenService tokenService;
    @Autowired
    private IHcSectionService sectionService;
    @Autowired
    private IHcMachineService machineService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("获取华测路面设备任务开始！");
        List<HcSection> sectionList = sectionService.list();
        for (HcSection hcSection : sectionList) {
            String pjid = hcSection.getPjid();
            String sectionid = hcSection.getSectionid();

            QueryWrapper<HcToken> tokenQueryWrapper = new QueryWrapper<>();
            tokenQueryWrapper.like("projectid", pjid);
            HcToken hcToken = tokenService.getOne(tokenQueryWrapper);
            if (null == hcToken){
                continue;
            }
            String token = hcToken.getToken();
            String bdorgcode = hcToken.getOrgcode();

            JSONObject sendDate = new JSONObject();

            sendDate.put("projectId", pjid);
            sendDate.put("sectionId", sectionid);

            String result = HttpRequest.post(url)
                    .header("token", token)
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();

            JSONObject jsonObject = JSONObject.parseObject(result);
            Integer code = jsonObject.getInteger("code");
            if (0 != code){
                continue;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);

                String machineId = object.getString("machineId");

                HcMachine hcMachine = new HcMachine();

                QueryWrapper<HcMachine> hcMachineQueryWrapper = new QueryWrapper<>();
                hcMachineQueryWrapper.eq("machineId",machineId);
                HcMachine one = machineService.getOne(hcMachineQueryWrapper);
                if (null != one){
                    hcMachine.setId(one.getId());
                }else {
                    hcMachine.setSectionid(sectionid);
                }
                hcMachine.setSjid(object.getString("id"));
                hcMachine.setMachineid(machineId);
                hcMachine.setTerminalsncode(object.getString("terminalSnCode"));
                hcMachine.setMachinesize(object.getDouble("machineSize"));
                hcMachine.setMachinename(object.getString("machineName"));
                hcMachine.setMachinetypecode(object.getInteger("machineTypeCode"));
                hcMachine.setMachinetypename(object.getString("machineTypeName"));
                hcMachine.setTmstarttime(object.getDate("tmStartTime"));
                hcMachine.setTmendtime(object.getDate("tmEndTime"));
                hcMachine.setTmwireheight(object.getDouble("tmWireHeight"));
                hcMachine.setTmantemode(object.getInteger("tmAnteMode"));
                hcMachine.setTmwheelwidthsingle(object.getDouble("tmWheelWidthSingle"));
                hcMachine.setTmdistancefromwheel(object.getDouble("tmDistanceFromWheel"));
                hcMachine.setTmdistancefromcenter(object.getDouble("tmDistanceFromCenter"));
                hcMachine.setTmcentertomainx(object.getDouble("tmCenterToMainX"));
                hcMachine.setTmcentertomainy(object.getDouble("tmCenterToMainY"));
                hcMachine.setTmvicetomainx(object.getDouble("tmViceToMainX"));
                hcMachine.setTmvicetomainy(object.getDouble("tmViceToMainY"));
                hcMachine.setTmwheelwidthdouble(object.getDouble("tmWheelWidthDouble"));
                hcMachine.setPjid(pjid);
                hcMachine.setOrgcode(bdorgcode);

                machineService.saveOrUpdate(hcMachine);
                log.info("获取华测路面设备成功！");
            }
        }
    }
}
