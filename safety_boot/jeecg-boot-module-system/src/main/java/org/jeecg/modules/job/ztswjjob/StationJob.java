package org.jeecg.modules.job.ztswjjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.system.service.IShigongphbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName StationJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/5/27 11:16
 * @Version 1.0
 **/
@Slf4j
public class StationJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IShigongphbService shigongphbService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_SFBDB);//中铁十五局三分部拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到中铁十五局三分部拌合站的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输中铁十五局三分部拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        //获取中铁十五局三分部拌合站数据
        List<BhzCementBase> bhzCementBases1 = bhzCementBaseService.selectLists1(shebeilist, curid);
        if (null == bhzCementBases1 || bhzCementBases1.size() == 0) {
            log.info(String.format("暂无中铁十五局三分部拌合站未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        //循环
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (BhzCementBase bhzCementBase : bhzCementBases1) {
            //获取配料单号
            QueryWrapper<Shigongphb> bhzCementBaseQueryWrapper = new QueryWrapper<>();
            bhzCementBaseQueryWrapper.eq("Code", bhzCementBase.getWorkNo());
            List<Shigongphb> shigongphbs = shigongphbService.list(bhzCementBaseQueryWrapper);

            id = bhzCementBase.getId();
            JSONObject sendDate = new JSONObject();
            List cailiaoList = new ArrayList();
            List bhzdateList = new ArrayList();

            if (shigongphbs.size() > 0) {
                sendDate.set("token", String.valueOf(shigongphbs.get(0).getStation()));
                sendDate.set("date_time", sdf.format(shigongphbs.get(0).getDattim()));
                sendDate.set("code", shigongphbs.get(0).getRenwudan());
                sendDate.set("pour", shigongphbs.get(0).getPour());
                sendDate.set("variety", shigongphbs.get(0).getTag());
                sendDate.set("filter", shigongphbs.get(0).getFilter());
                sendDate.set("freeze", shigongphbs.get(0).getFreeze());
                sendDate.set("rec_code", shigongphbs.get(0).getCode());
                sendDate.set("inspector", shigongphbs.get(0).getAssoss());
            }

            QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper();
            String shebeibianhao = bhzCementBase.getShebeiNo();
            queryWrapper.select("sbname");
            queryWrapper.eq("sbjno", shebeibianhao);
            ShebeiInfo sbname = shebeiInfoService.getOne(queryWrapper);
            sendDate.set("mixer", sbname.getSbname());

//                sendDate.set("material_id_un", bhzCementBase.getBatchNo());
            sendDate.set("lands", bhzCementBase.getSlump());
            sendDate.set("prod_mete", String.valueOf(bhzCementBase.getEstimateNumber()));
            sendDate.set("device_imei", bhzCementBase.getShebeiNo());
            sendDate.set("produce_tag", bhzCementBase.getBatchNo());
            sendDate.set("prod_id", "");
            sendDate.set("mark", "");
            sendDate.set("attribute", "");
            sendDate.set("contract", "");
            sendDate.set("proj_name", bhzCementBase.getProjectName());
            sendDate.set("produce_price", "");
            sendDate.set("produce_total_money", "");
            sendDate.set("proj_type", "");
            sendDate.set("proj_grade", "");
            sendDate.set("proj_area", "");
            sendDate.set("proj_adr", bhzCementBase.getJobLocation());
            sendDate.set("distance", "");
            sendDate.set("conspos", bhzCementBase.getPoureLocation());
            sendDate.set("betLev", bhzCementBase.getStrengthRank());
            sendDate.set("cement", bhzCementBase.getCementVariety());
            sendDate.set("stone", "");
            sendDate.set("bnSize", "");
            sendDate.set("add_liq", "");
            sendDate.set("mix_last", String.valueOf(bhzCementBase.getStirDatetime()));
            sendDate.set("mor_code", "");
            sendDate.set("veh_code", "");
            sendDate.set("driver", "");
            sendDate.set("prod_tim_b", "");
            sendDate.set("prod_tim_e", sdf.format(bhzCementBase.getProductDatetime()));
            sendDate.set("mor_mete", "");
            sendDate.set("tot_vehs", "");
            sendDate.set("tot_mete", "");
            sendDate.set("operator", bhzCementBase.getHandlers());
//                sendDate.set("produce_use_statue", 0);
            sendDate.set("customer", "");
//                sendDate.set("produce_submit_time", sdf.format(bhzCementBase.getSaveDatetime()));
//                sendDate.set("produce_organ_id_union", "");

            //循环拌合站数据获取一盘数据
            Map bhzdateMap = new HashMap();

            bhzdateMap.put("piece_id", bhzCementBase.getBatchNo());
            bhzdateMap.put("serial", "");
            bhzdateMap.put("pie_amnt", String.valueOf(bhzCementBase.getEstimateNumber()));
            bhzdateMap.put("s_date_time", "");
            bhzdateMap.put("e_date_time", sdf.format(bhzCementBase.getProductDatetime()));
            bhzdateMap.put("flag", "");
            bhzdateMap.put("piece_warn_level", bhzCementBase.getOverLevel());


            //获取材料子表数据
            QueryWrapper<BhzCementDetail> bhzCementDetailQueryWrapper = new QueryWrapper<>();
            bhzCementDetailQueryWrapper.eq("batch_no", bhzCementBase.getBatchNo());
            List<BhzCementDetail> BhzCementDetails = bhzCementDetailService.list(bhzCementDetailQueryWrapper);
            for (BhzCementDetail bhzCementDetail : BhzCementDetails) {
                Map cailiaoMap = new HashMap();
//                        cailiaoMap.put("mixing_station_dosage_material_id_un", bhzCementBases.getBatchNo());
                String storage = "";
                Integer materialeType = bhzCementDetail.getMaterialeType();
                if (1 == materialeType) {
                    storage = "细骨料";
                }
                if (2 == materialeType) {
                    storage = "大石";
                }
                if (3 == materialeType) {
                    storage = "中石";
                }
                if (4 == materialeType) {
                    storage = "小石";
                }
                if (5 == materialeType) {
                    storage = "水";
                }
                if (6 == materialeType) {
                    storage = "水泥";
                }
                if (7 == materialeType) {
                    storage = "矿粉";
                }
                if (8 == materialeType) {
                    storage = "粉煤灰";
                }
                if (9 == materialeType) {
                    storage = "外加剂";
                }
                if (10 == materialeType) {
                    storage = "其他";
                }
                cailiaoMap.put("storage", storage);
                cailiaoMap.put("material", bhzCementDetail.getMaterialeName());
                cailiaoMap.put("rec_amnt", String.valueOf(bhzCementDetail.getTheoryNumber()));
                cailiaoMap.put("plan_amnt", String.valueOf(bhzCementDetail.getTheoryNumber()));
                cailiaoMap.put("fact_amnt", String.valueOf(bhzCementDetail.getRealityNumber()));
                cailiaoMap.put("rate", String.valueOf(bhzCementDetail.getOverValue()));
                cailiaoMap.put("dosage_warn_level", bhzCementDetail.getMaterialeOverLevel());
                cailiaoList.add(cailiaoMap);
            }
            bhzdateMap.put("dosages", cailiaoList);
            bhzdateList.add(bhzdateMap);
            sendDate.set("pieces", bhzdateList);
            String back = HttpRequest.post("https://build.cninct.com/CR15G3GS?op=UploadMixingStationProduce")
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();

            pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), back);
            JSONObject jsonObject1 = new JSONObject(back);
            String codes = jsonObject1.get("message").toString();

            if (codes.contains("成功")) {
                log.info("中铁十五局三分部拌合站推送成功!{}", jsonObject1);
                bhzCementBase.setIssend(1);
            } else {
                log.info("中铁十五局三分部拌合站推送失败!{}", jsonObject1);
                bhzCementBase.setIssend(2);
            }

            bhzCementBaseService.updateById(bhzCementBase);
        }
    }
}
