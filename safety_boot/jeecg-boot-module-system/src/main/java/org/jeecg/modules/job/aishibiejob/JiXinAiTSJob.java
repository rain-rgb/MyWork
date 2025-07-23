package org.jeecg.modules.job.aishibiejob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.aiwarnmsg.entity.AiWarnMsg;
import com.trtm.iot.aiwarnmsg.service.IAiWarnMsgService;
import com.trtm.iot.aiwarnmsgcfg.entity.AiWarnMsgCfg;
import com.trtm.iot.aiwarnmsgcfg.service.IAiWarnMsgCfgService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.mapper.ShebeiInfoMapper;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JiXinAiTSJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IAiWarnMsgCfgService aiWarnMsgCfgService;
    @Autowired
    private IAiWarnMsgService aiWarnMsgService;
    @Autowired
    private ISysDepartService sysDepartService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.JXGS_AITS);//济新高速#AI识别数据
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到济新高速JXSG-2标#焦济1标#AI识别定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输济新高速JXSG-2标#焦济1标#AI识别的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
//        String[] split = shebeilist.split(",");
//        List<String> strsToList1 = Arrays.asList(split);
        List<AiWarnMsg> aiWarnMsgs = aiWarnMsgService.selectByShebeiNo(shebeilist, curid);
        if (null == aiWarnMsgs || aiWarnMsgs.size() == 0) {
            log.info(String.format("暂无济新高速JXSG-2标#焦济1标#AI识别的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (AiWarnMsg aiWarnMsg : aiWarnMsgs) {
            id = aiWarnMsg.getId();
            JSONObject sendObject = new JSONObject();
            String guid = "";
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(aiWarnMsg.getCid());
            AiWarnMsgCfg aiWarnMsgCfg = aiWarnMsgCfgService.selectByCid(aiWarnMsg.getCid());
            if (selectshebeione != null) {

                SysDepart sysDepart = sysDepartService.queryone(selectshebeione.getSysOrgCode());
                if (sysDepart != null) {
                    guid = sysDepart.getGuid();
                    sendObject.set("SectionGUID", sysDepart.getGuid());//标段guid
//                    String departName = sysDepart.getDepartName();
//                    String substring = departName.substring(departName.length() - 2, departName.length() - 1);
//                    switch (substring) {
//                        case "1":
////                            sendObject.set("SectionGUID", "");//标段guid
//                            break;
//                        case "2":
//                            sendObject.set("SectionGUID", "1572515707655032832");//标段guid
//                            break;
//                        case "3":
////                            sendObject.set("SectionGUID", "");//标段guid
//                            break;
//                        default:
//                            break;
//                    }

                } else {
                    log.info("济新高速#AI识别数据没有找到相应的标段信息");
                    return;
                }
            }
            sendObject.set("Equipment", selectshebeione.getSbname());//设备名
            sendObject.set("ID", aiWarnMsg.getId().toString());//第三方主键 id
            sendObject.set("WarningType", aiWarnMsg.getAlgtype());//预警类型
            switch (aiWarnMsg.getAlgtype()) {
                case "1":
                    sendObject.set("WarningName", "未戴安全帽");//预警名称
                    sendObject.set("EventLevelValue", "D级");
                    sendObject.set("EventTypeName", "未戴安全帽");
                    break;
                case "2":
                    sendObject.set("WarningName", "未穿反光衣");//预警名称
                    sendObject.set("EventLevelValue", "D级");
                    sendObject.set("EventTypeName", "未穿反光衣");
                    break;
                default:
                    break;
            }
            sendObject.set("WarningLocation", aiWarnMsgCfg.getLocal());//预警地点
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date(Long.parseLong(aiWarnMsg.getWarntime())));
            sendObject.set("WarningTime", time);//预警时间
            List<String> picList = new LinkedList<>();
            picList.add(aiWarnMsg.getPicurls());
            sendObject.set("UrlList", picList);//图片完整路径
            if(guid.equals("1572515707655032832")) {
                String post = HttpRequest.post("http://218.29.117.148:8122/api/opi/aimonitor/add")
                        .header("Content-Type", "application/json")
                        .body(String.valueOf(sendObject))
                        .execute()
                        .body();
                JSONObject postObject = new JSONObject(post);
                Integer code = (Integer) postObject.get("Code");
                if (code == 200) {
                    log.info(String.format("济新高速#AI识别数据推送成功" + DateUtils.now()));
                    sysConfigService.updateSysConfig(JobUtil.JXGS_AITS, id);//根据传过来的唯一值来修改当前判断到的数据id
                } else {
                    log.info(String.format("济新高速#AI识别数据推送失败" + DateUtils.now()));
                }
            }else if(guid.equals("73901")||guid.equals("73902")){
                String post = HttpRequest.post("http://218.29.117.146:8118/api/opi/aimonitor/add")
                        .header("Content-Type", "application/json")
                        .body(String.valueOf(sendObject))
                        .execute()
                        .body();
                JSONObject postObject = new JSONObject(post);
                Integer code = (Integer) postObject.get("Code");
                if (code == 200) {
                    log.info(String.format("焦济1标#AI识别数据推送成功" + DateUtils.now()));
                    sysConfigService.updateSysConfig(JobUtil.JXGS_AITS, id);//根据传过来的唯一值来修改当前判断到的数据id
                } else {
                    log.info(String.format("焦济1标#AI识别数据推送失败" + DateUtils.now()));
                }
            }
        }

    }
}
