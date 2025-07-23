package org.jeecg.modules.job.stprojectjob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class STzlsxJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZL_SX);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台张拉定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台张拉的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrZhanglaXxb> zhanglaXxbList = zhanglaXxbService.selectBYSBList(shebeilist, curid);
        if (null == zhanglaXxbList || zhanglaXxbList.size() == 0) {
            log.info(String.format("暂无苏台张拉的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TrZhanglaXxb trZhanglaXxb : zhanglaXxbList) {
            if (trZhanglaXxb.getGjbh().contains("二期")) {
                id = trZhanglaXxb.getId();
                trZhanglaXxb.setShebeibianhao("stgseqtjyb01");//苏台高速二期土建一标一号张拉机
                List<TrZhanglaM> trZhanglaMList = zhanglaMService.selectmList(trZhanglaXxb.getSyjid());
                for (TrZhanglaM trZhanglaM : trZhanglaMList) {
                    zhanglaMService.updateSbjById("stgseqtjyb01", trZhanglaM.getId());
                }
                boolean b = zhanglaXxbService.updateById(trZhanglaXxb);
                if (b) {
                    sysConfigService.updateSysConfig(JobUtil.ZL_SX, id);//根据传过来的唯一值来修改当前判断到的数据id
                    log.info("苏台高速二期土建一标一号张拉机数据保存成功!!!");
                } else {
                    log.info("苏台高速二期土建一标一号张拉机数据保存失败...");
                }
            } else {
                id = trZhanglaXxb.getId();
                sysConfigService.updateSysConfig(JobUtil.ZL_SX, id);//根据传过来的唯一值来修改当前判断到的数据id
                log.info("未筛选到需要的张拉机数据...");
            }
        }
    }
}
