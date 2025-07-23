package org.jeecg.modules.job.wztaizhangjob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * \* @author:
 * \* Date: 2021/11/03
 * \* Time: 15:00
 * \* Description:义东料仓数据推送(义东高速：A05A01A03A01A01A01)
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LiaocangTuiSongYDJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private JobUtil jobUtil;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.LC_YDTS);//料仓数据推送义东高速
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到料仓数据推送义东高速配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东高速料仓" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1= Arrays.asList(split);
        List<Wzliaocang> selectwzlcones = wzliaocangService.selectwzlcList(curid,strsToList1);//所有未推送的数据
        if (null == selectwzlcones || selectwzlcones.size() == 0) {
            log.info(String.format("暂无未推送的料仓数据" + DateUtils.now()));
            return;
        }
        int id=0;
        for (Wzliaocang wzliaocang : selectwzlcones) {
            id = wzliaocang.getId();
            JSONObject publicPitchList1 = new JSONObject();
            if (wzliaocang.getWeight() != null){
                publicPitchList1.putOpt("picizhong",wzliaocang.getWeight());//重量（吨）
            } else {
                if (wzliaocang.getPicizhong() != null) {
                    publicPitchList1.putOpt("picizhong", wzliaocang.getPicizhong());//重量（吨）
                }else {
                    publicPitchList1.putOpt("picizhong", 0);//重量（吨）
                }
            }
            publicPitchList1.putOpt("name",wzliaocang.getName());
            publicPitchList1.putOpt("guid",wzliaocang.getGuid());
            publicPitchList1.putOpt("danwei","吨");
            publicPitchList1.putOpt("liaocangStatus",wzliaocang.getLiaocangStatus());
            publicPitchList1.putOpt("cailiaoname",wzliaocang.getCailiaoname());
            publicPitchList1.putOpt("sysOrgCode",wzliaocang.getSysOrgCode());
            Integer integer = jobUtil.GETYidongWzLC(String.valueOf(publicPitchList1));
            if(integer==200){
                log.info("物资料仓数据推义东高速送成功!");
            }else{
                log.info("物资料仓数据推义东高速送失败!");
            }
        }
        sysConfigService.updateSysConfig(JobUtil.LC_YDTS, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
