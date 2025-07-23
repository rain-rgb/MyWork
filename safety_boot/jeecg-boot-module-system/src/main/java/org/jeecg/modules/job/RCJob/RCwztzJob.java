package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCwztzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCWZTZ);//瑞仓地磅wztaizhang推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓地磅wztaizhang推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓地磅wztaizhang推送数据的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Wztaizhang> list = wztaizhangService.selectLists(shebeilist, curid);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无瑞仓地磅wztaizhang未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for(Wztaizhang wztaizhang : list){
            id =wztaizhang.getId();
            JSONObject jsonObject1 =new JSONObject();
            jsonObject1.set("jinchangshijian",wztaizhang.getJinchangshijian());
            jsonObject1.set("shebeibianhao",wztaizhang.getShebeibianhao());
            jsonObject1.set("LCbianhao",wztaizhang.getLcbianhao());
            jsonObject1.set("cailiaoNo",wztaizhang.getCailiaono());
            jsonObject1.set("maozhongT",wztaizhang.getMaozhongt());
            jsonObject1.set("pizhongT",wztaizhang.getPizhongt());
            jsonObject1.set("jingzhongT",wztaizhang.getJingzhongt());
            jsonObject1.set("pici",wztaizhang.getPici());
            jsonObject1.set("guigexinghao",wztaizhang.getGuigexinghao());
            jsonObject1.set("create_time",wztaizhang.getCreateTime());
            jsonObject1.set("gblx",wztaizhang.getGblx());
            jsonObject1.set("jianyanstate",wztaizhang.getJianyanstate());
            jsonObject1.set("gongyingshangdanweibianma",wztaizhang.getGongyingshangdanweibianma());
            jsonObject1.set("isfinish",wztaizhang.getIsfinish());
            jsonObject1.set("isquyang",wztaizhang.getIsquyang());
            jsonObject1.set("baogaofile",wztaizhang.getBaogaofile());
            jsonObject1.set("yidongstatus",wztaizhang.getYidongstatus());
            jsonObject1.set("sys_org_code",wztaizhang.getSysOrgCode());
            jsonObject1.set("ruleway",wztaizhang.getRuleway());
            System.out.println(jsonObject1);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/wztz/wztz/wztaizhangUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200 ) {
                log.info(String.format("瑞仓地磅wztaizhang推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCWZTZ, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else if(code == 402){
                log.info(String.format("瑞仓地磅wztaizhang推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCWZTZ, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞仓地磅wztaizhang推送失败!" + id));
            }


        }
    }
}
