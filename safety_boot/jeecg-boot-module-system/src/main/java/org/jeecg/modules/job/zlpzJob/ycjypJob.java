package org.jeecg.modules.job.zlpzJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ycjypJob
 * @Author
 * @Date 2024/10/8 10:22
 * @Version
 * @Description
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ycjypJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZLPZ_YCJYP);//浙路品质原材检验批推送
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
            log.info(String.format("没有配置要传输试验机的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Wztaizhang> wztaizhangs = wztaizhangService.getListzlpz(shebeilist);
        if (null == wztaizhangs || wztaizhangs.size() == 0) {
            log.info(String.format("暂无试验机未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Wztaizhang wztaizhang : wztaizhangs) {
            id = wztaizhang.getId();
            List sendList = new ArrayList();
            JSONObject sendDate = new JSONObject();

            sendDate.set("id", "2205-330200-04-01-277386" + "_" + id);
            sendDate.set("projectId", "2205-330200-04-01-277386");
            sendDate.set("sectionId", "2205-330200-04-01-277386_SG_04");
            Integer jianyanstate = wztaizhang.getJianyanstate();
            int inspectionType = 2;
            if (jianyanstate>0){
                inspectionType = 1;
            }
            sendDate.set("inspectionType", inspectionType);//1施工自检；2监理抽检；3监督抽检
//            sendDate.set("name", );//1水泥；2沥青；3碎石；4砂；5石屑；6掺合料；7外加剂；8钢材；9钢绞线；10锚夹具；11静载锚固性能；12土工合成材料；13防水卷材；14压浆料15波纹管；16橡胶支座检；2监理抽检；3监督抽检
//            sendDate.set("totalCount", );//实验次数
//            sendDate.set("unqualifiedCount", );//不合格次数
            sendDate.set("inspectionTime", wztaizhang.getCreateTime());//检查时间
            String gongyingshangdanweibianma = wztaizhang.getGongyingshangdanweibianma();
            Wzgongyingshang selectnameone = wzgongyingshangService.selectnameone(gongyingshangdanweibianma);
            sendDate.set("manufacturer", selectnameone.getGongyingshangname());//生产厂家
            sendDate.set("unit", wztaizhang.getGuigexinghao());//规格
            sendDate.set("batchNumber", wztaizhang.getPici());//批次号
//            sendDate.set("part", );//使用部位
//            sendDate.set("detailList", );//不合格详情
//            sendDate.set("index", );//不合格指标
//            sendDate.set("value", );//不合格数据
//            sendDate.set("design", );//规范(设计值)
//            sendDate.set("pdyj", );//判定依据
//            sendDate.set("file", );//附件

            sendList.add(sendDate);

            JSONObject sendJsonObject = new JSONObject();

            sendJsonObject.set("serviceName","ZLPZ_ZX_CLCPTJ");
            sendJsonObject.set("token","93cd2c6567594107a16b51a65bcd5a37");
            sendJsonObject.set("updateNull","true");
            sendJsonObject.set("param",sendList);
            System.out.println(sendJsonObject);
            String url = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/push";
            String back = HttpRequest.post(url)
                    .body(sendJsonObject.toString())
                    .timeout(20000)
                    .execute().body();

            com.alibaba.fastjson.JSONObject backJson = JSON.parseObject(back);
            int result = backJson.getIntValue("result");
            if (result == 0) {
                wztaizhang.setIszlpz(1);
                log.info(String.format("试验机浙路品质推送成功!" + id));
            } else {
                wztaizhang.setIszlpz(2);
                log.info(String.format("试验机浙路品质推送失败!" + id));
            }
            wztaizhangService.updateById(wztaizhang);
            pushandreturnService.saveData(id,String.valueOf(sendDate),selectsysconfigone.getRemark(),back);
        }
    }
}