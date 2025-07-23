package org.jeecg.modules.job.taicangProjectjob;

import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.devicemixpilehistorydata.entity.DeviceMixpileHistorydata;
import com.trtm.iot.devicemixpilehistorydata.service.IDeviceMixpileHistorydataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * \* @author: zml
 * \* Date: 2022/08/02
 * \* Time: 11:41
 * \* Description:
 * \
 */
@Component
@Slf4j
public class tcUtil {
    @Autowired
    private IDeviceMixpileHistorydataService deviceMixpileHistorydataService;

    private static tcUtil tcUtil;


    @PostConstruct
    public void init() {
        tcUtil = this;
    }


    public synchronized String getdata(DeviceMixpileHistorydataOne deviceMixpileHistorydataOne) throws ParseException {
        String shebeino = deviceMixpileHistorydataOne.getShebeino();
        String pileno = deviceMixpileHistorydataOne.getPileNo();
        String piletime = deviceMixpileHistorydataOne.getPileTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder datas = null;
        try {
            if (!StringUtils.isEmpty(piletime)) {
                Date end = dateFormat.parse(piletime);
                if (!StringUtils.isEmpty(deviceMixpileHistorydataOne.getPileDowntime()) && !StringUtils.isEmpty(deviceMixpileHistorydataOne.getPileUptime())) {
                    int time = Integer.parseInt(deviceMixpileHistorydataOne.getPileDowntime()) + Integer.parseInt(deviceMixpileHistorydataOne.getPileDowntime());
                    Long starttimes = end.getTime() - time * 1000;
                    String starttime = dateFormat.format(starttimes);
                    String datatime = dateFormat.format(deviceMixpileHistorydataOne.getDatatime());
                    List<DeviceMixpileHistorydata> deviceMixpileHistorydataList = deviceMixpileHistorydataService.selectlist(shebeino, pileno, datatime, starttime);
                    if (deviceMixpileHistorydataList.size() > 0) {
                        int i = 0;
                        for (DeviceMixpileHistorydata deviceMixpileHistorydata1 : deviceMixpileHistorydataList) {
                            String data = "";
                            String ids = "";
                            String ltd = deviceMixpileHistorydata1.getLtdfloat();
                            String lgd = deviceMixpileHistorydata1.getLgdfloat();
                            String status = null;
                            String datatimes = dateFormat.format(deviceMixpileHistorydata1.getDatatime());
                            if ("1".equals(deviceMixpileHistorydata1.getWorksta())) {
                                status = "DOWN";
                            } else if ("2".equals(deviceMixpileHistorydata1.getWorksta())) {
                                status = "UP";
                            }
                            if (deviceMixpileHistorydata1 == deviceMixpileHistorydataList.get(deviceMixpileHistorydataList.size() - 1)) {
                                ids = "999";
                            } else {
                                ids = String.format("%03d", i);
                            }
                            String flowmeter = "0";
                            String flowmeter1 = "0";
                            if (!StringUtils.isEmpty(deviceMixpileHistorydata1.getFlowmeter())) {
                                flowmeter = deviceMixpileHistorydata1.getFlowmeter();
                            }
                            if (deviceMixpileHistorydata1.getPiletype() == 0) {
                                data = "#{PKTID:" + ids + ";PRJ:" + deviceMixpileHistorydata1.getShebeino() + ";POS:"
                                        + deviceMixpileHistorydata1.getMileage() + "-" + deviceMixpileHistorydata1.getPileno().replace("-", "+")
                                        + ";JD:" + deviceMixpileHistorydata1.getLtdfloat() + ";WD:" + deviceMixpileHistorydata1.getLgdfloat()
                                        + ";DEG1:" + deviceMixpileHistorydata1.getX() + ";DEG2:" + deviceMixpileHistorydata1.getY() + ";DEP:"
                                        + deviceMixpileHistorydata1.getCurdep() + ";V:" + deviceMixpileHistorydata1.getSpeed() + ";INSTFLOW1:"
                                        + deviceMixpileHistorydata1.getFlowlst() + ";TFLOW1:" + deviceMixpileHistorydata1.getGrouting()
                                        + ";INSTFLOW2:0;TFLOW2:0;IINT:" + flowmeter + ";IEXT:" + flowmeter1 + ";SJ:"
                                        + datatimes + ";STATE:" + status + ";POS2:0}#\r\n";
                            } else if (deviceMixpileHistorydata1.getPiletype() == 2) {
                                DeviceMixpileHistorydata deviceMixpileHistorydata = deviceMixpileHistorydataService.selectone(shebeino, datatimes, ltd, lgd, deviceMixpileHistorydata1.getPileno());
                                if (null == deviceMixpileHistorydata) {
                                    continue;
                                } else {
                                    if (!StringUtils.isEmpty(deviceMixpileHistorydata.getFlowmeter())) {
                                        flowmeter1 = deviceMixpileHistorydata1.getFlowmeter();
                                    }
                                    data = "#{PKTID:" + ids + ";PRJ:" + deviceMixpileHistorydata1.getShebeino() + ";POS:"
                                            + deviceMixpileHistorydata1.getMileage() + "-" + deviceMixpileHistorydata1.getPileno().replace("-", "+")
                                            + ";JD:" + deviceMixpileHistorydata1.getLtdfloat() + ";WD:" + deviceMixpileHistorydata1.getLgdfloat()
                                            + ";DEG1:" + deviceMixpileHistorydata1.getX() + ";DEG2:" + deviceMixpileHistorydata1.getY() + ";DEP:"
                                            + deviceMixpileHistorydata1.getCurdep() + ";V:" + deviceMixpileHistorydata1.getSpeed() + ";INSTFLOW1:"
                                            + deviceMixpileHistorydata1.getFlowlst() + ";TFLOW1:" + deviceMixpileHistorydata1.getGrouting()
                                            + ";INSTFLOW2:" + deviceMixpileHistorydata.getFlowlst() + ";TFLOW2:" + deviceMixpileHistorydata.getGrouting()
                                            + ";IINT:" + flowmeter + ";IEXT:" + flowmeter1
                                            + ";SJ:" + datatimes + ";STATE:" + status + ";POS2:" + deviceMixpileHistorydata.getMileage() + "-"
                                            + deviceMixpileHistorydata.getPileno().replace("-", "+") + "}#\r\n";
                                }
                            }
                            if (null == datas) {
                                datas = new StringBuilder(data);
                            } else {
                                datas.append(data);
                            }
                            i++;
                        }
                    }else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
        return String.valueOf(datas);
    }
}
