package org.jeecg.modules.job.jobutil;

import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.byscfg.entity.BysCfg;
import com.trtm.iot.byscfg.service.IBysCfgService;
import com.trtm.iot.byssta.entity.BysSta;
import com.trtm.iot.byssta.service.IBysStaService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.zhydstatistics.entity.DeviceElectrcStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.aspectj.runtime.internal.Conversions.doubleValue;

@Component
public class BysUtil {

    @Autowired
    private IBysCfgService bysCfgService;

    @Autowired
    private IBysRealService bysRealService;
    @Autowired
    private IBysStaService bysStaService;

    private static BysUtil bysUtil;

    @PostConstruct
    public void init() {
        bysUtil = this;
    }

    /**
     * 判断标养室温湿度数据是否超标
     *
     * @param bysReal
     * @param
     * @param is_call
     * @return
     */
    public Map bysjudgment(BysReal bysReal, ShebeiInfo selectshebeione, Integer is_call) {

        Map map = new HashMap<>();

        int final_over_level = 0;
        StringBuilder final_content = new StringBuilder();
        double temperaturebz = 0.0;//温度预警值
        double humiditybz = 0.0;//湿度预警值
        String shebeino = selectshebeione.getSbjno();
        //获取标养室数据
        double tem = bysReal.getTemperature().doubleValue();
        double hum = bysReal.getHumidity().doubleValue();
        if (null != bysReal.getTemperature()) {
            tem = bysReal.getTemperature().doubleValue();
        }
        if (null != bysReal.getHumidity()) {
            hum = bysReal.getHumidity().doubleValue();
        }
        List<BysCfg> selectbyslist = bysCfgService.selectbyslist(shebeino);
        if (null != selectbyslist && selectbyslist.size() != 0) {
            //此循环是获取标养室预警配置信息
            for (BysCfg bysCfg1 : selectbyslist) {
                temperaturebz = bysCfg1.getTemperaturebz().doubleValue();
                humiditybz = bysCfg1.getHumiditybz().doubleValue();
            }
        } else {
            temperaturebz = 20;
            humiditybz = 95;
        }
        int temstatus = 0;
        int humstatus = 0;
        int status = 0;
        String massage = "";
        if (tem < temperaturebz - 2 || tem > temperaturebz + 2) {
            status = 2;
            temstatus = 2;
            massage = massage + "，" + "温度异常";
        } else {
            temstatus = 1;
        }
        if (hum < humiditybz) {
            status = 2;
            humstatus = 2;
            massage = massage + "，" + "湿度异常";
        } else {
            humstatus = 1;
        }
        if (status > final_over_level) {
            final_over_level = status;
        }
        Date datatime = bysReal.getDatatime();
        String datanyr = NumberUtil.Stringnyr(datatime);//格式化后的时间
        Date datanyr1 = NumberUtil.datanyr(datanyr);
        BysSta selectlimit = bysStaService.selectlimit(datanyr1, shebeino);
        if (selectlimit != null) {
            count(selectlimit, bysReal);
        } else {
            BysSta bysSta = new BysSta();
            bysSta.setShebeino(shebeino);
            bysSta.setStatisticsTime(datanyr1);
            bysSta.setChaobiaonum(0);
            bysSta.setHumchaobiao(0);
            bysSta.setTemchaobiao(0);
            bysSta.setTotal(0);
            bysStaService.save(bysSta);
            BysSta selectlimit1 = bysStaService.selectlimit(datanyr1, shebeino);
            count(selectlimit1, bysReal);
        }
        BysReal bysReal1 = new BysReal();
        bysReal1.setTemstatus(temstatus);
        bysReal1.setHumstatus(humstatus);
        bysReal1.setId(bysReal.getId());
        bysRealService.updateById(bysReal1);
        final_content.append(String.format("%1$s", massage));
        map.put("status", final_over_level);
        map.put("final_content", final_content.toString());
        return map;
    }

    private void count(BysSta selectlimit, BysReal bysReal) {
        Integer humchaobiao = selectlimit.getHumchaobiao();
        Integer temchaobiao = selectlimit.getTemchaobiao();
        Integer chaobiaonum = selectlimit.getChaobiaonum();
        Integer total = selectlimit.getTotal();
        if (bysReal.getHumstatus() == 2) {
            humchaobiao += 1;
        }
        if (bysReal.getTemstatus() == 2) {
            temchaobiao += 1;
        }
        if (bysReal.getStatus() == 2) {
            chaobiaonum += 1;
        }
        total += 1;
        BysSta bysSta = new BysSta();
        bysSta.setId(selectlimit.getId());
        bysSta.setTotal(total);
        bysSta.setTemchaobiao(temchaobiao);
        bysSta.setHumchaobiao(humchaobiao);
        bysSta.setChaobiaonum(chaobiaonum);
        bysStaService.updateById(bysSta);
    }
}
