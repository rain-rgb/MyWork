package org.jeecg.modules.job.zhongjiaojob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.service.IBhzrenwudanService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName hntscJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/11/18 14:44
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class hntscJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZJ_BHZSC);//拌合站生产（推送中交）
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到中交定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台试验机的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBases = bhzCementBaseService.selectListszt(shebeilist, curid);
        if (null == bhzCementBases || bhzCementBases.size() == 0) {
            log.info(String.format("暂无苏台砼拌合站的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (BhzCementBase bhzCementBase : bhzCementBases) {
            id = bhzCementBase.getId();
            String workNo = bhzCementBase.getWorkNo();
            JSONObject sendmap = JSONUtil.createObj();
            QueryWrapper<Bhzrenwudan> bhzrenwudanQueryWrapper = new QueryWrapper<>();
            bhzrenwudanQueryWrapper.eq("Code", workNo);
            List<Bhzrenwudan> list = bhzrenwudanService.list(bhzrenwudanQueryWrapper);
            if (list.size() > 0) {
                Bhzrenwudan bhzrenwudan = list.get(0);
                sendmap.set("taskId", bhzrenwudan.getCode() + "-" + bhzrenwudan.getId());
                sendmap.set("mixstationCode", "BHL22111108344840");
                sendmap.set("taskCode", bhzrenwudan.getCode());
                sendmap.set("taskDate", bhzrenwudan.getDattim());
                sendmap.set("taskStruct", bhzrenwudan.getConspos() + "_code");
                sendmap.set("taskLevel", bhzrenwudan.getBetlev() + "_code");
                sendmap.set("taskProjname", bhzrenwudan.getProjname() + "_code");
                sendmap.set("taskProjadr", bhzrenwudan.getProjadr());
                sendmap.set("taskPour", bhzrenwudan.getPour() + "_code");
                sendmap.set("taskConsPos", bhzrenwudan.getConspos());
//                sendmap.set("taskRecipe", bhzrenwudan.get);
//                sendmap.set("sample_groupnumber", bhzrenwudan.get);
//                sendmap.set("taskMete", bhzrenwudan.getMete());
//                sendmap.set("taskTotVehs", bhzrenwudan.get);
//                sendmap.set("taskTotMete", bhzrenwudan.get);
//                sendmap.set("taskBak1", bhzrenwudan.get);
//                sendmap.set("taskBak2", bhzrenwudan.get);

                //车次
                List prodLst = new ArrayList();

                JSONObject yscjson = JSONUtil.createObj();
//                yscjson.set("prodId", );
//                yscjson.set("prodMete", );
//                yscjson.set("prodMormete", );
//                yscjson.set("prodMorrec", );
//                yscjson.set("prodRecipe", );
//                yscjson.set("prodVeh", );
//                yscjson.set("prodVehcode", );
//                yscjson.set("prodTimb", );
//                yscjson.set("prodTime", );
//                yscjson.set("prodLeftim", );
//                yscjson.set("prodTotVehs", );
//                yscjson.set("prodTotMete", );
//                yscjson.set("prodBak1", );
//                yscjson.set("prodBak2", );
//
//                //盘
//                List pieceLst = new ArrayList();
//                JSONObject pjson = JSONUtil.createObj();
//                pjson.set("pieceId", );
//                pjson.set("pieceSeri", );
//                pjson.set("pieceDate", );
//                pjson.set("pieceAmount", );
//                pjson.set("pieceTemper", );
//                pjson.set("pieceErr", );
//                pjson.set("pieceStart", );
//                pjson.set("pieceEnd", );
//                pjson.set("pieceBak1", );
//                pjson.set("pieceBak2", );
//                pjson.set("pieceElestart", );
//                pjson.set("pieceEleend", );
//                pjson.set("pieceElectric", "[]");
//
//                //盘详情
//                List panxqLst = new ArrayList();
//                JSONObject pxqjson = JSONUtil.createObj();
//                pxqjson.set("dosId",);
//                pxqjson.set("dosStorid",);
//                pxqjson.set("dosStorna",);
//                pxqjson.set("dosMaterid",);
//                pxqjson.set("dosMaterna",);
//                pxqjson.set("dosRate",);
//                pxqjson.set("dosRec",);
//                pxqjson.set("dosPlan",);
//                pxqjson.set("dosFact",);
//                pxqjson.set("dosFall",);
//                pxqjson.set("dosFintim",);
//                pxqjson.set("dosTemper",);
//                pxqjson.set("dosBak1",);
//                pxqjson.set("dosBak2",);
//                panxqLst.add(pxqjson);

//                pjson.set("dosageLst", panxqLst);
//
//                pieceLst.add(pjson);
//
//
//                yscjson.set("prodBak2", pieceLst);
//
//
//                prodLst.add(yscjson);
//
//
//
//                sendmap.set("prodLst", prodLst);


            }


        }
    }
}
