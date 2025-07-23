package org.jeecg.modules.job.panan351Job;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName syjJob
 * @Author
 * @Date 2024/12/5 9:30
 * @Version
 * @Description 磐安351试验推送
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class syjJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService syjzbService;
    @Autowired
    private ISysDictItemService sysDictItemService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysDepartService sysDepartService;

    private static final String SYJ_URL = "http://122.226.24.18:8878/api/construction-url/solutionsPress/insertBacth";
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YD_BHZ);//义东拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东拌合站的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TSyjzb> tSyjzbs = syjzbService.seletListPanan351(shebeilist);
        if (null == tSyjzbs || tSyjzbs.isEmpty()) {
            log.info(String.format("没有要传输的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TSyjzb tSyjzb : tSyjzbs) {
            id = tSyjzb.getId();
            JSONObject sendDate = new JSONObject();
            sendDate.set("designStrength", "");
            sendDate.set("equipmentNo", tSyjzb.getSbbh());
            sendDate.set("experimentTime", tSyjzb.getSyrq());

            Map<String,String> experimentType = new HashMap<>();
            experimentType.put("key", tSyjzb.getSylx());
            LambdaQueryWrapper<SysDictItem> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysDictItem::getDictId, "1412293231980355585")
                    .eq(SysDictItem::getItemValue, tSyjzb.getSylx());
            SysDictItem one = sysDictItemService.getOne(queryWrapper);
            experimentType.put("data", one.getItemText());
            sendDate.set("experimentType", experimentType);

            sendDate.set("instarTime", tSyjzb.getLq());

            LambdaQueryWrapper<ShebeiInfo> shebeiInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            shebeiInfoLambdaQueryWrapper.eq(ShebeiInfo::getSbjno, tSyjzb.getSbbh());
            ShebeiInfo shebeiInfo = shebeiInfoService.getOne(shebeiInfoLambdaQueryWrapper);
            Integer sbtype = shebeiInfo.getSbtype();
            if (sbtype == 4) {
                sendDate.set("laboratoryType", "压力机");
            } else if (sbtype == 3) {
                sendDate.set("laboratoryType", "万能机");
            } else if (sbtype == 12) {
                sendDate.set("laboratoryType", "抗折抗压");
            }

            sendDate.set("makeTime", tSyjzb.getZzrq());
            sendDate.set("operator", tSyjzb.getCzry());
            sendDate.set("projectName", tSyjzb.getSgbw());
            sendDate.set("result", tSyjzb.getPdjg());

            Map<String,String> sectionId = new HashMap<>();
            LambdaQueryWrapper<SysDepart> sysDepartLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysDepartLambdaQueryWrapper.eq(SysDepart::getOrgCode, shebeiInfo.getSysOrgCode());
            SysDepart sysDepart = sysDepartService.getOne(sysDepartLambdaQueryWrapper);
            sectionId.put("key", sysDepart.getId());
            sectionId.put("data", sysDepart.getDepartName());
            sendDate.set("sectionId", sectionId);

            sendDate.set("sectionType", 6);
//            测试数据 DEV 正式数据 PRO 正式数据并且触发预警推送 PRO_SEND
            sendDate.set("sendType", "DEV");
            sendDate.set("specifiedNumber", tSyjzb.getSjbh());
            sendDate.set("strengthNum", tSyjzb.getQddbz());
            sendDate.set("testNum", tSyjzb.getSjsl());
            sendDate.set("testSize", tSyjzb.getGczj());

            String request = HttpRequest.post(SYJ_URL)
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();
            System.out.println(request);
        }
    }
}