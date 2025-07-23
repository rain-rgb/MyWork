package org.jeecg.modules.job.zhongjiaojob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.syj.entity.FWangnj;
import com.trtm.iot.syj.entity.FsYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.IFWangnjService;
import com.trtm.iot.syj.service.IFYalijiService;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName syjJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/11/23 14:43
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class syjJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ITSyjzbService syjzbService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IFYalijiService yalijiService;
    @Autowired
    private IFWangnjService wangnjService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;

    private static String key = "8399c86ab521e411";
    private static String secret = "116b179ee7c74e08a64def0b8f8757e8";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZJ_SYJSC);//拌合站生产（推送中交）
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到中交定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输中交试验机的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");

        List<TSyjzb> selectzj = syjzbService.selectzj(shebeilist, curid);
        if (null == selectzj || selectzj.size() == 0) {
            log.info(String.format("暂无中交砼拌合站的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (TSyjzb tSyjzb : selectzj) {
            id = tSyjzb.getId();
            JSONObject sendmap = JSONUtil.createObj();
            QueryWrapper<FsYaliji> queryWrapper = new QueryWrapper();
            queryWrapper.eq("syjid", tSyjzb.getSyjid());
            List<FsYaliji> yalijis = yalijiService.list(queryWrapper);
            List kyqd = new ArrayList();
            for (FsYaliji fsYaliji : yalijis) {
                kyqd.add(fsYaliji.getKyqd());
            }
            sendmap.set("compressivestrength", kyqd);
            sendmap.set("test_reportcode", tSyjzb.getSjbh());

            sendmap.set("project_code", "101285513");
            sendmap.set("pouringorder_no", "");
            sendmap.set("structuralposition_code", "");

            QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
            sysDictQueryWrapper.eq("dict_code", "SJQD");
            SysDict one = sysDictService.getOne(sysDictQueryWrapper);
            String id1 = one.getId();

            QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
            sysDictItemQueryWrapper.eq("dict_id", id1);
            sysDictItemQueryWrapper.eq("item_text", tSyjzb.getSjqd());
            SysDictItem one1 = sysDictItemService.getOne(sysDictItemQueryWrapper);
            sendmap.set("strengthgrade_code", one1.getItemValue());
            sendmap.set("taskConsPos", tSyjzb.getCjmc());
            String syrq = tSyjzb.getSyrq();
            if (syrq.length()<11){
                syrq += " 00:00:00";
            }
            sendmap.set("testtime", syrq);
            sendmap.set("age", tSyjzb.getLq());
            sendmap.set("sizetype", tSyjzb.getSjcc());
            sendmap.set("area", tSyjzb.getSjmj());
            System.out.println(sendmap);

            long time = new Date().getTime();
            String sign = MD5Util.MD5Encode(time + "#" + secret, "utf-8");

            String url = "http://concretedevice.prod.shecltd.com/concretedevice/test/testblockstrength/add?app_key=" + key + "&timestamp=" + time + "&sign=" + sign;
            String result = HttpRequest.post(url)
                    .body(String.valueOf(sendmap))
                    .execute()
                    .body();

            JSONObject jsonObject1 = new JSONObject(result);
            String message = jsonObject1.get("message").toString();
            if ("操作成功".equals(message)) {
                log.info(String.format("试验室推送中交成功!" + id + "返回结果" + result));
            } else {
                log.info(String.format("试验室推送中交失败!" + id + "Json数据" + sendmap));
            }
            sysConfigService.updateSysConfig(JobUtil.ZJ_SYJSC, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
