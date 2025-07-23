package com.trtm.iot.openapigpsdatavo.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.openapigpsdatavo.entity.Openapigpsdatavo;
import com.trtm.iot.openapigpsdatavo.service.IOpenapigpsdatavoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Description: 摊铺碾压数据
 * @Author: jeecg-boot
 * @Date: 2023-04-21
 * @Version: V1.0
 */
@Api(tags = "摊铺碾压数据")
@RestController
@RequestMapping("/DataPushController")
@Slf4j
public class DataPushController extends JeecgController<Openapigpsdatavo, IOpenapigpsdatavoService> {
    private static String url = "http://123.60.37.16:443/receive/qcdata/dtu";
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;

    /**
     * 将度分格式的经纬度转换为ddmm.mmmmm格式
     * @param degree 分离度分格式的度部分
     * @param minute 分离度分格式的分部分
     * @return 转换后的ddmm.mmmmm格式的经纬度
     */
    public static String convertDMS2DDMM(double degree, double minute) {
        // 计算度分部分对应的十进制度数
        double decimalDegree = convertDMS2Decimal(degree, minute);
        // 将十进制度数转换为ddmm.mmmmm格式
        int dd = (int) decimalDegree;                  // 取整数部分为度数部分
        double mm = (decimalDegree - dd) * 60.0;       // 将小数部分转换为分数部分
        String result = String.format("%02d%.11f", dd, mm);
        return result;
    }

    /**
     * 将度分格式的经纬度转换为十进制度数
     * @param degree 分离度分格式的度部分
     * @param minute 分离度分格式的分部分
     * @return 十进制度数
     */
    public static double convertDMS2Decimal(double degree, double minute) {
        return degree + minute / 60.0;
    }

    @PostMapping(value = "/recvGpsData1")
    public Result<?> recvGpsData1(@Valid @RequestBody List<Openapigpsdatavo> list) {
        //业务操作
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 时间格式化器
        log.info("义东摊铺碾压转发开始!");
        try {
            for (Openapigpsdatavo openapigpsdatavo : list) {
                Double velocity = Double.parseDouble(openapigpsdatavo.getVelocity());
                openapigpsdatavo.setVelocity(String.valueOf(velocity*60));

                JSONObject sendDate = new JSONObject();

                String machineid = openapigpsdatavo.getMachineId();

                sendDate.set("XT",machineid);  //心跳(设备编号)
                String temperature = openapigpsdatavo.getTemperature();
                String[] split = temperature.split(";");
                String T = split[0];
                double v = Double.parseDouble(openapigpsdatavo.getVelocity());
                sendDate.set("T",T);  //温度
                sendDate.set("V",v);  //速度

                String longitude = openapigpsdatavo.getLon();
                String latitude = openapigpsdatavo.getLat();
                int longitudeDegree = (int) Double.parseDouble(longitude);// 取整数部分为度数部分
                double longitudeMinute = (Double.parseDouble(longitude) - longitudeDegree) * 60.0;  // 将小数部分转换为分数部分
                String longitudeDDMM = convertDMS2DDMM(longitudeDegree, longitudeMinute);    // 转换为ddmm.mmmmmmmmmmm格式

                int latitudeDegree = (int) Double.parseDouble(latitude);                  // 取整数部分为度数部分
                double latitudeMinute = (Double.parseDouble(latitude) - latitudeDegree) * 60.0;    // 将小数部分转换为分数部分
                String latitudeDDMM = convertDMS2DDMM(latitudeDegree, latitudeMinute);   // 转换为ddmm.mmmmmmmmmmm格式

                sendDate.set("JD",longitudeDDMM);  //经度
                sendDate.set("WD",latitudeDDMM);  //纬度
                String roadstation = openapigpsdatavo.getRoadStation();

                //取整
                int station = (int)Double.parseDouble(roadstation);
                String s = String.valueOf(station);
                int len = s.length();

                if (len > 3) {
                    // 截取后三位并进行强制类型转换
                    String endtring = s.substring(len - 3);

                    // 获取前面的字符串
                    String firstPart = s.substring(0, len - 3);

                    roadstation = "K"+firstPart+"+"+endtring;
                }else if (len == 3) {
                    roadstation = "K0"+"+"+station;
                }else {
                    roadstation = "K0+000";
                }
                sendDate.set("PILE",roadstation);  //桩号

                String gpstime = sdf.format(openapigpsdatavo.getGpsTime());

                int newOffset = 0;
                double offset = Double.parseDouble(openapigpsdatavo.getOffset());
                if (offset > 0) {
                    newOffset = 1;
                }

                QueryWrapper<BhzLqBases> bhzLqBasesQueryWrapper = new QueryWrapper<>();
                bhzLqBasesQueryWrapper.eq("shebeibianhao", "ydlm1blq_01")
                        .isNotNull("chuliaoshijian")
                        .orderByAsc("ABS(TIMESTAMPDIFF(SECOND, chuliaoshijian, '"+ gpstime+"'))")
                        .last("LIMIT 1");
                BhzLqBases one = bhzLqBasesService.getOne(bhzLqBasesQueryWrapper);
                String formulaNo = one.getFormulaNo();
                String itemText = "";
                if ("36".equals(formulaNo)){
                    itemText = "SMA-16";
                }else {
                    itemText = "SMA-13";
                }
                sendDate.set("LEFTRIGHT", newOffset);  //左右幅
                sendDate.set("surfaceCourse", itemText);  //上面层
                sendDate.set("SJ", gpstime);  //时间

                String body = HttpRequest.post(url)
                        .form("data",sendDate)
                        .execute()
                        .body();

                if (body.contains("success")) {
                    log.info("义东摊铺碾压转发成功!"  +"body: "+ body);
                } else {
                    log.info("义东摊铺碾压转发失败!"  + "Json数据" + sendDate);
                }
            }
        } catch (Exception e) {
            return Result.error("接收异常！");
        }

        return Result.OK();
    }
}
