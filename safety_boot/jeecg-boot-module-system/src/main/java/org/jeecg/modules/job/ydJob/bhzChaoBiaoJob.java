package org.jeecg.modules.job.ydJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * @ClassName bhzChaoBiaoJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/6/17 15:08
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class bhzChaoBiaoJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;

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
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzCementBase> bhzCementBases = bhzCementBaseService.selectLists2(shebeilist, curid);
        if (null == bhzCementBases || bhzCementBases.size() == 0) {
            log.info(String.format("暂无义东拌合站未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (BhzCementBase bhzCementBase : bhzCementBases) {
            id = bhzCementBase.getId();
            JSONObject sendDate = new JSONObject();
            QueryWrapper<BhzCementDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("batch_no",bhzCementBase.getBatchNo());
            List<BhzCementDetail> bhzCementDetails = bhzCementDetailService.list(queryWrapper);
            int gl1 = 0;
            int sn = 0;
            int fmh = 0;
            int shui = 0;
            int waijiaji = 0;
            for (BhzCementDetail bhzCementDetail : bhzCementDetails){
                Integer materialeType = bhzCementDetail.getMaterialeType();
                String materialeName = bhzCementDetail.getMaterialeName();
                if (gl1==0&&(materialeType<=4||materialeName.contains("骨料"))){
                    gl1 = 1;
                    sendDate.set("sha1_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("sha1_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("glper1",bhzCementDetail.getOverValue());
                    sendDate.set("glw1",bhzCementDetail.getErrorValue());

                }else if (gl1==1&&(materialeType<=4||materialeName.contains("骨料"))){
                    gl1 = 2;
                    sendDate.set("shi1_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("shi1_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("glper2",bhzCementDetail.getOverValue());
                    sendDate.set("glw2",bhzCementDetail.getErrorValue());

                }else if (gl1==2&&(materialeType<=4||materialeName.contains("骨料"))){
                    gl1 = 3;
                    sendDate.set("shi2_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("shi2_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("glper3",bhzCementDetail.getOverValue());
                    sendDate.set("glw3",bhzCementDetail.getErrorValue());

                }else if (gl1==3&&(materialeType<=4||materialeName.contains("骨料"))){
                    gl1 = 4;
                    sendDate.set("sha2_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("sha2_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("glper4",bhzCementDetail.getOverValue());
                    sendDate.set("glw4",bhzCementDetail.getErrorValue());

                }else if (gl1==4&&(materialeType<=4||materialeName.contains("骨料"))){
                    gl1 = 5;
                    sendDate.set("guliao5_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("guliao5_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("glper5",bhzCementDetail.getOverValue());
                    sendDate.set("glw5",bhzCementDetail.getErrorValue());

                }
                if (sn==0&&(materialeType==6||materialeName.contains("水泥"))){
                    sn = 1;
                    sendDate.set("shuini1_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("shuini1_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("flper1",bhzCementDetail.getOverValue());
                    sendDate.set("flw1",bhzCementDetail.getErrorValue());

                }else if (sn==1&&(materialeType==6||materialeName.contains("水泥"))){
                    sn = 2;
                    sendDate.set("shuini2_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("shuini2_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("flper2",bhzCementDetail.getOverValue());
                    sendDate.set("flw2",bhzCementDetail.getErrorValue());

                }
                if (!materialeName.contains("水泥")&& (materialeType==7&&materialeName.contains("矿粉"))){

                    sendDate.set("kuangfen3_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("kuangfen3_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("flper3",bhzCementDetail.getOverValue());
                    sendDate.set("flw3",bhzCementDetail.getErrorValue());

                }else if (!materialeName.contains("水泥")&&fmh==0&&(materialeType==7||materialeName.contains("粉煤灰"))){
                    fmh = 1;
                    sendDate.set("feimeihui4_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("feimeihui4_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("flper4",bhzCementDetail.getOverValue());
                    sendDate.set("flw4",bhzCementDetail.getErrorValue());

                }else if (!materialeName.contains("水泥")&&fmh==1&&(materialeType==7||materialeName.contains("粉煤灰"))){
                    fmh = 2;
                    sendDate.set("fenliao5_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("fenliao5_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("flper5",bhzCementDetail.getOverValue());
                    sendDate.set("flw5",bhzCementDetail.getErrorValue());

                }
                if (!materialeName.contains("水泥")&&shui == 0&&(materialeType==5||materialeName.contains("水"))){
                    shui = 1;
                    sendDate.set("shui1_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("shui1_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("shper1",bhzCementDetail.getOverValue());
                    sendDate.set("shw1",bhzCementDetail.getErrorValue());

                }else if (!materialeName.contains("水泥")&&shui == 1&&(materialeType==5||materialeName.contains("水"))){
                    sendDate.set("shui2_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("shui2_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("shper2",bhzCementDetail.getOverValue());
                    sendDate.set("shw2",bhzCementDetail.getErrorValue());
                }
                if (waijiaji == 0&&(materialeType==9||materialeName.contains("外"))){
                    waijiaji = 1;
                    sendDate.set("waijiaji1_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("waijiaji1_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("wjper1",bhzCementDetail.getOverValue());
                    sendDate.set("wjw1",bhzCementDetail.getErrorValue());
                }else if (waijiaji == 1&&(materialeType==9||materialeName.contains("外"))){
                    waijiaji = 2;
                    sendDate.set("waijiaji2_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("waijiaji2_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("wjper2",bhzCementDetail.getOverValue());
                    sendDate.set("wjw2",bhzCementDetail.getErrorValue());
                }else if (waijiaji == 2&&(materialeType==9||materialeName.contains("外"))){
                    waijiaji = 3;
                    sendDate.set("waijiaji3_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("waijiaji3_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("wjper3",bhzCementDetail.getOverValue());
                    sendDate.set("wjw3",bhzCementDetail.getErrorValue());
                }else if (waijiaji == 3&&(materialeType==9||materialeName.contains("外"))){
                    waijiaji = 4;
                    sendDate.set("waijiaji4_shijizhi",bhzCementDetail.getRealityNumber());
                    sendDate.set("waijiaji4_lilunzhi",bhzCementDetail.getTheoryNumber());
                    sendDate.set("wjper4",bhzCementDetail.getOverValue());
                    sendDate.set("wjw4",bhzCementDetail.getErrorValue());
                }

            }
            sendDate.set("gongdanhao",bhzCementBase.getWorkNo());
            sendDate.set("chaozuozhe",bhzCementBase.getHandlers());
            sendDate.set("chuliaoshijian",sdf.format(bhzCementBase.getProductDatetime()));
            sendDate.set("gongchengmingcheng",bhzCementBase.getProjectName());
            sendDate.set("sigongdidian",bhzCementBase.getJobLocation());
            sendDate.set("jiaozuobuwei",bhzCementBase.getPoureLocation());
            sendDate.set("shuinipingzhong",bhzCementBase.getCementVariety());
            sendDate.set("waijiajipingzhong",bhzCementBase.getAdditiveVariety());
            sendDate.set("peifanghao",bhzCementBase.getFormulaNo());
            sendDate.set("qiangdudengji",bhzCementBase.getStrengthRank());
            sendDate.set("jiaobanshijian",bhzCementBase.getStirDatetime());
            sendDate.set("shebeibianhao",bhzCementBase.getShebeiNo());
            sendDate.set("baocunshijian",sdf.format(bhzCementBase.getSaveDatetime()));
            sendDate.set("kehuduanbianhao",bhzCementBase.getClientNo());
            sendDate.set("caijishijian",sdf.format(bhzCementBase.getCollectDatetime()));
            sendDate.set("gujifangshu",bhzCementBase.getEstimateNumber());
//            sendDate.set("phbid",bhzCementBase.get);
            sendDate.set("phbfangliang",bhzCementBase.getEstimateNumber());
            sendDate.set("sgphbNo",bhzCementBase.getFormulaNo());
            sendDate.set("isbzb","1");

            String back = HttpRequest.post("http://47.97.173.113:8081/yd/rest/BhzDateReceive/receive")
                    .body(String.valueOf(sendDate))
                    .execute()
                    .body();

            JSONObject jsonObject1 = new JSONObject(back);
            String codes = jsonObject1.get("code").toString();
            if ("200".equals(codes)) {
                log.info(String.format("义东拌合站推送成功!" + id + "Json数据" + sendDate));
            } else {
                log.info(String.format("义东拌合站推送失败!" + id + "Json数据" + sendDate));
            }
            sysConfigService.updateSysConfig(JobUtil.YD_BHZ, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
