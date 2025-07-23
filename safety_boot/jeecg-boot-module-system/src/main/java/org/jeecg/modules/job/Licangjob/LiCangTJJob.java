package org.jeecg.modules.job.Licangjob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.mapper.WzliaocangMapper;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.mapper.WzycljinchanggbMapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 统计料仓累计进场
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LiCangTJJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private WzliaocangMapper wzliaocangMapper;
    @Autowired
    private WzycljinchanggbMapper wzycljinchanggbMapper;

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.TJ_LIAOCANG_JC);//查询料仓进场总数之前统计id
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到料仓统计信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        QueryWrapper<Wzycljinchanggb> wrapper = new QueryWrapper<>();
        wrapper.select("max(id) id, SUM(CAST(jingzhongT AS DECIMAL(10,5))) jingzhongT, lcbianhao").gt("id", curid).groupBy("LCbianhao").orderByDesc("id");
        List<Wzycljinchanggb> wzycljinchanggbs = wzycljinchanggbMapper.selectList(wrapper);
        if (oConvertUtils.isEmpty(wzycljinchanggbs) || wzycljinchanggbs.size() == 0) {
            log.info("没有要新的数据统计");
            return;
        }
        for (Wzycljinchanggb wzycljinchanggb : wzycljinchanggbs) {
            if (oConvertUtils.isEmpty(wzycljinchanggb.getLcbianhao()) || oConvertUtils.isEmpty(wzycljinchanggb.getJingzhongt())) {
                continue;
            }
            Double jinzhong = Double.valueOf(wzycljinchanggb.getJingzhongt());
            String lcbianhao = wzycljinchanggb.getLcbianhao();
            Wzliaocang wzliaocang = wzliaocangMapper.selectOne(new QueryWrapper<Wzliaocang>().lambda()
                    .select(Wzliaocang::getId, Wzliaocang::getLjjinchang, Wzliaocang::getLjshiyong, Wzliaocang::getLjxiuzheng, Wzliaocang::getKucunxh)
                    .eq(Wzliaocang::getGuid, lcbianhao).last("FOR UPDATE"));
            if (oConvertUtils.isNotEmpty(wzliaocang)) {
                wzliaocang.setLjjinchang(jinzhong + wzliaocang.getLjjinchang());
                wzliaocangMapper.updateById(wzliaocang);
                log.info("修改料仓 编号为：{} 料仓的进场总量：{}", lcbianhao, jinzhong);
            }
        }
        selectsysconfigone.setCurid(wzycljinchanggbs.get(0).getId());
        log.info("已经统计到的数据 {}", wzycljinchanggbs.get(0).getId());
        sysConfigService.updateById(selectsysconfigone);//修改统计之后的最大id，下次统计跳过之前统计的
    }

}
