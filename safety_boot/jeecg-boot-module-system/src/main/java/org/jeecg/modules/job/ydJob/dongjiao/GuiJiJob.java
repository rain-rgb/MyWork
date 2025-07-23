package org.jeecg.modules.job.ydJob.dongjiao;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.spreadandcrush_car.entity.SpreadandcrushCar;
import com.trtm.iot.spreadandcrush_car.service.ISpreadandcrushCarService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.generic.RET;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.util.CoordinateTransformUtil;
import org.jeecgframework.codegenerate.window.CodeWindow;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GuiJiJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/4/20 16:26
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class GuiJiJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private ISpreadandcrushCarService spreadandcrushCarService;

    /**
     * 将度分格式的经纬度转换为十进制度数
     * @param degree 分离度分格式的度部分
     * @param minute 分离度分格式的分部分
     * @return 十进制度数
     */
    public static double convertDMS2Decimal(double degree, double minute) {
        return degree + minute / 60.0;
    }

    /**
     * 将度分格式的经纬度转换为ddmm.mmmmmmmmmmm格式
     * @param degree 分离度分格式的度部分
     * @param minute 分离度分格式的分部分
     * @return 转换后的ddmm.mmmmmmmmmmm格式的经纬度
     */
    public static String convertDMS2DDMM(double degree, double minute) {
        // 计算度分部分对应的十进制度数
        double decimalDegree = convertDMS2Decimal(degree, minute);
        // 将十进制度数转换为ddmm.mmmmmmmmmmm格式
        int dd = (int) decimalDegree;                  // 取整数部分为度数部分
        double mm = (decimalDegree - dd) * 60.0;       // 将小数部分转换为分数部分
        String f;
        if (mm < 10) {
            f = String.format("%.11f", (float) mm);     // 使用 float 类型进行格式化
            f = "0" + f;
        } else {
            f = String.format("%.11f", (float) mm);     // 使用 float 类型进行格式化
        }
        String result = String.format("%02d%s", dd, f);
        return result;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.DJ_YSC);//运输车
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东运输车定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东运输车的设备" + DateUtils.now()));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 时间格式化器
        String shebeilist = jsonObject.getStr("shebeilist");
        List<SpreadandcrushCar> spreadandcrushCarList = spreadandcrushCarService.getList(shebeilist,curid);
        if (null == spreadandcrushCarList || spreadandcrushCarList.size() == 0) {
            log.info(String.format("暂无义东运输车未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (SpreadandcrushCar spreadandcrushCar : spreadandcrushCarList) {
            id = spreadandcrushCar.getId();
            JSONObject sendDate = new JSONObject();

            sendDate.set("XT",spreadandcrushCar.getTruckid());
            sendDate.set("V",spreadandcrushCar.getSpeed());//速度

            //经度
            String longitude = spreadandcrushCar.getLongitude();
            //纬度
            String latitude = spreadandcrushCar.getLatitude();

            int longitudeDegree = (int) Double.parseDouble(longitude);// 取整数部分为度数部分
            double longitudeMinute = (Double.parseDouble(longitude) - longitudeDegree) * 60.0;  // 将小数部分转换为分数部分
            String longitudeDDMM = convertDMS2DDMM(longitudeDegree, longitudeMinute);    // 转换为ddmm.mmmmmmmmmmm格式

            int latitudeDegree = (int) Double.parseDouble(latitude);                  // 取整数部分为度数部分
            double latitudeMinute = (Double.parseDouble(latitude) - latitudeDegree) * 60.0;    // 将小数部分转换为分数部分
            String latitudeDDMM = convertDMS2DDMM(latitudeDegree, latitudeMinute);   // 转换为ddmm.mmmmmmmmmmm格式

            sendDate.set("JD",longitudeDDMM);
            sendDate.set("WD",latitudeDDMM);
            sendDate.set("SJ",sdf.format(spreadandcrushCar.getLoctime()));
            System.out.println(sendDate);
            String body = HttpRequest.post("http://123.60.37.16:443/receive/vehicledata/routeDtu")
                    .form("data",sendDate)
                    .execute()
                    .body();

            if (body.contains("1")) {
                log.info(String.format("义东运输车推送成功!" + id));
                spreadandcrushCar.setIsdj(1);

            } else {
                log.info(String.format("义东运输车推送失败!" + id + "Json数据" + sendDate));
                spreadandcrushCar.setIsdj(2);
            }
            spreadandcrushCarService.saveOrUpdate(spreadandcrushCar);
            sysConfigService.updateSysConfig(JobUtil.DJ_YSC, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}
