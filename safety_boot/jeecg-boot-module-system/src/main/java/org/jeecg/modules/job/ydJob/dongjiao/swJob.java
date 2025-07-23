package org.jeecg.modules.job.ydJob.dongjiao;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.swbhz.service.IBhzSwCailiaoService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName swJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/5/26 9:34
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class swJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzSwBasesService swBasesService;
    @Autowired
    private IBhzSwCailiaoService swCailiaoService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    private String url = "http://123.60.37.16:443/receive/hcdata/dtu";

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.DJ_SW);//水稳历史数据推送交投集团
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东水稳的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东水稳的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzSwBases> bhzSwBasesList = swBasesService.selectListToDJ(shebeilist, curid);
        if (null == bhzSwBasesList || bhzSwBasesList.size() == 0) {
            log.info(String.format("暂无义东水稳未推送数据" + DateUtils.now()));
            return;
        }

        int id = 0;
        DecimalFormat df = new DecimalFormat("#.00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (BhzSwBases bhzSwBases : bhzSwBasesList) {
            id = bhzSwBases.getId();
            JSONObject sendJSONObject = new JSONObject();
            String shebeibianhao = bhzSwBases.getShebeibianhao();
            String baocunshijian = bhzSwBases.getBaocunshijian();

            Date caijishijian = bhzSwBases.getCaijishijian();
            String format = sdf.format(caijishijian);

            String chuliaoshijian = bhzSwBases.getChuliaoshijian();
            Date date = sdf.parse(chuliaoshijian);
//            long timestamp = date.getTime();

            sendJSONObject.set("id", bhzSwBases.getId() ); //id
            sendJSONObject.set("mp_num", shebeibianhao ); //拌合站编号
            sendJSONObject.set("col_time", format ); //采集时间
            sendJSONObject.set("discharge_time", sdf.format(date) ); //出料时间
//            sendJSONObject.set("remark_1", timestamp ); //出料时间时间戳
            sendJSONObject.set("is_p", 1); // 理论配合比传送方式 0:无配合比;1:质量;2:配合比
            sendJSONObject.set("formula", bhzSwBases.getFormulaNo()); // 配方号
            sendJSONObject.set("baocunshijian", baocunshijian); // 保存时间
            sendJSONObject.set("remark_2", "0"); // 为空则按吨计算，否则按kg计算

            QueryWrapper<BhzSwCailiao> swCailiaoQueryWrapper = new QueryWrapper<>();
            swCailiaoQueryWrapper.eq("base_guid",bhzSwBases.getGuid());
            List<BhzSwCailiao> cailiaoList = swCailiaoService.list(swCailiaoQueryWrapper);
            for (BhzSwCailiao bhzSwCailiao : cailiaoList) {
                String cailiaoming = bhzSwCailiao.getCailiaoming();

                Double shijiyongliang = bhzSwCailiao.getShijiyongliang();
                Double shijipb = Double.parseDouble(bhzSwCailiao.getShijipb()) * 0.01;
                Double lilunpb = Double.parseDouble(bhzSwCailiao.getLilunpb()) * 0.01;

                String theoryNumber =  df.format(shijiyongliang / shijipb * lilunpb);
                if ("骨料1".equals(cailiaoming)){
                    sendJSONObject.set("stone_1", shijiyongliang); // 骨料 1 实际用量 double 否 单位：t
                    sendJSONObject.set("th_stone_1", theoryNumber); // 骨料 1 理论用量 double 否 单位：t
                }
                if ("骨料2".equals(cailiaoming)){
                    sendJSONObject.set("stone_2", shijiyongliang);
                    sendJSONObject.set("th_stone_2", theoryNumber);
                }
                if ("骨料3".equals(cailiaoming)){
                    sendJSONObject.set("stone_3", shijiyongliang);
                    sendJSONObject.set("th_stone_3", theoryNumber);
                }
                if ("骨料4".equals(cailiaoming)){
                    sendJSONObject.set("stone_4", shijiyongliang);
                    sendJSONObject.set("th_stone_4", theoryNumber);
                }
                if ("骨料5".equals(cailiaoming)){
                    sendJSONObject.set("stone_5", shijiyongliang);
                    sendJSONObject.set("th_stone_5", theoryNumber);
                }
                if ("骨料6".equals(cailiaoming)){
                    sendJSONObject.set("stone_6", shijiyongliang);
                    sendJSONObject.set("th_stone_6", theoryNumber);
                }
                if ("骨料7".equals(cailiaoming)){
                    sendJSONObject.set("stone_7", shijiyongliang);
                    sendJSONObject.set("th_stone_7", theoryNumber);
                }
                if ("粉料1".equals(cailiaoming)){
                    sendJSONObject.set("fl_1", shijiyongliang);
                    sendJSONObject.set("th_fl_1", theoryNumber);
                }
                if ("粉料2".equals(cailiaoming)){
                    sendJSONObject.set("fl_2", shijiyongliang);
                    sendJSONObject.set("th_fl_2", theoryNumber);
                }
                if ("粉料3".equals(cailiaoming)){
                    sendJSONObject.set("fl_3", shijiyongliang);
                    sendJSONObject.set("th_fl_3", theoryNumber);
                }
                if ("粉料3".equals(cailiaoming)){
                    sendJSONObject.set("s_total", shijiyongliang);
                    sendJSONObject.set("th_fl_3", theoryNumber);
                }
                if ("水".equals(cailiaoming)){
                    sendJSONObject.set("water", shijiyongliang);
                    sendJSONObject.set("th_water", theoryNumber);
                }
                if ("水泥".equals(cailiaoming)){
                    sendJSONObject.set("fl_1", shijiyongliang);
                    sendJSONObject.set("th_fl_1", theoryNumber);
                }
            }

            String back = HttpRequest.post(url)
                    .form("data", sendJSONObject)
                    .execute()
                    .body();

            pushandreturnService.saveData(id, String.valueOf(sendJSONObject), selectsysconfigone.getRemark(), back);
            if (back.contains("200")) {
                log.info(String.format("义东水稳站推送成功!" + id + "Json数据" + sendJSONObject));
            } else {
                log.info(String.format("义东水稳站推送失败!" + id + "Json数据" + sendJSONObject));
            }
            sysConfigService.updateSysConfig(JobUtil.DJ_SW, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
