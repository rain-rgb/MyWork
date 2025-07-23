package org.jeecg.modules.job.sutaiTwoFivejob;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class STTFSyjJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private SuTaiTFUtil suTaiTFUtil;
    @Autowired
    private ITSyjzbService itSyjzbService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.STTWO_FIVESYJ);//苏台二期二标试验机
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台试验机定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台试验机的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
//        String[] split = shebeilist.split(",");
//        List<String> strsToList1 = Arrays.asList(split);
        List<TSyjzb> tSyjzbs = itSyjzbService.selectSTlist(shebeilist, curid);
        if (null == tSyjzbs || tSyjzbs.size() == 0) {
            log.info(String.format("暂无苏台试验机的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        JSONArray jsonArray = new JSONArray();
        for (TSyjzb tSyjzb : tSyjzbs) {
            id = tSyjzb.getId();
            JSONObject saveDTOList = new JSONObject();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(tSyjzb.getSbbh());
            if (selectshebeione.getSbtype() == 4) {
                saveDTOList.set("laboratoryType", 1);//1 压力机 2 万能机 3抗折抗压
                saveDTOList.set("strengthNum", tSyjzb.getQddbz());//强度代表值
                saveDTOList.set("testSize", tSyjzb.getSjcc());//试件尺寸
            } else if (selectshebeione.getSbtype() == 3) {
                saveDTOList.set("laboratoryType", 2);//1 压力机 2 万能机 3抗折抗压
                if(tSyjzb.getSylx() != null && (tSyjzb.getSylx().equals("100047")||tSyjzb.getSylx().equals("100048")||tSyjzb.getSylx().equals("100049"))){
                    saveDTOList.set("strengthNum", tSyjzb.getPzbm());//强度代表值
                    saveDTOList.set("testSize", tSyjzb.getGczj());//试件尺寸
                }else{
                    saveDTOList.set("strengthNum", tSyjzb.getQddbz());//强度代表值
                    saveDTOList.set("testSize", tSyjzb.getSjcc());//试件尺寸
                }
            } else if (selectshebeione.getSbtype() == 12) {
                saveDTOList.set("laboratoryType", 3);//1 压力机 2 万能机 3抗折抗压
                saveDTOList.set("makeTime", tSyjzb.getZzrq());//质检日期
                saveDTOList.set("strengthNum", tSyjzb.getQddbz());//强度代表值
                saveDTOList.set("testSize", tSyjzb.getSjcc());//试件尺寸
            }
            saveDTOList.set("designStrength", tSyjzb.getSjqd());//设计强度
            saveDTOList.set("equipmentNo", tSyjzb.getSbbh());//设备编号
            Map map = new HashMap();
            map.put("key", tSyjzb.getSylx());
            saveDTOList.set("experimentType", map);//实验类型
            saveDTOList.set("instarTime", tSyjzb.getLq());//	龄期
            saveDTOList.set("operator", tSyjzb.getCzry());//操作员
            if(tSyjzb.getCjmc() != null){
                saveDTOList.set("projectName", tSyjzb.getCjmc());//	工程名称
            }else{
                saveDTOList.set("projectName", tSyjzb.getSgbw());//	工程名称
            }
            saveDTOList.set("sectionType", 5);//1 一标 2 二标 3 三标 4 四标 5 五标
            saveDTOList.set("result", tSyjzb.getPdjg());//判定结果
            saveDTOList.set("testNum", tSyjzb.getSjsl());//试件数量
            saveDTOList.set("specifiedNumber",tSyjzb.getSjbh());
            saveDTOList.set("experimentTime", tSyjzb.getSyrq());//试验日期
            jsonArray.add(saveDTOList);
        }
        Integer integer = suTaiTFUtil.PostSTSyjList(jsonArray);
        if (integer == 200) {
            log.info(String.format("苏台试验机数据推送成功!" + id));
            sysConfigService.updateSysConfig(JobUtil.STTWO_FIVESYJ, id);//根据传过来的唯一值来修改当前判断到的数据id
        } else {
            log.info(String.format("苏台试验机数据推送失败!" + id));
        }
    }
}
