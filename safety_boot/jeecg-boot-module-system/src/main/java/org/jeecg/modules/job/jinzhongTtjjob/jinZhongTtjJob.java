package org.jeecg.modules.job.jinzhongTtjjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.ruicangjob.RuiCangUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 进厂过磅净重统计
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class jinZhongTtjJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;
    @Autowired
    private IWzliaocangService wzliaocangService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BZB_JZTTJ);//标准版，净重T统计
        //如果为空
        if (null == selectsysconfigone) {
            log.info("未获取到瑞仓高速原材料检验批数据推送到实验平台定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();

        int id = 0;
        List<Wzycljinchanggb> list = wzycljinchanggbService.selectlistjz(curid,0);
        if (list.size() > 0){
            for(Wzycljinchanggb l :list){
                if (l.getLcbianhao() != null){
                    QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("guid",l.getLcbianhao());
                    Wzliaocang one = wzliaocangService.getOne(queryWrapper);

                    if (one != null){
                        one.setLjjinchang(one.getLjjinchang()+Double.parseDouble(l.getJingzhongt()));
                    }
                    id = l.getId();
                    l.setJingzhongttj(1);
                    wzliaocangService.updateById(one);
                    wzycljinchanggbService.updateById(l);
                }else {
                    log.info("该数据没有料仓编号！！！");
                }
            }
        }else {
            log.info("暂无需要统计的数据。");
        }
        sysConfigService.updateSysConfig(JobUtil.BZB_JZTTJ, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}