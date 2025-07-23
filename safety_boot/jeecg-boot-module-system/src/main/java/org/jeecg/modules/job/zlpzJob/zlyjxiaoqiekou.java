package org.jeecg.modules.job.zlpzJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yj.entity.TrYajiang;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import com.trtm.iot.zlpz.entity.ZlpzProject;
import com.trtm.iot.zlpz.service.IZlpzProjectService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName zlyjxiaoqiekou：
 * @Description TODO
 * @Author 55314
 * @Date 2023/7/13 15:35
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zlyjxiaoqiekou implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IZlpzProjectService zlpzProjectService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZLPZ_ZLYJXQK);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到定时任务的配置信息" + DateUtils.now()));
            return;
        }
        // 获取当前日期
        Date currentDate = new Date();
        // 定义日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 格式化当前日期为指定格式
        String currentDateString = dateFormat.format(currentDate);

        QueryWrapper<ZlpzProject> zlpzProjectQueryWrapper = new QueryWrapper<>();
        zlpzProjectQueryWrapper.isNotNull("orgcode");
        List<ZlpzProject> zlpzProjectList = zlpzProjectService.list(zlpzProjectQueryWrapper);
        for (ZlpzProject zlpzProject : zlpzProjectList) {
            String xmid = zlpzProject.getXmid();
            String orgcode = zlpzProject.getOrgcode();
            //查询orgcode下张拉设备
            QueryWrapper<ShebeiInfo> shebeiInfoQueryWrapper = new QueryWrapper<>();
            shebeiInfoQueryWrapper.eq("sbtype", 9);
            shebeiInfoQueryWrapper.likeRight("sys_org_code", orgcode);
            List<ShebeiInfo> shebeiInfozlList = shebeiInfoService.list(shebeiInfoQueryWrapper);
            String shebeiIdzls = shebeiInfozlList.stream()
                    .map(ShebeiInfo::getSbjno)
                    .map(id -> "'" + id + "'")
                    .collect(Collectors.joining(",", "(", ")"));
            QueryWrapper<TrZhanglaXxb> zhanglaXxbQueryWrapper = new QueryWrapper<>();
            zhanglaXxbQueryWrapper.select("count(1) as id");
            zhanglaXxbQueryWrapper.in("shebeibianhao",shebeiIdzls);
            zhanglaXxbQueryWrapper.likeRight("sgsj", currentDateString);
            TrZhanglaXxb one = zhanglaXxbService.getOne(zhanglaXxbQueryWrapper);
            //当日张拉条数
            Integer zlts = one.getId();

            //查询orgcode下压浆设备
            QueryWrapper<ShebeiInfo> shebeiInfoyjQueryWrapper = new QueryWrapper<>();
            shebeiInfoyjQueryWrapper.eq("sbtype", 10);
            shebeiInfoyjQueryWrapper.likeRight("sys_org_code", orgcode);
            List<ShebeiInfo> shebeiInfoyjList = shebeiInfoService.list(shebeiInfoyjQueryWrapper);
            String shebeiyjIds = shebeiInfoyjList.stream()
                    .map(ShebeiInfo::getSbjno)
                    .map(id -> "'" + id + "'")
                    .collect(Collectors.joining(",", "(", ")"));
            QueryWrapper<TrYajiangM> yajiangMQueryWrapper = new QueryWrapper<>();
            yajiangMQueryWrapper.select("count(1) as id");
            yajiangMQueryWrapper.in("yjsbbh",shebeiyjIds);
            yajiangMQueryWrapper.likeRight("yjsj", currentDateString);
            TrYajiangM oneyj = yajiangMService.getOne(yajiangMQueryWrapper);
            //当日压浆条数
            Integer yjts = oneyj.getId();

            List sendList = new ArrayList();
            JSONObject sendDate = new JSONObject();
            sendDate.set("projectId", xmid);
            sendDate.set("rq", currentDateString);
            sendDate.set("zlsl", zlts);
            sendDate.set("yjsl", yjts);
            sendList.add(sendDate);

            JSONObject sendJsonObject = new JSONObject();
            sendJsonObject.set("serviceName", "ZLPZ_ZXN_ZLYJXQK");
            sendJsonObject.set("token", "93cd2c6567594107a16b51a65bcd5a37");
            sendJsonObject.set("updateNull", "true");
            sendJsonObject.set("param", sendList);
            System.out.println(sendJsonObject);

            String url = "http://223.4.78.87:21086/dtas-server/api/service/push";
            String back = HttpRequest.post(url)
                    .body(sendJsonObject.toString())
                    .timeout(20000)
                    .execute().body();

            pushandreturnService.saveData(100, String.valueOf(sendJsonObject), selectsysconfigone.getRemark(), back);

            com.alibaba.fastjson.JSONObject backJson = JSON.parseObject(back);
            int result = backJson.getIntValue("result");
            if (result == 0) {
                log.info(String.format("浙路品质当日张拉压浆情况推送成功！%s", xmid));
            } else {
                log.info(String.format("浙路品质当日张拉压浆情况推送失败！%s", xmid));
            }
        }
    }
}
