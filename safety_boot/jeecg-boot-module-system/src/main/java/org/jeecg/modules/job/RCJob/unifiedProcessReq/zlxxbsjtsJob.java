package org.jeecg.modules.job.RCJob.unifiedProcessReq;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.hntbhz.vo.BhzCementBasePage;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import com.trtm.iot.zhanglaxxb.vo.Trzhanglaxxbmsss;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class zlxxbsjtsJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IBhzCementOverHandlerService bhzCementOverHandlerService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZLXXBM_QIANYI);//曹宅项目拌合站生产数据推送标准版平台
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到拌合站推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<TrZhanglaXxb> selectbigid = zhanglaXxbService.selectbigid(curid);
        if (null == selectbigid || selectbigid.size() == 0) {
            log.info(String.format("暂无拌合站未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrZhanglaXxb zhanglaXxb : selectbigid) {

            Trzhanglaxxbmsss trzhanglaxxbmsss = new Trzhanglaxxbmsss();
            BeanUtils.copyProperties(trzhanglaxxbmsss, zhanglaXxb);

            String bhzCementBaseCZ = JSON.toJSONString(trzhanglaxxbmsss, SerializerFeature.WRITE_MAP_NULL_FEATURES);
            com.alibaba.fastjson.JSONObject object = JSON.parseObject(bhzCementBaseCZ);
            String back = HttpRequest.post("http://z.traiot.cn/jeecg-boot/hntbhz/bhzCementBase/addCZ")
                    .body(String.valueOf(trzhanglaxxbmsss))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200) {
                log.info(String.format("曹宅项目拌合站数据推送成功" + DateUtils.now()));
                sysConfigService.updateSysConfig(JobUtil.ZLXXBM_QIANYI, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("曹宅项目拌合站数据推送失败" + DateUtils.now()));
            }
        }
    }
}
