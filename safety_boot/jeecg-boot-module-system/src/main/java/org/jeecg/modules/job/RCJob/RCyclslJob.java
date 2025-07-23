package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCyclslJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCYCLSL);//瑞仓原材料收料推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓原材料收料推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        String token = selectsysconfigone.getToken();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓原材料收料的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Wzycljinchanggb> list = wzycljinchanggbService.selectyclList(curid, shebeilist);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无瑞仓原材料收料未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (Wzycljinchanggb wzycljinchanggb : list) {
            id = wzycljinchanggb.getId();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.set("jinchuliaodanNo", wzycljinchanggb.getJinchuliaodanno());
            jsonObject1.set("cailiaoNo", wzycljinchanggb.getCailiaono());
            jsonObject1.set("jinchangshijian", wzycljinchanggb.getJinchangshijian());
            jsonObject1.set("chuchangshijian", wzycljinchanggb.getChuchangshijian());
            jsonObject1.set("pici", wzycljinchanggb.getPici());
            jsonObject1.set("cheliangbianhao", wzycljinchanggb.getCheliangbianhao());
            jsonObject1.set("qianchepai", wzycljinchanggb.getQianchepai());
            jsonObject1.set("houchepai", wzycljinchanggb.getHouchepai());
            jsonObject1.set("maozhong", wzycljinchanggb.getMaozhong());
            jsonObject1.set("pizhong", wzycljinchanggb.getPizhong());
            jsonObject1.set("jingzhong", wzycljinchanggb.getJingzhong());
            jsonObject1.set("kouzhong", wzycljinchanggb.getKouzhong());
            jsonObject1.set("koulv", wzycljinchanggb.getKoulv());
            jsonObject1.set("chengzhongpiancha", wzycljinchanggb.getChengzhongpiancha());
            jsonObject1.set("liaocang", wzycljinchanggb.getLiaocang());
            jsonObject1.set("sibangyuan", wzycljinchanggb.getSibangyuan());
            jsonObject1.set("remark", wzycljinchanggb.getRemark());
            jsonObject1.set("shebeibianhao", wzycljinchanggb.getShebeibianhao());
            jsonObject1.set("gongyingshangdanweibianma", wzycljinchanggb.getGongyingshangdanweibianma());
            jsonObject1.set("guobangleibie", wzycljinchanggb.getGuobangleibie());
            jsonObject1.set("cheliangleixing", wzycljinchanggb.getCheliangleixing());
            jsonObject1.set("guid", wzycljinchanggb.getGuid());
            jsonObject1.set("ts", wzycljinchanggb.getTs());
            jsonObject1.set("isdel", wzycljinchanggb.getIsdel());
            jsonObject1.set("status", wzycljinchanggb.getStatus());
            jsonObject1.set("maozhongT", wzycljinchanggb.getMaozhongt());
            jsonObject1.set("pizhongT", wzycljinchanggb.getPizhongt());
            jsonObject1.set("candi", wzycljinchanggb.getCandi());
            jsonObject1.set("yunshudanwei", wzycljinchanggb.getYunshudanwei());
            jsonObject1.set("LCbianhao", wzycljinchanggb.getLcbianhao());
            jsonObject1.set("isshouliao", wzycljinchanggb.getIsshouliao());
            jsonObject1.set("jingzhongT", wzycljinchanggb.getJingzhongt());
            jsonObject1.set("istongji", wzycljinchanggb.getIstongji());
            jsonObject1.set("LiaoCangId", wzycljinchanggb.getLiaocangid());
            jsonObject1.set("serialNo", wzycljinchanggb.getSerialno());
            jsonObject1.set("reason", wzycljinchanggb.getReason());
            jsonObject1.set("file_upload", wzycljinchanggb.getFileUpload());
            jsonObject1.set("istaizhangtj", wzycljinchanggb.getIstaizhangtj());
            jsonObject1.set("songhuodanPic", wzycljinchanggb.getSonghuodanpic());
            jsonObject1.set("shifouhege", wzycljinchanggb.getShifouhege());
            jsonObject1.set("yuancaimiaoshu", wzycljinchanggb.getYuancaimiaoshu());
            jsonObject1.set("beizhu", wzycljinchanggb.getBeizhu());
            jsonObject1.set("jianlipic", wzycljinchanggb.getJianlipic());
            jsonObject1.set("sibanpic", wzycljinchanggb.getSibanpic());
            jsonObject1.set("JCGKPic", wzycljinchanggb.getJcgkpic());
            jsonObject1.set("JCCPPic", wzycljinchanggb.getJccppic());
            jsonObject1.set("JCHCPPic", wzycljinchanggb.getJchcppic());
            jsonObject1.set("JCBFPic", wzycljinchanggb.getJcbfpic());
            jsonObject1.set("CCGKPic", wzycljinchanggb.getCcgkpic());
            jsonObject1.set("CCCPPic", wzycljinchanggb.getCccppic());
            jsonObject1.set("CCHCPPic", wzycljinchanggb.getCchcppic());
            jsonObject1.set("CCBFPic", wzycljinchanggb.getCcbfpic());
            jsonObject1.set("taizhangtj", wzycljinchanggb.getTaizhangtj());
            jsonObject1.set("taizhangid", wzycljinchanggb.getTaizhangid());
            System.out.println(jsonObject1);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/yclsl/s/rawMaterialReceiveUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject1))
                    .execute()
                    .body();
            JSONObject jsonObject2 = new JSONObject(back);
            Integer code = (Integer) jsonObject2.get("code");
            if (code == 200 ) {
                log.info(String.format("瑞仓原材料收料推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCYCLSL, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else if(code == 402){
                log.info(String.format("瑞仓原材料收料推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCYCLSL, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞仓原材料收料推送失败!" + id));
            }

        }
    }
}
