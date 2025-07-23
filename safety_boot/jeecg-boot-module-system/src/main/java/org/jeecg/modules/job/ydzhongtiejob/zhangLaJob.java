package org.jeecg.modules.job.ydzhongtiejob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglam.vo.TrZhanglaXxbM;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName zhangLaYaJiangJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/8/15 13:38
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zhangLaJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;
    @Autowired
    private ITrZhanglaSService zhanglaSService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YB_GTZL);//义东张拉
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东张拉的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东张拉的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrZhanglaXxb> zhanglaXxbs = zhanglaXxbService.selectListsss(shebeilist, curid);
        if (null == zhanglaXxbs || zhanglaXxbs.size() == 0) {
            log.info(String.format("暂无义东张拉未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrZhanglaXxb zhanglaXxb : zhanglaXxbs){
            id = zhanglaXxb.getId();
            JSONObject sendDate = new JSONObject();
            sendDate.set("beamBodyNum",zhanglaXxb.getGjbh());
            sendDate.set("bridgeName",zhanglaXxb.getGcmc());
            sendDate.set("checkCode","");
            sendDate.set("deviceCode","");
            sendDate.set("equipmentManufacturer","");
            sendDate.set("recordReportAttachment","");
            sendDate.set("tensionTime",zhanglaXxb.getSgsj());

            QueryWrapper<TrZhanglaM> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("syjid",zhanglaXxb.getSyjid());
            List<TrZhanglaM> zhanglaMS = zhanglaMService.list(queryWrapper);
            List zhanglaMList = new ArrayList();
            for (TrZhanglaM zhanglaM : zhanglaMS){
                JSONObject zhanglaMsendDate = new JSONObject();
                zhanglaMsendDate.set("channelNum",zhanglaM.getGsbh());
                Integer sclper = Integer.parseInt(zhanglaM.getSclper());
                if (sclper>6){
                    zhanglaMsendDate.set("elongationExceptionStatus",1);
                }else {
                    zhanglaMsendDate.set("elongationExceptionStatus",0);
                }
                zhanglaMsendDate.set("jackExceptionStatus","");
                zhanglaMsendDate.set("pipeExceptionStatus","");
                zhanglaMsendDate.set("qcEngineerName",zhanglaM.getMemo());
                zhanglaMsendDate.set("qcEngineerTel","");
                zhanglaMsendDate.set("slideWireExceptionStatus","");
                zhanglaMsendDate.set("technicalResponsiblePersonName","");
                zhanglaMsendDate.set("technicalResponsiblePersonTel","");
                zhanglaMsendDate.set("tensionExceptionStatus","");

                QueryWrapper<TrZhanglaS> queryWrappers = new QueryWrapper<>();
                queryWrappers.eq("syjid",zhanglaM.getSyjid());
                queryWrappers.eq("gsbh",zhanglaM.getGsbh());
                List<TrZhanglaS> zhanglaSs = zhanglaSService.list(queryWrappers);
                List zhanglaSList = new ArrayList();
                for (TrZhanglaS zhanglaS : zhanglaSs){
                    JSONObject zhanglaSsendDate = new JSONObject();
                    zhanglaSsendDate.set("collectTime",zhanglaM.getZlsj());
                    zhanglaSsendDate.set("displacementOffset","");
//                    zhanglaSsendDate.set("displacementOneTop",zhanglaS.get);
//                    zhanglaSsendDate.set("displacementTwoTop",zhanglaS.get);
//                    zhanglaSsendDate.set("tensionOffset",zhanglaS.get);
//                    zhanglaSsendDate.set("tensionOneTop",zhanglaS.get);
//                    zhanglaSsendDate.set("tensionTwoTop",zhanglaS.get);
                    zhanglaSList.add(zhanglaSsendDate);
                }
                zhanglaMsendDate.set("itemList",zhanglaSList);
                zhanglaMList.add(zhanglaMsendDate);
            }
            sendDate.set("channelDataList",zhanglaMList);
        }
    }
}
