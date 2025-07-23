package org.jeecg.modules.job.chuanzangwzjcGB;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.HttpRequestUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * \* @author: zml
 * \* Date: 2022/07/07
 * \* Time: 10:33
 * \* Description:  获取川藏地磅数据定时任务
 * \
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class wzjcGBJob implements Job {
    @Autowired
    ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.CHANZANG_WZGB);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info("未获取到川藏地磅数据定时任务配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info("没有配置要传输地磅的设备" + DateUtils.now());
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        for (Object object : split) {
            String shebeibianhao = String.valueOf(object).replace("'", "");
            ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(shebeibianhao);
            try {
                String post = HttpRequest.get("http://120.27.223.14:8081/cztlwz/rest/wzjc?shebeibianhao=" + shebeibianhao)
                        .header("Content-Type", "application/json")
                        .execute()
                        .body();
                JSONObject data = new JSONObject(post);
                System.out.println(data);
                if ((boolean) data.get("success")) {
                    String datas = String.valueOf(data.get("obj"));
                    if (!"null".equals(datas)) {
                        JSONObject data1 = (JSONObject) data.get("obj");
                        String jinchuliaodanno = String.valueOf(data1.get("jinchuliaodanno"));
                        Wzycljinchanggb wzycljinchanggb1 = wzycljinchanggbService.selectone(jinchuliaodanno);
                        if (wzycljinchanggb1 != null) {
                            log.info("该川藏过磅数据已存在!");
                        } else {
                            Wzycljinchanggb wzycljinchanggb = new Wzycljinchanggb();
                            wzycljinchanggb.setJinchuliaodanno(jinchuliaodanno);
                            wzycljinchanggb.setJinchangshijian(String.valueOf(data1.get("jinchangshijian")));
                            wzycljinchanggb.setChuchangshijian(String.valueOf(data1.get("chuchangshijian")));
                            wzycljinchanggb.setCailiaono(String.valueOf(data1.get("cailiaoname")));
                            wzycljinchanggb.setShebeibianhao(shebeibianhao);
                            wzycljinchanggb.setGongyingshangdanweibianma(String.valueOf(data1.get("gongyingshangname")));
                            wzycljinchanggb.setLcbianhao(String.valueOf(data1.get("name")));
                            if (!"null".equals(String.valueOf(data1.get("candi")))) {
                                wzycljinchanggb.setCandi(String.valueOf(data1.get("candi")));
                            }
                            wzycljinchanggb.setPici(String.valueOf(data1.get("pici")));
                            wzycljinchanggb.setSibangyuan(String.valueOf(data1.get("sibangyuan")));
                            wzycljinchanggb.setQianchepai(String.valueOf(data1.get("qianchepai")));
                            wzycljinchanggb.setMaozhong(String.valueOf(data1.get("maozhong")));
                            wzycljinchanggb.setMaozhongt(String.valueOf(data1.get("maozhongt")));
                            wzycljinchanggb.setPizhong(String.valueOf(data1.get("pizhong")));
                            wzycljinchanggb.setPizhongt(String.valueOf(data1.get("pizhongt")));
                            wzycljinchanggb.setJingzhong(String.valueOf(data1.get("jingzhong")));
                            wzycljinchanggb.setJingzhongt(String.valueOf(data1.get("jingzhongt")));
                            wzycljinchanggb.setSerialno(String.valueOf(data1.get("serialno")));
                            if (!"null".equals(String.valueOf(data1.get("ccgkpicname")))) {
                                wzycljinchanggb.setCcgkpic(String.valueOf(data1.get("ccgkpicname")));
                            }
                            if (!"null".equals(String.valueOf(data1.get("ccbfpicname")))) {
                                wzycljinchanggb.setCcbfpic(String.valueOf(data1.get("ccbfpicname")));
                            }
                            if (!"null".equals(String.valueOf(data1.get("cccppicname")))) {
                                wzycljinchanggb.setCccppic(String.valueOf(data1.get("cccppicname")));
                            }
                            if (!"null".equals(String.valueOf(data1.get("songhuodanpicname")))) {
                                wzycljinchanggb.setSonghuodanpic(String.valueOf(data1.get("songhuodanpicname")));
                            }
                            if (!"null".equals(String.valueOf(data1.get("cchcppicname")))) {
                                wzycljinchanggb.setCchcppic(String.valueOf(data1.get("cchcppicname")));
                            }
                            if (!"null".equals(String.valueOf(data1.get("Jccppicname")))) {
                                wzycljinchanggb.setJccppic(String.valueOf(data1.get("Jccppicname")));
                            }

                            boolean save = wzycljinchanggbService.save(wzycljinchanggb);
                            if (save) {
                                log.info("川藏过磅数据添加成功!!" + shebeibianhao + "设备编号" + DateUtils.now());
                            } else {
                                log.info("川藏过磅数据添加失败!!" + shebeibianhao + "设备编号" + DateUtils.now());
                            }
                        }
                    } else {
                        log.info("暂无川藏过磅数据!");
                    }
                } else {
                    log.info("获取川藏过磅数据失败!");
                }
                log.info("获取川藏地磅数据定时任务" + DateUtils.now());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
