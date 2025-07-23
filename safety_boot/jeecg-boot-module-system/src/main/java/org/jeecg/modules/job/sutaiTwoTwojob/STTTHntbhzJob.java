package org.jeecg.modules.job.sutaiTwoTwojob;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
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

import java.util.Arrays;
import java.util.List;

@Slf4j
public class STTTHntbhzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private SuTaiTTUtil suTaiTTUtil;
    @Autowired
    private IBhzCementDetailService iBhzCementDetailService;
    @Autowired
    private IBhzCementBaseService iBhzCementBaseService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.STTWO_TWOHNTBHZ);//苏台二期二标混凝土拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
//        String[] split = shebeilist.split(",");
//        List<String> strsToList1 = Arrays.asList(split);
        List<BhzCementBase> bhzCementBasess = iBhzCementBaseService.selectHntbhzList2(curid, 1, shebeilist);
        if (null == bhzCementBasess || bhzCementBasess.size() == 0) {
            log.info(String.format("暂无苏台砼拌合站的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        JSONArray jsonArray = new JSONArray();
        for (BhzCementBase bhzCementBases : bhzCementBasess) {
            String projectName = bhzCementBases.getProjectName();
            id = bhzCementBases.getId();
            JSONObject saveDTOList = new JSONObject();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(bhzCementBases.getShebeiNo());
            saveDTOList.set("endTime", bhzCementBases.getProductDatetime());//出料时间
//            saveDTOList.set("equipmentName", "苏台二期二标测试拌合站");//设备名称
            saveDTOList.set("equipmentNo",bhzCementBases.getShebeiNo());//设备编号
            saveDTOList.set("equipmentName", selectshebeione.getSbname());//设备名称
            saveDTOList.set("grade", bhzCementBases.getStrengthRank());//强度等级
            saveDTOList.set("malaxationTime", bhzCementBases.getStirDatetime());//拌合时间(单位/秒)
            saveDTOList.set("operator", bhzCementBases.getHandlers());//操作员
            Integer overLevel = bhzCementBases.getOverLevel();
            if (overLevel == 0) {
                saveDTOList.set("overweightGrade", 1);//超标等级 1 合格 2 初级超标 3 中级超标 4 高级超标
            } else if (overLevel == 1) {
                saveDTOList.set("overweightGrade", 2);//超标等级 1 合格 2 初级超标 3 中级超标 4 高级超标
            } else if (overLevel == 2) {
                saveDTOList.set("overweightGrade", 3);//超标等级 1 合格 2 初级超标 3 中级超标 4 高级超标
            } else if (overLevel == 3) {
                saveDTOList.set("overweightGrade", 4);//超标等级 1 合格 2 初级超标 3 中级超标 4 高级超标
            }
            saveDTOList.set("palce", bhzCementBases.getJobLocation());//施工地点
            saveDTOList.set("part", bhzCementBases.getPoureLocation());//浇筑部位
            saveDTOList.set("recipeNo", bhzCementBases.getFormulaNo());//配方号
            saveDTOList.set("sectionType", 2);//1 一标 2 二标 3 三标 4 四标 5 五标
            saveDTOList.set("sendType","PRO");
            saveDTOList.set("slumps", bhzCementBases.getSlump());//坍落度
            saveDTOList.set("volume", bhzCementBases.getEstimateNumber());//方量(单位/立方米)

            List<BhzCementDetail> bhzCementDetails = iBhzCementDetailService.selectcementlist(bhzCementBases.getBatchNo());
            JSONArray jsonArray1 = new JSONArray();

            for (BhzCementDetail bhzCementDetail : bhzCementDetails) {
                JSONObject saveDTOList1 = new JSONObject();
                saveDTOList1.set("actualQuantity", bhzCementDetail.getRealityNumber());//实际用量(保留两位小数)
                saveDTOList1.set("createTime", null);//创建时间
                saveDTOList1.set("createdBy", null);//创建人ID
                saveDTOList1.set("designQuantity", bhzCementDetail.getTheoryNumber());//设计用量(保留两位小数)
                saveDTOList1.set("echoMap", null);//
                saveDTOList1.set("highErrorRange", "±" + bhzCementDetail.getOverAdvancedSetvalue());//误差范围高级
                saveDTOList1.set("initialErrorRange", "±" + bhzCementDetail.getOverPrimarySetvalue());//误差范围初级
                saveDTOList1.set("mediumErrorRange", "±" + bhzCementDetail.getOverMiddleSetvalue());//误差范围中级
                saveDTOList1.set("materialName", bhzCementDetail.getMaterialeName());//	材料名称
                saveDTOList1.set("updateTime", null);//最后修改时间
                saveDTOList1.set("updatedBy", null);//最后修改人ID
                jsonArray1.add(saveDTOList1);
            }
            saveDTOList.set("solutionsConcreteStandItemList", jsonArray1);
            jsonArray.add(saveDTOList);

        }
        if (jsonArray.size() > 0) {
            Integer integer = suTaiTTUtil.PostSTTHIBHZList(jsonArray);
            if (integer == 200) {
                log.info(String.format("苏台拌合站二期二标数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.STTWO_TWOHNTBHZ, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("苏台拌合站二期二标数据推送失败!" + id));
            }
        } else {
            log.info(String.format("苏台拌合站当前数据属于二期二标" + id));
            sysConfigService.updateSysConfig(JobUtil.STTWO_TWOHNTBHZ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
