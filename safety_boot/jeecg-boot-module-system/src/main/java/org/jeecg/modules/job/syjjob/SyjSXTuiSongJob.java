package org.jeecg.modules.job.syjjob;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.trtm.iot.syj.entity.*;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.syj.vo.TSyjzbWnjPage;
import com.trtm.iot.syj.vo.YljPage;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SyjSXTuiSongJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService itSyjzbService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SYJ_TUISONGSX);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到试验机定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置需要推送试验机数据的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TSyjzb> syjList = itSyjzbService.selectSYJData(curid, shebeilist);
        if (null == syjList || syjList.size() == 0) {
            log.info(String.format("暂无试验机未判断的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        String[] sylxWnjList = {"100047", "100048", "100049", "200048", "200049", "100057"};
        String[] sylxYljList = {"100001", "100014", "100131", "100133", "100134", "100135", "100136", "100137", "100138", "100161", "100140", "100162", "100235", "100236"};
        for (TSyjzb syjOne : syjList) {
            id = syjOne.getId();
            syjOne.setId(null);
            String sylx = syjOne.getSylx();
            if (isHave(sylxWnjList, sylx)) {
                TSyjzbWnjPage tSyjzbWnjPage = new TSyjzbWnjPage();
                BeanUtil.copyProperties(syjOne, tSyjzbWnjPage);
                List<fswangnj> wangnjList = itSyjzbService.selectWnjList(syjOne.getSyjid());
                if (ObjectUtil.isNotEmpty(wangnjList)) {
                    for (fswangnj fswangnj : wangnjList) {
                        fswangnj.setId(null);
                    }
                    tSyjzbWnjPage.setFsWangnjList(wangnjList);
                }
                String syWnj = JSON.toJSONString(tSyjzbWnjPage, SerializerFeature.WRITE_MAP_NULL_FEATURES);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(syWnj);
                String back = HttpRequest.post("http://47.98.32.195:8026/jeecg-boot/syj/tSyjzb/addWNJOpen")
                        .body(String.valueOf(object))
                        .execute()
                        .body();
                JSONObject jsonObject2 = new JSONObject(back);
                Integer code = (Integer) jsonObject2.get("code");
                if (code == 200) {
                    log.info(String.format("试验万能机数据推送苏信成功" + DateUtils.now()));
                    sysConfigService.updateSysConfig(JobUtil.SYJ_TUISONGSX, id);//根据传过来的唯一值来修改当前判断到的数据id
                } else {
                    log.info(String.format("试验万能机数据推送苏信失败" + DateUtils.now()));
                }
            }
            if (isHave(sylxYljList, sylx)) {
                YljPage yljPage = new YljPage();
                BeanUtil.copyProperties(syjOne, yljPage);
                List<FsYaliji> yalijList = itSyjzbService.selectYljList(syjOne.getSyjid());
                if (ObjectUtil.isNotEmpty(yalijList)) {
                    for (FsYaliji fsYaliji : yalijList) {
                        fsYaliji.setId(null);
                    }
                    yljPage.setFsYalijiList(yalijList);
                }
                String syYlj = JSON.toJSONString(yljPage, SerializerFeature.WRITE_MAP_NULL_FEATURES);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(syYlj);
                String back = HttpRequest.post("http://47.98.32.195:8026/jeecg-boot/syj/tSyjzb/yljadd")
                        .body(String.valueOf(object))
                        .execute()
                        .body();
                JSONObject jsonObject2 = new JSONObject(back);
                Integer code = (Integer) jsonObject2.get("code");
                if (code == 200) {
                    log.info(String.format("试验压力机数据推送苏信成功" + DateUtils.now()));
                    sysConfigService.updateSysConfig(JobUtil.SYJ_TUISONGSX, id);//根据传过来的唯一值来修改当前判断到的数据id
                } else {
                    log.info(String.format("试验压力机数据推送苏信失败" + DateUtils.now()));
                }
            }
        }
    }


    public static boolean isHave(String[] strs, String s) {
        /*此方法有两个参数，第一个是要查找的字符串数组，第二个是要查找的字符或字符串*/
        int i = strs.length;
        while (i-- > 0) {
            if (s.equals(strs[i])) {
                return true;
            }
        }
        return false;
    }
}
