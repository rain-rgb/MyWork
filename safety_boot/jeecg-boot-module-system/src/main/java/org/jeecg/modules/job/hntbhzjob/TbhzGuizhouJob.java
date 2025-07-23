package org.jeecg.modules.job.hntbhzjob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import com.trtm.iot.guizhouttuisong.entity.GuizhoutTuisong;
import com.trtm.iot.guizhouttuisong.service.IGuizhoutTuisongService;
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
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.ShandongUtil;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/9/1
 * \* Time: 16:00
 * \* Description:贵州质监局拌合站数据推送(金仁铜一标拌合站)
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TbhzGuizhouJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private IBhzChaobiaoCfgService bhzChaobiaoCfgService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private ShandongUtil shandongUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IGuizhoutTuisongService guizhoutTuisongService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_JRTT);//砼拌合站数据推送贵州质监局
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到砼拌合站推送贵州质监局配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输砼拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1= Arrays.asList(split);
        List<BhzCementBase> selecthntbhzones = bhzCementBaseService.selecthntbhzList(curid, 1,strsToList1);//所有的数据
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无砼拌合站未判断的数据" + DateUtils.now()));
            return;
        }
        int id=0;
        for (BhzCementBase selecthntbhzone : selecthntbhzones) {
            JSONObject publicPitchList1 =new JSONObject();
            String shebeiNo = selecthntbhzone.getShebeiNo();
            GuizhoutTuisong guizhoutTuisong = guizhoutTuisongService.queryselectOne(shebeiNo);
            String guid = guizhoutTuisong.getGuid();//秘钥
            String sbjno = guizhoutTuisong.getSbjno();//推送设备编号
            String sbname = guizhoutTuisong.getSbname();//推送设备名称
            if(sbname==null||sbjno==null||guid==null){
                log.info(String.format("贵州质监局推送拌合站数据未提供秘钥以及设备名称" + DateUtils.now()));
            }
            id = selecthntbhzone.getId();
            String product_datetime = DateUtil.format(selecthntbhzone.getProductDatetime(), "yyyy-MM-dd HH:mm:ss");
            String CollectDatetime = DateUtil.format(selecthntbhzone.getCollectDatetime(), "yyyy-MM-dd HH:mm:ss");
            publicPitchList1.set("MachineName",sbname);//设备名称
            publicPitchList1.set("OriginalId",id);//原始数据主键
            publicPitchList1.set("Engineering",selecthntbhzone.getProjectName());//工程名称
            publicPitchList1.set("poure_location",selecthntbhzone.getPoureLocation());//浇筑部位
            publicPitchList1.set("JobLocation",selecthntbhzone.getJobLocation());//施工地点
            publicPitchList1.set("StrengthType",selecthntbhzone.getStrengthRank());//强度等级
            publicPitchList1.set("MakeTime",product_datetime);//出料时间
            publicPitchList1.set("UploadTime",CollectDatetime);//采集时间
            publicPitchList1.set("CubeCount",selecthntbhzone.getEstimateNumber());//方量
            publicPitchList1.set("WarningLevel",selecthntbhzone.getOverLevel());//超标等级
            publicPitchList1.set("CartWarningLevel",selecthntbhzone.getOverLevel());//整车超标等级
            publicPitchList1.set("StirTime",selecthntbhzone.getStirDatetime());//搅拌时长
            publicPitchList1.set("MixProportionCode",selecthntbhzone.getFormulaNo());//施工配合比编号
            if(selecthntbhzone.getOverLevel()>0){
                publicPitchList1.set("IsWarning",true);//是否超差
            }else{
                publicPitchList1.set("IsWarning",false);//是否超差
                publicPitchList1.set("IsClosed",true);//是否闭合
            }
            publicPitchList1.set("BetonType",selecthntbhzone.getCementVariety());//水泥品种
            publicPitchList1.set("TaskNO",selecthntbhzone.getFormulaNo());//任务单号
            List<BhzCementDetail> selectcementlist = bhzCementDetailService.selectcementlist(selecthntbhzone.getBatchNo());
            if(selectcementlist.size()==0){
                log.info(String.format("推送贵州质监局拌合站没有材料信息" + DateUtils.now()));
                continue;
            }
            int[] detailstate1 = {0,0,0,0,0,0,0,0,0,0};
            JSONObject publicPitchList2 =new JSONObject();
            for (BhzCementDetail bhzCementDetail : selectcementlist) {
                Integer materialeType = bhzCementDetail.getMaterialeType();
                if (materialeType == 2 || materialeType == 3 || materialeType == 4) {
                    if(detailstate1[1] == 0){//粗骨料1
                        if (bhzCementDetail.getTheoryNumber() != null) {
                            publicPitchList2.putOpt("Coarse1", bhzCementDetail.getTheoryNumber());
                        } else {
                            publicPitchList2.putOpt("Coarse1", 0);
                        }

                        detailstate1[1] = 1;
                    }else if(detailstate1[1]==1){//粗骨料2
                        if (bhzCementDetail.getTheoryNumber() != null) {
                            publicPitchList2.putOpt("Coarse2", bhzCementDetail.getTheoryNumber());
                        } else {
                            publicPitchList2.putOpt("Coarse2", 0);
                        }

                        detailstate1[1] = 2;
                    }else {//粗骨料3
                        if (bhzCementDetail.getTheoryNumber() != null) {
                            publicPitchList2.putOpt("Coarse3", bhzCementDetail.getTheoryNumber());
                        } else {
                            publicPitchList2.putOpt("Coarse3", 0);
                        }

                        detailstate1[1] = 3;
                    }
                }else if(materialeType == 1){
                    if(detailstate1[0] == 0){//细骨料1
                        if (bhzCementDetail.getTheoryNumber() != null) {
                            publicPitchList2.putOpt("Fines1", bhzCementDetail.getTheoryNumber());
                        } else {
                            publicPitchList2.putOpt("Fines1", 0);
                        }

                        detailstate1[0] = 1;
                    }else if(detailstate1[0]==1){//细骨料2
                        if (bhzCementDetail.getTheoryNumber() != null) {
                            publicPitchList2.putOpt("Fines2", bhzCementDetail.getTheoryNumber());
                        } else {
                            publicPitchList2.putOpt("Fines2", 0);
                        }

                        detailstate1[0] = 2;
                    }
                }else if(materialeType == 5){
                    if(detailstate1[4] == 0){//水
                        if (bhzCementDetail.getTheoryNumber() != null) {
                            publicPitchList2.putOpt("Water", bhzCementDetail.getTheoryNumber());
                        } else {
                            publicPitchList2.putOpt("Water", 0);
                        }

                        detailstate1[4] = 1;
                    }
                }else if(materialeType == 6){
                    if(detailstate1[5] == 0){//水泥1
                        if (bhzCementDetail.getTheoryNumber() != null) {
                            publicPitchList2.putOpt("Cement1", bhzCementDetail.getTheoryNumber());
                        } else {
                            publicPitchList2.putOpt("Cement1", 0);
                        }

                        detailstate1[5] = 1;
                    }else if(detailstate1[5] == 1){//水泥2
                        if (bhzCementDetail.getTheoryNumber() != null) {
                            publicPitchList2.putOpt("Cement2", bhzCementDetail.getTheoryNumber());
                        } else {
                            publicPitchList2.putOpt("Cement2", 0);
                        }

                    }
                }else if(materialeType == 7||materialeType == 8){
                    if(detailstate1[7] == 0){//粉煤灰
                        if (bhzCementDetail.getTheoryNumber() != null) {
                            publicPitchList2.putOpt("Coal", bhzCementDetail.getTheoryNumber());
                        } else {
                            publicPitchList2.putOpt("Coal", 0);
                        }

                        detailstate1[7] = 1;
                    }
                }else if(materialeType == 9){
                    if(detailstate1[8] == 0){//添加剂1
                        if (bhzCementDetail.getTheoryNumber() != null) {
                            publicPitchList2.putOpt("Additive1", bhzCementDetail.getTheoryNumber());
                        } else {
                            publicPitchList2.putOpt("Additive1", 0);
                        }

                        detailstate1[8] = 1;
                    }else if(detailstate1[8] == 1){//添加剂2
                        if (bhzCementDetail.getTheoryNumber() != null) {
                            publicPitchList2.putOpt("Additive2", bhzCementDetail.getTheoryNumber());
                        } else {
                            publicPitchList2.putOpt("Additive2", 0);
                        }

                        detailstate1[8] = 2;

                    }else if(detailstate1[8] == 2){//添加剂2
                        if (bhzCementDetail.getTheoryNumber() != null) {
                            publicPitchList2.putOpt("Additive3", bhzCementDetail.getTheoryNumber());
                        } else {
                            publicPitchList2.putOpt("Additive3", 0);
                        }
                        detailstate1[8] = 3;

                    }else if(detailstate1[8] == 3){//添加剂2
                        if (bhzCementDetail.getTheoryNumber() != null) {
                            publicPitchList2.putOpt("Additive4", bhzCementDetail.getTheoryNumber());
                        } else {
                            publicPitchList2.putOpt("Additive4", 0);
                        }

                        detailstate1[8] = 4;
                    }
                }
                publicPitchList2.set("Type",1);//1：设定用量2：实际用量
            }
            publicPitchList1.set("DesignRatio",publicPitchList2);
            int[] detailstate = {0,0,0,0,0,0,0,0,0,0};
            JSONObject publicPitchList3 =new JSONObject();
            for (BhzCementDetail record : selectcementlist) {
                Integer materialeType = record.getMaterialeType();
                if (materialeType == 2 || materialeType == 3 || materialeType == 4) {
                    if(detailstate[1] == 0){//粗骨料1
                        if (record.getRealityNumber() != null) {
                            publicPitchList3.putOpt("Coarse1", record.getRealityNumber());
                        } else {
                            publicPitchList3.putOpt("Coarse1", 0);
                        }

                        detailstate[1] = 1;
                    }else if(detailstate[1]==1){//粗骨料2
                        if (record.getRealityNumber() != null) {
                            publicPitchList3.putOpt("Coarse2", record.getRealityNumber());
                        } else {
                            publicPitchList3.putOpt("Coarse2", 0);
                        }

                        detailstate[1] = 2;
                    }else {//粗骨料3
                        if (record.getRealityNumber() != null) {
                            publicPitchList3.putOpt("Coarse3", record.getRealityNumber());
                        } else {
                            publicPitchList3.putOpt("Coarse3", 0);
                        }

                        detailstate[1] = 3;
                    }
                }else if(materialeType == 1){
                    if(detailstate[0] == 0){//细骨料1
                        if (record.getRealityNumber() != null) {
                            publicPitchList3.putOpt("Fines1", record.getRealityNumber());
                        } else {
                            publicPitchList3.putOpt("Fines1", 0);
                        }

                        detailstate[0] = 1;
                    }else if(detailstate[0]==1){//细骨料2
                        if (record.getRealityNumber() != null) {
                            publicPitchList3.putOpt("Fines2", record.getRealityNumber());
                        } else {
                            publicPitchList3.putOpt("Fines2", 0);
                        }

                        detailstate[0] = 2;
                    }
                }else if(materialeType == 5){
                    if(detailstate[4] == 0){//水
                        if (record.getRealityNumber() != null) {
                            publicPitchList3.putOpt("Water", record.getRealityNumber());
                        } else {
                            publicPitchList3.putOpt("Water", 0);
                        }

                        detailstate[4] = 1;
                    }
                }else if(materialeType == 6){
                    if(detailstate[5] == 0){//水泥1
                        if (record.getRealityNumber() != null) {
                            publicPitchList3.putOpt("Cement1", record.getRealityNumber());
                        } else {
                            publicPitchList3.putOpt("Cement1", 0);
                        }

                        detailstate[5] = 1;
                    }else if(detailstate[5] == 1){//水泥2
                        if (record.getRealityNumber() != null) {
                            publicPitchList3.putOpt("Cement2", record.getRealityNumber());
                        } else {
                            publicPitchList3.putOpt("Cement2", 0);
                        }

                    }
                }else if(materialeType == 7||materialeType == 8){
                    if(detailstate[7] == 0){//粉煤灰
                        if (record.getRealityNumber() != null) {
                            publicPitchList3.putOpt("Coal", record.getRealityNumber());
                        } else {
                            publicPitchList3.putOpt("Coal", 0);
                        }

                        detailstate[7] = 1;
                    }
                }else if(materialeType == 9){
                    if(detailstate[8] == 0){//添加剂1
                        if (record.getRealityNumber() != null) {
                            publicPitchList3.putOpt("Additive1", record.getRealityNumber());
                        } else {
                            publicPitchList3.putOpt("Additive1", 0);
                        }

                        detailstate[8] = 1;
                    }else if(detailstate[8] == 1){//添加剂2
                        if (record.getRealityNumber() != null) {
                            publicPitchList3.putOpt("Additive2", record.getRealityNumber());
                        } else {
                            publicPitchList3.putOpt("Additive2", 0);
                        }

                        detailstate[8] = 2;
                    }else if(detailstate[8] == 2){//添加剂2
                        if (record.getRealityNumber() != null) {
                            publicPitchList3.putOpt("Additive3", record.getRealityNumber());
                        } else {
                            publicPitchList3.putOpt("Additive3", 0);
                        }

                        detailstate[8] = 3;
                    }else if(detailstate[8] == 3){//添加剂2
                        if (record.getRealityNumber() != null) {
                            publicPitchList3.putOpt("Additive4", record.getRealityNumber());
                        } else {
                            publicPitchList3.putOpt("Additive4", 0);
                        }

                        detailstate[8] = 4;
                    }
                }
                publicPitchList3.set("Type",2);//1：设定用量2：实际用量
            }
            publicPitchList1.set("RealityRatio",publicPitchList3);
            JSONArray jsonArray=new JSONArray();
            JSONObject sendData = JSONUtil.createObj();
            sendData.put("InterfaceGuid",guid);
            sendData.put("EquipCode", sbjno);
            sendData.put("Data", publicPitchList1);
            jsonArray.add(sendData);
            Integer integer = jobUtil.GETLeiRongTBHZ(jsonArray);
            if(integer==200){
                log.info(String.format("贵州质监局拌合站数据推送成功!" + sbname));
            }else{
                log.info(String.format("贵州质监局拌合站数据推送失败!" + sbname));
            }
        }
        sysConfigService.updateSysConfig(JobUtil.BHZ_JRTT, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
