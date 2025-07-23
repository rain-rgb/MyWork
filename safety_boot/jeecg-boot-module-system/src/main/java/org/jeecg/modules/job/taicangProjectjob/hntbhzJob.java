package org.jeecg.modules.job.taicangProjectjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * \* @author: zml
 * \* Date: 2022/07/29
 * \* Time: 16:50
 * \* Description:  256太仓混凝土拌合站数据推送
 * \
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class hntbhzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.TAICANG_TBHZDATA);//混凝土拌合站数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到太仓混凝土拌合站数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info("没有配置要传输太仓混凝土拌合站的设备" + DateUtils.now());
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBaseList = bhzCementBaseService.selectListdata(shebeilist, curid);
        if (null == bhzCementBaseList || bhzCementBaseList.size() == 0) {
            log.info("暂无太仓太仓混凝土拌合站未推送数据" + DateUtils.now());
            return;
        }
        int id = 0;
        for (BhzCementBase bhzCementBase : bhzCementBaseList) {
            JSONObject sendObject = JSONUtil.createObj();
            id = bhzCementBase.getId();
            sendObject.set("id", id);
            if ("A05A07A02A01A01A01A01A01_BHZ_1154".equals(bhzCementBase.getShebeiNo())) {
                sendObject.set("mp_num", "BH00639");
            }
            if ("A05A07A02A01_BHZ_0454".equals(bhzCementBase.getShebeiNo())) {
                if ("苏交集团".equals(bhzCementBase.getProjectName()) && bhzCementBase.getJobLocation().contains("双凤")) {
                    sendObject.set("mp_num", "BH00640");
                }else {
                    continue;
                }
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sendObject.set("baocunshijian", dateFormat.format(bhzCementBase.getCollectDatetime()));
            sendObject.set("remark_1", bhzCementBase.getProductDatetime().getTime()/1000);
            sendObject.set("projectname", bhzCementBase.getProjectName());
            sendObject.set("strength_grade", bhzCementBase.getStrengthRank());
            sendObject.set("place_part", bhzCementBase.getPoureLocation());
            sendObject.set("volume", bhzCementBase.getEstimateNumber());
            sendObject.set("stir_time", bhzCementBase.getStirDatetime());
            sendObject.set("remark_2",bhzCementBase.getWorkNo());
            List<BhzCementDetail> bhzCementDetails = bhzCementDetailService.selectcementlist(bhzCementBase.getBatchNo());
            if (bhzCementDetails.size() > 0) {
                int[] datelist = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                for (BhzCementDetail bhzCementDetail : bhzCementDetails) {
                    if ((bhzCementDetail.getMaterialeType() == 1 || bhzCementDetail.getMaterialeType() == 4 || bhzCementDetail.getMaterialeType() == 3 || bhzCementDetail.getMaterialeType() == 2)) {
                        if (!bhzCementDetail.getMaterialeName().contains("沙子") && !bhzCementDetail.getMaterialeName().contains("砂子")) {//骨料
                            if (datelist[1] == 0) {
                                sendObject.set("stone_1", bhzCementDetail.getRealityNumber());//骨料1实际
                                sendObject.set("th_stone_1", bhzCementDetail.getTheoryNumber());//骨料1理论
                                datelist[1] = 1;
                            } else if (datelist[1] == 1) {
                                sendObject.set("stone_2", bhzCementDetail.getRealityNumber());//骨料2实际
                                sendObject.set("th_stone_2", bhzCementDetail.getTheoryNumber());//骨料2理论
                                datelist[1] = 2;
                            } else if (datelist[1] == 2) {
                                sendObject.set("stone_3", bhzCementDetail.getRealityNumber());//骨料3实际
                                sendObject.set("th_stone_3", bhzCementDetail.getTheoryNumber());//骨料3理论
                                datelist[1] = 3;
                            } else if (datelist[1] == 3) {
                                sendObject.set("stone_3", bhzCementDetail.getRealityNumber());//骨料3实际
                                sendObject.set("th_stone_3", bhzCementDetail.getTheoryNumber());//骨料3理论
                                datelist[1] = 4;
                            } else if (datelist[1] == 4) {
                                sendObject.set("stone_4", bhzCementDetail.getRealityNumber());//骨料4实际
                                sendObject.set("th_stone_4", bhzCementDetail.getTheoryNumber());//骨料4理论
                                datelist[1] = 5;
                            } else if (datelist[1] == 5) {
                                sendObject.set("stone_5", bhzCementDetail.getRealityNumber());//骨料5实际
                                sendObject.set("th_stone_5", bhzCementDetail.getTheoryNumber());//骨料5理论
                                datelist[1] = 6;
                            } else if (datelist[1] == 6) {
                                sendObject.set("stone_6", bhzCementDetail.getRealityNumber());//骨料6实际
                                sendObject.set("th_stone_6", bhzCementDetail.getTheoryNumber());//骨料6理论
                                datelist[1] = 7;
                            } else if (datelist[1] == 7) {
                                sendObject.set("stone_7", bhzCementDetail.getRealityNumber());//骨料7实际
                                sendObject.set("th_stone_7", bhzCementDetail.getTheoryNumber());//骨料7理论
                                datelist[1] = 8;
                            }
                        }else {
                            if (datelist[0] == 0) {
                                sendObject.set("sz_1", bhzCementDetail.getRealityNumber());//沙子1实际
                                sendObject.set("th_sz_1", bhzCementDetail.getTheoryNumber());//沙子1理论
                                datelist[0] = 1;
                            } else if (datelist[1] == 1) {
                                sendObject.set("sz_2", bhzCementDetail.getRealityNumber());//沙子2实际
                                sendObject.set("th_sz_2", bhzCementDetail.getTheoryNumber());//沙子2理论
                                datelist[0] = 2;
                            } else if (datelist[1] == 2) {
                                sendObject.set("sz_3", bhzCementDetail.getRealityNumber());//沙子3实际
                                sendObject.set("th_sz_3", bhzCementDetail.getTheoryNumber());//沙子3理论
                                datelist[0] = 3;
                            }
                        }
                    }
                    if (bhzCementDetail.getMaterialeType() == 5) {//水
                        if (datelist[4] == 0) {
                            sendObject.set("water_1", bhzCementDetail.getRealityNumber());//水1
                            sendObject.set("th_water_1", bhzCementDetail.getTheoryNumber());//水1配比
                            datelist[4] = 1;
                        } else if (datelist[4] == 1) {
                            sendObject.set("water_2", bhzCementDetail.getRealityNumber());//    水2
                            sendObject.set("th_water_2", bhzCementDetail.getTheoryNumber());//    水2配比
                            datelist[4] = 2;
                        }
                    }
                    if (bhzCementDetail.getMaterialeType() == 6) {//水泥
                        if (datelist[5] == 0) {
                            sendObject.set("sn_1", bhzCementDetail.getRealityNumber());//水泥1
                            sendObject.set("th_sn_1", bhzCementDetail.getTheoryNumber());//水泥1配比
                            datelist[5] = 1;
                        } else if (datelist[5] == 1) {
                            sendObject.set("sn_2", bhzCementDetail.getRealityNumber());//水泥2
                            sendObject.set("th_sn_2", bhzCementDetail.getTheoryNumber());//水泥2配比
                            datelist[5] = 2;
                        } else if (datelist[5] == 2) {
                            sendObject.set("sn_3", bhzCementDetail.getRealityNumber());//水泥3
                            sendObject.set("th_sn_", bhzCementDetail.getTheoryNumber());//水泥3配比
                            datelist[5] = 3;
                        }
                    }
                    if (bhzCementDetail.getMaterialeType() == 7) {//矿粉&&粉料
                        if (datelist[6] == 0) {
                            sendObject.set("kf_1", bhzCementDetail.getRealityNumber());//矿粉1
                            sendObject.set("th_kf_1", bhzCementDetail.getTheoryNumber());//矿粉1配比
                            datelist[6] = 1;
                        } else if (datelist[6] == 1) {
                            sendObject.set("kf_2", bhzCementDetail.getRealityNumber());//矿粉2
                            sendObject.set("th_kf_2", bhzCementDetail.getTheoryNumber());//矿粉2配比
                            datelist[6] = 2;
                        }
                    }
                    if (bhzCementDetail.getMaterialeType() == 8) {//粉煤灰
                        if (datelist[7] == 0) {
                            sendObject.set("mh_1", bhzCementDetail.getRealityNumber());//煤灰1
                            sendObject.set("th_mh_1",bhzCementDetail.getTheoryNumber());//煤灰1配比
                            datelist[7] = 1;
                        } else if (datelist[7] == 1) {
                            sendObject.set("mh_2", bhzCementDetail.getRealityNumber());//煤灰2
                            sendObject.set("th_mh_2",bhzCementDetail.getTheoryNumber());//煤灰2配比
                            datelist[7] = 2;
                        }
                    }
                    if (bhzCementDetail.getMaterialeType() == 9) {//外加剂/减水剂
                        if (bhzCementDetail.getMaterialeName().contains("减水剂")){
                            if (datelist[8] == 0) {
                                sendObject.set("jsj_1", bhzCementDetail.getRealityNumber());//减水剂1
                                sendObject.set("th_jsj_1",bhzCementDetail.getTheoryNumber());//减水剂1配比
                                datelist[8] = 1;
                            } else if (datelist[8] == 1) {
                                sendObject.set("jsj_2", bhzCementDetail.getRealityNumber());//减水剂2
                                sendObject.set("th_jsj_2",bhzCementDetail.getTheoryNumber());//减水剂2配比
                                datelist[8] = 2;
                            }
                        }
                        if (bhzCementDetail.getMaterialeName().contains("外加剂")){
                            if (datelist[9] == 0) {
                                sendObject.set("wjj_1", bhzCementDetail.getRealityNumber());//外加剂1
                                sendObject.set("th_wjj_1",bhzCementDetail.getTheoryNumber());//外加剂1配比
                                datelist[9] = 1;
                            } else if (datelist[9] == 1) {
                                sendObject.set("wjj_2", bhzCementDetail.getRealityNumber());//外加剂2
                                sendObject.set("th_wjj_2",bhzCementDetail.getTheoryNumber());//外加剂2配比
                                datelist[9] = 2;
                            } else if (datelist[9] == 2) {
                                sendObject.set("wjj_3", bhzCementDetail.getRealityNumber());//外加剂3
                                sendObject.set("th_wjj_3",bhzCementDetail.getTheoryNumber());//外加剂3配比
                                datelist[9] = 3;
                            } else if (datelist[9] == 3) {
                                sendObject.set("wjj_4", bhzCementDetail.getRealityNumber());//外加剂4
                                sendObject.set("th_wjj_4",bhzCementDetail.getTheoryNumber());//外加剂4配比
                                datelist[9] = 4;
                            }
                        }
                    }
                }
            }
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1NTY2MDAyNzYwODQsInVpZCI6MSwiaWF0IjoxNTU2NTEzODc2MDg0fQ.J034Wc5GgMTbLOsWGEp3qGnxSjf_IZHQZmv1kf2Gkfk";
            String source = "gkxt-api";
            String url = "http://58.213.77.74:10724/gkxt-api/api/receive/hcdata";
            String body = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .header("token",token)
                    .header("source",source)
                    .body(String.valueOf(sendObject))
                    .execute()
                    .body();
            System.out.println(body);
            JSONObject jsonObject1 = new JSONObject(body);
            String codes = (String) jsonObject1.get("code");
            if ("200".equals(codes)) {
                log.info("太仓拌合站数据推送成功!" + id + "Json数据" + sendObject + DateUtils.now());
                sysConfigService.updateSysConfig(JobUtil.TAICANG_TBHZDATA, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info("太仓拌合站数据推送失败!" + id + "Json数据" + sendObject+ DateUtils.now());
            }
        }
    }
}
