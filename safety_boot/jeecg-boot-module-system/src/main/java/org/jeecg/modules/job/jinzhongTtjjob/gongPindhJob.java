package org.jeecg.modules.job.jinzhongTtjjob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IShigongphbService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
/**
 * 出厂消耗净重统计
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class gongPindhJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShigongphbService shigongphbService;
    @Autowired
    private IWzliaocangService wzliaocangService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BZB_GPDH);//标准版，净重T统计
        //如果为空
        if (null == selectsysconfigone) {
            log.info("未获取到瑞仓高速原材料检验批数据推送到实验平台定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();

        int id = 0;
        List<Shigongphb> list = shigongphbService.selectlistjz(curid,0);
        if (list.size() > 0){
            for(Shigongphb l :list){
                id = l.getId();
                if (l.getLc1() != null && l.getRu1() != null){
                    tongJ(l.getLc1(),l.getRu1()); //统计方法
                }
                if (l.getLc2() != null && l.getRu2() != null){
                    tongJ(l.getLc2(),l.getRu2()); //统计方法
                }
                if (l.getLc3() != null && l.getRu3() != null){
                    tongJ(l.getLc3(),l.getRu3()); //统计方法
                }
                if (l.getLc4() != null && l.getRu4() != null){
                    tongJ(l.getLc4(),l.getRu4()); //统计方法
                }
                if (l.getLc5() != null && l.getRu5() != null){
                    tongJ(l.getLc5(),l.getRu5()); //统计方法
                }
                if (l.getLc6() != null && l.getRu6() != null){
                    tongJ(l.getLc6(),l.getRu6()); //统计方法
                }
                if (l.getLc7() != null && l.getRu7() != null){
                    tongJ(l.getLc7(),l.getRu7()); //统计方法
                }
                if (l.getLc8() != null && l.getRu8() != null){
                    tongJ(l.getLc8(),l.getRu8()); //统计方法
                }
                if (l.getLc9() != null && l.getRu9() != null){
                    tongJ(l.getLc9(),l.getRu9()); //统计方法
                }
                if (l.getLc11() != null && l.getRu11() != null){
                    tongJ(l.getLc11(),l.getRu11()); //统计方法
                }
                if (l.getLc12() != null && l.getRu12() != null){
                    tongJ(l.getLc12(),l.getRu12()); //统计方法
                }
                if (l.getLc13() != null && l.getRu13() != null){
                    tongJ(l.getLc13(),l.getRu13()); //统计方法
                }

                l.setStatistic(1);
                shigongphbService.updateById(l);
            }
        }else {
            log.info("暂无需要统计的数据。");
        }
        sysConfigService.updateSysConfig(JobUtil.BZB_GPDH, id);//根据传过来的唯一值来修改当前判断到的数据id
    }

    public void tongJ(String s,Double ru){
        QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("guid",s);
        Wzliaocang one = wzliaocangService.getOne(queryWrapper);

        if (one != null){
            one.setLjshiyong(one.getLjshiyong()+ru*0.001);
            wzliaocangService.updateById(one);
        }else {
            log.info("未搜索到对应的料仓！！！");
        }
    }
}