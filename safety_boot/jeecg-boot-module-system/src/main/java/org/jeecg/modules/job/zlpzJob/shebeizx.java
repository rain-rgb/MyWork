package org.jeecg.modules.job.zlpzJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.openapigpsdatavo.entity.Openapigpsdatavo;
import com.trtm.iot.openapigpsdatavo.service.IOpenapigpsdatavoService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import com.trtm.iot.zlpz.entity.Zlpz;
import com.trtm.iot.zlpz.service.IZlpzService;
import com.xkcoding.http.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName shebeizx：
 * @Description 设备在线
 * @Author 55314
 * @Date 2023/7/13 13:49
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class shebeizx implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IZlpzService zlpzService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzLqBasesService lqBasesService;
    @Autowired
    private IBhzSwBasesService swBasesService;
    @Autowired
    private ITSyjzbService syjzbService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private IOpenapigpsdatavoService openapigpsdatavoService;
    @Autowired
    private IPushandreturnService pushandreturnService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZLPZ_SBZX);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到定时任务的配置信息" + DateUtils.now()));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Zlpz> zlpzList = zlpzService.list();
        for (Zlpz zlpz : zlpzList) {

            String sbtype = zlpz.getSbtype();
            String shebeiid = zlpz.getShebeiid();
            String shebeino = zlpz.getShebeino();

            List sendList = new ArrayList();
            JSONObject sendDate = new JSONObject();
            sendDate.set("equipId", shebeiid);

            if ("0".equals(sbtype)) {//砼拌合站
                QueryWrapper<BhzCementBase> bhzCementBaseQueryWrapper = new QueryWrapper<>();
                bhzCementBaseQueryWrapper.select("COALESCE(DATE_FORMAT(MAX(product_datetime), '%Y-%m-%d %H:%i:%s'), '2023-07-01 00:00:00') as productDatetime");
                bhzCementBaseQueryWrapper.eq("shebei_no", shebeino);
                BhzCementBase one = bhzCementBaseService.getOne(bhzCementBaseQueryWrapper);
                sendDate.set("onlineDate", sdf.format(one.getProductDatetime()));

            } else if ("1".equals(sbtype)) {//沥青
                QueryWrapper<BhzLqBases> bhzLqBasesQueryWrapper = new QueryWrapper<>();
                bhzLqBasesQueryWrapper.select("COALESCE(DATE_FORMAT(MAX(chuliaoshijian), '%Y-%m-%d %H:%i:%s'), '2023-07-01 00:00:00') AS chuliaoshijian");
                bhzLqBasesQueryWrapper.eq("shebeibianhao", shebeino);
                BhzLqBases one = lqBasesService.getOne(bhzLqBasesQueryWrapper);
                String syrq = one.getChuliaoshijian();
                Date date = null;  // 将日期字符串解析为Date对象
                try {
                    date = sdf.parse(syrq);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = sdf.format(date);
                sendDate.set("onlineDate", formattedDate);

            } else if ("2".equals(sbtype)) {//水稳
                QueryWrapper<BhzSwBases> swBasesQueryWrapper = new QueryWrapper<>();
                swBasesQueryWrapper.select("COALESCE(DATE_FORMAT(MAX(chuliaoshijian), '%Y-%m-%d %H:%i:%s'), '2023-07-01 00:00:00') AS chuliaoshijian");
                swBasesQueryWrapper.eq("shebeibianhao", shebeino);
                BhzSwBases one = swBasesService.getOne(swBasesQueryWrapper);
                String syrq = one.getChuliaoshijian();

                Date date = null;  // 将日期字符串解析为Date对象
                try {
                    date = sdf.parse(syrq);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = sdf.format(date);
                sendDate.set("onlineDate", formattedDate);

            } else if ("3".equals(sbtype) || "4".equals(sbtype) || "12".equals(sbtype)) {//试验机
                QueryWrapper<TSyjzb> syjzbQueryWrapper = new QueryWrapper<>();
                syjzbQueryWrapper.select("COALESCE(DATE_FORMAT(MAX(SYRQ), '%Y-%m-%d %H:%i:%s'), '2023-07-01 00:00:00') AS syrq");
                syjzbQueryWrapper.eq("sbbh", shebeino);
                TSyjzb one = syjzbService.getOne(syjzbQueryWrapper);
                String syrq = one.getSyrq();

                Date date = null;  // 将日期字符串解析为Date对象
                try {
                    date = sdf.parse(syrq);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = sdf.format(date);
                sendDate.set("onlineDate", formattedDate);

            } else if ("9".equals(sbtype)) {//张拉
                QueryWrapper<TrZhanglaXxb> zhanglaXxbQueryWrapper = new QueryWrapper<>();
                zhanglaXxbQueryWrapper.select("COALESCE(DATE_FORMAT(MAX(sgsj), '%Y-%m-%d %H:%i:%s'), '2023-07-01 00:00:00') AS sgsj");
                zhanglaXxbQueryWrapper.eq("shebeibianhao", shebeino);
                TrZhanglaXxb one = zhanglaXxbService.getOne(zhanglaXxbQueryWrapper);
                String syrq = one.getSgsj();

                Date date = null;  // 将日期字符串解析为Date对象
                try {
                    date = sdf.parse(syrq);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = sdf.format(date);
                sendDate.set("onlineDate", formattedDate);

            } else if ("10".equals(sbtype)) {//压浆
                QueryWrapper<TrYajiangM> yajiangMQueryWrapper = new QueryWrapper<>();
                yajiangMQueryWrapper.select("COALESCE(DATE_FORMAT(MAX(yjsj), '%Y-%m-%d %H:%i:%s'), '2023-07-01 00:00:00') AS yjsj");
                yajiangMQueryWrapper.eq("yjsbbh", shebeino);
                TrYajiangM one = yajiangMService.getOne(yajiangMQueryWrapper);
                String syrq = one.getYjsj();
                Date date = null;  // 将日期字符串解析为Date对象
                try {
                    date = sdf.parse(syrq);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = sdf.format(date);
                sendDate.set("onlineDate", formattedDate);

            } else if ("47".equals(sbtype) || "48".equals(sbtype)) {//摊铺碾压
                QueryWrapper<Openapigpsdatavo> openapigpsdatavoQueryWrapper = new QueryWrapper<>();
                openapigpsdatavoQueryWrapper.select("COALESCE(DATE_FORMAT(MAX(gps_time), '%Y-%m-%d %H:%i:%s'), '2023-07-01 00:00:00')  as gpsTime");
                openapigpsdatavoQueryWrapper.eq("machine_id", shebeino);
                Openapigpsdatavo one = openapigpsdatavoService.getOne(openapigpsdatavoQueryWrapper);
                Date gpsTime = one.getGpsTime();
                String formattedDate = sdf.format(gpsTime);
                sendDate.set("onlineDate", formattedDate);
            }

            sendList.add(sendDate);

            JSONObject sendJsonObject = new JSONObject();
            sendJsonObject.set("serviceName", "ZLPZ_ZXN_SBZXQK");
            sendJsonObject.set("token", "93cd2c6567594107a16b51a65bcd5a37");
            sendJsonObject.set("updateNull", "true");
            sendJsonObject.set("param", sendList);
            System.out.println(sendJsonObject);

            String url = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/push";
            String back = HttpRequest.post(url)
                    .body(sendJsonObject.toString())
                    .timeout(20000)
                    .execute().body();

//            pushandreturnService.saveData(zlpz.getId(), String.valueOf(sendJsonObject), selectsysconfigone.getRemark(), back);

            com.alibaba.fastjson.JSONObject backJson = JSON.parseObject(back);
            int result = backJson.getIntValue("result");
            if (result == 0) {
                log.info(String.format("浙路品质设备在线推送成功！%s", shebeino));
            } else {
                log.info(String.format("浙路品质设备在线推送失败！%s", shebeino));
            }
        }

    }
}
